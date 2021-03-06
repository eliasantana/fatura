package br.com.faturaweb.fatura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.Conta;
import io.lettuce.core.dynamic.annotation.Param;

public interface ContaRepository extends CrudRepository<Conta,Long>{

@Query("SELECT c from Conta c where c.nrConta =:nrConta")
 Optional<Conta>findConta(@Param(value = "nrConta") String nrConta);

/**
 * Retornas todas as contas cadastradas
 * @author elias
 * @since 24-12-2021
 * */
@Query("SELECT c FROM Conta c")
	List<Conta>findcontas();

/**
 * Retorna uma conta pelo id Informado
 * @author elias
 * @since 28/04/2022
 * @return {@link Conta} - Conta
 * */
@Query(value = "SELECT * from  conta WHERE cd_conta=:id", nativeQuery = true)
Conta findContaId (@Param(value = "id") Long id);
}

