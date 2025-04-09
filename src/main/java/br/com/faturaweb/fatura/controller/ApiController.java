package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.faturaweb.fatura.dto.HistoricoPagamentoDto;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.services.LancamentoServices;
import br.com.faturaweb.fatura.services.ReceitaServices;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@Autowired
	TipoLancamentoRepository tiposRepository;
	
	@Autowired
	LancamentoServices services;
	@Autowired
	ReceitaServices receitaServices;

	@GetMapping("/lancamentos")
	public List<Lancamento> getLancamentos() {
		
			return lancamentoRepository.findAllLancamentos();
	}
	
	@GetMapping("/tipolancamento")
	@ResponseBody
	public List<TipoLancamento> getTipoLancamento(){
		
		return tiposRepository.findAllTipoLancamentos();
		
	}
	@GetMapping("/getTotalizacao")
	public 	String getTotal(){
		HashMap<String, BigDecimal> totalizacaoDespesaCategoria = services.totalizacaoDespesaCategoria();
		Set<String> keySet = totalizacaoDespesaCategoria.keySet();
		  java.util.Collection<BigDecimal> values = totalizacaoDespesaCategoria.values();
	    Iterator i = keySet.iterator();
		Iterator<BigDecimal> ivalues = values.iterator();
		String str = new String();
	    while (i.hasNext()) {
	    	str= str+"{ name : ' " +i.next() + " ' , y: " +ivalues.next() + " },";
	    }
	    str = str.substring(0,str.length()-1);
	 return str;
	}
	@PostMapping("/integra")
		public ResponseEntity<Lancamento>integra(@RequestBody HistoricoPagamentoDto dto, UriComponentsBuilder builder ) throws Exception{
				return services.integra(dto, builder);
		}
	@PostMapping("/integratodos")
	public ResponseEntity<Lancamento>integraTodos(@RequestBody List< HistoricoPagamentoDto>dto, UriComponentsBuilder builder ) throws Exception{
			return services.integraTodos(dto, builder);
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Lancamento>delete(@PathVariable Long id) throws Exception{
			return receitaServices.delete(id);
	}
	
	@GetMapping("/status")
	public ResponseEntity<String> getStatusCode() {
			String status = "S";
		return ResponseEntity.ok(status);
	}
}

