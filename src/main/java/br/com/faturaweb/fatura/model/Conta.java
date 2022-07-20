package br.com.faturaweb.fatura.model;

import java.math.BigDecimal;
import java.util.Arrays;

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
	private String dsConta;
	
	@Lob
	private byte[] qrcod;
	
	public Conta() {
		
	}
	
	public Conta (String nrConta, String nrAgencia, BigDecimal saldo, String dsConta) {
			this.nrConta = nrConta;
			this.nrAgencia = nrAgencia;
			this.saldo = saldo;
			this.dsConta = dsConta;
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
public void setDsConta(String dsConta) {
	this.dsConta = dsConta;
}

public String getDsConta() {
	return dsConta;
}

@Override
public String toString() {
	return "Conta [cdConta=" + cdConta + ", nrConta=" + nrConta + ", nrAgencia=" + nrAgencia + ", saldo=" + saldo
			+ ", dsConta=" + dsConta + ", qrcod=" + Arrays.toString(qrcod) + "]";
}
	
	
	
}
