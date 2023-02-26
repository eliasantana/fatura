package br.com.faturaweb.fatura.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;
import br.com.faturaweb.fatura.services.AppServices;
import br.com.faturaweb.fatura.services.ContaServices;

@Controller
@RequestMapping("/conta")
public class ContaController {
	
	@Autowired
	ContaServices contaServices;
	

	@GetMapping("listar")
	public String conta(Model model) {
		contaServices.listar(model);

		return "conta";
	}

	@PostMapping("salvar")
	public RedirectView salvar(Model model, Conta conta, @RequestParam("file") MultipartFile file) throws IOException {
		RedirectView rw = contaServices.salvar(model, conta, file);

		return rw;
	}

	@GetMapping("/qrcode/{id}")
	@ResponseBody
	public byte[] getImagem(@PathVariable Long id) {
		Optional<Conta> conta = contaServices.getImagem(id);

		return conta.get().getQrcod();
	}

	@GetMapping("alterar/{id}")
	public String alterar(@PathVariable Long id, Model model) {
		contaServices.alterar(id, model);

		return "conta";
	}

	@GetMapping("/excluir/{id}")
	public RedirectView excluirConta(@PathVariable Long id, Model model) {
		RedirectView rw = contaServices.excluir(id, model);


		return rw;
	}

	@PostMapping("/creditar/{id}")
	public void creditar(@PathVariable Long id, Model model, Conta conta) {
		Conta contaLocalizada  = contaServices.creditar(id, model, conta);
	}

	@PostMapping("/movimentacao")
	public RedirectView movimentacao(Model model, @RequestParam("valor") String valor,
			@RequestParam("conta") String conta, @RequestParam("operacao") String operacao,
			@RequestParam("motivo") String motivo) {
		RedirectView rw = contaServices.movimentacao(model, valor, conta, operacao, motivo);

		return rw;
	}

	@PostMapping("/transferir")
	public String transferir(Model model, @RequestParam(name = "ctaorigem") String ctaorigem,
			@RequestParam(name = "ctadestino") String ctadestino, @RequestParam(name = "vlr") String vlr,
			@RequestParam(name = "motivo") String motivo

	) {
		contaServices.trasferir(ctaorigem, ctadestino, vlr, motivo, model);

		return "conta";
	}

}
