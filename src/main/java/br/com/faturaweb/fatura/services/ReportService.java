package br.com.faturaweb.fatura.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ReportService {

	@Autowired
	Connection connection;
	
	@Autowired
	ConfiguracoesRepository configuracoesRepository;
	
	private  String JASPER_DIRETORIO="";
	private static  final String JASPER_PREFIXO= "rel-";
	private static  final String JASPER_SUFIXO= ".jasper";
	
	private Map<String, Object> params = new HashedMap();
	
	public void addParam(String key, Object value) {
		this.params.put(key, value);
	}
	
	public byte[] exportarPDF (String code) throws FileNotFoundException, JRException {
	Configuracoes config = configuracoesRepository.findConfiguracao();	
		JASPER_DIRETORIO = config.getDirRelatorio();
		byte[] bytes = null;
		File file = ResourceUtils.getFile(JASPER_DIRETORIO.concat(JASPER_PREFIXO).concat(code).concat(JASPER_SUFIXO));
		JasperPrint print = JasperFillManager.fillReport(file.getAbsolutePath(), params, connection);
		bytes = JasperExportManager.exportReportToPdf(print);
		return bytes;
	}

	/**
	 * Imprimir Relatório PDF
	 * @author elias
	 * @since 13-09-2022
	 * @param nmRelatorio - Nome do relatorio ou tipo
	 * @param acao = d - Download  v - Exibir no navagador
	 * @param response - Retorno da requisição
	 * */
	public void imprime(String nmRelatorio, String acao,  HttpServletResponse response) {
		
		try {
	    	
			byte[] relPDF = this.exportarPDF(nmRelatorio);
			
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
		
	}
/**
 * Formata a data de acordo com o formato informado
 * Ex: dd/mm/yyyy ddmmyyyy
 * @author elias
 * @since 13/09/2022
 * @param - {@link DateTimeFormatter}
 * @param formato
 * em teste
 * */
	public String  formatadata(LocalDate data, String formato) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern(formato.toUpperCase()); 
		String strData=null;
		try {
			strData = data.format(df).toString();
		} catch (Exception e) {
			System.out.println("Não foi possível converter para o formato informado");
		}
		
		return  strData;
		
	}

	
}
