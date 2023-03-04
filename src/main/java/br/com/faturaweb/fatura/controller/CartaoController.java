package br.com.faturaweb.fatura.controller;

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
import br.com.faturaweb.fatura.services.CartaoServices;

@Controller
@RequestMapping("/cartao")
public class CartaoController {

	@Autowired
	CartaoServices services;

	@GetMapping("/cadastro")
	public String cartacao(Model model, Cartao cartaoForm) {
		services.listar(model, cartaoForm);
		return "cartao_cadastro";

	}

	@PostMapping("salvar")
	public String salvar(Model model, Cartao cartaoForm, @RequestParam("file") MultipartFile file) throws IOException {
		services.salvar(model, cartaoForm, file);
		return "cartao_cadastro";

	}

	@GetMapping("/excluir/{id}")
	public RedirectView excluir(@PathVariable Long id) {
		RedirectView rw = services.excluir(id);
		return rw;
	}

	@GetMapping("/alterar/{id}")
	public String alterar(@PathVariable Long id, Model model, Cartao cartao) {
		services.alterar(id, model, cartao);
		return "cartao_cadastro";
	}

	@GetMapping("/getimagem/{id}")
	@ResponseBody
	public byte[] getLogoCartao(@PathVariable Long id) {
		Cartao cartao = services.getImagem(id);
//		Optional<Cartao> cartaoLocalizado = cartaoReopsitory.findById(id);
//		Cartao cartao = cartaoLocalizado.get();
		return cartao.getLogoCartao();
	}

}
