package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.ItLote;
import br.com.faturaweb.fatura.model.LogProvisao;
import br.com.faturaweb.fatura.model.Lote;
import br.com.faturaweb.fatura.model.Provisao;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.ItLoteRepository;
import br.com.faturaweb.fatura.repository.LogProvisaoRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;
import br.com.faturaweb.fatura.repository.ProvisaoRepository;
import br.com.faturaweb.fatura.services.LoteServices;

@Controller
@RequestMapping("/lotecontabil")
public class LoteContabilController {
	@Autowired
	LoteRepository loterepository;
	@Autowired
	ItLoteRepository itloteRepository;
	@Autowired
	ProvisaoRepository provisaoRepository;
	@Autowired
	LogProvisaoRepository logProvisaoRepository;
	@Autowired
	ContaRepository contaRepository;
	@Autowired
	LoteServices loteServices;
	
	@GetMapping(value = {"/pesquisar","/pesquisar/{flag}/{desc}"})
	public String pesquisar(Model model, 
											@PathVariable(name = "flag", required = false ) String flag ,
											@PathVariable(name = "desc", required = false ) String desc 
											) {
		List<Provisao> provisoesDaCompetencia = provisaoRepository.findAllProvisao();
		List<LogProvisao> logProvisao = logProvisaoRepository.findAllLogProvisaoDaCompetencia();
		List<Lote> lotes = loterepository.findAllLote();
		List<Conta> contasLocalizadas = contaRepository.findcontas();
		String competenciaLote = null;
		BigDecimal totalizacaoDoLote = BigDecimal.ZERO;
		BigDecimal totalizacaoProvisaoCompetencia = BigDecimal.ZERO;
		BigDecimal totalSaldoLotesGerados = BigDecimal.ZERO;
		if (lotes.size()>0 ) {
			if ( loterepository.findLoteCompetencia()!=null) {
				Lote loteDaCompetencia = loterepository.findLoteCompetencia();
				model.addAttribute("status",loteDaCompetencia.getStatus());
			    totalSaldoLotesGerados = loteServices.getTotalizaLotes(lotes);
			}
		}else {
			model.addAttribute("status","F");
		}
		if (flag!=null) {
			long id = Long.parseLong(flag);
			List<ItLote> itensDoLote = itloteRepository.findAllItLote(id);
			totalizacaoDoLote = loteServices.getTotalizacaoItLote(itensDoLote);
			Optional<Lote> lote = loterepository.findById(id);
			if(lote.isPresent()) {
				String dsLote = lote.get().getDsLote();
				dsLote =  dsLote.replace(" ", "");
				competenciaLote = dsLote.substring(dsLote.length()-6);
				List<LogProvisao> logProvisaoLocalizado = logProvisaoRepository.findLogProvisaoDaCompetencia(competenciaLote);
				totalizacaoProvisaoCompetencia= loteServices.totalizaLogProvisao(logProvisaoLocalizado);
				model.addAttribute("totallogprovisao",totalizacaoProvisaoCompetencia);
				model.addAttribute("logprovisao",logProvisaoLocalizado);
				model.addAttribute("status",lote.get().getStatus());
			}else {
				model.addAttribute("logprovisao",logProvisao);
			}
				
			model.addAttribute("itlote", itensDoLote);
			model.addAttribute("desc",desc);
			model.addAttribute("contas",contasLocalizadas);
			
		}else {
			List<ItLote> listaVazia = new ArrayList<ItLote>();
			ItLote loteVazio = new ItLote();
			listaVazia.add(loteVazio);
			model.addAttribute("itlote", listaVazia);
		}
		
		model.addAttribute("lotes",lotes);
		model.addAttribute("flag",flag);
		model.addAttribute("totalizacaodolote",totalizacaoDoLote);
		model.addAttribute("totallogprovisao",totalizacaoProvisaoCompetencia);
		model.addAttribute("provisao",provisoesDaCompetencia);
		model.addAttribute("totalsaldolote",totalSaldoLotesGerados);
	    //model.addAttribute("logprovisao",logProvisao);
		return "lotecontabil";
	}
	
	@GetMapping("/processar")
	public RedirectView prcProcessaFechamentoContabil() {
		RedirectView rw = new RedirectView("/lotecontabil/pesquisar");
		SimpleDateFormat df = new SimpleDateFormat("MMYYYY");
		String competenciaAtual = df.format(new Date());
		try {
			loterepository.prcProcessaFechamentoContabil(competenciaAtual);
		} catch (Exception e) {
			
		}
		return rw;
	}
	
	@GetMapping("/operacao/{operacao}")
	public RedirectView fechaLoteContabil(@PathVariable(name = "operacao") String operacao) {
		RedirectView rw = new RedirectView("/lotecontabil/pesquisar");
		Lote loteDaCompetencia = loterepository.findLoteCompetencia();
		if (loteDaCompetencia!=null) {
			loteDaCompetencia.setStatus(operacao);
			loterepository.save(loteDaCompetencia);
		}
		return rw;
	}
/**
 * Reprocessa o lote contábil da competencia
 * @author elias
 * @since - 12/10/2022
 * */	
@GetMapping("/reprocessar")
public RedirectView reprocessar() {
	RedirectView rw = new RedirectView("/lotecontabil/pesquisar");
			loterepository.reprocessar();
	return rw;		
}

}
