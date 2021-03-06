package br.com.faturaweb.fatura.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.UsuarioRepository;

@Controller
public class LoginController {

@Autowired	
UsuarioRepository UsuarioRepository;	
	
	@GetMapping("/login")
	public String login(Model model) {
		Map<String, Integer>data = new LinkedHashMap<String, Integer>();
		data.put("Janeiro", 200);
		data.put("Fevereiro", 300);
		data.put("Março", 150);
		data.put("Abril", 400);
		data.put("Maio", 800);
		data.put("Junho", 950);
		
		model.addAttribute("keyset",data.keySet());
		model.addAttribute("values",data.values());
		
		return "login";
	}

	@GetMapping("/logout")
	public String logout() {
		
		return "login";
	}


}
