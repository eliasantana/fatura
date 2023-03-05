package br.com.faturaweb.fatura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.services.ExtratoLancamentoServices;

@Controller
@RequestMapping("/extrato")

public class ExtratoLancamentoController {
	
	@Autowired
	ExtratoLancamentoServices services;
	@GetMapping("/financeiro")
	public String extratoLancamento(@RequestParam  String mesAno, 
																@RequestParam (name = "anolancamento", required = false) String anolancamento  , Model  model) {
		services.getExtratoLancamento(mesAno, anolancamento, model);
		return "extrato_lancamento";
	}
	
	@GetMapping("/getimagem")
	@ResponseBody
	public byte[] getlogo() {
		Configuracoes config  = services.getLogo();		
		return config.getLogo();
	}
}
