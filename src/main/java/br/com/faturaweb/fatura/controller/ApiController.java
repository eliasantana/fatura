package br.com.faturaweb.fatura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.repository.LancamentoRepository;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	LancamentoRepository lancamentoRepository;

	@GetMapping("/lancamentos")
	public List<Lancamento> getLancamentos() {
		
			return lancamentoRepository.findAllLancamentos();
	}
}
