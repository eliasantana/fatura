package br.com.faturaweb.fatura.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "manutencao")
public class Manutencao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdManutencao;
    @ManyToOne
    @JoinColumn(name="cd_veiculo")
    private Veiculo veiculo;
    @ManyToOne
    @JoinColumn(name="cd_fornecedor")
    private Fornecedor fornecedor;
    private Double km;
    private String snRealizado;
    private LocalDate dtRealizacao;
    private LocalDate dtCadastro;

    public Manutencao(){
        this.dtCadastro = LocalDate.now();
    }

    public Long getCdManutencao() {
        return cdManutencao;
    }

    public void setCdManutencao(Long cdManutencao) {
        this.cdManutencao = cdManutencao;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public String getSnRealizado() {
        return snRealizado;
    }

    public void setSnRealizado(String snRealizado) {
        this.snRealizado = snRealizado;
    }

    public LocalDate getDtRealizacao() {
        return dtRealizacao;
    }

    public void setDtRealizacao(LocalDate dtRealizacao) {
        this.dtRealizacao = dtRealizacao;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    @Override
    public String toString() {
        return "Manutencao{" +
                "cdManutencao=" + cdManutencao +
                ", veiculo=" + veiculo +
                ", fornecedor=" + fornecedor +
                ", km=" + km +
                ", snRealizado='" + snRealizado + '\'' +
                ", dtRealizacao=" + dtRealizacao +
                ", dtCadastro=" + dtCadastro +
                '}';
    }
}
