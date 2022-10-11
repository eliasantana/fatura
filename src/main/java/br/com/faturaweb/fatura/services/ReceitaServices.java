package br.com.faturaweb.fatura.services;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.faturaweb.fatura.model.Receita;

@Service
public class ReceitaServices {
	/**
	 * Totaliza as receitas e retorna uma Map com chave e valor 
	 * @author elias
	 * @since 24-11-2021
	 * @param receitas
	 * @return Map<String, BigDecimal> dadosReceita
	 * */	
	public Map<String, BigDecimal> totalizaReceita(List<Receita> receitas) {
		
		System.out.println("Quantidade de Despesas: " +receitas.size());
		for (Receita receita : receitas) {
			 System.out.println(receita.getDsReceita() + " - " + receita.getSalLiquido() + " Data: " + receita.getDtRecebimento()  );
		}
		
		 DateTimeFormatter df = DateTimeFormatter.ofPattern("MM");
		 Map<String, BigDecimal>dadosReceita = new LinkedHashMap<String, BigDecimal>();
		 	//Meses do ano 
		    BigDecimal jan = new BigDecimal(0);
			BigDecimal fev = new BigDecimal(0);
			BigDecimal mar = new BigDecimal(0);
			BigDecimal abr = new BigDecimal(0);
			BigDecimal mai = new BigDecimal(0);
			BigDecimal jun = new BigDecimal(0);
			BigDecimal julho = new BigDecimal(0);
			BigDecimal ago = new BigDecimal(0);
			BigDecimal set = new BigDecimal(0);
			BigDecimal out = new BigDecimal(0);
			BigDecimal nov = new BigDecimal(0);
			BigDecimal dez = new BigDecimal(0);
			
			if (receitas.size()>0) {
				for (Receita receita : receitas) {
					String mes = receita.getDtRecebimento().format(df);
					System.out.println(receita.getSalLiquido());
					switch (mes) {
					case "01":
							jan = jan.add(receita.getSalLiquido());
							break;
					case "02":
						fev = fev.add(receita.getSalLiquido());
						break;
					case "03":
						mar = mar.add(receita.getSalLiquido());
						break;
					case "04":
						abr = abr.add(receita.getSalLiquido());
						break;
					case "05":
						mai = mai.add(receita.getSalLiquido());
						break;
					case "06":
						jun = jun.add(receita.getSalLiquido());
						break;
					case "07":
						julho = julho.add(receita.getSalLiquido());
						break;
					case "08":
						ago = ago.add(receita.getSalLiquido());
						break;
					case "09":
						set= set.add(receita.getSalLiquido());
						break;
					case "10":
						out = out.add(receita.getSalLiquido());
						break;
					case "11":
						nov = nov.add(receita.getSalLiquido());
						System.out.println("Nov"+nov);
						break;
					case "12":
						dez = dez.add(receita.getSalLiquido());
						break;		
					default:
						break;
					}		   
				
					dadosReceita.put("janeiro", jan);
					dadosReceita.put("fevereiro", fev);
					dadosReceita.put("março", mar);
					dadosReceita.put("abril", abr);
					dadosReceita.put("maio", mai);
					dadosReceita.put("junho", jun);
					dadosReceita.put("julho", julho);					
					dadosReceita.put("agosto", ago);
					dadosReceita.put("setembro", set);
					dadosReceita.put("outubro", out);					
					dadosReceita.put("novembro", nov);
					dadosReceita.put("dezembro", dez);
					
				}
				System.out.println("Total Dezembro"+dez);
			}
			return dadosReceita;
			
	}
}
