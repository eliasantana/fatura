package br.com.faturaweb.fatura.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chave_config")
public class ChaveConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ChaveConfig;
	
	
	private String descricao;
	private String valor;
	private LocalDate dtCriacao;
	@Enumerated(EnumType.STRING)
	private Chave chave;
		
	public ChaveConfig() {
		dtCriacao = LocalDate.now();	
	}

	public Long getChaveConfig() {
		return ChaveConfig;
	}

	public void setChaveConfig(Long chaveConfig) {
		ChaveConfig = chaveConfig;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public LocalDate getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(LocalDate dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public Chave getChave() {
		return chave;
	}

	public void setChave(Chave chave) {
		this.chave = chave;
	}


}
