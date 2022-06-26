package br.com.faturaweb.fatura.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;

public interface LogMovimentacaoFinanceiraRepository  extends CrudRepository<LogMovimentacaoFinanceira, Long>{

	/**
	 * Retorna todos os registros  de log
	 * @author elias
	 * @since 25-02-2021	 * 
	 * */
	@Query(value="SELECT * FROM log_movimetacao_financeira",nativeQuery = true )
	List<LogMovimentacaoFinanceira> findAllLogs();
	/**
	 * Retorna os logs do mes informado
	 * @param dt_movimentacao - Data da Movimentaação
	 * @since 25/02/2022
	 * @author elias 
	 * */
	@Query(value = "SELECT * FROM log_movimetacao_financeira "
								 + " where date_format(dt_movimentacao,'%m')=:mes", nativeQuery = true)
	List<LogMovimentacaoFinanceira> findAllLogsPorData(Integer  mes);
	
}
