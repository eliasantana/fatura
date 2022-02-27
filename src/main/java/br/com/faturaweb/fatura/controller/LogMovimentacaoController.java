package br.com.faturaweb.fatura.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;

@Controller
@RequestMapping("log")
public class LogMovimentacaoController {

	@Autowired
	LogMovimentacaoFinanceiraRepository repository;
	
	@GetMapping("/logs/{mes}")
	public String log(@PathVariable Integer mes, Model model) {
		List<LogMovimentacaoFinanceira> todosOsLogs = new ArrayList<LogMovimentacaoFinanceira>();
		System.out.println("mes: "+mes);
		todosOsLogs = repository.findAllLogsPorData(mes);		
		model.addAttribute("logs",todosOsLogs);
		model.addAttribute("usuario","Elias");
		
		
		return "logs";
		
	}
	

}
