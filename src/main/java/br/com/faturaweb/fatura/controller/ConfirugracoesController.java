package br.com.faturaweb.fatura.controller;

import java.util.List;

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
@RequestMapping("configuracoes")
public class ConfirugracoesController {

@Autowired	
private ConfiguracoesRepository configuracoesRepository;

@GetMapping("listar")
public String configuracoes(Model model) {
	List<Configuracoes> findConfiguracao = configuracoesRepository.findConfiguracao();
	model.addAttribute("config",findConfiguracao.get(0));
	
	return "configuracoes/configuracoes";
}

@PostMapping("salvar")
public RedirectView salvar(Model model, Configuracoes configuracoesForm) {
	List<Configuracoes> findConfiguracao = configuracoesRepository.findConfiguracao();
		
	Configuracoes c = new Configuracoes();
	c.setCdConfiguracao(findConfiguracao.get(0).getCdConfiguracao());
	System.out.println(c.toString());
	if (configuracoesForm.getSnParcelado().equals("on")) {
		c.setSnParcelado("S");
	}else {
		c.setSnParcelado("N");
	}

	
	System.out.println(configuracoesForm.getSnParcelado());
	System.out.println(c.toString());
	configuracoesRepository.save(c);
	model.addAttribute("config",c);
	RedirectView redirectView = new RedirectView("http://localhost:8080/configuracoes/listar");
	return	 redirectView;
}

}
