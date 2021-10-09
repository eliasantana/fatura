package br.com.faturaweb.fatura.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;

@Service
public class ReportService {

	@Autowired
	TipoLancamentoRepository lancamentoRepository;

	
	/**
	 * @author elias
	 * @param format         - Formado do relatório PDF ou HTML
	 * @param nomeRelatoirio - Nome do relatório sem a extenção
	 * @param dataSourceList - Lista de objetos
	 * @since 17/08/2021
	 */
	/*
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
	/*
	public void criaDiretorio() {

		File diretorios = new File("C:\\fatura\\relatorio");
		if (!diretorios.exists()) {
			diretorios.mkdirs();
		}

	}

	/**
	 * Realiza o downloado do arquivo de relatório gerado
	 * @param nomerelatorio - Nome do relatório sem extensão
	 * @return {@link ResponseEntity} 
	 * */
	/*
	public ResponseEntity download(String nomerelatorio) {
	
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
	*/
}
