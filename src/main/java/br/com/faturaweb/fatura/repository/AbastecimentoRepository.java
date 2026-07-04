package br.com.faturaweb.fatura.repository;

import br.com.faturaweb.fatura.model.Abastecimento;
import br.com.faturaweb.fatura.projection.ViewConsumo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbastecimentoRepository extends CrudRepository<Abastecimento, Long> {

    @Query(value = "select a.* " +
            "   from abastecimento a, veiculo v " +
            "   where a.cd_veiculo = v.cd_veiculo and a.km_atual = (select min(km_atual) from abastecimento " +
            "                                                           where cd_veiculo = v.cd_veiculo) " +
            "   and v.cd_veiculo=:cdveiculo",nativeQuery = true)
    Optional<Abastecimento> kmMinimo(long cdveiculo);
    @Query(value = "select a.* " +
            "   from abastecimento a, veiculo v " +
            "   where a.cd_veiculo = v.cd_veiculo and a.km_atual = (select max(km_atual) from abastecimento " +
            "                                                           where cd_veiculo = v.cd_veiculo) " +
            "   and v.cd_veiculo=:cdveiculo",nativeQuery = true)
    Optional<Abastecimento> kmMaximo(long cdveiculo);

    @Query(value = "select a.*  from abastecimento a, veiculo v " +
            " where a.cd_veiculo = v.cd_veiculo " +
            " and a.km_atual = (select max(km_atual) from abastecimento   where cd_veiculo = v.cd_veiculo) " +
            " and v.cd_veiculo=:cdveiculo and date_format(dt_abastecimento,'%Y%m' )=date_format(curdate(),'%Y%m' )",nativeQuery = true)
    Optional<Abastecimento> kmMaximoMes(long cdveiculo);
    @Query(value = "select a.*  from abastecimento a, veiculo v " +
            " where a.cd_veiculo = v.cd_veiculo " +
            " and a.km_atual = (select min(km_atual) from abastecimento   where cd_veiculo = v.cd_veiculo " +
            " and date_format(dt_abastecimento,'%Y%m' )=date_format(curdate(),'%Y%m' )) " +
            " and v.cd_veiculo=:cdveiculo ",nativeQuery = true)
    Optional<Abastecimento> kmMinimoMes(long cdveiculo);


    @Query(value = "select * from abastecimento where cd_veiculo =:cdveiculo", nativeQuery = true)
    List<Abastecimento> todosOsAbastecimentos( Long cdveiculo);
    @Query(value = "select * from abastecimento where cd_veiculo =:cdveiculo and date_format(dt_abastecimento,'%Y%m')=date_format(curdate(),'%Y%m')", nativeQuery = true)
    List<Abastecimento> todosOsAbastecimentosNoMes(Long cdveiculo);
    @Query(value = " select * from  vw_consumo where cd_veiculo =:cdveiculo ", nativeQuery = true)
    List<ViewConsumo> evolcaoConsumo(Long cdveiculo);
}
