package br.com.faturaweb.fatura.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "Conta")
public class Conta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdConta;
	private String nrConta;
	private String nrAgencia;
	private BigDecimal saldo;
	@Lob
	private byte[] qrcod;
	
	public Conta() {
		
	}
	
	public Conta (String nrConta, String nrAgencia, BigDecimal saldo) {
			this.nrConta = nrConta;
			this.nrAgencia = nrAgencia;
			this.saldo = saldo;
	}

	public Long getCdConta() {
		return cdConta;
	}

	public void setCdConta(Long cdConta) {
		this.cdConta = cdConta;
	}

	public String getNrConta() {
		return nrConta;
	}

	public void setNrConta(String nrConta) {
		this.nrConta = nrConta;
	}

	public String getNrAgencia() {
		return nrAgencia;
	}

	public void setNrAgencia(String nrAgencia) {
		this.nrAgencia = nrAgencia;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public void setQrcod(byte[] qrcod) {
		this.qrcod = qrcod;
	}
	
	public byte[] getQrcod() {
		return qrcod;
	}

	@Override
	public String toString() {
		return "Conta [cdConta=" + cdConta + ", nrConta=" + nrConta + ", nrAgencia=" + nrAgencia + ", saldo=" + saldo
				+ "]";
	}
	
	
	
}
