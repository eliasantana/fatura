package br.com.faturaweb.fatura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.Menssageria;
import br.com.faturaweb.fatura.repository.MensageriaRepository;

@Service
public class MensageriaServices {
	@Autowired
	MensageriaRepository mensageriaRepository;
	
	/**
	 * Retorna a quantidade de mensagens enviadas no dia
	 * @since 22-01-2022
	 * @author elias
	 * */
	public  Boolean enviaMensagem() {
		Boolean resp = false;
			List<Menssageria> msgEnviadas = mensageriaRepository.findMensagensDia();
			if (msgEnviadas.size() < 2) {
				resp = true;
			}		
		return resp;
	}
}
