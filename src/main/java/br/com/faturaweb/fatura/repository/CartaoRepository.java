package br.com.faturaweb.fatura.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.faturaweb.fatura.model.Cartao;

@Repository
public interface CartaoRepository extends CrudRepository<Cartao, Long>{

public Optional<Cartao> findBydsCartao(String dsCartao);

@Query(value ="SELECT * FROM cartao",nativeQuery = true )
public java.util.List<Cartao>findAllCartoes();
	
}
