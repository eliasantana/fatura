package br.com.faturaweb.fatura;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@org.springframework.context.annotation.Configuration
public class Configuration {
	 @Bean
	    public JavaMailSender javaMailSender() {
	        return new JavaMailSenderImpl();
	    }
	 @Bean
	 public Connection connection (DataSource dataSource) throws SQLException {
		 return dataSource.getConnection();
	 }
}
