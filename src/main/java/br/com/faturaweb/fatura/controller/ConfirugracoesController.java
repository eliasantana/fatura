package br.com.faturaweb.fatura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;

@Controller
@RequestMapping("/configuracoes")
public class ConfirugracoesController {

@Autowired	
private ConfiguracoesRepository configuracoesRepository;

@GetMapping("/listar")
public String configuracoes(Model model) {
	 Configuracoes config = configuracoesRepository.findConfiguracao();
	model.addAttribute("config",config);
	
	return "home/configuracoes";
}

@PostMapping("salvar")
public RedirectView salvar(Model model, Configuracoes formConfiguracoes) {
 
  System.out.println(formConfiguracoes.getSnParcelado());
   RedirectView redirectView = new RedirectView("http://localhost:8080/configuracoes/listar");
	return	 redirectView;
}
}
 