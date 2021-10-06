package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.apache.lucene.analysis.ca.CatalanAnalyzer;
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
				+ "where date_format(dt_cadastro,'%m') = (date_format(now(),'%m' )-1)  "
				+ "group by  ds_lancamento", nativeQuery = true)
		List<Lancamento> findAllLancamentosDoMes();
		
}
