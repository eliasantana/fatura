package br.com.faturaweb.fatura.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.form.LancamentoForm;
import br.com.faturaweb.fatura.model.Cartao;
import br.com.faturaweb.fatura.model.Chave;
import br.com.faturaweb.fatura.model.ChaveConfig;
import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.FormaDePagamento;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.model.Lote;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.projection.AnoLancamentoProjection;
import br.com.faturaweb.fatura.repository.CartaoRepository;
import br.com.faturaweb.fatura.repository.ChaveRepository;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.repository.UsuarioRepository;

@Service
public class LancamentoServices {
	@Autowired
	LancamentoRepository lancamentoRepository;
	@Autowired
	TipoLancamentoRepository tipoLancamentoRepository;
	@Autowired
	FormaDePagamentoRepository formaPagtoRepository;
	@Autowired
	ConfiguracoesRepository configuracaoRepository;
	@Autowired
	ChaveRepository chaveRepository;
	@Autowired
	ConfiguracoesRepository configuracoesRepository;
	@Autowired
	CartaoRepository cartaoRepository;
	@Autowired
	FormaDePagamentoRepository formaDePagamentoRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	LoteRepository loteRepository;
	@Autowired
	ContaRepository contaRepository;
	@Autowired
	LogMovimentacaoFinanceiraRepository logRepository;

	public List<Lancamento> parcelar(String snParcelar, Long cdUsuario, Integer qtParcela,
			String primeiraParcelaNaCompetencia) {
		BigDecimal vlPago = new BigDecimal(0);
		Integer nrParcela = 0;
		BigDecimal vlParcela = new BigDecimal(0);
		MathContext mctx = new MathContext(2, RoundingMode.HALF_UP);
		List<Lancamento> listaDeLancamentos = new ArrayList<Lancamento>();
		if (snParcelar.toLowerCase().equals("s")) {
			System.out.println("Localizando o último lancamento do usuário!");
			Lancamento lancamento = lancamentoRepository.findUltimoLancamentoUsuario(cdUsuario);
			vlPago = lancamento.getVlPago();
			nrParcela = qtParcela;
			vlParcela = (vlPago.divide(new BigDecimal(nrParcela), MathContext.DECIMAL32));
			// Adicona a primeira parcela para o mês seguinte
			for (int i = 1; i <= nrParcela; i++) {
				Lancamento l = new Lancamento();
				l.setDsLancamento(lancamento.getDsLancamento() + " " + (i) + "/" + nrParcela);
				l.setDtCadastro(lancamento.getDtCadastro());
				l.setCartao(lancamento.getCartao());
				// Lança a primeira parcela na competencia atual somente se a configuração
				// global estiver ligada sn_nacompetencia = 'S'
				if (primeiraParcelaNaCompetencia.equals("S")) {
					l.setDtCompetencia(lancamento.getDtCompetencia().plusMonths(i - 1)); // lança a parcela no mês atual
				} else {
					l.setDtCompetencia(LocalDate.now());
					l.setDtCompetencia(lancamento.getDtCompetencia().plusMonths(i)); // lança a primeira parcela no mês
																						// seguinte
				}
				l.setFormaDePagamento(lancamento.getFormaDePagamento());
				l.setNrParcela(i + 1);
				l.setSnPago(lancamento.getSnPago());
				l.setTipoLancamento(lancamento.getTipoLancamento());
				l.setUsuario(lancamento.getUsuario());
				l.setVlPago(vlParcela);
				l.setObservacao(lancamento.getObservacao());

				listaDeLancamentos.add(l);
			}
		}
		return listaDeLancamentos;
	}

	/**
	 * Retorna os laçamentos do mês atual totalizados por Tipo, Utilizando em:
	 * Extrato de Pagamento, index Obs:Fornece dados para os gráficos
	 * 
	 * @author elias
	 * @since 08-02-2021
	 * @return {@link HashMap}
	 */
	public HashMap<String, BigDecimal> totalizacaoDespesaCategoria() {
		HashMap<String, BigDecimal> mapTotalizador = new HashMap<String, BigDecimal>();

		List<TipoLancamento> tiposLancamentos = tipoLancamentoRepository.findAllTipoLancamentos();
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentosDoMes();

		BigDecimal totalizador = new BigDecimal(0);

		for (TipoLancamento tipoLancamento : tiposLancamentos) {

			for (Lancamento lancamento : lancamentos) {
				if (lancamento.getTipoLancamento().getCdTipoLancamento().equals(tipoLancamento.getCdTipoLancamento())) {
					totalizador = totalizador.add(lancamento.getVlPago());
				}
			}
			// Só adiciona o valor se ele for maior que zero
			if (totalizador.compareTo(BigDecimal.ZERO) == 1) {
				mapTotalizador.put(tipoLancamento.getDsTipoLancamento(), totalizador);
			}
			totalizador = totalizador.ZERO;
		}

		return mapTotalizador;
	}

	/**
	 * Retorna os laçamentos do mês atual totalizados por Tipo
	 * 
	 * @author elias
	 * @since 08-02-2021
	 * @return {@link HashMap}
	 */
	public HashMap<String, BigDecimal> totalizacaoDespesaCategoria(String mesAno) {
		HashMap<String, BigDecimal> mapTotalizador = new HashMap<String, BigDecimal>();
		List<Lancamento> lancamentos = new ArrayList<>();		
		List<TipoLancamento> tiposLancamentos = tipoLancamentoRepository.findAllTipoLancamentos();
		//Se mes ano for menor que 6 dígitos. Busca os lançamentos do ano
		if (mesAno.length()<6) {
			lancamentos = lancamentoRepository.findAllLancamentosDoAno(mesAno);			
		}else {
			lancamentos = lancamentoRepository.findAllLancamentosDoMes(mesAno);			
		}

		BigDecimal totalizador = new BigDecimal(0);

		for (TipoLancamento tipoLancamento : tiposLancamentos) {

			for (Lancamento lancamento : lancamentos) {
				if (lancamento.getTipoLancamento().getCdTipoLancamento().equals(tipoLancamento.getCdTipoLancamento())) {
					totalizador = totalizador.add(lancamento.getVlPago());
				}
			}
			// Só adiciona o valor se ele for maior que zero
			if (totalizador.compareTo(BigDecimal.ZERO) == 1) {
				mapTotalizador.put(tipoLancamento.getDsTipoLancamento(), totalizador);
			}
			totalizador = totalizador.ZERO;
		}

		return mapTotalizador;
	}

	/**
	 * Retorna a totalização das despesas do mês corrrente
	 * 
	 * @author elias
	 * @since 01/04/2022
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getTotalLctoMes(String mesAno) {
		BigDecimal total = BigDecimal.ZERO;
		List<Lancamento> lctoDoMes = new ArrayList<>();
		if(mesAno.length() < 6) {
			lctoDoMes = lancamentoRepository.findAllLancamentosDoAno(mesAno);
		}else {
			lctoDoMes = lancamentoRepository.findAllLancamentosDoMes(mesAno);
		}
		for (int i = 0; i < lctoDoMes.size(); i++) {
			total = total.add(lctoDoMes.get(i).getVlPago());
		}
		return total;
	}

	public String getTotal() {
		HashMap<String, BigDecimal> totalizacaoDespesaCategoria = totalizacaoDespesaCategoria();

		Set<String> keySet = totalizacaoDespesaCategoria.keySet();
		java.util.Collection<BigDecimal> values = totalizacaoDespesaCategoria.values();
		Iterator i = keySet.iterator();
		Iterator<BigDecimal> ivalues = values.iterator();
		String str = new String();
		while (i.hasNext()) {
			str = str + "{ name : ' " + i.next() + " ' , y: " + ivalues.next() + " },";
		}

		str = " [ " + str.substring(0, str.length() - 1) + " ]";

		return str;
	}

	/**
	 * Retorna a totalização dos lançamentos por Forma de Pagamento
	 * 
	 * @since 16/06/2022
	 * @author elias.silva
	 * @param mesAno - Mes ano mm/AAAA
	 * @return {@link HashMap} - HashMap <String, BigDecimal>
	 */
	public HashMap<String, BigDecimal> getTotalizacaoDespesaFormaPagto(String mesAno) {

		HashMap<String, BigDecimal> hashTotalizacao = new HashMap<String, BigDecimal>();
		List<FormaDePagamento> formasDePagamento = formaPagtoRepository.findAllFormasDePagamento();
		List<Lancamento> lancamentosDoMes = new ArrayList<>();
		if (mesAno.length()<6) {
			lancamentosDoMes = lancamentoRepository.findAllLancamentosDoAno(mesAno);
		}else {
			lancamentosDoMes = lancamentoRepository.findAllLancamentosDoMes(mesAno);
		}
		BigDecimal total = BigDecimal.ZERO;

		for (FormaDePagamento pagamentos : formasDePagamento) {
			for (Lancamento lancamento : lancamentosDoMes) {
				String pagtoDescricao = pagamentos.getDescricao();
				String descricao = lancamento.getFormaDePagamento().getDescricao();
				if (pagtoDescricao.equals(lancamento.getFormaDePagamento().getDescricao())) {
					total = total.add(lancamento.getVlPago());
				}
			}
			if (total.compareTo(BigDecimal.ZERO) == 1) {
				hashTotalizacao.put(pagamentos.getDescricao(), total);
			}
			total = BigDecimal.ZERO;
		}
		return hashTotalizacao;
	}

	/**
	 * Retorna o percentual de gasto de a cordo com a forma de pagamento informada
	 * 
	 * @since 22/06/2022
	 * @author elias.silva
	 * @param mesAno           - Mes ano mm/AAAA
	 * @param formaDePAgamento - Cartão, Dinheiro etc
	 * @return {@link BigDecimal} - Percentual de Gasto
	 */
	public BigDecimal getLimiteCartao(String mesAno, String formaDePAgamento) {
		Configuracoes configuracao = configuracaoRepository.findConfiguracao();
		BigDecimal limite = configuracao.getLimiteCartao();
		BigDecimal percent = BigDecimal.ZERO;
		MathContext mtx = new MathContext(2, RoundingMode.HALF_UP);
		// Limite 1000 gasto de 1200
		List<Lancamento> lancamentosDoMes = lancamentoRepository.findAllLancamentosDoMes(mesAno);
		BigDecimal totalGasto = BigDecimal.ZERO;
		for (Lancamento lancamento : lancamentosDoMes) {
			if (formaDePAgamento.equals(lancamento.getFormaDePagamento().getDescricao())) {
				totalGasto = totalGasto.add(lancamento.getVlPago());
			}
		}
		System.out.println("Total Gasto: " + totalGasto);
		// Se o total for menor que o limite
		if (totalGasto.compareTo(limite) == -1) {
			System.out.println("Entrei aqui");
			percent = (totalGasto.divide(limite, 3, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(100.0)));
		} else {
			// Retorna o percentual negativo quando o totalGasto for maior que o limite
			BigDecimal diferenca = totalGasto.subtract(limite);
			percent = (diferenca.divide(limite, 3, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(100.0))
					.multiply(BigDecimal.valueOf(-1)));
		}
		System.out.println("Percentual Calcualdo " + percent);
		System.out.println("Limite " + limite);
		return percent;
	}

	/**
	 * Valida o lote e o lancamento, permitindo apenas lançamento com data máxima
	 * retroativa a um mês. Caso o lote esteja fechado o lançamento será adicionado
	 * na próxma competência.
	 * 
	 * @author elias
	 * @param lancamento     - Lançamento
	 * @param loteRepository - LoteRepository
	 * @return {@link Lancamento} - Lançamento com nova Competência.
	 */
	public Lancamento validaLoteLancamento(Lancamento lancamento, LoteRepository loteRepository) {
		try {
			Lote loteCompetencia = loteRepository.findLoteCompetencia();
			if (loteCompetencia.getStatus().equals("A")) {
				LocalDate mesAtual = LocalDate.now();
				LocalDate mesLancamento = lancamento.getDtCompetencia();
				int total = mesAtual.getMonthValue() - mesLancamento.getMonthValue();
				if (total > 1) {
					lancamento.setDtCompetencia(LocalDate.now().minusMonths(1));
				}
			} else {
				lancamento.setDtCompetencia(LocalDate.now());
			}

		} catch (Exception e) {
			lancamento.setDtCompetencia(LocalDate.now());
		}
		return lancamento;
	}

	/**
	 * Altera todos os lancamentos se existirem
	 * 
	 * @since 19/11/2022
	 * @author elias
	 * @param lancamentoForm       - Lançamento com as alterações da página
	 * @param lancamentoLocalizado - Lancamento Original Localizado
	 */
	public void alterarTodos(LancamentoForm lancamentoForm, Lancamento lancamentoLocalizado) {

		System.out.println(lancamentoLocalizado.getDsLancamento());
		int length = lancamentoLocalizado.getDsLancamento().length();
		String dsLancamento = lancamentoLocalizado.getDsLancamento().substring(0, length - 4);

		BigDecimal vlPago = lancamentoLocalizado.getVlPago();
		List<Lancamento> listaDemaisLancamentos = lancamentoRepository.findDemiasLancamento(dsLancamento, vlPago);
		List<Lancamento> listaLancamentosAlterarados = new ArrayList<Lancamento>();
		if (listaDemaisLancamentos.size() > 1) {
			for (Lancamento lancamento2 : listaDemaisLancamentos) {
				String controle = lancamento2.getDsLancamento().substring(length - 4);

				Lancamento lancamentoAlterado = new Lancamento();
				lancamentoAlterado = lancamento2;
				lancamentoAlterado.setDsLancamento(lancamentoForm.getDsLancamento() + " " + controle);
				Optional<FormaDePagamento> formaPagtoLocalizado = formaPagtoRepository
						.findByDescricaoFormaDePagamento(lancamentoForm.getDsFormaDePagamento());
				lancamentoAlterado.setFormaDePagamento(formaPagtoLocalizado.get());
				Optional<TipoLancamento> tipoLancamentoLocalizado = tipoLancamentoRepository
						.findBydsTipoLancamento(lancamentoForm.getDsTipoLancamento());
				lancamentoAlterado.setTipoLancamento(tipoLancamentoLocalizado.get());
				lancamentoAlterado.setSnPago(lancamentoForm.getSnPago());
				lancamentoAlterado.setVlPago(lancamentoForm.getVlPago());
				lancamentoAlterado.setObservacao(lancamentoForm.getDsLancamento());
				listaLancamentosAlterarados.add(lancamentoAlterado);

			}
			lancamentoRepository.saveAll(listaDemaisLancamentos);
		}

	}

	/**
	 * Retorna os anos que possuem Lançamentos
	 * 
	 * @author elias
	 * @since 24/12/2022 Utilizado: Extrato de Pagamento
	 */
	public List<AnoLancamentoProjection> getAnosLancamento() {
		List<AnoLancamentoProjection> findAnosDeLancamento = lancamentoRepository.findAnosDeLancamento();
		return findAnosDeLancamento;
	}

	public void cadastro(Model model) {
		// Obtem o valor da chave
		Optional<ChaveConfig> chave = chaveRepository
				.findChaveConfigByDescricao(Chave.SN_CAD_DESPESA_INICIO.toString());
		String valorChave = null;
		if (chave.isPresent()) {
			valorChave = chave.get().getValor();
		}

		String status = "A";
		String msg = "";
		try {
			status = loteRepository.findLoteCompetencia().getStatus();
			Configuracoes config = configuracoesRepository.findConfiguracao();
			if (status.equals("F") && config.getSnNaCompetencia().equals("S")) {
				msg = "Lote fechado! Não é possível lancar Despesa nesta Competência!";
			} else {
				status = "A";
			}
		} catch (Exception e) {
			System.out.println("Não há lote aberto na competencia!");
		}
		try {
			List<Cartao> listaCartoes = cartaoRepository.findAllCartoes();
			Lancamento lancamento = new Lancamento();
			LancamentoForm lf = new LancamentoForm();
			Usuario u = new Usuario();
			Optional<Usuario> usuario = usuarioRepository.findById(5L);
			List<TipoLancamento> tiposDeLancamento = tipoLancamentoRepository.findAllTipoLancamentos();

			List<FormaDePagamento> formasDePagamento = formaDePagamentoRepository.findAllFormasDePagamento();

			model.addAttribute("lancamentos", lf);
			model.addAttribute("formapagto", formasDePagamento);
			model.addAttribute("tpLancamentos", tiposDeLancamento);
			model.addAttribute("usuario", usuario.get());
			model.addAttribute("status", status);
			model.addAttribute("menssagem", msg);
			model.addAttribute("cartao", listaCartoes);
			model.addAttribute("chave", valorChave);

		} catch (Exception e) {
			e.getMessage();
		}

	}

	/**
	 * Salva um lancamento
	 * 
	 * @autor
	 * @since
	 * @param lancamentoForm
	 * @param model
	 */
	public void salvar(LancamentoForm lancamentoForm, Model model) {
		Optional<FormaDePagamento> findByDescricaoFormaDePagamento = formaDePagamentoRepository
				.findByDescricaoFormaDePagamento(lancamentoForm.getDsFormaDePagamento());
		FormaDePagamento formadepagamento = findByDescricaoFormaDePagamento.get();
		Configuracoes config = configuracoesRepository.findConfiguracao();

		Optional<Lancamento> lancamentoAnterior = lancamentoRepository.findById(lancamentoForm.getCdLancamento());
		Optional<Lancamento> lancamentoLocalizado = lancamentoRepository.findById(lancamentoForm.getCdLancamento());

		Lancamento lancamento = lancamentoLocalizado.get();
		Optional<TipoLancamento> findBydsTipoLancamento = tipoLancamentoRepository
				.findBydsTipoLancamento(lancamentoForm.getDsTipoLancamento());
		TipoLancamento tipoLancamento = findBydsTipoLancamento.get();
		Optional<Usuario> usuario = usuarioRepository.findById(5L);
		Optional<Cartao> cartaoLocalizado = cartaoRepository.findBydsCartao(lancamentoForm.getDsCartao());
		// Se nenhum cartao for selecionado será aplicado o cartão padrão
		if (cartaoLocalizado.isPresent()) {
			lancamento.setCartao(cartaoLocalizado.get());
		} else {
			Optional<Cartao> cartaoPadrao = cartaoRepository.findBydsCartao("nenhum");
			lancamento.setCartao(cartaoPadrao.get());
		}
		lancamento.setCdLancamento(lancamentoForm.getCdLancamento());
		lancamento.setDsLancamento(lancamentoForm.getDsLancamento());
		lancamento.setDtCadastro(lancamentoForm.getDtCadastro());
		lancamento.setDtCompetencia(lancamentoForm.getDtCompetencia());
		lancamento.setFormaDePagamento(formadepagamento);
		lancamento.setSnPago(lancamentoForm.getSnPago());
		lancamento.setTipoLancamento(tipoLancamento);
		lancamento.setUsuario(usuario.get());
		lancamento.setVlPago(lancamentoForm.getVlPago());
		lancamento.setObservacao(lancamentoForm.getObservacao());
		lancamentoRepository.save(lancamento);
		Lancamento findUltimoLancamentoUsuario = lancamentoRepository
				.findUltimoLancamentoUsuario(usuario.get().getCdUsuario());
		System.out.println("Ultimo lancamento Localizado: " + findUltimoLancamentoUsuario.toString());

		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentosDoMes();
		model.addAttribute("lancamentos", lancamentos);

	}

	/**
	 * Exclui um lancamento
	 * 
	 * @since 04-03-2023
	 * @param id
	 * @param model
	 */
	public RedirectView excluir(Long id, Model model) {
		List<Lancamento> lancamentos = lancamentoRepository.findLancamentoMesSeguinte();
		model.addAttribute("lancamentos", lancamentos);
		System.out.println("Excluindo lançamentos");
		Optional<Lancamento> lancamentoLocalizado = lancamentoRepository.findById(id);
		lancamentoRepository.delete(lancamentoLocalizado.get());
		System.out.println("Lançamento Excluído com sucesso!");
		RedirectView rw = new RedirectView("/listar");
		return rw;
	}

	/**
	 * Altera um lancamento
	 * 
	 * @since 04-03-2023
	 * @param id
	 * @param model
	 */
	public void alterar(Long id, Model model) {
		Lancamento lancamento = lancamentoRepository.findByIdLancamento(id);

		Optional<FormaDePagamento> formaDePagamento = formaDePagamentoRepository
				.findById(lancamento.getFormaDePagamento().getCdFormaPgamento());

		Optional<TipoLancamento> findBydsTipoLancamento = tipoLancamentoRepository
				.findById(lancamento.getCdLancamento());

		TipoLancamento tipoLancamento = tipoLancamentoRepository
				.findTipoLancamentoId(lancamento.getTipoLancamento().getCdTipoLancamento());

		Optional<Usuario> usuario = usuarioRepository.findById(5L);

		LancamentoForm lf = new LancamentoForm();
		lf.setCdLancamento(lancamento.getCdLancamento());
		lf.setDsLancamento(lancamento.getDsLancamento());
		lf.setDsFormaDePagamento(formaDePagamento.get().getDescricao());
		lf.setSnPago(lancamento.getSnPago());
		lf.setVlPago(lancamento.getVlPago());
		lf.setObservacao(lancamento.getObservacao());
		model.addAttribute("lancamentos", lf);
		model.addAttribute("formapagto", formaDePagamento.get());
		model.addAttribute("tpLancamentos", tipoLancamento);
		model.addAttribute("usuario", usuario.get());

	}

	/**
	 * Realiza o pagamento de um lançamento
	 * 
	 * @author elias
	 * @since 04-03-2023
	 * @param id
	 * @param model
	 */
	public RedirectView pagar(Long id, Model model) {
		RedirectView rw = new RedirectView("/listar");
		Lancamento lancamento = lancamentoRepository.findByIdLancamento(id);
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentosDoMes();
		lancamento.setSnPago("SIM");
		lancamentoRepository.save(lancamento);

		if ("Débito".equals(lancamento.getFormaDePagamento().getDescricao())) {
			// Debitar na conta informada
			Configuracoes config = configuracoesRepository.findConfiguracao();
			String nrContaOrigem = config.getNrContaOrigem();
			Optional<Conta> contaLocalizada = contaRepository.findConta(nrContaOrigem);

			if (contaLocalizada.isPresent()) {
				Conta conta = contaLocalizada.get();
				BigDecimal novoSaldo = conta.getSaldo().subtract(lancamento.getVlPago());
				conta.setSaldo(novoSaldo);
				contaRepository.save(conta);

				LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();
				log.setDescricao("Pagamento " + lancamento.getDsLancamento() + "  Data:  " + LocalDate.now()
						+ " Conta: " + conta.getNrConta() + " - " + conta.getDsConta());
				log.setNrConta(conta.getNrConta());
				log.setDtMovimentacao(LocalDate.now());
				log.setTpMovimentacao("D");
				log.setUsuario("Elias");
				log.setVlMovimentado(lancamento.getVlPago());
				logRepository.save(log);

			}

		}
		model.addAttribute("lancamentos", lancamentos);
		return rw;
	}

	public void anexar(Long id, Model model) {
		Lancamento lancamento = lancamentoRepository.findByIdLancamento(id);

		Optional<FormaDePagamento> formaDePagamento = formaDePagamentoRepository
				.findById(lancamento.getFormaDePagamento().getCdFormaPgamento());

		Optional<TipoLancamento> findBydsTipoLancamento = tipoLancamentoRepository
				.findById(lancamento.getCdLancamento());

		TipoLancamento tipoLancamento = tipoLancamentoRepository
				.findTipoLancamentoId(lancamento.getTipoLancamento().getCdTipoLancamento());

		Optional<Usuario> usuario = usuarioRepository.findById(5L);

		LancamentoForm lf = new LancamentoForm();
		lf.setCdLancamento(lancamento.getCdLancamento());
		lf.setDsLancamento(lancamento.getDsLancamento());
		lf.setDsFormaDePagamento(formaDePagamento.get().getDescricao());
		lf.setSnPago(lancamento.getSnPago());
		lf.setVlPago(lancamento.getVlPago());
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
		model.addAttribute("lancamentos", lf);
		model.addAttribute("formapagto", formaDePagamento.get());
		model.addAttribute("tpLancamentos", tipoLancamento);
		model.addAttribute("usuario", usuario.get());
		model.addAttribute("cdLancamento", id);

	}

//teste
	public void exibirAnexo(Long id, HttpServletResponse response, HttpServletRequest request) throws IOException {
		Configuracoes config = configuracoesRepository.findConfiguracao();
		Lancamento lancamento = lancamentoRepository.findByIdLancamento(id);
		System.out.println(" Anexo: " + lancamento.getDsAnexo());
		File file = new File(lancamento.getDsAnexo());

		if (file.exists()) {
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			System.out.println("Nome do Arquivo: " + file.getName());
			System.out.println("Encontrei o arquivo");
			if (lancamento.getDsAnexo().contains(file.getName())) {
				System.out.println("A descrição contem");
			}
			System.out.println();
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			response.setContentType(mimeType);
			response.setContentLength((int) file.length());

			InputStream input = new BufferedInputStream(new FileInputStream(file));
			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(input, outputStream);
		} else {
			System.out.println("Arquivo não localizado!");
		}

	}
/**
 * Exibe o detalhe do lancamento 
 * @author elias
 * @param model
 * @param id
 * */
	public void getDetalheLancamento(Model model, Long id) {
		Lancamento l = lancamentoRepository.findByIdLancamento(id);
		model.addAttribute("lancamento", l);
		
	}
/**
 * Retorna o logo do Sistema
 * @since 04-03-2023
 * @author elias
 * */
public Configuracoes getLogoSistema() {
	Configuracoes config = configuracoesRepository.findConfiguracao();
	return config;
}
/**
 * Transfere um lançamento para a próxima competência
 * @since 04-03-2023
 * @author elias
 * @param id
 * */

public RedirectView transferir(Long id) {
	RedirectView rw = new RedirectView("/listar");
	Lancamento lancamentoLocalizado = lancamentoRepository.findByIdLancamento(id);
	LocalDate novaCompetencia = LocalDate.now().plusMonths(1L);
	if (lancamentoLocalizado.getSnPago().toUpperCase().equals("NÃO")) {
		lancamentoLocalizado.setDtCompetencia(novaCompetencia);
		lancamentoRepository.save(lancamentoLocalizado);
	}
	
	return rw;
}
/**
 * Paga todos os lançamentos do tipo crédito
 * @since 04-03-2023
 * @author elias
 * */
public RedirectView pagarTodos() {
	RedirectView rw = new RedirectView("/listar");
	Optional<FormaDePagamento> forma = formaDePagamentoRepository.findByDescricaoFormaDePagamento("Crédito");

	if (forma.isPresent()) {
		Long cdFromapagto = forma.get().getCdFormaPgamento();
		List<Lancamento> lancamentosDoMes = lancamentoRepository.findLancamentoPorFormaDePagamento(cdFromapagto);
		if (lancamentosDoMes.size() > 0) {
			for (Lancamento lancamento : lancamentosDoMes) {
				lancamento.setSnPago("SIM");
			}
		}

		lancamentoRepository.saveAll(lancamentosDoMes);
	}
	return rw;
}

}
