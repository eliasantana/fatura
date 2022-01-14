package br.com.faturaweb.fatura.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.Teste;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.TesteRepository;
import io.lettuce.core.dynamic.annotation.Param;

@Controller
public class testeController {

		@Autowired
		LancamentoRepository r;
		@Autowired
		TesteRepository testeRepository;
		@GetMapping("/teste")
		public String teste(Model model) {
			LocalDate localDate = LocalDate.now();
		    for (int i=0; i < 7; i++) {
		    	localDate = localDate.plusDays(7);
		    	System.out.println(localDate);
		    }
	
			return "teste";
		}
		
		@GetMapping("/ds2")
		public String teste2() {
			
			return "home/dashboard2";
		}
		
		@GetMapping("/divide")
		public RedirectView divide() {
		
			RedirectView rw = new RedirectView("http://localhost/teste");
			return  rw;
		}
		
		@PostMapping("/teste/arquivo")
		public String arquivo(Model model, @RequestParam("file") MultipartFile file) {
		
		
		file.getOriginalFilename();
		   Teste teste = new Teste();
		   try {
			teste.setAnexo(file.getBytes());
			testeRepository.save(teste);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao tentar salvar a imagem no banco");
		}
		    model.addAttribute("teste", teste);
			return "teste";
		}
		
		@GetMapping("imagem/{id}")
		@ResponseBody
		public byte[] getImagem(@PathVariable Long id) {
			Optional<Teste> imagem = testeRepository.findById(1L);
			System.out.println("Exibindo a imamgem");
			return imagem.get().getAnexo();
		}
}
