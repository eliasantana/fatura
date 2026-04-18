package br.com.faturaweb.fatura.controller;

import br.com.faturaweb.fatura.model.Fornecedor;
import br.com.faturaweb.fatura.services.FornecedorServices;
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
@RequestMapping("/fornecedor")
public class FonecedorController {
    @Autowired
    FornecedorServices services;

    @GetMapping("/cadastrar")
    public String cadastro(Model model, RedirectAttributes ra){
        return services.cadastro(model, ra);
    }
    @PostMapping("/adicionar")
    public String adicionar(Model model, Fornecedor f){
     return services.adicionarFornededor(model,f);
    }
    @GetMapping("/pesquisar")
    public String pesquisar(Model model){
        return services.pesquisar(model);
    }
    @GetMapping("/editar/{idfornecedor}")
    public String pesquisar(Model model , @PathVariable Long idfornecedor){
        return services.localizarFornecedor(model, idfornecedor);
    }
    @GetMapping("/excluir/{idfornecedor}")
    public RedirectView excluirFornecedor(Model model , @PathVariable Long idfornecedor){
      return   services.excluirFornecedor(idfornecedor);
    }
}
