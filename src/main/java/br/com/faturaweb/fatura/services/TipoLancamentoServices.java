package br.com.faturaweb.fatura.services;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.TipoLancamento;

@Service
public interface TipoLancamentoServices {

	@Query("select * t from TipoLancamento t")
	List<TipoLancamento>findAllTipoLancamentos();
	
}
