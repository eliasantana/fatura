package br.com.faturaweb.fatura.model;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "Teste")
public class Teste {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdTteste;
	@Lob
	private byte[]  anexo;
	
	public Teste() {
	
	}

	public Teste(Long cdTteste, byte[] anexo) {
		super();
		this.cdTteste = cdTteste;
		this.anexo = anexo;
	}

	public Long getCdTteste() {
		return cdTteste;
	}

	public void setCdTteste(Long cdTteste) {
		this.cdTteste = cdTteste;
	}

	public byte[] getAnexo() {
		return anexo;
	}

	public void setAnexo(byte[] anexo) {
		this.anexo = anexo;
	}

	@Override
	public String toString() {
		return "Teste [cdTteste=" + cdTteste + ", anexo=" + Arrays.toString(anexo) + ", getCdTteste()=" + getCdTteste()
				+ ", getAnexo()=" + Arrays.toString(getAnexo()) + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
}
