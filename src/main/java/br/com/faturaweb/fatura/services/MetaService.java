package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.common.net.PercentEscaper;
import com.mysql.cj.log.Log;
import com.mysql.cj.x.protobuf.MysqlxResultset.FetchSuspendedOrBuilder;

import br.com.faturaweb.fatura.model.ItMeta;
import br.com.faturaweb.fatura.model.Meta;
import br.com.faturaweb.fatura.repository.ItMetaRepository;
import br.com.faturaweb.fatura.repository.MetaRepository;

@Service
public class MetaService {
	@Autowired
	MetaRepository metaRepository;
	
	@Autowired
	ItMetaRepository itMetaRepository;
	
	/**
	 * Gera um item de meta semanal ou mensal de acordo com o tipo de meta.
	 * @author elias
	 * @param meta
	 * @return {@link ArrayList}
	 * */
	public List<ItMeta>geraItMeta(Meta meta) {
		
		List<ItMeta> itensMeta = new ArrayList<ItMeta>();
		MathContext mt = new MathContext(0, RoundingMode.HALF_UP);
		if (meta.getTpMeta().equals("M")){
			//Meta Mensal
			Long totalDeMes = ChronoUnit.MONTHS.between(meta.getDtInicio(), meta.getDtFim());
			if (totalDeMes==0) {
				totalDeMes =1L;
			}
			BigDecimal vlParcela = meta.getVlMeta().divide(new BigDecimal(totalDeMes), mt.DECIMAL32);
			LocalDate dataMeta = meta.getDtInicio();
			
			//Gera os itens da meta
			for (int i=1; i <= totalDeMes; i++) {
				dataMeta = dataMeta.plusMonths(i);
				ItMeta it = new ItMeta(meta.getDescricao() + " - " + "Mês " + i +"/" + totalDeMes + " - " + dataMeta, i, vlParcela, meta);
				itensMeta.add(it);
			}
		}else {
			//Meta Semanal
		
			long totalDeDias = ChronoUnit .DAYS.between(meta.getDtInicio(), meta.getDtFim());
			long totalSemanas = (totalDeDias/7);
			BigDecimal vlParcela = meta.getVlMeta().divide(new BigDecimal(totalSemanas), mt.DECIMAL32);
			LocalDate dataMeta = meta.getDtInicio();
			
			//Gera os itens da meta
			for (int i=1; i <= totalSemanas; i++) {
				dataMeta = dataMeta.plusDays(7);
				ItMeta it = new ItMeta(meta.getDescricao() + " - " + "Semana " + i +"/" + totalSemanas + " - " + dataMeta, i, vlParcela, meta);
				itensMeta.add(it);
			}
		}
			return itensMeta;
	}
	
	/**
	 * Recalcula  o valor para os itens de meta tomando como base o valor atual da meta e 
	 * a quantidade de semanas a partir da data do récalculo
	 * @author elias
	 * @param meta
	 * @since 21/04/2022
	 * @return {@link ArrayList}
	 * */
	public List<ItMeta>reGeraItMeta(Meta meta) {
		List<ItMeta> itensMeta = new ArrayList<ItMeta>();
		List<ItMeta> itensLocalizados = itMetaRepository.findAllItensMeta(meta.getCdMeta());
		LocalDate dataMeta = LocalDate.now();
		
		if (itensLocalizados.size() > 0) { //Recalcula os itens da meta
			
				List<ItMeta> findItMetas = itMetaRepository.findItNaoCreditado(meta.getCdMeta()); //Listando metas não pagas
			
				BigDecimal diferenca = BigDecimal.ZERO;
				BigDecimal totalMeta = BigDecimal.ZERO;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
				
				for (ItMeta itMeta : findItMetas) {
					itMetaRepository.delete(itMeta); //Excluíndo os itens de meta não pagos		
				}
			
				BigDecimal totalCreditado = getTotalItMetaCreditada(meta); 	//Retorna o valor dos itens de meta já creditados 
				BigDecimal novoTotalAcreditar = meta.getVlMeta().subtract(totalCreditado); //Calcula o novo valor a creditar
				MathContext mt = new MathContext(0, RoundingMode.HALF_UP);
			
				diferenca = meta.getVlMeta().subtract(novoTotalAcreditar.add(totalCreditado)); 	//Calculando a diferença
				long totalDeDias = ChronoUnit .DAYS.between(LocalDate.now(), meta.getDtFim()); //Calcula a quantidade de dias entre a data atual e a data fim da meta
				long totalSemanas = (totalDeDias/7);
				long totalMes = (totalDeDias / 30);
			
				//Meta Mensal
				if (meta.getTpMeta().equals("M")) {
					BigDecimal vlParcela = novoTotalAcreditar.divide(new BigDecimal(totalMes), mt.DECIMAL32);
					System.out.println("Diferença : " + diferenca);
					for (int i=1; i <= totalMes; i++) {
						dataMeta = dataMeta.plusMonths(i);
						ItMeta it = new ItMeta(meta.getDescricao() + " - " + "Mês (R) " + i +"/" + totalMes + "  -  " + dataMeta.format(formatter), i, vlParcela, meta);
						itensMeta.add(it);
					}
				}else { 
					//Meta Semanal
					BigDecimal vlParcela = novoTotalAcreditar.divide(new BigDecimal(totalSemanas), mt.DECIMAL32);
					System.out.println("Diferença : " + diferenca);
					for (int i=1; i <= totalSemanas; i++) {
						dataMeta = dataMeta.plusDays(7);
						ItMeta it = new ItMeta(meta.getDescricao() + " - " + "Semana (R) " + i +"/" + totalSemanas + "  -  " + dataMeta.format(formatter), i, vlParcela, meta);
						itensMeta.add(it);
					}
				}
		
		} else { 
			itensMeta = geraItMeta(meta); // Gera novamente todos os itens da meta
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
	
	/**
	 * Método utilizado para retornar a quantidade de Metas ativas
	 * @author elias
	 * @since 30-12-2021
	 * @return {@link Integer}
	 * */
	public Integer qtdMetasAtivas() {
		List<Meta> findAllMetas = metaRepository.findAllMetas();
		return findAllMetas.size();
	}
	
	/**
	 * Retorna o total de valores já creditados
	 * @since 30-12-2022
	 * @author elias
	 * @return {@link BigDecimal} - Total já pago
	 * */
	public BigDecimal getTotalItMetaCreditada(Meta meta) {
		BigDecimal vlrPAgo = BigDecimal.ZERO;
		
		List<ItMeta> metasCreditadas = itMetaRepository.findItMetaCreditada(meta.getCdMeta());
		for (ItMeta itMeta : metasCreditadas) {
			vlrPAgo = vlrPAgo.add(itMeta.getVlrSemana());
		}
		
		return vlrPAgo;
		
	}
}
