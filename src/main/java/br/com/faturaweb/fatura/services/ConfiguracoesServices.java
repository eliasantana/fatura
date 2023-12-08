package br.com.faturaweb.fatura.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import com.google.common.base.Optional;

import br.com.faturaweb.fatura.model.ChaveConfig;
import br.com.faturaweb.fatura.repository.ChaveRepository;
import br.com.faturaweb.fatura.repository.ConfiguracoesRepository;

@Service
public class ConfiguracoesServices {

	@Autowired
	ConfiguracoesRepository repository;
	@Autowired
	ChaveRepository chaveRepository;

	public RedirectView getChave(String chave) {
		//RedirectView rw = new RedirectView("/configuracoes/listar");
		RedirectView rw = new RedirectView("/");
		java.util.Optional<ChaveConfig> chaveConfig = chaveRepository.findChaveConfigByDescricao(chave);
		if (chaveConfig.isPresent()) {
			ChaveConfig chaveLocalizada = chaveConfig.get();
			if ("S".equals(chaveConfig.get().getValor())) {
				chaveLocalizada.setValor("N");
			} else {
				chaveLocalizada.setValor("S");
			}
			chaveRepository.save(chaveLocalizada);
		}
		return rw;
	}
	/**
	 * Retorna o valor da chave informada
	 * @author elias
	 * @since 08-12-2023
	 * @param chave - Nome da Chave
	 * */
	public String  getChaveValor(String chave) {
		java.util.Optional<ChaveConfig> chaveConfig = chaveRepository.findChaveConfigByDescricao(chave);
		String valorChave = null;
		if (chaveConfig.isPresent()) {
			valorChave = chaveConfig.get().getValor();
		}
		return valorChave;
	}
	

}
