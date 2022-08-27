package br.com.faturaweb.fatura.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "LogProvisao")
public class LogProvisao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long cdLogProvisao;
		private Long cdProvisao;
		private BigDecimal vlProvisionado;
		private String snCreditado;
		private String nrConta;
		private String competencia;
		@DateTimeFormat(iso =DateTimeFormat.ISO.DATE_TIME)
		private LocalDateTime dtInclusao;
		
		public LogProvisao() {
		}

		public Long getCdLogProvisao() {
			return cdLogProvisao;
		}

		public void setCdLogProvisao(Long cdLogProvisao) {
			this.cdLogProvisao = cdLogProvisao;
		}

		public Long getCdProvisao() {
			return cdProvisao;
		}

		public void setCdProvisao(Long cdProvisao) {
			this.cdProvisao = cdProvisao;
		}

		public BigDecimal getVlProvisionado() {
			return vlProvisionado;
		}

		public void setVlProvisionado(BigDecimal vlProvisionado) {
			this.vlProvisionado = vlProvisionado;
		}

		public String getSnCreditado() {
			return snCreditado;
		}

		public void setSnCreditado(String snCreditado) {
			this.snCreditado = snCreditado;
		}

		public String getNrConta() {
			return nrConta;
		}

		public void setNrConta(String nrConta) {
			this.nrConta = nrConta;
		}

		public String getCompetencia() {
			return competencia;
		}

		public void setCompetencia(String competencia) {
			this.competencia = competencia;
		}

		public LocalDateTime getDtInclusao() {
			return dtInclusao;
		}

		public void setDtInclusao(LocalDateTime dtInclusao) {
			this.dtInclusao = dtInclusao;
		}

		@Override
		public String toString() {
			return "LogProvisao [cdLogProvisao=" + cdLogProvisao + ", cdProvisao=" + cdProvisao + ", vlProvisionado="
					+ vlProvisionado + ", snCreditado=" + snCreditado + ", nrConta=" + nrConta + ", competencia="
					+ competencia + ", dtInclusao=" + dtInclusao + "]";
		}

		
}
