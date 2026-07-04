package br.com.faturaweb.fatura.services;

import br.com.faturaweb.fatura.model.Fornecedor;
import br.com.faturaweb.fatura.model.ItManutencao;
import br.com.faturaweb.fatura.model.Manutencao;
import br.com.faturaweb.fatura.model.Veiculo;
import br.com.faturaweb.fatura.repository.FornecedorRepository;
import br.com.faturaweb.fatura.repository.ItManutencaoRepository;
import br.com.faturaweb.fatura.repository.ManutencaoRepository;
import br.com.faturaweb.fatura.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Service
public class ManutencaoServices {
    @Autowired
    VeiculoRepository veiculoRepository;
    @Autowired
    ManutencaoRepository repository;
    @Autowired
    FornecedorRepository fornecedorRepository;
    @Autowired
    ItManutencaoRepository itManutencaoRepository;

    public String cadastro(Model model, Manutencao manutencao,
                           Veiculo veiculo,
                           Fornecedor fornecedor,
                           String acao) {

        List<Veiculo> veiculos = veiculoRepository.veiculosAdicionados();
        List<Manutencao> manutencoes = repository.listarManutencao();
        List<Fornecedor> fornecedores = fornecedorRepository.pesquisarFornecedores();
        model.addAttribute("operacao","C");
        model.addAttribute("veiculos",veiculos);
        model.addAttribute("manutencoes",manutencoes);
        model.addAttribute("fornecedores",fornecedores);
        Manutencao m = new Manutencao();
        m.setVeiculo(new Veiculo());
        m.setFornecedor(new Fornecedor());
        model.addAttribute("manutencao", m);
        if (acao.equals("A")){//A = Salvar C=Cadastro
            repository.save(manutencao);
        }
        System.out.println(manutencoes);
        System.out.println(manutencoes.size());
        return "manutencao";
    }

    public RedirectView adicionar(Model model, Manutencao manutencao, Veiculo veiculo, Fornecedor fornecedor) {
        cadastro(model, manutencao, veiculo,fornecedor,"A");
        return new RedirectView("/manutencao/cadastro");
    }

    public String pesquisar(Model model, Manutencao manutencao, Veiculo veiculo, Fornecedor fornecedor) {
        //cadastro(model, manutencao, veiculo,fornecedor,"A");
        List<Veiculo> veiculos = veiculoRepository.veiculosAdicionados();
        List<Manutencao> manutencoes = repository.listarManutencao();
        List<Fornecedor> fornecedores = fornecedorRepository.pesquisarFornecedores();
        model.addAttribute("operacao","P");
        model.addAttribute("veiculos",veiculos);
        model.addAttribute("manutencoes",manutencoes);
        model.addAttribute("fornecedores",fornecedores);
        Manutencao m = new Manutencao();
        m.setVeiculo(new Veiculo());
        m.setFornecedor(new Fornecedor());
        model.addAttribute("manutencao", m);
        return "manutencao";
    }

    public RedirectView excluirManutencao(Model model, Long idManutencao, RedirectAttributes ra) {
        Manutencao manutencao =  repository.localizarManutencao(idManutencao);
        List<ItManutencao> itens = itManutencaoRepository.getItensManutencao(manutencao.getCdManutencao());
        if (!itens.isEmpty()){
            ra.addFlashAttribute("msg","Não foi possível excluir esta manutenção!  - Registros Filhos Encontrado!");
            return new RedirectView("/manutencao/pesquisar");
        }else{
            repository.delete(manutencao);
            ra.addFlashAttribute("msg","Manutenção Excluída com sucesso!");
            return new RedirectView("/manutencao/pesquisar");
        }
    }

}
