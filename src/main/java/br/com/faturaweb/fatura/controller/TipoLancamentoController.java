package br.com.faturaweb.fatura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.services.TipoLancamentoServices;

@Controller
@EnableAutoConfiguration

public class TipoLancamentoController {

	@Autowired
	private TipoLancamentoRepository tipoLancamentoRepository;
	
	@PostMapping("/tipolancamento")
	public String home(TipoLancamento tipoLancamento) {
		
		TipoLancamento tpLancamento = new TipoLancamento(tipoLancamento.getDsTipoLancamento());
		tipoLancamentoRepository.save(tpLancamento);
			
		return "home/tipolancamento";
	}

}
