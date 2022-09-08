package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.ItLote;
import br.com.faturaweb.fatura.model.LogProvisao;

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
	
}
