package br.com.faturaweb.fatura.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.ChaveConfig;
import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.repository.ChaveRepository;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.ProvisaoRepository;
import br.com.faturaweb.fatura.services.ConfiguracoesServices;

@Controller
@RequestMapping("/configuracoes")
public class ConfiguracoesController {

@Autowired	
private ConfiguracoesRepository configuracoesRepository;
@Autowired
private ContaRepository contaRepository;
@Autowired
private ProvisaoRepository provisaoRepository;
@Autowired
ChaveRepository chaveRepository;
@Autowired
ConfiguracoesServices services;

@GetMapping("/listar")
public String configuracoes(Model model) {
	Configuracoes config = configuracoesRepository.findConfiguracao();
	List<ChaveConfig> listaDeChaves = chaveRepository.listarTodas();
	List<Conta> contas = contaRepository.findcontas();
	model.addAttribute("config",config);
	model.addAttribute("contas",contas);
	model.addAttribute("contaselecionada",config.getNrContaOrigem());
	model.addAttribute("chave",listaDeChaves);
	
	return "configuracoes";
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
   byte[] imagemForm = file.getBytes();
   //Só adicionará a imagem se for informado no form.
   if (imagemForm.length > 0) {
	   config.setLogo(file.getBytes());
   }
   config.setNrMsgDiaria(formConfiguracoes.getNrMsgDiaria());
   config.setLimiteCartao(formConfiguracoes.getLimiteCartao());
   config.setNomeArquivo(nomeArquivoPostado);
   config.setSnNaCompetencia(formConfiguracoes.getSnNaCompetencia());
   config.setNrContaOrigem(formConfiguracoes.getNrContaOrigem());
   config.setEmailDestino(formConfiguracoes.getEmailDestino());
   config.setEmailOrigem(formConfiguracoes.getEmailOrigem());
   config.setNmDestino(formConfiguracoes.getNmDestino());
   config.setNmOrigem(formConfiguracoes.getNmOrigem());
   config.setTituloMsgEmailDestino(formConfiguracoes.getTituloMsgEmailDestino());
   config.setDirRelatorio(formConfiguracoes.getDirRelatorio());
   configuracoesRepository.save(config);
   config = configuracoesRepository.findConfiguracao();   
   
   model.addAttribute("msg","Configurações salvas com sucesso!");
   model.addAttribute("contas",contas);
   model.addAttribute("config",config);
   
   RedirectView redirectView = new RedirectView("/configuracoes/listar");
	return	 redirectView;
}

@GetMapping("/getimagem")
@ResponseBody
public byte[] getlogo() {
	Configuracoes config = configuracoesRepository.findConfiguracao();
	return config.getLogo();
}
/**
 * Ativa ou de sativa uma chave de configuração
 * @author elias
 * @since 17/03/2023
 * */
@GetMapping("/chave/{chave}")
public RedirectView chaveConfig(@PathVariable  String  chave) {
	RedirectView rw = 	services.getChave(chave);  
		return rw;
}
	

}
 