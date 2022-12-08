package br.com.faturaweb.fatura.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.FormaDePagamento;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.services.AppServices;
import br.com.faturaweb.fatura.services.ReportService;
import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping ("/relatorio")
public class ReportController {
@Autowired
private Connection connection;
@Autowired
ReportService services;

@Autowired
FormaDePagamentoRepository formaPagtoRepository;
@Autowired
TipoLancamentoRepository tipoLancamentoRepository;
@Autowired
AppServices appServices;

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
 * @param nmRelatorio - Nome do Relório
 * @param acao - Determina se o relatório deverá Abrir em Tela ou Fazer download do PDF (V -> Tela D-Download)
 * @param formapagto = Forma de Pagamento
 * @param tppagto - Tipo de Pagamento
 * @param periodoini - Início do Período
 * @param periodofim - Fim do período
 * @param competencia - A competêncai desejada no formado mmyyyy
 * @param nrLote - Número do lote
 * */
@GetMapping("/imprimir")
public void imprimir(
										@RequestParam(name="nmrelatorio") String  nmRelatorio,
										@RequestParam(name="acao") String  acao,
										@RequestParam (name = "formapagto", required = false)String formapagto,
										@RequestParam(name = "tppagto", required = false)String tppagto,
										@RequestParam(name = "periodoini", required = false) String periodoini,
										@RequestParam(name = "periodofim", required = false)String periodofim,
										@RequestParam(name = "competencia", required = false)String competencia,
										@RequestParam(name = "ordenacao", required = false)String ordenacao,
										@RequestParam(name = "lote", required = false)String nrLote,
										
										HttpServletResponse response
									) {	
									 DateTimeFormatter df = DateTimeFormatter.ofPattern("MM");
									 String strMes = LocalDate.now().format(df).toString();
									 String strAno = String.valueOf(LocalDate.now().getYear());
									
									 String orderBy = appServices.getOrdenacao(ordenacao);
									  appServices.imprmirRelatorio(nmRelatorio, strMes, strAno,orderBy, response,acao,formapagto,tppagto,periodoini,periodofim,competencia, nrLote);
									//  appServices.renomeiaArquivo("imprimir.pdf", nmRelatorio.concat(strAno).concat("-").concat(strMes).concat(".pdf"));
									  
}

@GetMapping("/telaimprimir")
public String telaImprimir(Model model) {	
	List<FormaDePagamento> formaDePagamentos = formaPagtoRepository.findAllFormasDePagamento();
	List<TipoLancamento> tiposLancamento = tipoLancamentoRepository.findAllTipoLancamentos();
	model.addAttribute("formapagto",formaDePagamentos);
	model.addAttribute("tl",tiposLancamento);
	return "imprimir";
}

}
