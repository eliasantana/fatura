package br.com.faturaweb.fatura.model;

import java.io.UnsupportedEncodingException;
import java.util.List;

import br.com.faturaweb.fatura.services.AppServices;

public class EnviaEmailThread extends Thread {
	
	public void run (List<Lancamento> lancamentosVencidos ) {
	AppServices appservices = new AppServices();
		
		System.out.println("Preparando dados para o evio do e-mail");
		StringBuilder sbw = new StringBuilder();
		if (lancamentosVencidos.size() > 0) {
			System.out.println("Dados localizados com sucesso!");
			 sbw.append("Atenção!\n");
			   for (Lancamento lancamento : lancamentosVencidos) {
				   sbw.append("As depesas abaixo estão vencidas a pelo menos  5  dias! \n");
				   sbw.append("\nDescrição: " + lancamento.getDsLancamento()+ "\n");
				   sbw.append("\nVencimento: " + lancamento.getDtCompetencia() + "\n");
				   sbw.append("\nValor: " + lancamento.getVlPago());
			}			
			   try {
				appservices.sendEmai("eliasantana@gmail.com","Elias Santana" , "eliasantana@hotmail.com", "Elias Santana da Silva", "Contas Vencidas!", sbw);
				System.out.println("E-mail enviado com sucesso!");
				
			} catch (UnsupportedEncodingException e) {
				System.out.println("Erro ao tentar enviar e-mail -> sendEmail ");
				e.printStackTrace();			
			} 
		}
	}
}
