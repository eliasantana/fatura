package br.com.faturaweb.fatura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.faturaweb.fatura.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
	@Query("SELECT u from Usuario u WHERE u.nome =:nome")
	Optional<Usuario>findByNomeUsuario(String nome);
	
	@Query("SELECT u from Usuario u WHERE u.senha=:password")
	Optional <Usuario>findByPassword(String password);

	@Query("SELECT u from Usuario u")
	List<Usuario> listarTodos();
	
	
}
