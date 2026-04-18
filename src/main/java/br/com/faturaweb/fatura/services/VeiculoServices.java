package br.com.faturaweb.fatura.services;

import br.com.faturaweb.fatura.model.Veiculo;
import br.com.faturaweb.fatura.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoServices {
    @Autowired
    VeiculoRepository repository;


    public String cadastro(Model model) {
        Veiculo v = new Veiculo();
        model.addAttribute("veiculo",v);
        model.addAttribute("veiculosadicionados",repository.veiculosAdicionados());
        model.addAttribute("msg",null);
        model.addAttribute("operacao","C");
        return "veiculos";
    }

    public String adicionar(Model model, Veiculo veiculo) {
        Veiculo veiculoAdicionado = repository.save(veiculo);
        Veiculo v = new Veiculo();
        model.addAttribute("operacao","P");
        model.addAttribute("veiculo",v);
        model.addAttribute("veiculosadicionados", repository.veiculosAdicionados());
        if (veiculoAdicionado.getCdVeiculo()!=null){
            model.addAttribute("msg","Veículo adicionado com sucesso!");
        }else{
            model.addAttribute("msg","Erro ao tentare incluir o veículo!");
        }
        return "veiculos";
    }

    public String editar(Model model, Long idveiculo) {
        Optional<Veiculo> veiculoLocalizado = repository.localizarVeiculo(idveiculo);
        model.addAttribute("operacao","C");
        if (veiculoLocalizado.isPresent()){
            Veiculo v = veiculoLocalizado.get();
            model.addAttribute("veiculo",v);
        }else{
            model.addAttribute("veiculo",new Veiculo());
        }
        model.addAttribute("veiculosadicionados", repository.veiculosAdicionados());
        return "veiculos";
    }

    public RedirectView excluirVeiculo(Long cdVeiculo, Model model, RedirectAttributes ra) {
        Optional<Veiculo> veiculoLocalizado = repository.localizarVeiculo(cdVeiculo);
        RedirectView vw = new RedirectView("/veiculo/pesquisar");
        if (veiculoLocalizado.isPresent()){
            repository.delete(veiculoLocalizado.get());
            ra.addFlashAttribute("msg",String.format("O Veiculo %s - %s foi excluído com sucesso!",veiculoLocalizado.get().getFabricante(), veiculoLocalizado.get().getModelo()));
        }else{
            ra.addFlashAttribute(String.format("msg","Erro ao tentar excluir o veículo %s - %s",veiculoLocalizado.get().getFabricante(), veiculoLocalizado.get().getModelo()));
        }
        return vw;
    }

    public String pesquisar(Model model) {
        Veiculo v = new Veiculo();
        model.addAttribute("veiculo",v);
        model.addAttribute("veiculosadicionados",repository.veiculosAdicionados());
        model.addAttribute("msg",null);
        model.addAttribute("operacao","P");
        return "veiculos";

    }

    public List<Veiculo>veiculosAdicionados(){
        return repository.veiculosAdicionados();
    }


}
