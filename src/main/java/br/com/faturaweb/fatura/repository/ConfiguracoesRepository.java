package br.com.faturaweb.fatura.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;

import br.com.faturaweb.fatura.model.ChaveConfig;
import br.com.faturaweb.fatura.model.Configuracoes;
@Repository
public interface ConfiguracoesRepository  extends CrudRepository<Configuracoes, Long>{


@Query(value  = "SELECT *  FROM configuracao ",nativeQuery = true)
Configuracoes findConfiguracao();


}