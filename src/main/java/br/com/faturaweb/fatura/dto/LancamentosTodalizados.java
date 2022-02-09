package br.com.faturaweb.fatura.dto;

import java.math.BigDecimal;

public class LancamentosTodalizados {
	private String ds_tipo_lancamento;
	private Long tipo_lancamento_cd_tipo_lancamento;
	 private BigDecimal  vl_pago;
	 
	 public LancamentosTodalizados() {
		
	}

	public String getDs_tipo_lancamento() {
		return ds_tipo_lancamento;
	}

	public void setDs_tipo_lancamento(String ds_tipo_lancamento) {
		this.ds_tipo_lancamento = ds_tipo_lancamento;
	}

	public Long getTipo_lancamento_cd_tipo_lancamento() {
		return tipo_lancamento_cd_tipo_lancamento;
	}

	public void setTipo_lancamento_cd_tipo_lancamento(Long tipo_lancamento_cd_tipo_lancamento) {
		this.tipo_lancamento_cd_tipo_lancamento = tipo_lancamento_cd_tipo_lancamento;
	}

	public BigDecimal getVl_pago() {
		return vl_pago;
	}

	public void setVl_pago(BigDecimal vl_pago) {
		this.vl_pago = vl_pago;
	}

	@Override
	public String toString() {
		return "LancamentosTodalizados [ds_tipo_lancamento=" + ds_tipo_lancamento
				+ ", tipo_lancamento_cd_tipo_lancamento=" + tipo_lancamento_cd_tipo_lancamento + ", vl_pago=" + vl_pago
				+ "]";
	}
	 
	 
	
}
