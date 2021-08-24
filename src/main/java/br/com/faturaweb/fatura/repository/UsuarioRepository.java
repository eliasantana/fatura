package br.com.faturaweb.fatura.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>{

}
