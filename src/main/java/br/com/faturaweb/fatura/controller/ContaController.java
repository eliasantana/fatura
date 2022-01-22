package br.com.faturaweb.fatura.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.services.AppServices;

@Controller
@RequestMapping("/conta")
public class ContaController {
@Autowired
ContaRepository repository;
@Autowired
AppServices appservices;	
	@GetMapping("listar")
	public String conta(Model model) {
		List<Conta> contas = repository.findcontas();
		Conta conta = new Conta();
		model.addAttribute("conta",conta);
		model.addAttribute("contas",contas);
		return "conta";	
	}
	
	@PostMapping("salvar")
	public RedirectView salvar(Model model, Conta conta , @RequestParam("file") MultipartFile file) throws IOException {
		RedirectView rw = new RedirectView("http://localhost:8080/conta/listar");
		conta.setQrcod(file.getBytes());
		repository.save(conta);
		return rw;
	}
	
	@GetMapping("/qrcode/{id}")
	@ResponseBody
	public byte[] getImagem(@PathVariable Long id) {	
		Optional<Conta> conta = repository.findById(id);
		
			return conta.get().getQrcod();		
		
	}
	
	@GetMapping("alterar/{id}")
	public String alterar(@PathVariable Long id, Model model) {
		try {
			Optional<Conta> conta = repository.findById(id);
			List<Conta> contas = repository.findcontas();
			model.addAttribute("conta",conta.get());
			model.addAttribute("contas",contas);
		} catch (Exception e) {
			System.out.println("Conta não localizada!");
		}
		
		return "conta";	
	}
	
	@GetMapping("excluir/{id}")
	public RedirectView excluirConta(@PathVariable Long Id) {
		RedirectView rw = new RedirectView("http://localhost:8080/conta/listar");
		Optional<Conta> conta = repository.findById(Id);
		repository.delete(conta.get());
		return rw;
	}
	
}
