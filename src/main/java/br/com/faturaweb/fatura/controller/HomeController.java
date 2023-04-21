package br.com.faturaweb.fatura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.faturaweb.fatura.model.Chave;
import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.services.HomeSercices;

@ComponentScan
@Controller

public class HomeController {
	@Autowired
	HomeSercices homeservices;

	@GetMapping("/")
	public String index(Model model) {
		String valorChave;
		valorChave = homeservices.index(model);
		String tpDashBoard = homeservices.getTpDashBoard(Chave.TP_DASHBOARD.toString());
		if ("S".equals(valorChave)) {
			return "redirect:/lancamento/cadastro";
		} else {			
			if (tpDashBoard.equals("2")) {
				return "home/dashboard2";
			}else {				
				return  "home/dashboard";
			}
		}
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		homeservices.dashBoard(model);
		return "home/dashboard";
	}

	@GetMapping(value = "/listar")
	public String listar(Model model) {
		homeservices.listar(model);

		return "home/listar-lancamento";
	}

	@GetMapping("configuracoes")
	public String configuracoes(Model model) {
		homeservices.getConfiguracoes(model);
		return "configuracoes";
	}

	@GetMapping("email")
	public String email() {
		homeservices.emailTeste();
		return "home/dashboard";
	}

	@GetMapping("/getimagem")
	@ResponseBody
	public byte[] getlogo() {
		Configuracoes config = homeservices.getConfiguracoes();
		return config.getLogo();
	}

	@GetMapping("/agenda")
	public String agenda() {
		return "agenda";
	}

}
