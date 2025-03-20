package br.com.faturaweb.fatura.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HistoricoPagamentoDto {

	private Long cdHistoricoDto;
	private Long cdFormaPagamento;
	private LocalDate dtPagamento;
	private Long cdCliente;
	private String usurecebimento;
	private BigDecimal valor;
	private String token;
	
	public HistoricoPagamentoDto() {
		
	}

	public HistoricoPagamentoDto(Long cdHistoricoDto, Long cdFormaPagamento,
			LocalDate dtPagamento, Long cdCliente, String usurecebimento, BigDecimal valor, String token) {
		super();
		this.cdHistoricoDto = cdHistoricoDto;
		this.cdFormaPagamento = cdFormaPagamento;
		this.dtPagamento = dtPagamento;
		this.cdCliente = cdCliente;
		this.usurecebimento = usurecebimento;
		this.valor = valor;
		this.token = token;
	}

	public Long getCdHistoricoDto() {
		return cdHistoricoDto;
	}

	public void setCdHistoricoDto(Long cdHistoricoDto) {
		this.cdHistoricoDto = cdHistoricoDto;
	}

	public Long getCdFormaPagamento() {
		return cdFormaPagamento;
	}

	public void setCdFormaPagamento(Long cdFormaPagamento) {
		this.cdFormaPagamento = cdFormaPagamento;
	}

	public LocalDate getDtPagamento() {
		return dtPagamento;
	}

	public void setDtPagamento(LocalDate dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	public Long getCdCliente() {
		return cdCliente;
	}

	public void setCdCliente(Long cdCliente) {
		this.cdCliente = cdCliente;
	}

	public String getUsurecebimento() {
		return usurecebimento;
	}

	public void setUsurecebimento(String usurecebimento) {
		this.usurecebimento = usurecebimento;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		return "HistoricoPagamentoDto [cdHistoricoDto=" + cdHistoricoDto
				+ ", cdFormaPagamento=" + cdFormaPagamento + ", dtPagamento="
				+ dtPagamento + ", cdCliente=" + cdCliente + ", usurecebimento="
				+ usurecebimento + ", valor=" + valor + ", token=" + token
				+ "]";
	}


		
	
	
}
