package br.com.faturaweb.fatura.services;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import br.com.faturaweb.fatura.model.Conta;
import br.com.faturaweb.fatura.model.LogMovimentacaoFinanceira;
import br.com.faturaweb.fatura.repository.ContaRepository;
import br.com.faturaweb.fatura.repository.LogMovimentacaoFinanceiraRepository;

@Service
public class ContaServices {

	@Autowired
	AppServices appservices;
	@Autowired
	ContaRepository repository;
	@Autowired
	ContaServices contaServices;

	@Autowired
	LogMovimentacaoFinanceiraRepository logMovimentacao;

	/**
	 * Valida a transação entre as contas de Origem e Destino
	 * 
	 * @author elias
	 * @param ctaorigem  - Conta de Origem
	 * @param ctadestino - Conta de Destino
	 * @return {@link Boolean}
	 */
	public String validaTransacao(String ctaorigem, String ctadestino, String vlr) {
		Optional<Conta> contaDeOrigem = repository.findConta(ctaorigem);
		Optional<Conta> contaDeDestino = repository.findConta(ctadestino);
		BigDecimal bValor = new BigDecimal(vlr);
		String msg = null;
		// Verificando se as contas existem
		if (contaDeOrigem.isPresent() && contaDeDestino.isPresent()) {
			// Verificando se as contas são iguais
			if (ctaorigem.equals(ctadestino)) {
				msg = "A conta de origem e destino não podem ser iguais";
			} else if ((bValor.compareTo(BigDecimal.ZERO) == 0) || (bValor.compareTo(BigDecimal.ZERO) == -1)) {
				msg = " Valor " + bValor + " Não é válido!";
			} else if (bValor.compareTo(BigDecimal.ZERO) == 0) {
				msg = " Valor " + bValor + " Não é válido!";
			} else if (contaDeOrigem.get().getSaldo().compareTo(bValor) > -1) {
				msg = "sucesso";
			} else {
				msg = "Saldo  Insuficiente!";
			}
		} else {
			msg = "Conta inválida!";
		}
		return msg;
	}

	/**
	 * Realiza a tranzação entre contas
	 * 
	 * @author elias
	 * @param ctaOrigem  - Conta de Origem
	 * @param ctaDestino - Conta de Destino
	 * @param vlr        - Valor a ser transferido
	 */
	public void transfere(ArrayList<Conta> contas, String ctaOrigem, String ctaDestino, String vlr) {
		Optional<Conta> contaDeOrigem = repository.findConta(ctaOrigem);
		Optional<Conta> contaDeDestino = repository.findConta(ctaDestino);
		BigDecimal bValor = new BigDecimal(vlr);
		// Obtendo o saldo da conta de origem e debitando o valor
		BigDecimal saldoOrigem = contaDeOrigem.get().getSaldo();
		System.out.println(" Saldo da Conta de origem " + saldoOrigem);

		saldoOrigem = saldoOrigem.subtract(bValor);
		contaDeOrigem.get().setSaldo(saldoOrigem);

		System.out.println("Novo saldo " + contaDeOrigem.get().getSaldo());

		// Obtendo o saldo da conta de Destino e creditando o valor tranferido
		BigDecimal saldoDestino = contaDeDestino.get().getSaldo();
		System.out.println("Saldo da conta de Destino  " + saldoDestino);

		saldoDestino = saldoDestino.add(bValor);
		contaDeDestino.get().setSaldo(saldoDestino);
		System.out.println("Novo Saldo da conta de Destino  " + contaDeDestino.get().getSaldo());
		// Adicionando as contas e salvando o novo saldo
		contas.add(contaDeOrigem.get());
		contas.add(contaDeDestino.get());
		repository.saveAll(contas);

		insereLog(" Transferência de  R$ " + vlr + "  |  " + contaDeOrigem.get().getNrConta() + " - "
				+ contaDeOrigem.get().getDsConta() + " para " + contaDeDestino.get().getNrConta() + " - "
				+ contaDeDestino.get().getDsConta(), ctaOrigem, ctaDestino, "T", bValor);
	}

	/**
	 * Gera um loga da movimentação Financeira
	 * 
	 * @author elias
	 * @param descricao      - Descrição da movimentação
	 * @param nrContaOrigem  - Conta de origem do recurso
	 * @param nrContaDestino - Conta de destino
	 * @param tpMovimentacao - Tipo de movimentação T - Tranferência D - Débito C -
	 *                       Crédito
	 * @param vlMovimentado  - Valor transferido
	 */
	public void insereLog(String descricao, String nrContaOrigem, String nrContaDestino, String tpMovimentacao,
			BigDecimal vlMovimentado) {

		LogMovimentacaoFinanceira log = new LogMovimentacaoFinanceira();
		log.setDescricao(descricao);
		log.setDtMovimentacao(LocalDate.now());
		log.setNrConta(nrContaOrigem);
		log.setTpMovimentacao(tpMovimentacao);
		log.setUsuario("Elias");
		log.setVlMovimentado(vlMovimentado);

		logMovimentacao.save(log);

	}

	/**
	 * Totaliza o saldo de todas as contas cadastradas
	 * 
	 * @author elias
	 * @since 16/09/2022
	 * @param contas - Lista com todas as contas cadastradas
	 */
	public BigDecimal getSaldoGeral(List<Conta> contas) {
		BigDecimal total = BigDecimal.ZERO;

		if (contas.size() > 0) {
			for (Conta conta : contas) {
				total = total.add(conta.getSaldo());
			}
		}

		return total;
	}

	/**
	 * Lista todas as contas cdastradas
	 * 
	 * @author Elias Santana da Silva
	 * @since 25-02-2022
	 * @param model
	 */
	public void listar(Model model) {
		List<Conta> contas = repository.findcontas();
		BigDecimal saldoGeral = contaServices.getSaldoGeral(contas);
		Conta conta = new Conta();
		model.addAttribute("conta", conta);
		model.addAttribute("contas", contas);
		model.addAttribute("erro", null);
		model.addAttribute("saldogeral", saldoGeral);
	}

	/**
	 * Salva um aconta
	 * 
	 * @author Elias Santana da Silva
	 * @since 25-02-2022
	 * @param model, {@link Conta}, {@link File}
	 */
	public RedirectView salvar(Model model, Conta conta, MultipartFile file) {
		RedirectView rw = new RedirectView("/conta/listar");

		try {
			if (file.getBytes().length > 0) {
				conta.setQrcod(file.getBytes());
			} else {
				Conta contaLocalizada = repository.findContaId(conta.getCdConta());
				conta.setQrcod(contaLocalizada.getQrcod());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		repository.save(conta);
		return rw;
	}

	/**
	 * Altera a conta selecionada carregando os dados da conta no formulário
	 * 
	 * @author Elias Santana da Silva
	 * @since 25-02-2022
	 * @param model
	 */
	public void alterar(Long id, Model model) {

		try {
			Optional<Conta> conta = repository.findById(id);
			List<Conta> contas = repository.findcontas();
			model.addAttribute("conta", conta.get());
			model.addAttribute("contas", contas);
			model.addAttribute("erro", null);
		} catch (Exception e) {
			System.out.println("Conta não localizada!");
		}

	}

	/**
	 * Exclui a conta iformada
	 * 
	 * @author Elias Santana da Silva
	 * @since 25-02-2022
	 * @param model
	 */
	public RedirectView excluir(Long id, Model model) {
		RedirectView rw = new RedirectView("/conta/listar");
		Conta conta = repository.findContaId(id);
		repository.delete(conta);
		return rw;
	}

	/**
	 * Registra a movimentação financeira de CRÉDITO e DÉBITO
	 * 
	 * @author Elias
	 * @since 25-02-2023
	 * @param model
	 * @param vavalor
	 * @param conta
	 * @param operacao
	 * @param motivo
	 */
	public RedirectView movimentacao(Model model, String valor, String conta, String operacao, String motivo) {
		RedirectView rw = new RedirectView("/conta/listar");
		Optional<Conta> contaLocalizada = repository.findConta(conta);
		LogMovimentacaoFinanceira lmf = new LogMovimentacaoFinanceira();
		valor = valor.replaceAll(",", ".");
		if (valor != null) {
			BigDecimal saldo = contaLocalizada.get().getSaldo();
			Double vlr = Double.valueOf(valor);
			BigDecimal vlr2 = BigDecimal.valueOf(vlr);

			// D = Débito C="Crédito"
			if (operacao.equals("D")) {
				saldo = saldo.subtract(vlr2);
				lmf.setDescricao(motivo.toUpperCase());
				lmf.setDtMovimentacao(LocalDate.now());
				lmf.setNrConta(contaLocalizada.get().getNrConta());
				lmf.setTpMovimentacao(operacao);
				lmf.setUsuario("Elias");
				lmf.setVlMovimentado(vlr2);

			} else {
				saldo = saldo.add(vlr2);
				lmf.setDescricao(motivo);
				lmf.setDtMovimentacao(LocalDate.now());
				lmf.setNrConta(contaLocalizada.get().getNrConta());
				lmf.setTpMovimentacao(operacao);
				lmf.setUsuario("Elias");
				lmf.setVlMovimentado(vlr2);
			}

			Conta novosaldo = contaLocalizada.get();
			novosaldo.setSaldo(saldo);
			repository.save(novosaldo);
			logMovimentacao.save(lmf);
		}
		return rw;
	}

	/**
	 * Transfere valores entre contas
	 * 
	 * @since 25-02-2023
	 * @author Elias
	 * @param ctaorigem
	 * @param ctadestino
	 * @param vlr
	 * @param motivo
	 * @param model
	 */
	public void trasferir(String ctaorigem, String ctadestino, String vlr, String motivo, Model model) {
		ArrayList<Conta> contas = new ArrayList<Conta>();
		List<Conta> listaDeContas = repository.findcontas();
		Conta conta = new Conta();
		vlr = vlr.replace(",", ".");
		String msgTransacao = contaServices.validaTransacao(ctaorigem, ctadestino, vlr);
		boolean contains = msgTransacao.contains("sucesso");

		if (contains) {
			contaServices.transfere(contas, ctaorigem, ctadestino, vlr);
			model.addAttribute("conta", conta);
			model.addAttribute("contas", listaDeContas);
			model.addAttribute("mensagem",
					"O valor " + vlr + " foi  transferido com sucesso para a conta " + ctadestino);
			model.addAttribute("erro", null);
		} else {
			model.addAttribute("conta", conta);
			model.addAttribute("contas", listaDeContas);
			model.addAttribute("erro", msgTransacao);
		}

	}

	/**
	 * Retorna a imagem do QRCode da conta informada
	 * 
	 * @since 25-02-2023
	 * @author Elias
	 * @param id
	 */
	public Optional<Conta> getImagem(Long id) {
		Optional<Conta> conta = repository.findById(id);
		return conta;
	}

	public Conta creditar(Long id, Model model, Conta conta) {
		Conta contaLocalizada = repository.findContaId(id);
		return contaLocalizada;
	}

}
