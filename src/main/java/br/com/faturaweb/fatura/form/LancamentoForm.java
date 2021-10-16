package br.com.faturaweb.fatura.form;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class LancamentoForm {
	private Long cdLancamento;
	private String dsLancamento;
	private String  usuario;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDate dtCadastro;
	private String dsTipoLancamento;
	private String snPago;
	private BigDecimal vlPago;
	private String dsFormaDePagamento;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDate dtCompetencia;
	
	
	public LancamentoForm() {
		 this.dtCadastro = LocalDate.now();
		 this.dtCompetencia=LocalDate.now();
	}
	

	public LancamentoForm(Long cdLancamento, String dsLancamento, String usuario, LocalDate dtCadastro,
			String dsTipoLancamento, String snPago, BigDecimal vlPago, String dsFormaDePagamento,
			LocalDate dtCompetencia) {
			super();
			this.cdLancamento = cdLancamento;
			this.dsLancamento = dsLancamento;
			this.usuario = usuario;
			this.dtCadastro = LocalDate.now();
			this.dsTipoLancamento = dsTipoLancamento;
			this.snPago = snPago;
			this.vlPago = vlPago;
			this.dsFormaDePagamento = dsFormaDePagamento;
			this.dtCompetencia = LocalDate.now();
	}



	public Long getCdLancamento() {
		return cdLancamento;
	}

	public void setCdLancamento(Long cdLancamento) {
		this.cdLancamento = cdLancamento;
	}

	public String getDsLancamento() {
		return dsLancamento;
	}

	public void setDsLancamento(String dsLancamento) {
		this.dsLancamento = dsLancamento;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public LocalDate getDtCadastro() {
		return dtCadastro;
	}

	public void setDtCadastro(LocalDate dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	public String getDsTipoLancamento() {
		return dsTipoLancamento;
	}

	public void setDsTipoLancamento(String dsTipoLancamento) {
		this.dsTipoLancamento = dsTipoLancamento;
	}

	public String getSnPago() {
		return snPago;
	}

	public void setSnPago(String snPago) {
		this.snPago = snPago;
	}

	public BigDecimal getVlPago() {
		return vlPago;
	}

	public void setVlPago(BigDecimal vlPago) {
		this.vlPago = vlPago;
	}

	public String getDsFormaDePagamento() {
		return dsFormaDePagamento;
	}

	public void setDsFormaDePagamento(String dsFormaDePagamento) {
		this.dsFormaDePagamento = dsFormaDePagamento;
	}

	public LocalDate getDtCompetencia() {
		return dtCompetencia;
	}

	public void setDtCompetencia(LocalDate dtCompetencia) {
		this.dtCompetencia = dtCompetencia;
	}

	@Override
	public String toString() {
		return "LancamentoForm [cdLancamento=" + cdLancamento + ", dsLancamento=" + dsLancamento + ", usuario="
				+ usuario + ", dtCadastro=" + dtCadastro + ", dsTipoLancamento=" + dsTipoLancamento + ", snPago="
				+ snPago + ", vlPago=" + vlPago + ", dsFormaDePagamento=" + dsFormaDePagamento + ", dtCompetencia="
				+ dtCompetencia + "]";
	}
	
	
	
}
