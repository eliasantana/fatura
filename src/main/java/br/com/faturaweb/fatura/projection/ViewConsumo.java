package br.com.faturaweb.fatura.projection;

import java.math.BigDecimal;
public interface ViewConsumo {
 int getMes();
 int getAano();
 BigDecimal getLitros();
 BigDecimal getPrecoMedio();
 BigDecimal getTotalGasto();
 Long getcdVeiculo();
}
