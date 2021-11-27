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
@Table(name="Receita")
public class Receita {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long cdReceita;
		private String dsReceita;
		private BigDecimal salBruto;
		private BigDecimal desconto;
		private BigDecimal salLiquido;
		@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
		private LocalDate dtRecebimento;
		
		public Receita() {


		}

		public Long getCdReceita() {
			return cdReceita;
		}

		public void setCdReceita(Long cdReceita) {
			this.cdReceita = cdReceita;
		}

		public String getDsReceita() {
			return dsReceita;
		}

		public void setDsReceita(String dsReceita) {
			this.dsReceita = dsReceita;
		}

		public BigDecimal getSalBruto() {
			return salBruto;
		}

		public void setSalBruto(BigDecimal salBruto) {
			this.salBruto = salBruto;
		}

		public BigDecimal getDesconto() {
			return desconto;
		}

		public void setDesconto(BigDecimal desconto) {
			this.desconto = desconto;
		}

		public BigDecimal getSalLiquido() {
			return salLiquido;
		}

		public void setSalLiquido(BigDecimal salLiquido) {
			this.salLiquido = salLiquido;
		}

		public LocalDate getDtRecebimento() {
			return dtRecebimento;
		}

		public void setDtRecebimento(LocalDate dtRecebimento) {
			this.dtRecebimento = dtRecebimento;
		}

		public Receita(Long cdReceita, String dsReceita, BigDecimal salBruto, BigDecimal desconto,
				BigDecimal salLiquido, LocalDate dtRecebimento) {
			super();
			this.cdReceita = cdReceita;
			this.dsReceita = dsReceita;
			this.salBruto = salBruto;
			this.desconto = desconto;
			this.salLiquido = salLiquido;
			this.dtRecebimento = dtRecebimento;
		}

		@Override
		public String toString() {
			return "Receita [cdReceita=" + cdReceita + ", dsReceita=" + dsReceita + ", salBruto=" + salBruto
					+ ", desconto=" + desconto + ", salLiquido=" + salLiquido + ", dtRecebimento=" + dtRecebimento
					+ "]";
		}
		
		
}
