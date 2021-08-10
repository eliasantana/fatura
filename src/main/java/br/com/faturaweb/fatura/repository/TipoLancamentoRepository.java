package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.faturaweb.fatura.model.TipoLancamento;

@Repository
public interface TipoLancamentoRepository extends CrudRepository<TipoLancamento, Long>{

	@Query("select t from TipoLancamento t")
	List<TipoLancamento>findAllTipoLancamentos();
}
