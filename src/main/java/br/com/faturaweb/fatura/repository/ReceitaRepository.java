package br.com.faturaweb.fatura.repository;

import java.util.ArrayList;
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
	@Query(value = "select * from receita where date_format(dt_recebimento,'%Y') = date_format(curdate(),'%Y')",nativeQuery = true)
	List<Receita> findaAllReceitaAnoCorrente();
	/**
	 * Retornas as receitas do mes Corrente
	 * @author elias
	 * @since 30-12-2021
	 * @return {@link ArrayList}
	 * */
	@Query(value = "SELECT * FROM receita where date_format(dt_recebimento,'%m')=date_format( curdate(),'%m')",nativeQuery = true)
	List<Receita> findAllReceitaMesCorrente();
	
	/**
	 * Retorna a última receita adicionada 
	 * @author elias
	 * @since 20/03/2022
	 * @return {@link Receita}
	 * */
	@Query(value="select * from receita where dt_recebimento = (select max(dt_recebimento) dt_recebimento from fatura.receita)",nativeQuery = true)
	Receita findMaxReceita();
	
}
