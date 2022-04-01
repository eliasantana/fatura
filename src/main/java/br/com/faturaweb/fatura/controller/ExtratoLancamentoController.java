package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.services.LancamentoServices;

@Controller
@RequestMapping("/extrato")


public class ExtratoLancamentoController {
	@Autowired
	LancamentoRepository lctoRepository;
	@Autowired
	LancamentoServices lctoServices;
	@GetMapping("/financeiro/{mesAno}")
	public String extratoLancamento(@PathVariable String mesAno, Model model) {
		String mesEano = null;
		int year = LocalDate.now().getYear();
		mesAno =mesAno+  Integer.toString(year);
		List<Lancamento> lctoFuturo = lctoRepository.findLancamentosFuturos(mesAno);
	
		HashMap<String, BigDecimal> totalizacaoDespesaCategoria = lctoServices.totalizacaoDespesaCategoria(mesAno);
		model.addAttribute("lcto",lctoFuturo);
		model.addAttribute("totalizacao",totalizacaoDespesaCategoria.keySet().toArray());
		model.addAttribute("valores",totalizacaoDespesaCategoria.values());
		model.addAttribute("keygrupodespesa",totalizacaoDespesaCategoria.keySet());
		model.addAttribute("grupovalues",totalizacaoDespesaCategoria.values());
		
		String competencia =" Relatório de Despesas Competência - " + mesAno.substring(0,2).concat("/").concat(String.valueOf(year));
		model.addAttribute("competencia",competencia);
		model.addAttribute("total",lctoServices.getTotalLctoMes(mesAno));
		return "extrato_lancamento";
	}
	

}
