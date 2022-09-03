package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.LogProvisao;

public interface LogProvisaoRepository extends CrudRepository<LogProvisao, Long> {

	@Query(value = "select *   from log_provisao  where competencia =  date_format(curdate(),'%m%Y')", nativeQuery = true)
	List<LogProvisao> findAllLogProvisaoDaCompetencia();
	
	/**
	 * Retorna os itens provisionados da competencia informada
	 * */
	@Query(value =  "select *   from log_provisao  where competencia  = :p_competencia", nativeQuery = true)
	List<LogProvisao>findLogProvisaoDaCompetencia(String p_competencia );
}
