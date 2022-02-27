package br.com.faturaweb.fatura.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
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
	TipoLancamentoRepository tipolancamentoRepository;

	@Autowired
	LancamentoRepository lancamentoRepository;

	public String exportReport(String formato, String relatorio) throws FileNotFoundException, JRException {
		List<Lancamento> todosOsLancamentos = lancamentoRepository.findAllLancamentos();
		File file =new File("src\\resources\\r_lancamentos.jrxml");
		System.out.println(file.getAbsolutePath());
	   JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
	   JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(todosOsLancamentos);
	   Map<String, Object> map = new HashMap<String, Object>();
	   JasperPrint print = JasperFillManager.fillReport(jasperReport, map,dataSource);	   
	   JasperExportManager.exportReportToPdfFile(file.getParent().toString(), "C:\sysfatura\\relatorios\\"+"teste.pdf");
	   
		System.out.println(file.getAbsolutePath());
		return "teste";
	}
}
