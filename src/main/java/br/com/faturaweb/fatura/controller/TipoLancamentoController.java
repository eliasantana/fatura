package br.com.faturaweb.fatura.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.services.ReportService;

@Controller
@EnableAutoConfiguration	
@RequestMapping("tipolancamento")
public class TipoLancamentoController {

	@Autowired
	private TipoLancamentoRepository tipoLancamentoRepository;

	@Autowired
	ReportService service;

	@GetMapping("listar")
	public String tipoLancamento(Model model) {
		List<TipoLancamento> tiposDeLancamentos = tipoLancamentoRepository.findAllTipoLancamentos();
		model.addAttribute("tipolancamentos", tiposDeLancamentos);
		return "home/listar-tipos-lancamentos";
	}

	@GetMapping("cadastro")
	public String formCadastro(Model model) {
		TipoLancamento tp = new TipoLancamento();
		model.addAttribute("tipo", tp);
		return "home/form-tipolancamento";
	}

	@PostMapping("adicionar")
	public String home(@Valid TipoLancamento tipoLancamento) {

		TipoLancamento tpLancamento = new TipoLancamento(tipoLancamento.getDsTipoLancamento());
		try {
			Optional<TipoLancamento> tipolancamentoLocalizado = tipoLancamentoRepository
					.findBydsTipoLancamento(tpLancamento.getDsTipoLancamento());
			TipoLancamento tipoLancamento2 = tipolancamentoLocalizado.get();

		} catch (NoSuchElementException e) {
			tipoLancamentoRepository.save(tpLancamento);
		}
		return "home/form-tipolancamento";
	}

	@GetMapping("/excluir/{id}")
	public RedirectView excluir(@PathVariable Long id) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:8080/tipolancamento/listar");

		if (id != null) {
			tipoLancamentoRepository.deleteById(id);
		} else {
			System.out.println("Lançamento não localizado!");
		}

		return redirectView;
	}

	@GetMapping("/alterar/{id}")
	public String alterar(@PathVariable Long id, Model model, TipoLancamento tpLancamento) {

		Optional<TipoLancamento> tipolancamento = tipoLancamentoRepository.findBycdTipoLancamento(id);
		TipoLancamento tipoLancamento2 = tipolancamento.get();
		model.addAttribute("tipo", tipoLancamento2);
		return "home/form-tipolancamento-atualizar";
	}

	@PostMapping("/atualizar")
	public RedirectView atualizar(TipoLancamento tp, Model model) {

		TipoLancamento tipolancamentoForm = tp;
		Optional<TipoLancamento> tipolancamento = tipoLancamentoRepository
				.findBycdTipoLancamento(tp.getCdTipoLancamento());

		tipolancamentoForm.setDtCadastro(tipolancamento.get().getDtCadastro());
		tipoLancamentoRepository.save(tp);
		model.addAttribute("tipo", tp);
		RedirectView rdw = new RedirectView();
		rdw.setUrl("http://localhost:8080/tipolancamento/listar");
		return rdw;
	}

	
}