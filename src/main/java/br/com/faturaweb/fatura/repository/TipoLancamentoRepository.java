package br.com.faturaweb.fatura.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.faturaweb.fatura.model.TipoLancamento;

@Repository
public interface TipoLancamentoRepository extends CrudRepository<TipoLancamento, Long>{

	@Override
	default Iterable<TipoLancamento> findAll() {
		
		return null;
	}
		
	
}
