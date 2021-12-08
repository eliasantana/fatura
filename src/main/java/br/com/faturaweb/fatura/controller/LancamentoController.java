package br.com.faturaweb.fatura.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.form.LancamentoForm;
import br.com.faturaweb.fatura.model.FormaDePagamento;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.repository.UsuarioRepository;

@Controller
@EnableAutoConfiguration
@RequestMapping("lancamento")

public class LancamentoController {

	@Autowired
	LancamentoRepository lancamentoRepository;

	@Autowired
	FormaDePagamentoRepository formaDePagamentoRepository;

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	TipoLancamentoRepository tipoLancamentoRepository;

	@GetMapping("cadastro")
	public String cadastrar(Model model) {
		
		try {
			Lancamento lancamento = new Lancamento();
			LancamentoForm lf = new LancamentoForm();
			Usuario u = new Usuario();
			Optional<Usuario> usuario = usuarioRepository.findById(5L);
			List<TipoLancamento> tiposDeLancamento = tipoLancamentoRepository.findAllTipoLancamentos();

			List<FormaDePagamento> formasDePagamento = formaDePagamentoRepository.findAllFormasDePagamento();

			model.addAttribute("lancamentos", lf);
			model.addAttribute("formapagto", formasDePagamento);
			model.addAttribute("tpLancamentos", tiposDeLancamento);
			model.addAttribute("usuario", usuario.get());
			
		} catch (Exception e) {
			e.getMessage();
		}
	
		
		return "lancamento/form-lancamento";
	}

	@PostMapping("/salvar")
	public String salvar(LancamentoForm lancamentoForm, Model model) {
		Optional<FormaDePagamento> findByDescricaoFormaDePagamento = formaDePagamentoRepository
				.findByDescricaoFormaDePagamento(lancamentoForm.getDsFormaDePagamento());
		FormaDePagamento formadepagamento = findByDescricaoFormaDePagamento.get();
		System.out.println(formadepagamento.getDescricao());
		//Lancamento lancamento = new Lancamento();
		Optional<Lancamento> lancamentoLocalizado = lancamentoRepository.findById(lancamentoForm.getCdLancamento());
		
		Lancamento lancamento = lancamentoLocalizado.get();
		Optional<TipoLancamento> findBydsTipoLancamento = tipoLancamentoRepository
				.findBydsTipoLancamento(lancamentoForm.getDsTipoLancamento());
		TipoLancamento tipoLancamento = findBydsTipoLancamento.get();
		Optional<Usuario> usuario = usuarioRepository.findById(5L);		

			lancamento.setCdLancamento(lancamentoForm.getCdLancamento());
			lancamento.setDsLancamento(lancamentoForm.getDsLancamento());
			lancamento.setDtCadastro(lancamentoForm.getDtCadastro());
			lancamento.setDtCompetencia(lancamentoForm.getDtCompetencia());
			lancamento.setFormaDePagamento(formadepagamento);
			lancamento.setSnPago(lancamentoForm.getSnPago());
			lancamento.setTipoLancamento(tipoLancamento);
			lancamento.setUsuario(usuario.get());
			lancamento.setVlPago(lancamentoForm.getVlPago());
			lancamentoRepository.save(lancamento);

			List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
			model.addAttribute("lancamentos", lancamentos);

		
		return "home/listar-lancamento";
	}

	@GetMapping("/listar")
	public String listar(Model model) {
		//List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
		//Lançamentos do Ano
		List<Lancamento> lancamentos = lancamentoRepository.findLancamentosDoAno();
		System.out.println("listando");
		model.addAttribute("lancamentos", lancamentos);

		return "home/listar-lancamento";
	}

	@GetMapping("excluir/{id}")
	public  RedirectView excluir(@PathVariable Long id, Model model) {
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
		model.addAttribute("lancamentos", lancamentos);
		System.out.println("Excluindo lançamentos");
		Optional<Lancamento> lancamentoLocalizado = lancamentoRepository.findById(id);
		lancamentoRepository.delete(lancamentoLocalizado.get());
		System.out.println("Lançamento Excluído com sucesso!");
		//RedirectView rw = new RedirectView("http://localhost:8080/listar");
		RedirectView rw = new RedirectView("https://sysfaturaapp.herokuapp.com/listar"); 
		return rw;
		
	}

	@GetMapping("alterar/{id}")
	public String alterar(@PathVariable Long id, Model model) {

		Lancamento lancamento = lancamentoRepository.findByIdLancamento(id);

		Optional<FormaDePagamento> formaDePagamento = formaDePagamentoRepository
				.findById(lancamento.getFormaDePagamento().getCdFormaPgamento());

		Optional<TipoLancamento> findBydsTipoLancamento = tipoLancamentoRepository
				.findById(lancamento.getCdLancamento());

		TipoLancamento tipoLancamento = tipoLancamentoRepository
				.findTipoLancamentoId(lancamento.getTipoLancamento().getCdTipoLancamento());

		Optional<Usuario> usuario = usuarioRepository.findById(5L);

		LancamentoForm lf = new LancamentoForm();
		lf.setCdLancamento(lancamento.getCdLancamento());
		lf.setDsLancamento(lancamento.getDsLancamento());
		lf.setDsFormaDePagamento(formaDePagamento.get().getDescricao());
		lf.setSnPago(lancamento.getSnPago());
		lf.setVlPago(lancamento.getVlPago());
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
		model.addAttribute("lancamentos", lf);
		model.addAttribute("formapagto", formaDePagamento.get());
		model.addAttribute("tpLancamentos", tipoLancamento);
		model.addAttribute("usuario", usuario.get());
		
		return "home/form-lancamento";
	}
	
	
	@GetMapping("/pagar/{id}")
	public String pagar(@PathVariable Long id, Model model) {
		//RedirectView rw = new RedirectView("https://sysfaturaapp.herokuapp.com/listar"); 
		Lancamento lancamento = lancamentoRepository.findByIdLancamento(id);
		//List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
		List<Lancamento> lancamentos = lancamentoRepository.findLancamentosDoAno();
		lancamento.setSnPago("SIM");
		lancamentoRepository.save(lancamento);
		model.addAttribute("lancamentos",lancamentos);
		return "home/listar-lancamento";
		}

}

