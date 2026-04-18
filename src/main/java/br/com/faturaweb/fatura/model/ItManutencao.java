package br.com.faturaweb.fatura.model;


import javax.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "it_manutencao")
public class ItManutencao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdItManitencao;
    @ManyToOne
    @JoinColumn(name = "cd_peca")
    private Pecas peca;

    @ManyToOne
    @JoinColumn(name = "cd_manutencao")
    private Manutencao manutencao;
    private LocalDate dtInclusao;

    private String sn_adicionar;

    public ItManutencao(){
        this.dtInclusao = LocalDate.now();
    }

    public Long getCdItManitencao() {
        return cdItManitencao;
    }

    public void setCdItManitencao(Long cdItManitencao) {
        this.cdItManitencao = cdItManitencao;
    }

    public Pecas getPeca() {
        return peca;
    }

    public void setPeca(Pecas peca) {
        this.peca = peca;
    }

    public Manutencao getManutencao() {
        return manutencao;
    }

    public void setManutencao(Manutencao manutencao) {
        this.manutencao = manutencao;
    }

    public LocalDate getDtInclusao() {
        return dtInclusao;
    }

    public void setDtInclusao(LocalDate dtInclusao) {
        this.dtInclusao = dtInclusao;
    }

    public void setSn_adicionar(String sn_adicionar) {
        this.sn_adicionar = sn_adicionar;
    }

    public String getSn_adicionar() {
        return sn_adicionar;
    }

    @Override
    public String toString() {
        return "ItManutencao{" +
                "cdItManitencao=" + cdItManitencao +
                ", peca=" + peca +
                ", manutencao=" + manutencao +
                ", dtInclusao=" + dtInclusao +
                ", sn_adicionar='" + sn_adicionar + '\'' +
                '}';
    }
}
