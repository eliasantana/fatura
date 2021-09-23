package br.com.faturaweb.fatura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.faturaweb.fatura.model.FormaDePagamento;
@Repository
public interface FormaDePagamentoRepository extends CrudRepository<FormaDePagamento, Long> {

	@Query("Select fp from FormaDePagamento fp")
    List<FormaDePagamento> findAllFormasDePagamento();
	
	
	@Query("Select fp from FormaDePagamento fp where fp.descricao =:descricao ")
   Optional<FormaDePagamento> findByDescricaoFormaDePagamento(String descricao);
}
