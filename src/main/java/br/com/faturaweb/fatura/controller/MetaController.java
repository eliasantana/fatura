package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import br.com.faturaweb.fatura.model.Meta;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.ItMetaRepository;
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
		RedirectView rw = new RedirectView("http://localhost:8080/meta/listar");
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
	public String teste() {
	  Conta c = new Conta();	
	  c.setNrConta("1007048-1");
	  c.setNrAgencia("1783-3");
	  c.setSaldo(BigDecimal.ZERO);
	  ArrayList<ItMeta> iteMetas = new  ArrayList<ItMeta>();
	  Meta meta = new Meta();
	  meta.setConta(c);
	  meta.setDescricao("teste");
	  meta.setDtFim(LocalDate.of(2022, 8, 31));
	  meta.setDtInicio(LocalDate.of(2022, 1, 1));
	  meta.setSnAtivo("S");
	  meta.setVlMeta(new BigDecimal(6000));
	  meta.setItMeta(iteMetas);
	  
	 List<ItMeta> itemCreditados = ItMetaRepository.findItMeta(11L);
	BigDecimal totalItMeta = metaServices.totalizaItMeta(itemCreditados);
    BigDecimal andamentoMeta = metaServices.andamentoMeta(meta, totalItMeta); 
	System.out.println("Totalização dos Itens da Meta: " + totalItMeta);
	System.out.println("Andamento da Meta: " + andamentoMeta);
	
		return "teste";
		
	}
	
	@GetMapping("excluir/{id}")
	public RedirectView exclulir (@PathVariable Long id, Model model) {
		RedirectView rw = new RedirectView("http://localhost:8080/meta/listar");
		try {
			Optional<Meta> meta = metaRepository.findById(id);
			if (meta.isPresent()) {
				model.addAttribute("mensagem", " A meta " + meta.get().getCdMeta() + " foi excluída com sucesso!");
				metaRepository.delete(meta.get());
			}
		} catch (Exception e) {
			
		}
		return rw;
	}
	
}
