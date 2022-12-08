package br.com.faturaweb.fatura.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.itextpdf.html2pdf.HtmlConverter;

import br.com.faturaweb.fatura.model.Configuracoes;
import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.Lancamento;
import br.com.faturaweb.fatura.model.Lote;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.LancamentoRepository;
import br.com.faturaweb.fatura.repository.LoteRepository;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import net.bytebuddy.asm.Advice.Return;

@Service
public class AppServices {
	@Autowired
	public EmailService emailService;
	@Autowired
	ContaRepository contaRepository;
	@Autowired
	LancamentoRepository lancamentoRepository;
	@Autowired
	ConfiguracoesRepository configGeraisRepository;
	@Autowired
	ReportService services;

	/**
	 * Envia e-mail de texto simples
	 * 
	 * @author elias
	 * @since 20-12-2021
	 * @param origem          - E-mail de Origem
	 * @param origemNome      - Nome do Remetente
	 * @param destino         - E-mail de Destino
	 * @param destinoNome     - Nome do Destinatário
	 * @param tiulo           = Titulo do E-mail
	 * @param corpoDamensagem - Mensagem do E-mail
	 */
	public void sendEmai(String origem, String origemNome, String destino, String destinoNome, String tiulo,
			StringBuilder corpoDamensagem) throws UnsupportedEncodingException {

		final Email email = DefaultEmail.builder().from(new InternetAddress(origem, origemNome))
				.to(Lists.newArrayList(new InternetAddress(destino, destinoNome))).subject(tiulo)
				.body(corpoDamensagem.toString()).encoding("UTF-8").build();

		emailService.send(email);
	}

	/**
	 * Credita o valor na conta informada
	 * 
	 * @author elias
	 * @since 24/12/2021
	 * @param c     - Conta
	 * @param valor - Valor a ser Creditado
	 * @return {@link Conta} - Conta atualiza
	 */
	public Conta credita(Conta c, BigDecimal valor) {
		BigDecimal saldo = c.getSaldo();
		if (!(valor.compareTo(BigDecimal.ZERO) < 0)) { // Valor maior que zero
			System.out.println("Creditando valor");
			c.setSaldo(saldo.add(valor));
		}
		return c;
	}

	/**
	 * Debita o valor na conta informada
	 * 
	 * @author elias
	 * @since 24/12/2021
	 * @param c     - Conta
	 * @param valor - Valor a ser Creditado
	 * @return {@link Conta} - Conta atualizada
	 */
	public Conta debita(Conta c, BigDecimal valor) {
		BigDecimal saldo = c.getSaldo();
		if (valor.compareTo(BigDecimal.ZERO) < 0) { // Valor Negativo
			System.out.println("Valor Inálido!  O Valor negativo não é válido!");
		} else if (valor.compareTo(saldo) == 1) {
			System.out.println("Saldo Insuficienete ->" + saldo);
		} else {
			c.setSaldo(saldo.subtract(valor));
		}
		return c;
	}

	/**
	 * Gera relatório PDF a partir de um arquivo HTML
	 * 
	 * @author elias
	 * @param fileIn      - Caminho de Origem do arquivo HTML
	 * @param nomeArquivo - Nome do arquivo a ser gerado
	 * @param fileOut     - Caminho onde o PDF será gerado.
	 */
	public void geraPDF(String fileIn, String nomeArquivo, String fileOut) throws IOException {
		try {
			HtmlConverter.convertToPdf(new FileInputStream(fileIn.toString()),
					new FileOutputStream(new File(fileOut.toString() + nomeArquivo)));
			System.out.println("Arquivo Criado com sucessos!");
		} catch (FileNotFoundException e) {
			System.out.println("Erro ao tentar gerar o arquivo");
			e.printStackTrace();
		}
	}

	/**
	 * Verifica o status do lote
	 * 
	 * @since 21/08/2022
	 * @author elias
	 * @param lote   - Lote Contábil
	 * @param status - Status a ser verificado A - Aberto - Fechado
	 * @return {@link Boolean}
	 */
	public Boolean verificalote(String status, Lote lote) {
		Boolean resp = false;
		if (lote != null) {
			if (lote.getStatus().equals(status)) {
				resp = true;
			}
		} 
		return resp;
	}

	/**
	 * Verifica se existe lancamentos não pagos
	 */
	public boolean isLancamentoAberto(List<Lancamento> lancamentos) {
		Boolean existe = false;
		int snAberto = 0;
		for (Lancamento lancamento : lancamentos) {
			if (lancamento.getSnPago().toUpperCase().equals("NÃO")) {
				snAberto = snAberto + 1;
			}
		}
		if (snAberto > 0)
			existe = true;

		return existe;
	}

	/**
	 * Debida valor na conta de Origem
	 * 
	 * @since 01/10/2022
	 * @author elias
	 * @param valor - Valor a ser Debitado na conta informada
	 */
	public void debitaNaOrigem(BigDecimal valor) {
		Configuracoes configuracoesGerais = configGeraisRepository.findConfiguracao();
		Optional<Conta> contaOrigem = contaRepository.findConta(configuracoesGerais.getNrContaOrigem());
		if (contaOrigem.isPresent()) {
			BigDecimal saldo = contaOrigem.get().getSaldo();
			if (valor.compareTo(BigDecimal.ZERO) < 0) { // Valor Negativo
				System.out.println("Valor Inálido!  O Valor negativo não é válido!");
			} else if (valor.compareTo(saldo) == 1) {
				System.out.println("Saldo Insuficienete ->" + saldo);
			} else {
				contaOrigem.get().setSaldo(saldo.subtract(valor));
			}

		}
	}

	/**
	 * Renomear arquivo
	 * 
	 * @param nomeAtual - Nome atual do Arquivo
	 * @param novoNome  - Novo Nome
	 * @param caminho   - Caminho do arquivo
	 */
	public void renomeiaArquivo(String nomeAtual, String novoNome) {
		String caminho = "C:\\Users\\elias\\Downloads\\";
		File arquivo = new File(caminho.concat(nomeAtual));
		File novoArquivo = new File(caminho.concat(novoNome));
		if (arquivo.exists()) {
			System.out.println("Arquivo " + nomeAtual + " foi localizado! " + " em " + caminho.concat(nomeAtual));
			System.out.println("O arquivo agora se chama " + novoNome);
			arquivo.renameTo(novoArquivo);
		}
	}

	/**
	 * Retorna a coluna a ser ordenada a partir da opção informada
	 * 
	 * @author elias
	 * @since 18-10-2022
	 * @param ordenacao - Tipo de Ordenação
	 */
	public String getOrdenacao(String ordenacao) {
		String orderByDefault = " l.sn_pago";
		String orderBy = "";
		if (ordenacao!=null) {
			
		
				switch (ordenacao) {
				case "FP":
					orderBy = " fp.descricao ";
					break;
				case "TP":
					orderBy = " tl.ds_tipo_lancamento ";
					break;
				case "DE":
					orderBy = " l.ds_lancamento ";
					break;
				case "CO":
					orderBy = "  l.dt_competencia ";
					break;
				case "VL":
					orderBy = " l.vl_pago";
					break;
				default:
					orderBy = orderByDefault;
					break;
				} 
		}else{
			orderBy = orderByDefault;
		}
		
		return orderBy;
	}

	/**
	 * Imprime o relatório informado
	 * 
	 * @author elias
	 * @since 19-10-2022
	 * @param nmRelatorio - Nome do relatório
	 * @param strMes      - Mês
	 * @param strAno      - Ano
	 * @param orderBy     - Tipo de ordenação a ser utilizada pelo relatório de
	 *                    lançamentos
	 * @param response    - Resposta da requisição
	 * @param acao        - Tipo de ação D - Download / V - Visulizar em Tela
	 * @param formapagto  - Forma de Pagamento
	 * @param tppagto     - Tipo de Pagamento
	 * @param periodoini  - Data Inicial
	 * @param periodofim  - Data Final
	 * @param competencia - Mês de competência / MMYYY
	 */
	public void imprmirRelatorio(String nmRelatorio, String strMes, String strAno, String orderBy,
		HttpServletResponse response, String acao, String formapagto, String tppagto, String periodoini,
		String periodofim, String competencia, String nrLote) {
		
		String cfWhere = "";
		String cfFiltro = "Filtro: ";
		
		switch (nmRelatorio) {
		
		case "lotecontabil":
			nmRelatorio="relFechamentoContabil";
			services.removeParam();
			services.addParam("COMPETENCIA", competencia);
			services.addParam("PCD_LOTE",nrLote);			
			services.imprime(nmRelatorio, acao, response);
		break;	
			
		
		case "relatoriodacompetencia":
			nmRelatorio = "lancamentos";

			services.addParam("COMPETENCIA", strMes.concat("/").concat(strAno));
			services.addParam("CF_COMPETENCIA", strMes.concat(strAno));
			services.addParam("CF_WHERE", "AND l.CD_LANCAMENTO = l.CD_LANCAMENTO");
			services.addParam("CF_ORDER_BY", orderBy);
			services.imprime(nmRelatorio, acao, response);
			break;

		case "relatoriocomfiltro":
			nmRelatorio = "lancamentos";
			services.addParam("COMPETENCIA", competencia.concat("/").concat(strAno));

			if (competencia.equals("0") || competencia == null) {
				cfWhere = cfWhere
						.concat(" AND DATE_FORMAT(l.dt_competencia,'%m%Y') = DATE_FORMAT(l.dt_competencia,'%m%Y') ");
				cfFiltro = cfFiltro.concat(" Competência: Todas ");
				services.addParam("CF_COMPETENCIA", "  DATE_FORMAT(l.dt_competencia,'%m%Y')  ");
			} else {
				cfFiltro = cfFiltro.concat(" Competência:  " + competencia.concat(strAno));
				services.addParam("CF_COMPETENCIA", competencia.concat(strAno));
			}
			if (formapagto.equals("0")) {
				cfFiltro = cfFiltro.concat(" Forma de Pagamento:  Todas");
				cfWhere = cfWhere
						.concat(" AND l.forma_de_pagamento_cd_forma_pgamento = l.forma_de_pagamento_cd_forma_pgamento");
			} else {
				cfWhere = cfWhere.concat(" AND l.forma_de_pagamento_cd_forma_pgamento =" + formapagto);
				cfFiltro = cfFiltro.concat(" Forma de Pagamento:  " + formapagto);
			}
			if (tppagto.equals("0")) {
				cfWhere = cfWhere
						.concat(" AND l.tipo_lancamento_cd_tipo_lancamento = l.tipo_lancamento_cd_tipo_lancamento ");
				cfFiltro = cfFiltro.concat(" Tipo de Pagamento : Todos ");
			} else {
				cfWhere = cfWhere.concat(" AND l.tipo_lancamento_cd_tipo_lancamento =  " + tppagto);
				cfFiltro = cfFiltro.concat("  Tipo de Pagamento:  " + tppagto);
			}
			if (periodoini.isEmpty() && periodofim.isEmpty()) {
				cfWhere = cfWhere.concat("  and l.dt_competencia  = l.dt_competencia ");
				cfFiltro = cfFiltro.concat("  Período:  Todos ");
			} else {
				cfWhere = cfWhere
						.concat(" AND  l.dt_competencia between  '" + periodoini + "' AND  '" + periodofim + "'");
				cfFiltro = cfFiltro.concat("  Período:  Data Inicial: " + periodoini + " Período Final: " + periodofim);
			}
			services.addParam("CF_WHERE", cfWhere);
			services.addParam("CF_FILTRO", cfFiltro);
			services.addParam("CF_ORDER_BY", orderBy);
			services.imprime(nmRelatorio, acao, response);

			break;

		default:
			break;
		}

	}

}
