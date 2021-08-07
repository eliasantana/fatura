package br.com.faturaweb.fatura.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;

@Service
public class TipoLancamentoServices {
	@Autowired
	TipoLancamentoRepository lancamentoRepository;
	
	public Iterable<TipoLancamento> todos() {
		Iterable<TipoLancamento> tiposLancamentos = lancamentoRepository.findAll();
		return tiposLancamentos;
	}
	
}
