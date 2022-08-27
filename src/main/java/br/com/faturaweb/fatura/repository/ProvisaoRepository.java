package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.hibernate.type.TrueFalseType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.faturaweb.fatura.model.Provisao;

public interface ProvisaoRepository extends CrudRepository<Provisao, Long>{

	/**
	 * Retorna os itens de provisao Cadastrados em Configurações
	 * */
	@Query(value = "select * from provisao",nativeQuery = true)
	List<Provisao>findAllProvisao();
	
	@Query(value  = "select * from provisao where cd_provisao = :id",nativeQuery = true)
	Provisao findProvisaoByID(Long id);
	
	
}
