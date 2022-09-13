package br.com.faturaweb.fatura.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.nio.file.Files;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.form.LancamentoForm;
import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.FormaDePagamento;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.repository.UsuarioRepository;
import br.com.faturaweb.fatura.services.LancamentoServices;
import br.com.faturaweb.fatura.services.ReportService;

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
	@Autowired
	ContaRepository contaRepository;
	@Autowired
	LogMovimentacaoFinanceiraRepository logRepository;
	@Autowired
	LoteRepository loteRepository;
	@Autowired
	ReportService reportServices;	
	@Autowired
	LancamentoServices services;	
@Autowired
	ConfiguracoesRepository configuracoesRepository;

	@GetMapping("/cadastro")
	public String cadastrar(Model model) {
		String status = "A";
		try {
			 status = loteRepository.findLoteCompetencia().getStatus();			
		} catch (Exception e) {
			System.out.println("Não há lote aberto na competencia!");
		}
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
			model.addAttribute("status",status);
			if(status.equals("F")) model.addAttribute("mensagem","Lançamento não permitido! O lote da competencia está fechado!");
			
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


	@GetMapping("/excluir/{id}")
	public  RedirectView excluir(@PathVariable Long id, Model model) {
		//List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
		List<Lancamento> lancamentos = lancamentoRepository.findLancamentoMesSeguinte();
		model.addAttribute("lancamentos", lancamentos);
		System.out.println("Excluindo lançamentos");
		Optional<Lancamento> lancamentoLocalizado = lancamentoRepository.findById(id);
		lancamentoRepository.delete(lancamentoLocalizado.get());
		System.out.println("Lançamento Excluído com sucesso!");
		RedirectView rw = new RedirectView("/listar");
		return rw;
		
	}

	@GetMapping("/alterar/{id}")
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
	
	
	@GetMapping("/pagar/{id}")
	public RedirectView pagar(@PathVariable Long id, Model model) {
		Lancamento lancamento = lancamentoRepository.findByIdLancamento(id);
		RedirectView rw = new RedirectView("/listar");
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentosDoMes();
		lancamento.setSnPago("SIM");
		lancamentoRepository.save(lancamento);
		
		if ("Débito".equals( lancamento.getFormaDePagamento().getDescricao())) {
			//Debitar na conta informada
			Configuracoes config = configuracoesRepository.findConfiguracao();
			String nrContaOrigem = config.getNrContaOrigem();
			Optional<Conta> contaLocalizada = contaRepository.findConta(nrContaOrigem);

			if(contaLocalizada.isPresent()) {
				Conta conta = contaLocalizada.get();				
				BigDecimal novoSaldo = conta.getSaldo().subtract(lancamento.getVlPago());
				conta.setSaldo(novoSaldo);
				contaRepository.save(conta);
				
				LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();
				log.setDescricao("Pagamento " + lancamento.getDsLancamento() + "  Data:  " + LocalDate.now() + " Conta: "+conta.getNrConta() + " - " + conta.getDsConta());
				log.setNrConta(conta.getNrConta());
				log.setDtMovimentacao(LocalDate.now());
				log.setTpMovimentacao("D");
				log.setUsuario("Elias");
				log.setVlMovimentado(lancamento.getVlPago());
				logRepository.save(log);
				
			}
			
		}
		model.addAttribute("lancamentos",lancamentos);
		//return "home/listar-lancamento";
		
		return rw;
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
				System.out.println("A descrição contem");
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
			System.out.println("Arquivo não localizado!");
		}
		
		return "teste";
	}
	
	
	@GetMapping("/detalhe/{id}")
	public String deTalheLancamento(Model model, @PathVariable  Long id) {
		Lancamento l = lancamentoRepository.findByIdLancamento(id);
		model.addAttribute("lancamento",l);
		return "home/detalhe-lancamento";
	}
	
	@GetMapping("/getimagem")
	@ResponseBody
	public byte[] getlogo() {
		Configuracoes config = configuracoesRepository.findConfiguracao();
		return config.getLogo();
	}
	
	@GetMapping("/transferir/{id}")
	public RedirectView transferir(@PathVariable  Long id) {
		RedirectView rw = new RedirectView("/listar");
		Lancamento lancamentoLocalizado = lancamentoRepository.findByIdLancamento(id);
		LocalDate novaCompetencia = LocalDate.now().plusMonths(1L);
		if (lancamentoLocalizado.getSnPago().toUpperCase().equals("NÃO")) {
			lancamentoLocalizado.setDtCompetencia(novaCompetencia);
			lancamentoRepository.save(lancamentoLocalizado);
		}
		return rw;
	}
}

