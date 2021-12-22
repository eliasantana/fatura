package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.repository.LancamentoRepository;

@Controller
public class testeController {

		@Autowired
		LancamentoRepository r;
		
		@GetMapping("/teste")
		public String teste(Model model) {
			
			//System.out.println(dados.values());
			//System.out.println(dados.keySet());
			List<Lancamento> todosOsLancamento = r.findAllLancamentos();
			
			DateTimeFormatter df = DateTimeFormatter.ofPattern("MM");
			BigDecimal janeiro = new BigDecimal(0);
			BigDecimal fevereiro = new BigDecimal(0);
			BigDecimal marco = new BigDecimal(0);
			BigDecimal abril = new BigDecimal(0);
			BigDecimal maio = new BigDecimal(0);
			BigDecimal junho = new BigDecimal(0);
			BigDecimal julho = new BigDecimal(0);
			BigDecimal agosto = new BigDecimal(0);
			BigDecimal setembro = new BigDecimal(0);
			BigDecimal outubro = new BigDecimal(0);
			BigDecimal novembro = new BigDecimal(0);
			BigDecimal dezembro = new BigDecimal(0);
			
			for (Lancamento lancamento : todosOsLancamento) {
			   LocalDate localDateFormat = lancamento.getDtCompetencia();
			   String mes = lancamento.getDtCompetencia().format(df);
			   
			switch (mes) {
			case "01":
				janeiro = lancamento.getVlPago().plus();
				break;
			case "02":
				fevereiro=fevereiro.add( lancamento.getVlPago());
				break;
			case "03":
				marco=marco.add(lancamento.getVlPago());
				break;
			case "04":
				abril=abril.add(lancamento.getVlPago());
				break;
			case "05":
				maio=maio.add(lancamento.getVlPago());
				break;
			case "06":
				junho=junho.add(lancamento.getVlPago());
				break;
			case "07":
				julho =julho.add(lancamento.getVlPago());
				break;
			case "08":
				agosto=agosto.add(lancamento.getVlPago());
				break;
			case "09":
				setembro = setembro.add(lancamento.getVlPago());
				break;
			case "10":
				outubro = outubro.add(lancamento.getVlPago());
				break;
			case "11":
				novembro = novembro.add(lancamento.getVlPago());
				break;
			case "12":
				dezembro = dezembro.add(lancamento.getVlPago());
				break;		
			default:
				break;
			}
			}
			System.out.println("Janeiro: " + janeiro);
			System.out.println("fevereiro: " + fevereiro);
			System.out.println("março: " + marco);
			System.out.println("abril: " + abril);
			System.out.println("maio: " + maio);
			System.out.println("junho: " + junho);
			System.out.println("julho: " + julho);
			System.out.println("Agosto: " + agosto);
			System.out.println("setembro: " + setembro);
			System.out.println("outubro: " + outubro);
			System.out.println("novembro: " + novembro);
			System.out.println("dezembro: " + dezembro);
			
			Map<String,BigDecimal> dados = new LinkedHashMap<String, BigDecimal>();
			dados.put("Janeiro", janeiro);
			dados.put("Fevereiro", fevereiro);
			dados.put("Março", marco);
			dados.put("Abril", abril);
			dados.put("Maio", maio);
			dados.put("Junho", junho);
			dados.put("Julho", julho);
			dados.put("Agosto", agosto);
			dados.put("Setembro", setembro);
			dados.put("Outubro", outubro);
			dados.put("Novembro", novembro);
			dados.put("Dezembro", dezembro);
			
			
			model.addAttribute("keyset",dados.keySet());
			model.addAttribute("values",dados.values());
			model.addAttribute("titulo","Faturamento Mensal - SysFatura");
			model.addAttribute("grafico","column"); //column - Gráfico de Colunas - bar - Gráfico de Barras
			
			System.out.println("Parcelando valores");
			BigDecimal total = new BigDecimal(800.75);
			BigDecimal qtdParcelas = new BigDecimal(12);
			BigDecimal parcela = new BigDecimal(0);
			 parcela = total.divide(qtdParcelas,MathContext.DECIMAL32);
			// parcela = total.divide(qtdParcelas, new MathContext(2, RoundingMode.CEILING));
			 BigDecimal totalCalculado  = qtdParcelas.multiply(parcela);
			 System.out.println(parcela);
			
			
			return "teste";
		}
		
		@GetMapping("/ds2")
		public String teste2() {
			
			return "home/dashboard2";
		}
		
		@GetMapping("/divide")
		public RedirectView divide() {
		
			RedirectView rw = new RedirectView("http://localhost/teste");
			return  rw;
		}
}
