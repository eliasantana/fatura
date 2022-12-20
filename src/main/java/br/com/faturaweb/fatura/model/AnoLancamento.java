package br.com.faturaweb.fatura.model;

import java.util.ArrayList;
import java.util.List;

public class AnoLancamento {
		private Integer ano;
		
		List<Integer> anos = new ArrayList<Integer>();
		public AnoLancamento() {
		
		}
		
	

		public Integer getAno() {
			return ano;
		}

		public void setAno(Integer ano) {
			this.ano = ano;
		}
		
		public void addAno(Integer ano) {		
			for (int i =0; i < anos.size() ;i++) {
				if (anos.get(i)!=ano) {
					anos.add(ano);
				}
			}
			anos.add(ano);
		}
		
		public List<Integer> getListAno() {
			return anos; 
		}
}
