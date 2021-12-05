package br.com.faturaweb.fatura.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.faturaweb.fatura.form.LancamentoForm;
import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.FormaDePagamento;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.TipoLancamento;
import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.FormaDePagamentoRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.ReceitaRepository;
import br.com.faturaweb.fatura.repository.TipoLancamentoRepository;
import br.com.faturaweb.fatura.repository.UsuarioRepository;
import br.com.faturaweb.fatura.services.ReceitaServices;
@ComponentScan
@Controller

public class HomeController {
	@Autowired
	LancamentoRepository lancamentoRepository;

	@Autowired
	FormaDePagamentoRepository formaDePagamentoRepository;

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	TipoLancamentoRepository tipoLancamentoRepository;

	@Autowired
	ReceitaRepository receitaRepository;
	
	@Autowired
	ReceitaServices services;
	@Autowired
	ConfiguracoesRepository configuracoesRepository;
	
	@GetMapping("/")
	public String index(Model model) {
		 List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
		 
		 //Formata data retorando apenas o mes ex: jan = 01
		 DateTimeFormatter df = DateTimeFormatter.ofPattern("MM");
		 //Variáveis para acumular os valores pagos 
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
		
		//Interando sobre  os lancamentos encontrados
		for (Lancamento lancamento : lancamentos) {
			   LocalDate localDateFormat = lancamento.getDtCompetencia();
			   String mes = lancamento.getDtCompetencia().format(df);
			   //Verificando o mes e acumulando seu valor
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
		//Imprimindo o resultado - apagar após finalização
		/*
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
			*/
			
			//Criando hash map e tribuindo o mes e seu valor
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
			
		    Map<String, BigDecimal> receitas = services.totalizaReceita(receitaRepository.findAllList());
			
			//Adicionando a view os valores acumuladods
			model.addAttribute("keyset",dados.keySet()); // Meses
			model.addAttribute("values",dados.values()); // Valores
			model.addAttribute("titulo","Faturamento Mensal - SysFatura");//Titulo do Gráfico
			model.addAttribute("grafico","column"); //Tipo do gráfico column - Gráfico de Colunas - bar - Gráfico de Barras
			model.addAttribute("keysetreceitas",receitas.keySet());
			model.addAttribute("valuesreceitas",receitas.values());
		return "home/dashboard";
	}
	
	@GetMapping(value = "/listar")
	public String listar(Model model) {
		List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
		System.out.println("listando");
		model.addAttribute("lancamentos", lancamentos);

		return "home/listar-lancamento";
	}

	@PostMapping("/salvar")
	public String salvar(LancamentoForm lancamentoForm, Model model) {
		Optional<FormaDePagamento> findByDescricaoFormaDePagamento = formaDePagamentoRepository
				.findByDescricaoFormaDePagamento(lancamentoForm.getDsFormaDePagamento());
		FormaDePagamento formadepagamento = findByDescricaoFormaDePagamento.get();
		Lancamento lancamento = new Lancamento();
		Optional<TipoLancamento> findBydsTipoLancamento = tipoLancamentoRepository
				.findBydsTipoLancamento(lancamentoForm.getDsTipoLancamento());
		TipoLancamento tipoLancamento = findBydsTipoLancamento.get();
		Optional<Usuario> usuario = usuarioRepository.findById(5L);		

			lancamento.setCdLancamento(lancamentoForm.getCdLancamento());
			lancamento.setDsLancamento(lancamentoForm.getDsLancamento());
			lancamento.setDtCadastro(lancamentoForm.getDtCadastro());
			lancamento.setDtCompetencia(lancamentoForm.getDtCompetencia());
			lancamento.setFormaDePagamento(formadepagamento);
			lancamento.setSnPago(lancamentoForm.getSnPago());
			lancamento.setTipoLancamento(tipoLancamento);
			lancamento.setUsuario(usuario.get());
			lancamento.setVlPago(lancamentoForm.getVlPago());
			lancamentoRepository.save(lancamento);

			List<Lancamento> lancamentos = lancamentoRepository.findAllLancamentos();
			model.addAttribute("lancamentos", lancamentos);

		
		return "home/listar-lancamento";
	}
@GetMapping("/configuracoes")
	public String configuracoes(Model model) {
		Configuracoes config = configuracoesRepository.findConfiguracao();
		model.addAttribute("config",config);
		return "home/configuracoes";
	}
}
