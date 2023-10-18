package br.com.faturaweb.fatura.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.spi.FileSystemProvider;
import java.sql.Connection;
import java.sql.SQLException;
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

import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;

import org.apache.commons.collections4.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
import br.com.faturaweb.fatura.services.QueryServices;
import br.com.faturaweb.fatura.services.ReportService;
import br.com.faturaweb.fatura.utils.ExportFromQuery;
import br.com.faturaweb.fatura.utils.ExportToExcel;
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
	@Autowired
	Connection conn;
	@Autowired
	QueryServices queryServices;

	@GetMapping("/teste")
	public String apiltipolancnamento(Model model) throws SQLException {

//	   List<Lancamento> findAllLancamentosDoMes = lancamentoRepository.findAllLancamentosDoMes();
//		List<String> colunas = new ArrayList<>();
//		colunas.add("cd_lancamento");
//		colunas.add("ds_lancamento");
//		colunas.add("dt_cadastro");
//		colunas.add("competencia");
//		colunas.add("sn_pago");
//		colunas.add("vl_pago");
//		colunas.add("forma_de_pagamento");
//		colunas.add("tipo_lancamento");
//		colunas.add("cd_usuario");
//		colunas.add("nr_parcela");
//		colunas.add("observacao");
//		
//	   ExportToExcel exportToExcel = new ExportToExcel("texte", findAllLancamentosDoMes, colunas, "C:\\comprovantes");

		String sql = "SELECT                                                                          																"
				+ "	l.cd_lancamento,                                                           																	"
				+ "    l.ds_lancamento,                                                            																	"
				+ "    l.dt_cadastro,                                                              																		"
				+ "    l.dt_competencia,                                                          																	"
				+ "    l.sn_pago,                                                                  																		"
				+ "    l.vl_pago,                                                                 																		    "
				+ "    fp.descricao,                                                               																		"
				+ "    tl.ds_tipo_lancamento,                                                      																"
				+ "    u.nome,                                                                    																		    "
				+ "    nr_parcela,                                                                 																		"
				+ "    l.ds_anexo,                                                                 																		"
				+ "    l.observacao,                                                               																		"
				+ "    c.ds_cartao                                                                 																		"
				+ "FROM lancamento l,                                                              																"
				+ "	 forma_pagto fp,                                                            																	"
				+ "     tipo_lancamento tl,                                                        																	"
				+ "     cartao c,                                                                  																	 		"
				+ "     usuario u                                                                  																	 		"
				+ "where date_format(dt_competencia,'%m%Y') = (date_format(CURDATE(),'%m%Y' ))    		 		"
				+ "and l.forma_de_pagamento_cd_forma_pgamento = fp.cd_forma_pgamento              		 		"
				+ "and l.tipo_lancamento_cd_tipo_lancamento = tl.cd_tipo_lancamento                					 		"
				+ "and l.cartao_cd_cartao = c.cd_cartao                                            												 		"
				+ "and l.usuario_cd_usuario = u.cd_usuario                                         ";

				 String dirImportacao = config.findConfiguracao().getDirImportacao();
				 String diretorio = dirImportacao.concat("\\lancamentos_da_compentencia".concat("_"+String.valueOf(LocalDate.now().getMonthValue())).concat("_"+String.valueOf(LocalDate.now().getYear())).concat(".xlsx"));
				 System.out.println(diretorio);
				 ExportFromQuery export = new ExportFromQuery(conn,  diretorio,sql);

			System.out.println(queryServices.getLancamentosCompetencia("012023"));
		
		return "teste";

	}

	@GetMapping("/sem-ajax")
	public String semAjax(Model model) {
		List<TipoLancamento> tipos = tipoLancamentoRepository.findAllTipoLancamentos();
		model.addAttribute("tipos", tipos);
		return "teste";
	}

	@GetMapping("/com-ajax")
	public String comAjax(Model model) {
		List<TipoLancamento> tipos = tipoLancamentoRepository.findAllTipoLancamentos();
		model.addAttribute("tipos", tipos);

		return "detalhe";
	}

	@GetMapping("/compare")
	public String compare() {

		BigDecimal b1 = new BigDecimal(100); // =0 <> -1 a > b = 1 (a = b) = 0 a <> b = -1
		BigDecimal saque = new BigDecimal(-110);

		System.out.println("Resultado " + b1.compareTo(saque));
		System.out.println("Comparando com zero " + b1.compareTo(BigDecimal.ZERO));

		System.out.println("Origem :" + b1);
		System.out.println("Destino:  " + saque);

		if (saque.compareTo(BigDecimal.ZERO) == 1) {
			if (b1.compareTo(saque) > -1) {
				System.out.println("Debida");
			} else {
				System.err.println("Saldo insuficiente");
			}
		} else {
			System.err.println("Valor inválido");
		}

		return "teste";
	}

//	@GetMapping("/download")
//	public ResponseEntity<Object> download(HttpServletResponse response) {
//		System.out.println("teste");
//		ResponseEntity<Object> download = appServices.download("C:\\comprovantes\\texte.xlsx", response);
//		return download;
//	}
	
	@GetMapping("/download/{diretorio}")
	public ResponseEntity<Object> download(HttpServletResponse response, String diretorio) {
		ResponseEntity<Object> download = appServices.download(diretorio, response);
		return download;
	}

	@GetMapping("/delete")
	@ResponseBody
	public String delete() throws IOException {
		appServices.deletaArquivo("C:\\comprovantes\\texte.xlsx");
		return "sucesso!";
	}

}
