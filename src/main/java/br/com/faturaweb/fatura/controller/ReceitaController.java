package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.model.Receita;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;
import br.com.faturaweb.fatura.repository.ReceitaRepository;
import br.com.faturaweb.fatura.services.AppServices;
import br.com.faturaweb.fatura.services.ReceitaServices;

@Controller
@RequestMapping("/receita")
public class ReceitaController {

	@Autowired
	ReceitaRepository receitaRepository;
	@Autowired
	ContaRepository contaRepository;
	@Autowired
	LogMovimentacaoFinanceiraRepository logRepository;
	@Autowired
	ReceitaServices services;
	@Autowired
	ConfiguracoesRepository configuracoesRepository;
	@Autowired
	AppServices appServices;
	@Autowired
	LoteRepository loteRepository;

	@GetMapping("/listar")
	public String listar(Model model) {
		services.listar(model);
		return "home/receita-listar";
	}

	@GetMapping("/cadastro")
	public String cadastrar(Model model) {
		services.cadastro(model);		
		return "home/receita";
	}

	@PostMapping("/salvar")
	public String salvar(Model model, Receita receitaForm) {
		services.salvar(model, receitaForm);
		return "home/receita";
	}

	@GetMapping("excluir/{id}")
	public RedirectView excluir(@PathVariable Long id, Model model) {
		services.excluir(id, model);
		RedirectView rw = new RedirectView();
		rw.setUrl("/receita/listar");
		return rw;
	}

	@GetMapping("alterar/{id}")
	public String alterar(@PathVariable Long id, Model model) {
		services.alterar(id, model);
		return "home/receita";
	}

	@GetMapping("/clonar/{id}")
	public RedirectView clonaReceita(@PathVariable Long id) {
		services.clonar(id);
		RedirectView rw = new RedirectView("/receita/listar");
		return rw;
	}
}
