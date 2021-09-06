package br.com.faturaweb.fatura.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdUsuario;
	@NotBlank
	@NotNull
	private String nome;
	@NotBlank
	@NotNull
	private String login;
	private String senha;
	private String email;
	private String snAtivo;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDate dtCardastro;
	
	 public Usuario() {
		 
	 }
	
	
	public Usuario(Long cdUsuario, String nome, String login, String senha, String email, String snAtivo,
			LocalDate dtCardastro) {
		this.cdUsuario = cdUsuario;
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.email = email;
		this.snAtivo = snAtivo;
		this.dtCardastro = dtCardastro;
	}
	
	public Usuario(String nome, String login, String senha, String email, String snAtivo, LocalDate dtCardastro) {
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.email = email;
		this.snAtivo = snAtivo;
		this.dtCardastro = dtCardastro;
	}
	
	public Long getCdUsuario() {
		return cdUsuario;
	}
	public void setCdUsuario(Long cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSnAtivo() {
		return snAtivo;
	}
	public void setSnAtivo(String snAtivo) {
		this.snAtivo = snAtivo;
	}
	public LocalDate getDtCardastro() {
		return dtCardastro;
	}
	public void setDtCardastro(LocalDate dtCardastro) {
		this.dtCardastro = dtCardastro;
	}


	@Override
	public String toString() {
		return "Usuario [cdUsuario=" + cdUsuario + ", nome=" + nome + ", login=" + login + ", senha=" + senha
				+ ", email=" + email + ", snAtivo=" + snAtivo + ", dtCardastro=" + dtCardastro + "]";
	}
	
	
	
}
