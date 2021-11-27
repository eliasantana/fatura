package br.com.faturaweb.fatura.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Receita;
import br.com.faturaweb.fatura.repository.ReceitaRepository;
import br.com.faturaweb.fatura.services.ReceitaServices;

@Controller
@RequestMapping("/receita")
public class ReceitaController {
	
	@Autowired
	ReceitaRepository receitaRepository;
	
	@Autowired
	ReceitaServices services;
	
	@GetMapping("/listar")	
	public String  listar(Model model) {
		List<Receita> receitas = receitaRepository.findAllList();
		model.addAttribute("receitas",receitas);
		return "home/receita-listar";
	}
	
	@GetMapping("/cadastro")
	public String cadastrar(Model model) {
	
	Receita receita = new Receita();
	model.addAttribute("receita",receita)	;
	 return "home/receita";
		
	}
	
	@PostMapping("/salvar")
	public String salvar(Model model, Receita recetaForm) {
		try {
			receitaRepository.save(recetaForm);
			//model.addAttribute("mensagem","Receita salva com sucesso!");
			model.addAttribute("mensagem","Receita Salva com sucesso!");
		} catch (Exception e) {
			  System.out.println("Não foi possível salvar a receita informada ->"+ recetaForm.getDsReceita());
			  model.addAttribute("mensagem","Falha ao tentar savar a receita!");
		}
		model.addAttribute("receita",new Receita());
		return "home/receita";
	}
	
	@GetMapping("excluir/{id}")
	public RedirectView excluir(@PathVariable Long id, Model model) {
		RedirectView rw = new RedirectView();
		rw.setUrl("http://localhost:8080/receita/listar");
		Optional<Receita> receita = receitaRepository.findById(id);
		if (receita.get().getCdReceita()!=null){
			receitaRepository.delete(receita.get());
		}
		
		return rw;
	}
	
	@GetMapping("alterar/{id}")
	public String alterar(@PathVariable Long id, Model model) {
		
		Optional<Receita> receita = receitaRepository.findById(id);
		model.addAttribute("receita",receita.get());
		
		return "home/receita";
	}
	
	}
