package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;

@Service
public class LancamentoServices {
	@Autowired
	LancamentoRepository lancamentoRepository;
	@Autowired
	TipoLancamentoRepository tipoLancamentoRepository;
	
	public List<Lancamento>  parcelar(String snParcelar, Long cdUsuario, Integer qtParcela){
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
						l.setDtCompetencia(lancamento.getDtCompetencia().plusMonths(i)); // teste
						l.setFormaDePagamento(lancamento.getFormaDePagamento());
						l.setNrParcela(i+1);
						l.setSnPago(lancamento.getSnPago());
						l.setTipoLancamento(lancamento.getTipoLancamento());
						l.setUsuario(lancamento.getUsuario());
						l.setVlPago(vlParcela);
						
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
		    System.out.println(mapTotalizador.keySet());
		    System.out.println(mapTotalizador.values());
		
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
		    System.out.println(mapTotalizador.keySet());
		    System.out.println(mapTotalizador.values());
		
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
	
}
