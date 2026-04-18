package br.com.faturaweb.fatura.controller;

import br.com.faturaweb.fatura.model.Fornecedor;
import br.com.faturaweb.fatura.model.Manutencao;
import br.com.faturaweb.fatura.model.Veiculo;
import br.com.faturaweb.fatura.services.ManutencaoServices;
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
@RequestMapping("/manutencao")
public class ManutencaoController {
    @Autowired
    ManutencaoServices services;
    @GetMapping("/cadastro")
    public String manutencao(Model model, Manutencao manutencao, Veiculo veiculo, Fornecedor fornecedor){
        return services.cadastro(model, manutencao, veiculo, fornecedor,"C");
    }
    @PostMapping("/adicionar")
    public RedirectView adicionar(Model model, Manutencao manutencao, Veiculo veiculo, Fornecedor fornecedor){
        return services.adicionar(model, manutencao, veiculo, fornecedor);
    }
    @GetMapping("/pesquisar")
    public String pesquisar(Model model, Manutencao manutencao, Veiculo veiculo, Fornecedor fornecedor){
        return services.pesquisar(model, manutencao, veiculo, fornecedor);
    }

    @GetMapping("/excluir/{idManutencao}")
    public RedirectView excluir(Model model, @PathVariable Long idManutencao, RedirectAttributes ra){
        return services.excluirManutencao(model, idManutencao, ra);
    }

}
