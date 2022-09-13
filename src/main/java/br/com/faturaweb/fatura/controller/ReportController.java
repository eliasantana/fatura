package br.com.faturaweb.fatura.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.services.ReportService;
import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping ("/relatorio")
public class ReportController {
@Autowired
private Connection connection;
@Autowired
ReportService services;

@GetMapping("/conexao")
public String report(Model model) {
	model.addAttribute("conexao",connection!=null?"ConexaoOK!":"Não Conectado");
	return "teste";
}
@GetMapping("/pdf")
public void abrirRelatorio(@RequestParam("code") String nmRelatorio, 
											 @RequestParam("acao") String acao,
											 HttpServletResponse response) {
	
	try {
		byte[] relPDF = services.exportarPDF(nmRelatorio);
		
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		if(acao.equals("v")) {
			//Abre no navegador
			response.setHeader("Content-disposition", "inline; file-name=relatorio-"+nmRelatorio+".pdf");
		}else {
			//Realiza o download
			response.setHeader("Content-disposition", "attachment; file-name=relatorio-"+nmRelatorio+".pdf");
		}
		try {
			response.getOutputStream().write(relPDF);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (JRException e) {
		e.printStackTrace();
	}
	
}

//Abrir relatório passando o filtro como parâmetro

@GetMapping("/filtro")
public String reportFiltro(Model model, 
											@RequestParam(name="competencia") String competencia,
											 HttpServletResponse response) {

		String nmRelatorio="lancamentos";
		String acao="v";
		System.out.println(competencia); 
		int ano = LocalDate.now().getYear();
	    String strAno = Integer.toString(ano);
	     
	    if (!competencia.equals("todos")) {
	    	services.addParam("COMPETENCIA", competencia.concat("/").concat(strAno));
	    	services.addParam("CF_COMPETENCIA",  competencia.concat(strAno));
	    	services.addParam("CF_WHERE", "AND CD_LANCAMENTO = CD_LANCAMENTO" );
	    }else {
	    	services.addParam("COMPETENCIA", competencia);
	    	services.addParam("CF_COMPETENCIA", " DATE_FORMAT(dt_competencia,'%m%Y') ");
	    	services.addParam("CF_WHERE", "AND CD_LANCAMENTO = CD_LANCAMENTO" );
	    }
	    
	   
	    
	    try {
	    	
			byte[] relPDF = services.exportarPDF(nmRelatorio);
			
			response.setContentType(MediaType.APPLICATION_PDF_VALUE);
			if(acao.equals("v")) {
				//Abre no navegador
				response.setHeader("Content-disposition", "inline; file-name=relatorio-"+nmRelatorio+".pdf");
			}else {
				//Realiza o download
				response.setHeader("Content-disposition", "attachment; file-name=relatorio-"+nmRelatorio+".pdf");
			}
			try {
				response.getOutputStream().write(relPDF);
				response.getOutputStream().flush();
				response.getOutputStream().close();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		
	return "teste";
}
/**
 * Fachada para impressão de relatórios
 * */
@GetMapping("/imprimir")
public void imprimir(
										@RequestParam(name="nmrelatorio") String  nmRelatorio,
										@RequestParam(name="acao") String  acao,
										HttpServletResponse response
									) {
	
	DateTimeFormatter df = DateTimeFormatter.ofPattern("MM");
	
	switch (nmRelatorio) {
		case  "relatoriodacompetencia":
			 	    nmRelatorio = "lancamentos";
			 	    
			 	   String strMes = LocalDate.now().format(df).toString();
			 	   String strAno = String.valueOf(LocalDate.now().getYear());
			 	  
			 	    services.addParam("COMPETENCIA", strMes.concat("/").concat(strAno));
			    	services.addParam("CF_COMPETENCIA",  strMes.concat(strAno));
			    	services.addParam("CF_WHERE", "AND CD_LANCAMENTO = CD_LANCAMENTO" );
				 	services.imprime(nmRelatorio, acao,response);
			break;
	
		default:
			break;
		}
	
}


}
