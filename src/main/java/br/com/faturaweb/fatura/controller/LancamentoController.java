package br.com.faturaweb.fatura.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/lancamento")
public class LancamentoController {
	
	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@Autowired
	FormaDePagamentoRepository formaDePagamentoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	TipoLancamentoRepository tipoLancamentoRepository;
	
	@RequestMapping("cadastro")
	public String cadastrar(Model model) {
		Lancamento lancamento= new Lancamento();		
		LancamentoForm lf = new LancamentoForm();
		Usuario u = new Usuario();
		Optional<Usuario> usuario = usuarioRepository.findById(1L);
		List<TipoLancamento> tiposDeLancamento = tipoLancamentoRepository.findAllTipoLancamentos();
		
		List<FormaDePagamento> formasDePagamento = formaDePagamentoRepository.findAllFormasDePagamento();
		
		model.addAttribute("lancamentos",lf);
		model.addAttribute("formapagto",formasDePagamento);
		model.addAttribute("tpLancamentos",tiposDeLancamento);
		model.addAttribute("usuario",usuario.get());
		
		return "lancamento/form-lancamento";
	}
	
	@PostMapping("salvar")
	public String salvar(LancamentoForm lancamentoForm) {
		Optional<FormaDePagamento> findByDescricaoFormaDePagamento = formaDePagamentoRepository.findByDescricaoFormaDePagamento(lancamentoForm.getDsFormaDePagamento());
		FormaDePagamento formadepagamento = findByDescricaoFormaDePagamento.get();
		Lancamento lancamento= new Lancamento();		
		Optional<TipoLancamento> findBydsTipoLancamento = tipoLancamentoRepository.findBydsTipoLancamento(lancamentoForm.getDsTipoLancamento());
		TipoLancamento tipoLancamento =findBydsTipoLancamento.get();
		Optional<Usuario> usuario = usuarioRepository.findById(1L);
		
		if (lancamentoForm.getCdLancamento()==null) {
			
			lancamento.setCdLancamento(lancamentoForm.getCdLancamento());
			lancamento.setDsLancamento(lancamentoForm.getDsFormaDePagamento());
			lancamento.setDtCadastro(lancamentoForm.getDtCadastro());
			lancamento.setDtCompetencia(lancamentoForm.getDtCompetencia());			
			lancamento.setFormaDePagamento(formadepagamento);
			lancamento.setSnPago(lancamentoForm.getSnPago());
			lancamento.setTipoLancamento(tipoLancamento);
			lancamento.setUsuario(usuario.get());
			lancamento.setVlPago(lancamentoForm.getVlPago());
			lancamentoRepository.save(lancamento);
			
			System.out.println(lancamento);
			
		
		}else {
			Optional<FormaDePagamento> formaDePagamento = formaDePagamentoRepository.findByDescricaoFormaDePagamento(lancamentoForm.getDsFormaDePagamento());
			
		}
		return "home/dashboard";
	}

}
