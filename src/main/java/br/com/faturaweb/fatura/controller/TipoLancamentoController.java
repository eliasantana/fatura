package br.com.faturaweb.fatura.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;

@Controller
@EnableAutoConfiguration
@RequestMapping("tipolancamento")
public class TipoLancamentoController {

	@Autowired	
	private TipoLancamentoRepository tipoLancamentoRepository;
	
	
	@GetMapping("listar")
	public String tipoLancamento(Model model) {
		List<TipoLancamento> tiposDeLancamentos = tipoLancamentoRepository.findAllTipoLancamentos();
		model.addAttribute("tipolancamentos", tiposDeLancamentos);
		return "home/listar-tipos-lancamentos";
	}
	
	@GetMapping("cadastro")
	public String formCadastro() {
		return "home/form-tipolancamento";
	}
	
	@PostMapping("adicionar")
	public String home(@Valid TipoLancamento tipoLancamento) {
		
		TipoLancamento tpLancamento = new TipoLancamento(tipoLancamento.getDsTipoLancamento());		
		try {
			Optional<TipoLancamento> tipolancamentoLocalizado = tipoLancamentoRepository.findBydsTipoLancamento(tpLancamento.getDsTipoLancamento());
			TipoLancamento tipoLancamento2 = tipolancamentoLocalizado.get();
			
		} catch (NoSuchElementException e) {			
			tipoLancamentoRepository.save(tpLancamento)	;
		}
		return "home/form-tipolancamento";
	}
	
	@GetMapping("/excluir/{id}")
	public RedirectView excluir(@PathVariable Long id) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:8080/tipolancamento/listar");
		
		if (id!=null) {
			tipoLancamentoRepository.deleteById(id);
		}else {
			System.out.println("Lançamento não localizado!");
		}
		
		return redirectView;
	}
}