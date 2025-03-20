package br.com.faturaweb.fatura.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HistoricoEnvio")
public class HistoricoPagamentoEnvio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdHistoricoPagEnvio;
	private Long cdCliente;
	private String usuRecebimento;
	private int StatusCode;
	private String body;
	private String uri;
	private LocalDate dtEnvio;
	
	public HistoricoPagamentoEnvio() {
	
	}

	public Long getCdHistoricoPagEnvio() {
		return cdHistoricoPagEnvio;
	}

	public void setCdHistoricoPagEnvio(Long cdHistoricoPagEnvio) {
		this.cdHistoricoPagEnvio = cdHistoricoPagEnvio;
	}

	public Long getCdCliente() {
		return cdCliente;
	}

	public void setCdCliente(Long cdCliente) {
		this.cdCliente = cdCliente;
	}

	public String getUsuRecebimento() {
		return usuRecebimento;
	}

	public void setUsuRecebimento(String usuRecebimento) {
		this.usuRecebimento = usuRecebimento;
	}

	public int getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(int statusCode) {
		StatusCode = statusCode;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public LocalDate getDtEnvio() {
		return dtEnvio;
	}

	public void setDtEnvio(LocalDate dtEnvio) {
		this.dtEnvio = dtEnvio;
	}
	
	
}
