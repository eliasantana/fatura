package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.Meta;

/**
 * Retorna todas as metas ativas cadastradas
 * */
public interface MetaRepository extends CrudRepository<Meta, Long>{
@Query("Select m FROM Meta m WHERE m.snAtivo='S'")
	List<Meta>findAllMetas();
}
