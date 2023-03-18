package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.FormaDePagamento;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.projection.AnoLancamentoProjection;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;

@Service
public class ExtratoLancamentoServices {
	@Autowired
	LancamentoRepository lctoRepository;
	@Autowired
	LancamentoServices lctoServices;
	@Autowired
	ContaRepository contaRepository;
	@Autowired
	ContaServices contaServices;
	@Autowired
	ConfiguracoesRepository configuracoesRepository;
	@Autowired
	FormaDePagamentoRepository formaPagtoRepository;
	@Autowired
	TipoLancamentoRepository tipoLancamentoRepository;

	/**
	 * Relatórios de Lançamento em tela
	 * 
	 * @author elias
	 * @since 04-03-2023
	 * @param mesAno
	 * @param anolancamento
	 * @model
	 */
	public void getExtratoLancamento(String mesAno, String anolancamento, String tprelatorio, Model model) {
		List<Lancamento> lctoFuturo = new ArrayList<>();
		HashMap<String, BigDecimal> totalDespesaFormaPagto = new HashMap<>();
		HashMap<String, BigDecimal> totalizacaoDespesaCategoria = new HashMap<>();
		BigDecimal totalLctoMes = BigDecimal.ZERO;
		String competencia = "";
		if (anolancamento == null)
			anolancamento = Integer.toString(LocalDate.now().getYear());
		if ("0".equals(mesAno))
			mesAno = "01"; // Se o mês não for informado aponta para o primeiro mês do ano
		mesAno = mesAno + anolancamento;
		List<FormaDePagamento> formaDePagamentos = formaPagtoRepository.findAllFormasDePagamento();
		// Busca os lancamento do ano informado quando o tipo de relatório for = 'A -  Anual'
		if ("A".equals(tprelatorio)) {
			lctoFuturo = lctoRepository.findLancamentosAnual(anolancamento);
			totalizacaoDespesaCategoria = lctoServices.totalizacaoDespesaCategoria(anolancamento);
			totalDespesaFormaPagto = lctoServices.getTotalizacaoDespesaFormaPagto(anolancamento);
			totalLctoMes = lctoServices.getTotalLctoMes(anolancamento);
		} else {
			lctoFuturo = lctoRepository.findLancamentosFuturos(mesAno);
			totalizacaoDespesaCategoria = lctoServices.totalizacaoDespesaCategoria(mesAno);
			totalDespesaFormaPagto = lctoServices.getTotalizacaoDespesaFormaPagto(mesAno);
			totalLctoMes = lctoServices.getTotalLctoMes(mesAno);
		}
		List<Conta> contas = contaRepository.findcontas();
		List<TipoLancamento> tiposLancamento = tipoLancamentoRepository.findAllTipoLancamentos();
		List<AnoLancamentoProjection> anosLancamento = lctoServices.getAnosLancamento();
		BigDecimal saldoGeral = contaServices.getSaldoGeral(contas);
		model.addAttribute("lcto", lctoFuturo);
		model.addAttribute("totalizacao", totalizacaoDespesaCategoria.keySet().toArray());
		model.addAttribute("valores", totalizacaoDespesaCategoria.values());
		model.addAttribute("keygrupodespesa", totalizacaoDespesaCategoria.keySet().toArray());
		model.addAttribute("grupovalues", totalizacaoDespesaCategoria.values());
		model.addAttribute("keytotalformapagto", totalDespesaFormaPagto.keySet().toArray());
		model.addAttribute("valorTotalformapagto", totalDespesaFormaPagto.values());
		model.addAttribute("saldoGeral", saldoGeral);
		model.addAttribute("formapagto", formaDePagamentos);
		model.addAttribute("tl", tiposLancamento);
		if ("A".equals(tprelatorio)) {
			competencia = " Relatório de Despesas Anuais  -  " + anolancamento;
		} else {
			competencia = " Relatório de Despesas Mensal  Competência - "
					+ mesAno.substring(0, 2).concat("/").concat(mesAno.substring(2, 6));
		}
		model.addAttribute("competencia", competencia);
		model.addAttribute("total", totalLctoMes);
		model.addAttribute("contas", contas);
		model.addAttribute("anosLancamento", anosLancamento);

		System.out.println(mesAno);

	}

	/**
	 * Retorna a logo do sistema
	 * 
	 * @author elias
	 * @since 04-03-2023
	 */
	public Configuracoes getLogo() {
		Configuracoes config = configuracoesRepository.findConfiguracao();
		return config;
	}

}
