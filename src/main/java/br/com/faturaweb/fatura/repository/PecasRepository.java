package br.com.faturaweb.fatura.repository;

import br.com.faturaweb.fatura.model.Pecas;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PecasRepository extends CrudRepository<Pecas, Long> {
    @Query(value = "select * from pecas",nativeQuery = true)
    List<Pecas> pecasLocalizadas();

    @Query(value = "select * from pecas where cd_pecas=:idpeca",nativeQuery = true)
    Optional<Pecas> localizarPecas(Long idpeca);

    @Query(value = "select p.* " +
            " from pecas p, manutencao m " +
            " where p.km_troca = m.km  " +
            " and m.km =:km " +
            " and not exists ( select 1 from it_manutencao it " +
            "                          where it.cd_peca = p.cd_pecas );",nativeQuery = true)
    List<Pecas> pecasLocalizadas(String km);
}
