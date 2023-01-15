package br.com.faturaweb.fatura.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.Menssageria;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.MensageriaRepository;
import br.com.faturaweb.fatura.services.AppServices;
import br.com.faturaweb.fatura.services.MensageriaServices;

@Controller
public class AgendamentoController {

	@Autowired
	ConfiguracoesRepository  configuracoesRepository;
	@Autowired
	LancamentoRepository lancamentoRepository;
	@Autowired
	MensageriaRepository mensageriaRepository;
	@Autowired
	MensageriaServices mensageriaservices;
	@Autowired
	AppServices appservices;
	
	private final long SEGUNDO =1000L;
	private final long MINUTO = SEGUNDO*60;
	private final long HORA = MINUTO*60;
	
	/**
	 * Notifica o usuário sobre  despesas vencidas a cada 2 horas
	 * @author elias
	 * @since 05/06/2022
	 * @param fixeRate = 600000 - Milisegundos (1 Minuto) 7200000 (2h)
	 * */
	@Scheduled( fixedRate = HORA*2)
	private void notificacaoDeContasVencidas() {
		System.out.println("Início da Execução: " + LocalDateTime.now());
		Configuracoes config = configuracoesRepository.findConfiguracao();
		Integer nrDias = config.getNrDias();
		String snNotificar = config.getSnNotificar();
		List<Lancamento> lancamentosVencidos = lancamentoRepository.findVencidos(nrDias);
		
		if (lancamentosVencidos.size() > 0 && snNotificar.equals("S")) {
			//Só enviará mensagens se aida não tiver atingido o limite diário configurado
			if ( mensageriaservices.enviarMensagem(config)) {
				//Preparando para adicionar a mensagem
				StringBuilder sbw = new StringBuilder();
				sbw.append("Atenção!\n");
				sbw.append("As despesas abaixo estão vencidas à pelo menos " + configuracoesRepository.findConfiguracao().getNrDias() + " dias! \n");
				sbw.append("SysfaturaWeb - Sistema  de Controle Financeiro Pessoal");
				sbw.append("\n\n\n");
				sbw.append("\n\n\nData e Hoda do Envio: : " + LocalDateTime.now());
				sbw.append("---------------------------------------------------------------------------");
				
				for (Lancamento lancamento : lancamentosVencidos) {
					sbw.append("\nDescrição: " + lancamento.getDsLancamento()+ "\n");
					sbw.append("\nVencimento: " + lancamento.getDtCompetencia() + "\n");
					sbw.append("\nValor: " + lancamento.getVlPago());					
					sbw.append("\n\n\n");
					sbw.append("---------------------------------------------------------------------");
					
				}			
				try {
					appservices.sendEmai(config.getEmailOrigem(), config.getNmOrigem() ,config.getEmailDestino(), config.getNmDestino(), config.getTituloMsgEmailDestino(), sbw);
					
					Menssageria menssageria = new Menssageria();
					menssageria.setDestino(config.getEmailDestino());
					menssageria.setDtEnvio(LocalDateTime.now());
					menssageria.setStatus("S");
					mensageriaRepository.save(menssageria);
					System.out.println("E-mail enviado com sucesso!");
				
				} catch (UnsupportedEncodingException e) {
					System.out.println("Erro ao tentar enviar e-mail -> sendEmail ");
					e.printStackTrace();
					
					Menssageria menssageria = new Menssageria();
					menssageria.setDestino(config.getEmailDestino());
					menssageria.setDtEnvio(LocalDateTime.now());
					menssageria.setStatus("E");
					mensageriaRepository.save(menssageria);
				}
				
				System.out.println("Processo de envio de e-mail finalizado!");
			}else {
				System.out.println("Quantidade de mensgem diária atingida");
			}
			
		}
	}
}
