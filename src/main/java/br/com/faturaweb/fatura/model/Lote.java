package br.com.faturaweb.fatura.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "Lote")
public class Lote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdLote;
	private String dsLote;
	@DateTimeFormat(iso =DateTimeFormat.ISO.DATE_TIME)
	private LocalDate competencia;
	private Integer qtdLancamentos;
	private BigDecimal vlTotalLote;
	private BigDecimal vlTotalReceita;
	private BigDecimal vlSaldo;
	private String status;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lote")
	private List<ItLote> itLote;
	
	public Lote() {

	}

	public Long getCdLote() {
		return cdLote;
	}

	public void setCdLote(Long cdLote) {
		this.cdLote = cdLote;
	}

	public String getDsLote() {
		return dsLote;
	}

	public void setDsLote(String dsLote) {
		this.dsLote = dsLote;
	}

	public LocalDate getCompetencia() {
		return competencia;
	}

	public void setCompetencia(LocalDate competencia) {
		this.competencia = competencia;
	}

	public Integer getQtdLancamentos() {
		return qtdLancamentos;
	}

	public void setQtdLancamentos(Integer qtdLancamentos) {
		this.qtdLancamentos = qtdLancamentos;
	}

	public BigDecimal getVlTotalLote() {
		return vlTotalLote;
	}

	public void setVlTotalLote(BigDecimal vlTotalLote) {
		this.vlTotalLote = vlTotalLote;
	}

	public BigDecimal getVlTotalReceita() {
		return vlTotalReceita;
	}

	public void setVlTotalReceita(BigDecimal vlTotalReceita) {
		this.vlTotalReceita = vlTotalReceita;
	}

	public BigDecimal getVlSaldo() {
		return vlSaldo;
	}

	public void setVlSaldo(BigDecimal vlSaldo) {
		this.vlSaldo = vlSaldo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setItLote(List<ItLote> itLote) {
		this.itLote = itLote;
	}
	
	public List<ItLote> getItLote() {
		return itLote;
	}

	@Override
	public String toString() {
		return "Lote [cdLote=" + cdLote + ", dsLote=" + dsLote + ", competencia=" + competencia + ", qtdLancamentos="
				+ qtdLancamentos + ", vlTotalLote=" + vlTotalLote + ", vlTotalReceita=" + vlTotalReceita + ", vlSaldo="
				+ vlSaldo + ", status=" + status + ", itLote=" + itLote + "]";
	}
	
	
}
