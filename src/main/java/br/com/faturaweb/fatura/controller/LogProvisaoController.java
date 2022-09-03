package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.LogProvisao;
import br.com.faturaweb.fatura.model.Lote;
import br.com.faturaweb.fatura.model.Provisao;
import br.com.faturaweb.fatura.repository.LogProvisaoRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;
import br.com.faturaweb.fatura.repository.ProvisaoRepository;

@Controller
@RequestMapping("/log")
public class LogProvisaoController {

	@Autowired
	LoteRepository loteRepository;
	@Autowired
	ProvisaoRepository provisaoRepository;
	@Autowired
	LogProvisaoRepository logProvisaoRepository;
	
	
@GetMapping(value = "/provisionar")	
public RedirectView provisionar(Model model, @PathVariable(name = "msg", required = false) String smg) {
Lote loteLocalizado = loteRepository.findLoteCompetencia();
RedirectView rw = new RedirectView("/lotecontabil/pesquisar/");

List<LogProvisao> provisoesDaCompetencia = logProvisaoRepository.findAllLogProvisaoDaCompetencia();
//Verifica se o lote está aberto
if (loteLocalizado.getStatus().equals("A")) {
	
List<LogProvisao> logProvisaoCompetencia = logProvisaoRepository.findAllLogProvisaoDaCompetencia();
if (logProvisaoCompetencia.size() >0) logProvisaoRepository.deleteAll(logProvisaoCompetencia);
List<Provisao> provisoes = provisaoRepository.findAllProvisao();
List<LogProvisao>logProvisoes = new ArrayList<LogProvisao>();

	if (loteLocalizado!=null && provisoes.size() >0) {
		BigDecimal vlSaldo = loteLocalizado.getVlSaldo();
		BigDecimal diferenca = BigDecimal.ZERO;
		int qtd = provisoes.size(); //Quantidade de provisõese
		BigDecimal total = BigDecimal.ZERO;
		/**
		 * Iterando sobre as prpovisões 
		 * */
			for (Provisao provisao2 : provisoes) {
				
				LogProvisao lp = new LogProvisao();
				lp.setCdProvisao(provisao2.getCdProvisao());
				SimpleDateFormat df = new SimpleDateFormat("MMYYYY");
				String competencia = df.format(new Date());
				System.out.println(competencia);
				lp.setCompetencia(competencia);
				lp.setDtInclusao(LocalDateTime.now());
				lp.setSnCreditado("N");
				lp.setVlProvisionado(provisao2.getPercentual().multiply(vlSaldo, MathContext.DECIMAL32));
				lp.setNrConta(provisao2.getNrConta());
				logProvisoes.add(lp);
				total = total.add(lp.getVlProvisionado(),MathContext.DECIMAL32);
		}
			//Verificando a diferença do saldo do lote e o total provisionado
			if (vlSaldo.compareTo(total)>-1) {				
				 diferenca = vlSaldo.subtract(total);				 
				 diferenca = diferenca.divide(new BigDecimal(qtd),MathContext.DECIMAL32);
			}else {				 
				 diferenca = total.subtract(vlSaldo);
				 diferenca = diferenca.divide(new BigDecimal(qtd),MathContext.DECIMAL32);
			}
			//Aplicando a diferença
	       for (LogProvisao logProvisao : logProvisoes) {
			  logProvisao.setVlProvisionado(logProvisao.getVlProvisionado().add(diferenca));
	       }
	       
	       logProvisaoRepository.saveAll(logProvisoes);
	}
		
	       model.addAttribute("provisoes",provisoesDaCompetencia);
	       
	}
	
	
	return rw;
}

/**
 * Muda o status dos valores provisionados
 * */
@GetMapping("/creditar")
public RedirectView creditarProvisionamento() {
	RedirectView rw = new RedirectView("/lotecontabil/pesquisar");
	List<LogProvisao> provisoesDaCompetencia = logProvisaoRepository.findAllLogProvisaoDaCompetencia();
	List<LogProvisao>listaDeProvisoes = new ArrayList<LogProvisao>();
	if (!loteRepository.findLoteCompetencia().getStatus().equals("F")) {
		if (provisoesDaCompetencia.size() >0) {
			for (LogProvisao logProvisao : provisoesDaCompetencia) {
				logProvisao.setSnCreditado("S");
				listaDeProvisoes.add(logProvisao);
			   //Creditar valores nas contas informadas	
			}
	}
		logProvisaoRepository.saveAll(listaDeProvisoes);
		
	}
	return rw;
}

}
