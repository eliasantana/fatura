package br.com.faturaweb.fatura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.faturaweb.fatura.model.TipoLancamento;

@Repository
public interface TipoLancamentoRepository extends CrudRepository<TipoLancamento, Long>{

	@Query("select t from TipoLancamento t")
	List<TipoLancamento>findAllTipoLancamentos();
	
	@Query("select t from TipoLancamento t where t.cdTipoLancamento=:id")
	TipoLancamento findTipoLancamentoId(Long id);
   
	Optional<TipoLancamento> findBydsTipoLancamento(String dsTipoLancamento);
	
	Optional<TipoLancamento> findBycdTipoLancamento(Long cdTipoLancamento);
}
