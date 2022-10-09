package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.services.LancamentoServices;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@Autowired
	TipoLancamentoRepository tiposRepository;
	
	@Autowired
	LancamentoServices services;

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
	
	
}
