package br.com.faturaweb.fatura.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

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
	
	@PostMapping("/alterar/{chave}")
	public RedirectView alterarCahave(@PathVariable String chave) {
		RedirectView rw = new RedirectView("/listar");  
		Optional<ChaveConfig> chaveLocalizada = chaveRepository.findChaveConfigByDescricao(chave);
		  if (chaveLocalizada.isPresent()) {
			  if (chaveLocalizada.get().getValor().equals("S")) {
				  chaveLocalizada.get().setValor("N");
			  }else {
				  chaveLocalizada.get().setValor("S");
			  }
			  chaveRepository.save(chaveLocalizada.get());
		  }
		  
		  return rw;
	}
	
}
