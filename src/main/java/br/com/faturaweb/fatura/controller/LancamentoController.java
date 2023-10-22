package br.com.faturaweb.fatura.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.form.LancamentoForm;
import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.services.AppServices;
import br.com.faturaweb.fatura.services.LancamentoServices;
import br.com.faturaweb.fatura.services.QueryServices;

@Controller
@EnableAutoConfiguration
@RequestMapping("/lancamento")

public class LancamentoController {

	@Autowired
	LancamentoServices services;
	@Autowired
	AppServices appServices;
	
	/**
	 * Só libera o cadastro quando sn_lancarNaCompetencia='N' das configuações
	 * Gerais habilitado
	 */
	@GetMapping("/cadastro")
	public String cadastrar(Model model) {
		services.cadastro(model);
		return "lancamento/form-lancamento";
	}

//Método chamado através da página de alteação de lancamento
	@PostMapping("/salvar")
	public String salvar(LancamentoForm lancamentoForm, Model model) {
		services.salvar(lancamentoForm, model);
		return "home/listar-lancamento";
	}

	@GetMapping("/excluir/{id}")
	public RedirectView excluir(@PathVariable Long id, Model model) {
		RedirectView rw = services.excluir(id, model);
		return rw;

	}

	@GetMapping("/alterar/{id}")
	public String alterar(@PathVariable Long id, Model model) {
		services.alterar(id, model);
		return "home/form-lancamento";
	}

	@GetMapping("/pagar/{id}")
	public RedirectView pagar(@PathVariable Long id, Model model) {
		RedirectView rw = services.pagar(id, model);
		return rw;
	}

	@GetMapping("anexar/{id}")
	public String anexar(@PathVariable Long id, Model model) {
		services.anexar(id, model);
		return "home/form-lancamento-anexo";
	}

	@GetMapping("anexo/{id}")
	public ResponseEntity<Object> exibirAnexo(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		Lancamento lancamento = services.getLancamento(id);
		return appServices.download(lancamento.getDsAnexo(), response);
	}

	@GetMapping("/detalhe/{id}")
	public String deTalheLancamento(Model model, @PathVariable Long id) {
		services.getDetalheLancamento(model, id);
		return "home/detalhe-lancamento";
	}

	@GetMapping("/getimagem")
	@ResponseBody
	public byte[] getlogo() {
		Configuracoes config = services.getLogoSistema();
		return config.getLogo();
	}

	@GetMapping("/transferir/{id}")
	public RedirectView transferir(@PathVariable Long id) {
		RedirectView rw = services.transferir(id);
		return rw;
	}

	@GetMapping("/pagartodos")
	public RedirectView pagarTodos() {
		RedirectView rw = services.pagarTodos();
		return rw;
	}

	@GetMapping({"/relxlscompetencia","/relxlscompetencia/{mesano}"})
	public ResponseEntity<Object> geraRelatorioMovimentacaoXls(@PathVariable(name = "mesano", required = false) String mesano,HttpServletResponse response) throws SQLException, IOException {
		String competencia=mesano;
		String arquivo = services.geraRelatorioXls(competencia);		
		ResponseEntity<Object> download = appServices.download(arquivo, response);	
		return download;
	}
}
