package br.com.faturaweb.fatura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.faturaweb.fatura.repository.ItMetaRepository;

@Controller

public class ItMEtaController {

	@Autowired
	ItMetaRepository itMetaRepository;
	
	
}
