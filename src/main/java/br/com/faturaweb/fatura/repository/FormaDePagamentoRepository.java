package br.com.faturaweb.fatura.repository;

import br.com.faturaweb.fatura.model.FormaDePagamento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FormaDePagamentoRepository extends CrudRepository<FormaDePagamento, Long> {

	//@Query("Select fp from FormaDePagamento fp")
    @Query(value = " SELECT fp.* "                                           +
                   " FROM forma_pagto fp "                                   +
                   " JOIN chave_config cc   ON cc.chave = 'FIXA_LANCAMENTO'" +
                   " WHERE cc.valor = 'T' " +
                   "  OR fp.descricao = CASE ifnull(cc.valor,'DE') "         +
                   "                    WHEN 'C'  THEN 'Crédito'   "         +
                   "                    WHEN 'D'  THEN 'Dinheiro'  "         +
                   "                    WHEN 'DE' THEN 'Débito'    "         +
                   " END;", nativeQuery = true)
    List<FormaDePagamento> findAllFormasDePagamento();
	
	
	@Query("Select fp from FormaDePagamento fp where fp.descricao =:descricao ")
   Optional<FormaDePagamento> findByDescricaoFormaDePagamento(String descricao);
}
