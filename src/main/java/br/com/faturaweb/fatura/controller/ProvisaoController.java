package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.Provisao;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.ProvisaoRepository;

@Controller
@RequestMapping("/provisao")
public class ProvisaoController {
	@Autowired
	ProvisaoRepository provisaoRepository;
	@Autowired
	ContaRepository contaRepository;
	
	@GetMapping(value={"/listar","/listar/{msg}"})
	public String provisaoListar(Model model, Provisao provisao, @PathVariable(name = "msg", required = false) String msg) {
		List<Conta> contas = contaRepository.findcontas();
		List<Provisao> provisoes = provisaoRepository.findAllProvisao();
		
		model.addAttribute("contas",contas);
		model.addAttribute("provisoes",provisoes);
		model.addAttribute("mensagem",msg);
		
		return "provisao";
	}
	
	@PostMapping("/salvar")
	public String provisaoSalvar(Model model, Provisao provisao, 
																	@RequestParam(name = "cdProvisao", required = false) Long id,
																	@RequestParam(name = "dsprovisao" ) String   dsprovisao,
																	@RequestParam(name="percentual") String percentual,
																	@RequestParam(name="nrconta") String nrconta)
	{
		 
		List<Conta> contas = contaRepository.findcontas();
		List<Provisao> provisoes = provisaoRepository.findAllProvisao();
		Optional<Conta> contaLocalizada = contaRepository.findConta(nrconta);
		BigDecimal totalPercent =  BigDecimal.ZERO;
		 String flag=""; //A - Alteração
		 
		//Extrair método para o service para simplificar a lógica
		 //Calcula o percentual total já cadastrado
		for (Provisao prov : provisoes) {
			System.out.println(" Valor "+totalPercent);
			totalPercent = totalPercent.add(prov.getPercentual());
		}
		totalPercent = totalPercent.multiply( new BigDecimal(100)); //Converte para inteiro 
		
		totalPercent = totalPercent.add(new BigDecimal(percentual)); //Adiciona o percentual informado ao total calculado
		System.out.println(" Total Calculado -> " + totalPercent);
		
		BigDecimal max = new BigDecimal(100);
		if ((totalPercent.compareTo(max)==0)) { // Se o total + o percentual informado for <=100%

				  Provisao p = new Provisao();
				  if (id!=null) {
					  flag="A";
					  //Alterando a provisao
					  	p.setCdProvisao(id);
					  	p.setDsProvisao(dsprovisao);
					  	BigDecimal bPercentual = new BigDecimal(percentual).divide(new BigDecimal(100));
					  	p.setPercentual(bPercentual);
					  	p.setNrConta(nrconta);
				  }else {
					  //Novo Cadastro
					  p.setDsProvisao(dsprovisao);
					  BigDecimal bPercentual = new BigDecimal(percentual).divide(new BigDecimal(100));
					  p.setPercentual(bPercentual);
					  p.setNrConta(contaLocalizada.get().getNrConta());
					  
				  }
			
		  		provisaoRepository.save(p);
		  		
		  		if (flag.equals("A")) {
					model.addAttribute("mensagem", "A provisão [ "+p.getDsProvisao() +" ] foi alterada com sucesso!");	
				}else {
					model.addAttribute("mensagem", "A provisão [ "+p.getDsProvisao() +" ] foi salva com sucesso!");	
				}
			
		}else {
			model.addAttribute("mensagem", "O Percentual informado ultrapassa o acumulado de 100%");	
		}
		provisoes =  provisaoRepository.findAllProvisao();
		model.addAttribute("contas",contas);
		model.addAttribute("provisoes",provisoes);
		
		return "provisao";
	}
	
	@GetMapping("/editar/{id}")
	public String provisaoEditar(Model model, @PathVariable Long id) {
	    Provisao provisaoLocalizada = provisaoRepository.findProvisaoByID(id);
		List<Conta> contas = contaRepository.findcontas();
		List<Provisao> provisoes = provisaoRepository.findAllProvisao();
		
		
		model.addAttribute("provisoes",provisoes);
		model.addAttribute("contas",contas);
		model.addAttribute("codigo",provisaoLocalizada.getCdProvisao());
		model.addAttribute("descricao",provisaoLocalizada.getDsProvisao());
		model.addAttribute("percentual",provisaoLocalizada.getPercentual().multiply(BigDecimal.valueOf(100)));
		model.addAttribute("nrconta",provisaoLocalizada);
		
		
		return "provisao";
	}
	
	@GetMapping("/excluir/{id}")
	public RedirectView provisaoExcluir(Model model, @PathVariable Long id) {
		Provisao provisaoExcluida = provisaoRepository.findProvisaoByID(id);
		String msg="Exclusão realizada com sucesso!";
		RedirectView rw  = new RedirectView("/provisao/listar/"+msg);
		
		
		Optional<Provisao> provisao = provisaoRepository.findById(id);
		if (provisao.isPresent()) provisaoRepository.delete(provisao.get());
		
		return rw;
	}
	
}
