package br.com.faturaweb.fatura.controller;

import br.com.faturaweb.fatura.model.Pecas;
import br.com.faturaweb.fatura.services.PecasServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/pecas")
public class PecasController  {
    @Autowired
    PecasServices services;

    @GetMapping("/cadastrar")
    public String cadastrar(Model model){
        return services.cadastrar(model);
    }
    @GetMapping("/pesquisar")
    public String pesquisar(Model model){
        return services.pesquisar(model);
    }

    @PostMapping("/adicionar")
    public RedirectView adicionar(Model model, Pecas pecas, RedirectAttributes ra){
        return services.adicionar(model, pecas, ra);
    }
    @GetMapping("/editar/{idpeca}")
    public String editar(Model model, @PathVariable Long idpeca, RedirectAttributes ra){
        return services.editar(model, idpeca, ra);
    }
    @GetMapping("/excluir/{idpeca}")
    public RedirectView excluirPeca(Model model, @PathVariable Long idpeca, RedirectAttributes ra){
        return  services.excluir(model, idpeca,ra);
    }


}
