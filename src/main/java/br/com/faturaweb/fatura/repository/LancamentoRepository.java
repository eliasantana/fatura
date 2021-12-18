package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.Lancamento;

public interface LancamentoRepository extends CrudRepository<Lancamento, Long> {

		@Query("SELECT l from Lancamento l")
		List<Lancamento> findAllLancamentos();	
		
		@Query("SELECT l from Lancamento l WHERE l.cdLancamento = :cdLancamento")
		Lancamento findByIdLancamento(Long cdLancamento );
		
		//retorna os lancamentos do mês corrente
		@Query(value = "SELECT cd_lancamento, ds_lancamento, dt_cadastro, dt_competencia, sn_pago, vl_pago, forma_de_pagamento_cd_forma_pgamento, tipo_lancamento_cd_tipo_lancamento, usuario_cd_usuario"
				+ " FROM fatura.lancamento "
				+ "where date_format(dt_cadastro,'%m') = (date_format(now(),'%m' ))  "
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
		 * */
		@Query(value="select * from fatura.lancamento where cd_lancamento = ("
										+ "select max(cd_lancamento) cd_lancamento from fatura.lancamento where usuario_cd_usuario = :cdUsuario) ",nativeQuery = true)
		Lancamento findUltimoLancamentoUsuario(Long cdUsuario);
}
