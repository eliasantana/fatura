package br.com.faturaweb.fatura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.Meta;
import br.com.faturaweb.fatura.services.MetaService;

@Controller
@RequestMapping("meta")
public class MetaController {
	@Autowired
	MetaService metaServices;

	@GetMapping("/listar")
	public String listar(Model model, Meta meta) {
		metaServices.listar(model, meta);
		return "meta";
	}

	@PostMapping("/salvar")
	public RedirectView salvar(Model model, Meta metaForm, Conta conta) {
		RedirectView rw = metaServices.salvar(model, metaForm, conta);
		return rw;
	}

	@GetMapping("excluir/{id}")
	public RedirectView exclulir(@PathVariable Long id, Model model) {
		RedirectView rw = metaServices.excluir(model, id);
		return rw;
	}
	
	@GetMapping("/encerrar/{idMeta}")
	public RedirectView encerrarMeta(@PathVariable Long idMeta, Model model) {
		RedirectView rw = metaServices.encerrarMeta(idMeta, model);
		return rw;
	}

}
