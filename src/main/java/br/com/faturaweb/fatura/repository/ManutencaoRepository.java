package br.com.faturaweb.fatura.repository;

import br.com.faturaweb.fatura.model.Manutencao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManutencaoRepository extends CrudRepository<Manutencao, Long> {
    @Query(value = "select * from manutencao",nativeQuery = true)
    List<Manutencao> listarManutencao();
    @Query(value = "select * from manutencao where cd_manutencao=:idmanutencao",nativeQuery = true)
    Manutencao localizarManutencao(Long idmanutencao);
}
