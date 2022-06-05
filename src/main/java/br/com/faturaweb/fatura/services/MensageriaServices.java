package br.com.faturaweb.fatura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Menssageria;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.MensageriaRepository;

@Service
public class MensageriaServices {
	@Autowired
	MensageriaRepository mensageriaRepository;
	@Autowired
	ConfiguracoesRepository configRepository;
	
	/**
	 * Verifica se a quantidade de mensagem diária enviada é menor que a quantidade configurada.
	 * @since 22-01-2022
	 * @author elias
	 * */
	public  Boolean enviaMensagem() {
		Boolean resp = false;
		Configuracoes configuracao = configRepository.findConfiguracao();
		List<Menssageria> msgEnviadas = mensageriaRepository.findMensagensDia();
			if (msgEnviadas.size() < configuracao.getNrMsgDiaria() ) {
				resp = true;		}		
		return resp;
	}
}
