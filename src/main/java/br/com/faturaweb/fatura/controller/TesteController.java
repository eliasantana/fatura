package br.com.faturaweb.fatura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.repository.LancamentoRepository;

@ComponentScan
@Controller
public class TesteController {
	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@GetMapping("/teste")
	public String listar(Model model) {
		
		
		return "/listar";
	}

}
