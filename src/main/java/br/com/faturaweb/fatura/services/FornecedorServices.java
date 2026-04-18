package br.com.faturaweb.fatura.services;

import br.com.faturaweb.fatura.model.Fornecedor;
import br.com.faturaweb.fatura.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;

@Service
public class FornecedorServices {
    @Autowired
    FornecedorRepository repository;

    public String cadastro(Model model, RedirectAttributes ra) {
        Fornecedor f = new Fornecedor();
        ArrayList<Fornecedor> listaDeFornecedores = repository.pesquisarFornecedores();
        model.addAttribute("f", f);
        model.addAttribute("fornec",listaDeFornecedores );
        model.addAttribute("operacao","C");
        return "cadastro_fornecedor";
    }


    public String adicionarFornededor(Model model, Fornecedor f) {
        Fornecedor fornecedor = new Fornecedor();
        ArrayList<Fornecedor> listaDeFornecedores = repository.pesquisarFornecedores();
        model.addAttribute("f", fornecedor);
        model.addAttribute("operacao","P");
        System.out.println(f.toString());
        Fornecedor fornecedorSalvo = repository.save(f);
        if (fornecedorSalvo.getCdFornecedor()!=null){
            model.addAttribute("msg",String.format("Fornecedor %s salvo com sucesso!",fornecedorSalvo.getNmFornecedor()));
        }else{
            model.addAttribute("msg","Falha ao tentar Salvar um Fornecedor!");
        }
        model.addAttribute("fornec",listaDeFornecedores );
        return "cadastro_fornecedor";
    }

    public String pesquisar(Model model) {
        Fornecedor f = new Fornecedor();
        ArrayList<Fornecedor> listaDeFornecedores = repository.pesquisarFornecedores();
        model.addAttribute("f", f);
        model.addAttribute("operacao","P");
        model.addAttribute("fornec",listaDeFornecedores );
        return "cadastro_fornecedor";
    }

    public String localizarFornecedor(Model model, Long idfornecedor) {
        Fornecedor fornecedorLocalizado =  repository.localizarFornecedor(idfornecedor);
        ArrayList<Fornecedor> listaDeFornecedores = repository.pesquisarFornecedores();
        model.addAttribute("f",fornecedorLocalizado);
        model.addAttribute("operacao","C");
        model.addAttribute("fornec",listaDeFornecedores );
        return "cadastro_fornecedor";
    }

    public RedirectView excluirFornecedor(Long idfornecedor) {
        Fornecedor fornecedorLocalizado = repository.localizarFornecedor(idfornecedor);
        if (fornecedorLocalizado.getCdFornecedor()!=null){
            repository.delete(fornecedorLocalizado);
        }
        return new RedirectView("/fornecedor/pesquisar");
    }
}
