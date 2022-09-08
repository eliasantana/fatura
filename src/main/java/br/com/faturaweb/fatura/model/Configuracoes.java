package br.com.faturaweb.fatura.model;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

@Entity
@Table(name="Configuracao")
public class Configuracoes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long cdConfiguracao;
	private String snParcelado;
	private String snNotificar;
	private Integer nrDias;
	private String dirImportacao;
	private Integer nrMsgDiaria;
	private BigDecimal limiteCartao;
	private String nomeArquivo;
	private String snNaCompetencia; //Determina se a primeira parcela do parcelamento deverá ser na competência atual ou não.
	private String nrContaOrigem; //Conta onde será debitado o valor para o lançamento do tipo Débito 
	private String emailOrigem;
	private String emailDestino;
	private String nmOrigem;
	private String nmDestino;
	private String tituloMsgEmailDestino;
	
	
	@Lob
	private byte[] logo;
	
	public Configuracoes() {
		
	}

	public Configuracoes(@NotBlank Long cdConfiguracao, String snParcelado) {
		this.cdConfiguracao = cdConfiguracao;
		this.snParcelado = snParcelado;
	}

	
	public Long getCdConfiguracao() {
		return cdConfiguracao;
	}

	public void setCdConfiguracao(Long cdConfiguracao) {
		this.cdConfiguracao = cdConfiguracao;
	}

	public String getSnParcelado() {
		return snParcelado;
	}

	public void setSnParcelado(String snParcelado) {
		this.snParcelado = snParcelado;
	}
	
	public String getSnNotificar() {
		return snNotificar;
	}
	public void setSnNotificar(String snNotificar) {
		this.snNotificar = snNotificar;
	}
	
	public void setNrDias(Integer nrDias) {
		this.nrDias = nrDias;
	}
	public Integer getNrDias() {
		return nrDias;
	}

	public void setDirImportacao(String dirImportacao) {
		this.dirImportacao = dirImportacao;
	}
	
	public String getDirImportacao() {
		return dirImportacao;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	
	public byte[] getLogo() {
		return logo;
	}
	
	public void setNrMsgDiaria(Integer nrMsgDiaria) {
		this.nrMsgDiaria = nrMsgDiaria;
	}
	
	public Integer getNrMsgDiaria() {
		return nrMsgDiaria;
	}
	
	public BigDecimal getLimiteCartao() {
		return limiteCartao;
	}
	
	public void setLimiteCartao(BigDecimal limiteCartao) {
		this.limiteCartao = limiteCartao;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public void setSnNaCompetencia(String snNaCompetencia) {
		this.snNaCompetencia = snNaCompetencia;
	}
	
	public String getSnNaCompetencia() {
		return snNaCompetencia;
	}
	
	public String getNrContaOrigem() {
		return nrContaOrigem;
	}
	
	public void setNrContaOrigem(String nrContaOrigem) {
		this.nrContaOrigem = nrContaOrigem;
	}

	public String getEmailOrigem() {
		return emailOrigem;
	}

	public void setEmailOrigem(String emailOrigem) {
		this.emailOrigem = emailOrigem;
	}

	public String getEmailDestino() {
		return emailDestino;
	}

	public void setEmailDestino(String emailDestino) {
		this.emailDestino = emailDestino;
	}

	public String getNmOrigem() {
		return nmOrigem;
	}

	public void setNmOrigem(String nmOrigem) {
		this.nmOrigem = nmOrigem;
	}

	public String getNmDestino() {
		return nmDestino;
	}

	public void setNmDestino(String nmDestino) {
		this.nmDestino = nmDestino;
	}

	public String getTituloMsgEmailDestino() {
		return tituloMsgEmailDestino;
	}

	public void setTituloMsgEmailDestino(String tituloMsgEmailDestino) {
		this.tituloMsgEmailDestino = tituloMsgEmailDestino;
	}

	@Override
	public String toString() {
		return "Configuracoes [cdConfiguracao=" + cdConfiguracao + ", snParcelado=" + snParcelado + ", snNotificar="
				+ snNotificar + ", nrDias=" + nrDias + ", dirImportacao=" + dirImportacao + ", nrMsgDiaria="
				+ nrMsgDiaria + ", limiteCartao=" + limiteCartao + ", nomeArquivo=" + nomeArquivo + ", snNaCompetencia="
				+ snNaCompetencia + ", nrContaOrigem=" + nrContaOrigem + ", emailOrigem=" + emailOrigem
				+ ", emailDestino=" + emailDestino + ", nmOrigem=" + nmOrigem + ", nmDestino=" + nmDestino
				+ ", tituloMsgEmailDestino=" + tituloMsgEmailDestino + ", logo=" + Arrays.toString(logo) + "]";
	}
	
	

}
