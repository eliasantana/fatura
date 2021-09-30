package br.com.faturaweb.fatura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.Configuracoes;

public interface ConfiguracoesRepository  extends CrudRepository<Configuracoes, Long>{

	@Query("SELECT c FROM Configuracoes c")
	List<Configuracoes> findConfiguracao();
	
}
