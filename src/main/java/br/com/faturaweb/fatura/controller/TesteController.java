package br.com.faturaweb.fatura.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.faturaweb.fatura.model.TipoLancamento;
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
				 ExportFromQuery export = new ExportFromQuery(conn,  diretorio,sql);
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
