package br.com.faturaweb.fatura.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.form.LancamentoForm;
import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.FormaDePagamento;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.repository.UsuarioRepository;
import br.com.faturaweb.fatura.services.LancamentoServices;
import br.com.faturaweb.fatura.services.ReportService;

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
	@Autowired
	ReportService reportServices;
	
	@Autowired
	LancamentoServices services;
	
@Autowired
	ConfiguracoesRepository configuracoesRepository;

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
		Configuracoes config = configuracoesRepository.findConfiguracao();
		
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
			lancamento.setObservacao(lancamentoForm.getObservacao());
			lancamentoRepository.save(lancamento);
			Lancamento findUltimoLancamentoUsuario = lancamentoRepository.findUltimoLancamentoUsuario(usuario.get().getCdUsuario());
			System.out.println("Ultimo lancamento Localizado: " + findUltimoLancamentoUsuario.toString());

			List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentosDoMes();
			model.addAttribute("lancamentos", lancamentos);
			
			
		return "home/listar-lancamento";
	}


	@GetMapping("excluir/{id}")
	public  RedirectView excluir(@PathVariable Long id, Model model) {
		//List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
		List<Lancamento> lancamentos = lancamentoRepository.findLancamentoMesSeguinte();
		model.addAttribute("lancamentos", lancamentos);
		System.out.println("Excluindo lan??amentos");
		Optional<Lancamento> lancamentoLocalizado = lancamentoRepository.findById(id);
		lancamentoRepository.delete(lancamentoLocalizado.get());
		System.out.println("Lan??amento Exclu??do com sucesso!");
		RedirectView rw = new RedirectView("/listar");
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
		lf.setObservacao(lancamento.getObservacao());
		model.addAttribute("lancamentos", lf);
		model.addAttribute("formapagto", formaDePagamento.get());
		model.addAttribute("tpLancamentos", tipoLancamento);
		model.addAttribute("usuario", usuario.get());
		
		return "home/form-lancamento";
	}
	
	
	@GetMapping("pagar/{id}")
	public String pagar(@PathVariable Long id, Model model) {
		Lancamento lancamento = lancamentoRepository.findByIdLancamento(id);
		//List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentosDoMes();
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentosDoMes();
		lancamento.setSnPago("SIM");
		lancamentoRepository.save(lancamento);
		model.addAttribute("lancamentos",lancamentos);
		return "home/listar-lancamento";
		}

	@GetMapping("anexar/{id}")
	public String anexar(@PathVariable Long id, Model model) {

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
		model.addAttribute("cdLancamento",id);
		return "home/form-lancamento-anexo";
	}

	@GetMapping("anexo/{id}")
	public String exibirAnexo(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) throws IOException  {
		Configuracoes config = configuracoesRepository.findConfiguracao();
		Lancamento lancamento = lancamentoRepository.findByIdLancamento(id);
		System.out.println(" Anexo: "+lancamento.getDsAnexo());
		File file = new File(lancamento.getDsAnexo());

		if (file.exists()) {
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			System.out.println("Nome do Arquivo: " + file.getName());
			System.out.println("Encontrei o arquivo");
			if (lancamento.getDsAnexo().contains(file.getName())) {
				System.out.println("A descri????o contem");
			}
			System.out.println();
			if (mimeType==null) {
				mimeType = "application/octet-stream";
			}
			response.setContentType(mimeType);
			response.setContentLength((int) file.length());
			
			InputStream input = new BufferedInputStream(new FileInputStream(file));
			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(input,outputStream);
		}else {
			System.out.println("Arquivo n??o localizado!");
		}
		
		return "teste";
	}
	
	@GetMapping("imprimir/{formato}")
	public String geraRelatorio(@PathVariable String formato) {
		String  mensagem = "";
		try {
			 mensagem = reportServices.exportReport("pdf","lancamento");
		} catch (Exception e) {
			mensagem = "N??o foi poss??vel gerar o relat??rio!";
		}
		return mensagem;
	}
}

