package br.com.faturaweb.fatura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.services.ItMetaServices;

@Controller
@RequestMapping("itmeta")
public class ItMEtaController {

	//Services
	@Autowired
	ItMetaServices itmetaservices;
	
	//05-02-2022Método Refatorado para o services excluir lihas comentadas
	@GetMapping("listar/{id}")
	public String listarItMeta(@PathVariable Long id, Model model) {
		String pagina = itmetaservices.listarItMeta(id, model);
		return pagina;
	}
	
	/**
	 * Credita o valor do item da meta na conta informada
	 * @author elias
	 * @since 29-12-2021
	 * */
	@GetMapping("credita/{idMeta}/{idItMeta}")
	public RedirectView creditar(@PathVariable Long idMeta, @PathVariable Long idItMeta, Model model) {
		RedirectView rw = itmetaservices.creditarItMeta(idMeta, idItMeta, model);
		return rw;
	}
	/**
	 * Regera os itens de meta excluíndo os ainda não creditados
	 * */
	
	@GetMapping("/regerar/{id}")
	public RedirectView reGerarMetaItMeta(@PathVariable Long id) {
		RedirectView rw = itmetaservices.reGerarMetaItMeta(id);
		return rw;
	}
	
}
