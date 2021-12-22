package br.com.faturaweb.fatura.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

@Entity
@Table(name="Configuracao")
public class Configuracoes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long cdConfiguracao;
	private String snParcelado;
	private String snNotificar;
	private Integer nrDias;
	
	public Configuracoes() {
		
	}

	public Configuracoes(@NotBlank Long cdConfiguracao, String snParcelado) {
		this.cdConfiguracao = cdConfiguracao;
		this.snParcelado = snParcelado;
	}

	
	public Long getCdConfiguracao() {
		return cdConfiguracao;
	}

	public void setCdConfiguracao(Long cdConfiguracao) {
		this.cdConfiguracao = cdConfiguracao;
	}

	public String getSnParcelado() {
		return snParcelado;
	}

	public void setSnParcelado(String snParcelado) {
		this.snParcelado = snParcelado;
	}
	
	public String getSnNotificar() {
		return snNotificar;
	}
	public void setSnNotificar(String snNotificar) {
		this.snNotificar = snNotificar;
	}
	
	public void setNrDias(Integer nrDias) {
		this.nrDias = nrDias;
	}
	public Integer getNrDias() {
		return nrDias;
	}

	@Override
	public String toString() {
		return "Configuracoes [cdConfiguracao=" + cdConfiguracao + ", snParcelado=" + snParcelado + ", snNotificar="
				+ snNotificar + ", nrDias=" + nrDias + "]";
	}

}
