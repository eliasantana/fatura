package br.com.faturaweb.fatura.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.model.LogProvisao;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;
import br.com.faturaweb.fatura.repository.LogProvisaoRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;

@Service
public class LogProvisaoServices {
	@Autowired
	LogMovimentacaoFinanceiraRepository logMovimentacaofinanceiraRepository;
	@Autowired
	ContaRepository contaRepository;
	@Autowired
	LogProvisaoRepository logProvisaoRepository;
	@Autowired
	LoteRepository loteRepository;
	@Autowired
	AppServices appService;
	/**
	 * Muda o status dos valores provisionados e credita os valores a provisionar em suas respectivas contas configuradas
	 * @since 20/11/2022
	 * @author elias
	 * */
	public void creditarTodasAsProvisoes() {
		List<LogProvisao> provisoesDaCompetencia = logProvisaoRepository.findAllLogProvisaoDaCompetencia();
		List<LogProvisao>listaDeProvisoes = new ArrayList<LogProvisao>();
		if (!loteRepository.findLoteCompetencia().getStatus().equals("F")) {
			if (provisoesDaCompetencia.size() >0) {
				for (LogProvisao logProvisao : provisoesDaCompetencia) {
					logProvisao.setSnCreditado("S");
					listaDeProvisoes.add(logProvisao);
				}
		}
		logProvisaoRepository.saveAll(listaDeProvisoes);
		//Creditando os valores provisionados no lote
		for (LogProvisao logProvisao : provisoesDaCompetencia) {
			Optional<Conta> findConta = contaRepository.findConta(logProvisao.getNrConta());
			if(findConta.isPresent()) {
				Conta conta = findConta.get();
				Conta novoSaldo = appService.credita(conta,logProvisao.getVlProvisionado());
				contaRepository.save(novoSaldo);
				
				//Registrando movimentação financeira
				LogMovimentacaoFinanceira lmf = new LogMovimentacaoFinanceira();
				lmf.setDescricao("Créditando " + logProvisao.getVlProvisionado() + " -> " + novoSaldo.getNrConta() + "( "+ novoSaldo.getDsConta()+")");
				lmf.setDtMovimentacao(LocalDate.now());
				lmf.setNrConta(novoSaldo.getNrConta());
				lmf.setTpMovimentacao("C");
				lmf.setUsuario("Elias");
				lmf.setVlMovimentado(logProvisao.getVlProvisionado());
				
				logMovimentacaofinanceiraRepository.save(lmf);
			}
		}
			
		}
	}
}
