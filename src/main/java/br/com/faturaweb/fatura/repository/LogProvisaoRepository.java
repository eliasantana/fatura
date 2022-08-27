package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.LogProvisao;

public interface LogProvisaoRepository extends CrudRepository<LogProvisao, Long> {

	@Query(value = "select *   from log_provisao  where competencia =  date_format(curdate(),'%m%Y')", nativeQuery = true)
	List<LogProvisao> findAllLogProvisaoDaCompetencia();
	
}
