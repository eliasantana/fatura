package br.com.faturaweb.fatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.faturaweb.fatura.model.Menssageria;

public interface MensageriaRepository extends CrudRepository<Menssageria, Long> {

	@Query(value = "SELECT * FROM fatura.mensageria where date_format(DT_ENVIO, '%d/%m/%Y') = date_format(curdate(), '%d/%m/%Y') ", nativeQuery = true)
	List<Menssageria> findMensagensDia();
	
}
