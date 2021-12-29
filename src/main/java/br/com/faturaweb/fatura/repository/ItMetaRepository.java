package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.faturaweb.fatura.model.ItMeta;
import io.lettuce.core.dynamic.annotation.Param;

@Repository

public interface ItMetaRepository extends CrudRepository<ItMeta, Long> {
	/**
	 * Retonas os itens da meta que já foram creditados
	 * */
	  @Query(value = "select * from fatura.it_meta where meta_cd_meta = :cdMeta and sn_creditado = 'S'",nativeQuery = true)
		List<ItMeta> findItMeta(@Param(value = "cdMeta") Long cdMeta);
	  /**
	   * Retorna os itens da meta que não foram creditatos
	   * @author elias
	   * */
	  @Query(value = "select * from fatura.it_meta where meta_cd_meta = :cdMeta and sn_creditado = 'N'",nativeQuery = true)
		List<ItMeta> findItNaoCreditado(@Param(value = "cdMeta") Long cdMeta);
	  /**
	   * Retorna o item de meta informado
	   * @author elias
	   * */
	  @Query(value = "SELECT * FROM fatura.it_meta where cd_it_meta = :id",nativeQuery = true)
	  ItMeta findItMetaId(@Param(value = "id") Long id);
}
