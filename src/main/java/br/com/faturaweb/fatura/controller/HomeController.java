package br.com.faturaweb.fatura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.faturaweb.fatura.repository.LancamentoRepository;

@Controller
public class HomeController {
	@Autowired
	LancamentoRepository LancamentoRepository;
	
	@GetMapping("/")
	public String index(Model model) {
		return "home/dashboard";
	}
	
}
