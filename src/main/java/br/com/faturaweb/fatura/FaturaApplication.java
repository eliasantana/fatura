package br.com.faturaweb.fatura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;

@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration
@EnableEmailTools
public class FaturaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaturaApplication.class, args);
	}

}
