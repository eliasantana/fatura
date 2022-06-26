package br.com.faturaweb.fatura;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@org.springframework.context.annotation.Configuration
public class Configuration {
	 @Bean
	    public JavaMailSender javaMailSender() {
	        return new JavaMailSenderImpl();
	    }
}
