package br.com.faturaweb.fatura.controller;

import java.text.SimpleDateFormat;
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

import br.com.faturaweb.fatura.model.ItLote;
import br.com.faturaweb.fatura.model.LogProvisao;
import br.com.faturaweb.fatura.model.Lote;
import br.com.faturaweb.fatura.model.Provisao;
import br.com.faturaweb.fatura.repository.ItLoteRepository;
import br.com.faturaweb.fatura.repository.LogProvisaoRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;
import br.com.faturaweb.fatura.repository.ProvisaoRepository;

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
	
	@GetMapping(value = {"/pesquisar","/pesquisar/{flag}/{desc}"})
	public String pesquisar(Model model, 
											@PathVariable(name = "flag", required = false ) String flag ,
											@PathVariable(name = "desc", required = false ) String desc 
											) {
		List<Provisao> provisoesDaCompetencia = provisaoRepository.findAllProvisao();
		List<LogProvisao> logProvisao = logProvisaoRepository.findAllLogProvisaoDaCompetencia();
		List<Lote> lotes = loterepository.findAllLote();
		if (lotes.size()>0) {
			Lote loteDaCompetencia = loterepository.findLoteCompetencia();
			model.addAttribute("status",loteDaCompetencia.getStatus());
		}else {
			model.addAttribute("status","F");
		}
		if (flag!=null) {
			long id = Long.parseLong(flag);
			List<ItLote> itensDoLote = itloteRepository.findAllItLote(id);
			model.addAttribute("itlote", itensDoLote);
			model.addAttribute("desc",desc);
		}else {
			List<ItLote> listaVazia = new ArrayList<ItLote>();
			ItLote loteVazio = new ItLote();
			listaVazia.add(loteVazio);
			model.addAttribute("itlote", listaVazia);
		}
		
		model.addAttribute("lotes",lotes);
		model.addAttribute("flag",flag);
	    model.addAttribute("logprovisao",logProvisao);
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
	
	

}
