package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
import br.com.faturaweb.fatura.services.AppServices;
import br.com.faturaweb.fatura.services.ReceitaServices;

@Controller
@RequestMapping("/receita")
public class ReceitaController {
	
	@Autowired
	ReceitaRepository receitaRepository;
	@Autowired
	ContaRepository contaRepository;
	@Autowired
	LogMovimentacaoFinanceiraRepository logRepository;
	@Autowired
	ReceitaServices services;
	@Autowired
	ConfiguracoesRepository configuracoesRepository;
	@Autowired
	AppServices appServices;
	@Autowired
	LoteRepository loteRepository;
	
	@GetMapping("/listar")	
	public String  listar(Model model) {
		List<Receita> receitas = receitaRepository.findaAllReceitaAnoCorrente();
		
		Boolean loteFechado = appServices.verificalote("F", loteRepository.findLoteCompetencia());
		String msg= null;
		String statusLote=null;
		if (loteFechado) {
			msg="Lote Fechado! Não é possivel - Excluir / Alterar ou Salvar uma Receita"
					+ "Ação: Reabra o lote contábil!!!";
			statusLote="F";
		}else 
			statusLote="A";
		{}
		
		model.addAttribute("receitas",receitas);
		model.addAttribute("mensagem",msg);
		model.addAttribute("statuslote",statusLote);
		return "home/receita-listar";
	}
	
	@GetMapping("/cadastro")
	public String cadastrar(Model model) {
	String statuslote=null;
	String mensagem=null;
	Receita receita = new Receita();
	model.addAttribute("receita",receita)	;
	
	if (appServices.verificalote("F", loteRepository.findLoteCompetencia())) {
		statuslote="F";
		mensagem="Lote da competência fechado!Não será possível adicionar uma nova Receita!";
	}
	model.addAttribute("statuslote",statuslote);
	model.addAttribute("menssagem",mensagem);
	 return "home/receita";
		
	}
	
	@PostMapping("/salvar")
	public String salvar(Model model, Receita receitaForm) {
		LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();
		try {
			receitaRepository.save(receitaForm);			
			model.addAttribute("mensagem","Receita Salva com sucesso!");
			//Obtendo a conta configurada
			Configuracoes config = configuracoesRepository.findConfiguracao();
			Optional<Conta> contaLocalizada = contaRepository.findConta(config.getNrContaOrigem());
			if(contaLocalizada.isPresent()) {
				//obtendo o  saldo e somando a receita líquida 
				BigDecimal saldo = contaLocalizada.get().getSaldo();
				BigDecimal novoSaldoBigDecimal = saldo.add(receitaForm.getSalLiquido());
				contaLocalizada.get().setSaldo(novoSaldoBigDecimal);
				//Salvando a conta com o novo saldo
				contaRepository.save(contaLocalizada.get());
				
				//registrando a movimentação no log
				//Registrando no log a movimentação financeira
				log.setDescricao("Creditando " +receitaForm.getSalLiquido()+" na "+ contaLocalizada.get().getNrConta() + " -"  + contaLocalizada.get().getDsConta());
				log.setDtMovimentacao(LocalDate.now());
				log.setNrConta(contaLocalizada.get().getNrConta());
				log.setTpMovimentacao("C");
				log.setUsuario("Elias");
				log.setVlMovimentado(receitaForm.getSalLiquido());
				logRepository.save(log);
			}
				
		} catch (Exception e) {
			  System.out.println("Não foi possível salvar a receita informada ->"+ receitaForm.getDsReceita());
			  model.addAttribute("mensagem","Falha ao tentar savar a receita!");
		}
		model.addAttribute("receita",new Receita());
		return "home/receita";
	}
	
	@GetMapping("excluir/{id}")
	public RedirectView excluir(@PathVariable Long id, Model model) {
		//Obtendo a conta de origem
		LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();
		Configuracoes config = configuracoesRepository.findConfiguracao();
		Optional<Conta> contaLocalizada = contaRepository.findConta(config.getNrContaOrigem());
		RedirectView rw = new RedirectView();
		
		rw.setUrl("/receita/listar");
		//Localizando a receita a ser excluída
		Optional<Receita> receita = receitaRepository.findById(id);
		//Se a receita for localizada, debita o valor da conta  e salva com o novo saldo.
		if (receita.isPresent()) {
			BigDecimal saldo = contaLocalizada.get().getSaldo();
			BigDecimal novoSaldo = saldo.subtract(receita.get().getSalLiquido());
			contaLocalizada.get().setSaldo(novoSaldo);
			contaRepository.save(contaLocalizada.get());
			//Registrando no log a movimentação financeira
			log.setDescricao("Debitando " + receita.get().getSalLiquido() +" na "+ contaLocalizada.get().getNrConta() + " -"  + contaLocalizada.get().getDsConta());
			log.setDtMovimentacao(LocalDate.now());
			log.setNrConta(contaLocalizada.get().getNrConta());
			log.setTpMovimentacao("D");
			log.setUsuario("Elias");
			log.setVlMovimentado(receita.get().getSalLiquido());
			logRepository.save(log);
		}
		if (receita.get().getCdReceita()!=null){
			receitaRepository.delete(receita.get());
		}
		
		
		return rw;
	}
	
	@GetMapping("alterar/{id}")
	public String alterar(@PathVariable Long id, Model model) {
		
		Optional<Receita> receita = receitaRepository.findById(id);
		model.addAttribute("receita",receita.get());
		
		return "home/receita";
	}
	/**
	 * Clona a última receita
	 * @author elias
	 * @since 20/03/2022
	 * @return {@link RedirectView}
	 * */
	@GetMapping("/clonar/{id}")
	public RedirectView clonaReceita(@PathVariable Long id) {
		Configuracoes config = configuracoesRepository.findConfiguracao();
		Optional<Conta> contaLocalizada = contaRepository.findConta(config.getNrContaOrigem());
		LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();
		
		Receita r = new Receita();
		 Optional<Receita> receitaOptional = receitaRepository.findById(id);
		if(receitaOptional.isPresent()) {
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
			
			//Registrando no log a movimentação financeira
			log.setDescricao("Creditando " +r.getSalLiquido()+" na "+ contaLocalizada.get().getNrConta() + " -"  + contaLocalizada.get().getDsConta());
			log.setDtMovimentacao(LocalDate.now());
			log.setNrConta(contaLocalizada.get().getNrConta());
			log.setTpMovimentacao("C");
			log.setUsuario("Elias");
			log.setVlMovimentado(r.getSalLiquido());
			logRepository.save(log);
		}
		
		RedirectView rw = new RedirectView("/receita/listar");
		//rw.setUrl("http://localhost:8080/receita/listar");
		
		return rw;
	}
	}
