package br.com.faturaweb.fatura.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileUpLoadServices {
	

	public void upLoadToLocal(MultipartFile file, Long cdAnexo);

}
