package br.com.faturaweb.fatura.services;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.repository.ContaRepository;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import net.bytebuddy.asm.Advice.Return;

@Service
public class AppServices {
	@Autowired
	public EmailService emailService;
	@Autowired
	ContaRepository contaRepository;
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
	
	/**
	 * Credita o valor na conta informada
	 * @author elias
	 * @since 24/12/2021
	 * @param c - Conta
	 * @param valor - Valor a ser Creditado
	 *  @return {@link Conta}  - Conta atualiza
	 * */
	public Conta credita(Conta c, BigDecimal valor) {
			BigDecimal saldo = c.getSaldo();
			if (!(valor.compareTo(BigDecimal.ZERO) < 0)) { // Valor maior que zero
				if (!(valor.compareTo(saldo)==1)) { // Saldo maior que o valor informado		
					System.out.println("Creditando valor");
					c.setSaldo(saldo.add(valor));			
				}else {
					System.out.println(" Saldo insuficiente -> "  + c.getSaldo());
				}
			}
		    return c;	
	}
	/**
	 * Debita o valor na conta informada
	 * @author elias
	 * @since 24/12/2021
	 * @param c - Conta
	 * @param valor - Valor a ser Creditado
	 * @return {@link Conta}  - Conta atualizada
	 * */
	public Conta debita(Conta c, BigDecimal valor) {
			BigDecimal saldo = c.getSaldo();			
			if (valor.compareTo(BigDecimal.ZERO) < 0) { // Valor Negativo
					System.out.println("Valor Inálido!  O Valor negativo não é válido!");
				}else if (valor.compareTo(saldo) == 1) {
						System.out.println("Saldo Insuficienete ->" + saldo);
			}else {
						c.setSaldo(saldo.subtract(valor));
					}	
		    return c;	
	}

}
