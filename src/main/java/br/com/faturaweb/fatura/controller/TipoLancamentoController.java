package br.com.faturaweb.fatura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;

@Controller
public class TipoLancamentoController {

	@Autowired
	private TipoLancamentoRepository tipoLancamentoRepository;
	
	@GetMapping("/tipolancamento")
	public String home() {
		System.out.println("Tipolancamento");
		TipoLancamento tipo = new TipoLancamento("Carteira");
	    tipoLancamentoRepository.save(tipo);
	    
		return "index";
	}
	
}
