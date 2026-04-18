package br.com.faturaweb.fatura.services;

import br.com.faturaweb.fatura.model.Pecas;
import br.com.faturaweb.fatura.repository.PecasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Service
public class PecasServices {
    @Autowired
    PecasRepository repository;

    public String cadastrar(Model model) {
        List<Pecas> pecasLocalizadas = repository.pecasLocalizadas();
        Pecas p = new Pecas();
        model.addAttribute("operacao","C");
        model.addAttribute("pecas",pecasLocalizadas);
        model.addAttribute("p",p);
        model.addAttribute("msg",null);
        return ("pecas");
    }

    public String pesquisar(Model model) {
        Pecas p = new Pecas();
        List<Pecas> pecasLocalizadas = repository.pecasLocalizadas();
        model.addAttribute("operacao","P");
        model.addAttribute("pecas",pecasLocalizadas);
        model.addAttribute("p",p);
        model.addAttribute("msg",null);
        return ("pecas");
    }

    public RedirectView adicionar(Model model, Pecas pecas, RedirectAttributes ra) {
        Pecas p = new Pecas();
        model.addAttribute("operacao","P");
        Pecas pecaSalva = repository.save(pecas);
        if (pecaSalva.getCdPecas()!=null){
            ra.addFlashAttribute("msg","Peça Salva com sucesso!");
        }else{
            ra.addFlashAttribute("msg","Erro ao entar salvar!");
        }
        return new RedirectView("/pecas/pesquisar");
    }

    public String editar(Model model, Long idpeca, RedirectAttributes ra) {
        List<Pecas> pecasLocalizadas = repository.pecasLocalizadas();
       Optional<Pecas>pecaLocalizada =  repository.localizarPecas(idpeca);
       if (pecaLocalizada.isPresent()){
           model.addAttribute("operacao","C");
           model.addAttribute("p", pecaLocalizada.get());
           model.addAttribute("pecas",pecasLocalizadas);
       }
        return "pecas";
    }

    public RedirectView excluir(Model model, Long idpeca, RedirectAttributes ra) {
        Optional<Pecas>pecaLocalizada =  repository.localizarPecas(idpeca);
        if (pecaLocalizada.isPresent()){
            repository.delete(pecaLocalizada.get());
            ra.addFlashAttribute("msg", "Peça excluída com sucesso!");
        }else{
            ra.addFlashAttribute("msg", "Erro ao tentar excluir a peça informada!");
        }
        return new RedirectView("/pecas/pesquisar");
    }
    public List<Pecas> getPecas(){
        return repository.pecasLocalizadas();
    }
    public List<Pecas> getPecas(String km){
        return repository.pecasLocalizadas(km);
    }
}
