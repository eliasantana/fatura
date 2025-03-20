package br.com.faturaweb.fatura.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;

import br.com.faturaweb.fatura.dto.HistoricoPagamentoDto;
import br.com.faturaweb.fatura.model.HistoricoPagamentoEnvio;
import br.com.faturaweb.fatura.repository.HistoricoPagamentoEnvioRepository;

@Service
public class HistoricoPagamentoEnvioServices {

	@Autowired
	HistoricoPagamentoEnvioRepository repository;
	
	public void salvaHistorico(HistoricoPagamentoDto dto, URI uri) {
			HistoricoPagamentoEnvio envio = new HistoricoPagamentoEnvio();
			envio.setStatusCode(201);
			envio.setBody(dto.toString());
			envio.setCdCliente(dto.getCdCliente());
			envio.setDtEnvio(dto.getDtPagamento());
			envio.setUri(uri.toString());
			envio.setUsuRecebimento(dto.getUsurecebimento());
			repository.save(envio);			
	}

}
