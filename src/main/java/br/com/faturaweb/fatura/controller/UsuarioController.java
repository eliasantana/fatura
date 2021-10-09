package br.com.faturaweb.fatura.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.repository.UsuarioRepository;
import br.com.faturaweb.fatura.services.ReportService;

@Controller
@EnableAutoConfiguration
@RequestMapping("usuario")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ReportService service;

	@GetMapping("cadastro")
	public String cadastro(Model model) {
		Usuario u = new Usuario();
		model.addAttribute("usuario", u);
		return "usuario/form-usuario";
	}

	@PostMapping("adicionar")
	public RedirectView adicionar(@Valid Usuario usuario, Model model) {
		RedirectView rw = new RedirectView("http://localhost:8080/usuario/listar");

		if (usuario.getCdUsuario() != null) {
			
			
			System.out.println("Alterando o usuário");
			try {
				Optional<Usuario> usuarioLocalizado = usuarioRepository.findById(usuario.getCdUsuario());
				System.out.println("Usuário localizado!" +   usuarioLocalizado.toString());
				
				Usuario usuarioForm = new Usuario();
				usuarioForm.setCdUsuario(usuarioLocalizado.get().getCdUsuario());
				usuarioForm.setDtCardastro(usuarioLocalizado.get().getDtCardastro());
				usuarioForm.setNome(usuario.getNome());
				usuarioForm.setEmail(usuario.getEmail());
				usuarioForm.setLogin(usuario.getLogin());
				usuarioForm.setSenha(usuario.getSenha());
				usuarioForm.setSnAtivo(usuario.getSnAtivo());				
				System.out.println("Usuario form: " + usuarioForm.toString());
				
				usuarioRepository.save(usuarioForm);
				
			} catch (Exception e) {
				
			}
			
		}else {
			 usuario.setDtCardastro(LocalDate.now());
			 usuarioRepository.save(usuario);
		}

		return rw;

	}

	@GetMapping("listar")
	public String listar(Model model) {
		try {
			List<Usuario> todosOsUusuarios = usuarioRepository.listarTodos();
			model.addAttribute("usuarios", todosOsUusuarios);
		} catch (Exception e) {

		}
		return "usuario/listar-usuario";
	}

	@GetMapping("excluir/{id}")
	public RedirectView excluir(@PathVariable Long id, Usuario usuario) {

		RedirectView rw = new RedirectView("http://localhost:8080/usuario/listar");
		Optional<Usuario> usuarioLocalizado = usuarioRepository.findById(id);
		if (usuarioLocalizado.get().getCdUsuario() != null) {
			usuarioRepository.delete(usuarioLocalizado.get());
			System.out.println(usuarioLocalizado.get().getNome() + "  excluído com sucesso! ");
		}
		return rw;
	}

	@GetMapping("alterar/{id}")
	public String alterar(@PathVariable Long id, Model model) {

		Optional<Usuario> usuarioLocalizado = usuarioRepository.findById(id);
		Usuario u = usuarioLocalizado.get();
		model.addAttribute("usuario", u);
		System.out.println(id);
		return "usuario/form-usuario";
	}
	/*
	@GetMapping("relatorio/{formato}")
	public RedirectView relatorio(@PathVariable String formato) throws NotFoundException, FileNotFoundException, JRException {
		
		List<Usuario> todosOsUsuarios = usuarioRepository.listarTodos();
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(todosOsUsuarios);
		String nomeRelatorio = service.exportReport(formato, "relUsuario", beanCollectionDataSource);
		RedirectView rw = new RedirectView("http://localhost:8080/usuario/download/"+nomeRelatorio);
		return rw;
	}
	*/
	/*
	@GetMapping("download/{nomerelatorio}")
	public ResponseEntity showPdf(@PathVariable String nomerelatorio) {
		
		ResponseEntity responseEntity = service.download(nomerelatorio);
		return responseEntity;
	}
	*/
}
