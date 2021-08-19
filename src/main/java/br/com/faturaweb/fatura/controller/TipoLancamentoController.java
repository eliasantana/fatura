package br.com.faturaweb.fatura.controller;

import java.awt.PageAttributes.MediaType;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.google.zxing.NotFoundException;

import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.services.ReportService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

	@GetMapping("relatorio/{formato}")
	public RedirectView relatorio(@PathVariable String formato)
			throws NotFoundException, FileNotFoundException, JRException {

		List<TipoLancamento> findAllTipoLancamentos = tipoLancamentoRepository.findAllTipoLancamentos();
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(findAllTipoLancamentos);
		String nomeRelatorio = service.exportReport(formato, "relTiposLancamento", beanCollectionDataSource);
	
		RedirectView rw = new RedirectView("http://localhost:8080/tipolancamento/download/" + nomeRelatorio);
		return rw;
	}

	@GetMapping("download/{nomerelatorio}")
	public ResponseEntity showPdf(@PathVariable String nomerelatorio) {
		String caminho = "C:\\fatura\\relatorio\\".concat(nomerelatorio);
		Path path = Paths.get(caminho);
		byte[] pdfContents = null;

		try {
			pdfContents = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();

		}

		org.springframework.http.HttpHeaders headers = new HttpHeaders();

		headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
		String filename = nomerelatorio;
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity response = new ResponseEntity(pdfContents, headers, HttpStatus.OK);
		
		return response;

	}
}