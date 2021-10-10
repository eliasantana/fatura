package br.com.faturaweb.fatura.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;

@Service
public class ReportService {

	@Autowired
	TipoLancamentoRepository lancamentoRepository;

	
}
