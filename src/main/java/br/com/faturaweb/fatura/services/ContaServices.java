package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;

@Service
public class ContaServices {
	
@Autowired
	ContaRepository repository;
@Autowired
ContaServices contaServices;

@Autowired
	LogMovimentacaoFinanceiraRepository logMovimentacao;

	/**
	 * Valida a transação entre as contas de Origem e Destino
	 * @author elias
	 * @param ctaorigem - Conta  de Origem
	 * @param ctadestino - Conta de Destino
	 * @return {@link Boolean} 
	 * */
	public String  validaTransacao(String ctaorigem, String ctadestino, String vlr) {
		Optional<Conta> contaDeOrigem= repository.findConta(ctaorigem);
		Optional<Conta> contaDeDestino= repository.findConta(ctadestino);
		BigDecimal bValor = new BigDecimal(vlr);
	    String msg = null;
		//Verificando se as contas existem
		 if (contaDeOrigem.isPresent() && contaDeDestino.isPresent()) {
			//Verificando se as contas são iguais 
				if (ctaorigem.equals(ctadestino)) {
					msg="A conta de origem e destino não podem ser iguais";
				}else if ((bValor.compareTo(BigDecimal.ZERO)==0) || (bValor.compareTo(BigDecimal.ZERO)==-1 )) {
					 msg=" Valor " + bValor + " Não é válido!";
				}else if (bValor.compareTo(BigDecimal.ZERO)==0) {
					msg=" Valor " + bValor + " Não é válido!";
				}else if (contaDeOrigem.get().getSaldo().compareTo(bValor)>-1) {
					msg="sucesso";
				}else {
					msg="Saldo  Insuficiente!";
				}
		}else {
			msg="Conta inválida!";
		}
		 return msg;
	}
	
	/**
	 * Realiza a tranzação entre contas
	 * @author elias
	 * @param ctaOrigem - Conta  de Origem
	 * @param ctaDestino - Conta de Destino
	 * @param vlr - Valor a ser transferido
	 * */
	public void transfere(ArrayList<Conta> contas, String ctaOrigem, String ctaDestino, String vlr) {
		Optional<Conta> contaDeOrigem= repository.findConta(ctaOrigem);
		Optional<Conta> contaDeDestino= repository.findConta(ctaDestino);
		BigDecimal bValor = new BigDecimal(vlr);
		//Obtendo o saldo da conta de origem e debitando o valor
		BigDecimal saldoOrigem = contaDeOrigem.get().getSaldo();
		System.out.println(" Saldo da Conta de origem " + saldoOrigem);
		
		saldoOrigem = saldoOrigem.subtract(bValor);
		contaDeOrigem.get().setSaldo(saldoOrigem);
		
		System.out.println("Novo saldo " + contaDeOrigem.get().getSaldo());
		
		//Obtendo o saldo da conta de Destino  e creditando o valor tranferido
		BigDecimal saldoDestino = contaDeDestino.get().getSaldo();
		System.out.println("Saldo da conta de Destino  " + saldoDestino);
		
		saldoDestino = saldoDestino.add(bValor);
		contaDeDestino.get().setSaldo(saldoDestino);
		System.out.println("Novo Saldo da conta de Destino  " + contaDeDestino.get().getSaldo());
		//Adicionando as contas e salvando o novo saldo
		contas.add(contaDeOrigem.get());
		contas.add(contaDeDestino.get());
		repository.saveAll(contas);
		
		insereLog(" Transferência de  R$ " + vlr + "  |  "
							+ contaDeOrigem.get().getNrConta() 
							+ " - " +contaDeOrigem.get().getDsConta()  
							+ " para " + contaDeDestino.get().getNrConta() 
							+ " - " + contaDeDestino.get().getDsConta()
										, ctaOrigem, ctaDestino, "T", bValor);
	}
	
	/**
	 * Gera um loga da movimentação Financeira
	 * @author elias
	 * @param descricao - Descrição da movimentação 
	 * @param nrContaOrigem - Conta de origem do recurso
	 * @param nrContaDestino - Conta de destino 
	 * @param tpMovimentacao - Tipo de movimentação T - Tranferência D - Débito C - Crédito 
	 * @param vlMovimentado - Valor transferido
	 * */
	public void insereLog(String descricao, String nrContaOrigem, String nrContaDestino, String tpMovimentacao, BigDecimal vlMovimentado) {
		
		LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();
		log.setDescricao(descricao);
		log.setDtMovimentacao(LocalDate.now());
		log.setNrConta(nrContaOrigem);
		log.setTpMovimentacao(tpMovimentacao);
		log.setUsuario("Elias");
		log.setVlMovimentado(vlMovimentado);
		
		logMovimentacao.save(log);
		
	}
	
}
