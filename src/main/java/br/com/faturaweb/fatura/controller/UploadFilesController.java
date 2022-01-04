package br.com.faturaweb.fatura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.faturaweb.fatura.services.FileUpLoadServices;

@Controller
@RestController
@RequestMapping("upload")
public class UploadFilesController {
	@Autowired
	FileUpLoadServices fileUploadServices;
	
	@PostMapping("local")
	public void Upload(@RequestParam("file") MultipartFile file) {
		System.out.println("local");
		System.out.println(file.getOriginalFilename());
		fileUploadServices.upLoadToLocal(file);
		
	}
	
	@PostMapping("db")
	public void UploadDb(@RequestParam("file") MultipartFile file) {
		//
	}
	
}
