package br.com.faturaweb.fatura.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Cartao;
import br.com.faturaweb.fatura.repository.CartaoRepository;


@Service
public class CartaoServices {
	@Autowired
	CartaoRepository cartaoReopsitory;

	public void listar(Model model, Cartao cartaoForm) {
		List<Cartao> listaCartoes = cartaoReopsitory.findAllCartoes();
		Cartao c = new Cartao();
		model.addAttribute("cartao",c);
		model.addAttribute("listacartao",listaCartoes);
	}
/**
 * Salva um cartão
 * @since 04-03-2023
 * @author elias
 * @param model
 * @param cartaoForm
 * @param file
 * */
	public void salvar(Model model, Cartao cartaoForm, MultipartFile file) {
		byte[] imagem;
		try {
			imagem = file.getBytes();
			if(imagem.length > 0) {
				cartaoForm.setLogoCartao(imagem);
				cartaoForm.setNmArquivoLogo(file.getOriginalFilename());
			}else {
				Optional<Cartao> cartaoLocalizado = cartaoReopsitory.findById(cartaoForm.getCdCartao());
				if (cartaoLocalizado.isPresent()) {
					cartaoForm.setLogoCartao(cartaoLocalizado.get().getLogoCartao());
					cartaoForm.setDsCartao(cartaoLocalizado.get().getDsCartao());
				}
				System.out.println("Arquivo não recebido");
			}
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		cartaoReopsitory.save(cartaoForm);
		List<Cartao> listaCartoes = cartaoReopsitory.findAllCartoes();
		model.addAttribute("cartao", new Cartao());
		model.addAttribute("listacartao",listaCartoes);
		
	}
	/**
	 * Exclui um cartão
	 * @since 04-03-2023
	 * @author elias
	 * @param id
	 * */	
public RedirectView excluir(Long id) {
	RedirectView rw = new RedirectView("/cartao/cadastro");
	Optional<Cartao> cartaoLocalizado = cartaoReopsitory.findById(id);
	if (cartaoLocalizado.isPresent()) {
		cartaoReopsitory.delete(cartaoLocalizado.get());			
	}
	return rw;
}
/**
 * Altera um cartão
 * @since 04-03-2023
 * @author elias
 * @param id
 * */	
	public void alterar(Long id, Model model, Cartao cartao) {
		List<Cartao> listaDeCartoes = cartaoReopsitory.findAllCartoes();
		Optional<Cartao> cartaoLocalizado = cartaoReopsitory.findById(id);
		model.addAttribute("listacartao", listaDeCartoes);
		model.addAttribute("cartao", cartaoLocalizado.get());		
	}
	/**
	 * Retorna a logo do cartão cadastrado
	 * @since 04-03-2023
	 * @author elias
	 * @param id
	 * */	
	public Cartao getImagem(Long id) {
	Optional<Cartao> cartaoLocalizado = cartaoReopsitory.findById(id);
	Cartao cartao = cartaoLocalizado.get();
	return cartao;
}
	
}
