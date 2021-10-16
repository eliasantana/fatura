package br.com.faturaweb.fatura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.repository.LancamentoRepository;

@ComponentScan
@RequestMapping("/")
public class TesteController {
	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@GetMapping("/listar")
	public String listar(Model model) {
		
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentosDoMes();
		System.out.println("listando");
		model.addAttribute("lancamentos", lancamentos);

		return "login";
	}

}