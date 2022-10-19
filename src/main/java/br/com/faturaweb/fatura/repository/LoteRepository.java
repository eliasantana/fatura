package br.com.faturaweb.fatura.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.Lote;
import io.lettuce.core.dynamic.annotation.Param;

public interface LoteRepository extends CrudRepository<Lote, Long> {

	/**
	 * Retorna todos os Lotes
	 * */
	@Query(value = "SELECT * FROM lote",nativeQuery = true)
	List<Lote>findAllLote();
	/**
	 * Retorna o lote pelo ID
	 * */
	@Query(value = "SELECT * FROM lote where cd_lote=:id", nativeQuery = true)
	List<Lote>findLoteId(Long id);
	
	/**
	 * Retora lote da competencia atual
	 * */
	@Query(value = "SELECT * FROM lote WHERE DATE_FORMAT(COMPETENCIA,'%m%Y') = DATE_FORMAT(curdate(),'%m%Y') ",nativeQuery = true)
	 Lote findLoteCompetencia();
	
	/**
	 * Retora lote da competencia informada
	 * */
	@Query(value = "SELECT * FROM lote where date_format(competencia,'%m%Y') = :pcompetencia ",nativeQuery = true)
	 Lote findLoteDtCompetencia(String pcompetencia);
	
	/**
	 * Chamando um procedimento de banco via notação de query informando um parâmetro de entrada
	 * 
	 * */
	@Query(value="CALL PRC_PROCESSA_FECHAMENTO_CONTABIL(:mesano)", nativeQuery = true)
	List<Lancamento> prcProcessaFechamentoContabil(@Param(value="mesano") String mesano);
	@Transactional
	@Modifying
	@Query(value = "CALL PRC_REPROCESSALOTE", nativeQuery = true)
	void reprocessar();
}

