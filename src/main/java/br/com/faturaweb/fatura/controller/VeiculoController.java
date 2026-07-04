package br.com.faturaweb.fatura.controller;

import br.com.faturaweb.fatura.model.Veiculo;
import br.com.faturaweb.fatura.services.VeiculoServices;
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
@RequestMapping("/veiculo")
public class VeiculoController {
@Autowired
VeiculoServices services;
@GetMapping("/cadastro")
public String cadastro(Model model){
    return services.cadastro(model);
}

@PostMapping("/adicionar")
public String adicionar(Model model, Veiculo veiculo, RedirectAttributes ra){
    return services.adicionar(model, veiculo);
}
@GetMapping("/editar/{idveiculo}")
public String editar(Model model, @PathVariable Long idveiculo){
   return services.editar(model,idveiculo);
}
@GetMapping("/excluir/{cdVeiculo}")
public RedirectView excluir (Model model, @PathVariable Long cdVeiculo, RedirectAttributes ra){
    return services.excluirVeiculo(cdVeiculo, model, ra);
}
@GetMapping("/pesquisar")
public String excluir (Model model){
    return services.pesquisar(model);
}


}