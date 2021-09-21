package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.apache.lucene.analysis.ca.CatalanAnalyzer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.Lancamento;

public interface LancamentoRepository extends CrudRepository<Lancamento, Long> {

		@Query("SELECT l from Lancamento l")
		List<Lancamento> findAllLancamentos();
		
		@Query("SELECT l from Lancamento l WHERE l.cdLancamento = :cdLancamento")
		Lancamento findByIdLancamento(Long cdLancamento );
}
