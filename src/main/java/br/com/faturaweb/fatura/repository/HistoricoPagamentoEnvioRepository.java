package br.com.faturaweb.fatura.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.faturaweb.fatura.model.HistoricoPagamentoEnvio;

@Repository
public interface HistoricoPagamentoEnvioRepository extends CrudRepository<HistoricoPagamentoEnvio, Long>{

}
