package br.com.faturaweb.fatura.services;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import br.com.faturaweb.fatura.model.Chave;
import br.com.faturaweb.fatura.model.ChaveConfig;
import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.Lote;
import br.com.faturaweb.fatura.model.Receita;
import br.com.faturaweb.fatura.repository.CartaoRepository;
import br.com.faturaweb.fatura.repository.ChaveRepository;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;
import br.com.faturaweb.fatura.repository.MensageriaRepository;
import br.com.faturaweb.fatura.repository.ReceitaRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.repository.UsuarioRepository;

@Service
public class HomeSercices {
	@Autowired
	LancamentoRepository lancamentoRepository;
	@Autowired
	FormaDePagamentoRepository formaDePagamentoRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	TipoLancamentoRepository tipoLancamentoRepository;
	@Autowired
	ReceitaRepository receitaRepository;
	@Autowired
	CartaoRepository cartaoRepository;
	@Autowired
	ReceitaServices services;
	@Autowired
	LancamentoServices lancamentoServices;
	@Autowired
	ConfiguracoesRepository configuracoesRepository;
	@Autowired
	AppServices appservices;
	@Autowired
	MetaService metaservice;
	@Autowired
	MensageriaServices mensageriaservices;
	@Autowired
	MensageriaRepository mensageriaRepository;
	@Autowired
	LoteRepository loteRepository;
	@Autowired
	ChaveRepository chaveRepository;

	public String index(Model model) {
		String valorChave = "N";
		String tpGraficoFaturamento ="column";
		Optional<ChaveConfig> chave = chaveRepository
				.findChaveConfigByDescricao(Chave.SN_CAD_DESPESA_INICIO.toString());
		if (chave.isPresent()) {
			valorChave = chave.get().getValor().toString();
		}
		//Lanacamentos que compõe o gráfico
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
		Integer dias = configuracoesRepository.findConfiguracao().getNrDias();
		List<Lancamento> lancamentosVencidos = lancamentoRepository.findVencidos(dias);
		HashMap<String, BigDecimal> totalizacao = lancamentoServices.totalizacaoDespesaCategoria();
		String statusLote = "F"; // Status Fechado como Default
		// Formata data retorando apenas o mes ex: jan = 01
		DateTimeFormatter df = DateTimeFormatter.ofPattern("MM");
		DateTimeFormatter df2 = DateTimeFormatter.ofPattern("MMYYYY");
		String mesAno = LocalDateTime.now().format(df2).toString();

		BigDecimal limiteCartao = lancamentoServices.getLimiteCartao(mesAno, "Crédito");
		// Variáveis para acumular os valores pagos
		BigDecimal janeiro = new BigDecimal(0);
		BigDecimal fevereiro = new BigDecimal(0);
		BigDecimal marco = new BigDecimal(0);
		BigDecimal abril = new BigDecimal(0);
		BigDecimal maio = new BigDecimal(0);
		BigDecimal junho = new BigDecimal(0);
		BigDecimal julho = new BigDecimal(0);
		BigDecimal agosto = new BigDecimal(0);
		BigDecimal setembro = new BigDecimal(0);
		BigDecimal outubro = new BigDecimal(0);
		BigDecimal novembro = new BigDecimal(0);
		BigDecimal dezembro = new BigDecimal(0);

		// Interando sobre os lancamentos encontrados
		for (Lancamento lancamento : lancamentos) {
			LocalDate localDateFormat = lancamento.getDtCompetencia();
			String mes = lancamento.getDtCompetencia().format(df);
			// Verificando o mes e acumulando seu valor
			switch (mes) {
			case "01":
				janeiro = janeiro.add(lancamento.getVlPago().plus());
				break;
			case "02":
				fevereiro = fevereiro.add(lancamento.getVlPago());
				break;
			case "03":
				marco = marco.add(lancamento.getVlPago());
				break;
			case "04":
				abril = abril.add(lancamento.getVlPago());
				break;
			case "05":
				maio = maio.add(lancamento.getVlPago());
				break;
			case "06":
				junho = junho.add(lancamento.getVlPago());
				break;
			case "07":
				julho = julho.add(lancamento.getVlPago());
				break;
			case "08":
				agosto = agosto.add(lancamento.getVlPago());
				break;
			case "09":
				setembro = setembro.add(lancamento.getVlPago());
				break;
			case "10":
				outubro = outubro.add(lancamento.getVlPago());
				break;
			case "11":
				novembro = novembro.add(lancamento.getVlPago());
				break;
			case "12":
				dezembro = dezembro.add(lancamento.getVlPago());
				break;
			default:
				break;
			}

		}

		// Criando hash map e tribuindo o mes e seu valor
		Map<String, BigDecimal> dados = new LinkedHashMap<String, BigDecimal>();
		dados.put("Janeiro", janeiro);
		dados.put("Fevereiro", fevereiro);
		dados.put("Março", marco);
		dados.put("Abril", abril);
		dados.put("Maio", maio);
		dados.put("Junho", junho);
		dados.put("Julho", julho);
		dados.put("Agosto", agosto);
		dados.put("Setembro", setembro);
		dados.put("Outubro", outubro);
		dados.put("Novembro", novembro);
		dados.put("Dezembro", dezembro);

		Map<String, BigDecimal> receitas = services.totalizaReceita(receitaRepository.findaAllReceitaAnoCorrente());
		
		// Adicionando a view os valores acumuladods
		model.addAttribute("keyset", dados.keySet()); // Meses
		model.addAttribute("values", dados.values()); // Valores
		model.addAttribute("titulo", "Faturamento Mensal - SysFatura");// Titulo do Gráfico
		model.addAttribute("grafico", tpGraficoFaturamento); // Tipo do gráfico column - Gráfico de Colunas - bar - Gráfico de
													// Barras
		model.addAttribute("keysetreceitas", receitas.keySet());
		model.addAttribute("valuesreceitas", receitas.values());
		model.addAttribute("keygrupodespesa", totalizacao.keySet());
		model.addAttribute("grupovalues", totalizacao.values());
		model.addAttribute("ano", LocalDate.now().getYear());
		model.addAttribute("limite", "%");
		model.addAttribute("limitecartao", limiteCartao);
		StringBuilder sb = new StringBuilder();
		if (lancamentosVencidos.size() > 0) {
			for (Lancamento lancamento2 : lancamentosVencidos) {
				sb.append(lancamento2.getDsLancamento() + " - ");
			}
			model.addAttribute("mensagem", " Atenção! Você possui despesas não pagas! " + sb.toString());
		} else {
			model.addAttribute("mesagem", null);
		}
		Integer qtdMetasAtivas = metaservice.qtdMetasAtivas();
		model.addAttribute("metasativas", qtdMetasAtivas);
		List<Lancamento> findAllLancamentosDoMes = lancamentoRepository.findAllLancamentosDoMes();
		model.addAttribute("lctomes", findAllLancamentosDoMes.size());
		List<Receita> receitaMesCorrente = receitaRepository.findAllReceitaMesCorrente();
		model.addAttribute("receitamescorrente", receitaMesCorrente.size());
		model.addAttribute("status", statusLote);

		return valorChave;
	}

	/**
	 * Retorna os dados para a DashBoard
	 * 
	 * @author Kauan Mateus
	 * @since 2603/2023
	 * @param model
	 */
	public void dashBoard(Model model) {
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
		Integer dias = configuracoesRepository.findConfiguracao().getNrDias();
		List<Lancamento> lancamentosVencidos = lancamentoRepository.findVencidos(dias);
		HashMap<String, BigDecimal> totalizacao = lancamentoServices.totalizacaoDespesaCategoria();
		String statusLote = "F"; // Status Fechado como Default
		// Formata data retorando apenas o mes ex: jan = 01
		DateTimeFormatter df = DateTimeFormatter.ofPattern("MM");
		DateTimeFormatter df2 = DateTimeFormatter.ofPattern("MMYYYY");
		String mesAno = LocalDateTime.now().format(df2).toString();

		BigDecimal limiteCartao = lancamentoServices.getLimiteCartao(mesAno, "Crédito");
		// Variáveis para acumular os valores pagos
		BigDecimal janeiro = new BigDecimal(0);
		BigDecimal fevereiro = new BigDecimal(0);
		BigDecimal marco = new BigDecimal(0);
		BigDecimal abril = new BigDecimal(0);
		BigDecimal maio = new BigDecimal(0);
		BigDecimal junho = new BigDecimal(0);
		BigDecimal julho = new BigDecimal(0);
		BigDecimal agosto = new BigDecimal(0);
		BigDecimal setembro = new BigDecimal(0);
		BigDecimal outubro = new BigDecimal(0);
		BigDecimal novembro = new BigDecimal(0);
		BigDecimal dezembro = new BigDecimal(0);

		// Interando sobre os lancamentos encontrados
		for (Lancamento lancamento : lancamentos) {
			LocalDate localDateFormat = lancamento.getDtCompetencia();
			String mes = lancamento.getDtCompetencia().format(df);
			// Verificando o mes e acumulando seu valor
			switch (mes) {
			case "01":
				janeiro = janeiro.add(lancamento.getVlPago().plus());
				break;
			case "02":
				fevereiro = fevereiro.add(lancamento.getVlPago());
				break;
			case "03":
				marco = marco.add(lancamento.getVlPago());
				break;
			case "04":
				abril = abril.add(lancamento.getVlPago());
				break;
			case "05":
				maio = maio.add(lancamento.getVlPago());
				break;
			case "06":
				junho = junho.add(lancamento.getVlPago());
				break;
			case "07":
				julho = julho.add(lancamento.getVlPago());
				break;
			case "08":
				agosto = agosto.add(lancamento.getVlPago());
				break;
			case "09":
				setembro = setembro.add(lancamento.getVlPago());
				break;
			case "10":
				outubro = outubro.add(lancamento.getVlPago());
				break;
			case "11":
				novembro = novembro.add(lancamento.getVlPago());
				break;
			case "12":
				dezembro = dezembro.add(lancamento.getVlPago());
				break;
			default:
				break;
			}

		}

		// Criando hash map e tribuindo o mes e seu valor
		Map<String, BigDecimal> dados = new LinkedHashMap<String, BigDecimal>();
		dados.put("Janeiro", janeiro);
		dados.put("Fevereiro", fevereiro);
		dados.put("Março", marco);
		dados.put("Abril", abril);
		dados.put("Maio", maio);
		dados.put("Junho", junho);
		dados.put("Julho", julho);
		dados.put("Agosto", agosto);
		dados.put("Setembro", setembro);
		dados.put("Outubro", outubro);
		dados.put("Novembro", novembro);
		dados.put("Dezembro", dezembro);

		Map<String, BigDecimal> receitas = services.totalizaReceita(receitaRepository.findaAllReceitaAnoCorrente());

		// Adicionando a view os valores acumuladods
		model.addAttribute("keyset", dados.keySet()); // Meses
		model.addAttribute("values", dados.values()); // Valores
		model.addAttribute("titulo", "Faturamento Mensal - SysFatura");// Titulo do Gráfico
		model.addAttribute("grafico", "column"); // Tipo do gráfico column - Gráfico de Colunas - bar - Gráfico de
													// Barras
		model.addAttribute("keysetreceitas", receitas.keySet());
		model.addAttribute("valuesreceitas", receitas.values());
		model.addAttribute("keygrupodespesa", totalizacao.keySet());
		model.addAttribute("grupovalues", totalizacao.values());
		model.addAttribute("ano", LocalDate.now().getYear());
		model.addAttribute("limite", "%");
		model.addAttribute("limitecartao", limiteCartao);
		StringBuilder sb = new StringBuilder();
		if (lancamentosVencidos.size() > 0) {
			for (Lancamento lancamento2 : lancamentosVencidos) {
				sb.append(lancamento2.getDsLancamento() + " - ");
			}
			model.addAttribute("mensagem", " Atenção! Você possui despesas não pagas! " + sb.toString());
		} else {
			model.addAttribute("mesagem", null);
		}
		Integer qtdMetasAtivas = metaservice.qtdMetasAtivas();
		model.addAttribute("metasativas", qtdMetasAtivas);
		List<Lancamento> findAllLancamentosDoMes = lancamentoRepository.findAllLancamentosDoMes();
		model.addAttribute("lctomes", findAllLancamentosDoMes.size());
		List<Receita> receitaMesCorrente = receitaRepository.findAllReceitaMesCorrente();
		model.addAttribute("receitamescorrente", receitaMesCorrente.size());
		model.addAttribute("status", statusLote);

	}

	/* TODO: MOVER MÉTODO PRA O CONTROLER LANCAMENTO */
	/**
	 * Lista todos os lancamentos e verifica o Status do Lote
	 * 
	 * @since 26-03-2023
	 * @author Kauan Mateus
	 * @param model
	 */
	public void listar(Model model) {
		String status = "A";
		List<Lancamento> lancamentos = new ArrayList<>();
		BigDecimal totalCredito = BigDecimal.ZERO;
		BigDecimal totalDebito = BigDecimal.ZERO;
		BigDecimal totalDinheiro = BigDecimal.ZERO;
		String proximacompetencia = null;

		Optional<ChaveConfig> proximaCompetencia = chaveRepository.findChaveConfigByDescricao("SN_PROXIMA_COMPETENCIA");
		Optional<ChaveConfig> snFixaLancamento = chaveRepository.findChaveConfigByDescricao("FIXA_LANCAMENTO");
		 				
		if (proximaCompetencia.isPresent()) {
		     if (proximaCompetencia.get().getValor().equals("S")) {
		    	 proximacompetencia = "S";
		    	 DateTimeFormatter df2 = DateTimeFormatter.ofPattern("MMYYYY");
		    	 String mesAno = LocalDate.now().plusMonths(1).format(df2).toString();			
		    	 lancamentos = lancamentoRepository.findAllLancamentosDoMes(mesAno);
		    	 Collections.sort(lancamentos);
		     }else {
		    	 proximacompetencia="N";		    	 
		    	 lancamentos = lancamentoRepository.findAllLancamentosDoMes();		    	 			
		    	Collections.sort(lancamentos);
		     }
		}else {
			lancamentos = lancamentoRepository.findAllLancamentosDoMes();
		}
		
		//Filtrando e totalizando despesas no Cartão de Crédito
		List<Lancamento> lctoCredito =  lancamentos.stream()
		.filter((Predicate<? super Lancamento>) l -> l.getFormaDePagamento().getDescricao().equals("Crédito"))
		.collect(Collectors.toList());
		
		totalCredito = lctoCredito.stream()
		.map(Lancamento::getVlPago)
		.reduce(BigDecimal.ZERO, BigDecimal::add);

		//Filtrando e Totalizando despesas no Débito
		List<Lancamento> lctoDebito =  lancamentos.stream()
		.filter((Predicate<? super Lancamento>) l -> l.getFormaDePagamento().getDescricao().equals("Débito"))
		.collect(Collectors.toList());
		
		totalDebito = lctoDebito.stream()
	 	.map(Lancamento::getVlPago)
	 	.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		//Filtrando e Totalizando despesas no Débito
		List<Lancamento> lctoDinheiro =  lancamentos.stream()
		.filter((Predicate<? super Lancamento>) l -> l.getFormaDePagamento().getDescricao().equals("Dinheiro"))
		.collect(Collectors.toList());
		
		BigDecimal totalGeral =  BigDecimal.ZERO;
		totalGeral = totalGeral.add(totalCredito.add(totalDebito)).add(totalDinheiro);
		
		totalDinheiro = lctoDinheiro.stream()
	 	.map(Lancamento::getVlPago)
	 	.reduce(BigDecimal.ZERO, BigDecimal::add);	
		
		model.addAttribute("totaldinheiro", totalDinheiro);
		model.addAttribute("totaldebito", totalDebito);
		model.addAttribute("totalcredito", totalCredito);
		model.addAttribute("totalgeral", totalGeral);
		if (snFixaLancamento.isPresent()) {
			      String valor = snFixaLancamento.get().getValor();
			      switch (valor) {
				case "D":
					lancamentos = lctoDebito;
					break;
				case "C":
					lancamentos = lctoCredito;
					break;
				case "DI":
					lancamentos = lctoDinheiro;
					break;
				default:
					break;
				}
		}	
		model.addAttribute("lancamentos", lancamentos);
		// Verifica lote da competencia
		boolean existeLctoAberto = appservices.isLancamentoAberto(lancamentos);
		try {
			Lote findLoteCompetencia = loteRepository.findLoteCompetencia();
			if (appservices.verificalote("F", findLoteCompetencia) && !existeLctoAberto) {
				status = findLoteCompetencia.getStatus();
			}
		} catch (Exception e) {
			if (existeLctoAberto) {
				status = "F";
			}
			status = "A";
		}
		model.addAttribute("status", status);
		model.addAttribute("proximacompetencia", proximacompetencia);
	}

	/**
	 * @author Kauan Mateus
	 * @since 26/32023
	 * @param model
	 */
	public void getConfiguracoes(Model model) {
		Configuracoes config = configuracoesRepository.findConfiguracao();
		model.addAttribute("config", config);
	}

	/**
	 * @author Kauan Mateus
	 * @since 26/32023
	 * @param model
	 */
	public Configuracoes getConfiguracoes() {
		Configuracoes config = configuracoesRepository.findConfiguracao();
		return config;
	}

	/**
	 * @author Kauan Mateus
	 * @since 26/03/2023 Envia um e-mail de Teste
	 */
	public void emailTeste() {
		Configuracoes config = configuracoesRepository.findConfiguracao();
		System.out.println("enviando e-mail");
		try {
			StringBuilder assunto = new StringBuilder();
			assunto.append("Teste de Envio:\n");
			assunto.append("Teste de Envio de mensagem\n");
			assunto.append("Vencimento:" + "12/12/2021\n");
			assunto.append("Valor:");
			assunto.append("0,00");
			appservices.sendEmai(config.getEmailOrigem(), config.getNmOrigem(), config.getEmailDestino(),
					config.getNmDestino(), config.getTituloMsgEmailDestino(), assunto);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Retorna o tipo de DashBoard
	 * */
	public String getTpDashBoard(String nomeChave) {
		String chaveConfig2 = appservices.getValorChave(nomeChave);
		return chaveConfig2;
	}

	 
}
