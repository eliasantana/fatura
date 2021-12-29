package br.com.faturaweb.fatura.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.faturaweb.fatura.model.ItMeta;
import br.com.faturaweb.fatura.model.Meta;
import br.com.faturaweb.fatura.repository.ItMetaRepository;
import br.com.faturaweb.fatura.repository.MetaRepository;

@Controller
@RequestMapping("itmeta")
public class ItMEtaController {

	@Autowired
	ItMetaRepository itMetaRepository;
	@Autowired
	MetaRepository metaRepository;
	@GetMapping("listar/{id}")
	public String listarItMeta(@PathVariable Long id, Model model) {
		System.out.println("tesete");
		List<ItMeta> itMeta = itMetaRepository.findItNaoCreditado(id);
		try {
			Optional<Meta> meta = metaRepository.findById(itMeta.get(0).getMeta().getCdMeta());			
			model.addAttribute("nome_meta",meta.get().getDescricao());
			model.addAttribute("cdmeta",meta.get().getCdMeta());
			model.addAttribute("itens",itMeta);
		} catch (Exception e) {
			model.addAttribute("nome_meta","Não há itens de  meta a serem exibidos!");
			model.addAttribute("cdmeta","");
			model.addAttribute("itens",itMeta);
		}
		return "detalhe-meta";
		
	}
	
}
