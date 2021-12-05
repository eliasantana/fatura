package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.faturaweb.fatura.model.Receita;
@Repository
public interface ReceitaRepository extends CrudRepository<Receita, Long>{

	@Query("SELECT r from Receita r")
	List<Receita> findAllList();
	/**
	 * Retorna as receitas do ano Atual
	 * @author elias
	 * @since 04/12/2021
	 * @return {@link List}
	 * */
	@Query(value = "select * from fatura.receita where date_format(dt_recebimento,'%Y') = date_format(curdate(),'%Y')",nativeQuery = true)
	List<Receita> findaAllReceitaAnoCorrente();
	
}
