package br.com.faturaweb.fatura.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
@Service
public class UploadServicesImpl implements FileUpLoadServices {
	@Autowired
	ConfiguracoesRepository config;
	
	private String urlLocal="";
	@Override
	public void upLoadToLocal(MultipartFile file, Long cdAnexo) {
		urlLocal = config.findConfiguracao().getDirImportacao().replace("\\", "/");
		//urlLocal  = urlLocal.concat("/uploaded_cdAnexo_".concat(cdAnexo.toString() +"_"));
		urlLocal  = urlLocal.concat("/");
		
		try {
			byte[]data = file.getBytes();
			Path path =  Paths.get(urlLocal+file.getOriginalFilename());
			Files.write(path,data);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}





}
