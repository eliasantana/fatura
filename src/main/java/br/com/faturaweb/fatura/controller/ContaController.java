package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.services.AppServices;

@Controller
@RequestMapping("conta")
public class ContaController {
@Autowired
ContaRepository repository;
@Autowired
AppServices appservices;	
	@GetMapping("/listar")
	public String conta() {
		
		Conta conta1 = new  Conta("1007042-1","Bradesco",BigDecimal.ZERO);
		System.out.println("Conta Informada: " + conta1.toString());
		Optional<Conta> contaLocalizada = repository.findConta(conta1.getNrConta());
		/*
		System.out.println("A conta informada não Existe, vou adicionar");
		if (!contaLocalizada.isPresent()) {
			repository.save(conta1);			
			System.out.println("A conta informada foi adicionada");
		}else {
			repository.save(contaLocalizada.get());
		}
		*/
		//Credita valor 
		if (contaLocalizada.isPresent()) {
			Conta conta = appservices.credita(contaLocalizada.get(), new BigDecimal(1));
			System.out.println("Novo Saldo> " + conta.getSaldo());
			repository.save(conta);
			
		}
		
		/*
		//Debita um valor na conta informada
		System.out.println("Debitando 1000");
		System.out.println("Saldo atual -> " + contaLocalizada.get().getSaldo());
		if (contaLocalizada.isPresent()) {
			Conta debita = appservices.debita(contaLocalizada.get(), new BigDecimal(9.00));
			System.out.println(" Novo Saldo ->"+debita.getSaldo());
			System.out.println("Valor debitado com sucesso!");
			repository.save(debita);
		}*/
		return "teste";	
	}
	
}
