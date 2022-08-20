package br.com.faturaweb.fatura.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Provisao")
public class Provisao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdProvisao;
	private String dsProvisao;
	private BigDecimal percentual;
	private String nrConta;
	
	public Provisao() {

	}
	public Long getCdProvisao() {
		return cdProvisao;
	}
	public void setCdProvisao(Long cdProvisao) {
		this.cdProvisao = cdProvisao;
	}
	public String getDsProvisao() {
		return dsProvisao;
	}
	public void setDsProvisao(String dsProvisao) {
		this.dsProvisao = dsProvisao;
	}
	public BigDecimal getPercentual() {
		return percentual;
	}
	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public String getNrConta() {
		return nrConta;
	}
	
	public void setNrConta(String nrConta) {
		this.nrConta = nrConta;
	}
	
}
