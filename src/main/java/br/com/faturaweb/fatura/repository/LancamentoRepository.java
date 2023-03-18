package br.com.faturaweb.fatura.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.NamedNativeQuery;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.faturaweb.fatura.model.AnoLancamento;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.projection.AnoLancamentoProjection;
import io.lettuce.core.dynamic.annotation.Param;

public interface LancamentoRepository extends CrudRepository<Lancamento, Long> {
		@Modifying
		@Query(value = "select * from lancamento where date_format(dt_competencia,'%Y') = date_format(curdate(),'%Y') order by dt_competencia",nativeQuery = true)
		List<Lancamento> findAllLancamentos();	
		
		@Query("SELECT l from Lancamento l WHERE l.cdLancamento = :cdLancamento")
		Lancamento findByIdLancamento(Long cdLancamento );
		
		/**
		 * @author elias
		 * @since 04/12/2021
		 * Retorna os lancamentos do mês/ano corrente
		 * Obs: Atualmente utilizado para fornecer dados para o gráfico de totalização de Despesas exibido na DashBoard 
		 * */
		@Modifying
		@Query(value = "SELECT *"
				+ " FROM lancamento "
				+ " where date_format(dt_competencia,'%m%Y') = (date_format(CURDATE(),'%m%Y' ))  ", nativeQuery = true)
		List<Lancamento> findAllLancamentosDoMes();
		
		/**
		 * @author elias
		 * @since 04/12/2021
		 * Retorna os lancamentos do mês informado
		 * Obs: Atualmente utilizado para fornecer dados para o gráfico de totalização de Despesas exibido na DashBoard 
		 * */
		@Modifying
//		@Query(value = "SELECT *"
//				+ " FROM lancamento "
//				+ "where date_format(dt_competencia,'%m%Y') =:mesano "
//				+ "group by  ds_lancamento", nativeQuery = true)
		@Query(value = "SELECT *"
				+ " FROM lancamento "
				+ "where date_format(dt_competencia,'%m%Y') =:mesano "
				, nativeQuery = true)
		List<Lancamento> findAllLancamentosDoMes(String mesano);
		
		/**
		 * @author elias
		 * @since 04/12/2021
		 * Retorna os lancamentos do mês informado
		 * Obs: Atualmente utilizado para fornecer dados para o gráfico de totalização de Despesas exibido na DashBoard 
		 * */
		@Modifying
//		@Query(value = "SELECT *"
//				+ " FROM lancamento "
//				+ "where date_format(dt_competencia,'%m%Y') =:mesano "
//				+ "group by  ds_lancamento", nativeQuery = true)
		@Query(value = "SELECT *"
				+ " FROM lancamento "
				+ "where date_format(dt_competencia,'%Y') =:ano "
				, nativeQuery = true)
		List<Lancamento> findAllLancamentosDoAno(String ano);
	
		/**
		 * Retorna todos os lancantos do ano corrente
		 * @author elias
		 * @since 04/12/2021
		 * @return {@link List}
		 * */
		@Modifying
		@Query(value = "SELECT * FROM lancamento "
										+ "where date_format(dt_cadastro,'%Y') = date_format(curdate(),'%Y') order by sn_pago"
											,nativeQuery = true)
		List<Lancamento>findLancamentosDoAno();
		
		/**
		 * Retorna o último lancamento realizado pelo usuário
		 * @author elias
		 * @since04-12-2021
		 * @param nrDias
		 * @return {@link Lancamento}
		 * */
		
		@Query(value="select * from lancamento where cd_lancamento = ("
										+ "select max(cd_lancamento) cd_lancamento from lancamento where usuario_cd_usuario = :cdUsuario) ",nativeQuery = true)
		Lancamento findUltimoLancamentoUsuario(Long cdUsuario);
		
		/**
		 * Retorna os pagametos vencidos filtrados a partir do número de dias de atrazo.
		 * @author elias
		 * @since 21/12/2021
		 * @param nrDias
		 * @return {@link List} - Lista dos lançamentos vencidos
		 * */
		@Modifying
		@Query(value="select * FROM lancamento L WHERE timestampdiff(DAY, CURDATE(),L.DT_COMPETENCIA) <=:dias  AND SN_PAGO = 'NÃO'",nativeQuery = true)
		List<Lancamento> findVencidos(@Param(value = "dias") Integer dias);
		
		/**
		 * Retorna os lançamentos do ano corrente para o mês informado
		 * @author elias
		 * @since 04/12/2021
		 * @return {@link List}
		 * */
		@Modifying
		@Query(value = "SELECT * FROM lancamento "
										+ "where date_format(dt_competencia,'%m%Y') =:mesano   order by tipo_lancamento_cd_tipo_lancamento"
											,nativeQuery = true)
		List<Lancamento> findLancamentosFuturos(String mesano);
		@Modifying
		@Query(value = "SELECT * FROM lancamento "
										+ "where date_format(dt_competencia,'%Y') =:ano   order by tipo_lancamento_cd_tipo_lancamento"
											,nativeQuery = true)
		List<Lancamento> findLancamentosAnual(String ano);
		
		/**
		 * Retorna os lancçamentos do Mês seguinte
		 * @author elias
		 * @since 27/04/2022
		 * @return {@link List} - Lista de Lançamentos do Mês seguinte.
		 * */
		@Modifying
		@Query(value="SELECT * FROM lancamento l "
									+ "WHERE date_format(l.dt_competencia ,'%m%Y') = date_format((curdate() + interval 1 month),'%m%Y');",nativeQuery = true)
		List<Lancamento> findLancamentoMesSeguinte();

		/**
		 * Retornas os demias lançamentos 
		 * */
		@Query(value = "select * from lancamento where ds_lancamento like %:pdslancamento% and vl_pago = :pvalorpago",nativeQuery = true)
		List<Lancamento> findDemiasLancamento(String pdslancamento, BigDecimal pvalorpago);
	
		/*
		 * Retorna os anos que possuem lancamanentos cadastrados
		 * */
		@Query(value = " SELECT distinct date_format(dt_competencia,'%Y') ano FROM lancamento; ",nativeQuery = true)
		List<AnoLancamentoProjection> findAnosDeLancamento();
		
		/**
		 * Retorna os lançamentos da competência por tipo de pagamennto
		 * @author elias
		 * @since 05-01-2022
		 * @param cdformapagamento - Código da forma de pagamento a ser localizada	
		 * */
		@Modifying
		@Query(value = "SELECT *"
				+ " FROM lancamento "
				+ " where date_format(dt_competencia,'%m%Y') = (date_format(CURDATE(),'%m%Y' ))  and forma_de_pagamento_cd_forma_pgamento =:cdformapagamento ", nativeQuery = true)
		List<Lancamento> findLancamentoPorFormaDePagamento(Long cdformapagamento);
}
