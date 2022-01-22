package br.com.faturaweb.fatura.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "Mensageria")
public class Menssageria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime dtEnvio;
	private String status;
	private String destino;
	
	public Menssageria() {
		 this.dtEnvio = LocalDateTime.now();
	}

	public Menssageria(Long id, LocalDateTime dtEnvio, String status, String destino) {
		super();
		this.id = id;
		this.dtEnvio = dtEnvio;
		this.status = status;
		this.destino = destino;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDtEnvio() {
		return dtEnvio;
	}

	public void setDtEnvio(LocalDateTime dtEnvio) {
		this.dtEnvio = dtEnvio;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	@Override
	public String toString() {
		return "Menssageria [id=" + id + ", dtEnvio=" + dtEnvio + ", status=" + status + ", destino=" + destino + "]";
	}

	
	
}
