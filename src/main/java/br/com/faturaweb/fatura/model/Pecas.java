package br.com.faturaweb.fatura.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pecas")
public class Pecas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long cdPecas;
    private String nmPeca;
    private LocalDate dtInclusao;

    private String snIncluir;

    public Pecas(){
        this.dtInclusao = LocalDate.now();
    }

    public Long getCdPecas() {
        return cdPecas;
    }

    public void setCdPecas(Long cdPecas) {
        this.cdPecas = cdPecas;
    }
    @Column(precision = 9, scale = 3)
    private BigDecimal kmTroca;


    public String getNmPeca() {
        return nmPeca;
    }

    public void setNmPeca(String nmPeca) {
        this.nmPeca = nmPeca;
    }

    public LocalDate getDtInclusao() {
        return dtInclusao;
    }

    public void setDtInclusao(LocalDate dtInclusao) {
        this.dtInclusao = dtInclusao;
    }

    public void setSnIncluir(String snIncluir) {
        this.snIncluir = snIncluir;
    }

    public String getSnIncluir() {
        return snIncluir;
    }

    public void setKmTroca(BigDecimal kmTroca) {
        this.kmTroca = kmTroca;
    }

    public BigDecimal getKmTroca() {
        return kmTroca;
    }

    @Override
    public String toString() {
        return "Pecas{" +
                "cdPecas=" + cdPecas +
                ", nmPeca='" + nmPeca + '\'' +
                ", dtInclusao=" + dtInclusao +
                ", snIncluir='" + snIncluir + '\'' +
                '}';
    }
}
