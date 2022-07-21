package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.ItMeta;
import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.model.Meta;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.ItMetaRepository;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;
import br.com.faturaweb.fatura.repository.MetaRepository;
import br.com.faturaweb.fatura.services.MetaService;

@Controller
@RequestMapping("meta")
public class MetaController {
@Autowired	
ContaRepository contaRepository;
@Autowired
MetaRepository metaRepository;
@Autowired
ItMetaRepository ItMetaRepository;
@Autowired
MetaService metaServices;
@Autowired
LogMovimentacaoFinanceiraRepository logServices;

	@GetMapping("/listar")
	public String listar(Model model, Meta meta) {
	   List<Conta> contas = contaRepository.findcontas();
	   List<Meta> metas = metaRepository.findAllMetas();
	   Conta c= new Conta();
	   Meta m = new Meta();
	   m.setConta(c);
	   model.addAttribute("meta",m);
	   model.addAttribute("contas",contas);
	   model.addAttribute(c);
	   model.addAttribute("mensagem",null);
	   model.addAttribute("metas",metas);
	   
		return "meta";
	}
	
	@PostMapping("/salvar")
	public RedirectView salvar(Model model, Meta metaForm, Conta conta) {
		RedirectView rw = new RedirectView("/meta/listar");
		List<Conta> contas = contaRepository.findcontas();
		 List<Meta> metas = metaRepository.findAllMetas();
		try {
			Optional<Conta> contaLocalizada = contaRepository.findConta(conta.getNrConta());
			if (contaLocalizada.isPresent()) {
				metaForm.setConta(contaLocalizada.get());
			}
		     Conta c= new Conta();
			 Meta m = new Meta();
			model.addAttribute("meta",m);
			model.addAttribute(c);
			model.addAttribute("contas",contas);
			model.addAttribute("mensagem","Meta salva com sucesso!");  
			model.addAttribute("metas",metas);
			List<ItMeta> itensDaMEta = metaServices.geraItMeta(metaForm);
			metaForm.setItMeta(itensDaMEta);
			metaRepository.save(metaForm);
			
			
		} catch (Exception e) {
			
		}
		return rw;
	}
	
	@GetMapping("/teste")
	public String teste(Model model) {
	  Conta c = new Conta();	
	  c.setNrConta("1007048-1");
	  c.setNrAgencia("1783-3");
	  c.setSaldo(BigDecimal.ZERO);
	  ArrayList<ItMeta> iteMetas = new  ArrayList<ItMeta>();
	  Meta meta = new Meta();
	  meta.setConta(c);
	  meta.setDescricao("Gramado 2022");
	  meta.setDtFim(LocalDate.of(2022, 8, 31));
	  meta.setDtInicio(LocalDate.of(2022, 1, 1));
	  meta.setSnAtivo("S");
	  meta.setVlMeta(new BigDecimal(6000));
	  meta.setItMeta(iteMetas);
	  meta.setCdMeta(13L);
	 List<ItMeta> itemCreditados = ItMetaRepository.findItMeta(13L);
	BigDecimal totalItMeta = metaServices.totalizaItMeta(itemCreditados);
    BigDecimal andamentoMeta = metaServices.andamentoMeta(meta, totalItMeta); 
	System.out.println("Totalização dos Itens da Meta: " + totalItMeta);
	System.out.println("Andamento da Meta: " + andamentoMeta);
	
	// Dados do Gráfico 
	
	model.addAttribute("totalmeta",totalItMeta);
	model.addAttribute("andamento", andamentoMeta);
	model.addAttribute("titulo", "Meta 2021");
	model.addAttribute("descricao"," Resta para a meta "+meta.getDescricao());
	
	List<ItMeta> itensDaMEta = metaServices.geraItMeta(meta);
	System.out.println("Itens gerados: "+itensDaMEta.size());
	 for (ItMeta itMeta : itensDaMEta) {
		itMeta.setCdItMeta(meta.getCdMeta());
		System.out.println(itMeta.getCdItMeta() + " " + itMeta.getDescricao());
	}
	ItMetaRepository.saveAll(itensDaMEta);
		return "teste";
		
	}
	
	@GetMapping("excluir/{id}")
	public RedirectView exclulir (@PathVariable Long id, Model model) {
		RedirectView rw = new RedirectView("/meta/listar");
		try {
			Optional<Meta> meta = metaRepository.findById(id);
			if (meta.isPresent()) {
				model.addAttribute("mensagem", " A meta " + meta.get().getCdMeta() + " foi excluída com sucesso!");
				LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();
				log.setDescricao("Excluisão do meta " +meta.get().getCdMeta()+ " - "+ meta.get().getDescricao() + " Valor " + meta.get().getVlMeta());
				log.setVlMovimentado(meta.get().getVlMeta());
				log.setTpMovimentacao("D");
				log.setNrConta(meta.get().getConta().getNrConta());
				log.setDtMovimentacao(LocalDate.now());
				logServices.save(log);
				metaRepository.delete(meta.get());
			}
		} catch (Exception e) {
			
		}
		return rw;
	}
	
	/**
	 * Encerra uma meta e exclui os itens não creditados
	 * @param idMeta - Código da Meta 
	 * @author elias
	 * @since 21/07/2022
	 * */	
	@GetMapping("/encerrar/{idMeta}")
	public RedirectView encerrarMeta(@PathVariable Long idMeta, Model model) {
		RedirectView rw = new RedirectView("/meta/listar");
		Optional<Meta> metaLocalizada = metaRepository.findById(idMeta);

		if (metaLocalizada.isPresent()) {
			Meta meta = metaLocalizada.get();
			meta.setSnAtivo("N");
			metaRepository.save(meta);

			List<ItMeta> itensNaoCreditados = ItMetaRepository.findItNaoCreditado(idMeta);
			ItMetaRepository.deleteAll(itensNaoCreditados);
		}
		
		
		List<Meta> metas = metaRepository.findAllMetas();
		 model.addAttribute("metas",metas);
		return rw;
	}
	
}
