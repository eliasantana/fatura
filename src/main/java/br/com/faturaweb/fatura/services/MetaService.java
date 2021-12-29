package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.net.PercentEscaper;

import br.com.faturaweb.fatura.model.ItMeta;
import br.com.faturaweb.fatura.model.Meta;

@Service
public class MetaService {

	/**
	 * Gera um item de meta para cada semana calculada.
	 * @author elias
	 * @param meta
	 * @return {@link ArrayList}
	 * */
	public List<ItMeta>geraItMeta(Meta meta) {
		 List<ItMeta> itensMeta = new ArrayList<ItMeta>();
		 MathContext mt = new MathContext(0, RoundingMode.HALF_UP);
		long totalDeDias = ChronoUnit .DAYS.between(meta.getDtInicio(), meta.getDtFim());
		long totalSemanas = (totalDeDias/7);
		BigDecimal vlParcela = meta.getVlMeta().divide(new BigDecimal(totalSemanas), mt.DECIMAL32);
		System.out.println("Valor da Parcela: " + vlParcela);
		System.out.println("Total de Semanas: "+ totalSemanas);
		for (int i=1; i <= totalSemanas; i++) {
			
			ItMeta it = new ItMeta(meta.getDescricao() + " - " + "Semana " + i +"/" + totalSemanas, i, vlParcela, meta);
			itensMeta.add(it);
		}
		
			return itensMeta;
	}
	
	/**
	 * Totaliza os itens da meta informada
	 * @author elias
	 * @param itensDaMeta
	 * @return total
	 * */
	public BigDecimal totalizaItMeta(List<ItMeta> itensDaMeta) {
		BigDecimal total = new BigDecimal(0);
		for (ItMeta itMeta : itensDaMeta) {
			total =	total.add(itMeta.getVlrSemana());
		}
		
		return total;
	}
	
	/**
	 * Calcula o percentual do antamente da meta
	 * @author elias
	 * @param meta
	 * @param totalItMeta
	 * @return percentual
	 * */
	public BigDecimal andamentoMeta (Meta meta, BigDecimal totalItMeta) {
		BigDecimal percentutal = new BigDecimal(0);
		MathContext mtx = new MathContext(2, RoundingMode.HALF_UP);
		percentutal = (totalItMeta.divide(meta.getVlMeta(),mtx.DECIMAL32).multiply(BigDecimal.valueOf(100.0)));
		return percentutal;
	}
}
