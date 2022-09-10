package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.ItLote;
import br.com.faturaweb.fatura.model.LogProvisao;
import br.com.faturaweb.fatura.model.Lote;

@Service
public class LoteServices {

public BigDecimal getTotalizacaoItLote(List<ItLote>itensDoLote) {
	BigDecimal total = BigDecimal.ZERO;
	
	if (itensDoLote.size() > 0) {
		
		for (ItLote itLote : itensDoLote) {
			total = total.add(itLote.getLancamento().getVlPago());
		}
		
	}
	return total;
}

public BigDecimal totalizaLogProvisao(List<LogProvisao> logProvisaoDaCompetencia) {
	BigDecimal total = BigDecimal.ZERO;
	if (logProvisaoDaCompetencia.size()>0) {
		for (LogProvisao logProvisao : logProvisaoDaCompetencia) {
			total = total.add(logProvisao.getVlProvisionado());
		}
	}
	return total;
}
/**
 * Retorana a totalização dosaldo dos lotes Gerados
 * @author elias
 * @since 08-09-2922
 * @param lotes
 * */	

public BigDecimal getTotalizaLotes(List<Lote> lotes) {
	BigDecimal total = BigDecimal.ZERO;
	if (lotes.size() > 0) {
		for (Lote lote : lotes) {
			total = total.add(lote.getVlSaldo());
		}
	}
	return total;
}

}
