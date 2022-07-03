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

import br.com.faturaweb.fatura.model.FormaDePagamento;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.services.ReportService;

@Controller
@EnableAutoConfiguration
@RequestMapping("formapagto")
public class FormaDePagamentoController {

	@Autowired
	FormaDePagamentoRepository formaDePagamentoRepository;

	@Autowired
	ReportService reportServices;

	@GetMapping("/listar")
	public String listarFormaPagto(Model model) {
		List<FormaDePagamento> formasDePagamento = formaDePagamentoRepository.findAllFormasDePagamento();
		model.addAttribute("forma", formasDePagamento);
		return "formapagto/listar-forma-de-pagamento";
	}

	@GetMapping("/cadastrar")
	public String cadastrarModel(Model model) {
		FormaDePagamento formaPagto = new FormaDePagamento();
		model.addAttribute("formapagto", formaPagto);
		return "formapagto/form-formade-pagamento";
	}

	@PostMapping("adicionar")
	public String adicionar(@Valid FormaDePagamento formaPagto, Model model) {
		System.out.println("Descrição do Form: " + formaPagto.getDescricao());
		FormaDePagamento formapagtoForm = new FormaDePagamento(formaPagto.getDescricao());
		model.addAttribute("formapagto", formaPagto);

		if (formaPagto.getCdFormaPgamento() != null) {
			try {
				Optional<FormaDePagamento> formaPagtoLocalizada = formaDePagamentoRepository
						.findById(formaPagto.getCdFormaPgamento());
				formapagtoForm.setCdFormaPgamento(formaPagtoLocalizada.get().getCdFormaPgamento());
				formapagtoForm.setDtInclusao(formaPagtoLocalizada.get().getDtInclusao());
				System.out.println("Salvando forma de pagamento localizada!");
				formaDePagamentoRepository.save(formapagtoForm);

			} catch (NoSuchElementException e) {

				System.out.println("Salvando forma de pagamento não localizada!");

			}
		} else {
			System.out.println("Salvando");
			formaDePagamentoRepository.save(formapagtoForm);

		}
		return "formapagto/form-formade-pagamento";
	}

	@GetMapping("/excluir/{id}")
	public RedirectView excluir(@PathVariable Long id, Model model) {

		FormaDePagamento formaDePagamentoForm = new FormaDePagamento();
		model.addAttribute(formaDePagamentoForm);
		RedirectView redirectView = new RedirectView("/formapagto/listar");

		Optional<FormaDePagamento> formapagto = formaDePagamentoRepository.findById(id);
		formaDePagamentoRepository.delete(formapagto.get());

		return redirectView;

	}

	@GetMapping("/alterar/{id}")
	public String alterar(@PathVariable Long id, Model model) {

		Optional<FormaDePagamento> formaPagtoLocalizada = formaDePagamentoRepository.findById(id);
		model.addAttribute("formapagto", formaPagtoLocalizada.get());
		System.out.println("Forma de pagamento localizada: " + formaPagtoLocalizada.get().toString());
		return "formapagto/form-formade-pagamento";
	}

	

}
