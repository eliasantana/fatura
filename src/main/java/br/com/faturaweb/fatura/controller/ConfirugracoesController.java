package br.com.faturaweb.fatura.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

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
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;

@Controller
@RequestMapping("/configuracoes")
public class ConfirugracoesController {

@Autowired	
private ConfiguracoesRepository configuracoesRepository;

@GetMapping("/listar")
public String configuracoes(Model model) {
	Configuracoes config = configuracoesRepository.findConfiguracao();
	model.addAttribute("config",config);
	
	return "home/configuracoes";
}

@PostMapping("salvar")
public  RedirectView  salvar(Model model, Configuracoes formConfiguracoes, @RequestParam("file") MultipartFile file) throws IOException {
   
	Configuracoes config = configuracoesRepository.findConfiguracao();
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
   configuracoesRepository.save(config);
   config = configuracoesRepository.findConfiguracao();   
   
   model.addAttribute("msg","Configurações salvas com sucesso!");
   model.addAttribute("config",config);
   
   RedirectView redirectView = new RedirectView("http://localhost:8080/configuracoes");
	return	 redirectView;
}



}
 