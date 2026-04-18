package br.com.faturaweb.fatura.repository;

import br.com.faturaweb.fatura.model.ItManutencao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItManutencaoRepository  extends CrudRepository<ItManutencao, Long> {

    @Query(value = "select * from it_manutencao", nativeQuery = true)
    List<ItManutencao> getItensManutencao();
    @Query(value = "select * from it_manutencao where cd_manutencao=:cdmanutencao", nativeQuery = true)
    List<ItManutencao> getItensManutencao(Long cdmanutencao);
    @Query(value = "select * from it_manutencao where cd_manutencao=:idmanutencao and cd_peca=:idpeca", nativeQuery = true)
    Optional<ItManutencao> localiarItem(Long idmanutencao, Long idpeca);
    @Transactional
    @Modifying
    @Query(value = "CALL PRC_ADICIONA_TODOS(:kmtroca,:idmanutencao)", nativeQuery = true)
    void adicionarTodos(@Param(value = "kmtroca") Long idmanutencao,
                        @Param(value = "idmanutencao") Long kmtroca);
}
