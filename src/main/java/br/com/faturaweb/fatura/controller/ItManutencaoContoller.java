package br.com.faturaweb.fatura.controller;

import br.com.faturaweb.fatura.services.ItManutencaoServices;
import br.com.faturaweb.fatura.services.PecasServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/item")
public class ItManutencaoContoller {
    @Autowired
    ItManutencaoServices services;

    @Autowired
    PecasServices pecasServices;

    @GetMapping("/cadastro/{idmanutencao}/{idveiculo}")
    public String cadastrarItem(Model model,
                                @PathVariable Long idmanutencao,
                                @PathVariable Long idveiculo,
                                HttpSession session){
        return services.cadastrarItem(model, idmanutencao, idveiculo, session);
    }

    @GetMapping({"/adicionar","/adicionar/{id}/{cdpeca}"})
    public String adicionar(Model model,
                            @PathVariable(required = false) Long id,
                            @PathVariable(required = false) Long cdpeca,
                            HttpSession session){
      return  services.adicionarPecas(model, id, session, cdpeca);
    }
    @GetMapping("/salvar/{id}/{cdpeca}")
    public RedirectView salvar(Model model,
                               @PathVariable(required = false) Long id,
                               @PathVariable(required = false) Long cdpeca,
                               HttpSession session){
        return  services.salvar(model, id, session, cdpeca);
    }
    @GetMapping("/todos/{idmanutencao}/{kmtroca}")
    public RedirectView adicionaTodos(@PathVariable Long idmanutencao, @PathVariable Long kmtroca, HttpSession session){
        return  services.adicionarTodos(idmanutencao, kmtroca,session);
    }

    @GetMapping({"/excluir/{idmanutencao}/{idveiculo}","/excluir/pecas/{idmanutencao}/{idpeca}"})
    public RedirectView excluirtodos(@PathVariable Long idmanutencao,
                                     @PathVariable (required = false) Long idveiculo,
                                     @PathVariable(required = false) Long idpeca){
        return services.excluirTodos(idmanutencao, idveiculo, idpeca);
    }

}
