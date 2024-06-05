package br.com.faturaweb.fatura.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.services.FileUpLoadServices;

@Controller
@RestController
@RequestMapping("upload")
public class UploadFilesController {
	@Autowired
	FileUpLoadServices fileUploadServices;
	@Autowired
	LancamentoRepository lanctoRepository;
	@Autowired
	ConfiguracoesRepository configRepository;
	
	@PostMapping("local")
	public RedirectView Upload(@RequestParam("file") MultipartFile file, @RequestParam("cdLancamento") Long codigo) {
		if (codigo!=null) {
			try {
				Configuracoes config = configRepository.findConfiguracao();
				System.out.println(config.getDirImportacao());
				Optional<Lancamento> lancamento = lanctoRepository.findById(codigo);				
				lancamento.get().setDsAnexo(config.getDirImportacao().concat(file.getOriginalFilename()));
				lanctoRepository.save(lancamento.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		fileUploadServices.upLoadToLocal(file,codigo);		
		RedirectView rw = new RedirectView("/listar");
		return rw;
	}
}
