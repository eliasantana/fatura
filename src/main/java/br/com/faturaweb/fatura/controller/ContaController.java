package br.com.faturaweb.fatura.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;
import br.com.faturaweb.fatura.services.AppServices;

@Controller
@RequestMapping("/conta")
public class ContaController {
@Autowired
ContaRepository repository;
@Autowired
AppServices services;

@Autowired
AppServices appservices;	
@Autowired
LogMovimentacaoFinanceiraRepository  logMovimentacaoRepository;

	@GetMapping("listar")
	public String conta(Model model) {
		List<Conta> contas = repository.findcontas();
		Conta conta = new Conta();
		model.addAttribute("conta",conta);
		model.addAttribute("contas",contas);
		return "conta";	
	}
	
	@PostMapping("salvar")
	public RedirectView salvar(Model model, Conta conta , @RequestParam("file") MultipartFile file) throws IOException {
		RedirectView rw = new RedirectView("/conta/listar");
		conta.setQrcod(file.getBytes());
		repository.save(conta);
		return rw;
	}
	
	@GetMapping("/qrcode/{id}")
	@ResponseBody
	public byte[] getImagem(@PathVariable Long id) {	
		Optional<Conta> conta = repository.findById(id);
		
			return conta.get().getQrcod();		
		
	}
	
	@GetMapping("alterar/{id}")
	public String alterar(@PathVariable Long id, Model model) {
		try {
			Optional<Conta> conta = repository.findById(id);
			List<Conta> contas = repository.findcontas();
			model.addAttribute("conta",conta.get());
			model.addAttribute("contas",contas);
		} catch (Exception e) {
			System.out.println("Conta não localizada!");
		}
		
		return "conta";	
	}
	
	@GetMapping("/excluir/{id}")
	public RedirectView  excluirConta(@PathVariable Long id, Model model) {
		RedirectView rw = new RedirectView("/conta/listar");
		//Optional<Conta> conta = repository.findById(Id);
		Conta conta = repository.findContaId(id);
		repository.delete(conta);
		
		System.out.println(id.toString());
		return rw;
	}
	
	@PostMapping("/creditar/{id}")
	public void creditar( @PathVariable Long id, Model model, Conta conta ) {
		  Conta contaLocalizada = repository.findContaId(id);
		 
	}
	
	@PostMapping("/movimentacao")
	public RedirectView movimentacao(Model model, 
			@RequestParam("valor") String valor, 
			@RequestParam("conta") String conta, 
			@RequestParam("operacao") String operacao,
			@RequestParam("motivo") String motivo) {
		
		RedirectView rw = new RedirectView("/conta/listar");
		Optional<Conta> contaLocalizada = repository.findConta(conta);
		LogMovimentacaoFinanceira lmf = new LogMovimentacaoFinanceira();
		valor = valor.replaceAll(",",".");
		if (valor!=null) {
			BigDecimal saldo = contaLocalizada.get().getSaldo();
			Double vlr = Double.valueOf(valor);
			BigDecimal vlr2 = BigDecimal.valueOf(vlr);
			
			//D = Débito  C="Crédito"
			if (operacao.equals("D")) {
				saldo = saldo.subtract(vlr2);
				lmf.setDescricao(motivo.toUpperCase());
				lmf.setDtMovimentacao(LocalDate.now());
				lmf.setNrConta(contaLocalizada.get().getNrConta());
				lmf.setTpMovimentacao(operacao);
				lmf.setUsuario("Elias");
				lmf.setVlMovimentado(vlr2);
				
			}
			else {
				saldo = saldo.add(vlr2);
				lmf.setDescricao(motivo);
				lmf.setDtMovimentacao(LocalDate.now());
				lmf.setNrConta(contaLocalizada.get().getNrConta());
				lmf.setTpMovimentacao(operacao);
				lmf.setUsuario("Elias");
				lmf.setVlMovimentado(vlr2);
			}
			
			Conta  novosaldo = contaLocalizada.get();
			novosaldo.setSaldo(saldo);
			repository.save(novosaldo);
			logMovimentacaoRepository.save(lmf);
		}
		return rw;
	}
	
}
