package br.com.faturaweb.fatura.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.faturaweb.fatura.model.ChaveConfig;

@Repository
public interface ChaveRepository extends CrudRepository<ChaveConfig, Long> {

	@Query(value = "SELECT * FROM chave_config where chave = :chave",nativeQuery = true)
	Optional<ChaveConfig> findChaveConfigByDescricao(String chave);
	
	
}
