package br.com.faturaweb.fatura.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "it_meta")
public class ItMeta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdItMeta;
	private String descricao;
	private int nrSemana;
	private BigDecimal vlrSemana;
	@ManyToOne
   private Meta meta;
   private String snCreditado; 
	
	public ItMeta() {
		
	}

	public ItMeta(String descricao, int nrSemana, BigDecimal vlrSemana, Meta meta) {
		super();
		this.descricao = descricao;
		this.nrSemana = nrSemana;
		this.vlrSemana = vlrSemana;
		this.meta = meta;
		this.snCreditado="N";
	}

	public Long getCdItMeta() {
		return cdItMeta;
	}

	public void setCdItMeta(Long cdItMeta) {
		this.cdItMeta = cdItMeta;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getNrSemana() {
		return nrSemana;
	}

	public void setNrSemana(int nrSemana) {
		this.nrSemana = nrSemana;
	}

	public BigDecimal getVlrSemana() {
		return vlrSemana;
	}

	public void setVlrSemana(BigDecimal vlrSemana) {
		this.vlrSemana = vlrSemana;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	@Override
	public String toString() {
		return "ItMeta [cdItMeta=" + cdItMeta + ", descricao=" + descricao + ", nrSemana=" + nrSemana + ", vlrSemana="
				+ vlrSemana + ", meta=" + meta + "]";
	}
	
	
	
	
}
