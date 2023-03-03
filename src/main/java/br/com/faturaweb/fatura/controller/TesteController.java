package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.ChaveRepository;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.LogProvisaoRepository;
import br.com.faturaweb.fatura.repository.TesteRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.services.AppServices;
import br.com.faturaweb.fatura.services.LancamentoServices;
import br.com.faturaweb.fatura.services.ReportService;

@Controller

public class TesteController {

		@Autowired
		LancamentoRepository r;
		@Autowired
		TesteRepository testeRepository;
		@Autowired
		LancamentoServices lctoServices;
		@Autowired
		TipoLancamentoRepository TipoLancamentoRepository;
	
		@Autowired
		ReportService reportServices;
		
		@Autowired
		TipoLancamentoRepository tipoLancamentoRepository;
		@Autowired
		LancamentoRepository lancamentoRepository;
		@Autowired
		LogProvisaoRepository logprovRepository;
		@Autowired
		ConfiguracoesRepository config;
		@Autowired
		AppServices appServices;
		@Autowired
		LancamentoServices lancamentoServices;
		@Autowired
		ChaveRepository chaveRepository;
		
	@GetMapping("/teste")
	public String apiltipolancnamento(Model model){
		
		String data = "2023-01-13";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			System.out.println(df.parse(data));
			Date date = df.parse(data);
			
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			System.out.println(format.parse(date.toString()));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return "teste";
	}
	
	@GetMapping("/sem-ajax")
	public String semAjax(Model model) {
		List<TipoLancamento> tipos = tipoLancamentoRepository.findAllTipoLancamentos();
		model.addAttribute("tipos",tipos);		
	return "teste";	
	}
	
	@GetMapping("/com-ajax")
	public String comAjax(Model model) {
		List<TipoLancamento> tipos = tipoLancamentoRepository.findAllTipoLancamentos();
		model.addAttribute("tipos",tipos);
		
		
	return"detalhe";	
	}
	
	@GetMapping("/compare")
	public String compare() {
		
		BigDecimal b1 = new BigDecimal(100);  // =0 <> -1    a > b = 1   (a = b) = 0   a <> b = -1
		BigDecimal saque = new BigDecimal(-110);
		
		System.out.println("Resultado "+b1.compareTo(saque));
		System.out.println("Comparando com zero "+b1.compareTo(BigDecimal.ZERO));
		
		System.out.println("Origem :" + b1);
		System.out.println("Destino:  " + saque);
		
		if (saque.compareTo(BigDecimal.ZERO)==1) {
			if (b1.compareTo(saque)>-1) {
				System.out.println("Debida");
			}else {
				System.err.println("Saldo insuficiente");
			}
		}else {
			System.err.println("Valor inválido");
		}
		
			
		
		return "teste";
	}
	
	
}

