package br.com.faturaweb.fatura.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "LogMovimetacaoFinanceira")
public class LogMovimentacaoFinanceira {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long  cdLogMovimentacao;
	private String descricao;
	private String usuario;
	private String nrConta;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDate dtMovimentacao;
	private String tpMovimentacao;
	private BigDecimal vlMovimentado;
	
	public LogMovimentacaoFinanceira() {
		
	}

	public LogMovimentacaoFinanceira( String descricao, String usuario, LocalDate dtMovimentacao,
			String tpMovimentacao, BigDecimal vlMovimentado, String nrConta) {
		super();
		this.descricao = descricao;
		this.usuario = usuario;
		this.dtMovimentacao = LocalDate.now();
		this.tpMovimentacao = tpMovimentacao;
		this.vlMovimentado = vlMovimentado;
		this.nrConta = nrConta;
	}

	public Long getCdLogMovimentacao() {
		return cdLogMovimentacao;
	}

	public void setCdLogMovimentacao(Long cdLogMovimentacao) {
		this.cdLogMovimentacao = cdLogMovimentacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public LocalDate getDtMovimentacao() {
		return dtMovimentacao;
	}

	public void setDtMovimentacao(LocalDate dtMovimentacao) {
		this.dtMovimentacao = dtMovimentacao;
	}

	public String getTpMovimentacao() {
		return tpMovimentacao;
	}

	public void setTpMovimentacao(String tpMovimentacao) {
		this.tpMovimentacao = tpMovimentacao;
	}

	public BigDecimal getVlMovimentado() {
		return vlMovimentado;
	}

	public void setVlMovimentado(BigDecimal vlMovimentado) {
		this.vlMovimentado = vlMovimentado;
	}

	public String getNrConta() {
		return nrConta;
	}
	public void setNrConta(String nrConta) {
		this.nrConta = nrConta;
	}
	@Override
	public String toString() {
		return "LogMovimentacaoFinanceira [cdLogMovimentacao=" + cdLogMovimentacao + ", descricao=" + descricao
				+ ", usuario=" + usuario + ", dtMovimentacao=" + dtMovimentacao + ", tpMovimentacao=" + tpMovimentacao
				+ ", vlMovimentado=" + vlMovimentado + "]";
	}
	
	
	
	
}
