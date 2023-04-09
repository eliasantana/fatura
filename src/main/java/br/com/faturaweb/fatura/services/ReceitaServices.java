package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.model.Receita;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;
import br.com.faturaweb.fatura.repository.ReceitaRepository;

@Service
public class ReceitaServices {

	@Autowired
	ReceitaRepository receitaRepository;
	@Autowired
	ContaRepository contaRepository;
	@Autowired
	LogMovimentacaoFinanceiraRepository logRepository;
	@Autowired
	ConfiguracoesRepository configuracoesRepository;
	@Autowired
	AppServices appServices;
	@Autowired
	LoteRepository loteRepository;

	/**
	 * Totaliza as receitas e retorna uma Map com chave e valor
	 * 
	 * @author elias
	 * @since 24-11-2021
	 * @param receitas
	 * @return Map<String, BigDecimal> dadosReceita
	 */
	public Map<String, BigDecimal> totalizaReceita(List<Receita> receitas) {

		DateTimeFormatter df = DateTimeFormatter.ofPattern("MM");
		Map<String, BigDecimal> dadosReceita = new LinkedHashMap<String, BigDecimal>();
		// Meses do ano
		BigDecimal jan = new BigDecimal(0);
		BigDecimal fev = new BigDecimal(0);
		BigDecimal mar = new BigDecimal(0);
		BigDecimal abr = new BigDecimal(0);
		BigDecimal mai = new BigDecimal(0);
		BigDecimal jun = new BigDecimal(0);
		BigDecimal julho = new BigDecimal(0);
		BigDecimal ago = new BigDecimal(0);
		BigDecimal set = new BigDecimal(0);
		BigDecimal out = new BigDecimal(0);
		BigDecimal nov = new BigDecimal(0);
		BigDecimal dez = new BigDecimal(0);

		if (receitas.size() > 0) {
			for (Receita receita : receitas) {
				String mes = receita.getDtRecebimento().format(df);
				System.out.println(receita.getSalLiquido());
				switch (mes) {
				case "01":
					jan = jan.add(receita.getSalLiquido());
					break;
				case "02":
					fev = fev.add(receita.getSalLiquido());
					break;
				case "03":
					mar = mar.add(receita.getSalLiquido());
					break;
				case "04":
					abr = abr.add(receita.getSalLiquido());
					break;
				case "05":
					mai = mai.add(receita.getSalLiquido());
					break;
				case "06":
					jun = jun.add(receita.getSalLiquido());
					break;
				case "07":
					julho = julho.add(receita.getSalLiquido());
					break;
				case "08":
					ago = ago.add(receita.getSalLiquido());
					break;
				case "09":
					set = set.add(receita.getSalLiquido());
					break;
				case "10":
					out = out.add(receita.getSalLiquido());
					break;
				case "11":
					nov = nov.add(receita.getSalLiquido());
					break;
				case "12":
					dez = dez.add(receita.getSalLiquido());
					break;
				default:
					break;
				}

				dadosReceita.put("janeiro", jan);
				dadosReceita.put("fevereiro", fev);
				dadosReceita.put("março", mar);
				dadosReceita.put("abril", abr);
				dadosReceita.put("maio", mai);
				dadosReceita.put("junho", jun);
				dadosReceita.put("julho", julho);
				dadosReceita.put("agosto", ago);
				dadosReceita.put("setembro", set);
				dadosReceita.put("outubro", out);
				dadosReceita.put("novembro", nov);
				dadosReceita.put("dezembro", dez);

			}
		}
		return dadosReceita;

	}

	/**
	 * Excluí a receita informada e realiza o débido do valor creditado durante o
	 * cadastro.
	 * 
	 * @author Kauan Mateus
	 * @since 08-04-2023
	 * @param id
	 * @param model
	 */
	public void excluir(Long id, Model model) {

		// Obtendo a conta de origem
		LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();
		Configuracoes config = configuracoesRepository.findConfiguracao();
		Optional<Conta> contaLocalizada = contaRepository.findConta(config.getNrContaOrigem());
		// Localizando a receita a ser excluída
		Optional<Receita> receita = receitaRepository.findById(id);
		// Se a receita for localizada, debita o valor da conta e salva com o novo
		// saldo.
		if (receita.isPresent()) {
			BigDecimal saldo = contaLocalizada.get().getSaldo();
			BigDecimal novoSaldo = saldo.subtract(receita.get().getSalLiquido());
			contaLocalizada.get().setSaldo(novoSaldo);
			contaRepository.save(contaLocalizada.get());
			// Registrando no log a movimentação financeira
			log.setDescricao("Debitando " + receita.get().getSalLiquido() + " na " + contaLocalizada.get().getNrConta()
					+ " -" + contaLocalizada.get().getDsConta());
			log.setDtMovimentacao(LocalDate.now());
			log.setNrConta(contaLocalizada.get().getNrConta());
			log.setTpMovimentacao("D");
			log.setUsuario("Elias");
			log.setVlMovimentado(receita.get().getSalLiquido());
			logRepository.save(log);
		}
		if (receita.get().getCdReceita() != null) {
			receitaRepository.delete(receita.get());
		}
	}

	/**
	 * Altera a receita informada
	 * 
	 * @since 08-04-2023
	 * @param id
	 * @param model *
	 */
	public void alterar(Long id, Model model) {
		Optional<Receita> receita = receitaRepository.findById(id);		
		model.addAttribute("receita", receita.get());
		model.addAttribute("receitaanterior", receita.get());
	}

	/**
	 * Clona a última receita
	 * 
	 * @author elias
	 * @since 20/03/2022
	 * @return {@link RedirectView}
	 */
	public void clonar(Long id) {

		Configuracoes config = configuracoesRepository.findConfiguracao();
		Optional<Conta> contaLocalizada = contaRepository.findConta(config.getNrContaOrigem());
		LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();

		Receita r = new Receita();
		Optional<Receita> receitaOptional = receitaRepository.findById(id);
		if (receitaOptional.isPresent()) {
			Receita receita = receitaOptional.get();
			r.setDesconto(receita.getDesconto());
			r.setDsReceita(receita.getDsReceita());
			r.setDtRecebimento(LocalDate.now().plusMonths(1));
			r.setSalBruto(receita.getSalBruto());
			r.setSalLiquido(receita.getSalLiquido());
		}

		receitaRepository.save(r);

		if (contaLocalizada.isPresent()) {
			BigDecimal saldo = contaLocalizada.get().getSaldo();
			BigDecimal novoSaldo = saldo.add(r.getSalLiquido());
			contaLocalizada.get().setSaldo(novoSaldo);
			contaRepository.save(contaLocalizada.get());

			// Registrando no log a movimentação financeira
			log.setDescricao("Creditando " + r.getSalLiquido() + " na " + contaLocalizada.get().getNrConta() + " -"
					+ contaLocalizada.get().getDsConta());
			log.setDtMovimentacao(LocalDate.now());
			log.setNrConta(contaLocalizada.get().getNrConta());
			log.setTpMovimentacao("C");
			log.setUsuario("Elias");
			log.setVlMovimentado(r.getSalLiquido());
			logRepository.save(log);
		}

	}

	/**
	 * Salva uma receita Creditando o valor na conta de origem
	 * 
	 * @author Kauan Mateus
	 * @since 08-04-2023
	 * @param model
	 * @param receitaForm
	 */
	public void salvar(Model model, Receita receitaForm) {
		LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();
		
		try {
			
			receitaRepository.save(receitaForm);
			model.addAttribute("mensagem", "Receita Salva com sucesso!");
			// Obtendo a conta configurada
			Configuracoes config = configuracoesRepository.findConfiguracao();
			Optional<Conta> contaLocalizada = contaRepository.findConta(config.getNrContaOrigem());
			if (contaLocalizada.isPresent()) {
				// obtendo o saldo e somando a receita líquida
				BigDecimal saldo = contaLocalizada.get().getSaldo();
				BigDecimal novoSaldoBigDecimal = saldo.add(receitaForm.getSalLiquido());
				contaLocalizada.get().setSaldo(novoSaldoBigDecimal);
				// Salvando a conta com o novo saldo
				contaRepository.save(contaLocalizada.get());
				// registrando a movimentação no log
				// Registrando no log a movimentação financeira
				log.setDescricao("Creditando " + receitaForm.getSalLiquido() + " na "
						+ contaLocalizada.get().getNrConta() + " -" + contaLocalizada.get().getDsConta());
				log.setDtMovimentacao(LocalDate.now());
				log.setNrConta(contaLocalizada.get().getNrConta());
				log.setTpMovimentacao("C");
				log.setUsuario("Elias");
				log.setVlMovimentado(receitaForm.getSalLiquido());
				logRepository.save(log);
			}

		} catch (Exception e) {
			System.out.println("Não foi possível salvar a receita informada ->" + receitaForm.getDsReceita());
			model.addAttribute("mensagem", "Falha ao tentar savar a receita!");
		}
		model.addAttribute("receita", new Receita());

	}

	/**
	 * Lista as receitas cadastradas
	 * @author Kauan Mateus
	 * @since 08-04-2023
	 * @param model
	 * */
	public void listar(Model model) {
		List<Receita> receitas = receitaRepository.findaAllReceitaAnoCorrente();
		Boolean loteFechado = appServices.verificalote("F", loteRepository.findLoteCompetencia());
		String msg = null;
		String statusLote = null;
		if (loteFechado) {
			msg = "Lote Fechado! Não é possivel - Excluir / Alterar ou Salvar uma Receita"
					+ "Ação: Reabra o lote contábil!!!";
			statusLote = "F";
		} else
			statusLote = "A";
		{
		}

		model.addAttribute("receitas", receitas);
		model.addAttribute("mensagem", msg);
		model.addAttribute("statuslote", statusLote);
	}
/**
 * Cadastra uma receita 
 * */
	public void cadastro(Model model) {
		String statuslote = null;
		String mensagem = null;
		Receita receita = new Receita();
		model.addAttribute("receita", receita);
		if (appServices.verificalote("F", loteRepository.findLoteCompetencia())) {
			statuslote = "F";
			mensagem = "Lote da competência fechado!Não será possível adicionar uma nova Receita!";
		}
		model.addAttribute("statuslote", statuslote);
		model.addAttribute("menssagem", mensagem);
		
	}

}
