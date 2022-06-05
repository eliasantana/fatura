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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.sound.midi.Soundbank;

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
		AppServices services;
		@Autowired
		ReportService reportServices;
		
		@Autowired
		TipoLancamentoRepository tipoLancamentoRepository;
		

	@GetMapping("/teste")
	public String apiltipolancnamento(Model model){
		String valores = lctoServices.getTotal();
		System.out.println(valores);
		// Exemplo de Formatação de Data 
		//Obtém LocalDate de hoje
        LocalDate hoje = LocalDate.now();

        System.out.println("LocalDate antes de formatar: " + hoje);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String hojeFormatado = hoje.format(formatter);

        System.out.println("LocalDate depois de formatar: " + hojeFormatado);

        //Obtém LocalDateTime de agora
        LocalDateTime agora = LocalDateTime.now();

        System.out.println("LocalDateTime antes de formatar: " + agora);

        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String agoraFormatado = agora.format(formatter);

        System.out.println("LocalDateTime depois de formatar: " + agoraFormatado);
		model.addAttribute("valores",valores);
		model.addAttribute("data",agora.format(formatter));
		
		return  "teste";
	}
	
	
	
}

