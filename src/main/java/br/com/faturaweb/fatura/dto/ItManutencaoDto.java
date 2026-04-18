package br.com.faturaweb.fatura.dto;

import br.com.faturaweb.fatura.model.Pecas;

import java.util.List;

public class ItManutencaoDto {
    private List<Pecas> pecas;
    public ItManutencaoDto (List<Pecas> p){
        this.pecas = p;
    }

    public void setPecas(List<Pecas> pecas) {
        this.pecas = pecas;
    }

    public List<Pecas> getPecas() {
        return pecas;
    }

    @Override
    public String toString() {
        return "ItManutencaoDto{" +
                "pecas=" + pecas +
                '}';
    }
}
