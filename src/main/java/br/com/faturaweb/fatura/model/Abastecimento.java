package br.com.faturaweb.fatura.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name="abastecimento")
public class Abastecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdAbastecimento;
    @ManyToOne
    @JoinColumn(name = "cd_veiculo")
    private Veiculo veiculo;
    private BigDecimal kmAtual;
    private BigDecimal litros;
    private LocalDate dtAbastecimento;

    private BigDecimal vlLitro;
    public Long getCdAbastecimento() {
        return cdAbastecimento;
    }

    public void setCdAbastecimento(Long cdAbastecimento) {
        this.cdAbastecimento = cdAbastecimento;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public BigDecimal getKmAtual() {
        return kmAtual;
    }

    public void setKmAtual(BigDecimal kmAtual) {
        this.kmAtual = kmAtual;
    }

    public BigDecimal getLitros() {
        return litros;
    }

    public void setLitros(BigDecimal litros) {
        this.litros = litros;
    }

    public LocalDate getDtAbastecimento() {
        return dtAbastecimento;
    }

    public void setDtAbastecimento(LocalDate dtAbastecimento) {
        this.dtAbastecimento = dtAbastecimento;
    }

    public void setVlLitro(BigDecimal vlLitro) {
        this.vlLitro = vlLitro;
    }

    public BigDecimal getVlLitro() {
        return vlLitro;
    }

    @Override
    public String toString() {
        return "Abastecimento{" +
                "cdAbastecimento=" + cdAbastecimento +
                ", veiculo=" + veiculo +
                ", kmAtual=" + kmAtual +
                ", litros=" + litros +
                ", dtAbastecimento=" + dtAbastecimento +
                '}';
    }
}
