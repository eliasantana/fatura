package br.com.faturaweb.fatura.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.spi.FileSystemProvider;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import br.com.faturaweb.fatura.form.LancamentoForm;
import br.com.faturaweb.fatura.model.AnoLancamento;
import br.com.faturaweb.fatura.model.Chave;
import br.com.faturaweb.fatura.model.ChaveConfig;
import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.LogProvisao;
import br.com.faturaweb.fatura.model.Teste;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.projection.AnoLancamentoProjection;
import br.com.faturaweb.fatura.repository.ChaveRepository;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.LogProvisaoRepository;
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
		@Autowired
		LancamentoRepository lancamentoRepository;
		@Autowired
		LogProvisaoRepository logprovRepository;
		@Autowired
		ConfiguracoesRepository config;
		@Autowired
		AppServices appServices;
		@Autowired
		LancamentoServices lancamentoServices;
		@Autowired
		ChaveRepository chaveRepository;
		
	@GetMapping("/teste")
	public String apiltipolancnamento(Model model){

		Configuracoes findConfiguracao = config.findConfiguracao();
		System.out.println(findConfiguracao.getDirImportacao());
		System.out.println(findConfiguracao.getDirRelatorio());
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
	
	@GetMapping("/compare")
	public String compare() {
		
		BigDecimal b1 = new BigDecimal(100);  // =0 <> -1    a > b = 1   (a = b) = 0   a <> b = -1
		BigDecimal saque = new BigDecimal(-110);
		
		System.out.println("Resultado "+b1.compareTo(saque));
		System.out.println("Comparando com zero "+b1.compareTo(BigDecimal.ZERO));
		
		System.out.println("Origem :" + b1);
		System.out.println("Destino:  " + saque);
		
		if (saque.compareTo(BigDecimal.ZERO)==1) {
			if (b1.compareTo(saque)>-1) {
				System.out.println("Debida");
			}else {
				System.err.println("Saldo insuficiente");
			}
		}else {
			System.err.println("Valor inválido");
		}
		
			
		
		return "teste";
	}
	
	
}

