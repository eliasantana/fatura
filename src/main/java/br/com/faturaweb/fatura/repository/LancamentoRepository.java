package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.Lancamento;
import io.lettuce.core.dynamic.annotation.Param;

public interface LancamentoRepository extends CrudRepository<Lancamento, Long> {

		@Query(value = "select * from fatura.lancamento where date_format(dt_competencia,'%Y') = date_format(curdate(),'%Y') order by dt_competencia",nativeQuery = true)
		List<Lancamento> findAllLancamentos();	
		
		@Query("SELECT l from Lancamento l WHERE l.cdLancamento = :cdLancamento")
		Lancamento findByIdLancamento(Long cdLancamento );
		
		//retorna os lancamentos do mês corrente
		@Query(value = "SELECT cd_lancamento, ds_lancamento, dt_cadastro, dt_competencia, sn_pago, vl_pago, forma_de_pagamento_cd_forma_pgamento, tipo_lancamento_cd_tipo_lancamento, usuario_cd_usuario"
				+ " FROM fatura.lancamento "
				+ "where date_format(dt_competencia,'%m') = (date_format(CURDATE(),'%m' ))  "
				+ "group by  ds_lancamento", nativeQuery = true)
		List<Lancamento> findAllLancamentosDoMes();
	
		/**
		 * Retorna todos os lancantos do ano corrente
		 * @author elias
		 * @since 04/12/2021
		 * @return {@link List}
		 * */
		@Query(value = "SELECT * FROM fatura.lancamento "
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
		@Query(value="select * from fatura.lancamento where cd_lancamento = ("
										+ "select max(cd_lancamento) cd_lancamento from fatura.lancamento where usuario_cd_usuario = :cdUsuario) ",nativeQuery = true)
		Lancamento findUltimoLancamentoUsuario(Long cdUsuario);
		
		/**
		 * Retorna os pagametos vencidos filtrados a partir do número de dias de atrazo.
		 * @author elias
		 * @since 21/12/2021
		 * @param nrDias
		 * @return {@link List} - Lista dos lançamentos vencidos
		 * */
		@Query(value="select * FROM FATURA.LANCAMENTO L WHERE timestampdiff(DAY, CURDATE(),L.DT_COMPETENCIA) <=:dias  AND SN_PAGO = 'NÃO'",nativeQuery = true)
		List<Lancamento> findVencidos0(@Param(value = "dias") Integer dias);
}
