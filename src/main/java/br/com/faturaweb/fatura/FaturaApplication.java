package br.com.faturaweb.fatura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan
public class FaturaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaturaApplication.class, args);
	}

}
