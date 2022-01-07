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

import br.com.faturaweb.fatura.model.Lancamento;
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
	/*
	@PostMapping("local")
	public void Upload(@RequestParam("file") MultipartFile file) {
		System.out.println("local");
		System.out.println(file.getOriginalFilename());
		fileUploadServices.upLoadToLocal(file);
		
	}
	*/
	@PostMapping("local")
	public RedirectView Upload(@RequestParam("file") MultipartFile file, @RequestParam("cdLancamento") Long codigo) {
		System.out.println("local");
		System.out.println(file.getOriginalFilename());
		System.out.println("codigo"+codigo);
		if (codigo!=null) {
			try {
				Optional<Lancamento> lancamento = lanctoRepository.findById(codigo);
				lancamento.get().setCdAnexo(codigo);
				lanctoRepository.save(lancamento.get());
				
			} catch (Exception e) {
				 System.out.println("Lançamento não lodalizado! (uplocad/local)");
			}
		}
		fileUploadServices.upLoadToLocal(file,codigo);		
		
		RedirectView rw = new RedirectView("http://localhost:8080/listar");
		return rw;
	}
	@PostMapping("db")
	public void UploadDb(@RequestParam("file") MultipartFile file) {
		//
	}
	
}
