package br.com.faturaweb.fatura.controller;

import br.com.faturaweb.fatura.services.AbastecimentoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/abastecimento")
public class AbastecimentoController {

    @Autowired
    AbastecimentoServices services;

    @GetMapping("/registrar")
    public String registrar(Model model){
            return services.registrar(model);
    }

    @PostMapping("/salvar")
    public RedirectView salvar(Model model,
                               @RequestParam(name = "cd_veiculo") long cdVeiculo,
                               @RequestParam(name = "dt_abastecimento") String dtAbastecimento,
                               @RequestParam(name = "km_atual") String kmAtual,
                               @RequestParam(name = "litros") String litros,
                               @RequestParam(name = "vl_litro") String valor,
                               RedirectAttributes ra){
        System.out.println(valor);
        return services.salvar(model, cdVeiculo, dtAbastecimento, kmAtual, litros, valor,ra);
    }

    @GetMapping("/gestao")
    public String gestao(Model model){
       return  services.gestao(model);
    }
    @GetMapping("/atualizar")
    public String atualizar(Model model, @RequestParam(name = "cd_veiculo") Long cdVeiculo){
        return services.atualizar(model, cdVeiculo);
    }
}
