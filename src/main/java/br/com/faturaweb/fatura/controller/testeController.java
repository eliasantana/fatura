package br.com.faturaweb.fatura.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import br.com.faturaweb.fatura.services.ReportService;
import net.sf.jasperreports.engine.JRException;

@Controller
public class testeController {

		@Autowired
		LancamentoRepository r;
		@Autowired
		TesteRepository testeRepository;
		@Autowired
		AppServices services;
		@Autowired
		ReportService reportServices;
		
		@Autowired
		TipoLancamentoRepository tipoLancamentoRepository;
		
		@GetMapping("/teste")
		public String teste(Model model) {
			LocalDate localDate = LocalDate.now();
		    for (int i=0; i < 7; i++) {
		    	localDate = localDate.plusDays(7);
		    	System.out.println("data:"+localDate);
		    }
		    
		    HashMap<String, BigDecimal> mapTotalizador = new HashMap<String, BigDecimal>();
		    
		    List<TipoLancamento> tiposLancamentos = tipoLancamentoRepository.findAllTipoLancamentos();
		    List<Lancamento> lancamentos = r.findAllLancamentosDoMes();
		 
		    BigDecimal totalizador = new BigDecimal(0);
		    
		    for (TipoLancamento tipoLancamento : tiposLancamentos) {
				
		    	for (Lancamento lancamento : lancamentos) {
		    		if (lancamento.getTipoLancamento().getCdTipoLancamento().equals(tipoLancamento.getCdTipoLancamento())) {
		    			totalizador = totalizador.add(lancamento.getVlPago());
		    		}
		    	}
		    	//Só adiciona o valor se ele for maior que zero
		    	if (totalizador.compareTo(BigDecimal.ZERO)==1) {
		    		mapTotalizador.put(tipoLancamento.getDsTipoLancamento(), totalizador);
		    	}
		    	totalizador = totalizador.ZERO;
			}
		    System.out.println(mapTotalizador.keySet());
		    System.out.println(mapTotalizador.values());
		
		    //C:\Users\elias\eclipse-workspace2021\fatura\src\main\resources\templates
		  
		    
			return "teste";
		}
		
		@GetMapping("/ds2")
		public String teste2() {
			
			return "home/dashboard2";
		}
		
		@GetMapping("/divide")
		public RedirectView divide() {
		
			RedirectView rw = new RedirectView("http://localhost/teste");
			return  rw;
		}
		
		@PostMapping("/teste/arquivo")
		public String arquivo(Model model, @RequestParam("file") MultipartFile file) {
		
		
		file.getOriginalFilename();
		   Teste teste = new Teste();
		   try {
			teste.setAnexo(file.getBytes());
			testeRepository.save(teste);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao tentar salvar a imagem no banco");
		}
		    model.addAttribute("teste", teste);
			return "teste";
		}
		
		@GetMapping("imagem/{id}")
		@ResponseBody
		public byte[] getImagem(@PathVariable Long id) {
			Optional<Teste> imagem = testeRepository.findById(1L);
			System.out.println("Exibindo a imamgem");
			return imagem.get().getAnexo();
		}
		
		@GetMapping("/getpdf")
		public String  gettPDF (Model model ) {
			System.out.println("Chamando o relatório");
			try {
				reportServices.exportReport("pdf","lancamento");
			} catch (FileNotFoundException | JRException e) {
				System.out.println(e.getStackTrace());
				
			}
			return "r_lancamentos";
		}
		
		@GetMapping("getpdf2")
		public String  gettPDF2 ( ) throws IOException {
			String path = "C:\\sysfatura\\relatorios\\teste.pdf";
			try {
			HtmlConverter.convertToPdf(new FileInputStream("src/main/resources/templates/listar.html"),
					new FileOutputStream(new File(path)));
			  System.out.println("Arquivo Criado com sucessos!");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return "r_lancamentos";
		}
			
			
		}

