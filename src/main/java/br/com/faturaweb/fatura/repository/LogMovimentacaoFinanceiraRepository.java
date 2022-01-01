package br.com.faturaweb.fatura.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;

public interface LogMovimentacaoFinanceiraRepository  extends CrudRepository<LogMovimentacaoFinanceira, Long>{

}
