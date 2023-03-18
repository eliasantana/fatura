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
		RedirectView rw = new RedirectView("/configuracoes/listar");
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

}
