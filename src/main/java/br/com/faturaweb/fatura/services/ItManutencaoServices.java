package br.com.faturaweb.fatura.services;

import br.com.faturaweb.fatura.model.ItManutencao;
import br.com.faturaweb.fatura.model.Manutencao;
import br.com.faturaweb.fatura.model.Pecas;
import br.com.faturaweb.fatura.model.Veiculo;
import br.com.faturaweb.fatura.repository.ItManutencaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Service
public class ItManutencaoServices {
    @Autowired
    ItManutencaoRepository repository;
    @Autowired
    ManutencaoServices manutencaoServices;
    @Autowired
    PecasServices pecasServices;
    @Autowired
    ItManutencaoServices itManutencaoServices;

    public List<ItManutencao> itens() {
        return repository.getItensManutencao();
    }

    public String cadastrarItem(Model model, Long idmanutencao, Long idveiculo, HttpSession session) {
        Manutencao manutencao=  manutencaoServices.repository.localizarManutencao(idmanutencao);
        List<Pecas> pecasLocalizadas = pecasServices.getPecas();
        List<ItManutencao> itensManutencao = itManutencaoServices.getItensMautencao(idmanutencao);
        System.out.println(itensManutencao.toString());
        Veiculo veiculo = new Veiculo();
       if (manutencao.getVeiculo()!=null){
           veiculo = manutencao.getVeiculo();
           session.setAttribute("cdManutencao",manutencao.getCdManutencao());
           session.setAttribute("km",manutencao.getKm());
           session.setAttribute("idveiculo",manutencao.getVeiculo());

       }
       model.addAttribute("itensmanutencao",itensManutencao);
       model.addAttribute("operacao","C");
       model.addAttribute("manutencao",manutencao);
       model.addAttribute("veiculo",veiculo);
       model.addAttribute("pecas",pecasLocalizadas);
       return "itensManutencao";
    }

    private List<ItManutencao> getItensMautencao(Long idmanutencao) {
        return repository.getItensManutencao(idmanutencao);
    }

    public String adicionarPecas(Model model, Long id, HttpSession session, Long cdpeca) {
        Long cdManutencao = Long.valueOf(session.getAttribute("cdManutencao").toString());
        String kilometragem = session.getAttribute("km").toString();
        kilometragem = kilometragem.replaceAll("\\.0$", "");
        List<Pecas> pecasLocalizadas = pecasServices.getPecas(kilometragem);
        model.addAttribute("operacao","A");
        model.addAttribute("pecas",pecasLocalizadas);
        model.addAttribute("cdmanutencao",cdManutencao);
        model.addAttribute("kilometragem",kilometragem);
        return "itensManutencao";
    }


    public RedirectView salvar(Model model, Long id, HttpSession session, Long cdpeca) {
        Long cdManutencao = Long.valueOf(session.getAttribute("cdManutencao").toString());
        Optional<ItManutencao> itemLocalizado= itManutencaoServices.repository.localiarItem(cdManutencao, cdpeca);
        Pecas pecas = new Pecas();
        pecas.setCdPecas(cdpeca);
        Manutencao m = new Manutencao();
        m.setCdManutencao(cdManutencao);
        ItManutencao it = new ItManutencao();
        it.setManutencao(m);
        it.setPeca(pecas);
        if (!itemLocalizado.isPresent()){
            repository.save(it);
        }
        return new RedirectView("/item/adicionar");
    }

    public RedirectView adicionarTodos(Long kmtroca, Long idmanutencao, HttpSession session) {
        repository.adicionarTodos(kmtroca,idmanutencao);
       return new RedirectView("/item/adicionar");
    }

    public RedirectView excluirTodos(Long idmanutencao, Long idveiculo, Long idpeca) {
        if (idpeca!=null){
            Optional<ItManutencao> itemLocalizdo=repository.localiarItem(idmanutencao, idpeca);
            itemLocalizdo.ifPresent(itManutencao -> repository.delete(itManutencao));
            idveiculo = itemLocalizdo.get().getManutencao().getVeiculo().getCdVeiculo();
        }else{
           List<ItManutencao> itens =  repository.getItensManutencao(idmanutencao);
           if (!itens.isEmpty()){
               repository.deleteAll(itens);
           }
        }
        //return new RedirectView("/item/cadastro/"+idmanutencao+"/"+ idveiculo);
        return new RedirectView("/item/adicionar");
    }
}
