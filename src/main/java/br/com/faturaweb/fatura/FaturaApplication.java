package br.com.faturaweb.fatura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;

@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration
@EnableEmailTools
@EnableScheduling
@EnableCaching
public class FaturaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaturaApplication.class, args);
	}

}
