package br.com.faturaweb.fatura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
@ComponentScan
@Controller

public class HomeController {
	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@GetMapping("/")
	public String index(Model model) {
		return "home/dashboard";
	}
	
	@GetMapping("/listar")
	public String listar(Model model) {
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentosDoMes();
		System.out.println("listando");
		model.addAttribute("lancamentos", lancamentos);

		return "home/listar-lancamento";
	}

	
}
