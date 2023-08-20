package br.com.faturaweb.fatura.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Tipo_Lancamento")
public class TipoLancamento implements Comparable<TipoLancamento> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdTipoLancamento;
	private LocalDate dtCadastro;
	@NotNull	
	@NotBlank
	private String dsTipoLancamento;
	@OneToMany(mappedBy = "tipoLancamento")	
	private List<Lancamento> lancamento = new ArrayList<Lancamento>();
	
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

	@Override
	public int compareTo(TipoLancamento o) {
	   
		return getDsTipoLancamento().compareTo(o.getDsTipoLancamento());
	}
	
	
	
	

}
