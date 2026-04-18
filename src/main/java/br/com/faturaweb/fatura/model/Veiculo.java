package br.com.faturaweb.fatura.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Veiculo")
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdVeiculo;
    private String fabricante;
    private Double modelo;
    private String cor;
    private int ano;
    private double kmAtual;
    private String placa;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate dtCadastro;

   public Veiculo(){
        this.dtCadastro = LocalDate.now();
   }

    public Long getCdVeiculo() {
        return cdVeiculo;
    }

    public void setCdVeiculo(Long cdVeiculo) {
        this.cdVeiculo = cdVeiculo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public Double getModelo() {
        return modelo;
    }

    public void setModelo(Double modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }
    public void setKmAtual(double kmAtual) {
        this.kmAtual = kmAtual;
    }

    public double getKmAtual() {
        return kmAtual;
    }

    @Override
    public String toString() {
        return "Veiculo{" +
                "cdVeiculo=" + cdVeiculo +
                ", fabricante='" + fabricante + '\'' +
                ", modelo=" + modelo +
                ", cor='" + cor + '\'' +
                ", ano=" + ano +
                ", kmAtual=" + kmAtual +
                ", placa='" + placa + '\'' +
                ", dtCadastro=" + dtCadastro +
                '}';
    }
}
