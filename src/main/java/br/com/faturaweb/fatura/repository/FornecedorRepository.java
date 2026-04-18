package br.com.faturaweb.fatura.repository;

import br.com.faturaweb.fatura.model.Fornecedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface FornecedorRepository extends CrudRepository<Fornecedor, Long> {
    @Query(value = "select * from fornecedor",nativeQuery = true)
    ArrayList<Fornecedor> pesquisarFornecedores();

    @Query(value = "select * from fornecedor where cd_fornecedor=:idfornecedor", nativeQuery = true)
    Fornecedor localizarFornecedor(Long idfornecedor);
}
