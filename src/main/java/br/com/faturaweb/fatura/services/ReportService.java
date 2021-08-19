package br.com.faturaweb.fatura.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.zxing.NotFoundException;

import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportService {

	@Autowired
	TipoLancamentoRepository lancamentoRepository;

	public String exportReport(String format) throws NotFoundException, FileNotFoundException, JRException {

		String path = "C:\\Users\\elias\\Desktop\\relatorio";
		List<TipoLancamento> tiposDeLancamento = lancamentoRepository.findAllTipoLancamentos();
		File file = ResourceUtils.getFile("classpath:reports/relTiposLancamento.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(tiposDeLancamento);
		Map<String, Object> parameters = new HashedMap();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

		if (format.equalsIgnoreCase("pdf")) {
			JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\relTipoLancamento.html");
		}

		if (format.equalsIgnoreCase("pdf")) {
			JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\relTipoLancamento.pdf");
		}

		return "Relatório Gerado com sucesso! " + path;
	}

	/**
	 * @author elias
	 * @param format         - Formado do relatório PDF ou HTML
	 * @param nomeRelatoirio - Nome do relatório sem a extenção
	 * @param dataSourceList - Lista de objetos
	 * @since 17/08/2021
	 */
	public String exportReport(String format, String nomeRelatoirio, JRBeanCollectionDataSource dataSourceList)
			throws NotFoundException, FileNotFoundException, JRException {

		String path = "C:\\fatura\\relatorio";
		System.out.println("Criei o relatorio");
		String relatorioPdf = nomeRelatoirio.concat(".pdf");
		String relatorioHtml = nomeRelatoirio.concat(".html");
		criaDiretorio();
		File file = ResourceUtils.getFile("classpath:reports/" + nomeRelatoirio.concat(".jrxml"));
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		Map<String, Object> parameters = new HashedMap();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSourceList);

		if (format.equalsIgnoreCase("html")) {
			JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\" + relatorioHtml);
			nomeRelatoirio = relatorioHtml;
		}

		if (format.equalsIgnoreCase("pdf")) {
			JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\" + relatorioPdf);
			nomeRelatoirio = relatorioPdf;
		}
		
		return nomeRelatoirio;
	}
	/**
	 * Cria estrutura de diretórios se não existir
	 * 
	 * @author elias
	 * @since 17/08/2021
	 */
	public void criaDiretorio() {

		File diretorios = new File("C:\\fatura\\relatorio");
		if (!diretorios.exists()) {
			diretorios.mkdirs();
		}

	}
	
}
