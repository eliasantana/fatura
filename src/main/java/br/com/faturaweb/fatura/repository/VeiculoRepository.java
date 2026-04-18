package br.com.faturaweb.fatura.repository;

import br.com.faturaweb.fatura.model.Veiculo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends CrudRepository<Veiculo, Long> {
    @Query(value = "select * from veiculo",nativeQuery = true)
    ArrayList<Veiculo> veiculosAdicionados();
@Query(value = "select * from veiculo where cd_veiculo=:idveiculo",nativeQuery = true)
    Optional<Veiculo> localizarVeiculo(Long idveiculo);
}
