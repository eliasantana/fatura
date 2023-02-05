package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Cartao;
import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.FormaDePagamento;
import br.com.faturaweb.fatura.model.ItMeta;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.model.Meta;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.repository.ItMetaRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;
import br.com.faturaweb.fatura.repository.MetaRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;

@Service
public class ItMetaServices {

	@Autowired
	ItMetaRepository itMetaRepository;
	@Autowired
	MetaRepository metaRepository;
	@Autowired
	ContaRepository contaRepository;
	@Autowired
	AppServices appServices;
	@Autowired
	FormaDePagamentoRepository formaPagtoRepository;
	@Autowired
	LancamentoRepository lancamentoRepository;
	@Autowired
	TipoLancamentoRepository tipoLancamentoRepository;
	@Autowired
	LogMovimentacaoFinanceiraRepository log;
	@Autowired
	MetaService metaservices;
	@Autowired
    ConfiguracoesRepository configRepository;

	/**
	 * Lista todos os itens da meta informada
	 * @author elias
	 * @since 05-02-2022
	 * @param id    - Código da meta
	 * @param model - Modelo
	 * */
public String listarItMeta(@PathVariable Long id, Model model) {
		
		List<ItMeta> itMeta = itMetaRepository.findItNaoCreditado(id);
		Optional<Meta> metaLocalizada = metaRepository.findById(id);
		Conta conta = metaLocalizada.get().getConta();
		List<ItMeta> itemCreditados = itMetaRepository.findItMeta(id);
		BigDecimal totalItMeta = metaservices.totalizaItMeta(itemCreditados);
		BigDecimal andamentoMeta = metaservices.andamentoMeta(metaLocalizada.get(), totalItMeta);
		BigDecimal metaRestante = BigDecimal.valueOf(100).subtract(andamentoMeta);
		try {
			Optional<Meta> meta = metaRepository.findById(itMeta.get(0).getMeta().getCdMeta());			
			model.addAttribute("nome_meta",meta.get().getDescricao());
			//model.addAttribute("cdmeta",meta.get().getCdMeta());
			model.addAttribute("cdmeta",id);
			model.addAttribute("itens",itMeta);
			model.addAttribute("restante",metaRestante);
			// Dados do Gráfico 
			model.addAttribute("totalmeta",totalItMeta);
			model.addAttribute("andamento", andamentoMeta);
			model.addAttribute("titulo", metaLocalizada.get().getDescricao());
			model.addAttribute("descricao"," Resta para a meta "+meta.get().getDescricao());
		} catch (Exception e) {
			model.addAttribute("nome_meta","Não há itens de  meta a serem exibidos!");
			model.addAttribute("cdmeta",id);
			model.addAttribute("itens",itMeta);
		}
		 model.addAttribute("conta",conta);
		
		return "detalhe-meta";
}

/**
 * Credita o valor pago na conta informada e gera um lançamento para o item pago
 * @author elias
 * @since 05-02-2023
 * @param idMeta - Código da Meta
 * @param idItMeta - Código do item da Meta
 * @param Model - Modelo
 * */
public RedirectView creditarItMeta(@PathVariable Long idMeta, @PathVariable Long idItMeta, Model model) {
	RedirectView rw = new RedirectView("/itmeta/listar/"+idMeta);
	ItMeta itMeta = itMetaRepository.findItMetaId(idItMeta);
	LogMovimentacaoFinanceira logMovimentacao = new LogMovimentacaoFinanceira();
	LogMovimentacaoFinanceira logLancamentoCredito = new LogMovimentacaoFinanceira();
	Optional<Meta> metaLocalizada = metaRepository.findById(idMeta);
	
	List<ItMeta> itemCreditados = itMetaRepository.findItMeta(idMeta);
	BigDecimal totalItMeta = metaservices.totalizaItMeta(itemCreditados);
	
   BigDecimal andamentoMeta = metaservices.andamentoMeta(metaLocalizada.get(), totalItMeta);
	//Valia o id informado é igual ao localizado
	if (itMeta.getCdItMeta()!=null) {
		//Localizando a conta  conta
		 Conta conta = itMeta.getMeta().getConta();
		 //Credita o valor do item da meta (Valor Semanal)
		appServices.credita(conta, itMeta.getVlrSemana());
		//Debitando o valor na conta de origem			
		appServices.debitaNaOrigem(itMeta.getVlrSemana());
		//Gerando log da movimentação
		logMovimentacao.setDescricao("Creditando valor em " + conta.getNrConta()  +" Ag. " + conta.getNrAgencia() );
		logMovimentacao.setNrConta(conta.getNrConta()  );
		logMovimentacao.setDtMovimentacao(LocalDate.now());
		logMovimentacao.setTpMovimentacao("C");
		logMovimentacao.setVlMovimentado(itMeta.getVlrSemana());
		log.save(logMovimentacao);
		
		//Muda o status para creditado
		itMeta.setSnCreditado("S");
		itMetaRepository.save(itMeta);
		
		// Gerando um lançamento despesas para a meta paga
		Lancamento lancamento = new Lancamento();
		lancamento.setDsLancamento("Pagamento Meta "+metaLocalizada.get().getDescricao());
		lancamento.setDtCadastro(LocalDate.now());
		lancamento.setDtCompetencia(LocalDate.now());
		Optional<FormaDePagamento> formaPagto = formaPagtoRepository.findById(5L);
		lancamento.setFormaDePagamento(formaPagto.get());
		lancamento.setNrParcela(1);
		lancamento.setSnPago("SIM"); //Pago
		//Optional<TipoLancamento> tl = tipoLancamentoRepository.findBycdTipoLancamento(36L); // 36-Metas
		Optional<TipoLancamento> tl = tipoLancamentoRepository.findBydsTipoLancamento("Metas");
	    Cartao cartao = new Cartao();
	    cartao.setCdCartao(1L);//Nenhum
		lancamento.setCartao(cartao);
		lancamento.setTipoLancamento(tl.get());
		Usuario u = new Usuario();
		u.setCdUsuario(5L);
		lancamento.setUsuario(u);//TESTES
		lancamento.setVlPago(itMeta.getVlrSemana());
		
		lancamentoRepository.save(lancamento);
		//Gerano um log para o credito lançado
		logLancamentoCredito.setDescricao(lancamento.getDsLancamento());
		logLancamentoCredito.setDtMovimentacao(LocalDate.now());
		logLancamentoCredito.setNrConta(conta.getNrConta());
		logLancamentoCredito.setVlMovimentado(lancamento.getVlPago());
		logLancamentoCredito.setTpMovimentacao("C");
		log.save(logLancamentoCredito);
	}
	// Dados do Gráfico 
		model.addAttribute("totalmeta",totalItMeta);
		model.addAttribute("andamento", andamentoMeta);
		model.addAttribute("titulo", metaLocalizada.get().getDescricao());
		model.addAttribute("descricao"," Resta para a meta "+metaLocalizada.get().getDescricao());
	return rw;
}

/**
 * Regera  os itens da meta recalculando as parcelas
 * @author eliaselias
 * @param id - Código do item da meta
 * */
public RedirectView reGerarMetaItMeta(@PathVariable Long id) {
		System.out.println("1 - Recalculando Meta a Meta!");
		RedirectView rw = new RedirectView("/itmeta/listar/"+id.toString());
		
		try {
			Optional<Meta> metaLocalizada = metaRepository.findById(id);
			Meta meta = metaLocalizada.get();
			
		    List<ItMeta> itensRecalculados = metaservices.reGeraItMeta(meta);
		    itMetaRepository.saveAll(itensRecalculados);
		    
		    System.out.println("Itens recalculados salvos com sucesso!");
			LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();
			log.setDescricao("Recalculando itens da meta  " + meta.getCdMeta() + " - " +meta.getDescricao());
			log.setDtMovimentacao(LocalDate.now());
			log.setTpMovimentacao("R");
			
			this.log.save(log);
			
		} catch (Exception e) {
			System.out.println(e.getMessage() + "Erro ao regerar os itens de meta!");
		}
	return rw;
}
}
