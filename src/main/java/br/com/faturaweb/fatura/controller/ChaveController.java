package br.com.faturaweb.fatura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.faturaweb.fatura.model.ChaveConfig;
import br.com.faturaweb.fatura.repository.ChaveRepository;

@Controller
@RequestMapping("/chaves")
public class ChaveController {
	@Autowired
	ChaveRepository chaveRepository;
	
	@RequestMapping("/listar")
	@ResponseBody
	public Iterable<ChaveConfig> chaveConfig() {
				
		return   chaveRepository.findAll();
	}
	
	
}
