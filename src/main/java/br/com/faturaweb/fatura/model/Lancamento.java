package br.com.faturaweb.fatura.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Lancamento")
public class Lancamento implements Comparable<Lancamento> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdLancamento;
	private String dsLancamento;
	@ManyToOne
	private Usuario usuario;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDate dtCadastro;
	@ManyToOne
	private TipoLancamento tipoLancamento;
	private String snPago;
	private BigDecimal vlPago;
	@ManyToOne
	private FormaDePagamento formaDePagamento;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDate dtCompetencia;
	@ManyToOne
	private Cartao cartao;
	private Integer nrParcela;
	private String dsAnexo;
	private String observacao;
	
	
	public Lancamento() {
		this.nrParcela = 1;	   
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalDate getDtCadastro() {
		return dtCadastro;
	}

	public void setDtCadastro(LocalDate dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
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

	public FormaDePagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}

	public LocalDate getDtCompetencia() {
		return dtCompetencia;
	}

	public void setDtCompetencia(LocalDate dtCompetencia) {
		this.dtCompetencia = dtCompetencia;
	}
	
	
	public Integer getNrParcela() {
		return nrParcela;
	}
	
	public void setNrParcela(Integer nrParcela) {
		this.nrParcela = nrParcela;
	}

public String getDsAnexo() {
	return dsAnexo;
}
public void setDsAnexo(String dsAnexo) {
	this.dsAnexo = dsAnexo;
}

public String getObservacao() {
	return observacao;
}

public void setObservacao(String observacao) {
	this.observacao = observacao;
}

public void setCartao(Cartao cartao) {
	this.cartao = cartao;
}

public Cartao getCartao() {
	return cartao;
}

@Override
public String toString() {
	return "Lancamento [cdLancamento=" + cdLancamento + ", dsLancamento=" + dsLancamento + ", usuario=" + usuario
			+ ", dtCadastro=" + dtCadastro + ", tipoLancamento=" + tipoLancamento + ", snPago=" + snPago + ", vlPago="
			+ vlPago + ", formaDePagamento=" + formaDePagamento + ", dtCompetencia=" + dtCompetencia + ", nrParcela="
			+ nrParcela + ", dsAnexo=" + dsAnexo + ", observacao=" + observacao + "]";
}


@Override
public int compareTo(Lancamento l) {	
	return  formaDePagamento.getDescricao().compareTo((l.formaDePagamento.getDescricao()));
}



}
