package br.com.faturaweb.fatura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.faturaweb.fatura.repository.LancamentoRepository;

@Controller
public class HomeController {
	@Autowired
	LancamentoRepository LancamentoRepository;
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("valores","'1200','2','3','4','5','6'");
		return "home/dashboard";
	}
}
