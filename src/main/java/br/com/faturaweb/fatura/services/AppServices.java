package br.com.faturaweb.fatura.services;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;

@Service
public class AppServices {
	@Autowired
	public EmailService emailService;
	
	/**
	 * Envia e-mail de texto simples
	 * @author elias
	 * @since 20-12-2021
	 * @param origem - E-mail de Origem
	 * @param origemNome - Nome do Remetente
	 * @param destino - E-mail de Destino
	 * @param destinoNome - Nome do Destinatário
	 * @param tiulo = Titulo do E-mail
	 * @param corpoDamensagem - Mensagem do E-mail
	 * */
	public void sendEmai(String origem, String origemNome,  String destino, String destinoNome, String tiulo, StringBuilder corpoDamensagem) throws UnsupportedEncodingException {
		
		final Email email = DefaultEmail.builder()
				 .from(new InternetAddress(origem,origemNome))
				 .to(Lists.newArrayList(new InternetAddress(destino,destinoNome)))
				 .subject(tiulo)
				 .body(corpoDamensagem.toString())
				 .encoding("UTF-8").build();
		 
		 		emailService.send(email);
	}

}
