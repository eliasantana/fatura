package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.repository.LancamentoRepository;

@Service
public class LancamentoServices {
	@Autowired
	LancamentoRepository lancamentoRepository;
	
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
	
}
