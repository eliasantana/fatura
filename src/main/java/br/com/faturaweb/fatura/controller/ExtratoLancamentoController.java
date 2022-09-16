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
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.services.ContaServices;
import br.com.faturaweb.fatura.services.LancamentoServices;

@Controller
@RequestMapping("/extrato")


public class ExtratoLancamentoController {
	@Autowired
	LancamentoRepository lctoRepository;
	@Autowired
	LancamentoServices lctoServices;
	@Autowired
	ContaRepository contaRepository;
	@Autowired
	ContaServices contaServices;
	@Autowired
	ConfiguracoesRepository configuracoesRepository;
	
	@GetMapping("/financeiro/{mesAno}")
	public String extratoLancamento(@PathVariable String mesAno, Model model) {
		int year = LocalDate.now().getYear();
		mesAno =mesAno+  Integer.toString(year);
		List<Lancamento> lctoFuturo = lctoRepository.findLancamentosFuturos(mesAno);
		List<Conta> contas = contaRepository.findcontas();
		BigDecimal saldoGeral = contaServices.getSaldoGeral(contas);
		HashMap<String, BigDecimal> totalizacaoDespesaCategoria = lctoServices.totalizacaoDespesaCategoria(mesAno);
		HashMap<String, BigDecimal> totalDespesaFormaPagto = lctoServices.getTotalizacaoDespesaFormaPagto(mesAno);

		model.addAttribute("lcto",lctoFuturo);
		model.addAttribute("totalizacao",totalizacaoDespesaCategoria.keySet().toArray());
		model.addAttribute("valores",totalizacaoDespesaCategoria.values());
		model.addAttribute("keygrupodespesa",totalizacaoDespesaCategoria.keySet().toArray());
		model.addAttribute("grupovalues",totalizacaoDespesaCategoria.values());
		model.addAttribute("keytotalformapagto",totalDespesaFormaPagto.keySet().toArray());
		model.addAttribute("valorTotalformapagto",totalDespesaFormaPagto.values());
		model.addAttribute("saldoGeral",saldoGeral);
		
		String competencia =" Relatório de Despesas Competência - " + mesAno.substring(0,2).concat("/").concat(String.valueOf(year));
		model.addAttribute("competencia",competencia);
		model.addAttribute("total",lctoServices.getTotalLctoMes(mesAno));
		model.addAttribute("contas",contas);
		return "extrato_lancamento";
	}
	
	@GetMapping("/getimagem")
	@ResponseBody
	public byte[] getlogo() {
		Configuracoes config = configuracoesRepository.findConfiguracao();
		return config.getLogo();
	}
}
