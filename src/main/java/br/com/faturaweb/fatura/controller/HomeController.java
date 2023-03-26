package br.com.faturaweb.fatura.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.form.LancamentoForm;
import br.com.faturaweb.fatura.model.Cartao;
import br.com.faturaweb.fatura.model.Chave;
import br.com.faturaweb.fatura.model.ChaveConfig;
import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.FormaDePagamento;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.Lote;
import br.com.faturaweb.fatura.model.Menssageria;
import br.com.faturaweb.fatura.model.Receita;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.repository.CartaoRepository;
import br.com.faturaweb.fatura.repository.ChaveRepository;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;
import br.com.faturaweb.fatura.repository.MensageriaRepository;
import br.com.faturaweb.fatura.repository.ReceitaRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.repository.UsuarioRepository;
import br.com.faturaweb.fatura.services.AppServices;
import br.com.faturaweb.fatura.services.HomeSercices;
import br.com.faturaweb.fatura.services.LancamentoServices;
import br.com.faturaweb.fatura.services.MensageriaServices;
import br.com.faturaweb.fatura.services.MetaService;
import br.com.faturaweb.fatura.services.ReceitaServices;

@ComponentScan
@Controller

public class HomeController {
	@Autowired
	HomeSercices homeservices;

	@GetMapping("/")
	public String index(Model model) {
		String valorChave;
		valorChave = homeservices.index(model);
		if ("S".equals(valorChave)) {
			return "redirect:/lancamento/cadastro";
		} else {
			return "home/dashboard";
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
