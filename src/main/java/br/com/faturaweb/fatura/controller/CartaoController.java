package br.com.faturaweb.fatura.controller;

import java.util.List;
import java.util.Optional;

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

import com.itextpdf.io.IOException;

import br.com.faturaweb.fatura.model.Cartao;
import br.com.faturaweb.fatura.repository.CartaoRepository;

@Controller
@RequestMapping("/cartao")
public class CartaoController {

	@Autowired
	CartaoRepository cartaoReopsitory;
	
	
	@GetMapping("/cadastro")
	public String cartacao(Model model, Cartao cartaoForm) {
		List<Cartao> listaCartoes = cartaoReopsitory.findAllCartoes();
		Cartao c = new Cartao();
		model.addAttribute("cartao",c);
		model.addAttribute("listacartao",listaCartoes);
		return "cartao_cadastro";
		
	}
	
	@PostMapping("salvar")
	public String salvar(Model model, Cartao cartaoForm, @RequestParam ("file") MultipartFile  file) throws IOException{
	
		byte[] imagem;
		try {
			imagem = file.getBytes();
			if(imagem.length > 0) {
				cartaoForm.setLogoCartao(imagem);
				cartaoForm.setNmArquivoLogo(file.getOriginalFilename());
			}else {
				System.out.println("Arquivo não recebido");
			}
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		cartaoReopsitory.save(cartaoForm);
		List<Cartao> listaCartoes = cartaoReopsitory.findAllCartoes();
		model.addAttribute("cartao", new Cartao());
		model.addAttribute("listacartao",listaCartoes);
	return "cartao_cadastro";
	
	}
	
	@GetMapping("/excluir/{id}")
	public RedirectView excluir(@PathVariable Long id) {
		RedirectView rw = new RedirectView("/cartao/cadastro");
		Optional<Cartao> cartaoLocalizado = cartaoReopsitory.findById(id);
		if (cartaoLocalizado.isPresent()) {
			cartaoReopsitory.delete(cartaoLocalizado.get());			
		}
		
		return rw;
	}
	
	@GetMapping("/alterar/{id}")
	public String alterar(@PathVariable Long id, Model model, Cartao cartao) {
		List<Cartao> listaDeCartoes = cartaoReopsitory.findAllCartoes();
		Optional<Cartao> cartaoLocalizado = cartaoReopsitory.findById(id);
		model.addAttribute("listacartao",listaDeCartoes);
		model.addAttribute("cartao",cartaoLocalizado.get());
				
		return "cartao_cadastro";
	}
	
	@GetMapping("/getimagem/{id}")
	@ResponseBody
	public byte[] getLogoCartao(@PathVariable Long id) {
		Optional<Cartao> cartaoLocalizado = cartaoReopsitory.findById(id);
		Cartao cartao = cartaoLocalizado.get();
		return cartao.getLogoCartao(); 
	}
	
	
	
}
