package br.com.faturaweb.fatura.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.ItMeta;
import br.com.faturaweb.fatura.model.Meta;
import br.com.faturaweb.fatura.repository.ItMetaRepository;
import br.com.faturaweb.fatura.repository.MetaRepository;
import br.com.faturaweb.fatura.services.AppServices;

@Controller
@RequestMapping("itmeta")
public class ItMEtaController {

	@Autowired
	ItMetaRepository itMetaRepository;
	@Autowired
	MetaRepository metaRepository;
	
	@Autowired
	AppServices appServices;
	
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
	
	/**
	 * Credita o valor do item da meta na conta informada
	 * @author elias
	 * @since 29-12-2021
	 * */
	@GetMapping("credita/{idMeta}/{idItMeta}")
	public RedirectView creditar(@PathVariable Long idMeta, @PathVariable Long idItMeta, Model model) {
		RedirectView rw = new RedirectView("http://localhost:8080/itmeta/listar/"+idMeta);
		ItMeta itMeta = itMetaRepository.findItMetaId(idItMeta);
		//Valia o id informadoé igual ao localizado
		if (itMeta.getCdItMeta()!=null) {
			//Localizando a conta  conta
			 Conta conta = itMeta.getMeta().getConta();
			 //Credita o valor do item da meta (Valor Semanal)
			appServices.credita(conta, itMeta.getVlrSemana());
			//Muda o status para creditado
			itMeta.setSnCreditado("S");
			itMetaRepository.save(itMeta);
		}
		return rw;
	}
	
}
