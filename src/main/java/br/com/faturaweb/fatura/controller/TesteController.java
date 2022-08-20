package br.com.faturaweb.fatura.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.sound.midi.Soundbank;

import org.apache.commons.collections4.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.Teste;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.TesteRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.services.AppServices;
import br.com.faturaweb.fatura.services.LancamentoServices;
import br.com.faturaweb.fatura.services.ReportService;
import net.sf.jasperreports.engine.JRException;

@Controller

public class TesteController {

		@Autowired
		LancamentoRepository r;
		@Autowired
		TesteRepository testeRepository;
		@Autowired
		LancamentoServices lctoServices;
		@Autowired
		TipoLancamentoRepository TipoLancamentoRepository;
	
		@Autowired
		ReportService reportServices;
		
		@Autowired
		TipoLancamentoRepository tipoLancamentoRepository;
		

	@GetMapping("/teste")
	public String apiltipolancnamento(Model model){
		model.addAttribute("mensagem","Conteudo muito importante!");
		return "teste";
	}
	
	@GetMapping("/sem-ajax")
	public String semAjax(Model model) {
		List<TipoLancamento> tipos = tipoLancamentoRepository.findAllTipoLancamentos();
		model.addAttribute("tipos",tipos);		
	return "teste";	
	}
	
	@GetMapping("/com-ajax")
	public String comAjax(Model model) {
		List<TipoLancamento> tipos = tipoLancamentoRepository.findAllTipoLancamentos();
		model.addAttribute("tipos",tipos);
		
	return"detalhe";	
	}
	
	/**
	 * Chamando um procedimento de banco
	 * */
	@GetMapping("/prc1")
	public String prc_teste() {
		List<Lancamento> findLancamentoPrc = r.findLancamentoPrc(10);	
		int qtdLancamentoLocalizados = findLancamentoPrc.size();
		for (Lancamento lancamento : findLancamentoPrc) {
			System.out.println(lancamento.getDsLancamento());
		}
		System.out.println("Lancamentos localizados" + qtdLancamentoLocalizados);
		return "teste";
	}
	
	
}

