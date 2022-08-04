package br.com.faturaweb.fatura.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;

@Controller
@RequestMapping("/configuracoes")
public class ConfirugracoesController {

@Autowired	
private ConfiguracoesRepository configuracoesRepository;
@Autowired
private ContaRepository contaRepository;

@GetMapping("/listar")
public String configuracoes(Model model) {
	Configuracoes config = configuracoesRepository.findConfiguracao();
	List<Conta> contas = contaRepository.findcontas();
	model.addAttribute("config",config);
	model.addAttribute("contas",contas);
	
	return "configuracoes";
	//return "/configuracoes";
}

@PostMapping("salvar")
public  RedirectView  salvar(Model model, Configuracoes formConfiguracoes, 
						@RequestParam("file") MultipartFile file) throws IOException {
   
	Configuracoes config = configuracoesRepository.findConfiguracao();
	List<Conta> contas = contaRepository.findcontas();
   config.setSnParcelado(formConfiguracoes.getSnParcelado());
   if (formConfiguracoes.getSnNotificar()==null) {
	   config.setSnNotificar("N");
   }else {
	   config.setSnNotificar("S");
   }
   
   String nomeArquivoPostado = file.getOriginalFilename();
      
   config.setNrDias(formConfiguracoes.getNrDias());
   config.setDirImportacao(formConfiguracoes.getDirImportacao().replaceAll("'\'", "'/'"));
   config.setLogo(file.getBytes());
   config.setNrMsgDiaria(formConfiguracoes.getNrMsgDiaria());
   config.setLimiteCartao(formConfiguracoes.getLimiteCartao());
   config.setNomeArquivo(nomeArquivoPostado);
   config.setSnNaCompetencia(formConfiguracoes.getSnNaCompetencia());
   config.setNrContaOrigem(formConfiguracoes.getNrContaOrigem());
   configuracoesRepository.save(config);
   config = configuracoesRepository.findConfiguracao();   
   
   model.addAttribute("msg","Configurações salvas com sucesso!");
   model.addAttribute("contas",contas);
   model.addAttribute("config",config);
   
  // RedirectView redirectView = new RedirectView("/configuracoes");
   RedirectView redirectView = new RedirectView("/configuracoes/listar");
	return	 redirectView;
}

@GetMapping("/getimagem")
@ResponseBody
public byte[] getlogo() {
	Configuracoes config = configuracoesRepository.findConfiguracao();
	return config.getLogo();
}


}
 