package br.com.faturaweb.fatura.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Cartao")
public class Cartao {
			@Id 
			@GeneratedValue(strategy = GenerationType.IDENTITY)
			private Long cdCartao;
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
			private LocalDate dtVencimento;
			private BigDecimal limite;
			private String dsCartao;
			@OneToMany(mappedBy= "cartao")
			private java.util.List<Lancamento> lancamento = new ArrayList<Lancamento>();			
			@Lob
			private byte[] logoCartao;
			private String nmArquivoLogo;
			
			public Cartao() {
				
			}

			public Long getCdCartao() {
				return cdCartao;
			}

			public void setCdCartao(Long cdCartao) {
				this.cdCartao = cdCartao;
			}

			public LocalDate getDtVencimento() {
				return dtVencimento;
			}

			public void setDtVencimento(LocalDate dtVencimento) {
				this.dtVencimento = dtVencimento;
			}

			public BigDecimal getLimite() {
				return limite;
			}

			public void setLimite(BigDecimal limite) {
				this.limite = limite;
			}

			public String getDsCartao() {
				return dsCartao;
			}

			public void setDsCartao(String dsCartao) {
				this.dsCartao = dsCartao;
			}

			public java.util.List<Lancamento> getLancamento() {
				return lancamento;
			}

			public void setLancamento(java.util.List<Lancamento> lancamento) {
				this.lancamento = lancamento;
			}
			
			public void setLogoCartao(byte[] logoCartao) {
				this.logoCartao = logoCartao;
			}
			
			public byte[] getLogoCartao() {
				return logoCartao;
			}
			public void setNmArquivoLogo(String nmArquivoLogo) {
				this.nmArquivoLogo = nmArquivoLogo;
			}
			public String getNmArquivoLogo() {
				return nmArquivoLogo;
			}
			
}
