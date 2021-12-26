package br.com.faturaweb.fatura.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Meta")
public class Meta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdMeta;
	private String descricao;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDate dtInicio;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDate dtFim;
	private BigDecimal vlMeta;
	private String snAtivo;
	@OneToOne	
	private Conta conta;
	
	public Meta() {
		
	}

	public Meta(String descricao, LocalDate dtInicio, LocalDate dtFim, BigDecimal vlMeta, String snAtivo, Conta conta) {
		super();
		this.descricao = descricao;
		this.dtInicio = dtInicio;
		this.dtFim = dtFim;
		this.vlMeta = vlMeta;
		this.snAtivo = snAtivo;
		this.conta = conta;
	}

	public Long getCdMeta() {
		return cdMeta;
	}

	public void setCdMeta(Long cdMeta) {
		this.cdMeta = cdMeta;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(LocalDate dtInicio) {
		this.dtInicio = dtInicio;
	}

	public LocalDate getDtFim() {
		return dtFim;
	}

	public void setDtFim(LocalDate dtFim) {
		this.dtFim = dtFim;
	}

	public BigDecimal getVlMeta() {
		return vlMeta;
	}

	public void setVlMeta(BigDecimal vlMeta) {
		this.vlMeta = vlMeta;
	}

	public String getSnAtivo() {
		return snAtivo;
	}

	public void setSnAtivo(String snAtivo) {
		this.snAtivo = snAtivo;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	@Override
	public String toString() {
		return "Meta [cdMeta=" + cdMeta + ", descricao=" + descricao + ", dtInicio=" + dtInicio + ", dtFim=" + dtFim
				+ ", vlMeta=" + vlMeta + ", snAtivo=" + snAtivo + ", conta=" + conta + "]";
	}
	
	
	
	
}
