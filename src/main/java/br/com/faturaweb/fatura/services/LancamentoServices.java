package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import org.hibernate.boot.model.source.internal.hbm.AbstractSingularAttributeSourceEmbeddedImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;
import com.itextpdf.kernel.pdf.tagutils.IRoleMappingResolver;

import br.com.faturaweb.fatura.form.LancamentoForm;
import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.FormaDePagamento;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.Lote;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;

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
	
	public List<Lancamento>  parcelar(String snParcelar, Long cdUsuario, Integer qtParcela, String primeiraParcelaNaCompetencia){
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
					vlParcela = (vlPago.divide(new BigDecimal(nrParcela),MathContext.DECIMAL32));
					//Adicona a primeira parcela para o mês seguinte 
					for (int i=1; i <= nrParcela; i++) {
						Lancamento l = new Lancamento();
						l.setDsLancamento(lancamento.getDsLancamento() + " "+ (i) +"/"+nrParcela);
						l.setDtCadastro(lancamento.getDtCadastro());
						l.setCartao(lancamento.getCartao());
						//Lança a primeira parcela na competencia atual somente se a configuração global estiver ligada sn_nacompetencia = 'S'
						if (primeiraParcelaNaCompetencia.equals("S")) {
							l.setDtCompetencia(lancamento.getDtCompetencia().plusMonths(i-1)); // lança a parcela no mês atual
						}else{
							l.setDtCompetencia(LocalDate.now());
							l.setDtCompetencia(lancamento.getDtCompetencia().plusMonths(i)); // lança a primeira  parcela no mês seguinte 
						}
						l.setFormaDePagamento(lancamento.getFormaDePagamento());
						l.setNrParcela(i+1);
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
	 * Retorna os laçamentos do mês atual totalizados por Tipo,
	 * Utilizando em: Extrato de Pagamento, index
	 * Obs:Fornece dados para os gráficos
	 * @author elias
	 * @since 08-02-2021
	 * @return {@link HashMap}
	 * */
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
		    	//Só adiciona o valor se ele for maior que zero
		    	if (totalizador.compareTo(BigDecimal.ZERO)==1) {
		    		mapTotalizador.put(tipoLancamento.getDsTipoLancamento(), totalizador);
		    	}
		    	totalizador = totalizador.ZERO;
			}
		
		
		return mapTotalizador;
	}
	
	/**
	 * Retorna os laçamentos do mês atual totalizados por Tipo
	 * @author elias
	 * @since 08-02-2021
	 * @return {@link HashMap}
	 * */
	public HashMap<String, BigDecimal> totalizacaoDespesaCategoria(String mesAno) {
		 HashMap<String, BigDecimal> mapTotalizador = new HashMap<String, BigDecimal>();
		    
		    List<TipoLancamento> tiposLancamentos = tipoLancamentoRepository.findAllTipoLancamentos();
		    List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentosDoMes(mesAno);
		 
		    BigDecimal totalizador = new BigDecimal(0);
		    
		    for (TipoLancamento tipoLancamento : tiposLancamentos) {
				
		    	for (Lancamento lancamento : lancamentos) {
		    		if (lancamento.getTipoLancamento().getCdTipoLancamento().equals(tipoLancamento.getCdTipoLancamento())) {
		    			totalizador = totalizador.add(lancamento.getVlPago());
		    		}
		    	}
		    	//Só adiciona o valor se ele for maior que zero
		    	if (totalizador.compareTo(BigDecimal.ZERO)==1) {
		    		mapTotalizador.put(tipoLancamento.getDsTipoLancamento(), totalizador);
		    	}
		    	totalizador = totalizador.ZERO;
			}
		    
		
		return mapTotalizador;
	}
	/**
	 * Retorna a totalização das despesas do mês corrrente
	 * @author elias
	 * @since 01/04/2022
	 * @return {@link BigDecimal}
	 * */
	public BigDecimal getTotalLctoMes(String mesAno) {
		BigDecimal total = BigDecimal.ZERO;
		List<Lancamento> lctoDoMes = lancamentoRepository.findAllLancamentosDoMes(mesAno);
		
		for (int i=0; i < lctoDoMes.size(); i++){
			total = total.add( lctoDoMes.get(i).getVlPago());
		}
		return total;
	}
	
	public 	String getTotal(){
		HashMap<String, BigDecimal> totalizacaoDespesaCategoria = totalizacaoDespesaCategoria();
	
		Set<String> keySet = totalizacaoDespesaCategoria.keySet();
		  java.util.Collection<BigDecimal> values = totalizacaoDespesaCategoria.values();
	    Iterator i = keySet.iterator();
		Iterator<BigDecimal> ivalues = values.iterator();
		String str = new String();
	    while (i.hasNext()) {
	    	str= str+"{ name : ' " +i.next() + " ' , y: " +ivalues.next() + " },";
	    }
	   
	    str = " [ " + str.substring(0,str.length()-1) + " ]";
	 
	
		
	 return str;
	}
	
	/**
	 * Retorna a totalização dos lançamentos por Forma de Pagamento
	 * @since 16/06/2022
	 * @author elias.silva
	 * @param mesAno - Mes ano  mm/AAAA
	 * @return {@link HashMap} - HashMap <String, BigDecimal>
	 * */
	public HashMap<String, BigDecimal>getTotalizacaoDespesaFormaPagto(String mesAno){
		
		HashMap<String, BigDecimal> hashTotalizacao = new HashMap<String, BigDecimal>();
		List<FormaDePagamento> formasDePagamento = formaPagtoRepository.findAllFormasDePagamento(); 
		List<Lancamento> lancamentosDoMes = lancamentoRepository.findAllLancamentosDoMes(mesAno);
		BigDecimal total = BigDecimal.ZERO;
		
		for (FormaDePagamento pagamentos : formasDePagamento) {
			for (Lancamento lancamento : lancamentosDoMes) {
				String pagtoDescricao = pagamentos.getDescricao();
				String descricao = lancamento.getFormaDePagamento().getDescricao();
					if (pagtoDescricao.equals(lancamento.getFormaDePagamento().getDescricao())) {
						total = total.add(lancamento.getVlPago());
					}				
			}
			if (total.compareTo(BigDecimal.ZERO)==1) {
				hashTotalizacao.put(pagamentos.getDescricao(),total);
			}
			total = BigDecimal.ZERO;
		}
	 		return hashTotalizacao;
	}
	
	/**
	 *Retorna o percentual de gasto de a cordo com a forma de pagamento informada
	 * @since 22/06/2022
	 * @author elias.silva
	 * @param mesAno - Mes ano  mm/AAAA
	 * @param formaDePAgamento - Cartão, Dinheiro etc
	 * @return {@link BigDecimal} - Percentual de Gasto
	 * */
	public BigDecimal getLimiteCartao(String mesAno, String formaDePAgamento){
		Configuracoes configuracao = configuracaoRepository.findConfiguracao();
		BigDecimal limite = configuracao.getLimiteCartao();
		BigDecimal percent = BigDecimal.ZERO;
		MathContext mtx = new  MathContext(2,RoundingMode.HALF_UP);
		//Limite 1000 gasto de 1200
		List<Lancamento> lancamentosDoMes = lancamentoRepository.findAllLancamentosDoMes(mesAno);
		BigDecimal totalGasto = BigDecimal.ZERO;
			for (Lancamento lancamento : lancamentosDoMes) {
					if (formaDePAgamento.equals( lancamento.getFormaDePagamento().getDescricao())) {
						totalGasto = totalGasto.add(lancamento.getVlPago());
					}				
			}
			System.out.println("Total Gasto: " + totalGasto);
			//Se o total for menor que o limite
			if (totalGasto.compareTo(limite)==-1) {
				System.out.println("Entrei aqui");
				percent = (totalGasto.divide(limite,3,RoundingMode.FLOOR).multiply(BigDecimal.valueOf(100.0)));
			}else {
				//Retorna o percentual negativo quando o totalGasto for maior que o limite
				BigDecimal diferenca = totalGasto.subtract(limite);
				percent = (diferenca.divide(limite,3,RoundingMode.FLOOR).multiply(BigDecimal.valueOf(100.0)).multiply(BigDecimal.valueOf(-1)));
			}
			System.out.println("Percentual Calcualdo " + percent);
			System.out.println("Limite " +limite);
	 		return percent;
	}
/**
 * Valida o lote e o lancamento, permitindo apenas lançamento com data máxima retroativa a um mês.
 * Caso o lote esteja fechado o lançamento será adicionado na próxma competência.
 * @author elias
 * @param lancamento - Lançamento 
 * @param loteRepository - LoteRepository 
 * @return {@link Lancamento} - Lançamento com nova Competência.
 * */
	public Lancamento validaLoteLancamento(Lancamento lancamento, LoteRepository loteRepository) {
		try {
			Lote  loteCompetencia = loteRepository.findLoteCompetencia();
			if (loteCompetencia.getStatus().equals("A")) {
				LocalDate mesAtual = LocalDate.now();
				LocalDate mesLancamento = lancamento.getDtCompetencia();
				int total = mesAtual.getMonthValue() -  mesLancamento.getMonthValue();
				if (total > 1) {
					lancamento.setDtCompetencia(LocalDate.now().minusMonths(1));
				}
			}else {
				lancamento.setDtCompetencia(LocalDate.now());
			}
	
		} catch (Exception e) {
			lancamento.setDtCompetencia(LocalDate.now());
		}
				return lancamento;
	}
/**
 * Altera todos os lancamentos se existirem 
 * @since 19/11/2022
 * @author elias
 * @param lancamentoForm - Lançamento com as alterações da página 
 * @param lancamentoLocalizado - Lancamento Original Localizado
 * */
public void alterarTodos(LancamentoForm lancamentoForm, Lancamento lancamentoLocalizado ) {
	
	System.out.println(lancamentoLocalizado.getDsLancamento());
	int length = lancamentoLocalizado.getDsLancamento().length();
	String dsLancamento = lancamentoLocalizado.getDsLancamento().substring(0,length-4);
	
	BigDecimal vlPago = lancamentoLocalizado.getVlPago();
	List<Lancamento> listaDemaisLancamentos = lancamentoRepository.findDemiasLancamento(dsLancamento, vlPago);
	List<Lancamento>listaLancamentosAlterarados = new ArrayList<Lancamento>();
	if (listaDemaisLancamentos.size()>1) {
		for (Lancamento lancamento2 : listaDemaisLancamentos) {
			String controle = lancamento2.getDsLancamento().substring(length-4);
			
			Lancamento lancamentoAlterado = new Lancamento();
			lancamentoAlterado = lancamento2;
			lancamentoAlterado.setDsLancamento(lancamentoForm.getDsLancamento()+" " +controle);
			Optional<FormaDePagamento> formaPagtoLocalizado = formaPagtoRepository.findByDescricaoFormaDePagamento(lancamentoForm.getDsFormaDePagamento());
			lancamentoAlterado.setFormaDePagamento(formaPagtoLocalizado.get());
			Optional<TipoLancamento> tipoLancamentoLocalizado = tipoLancamentoRepository.findBydsTipoLancamento(lancamentoForm.getDsTipoLancamento());
			lancamentoAlterado.setTipoLancamento(tipoLancamentoLocalizado.get());
			lancamentoAlterado.setSnPago(lancamentoForm.getSnPago());
			lancamentoAlterado.setVlPago(lancamentoForm.getVlPago());
			lancamentoAlterado.setObservacao(lancamentoForm.getDsLancamento());
			listaLancamentosAlterarados.add(lancamentoAlterado);
				
		}
		
		lancamentoRepository.saveAll(listaDemaisLancamentos);
		
	}
	
}



}
