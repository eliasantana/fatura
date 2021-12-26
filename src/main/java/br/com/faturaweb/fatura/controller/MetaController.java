package br.com.faturaweb.fatura.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.Meta;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.MetaRepository;

@Controller
@RequestMapping("meta")
public class MetaController {
@Autowired	
ContaRepository contaRepository;
@Autowired
MetaRepository metaRepository;

	@GetMapping("/listar")
	public String listar(Model model, Meta meta) {
	   List<Conta> contas = contaRepository.findcontas();
	   List<Meta> metas = metaRepository.findAllMetas();
	   Conta c= new Conta();
	   Meta m = new Meta();
	   m.setConta(c);
	   model.addAttribute("meta",m);
	   model.addAttribute("contas",contas);
	   model.addAttribute(c);
	   model.addAttribute("mensagem",null);
	   model.addAttribute("metas",metas);
		return "meta";
	}
	
	@PostMapping("/salvar")
	public RedirectView salvar(Model model, Meta metaForm, Conta conta) {
		RedirectView rw = new RedirectView("http://localhost:8080/meta/listar");
		List<Conta> contas = contaRepository.findcontas();
		 List<Meta> metas = metaRepository.findAllMetas();
		try {
			Optional<Conta> contaLocalizada = contaRepository.findConta(conta.getNrConta());
			if (contaLocalizada.isPresent()) {
				metaForm.setConta(contaLocalizada.get());
			}
		     Conta c= new Conta();
			 Meta m = new Meta();
			model.addAttribute("meta",m);
			model.addAttribute(c);
			model.addAttribute("contas",contas);
			model.addAttribute("mensagem","Meta salva com sucesso!");  
			model.addAttribute("metas",metas);
			metaRepository.save(metaForm);
		} catch (Exception e) {
			
		}
		return rw;
	}
	
	
}
