package br.com.faturaweb.fatura.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Tipo_Lancamento")
public class TipoLancamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdTipoLancamento;
	private LocalDate dtCadastro;
	private String dsTipoLancamento;
	
	public TipoLancamento() {
	
	}

	public TipoLancamento(String dsTipoLancamento) {
		this.dsTipoLancamento = dsTipoLancamento;
		this.dtCadastro = LocalDate.now();
	}

	public Long getCdTipoLancamento() {
		return cdTipoLancamento;
	}

	public void setCdTipoLancamento(Long cdTipoLancamento) {
		this.cdTipoLancamento = cdTipoLancamento;
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

	@Override
	public String toString() {
		return "TipoLancamento [dtCadastro=" + dtCadastro + ", dsTipoLancamento=" + dsTipoLancamento + "]";
	}
	
	
	
	

}
