package br.com.faturaweb.fatura.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "forma_pagto")
public class FormaDePagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdFormaPgamento;
	private LocalDate dtInclusao;
	private String descricao;
	@OneToMany(mappedBy = "formaDePagamento")
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();
	
	public FormaDePagamento() {
		
	}	
	public FormaDePagamento(String descricao) {
		this.descricao = descricao;
		this.dtInclusao = LocalDate.now();
	}
	
	
	public FormaDePagamento(Long cdFormaPgamento, String descricao) {
		this.cdFormaPgamento = cdFormaPgamento;
		this.dtInclusao = LocalDate.now();
		this.descricao = descricao;
	}


	public Long getCdFormaPgamento() {
		return cdFormaPgamento;
	}
	public void setCdFormaPgamento(Long cdFormaPgamento) {
		this.cdFormaPgamento = cdFormaPgamento;
	}
	public LocalDate getDtInclusao() {
		return dtInclusao;
	}
	public void setDtInclusao(LocalDate dtInclusao) {
		this.dtInclusao = dtInclusao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	@Override
	public String toString() {
		return "FormaDePagamento [cdFormaPgamento=" + cdFormaPgamento + ", dtInclusao=" + dtInclusao + ", descricao="
				+ descricao + "]";
	}
	
	
	
}
