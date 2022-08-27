package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.ItLote;

public interface ItLoteRepository extends CrudRepository<ItLote, Long>{

	/**
	 * Retorna todos os itens do lote
	 * */
	@Query(value = "select * from it_lote where lote_cd_lote=:id", nativeQuery = true)
	List<ItLote>findAllItLote(Long id);
}
