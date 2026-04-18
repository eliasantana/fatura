package br.com.faturaweb.fatura.services;

import br.com.faturaweb.fatura.model.Abastecimento;
import br.com.faturaweb.fatura.model.Veiculo;
import br.com.faturaweb.fatura.projection.ViewConsumo;
import br.com.faturaweb.fatura.repository.AbastecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AbastecimentoServices {
    @Autowired
    VeiculoServices veiculoServices;

    @Autowired
    AbastecimentoRepository repository;
    public String registrar(Model model) {
        List<Veiculo> veiculos= veiculoServices.veiculosAdicionados();
        model.addAttribute("veiculos",veiculos);
        return "formulario_abastecimento";
    }

    public RedirectView salvar(Model model,
                               long cdVeiculo,
                               String dtAbastecimento,
                               String kmAtual,
                               String litros,
                               String valor,
                               RedirectAttributes ra) {

       Optional<Veiculo> veiculoLocalizado = veiculoServices.repository.localizarVeiculo(cdVeiculo);
       Optional<Abastecimento> kmAbastecimentoMaximo = repository.kmMaximo(cdVeiculo);
       if (veiculoLocalizado.isPresent()){
            if (kmAbastecimentoMaximo.isPresent()){
                if (kmAbastecimentoMaximo.get().getKmAtual().compareTo(BigDecimal.valueOf(Long.parseLong(kmAtual))) > 0){
                    ra.addFlashAttribute("msg","A kilometragem atual é menor que a km atual já informada");
                }
            }
               Abastecimento abastecimento = new Abastecimento();
               abastecimento.setDtAbastecimento(LocalDate.parse(dtAbastecimento));
               abastecimento.setVeiculo(veiculoLocalizado.get());
               abastecimento.setLitros(new BigDecimal(litros).setScale(3,RoundingMode.DOWN));
               abastecimento.setKmAtual(BigDecimal.valueOf(Long.parseLong(kmAtual)));
               abastecimento.setVlLitro(new BigDecimal(valor).setScale(3, RoundingMode.DOWN));
               Abastecimento abastecimentoSalvo = repository.save(abastecimento);
               if(abastecimentoSalvo!=null){
                   ra.addFlashAttribute("msg","Abastecimento regisrado com sucesso!");
               }
       }
        return new RedirectView("/abastecimento/registrar");
    }


    public String gestao(Model model) {
      List<Veiculo> veiculos =veiculoServices.repository.veiculosAdicionados();
        model.addAttribute("veiculos", veiculos);
        model.addAttribute("modelo", null);
        return "gestao_abastecimento";
    }

    public String atualizar(Model model, Long cdVeiculo){
        List<Veiculo> veiculos =veiculoServices.repository.veiculosAdicionados();
        Optional<Abastecimento> kmMaximo =  repository.kmMaximo(cdVeiculo);
        Optional<Abastecimento> kmMinimo = repository.kmMinimo(cdVeiculo);
        Optional<Abastecimento> kmMesMinimo = repository.kmMinimoMes(cdVeiculo);
        Optional<Abastecimento> kmMesMaximo = repository.kmMaximoMes(cdVeiculo);
        List<Abastecimento> abastecimentoGeral = repository.todosOsAbastecimentos(cdVeiculo);
        List<ViewConsumo>evolucaoDeConsumo = repository.evolcaoConsumo(cdVeiculo);
        List<String>mesesConsumo =  retornaConsumoPorVeiuloMes(evolucaoDeConsumo);
        List<BigDecimal> litrosConsumo = retornaConsumoPorVeiculoLitros(evolucaoDeConsumo);
        System.out.println(mesesConsumo.toString());
        System.out.println(litrosConsumo.toString());
        BigDecimal kilometragemPercorrida = calculaKmPercorrido(kmMaximo, kmMinimo);
        BigDecimal kilometragemPercorridaNoMes = calculaKmPercorrido(kmMesMaximo, kmMesMinimo);
        BigDecimal consumoMensal = calculaConsumo(kilometragemPercorridaNoMes, cdVeiculo);
        BigDecimal consumoMedioGeral = calculaConsumo(kilometragemPercorrida, cdVeiculo);
        Map<YearMonth, Double> dados=totalizakmPorMes(abastecimentoGeral);
        List<YearMonth> meses = new ArrayList<>(dados.keySet());
        List<Double> valores = new ArrayList<>(dados.values());
        Map<YearMonth, Double> totalLitrosMes = totalizaLitrosPorMes(abastecimentoGeral);
        System.out.println(totalLitrosMes.values());
        model.addAttribute("veiculos", veiculos);
        model.addAttribute("kmpercorrido", kilometragemPercorrida);
        model.addAttribute("consumomediogeral", consumoMedioGeral);
        model.addAttribute("abastecimentogeral", abastecimentoGeral);
        model.addAttribute("cosumomensal", consumoMensal);
        model.addAttribute("ultimovalorpago", kmMaximo.get().getVlLitro());
        model.addAttribute("meses",meses);
        model.addAttribute("valores",valores);
        model.addAttribute("litrosmes",totalLitrosMes.values());
        model.addAttribute("cosumomeses",mesesConsumo);
        model.addAttribute("cosumolitros",litrosConsumo);


        return("gestao_abastecimento");
    }

    private List<String> retornaConsumoPorVeiuloMes(List<ViewConsumo> evolucaoDeConsumo) {
        List<String> consumoPorveiculo = new ArrayList<>();

        for (int i = 0; i < evolucaoDeConsumo.size(); i++) {
            switch (evolucaoDeConsumo.get(i).getMes()) {
                case 1 :
                    consumoPorveiculo.add("Janeiro");
                    break;
                case 2 :
                    consumoPorveiculo.add("Fevereiro");
                    break;
                case 3 :
                    consumoPorveiculo.add("Março");
                    break;
                case 4 :
                    consumoPorveiculo.add("Abril");
                    break;
                case 5 :
                    consumoPorveiculo.add("Maio");
                    break;
                case 6 :
                    consumoPorveiculo.add("Junho");
                    break;
                case 7 :
                    consumoPorveiculo.add("Julho");
                    break;
                case 8 :
                    consumoPorveiculo.add("Agosto");
                    break;
                case 9 :
                    consumoPorveiculo.add("Setembro");
                    break;
                case 10 :
                    consumoPorveiculo.add("Outubro");
                    break;
                case 11 :
                    consumoPorveiculo.add("Novembro");
                    break;
                case 12 :
                    consumoPorveiculo.add("Dezembro");
                    break;
            }
        }
        return consumoPorveiculo;
    }

    public BigDecimal calculaKmPercorrido(Optional<Abastecimento> kmMaximo,Optional<Abastecimento> kmMinimo){
        BigDecimal kilometragemMaxima = BigDecimal.ZERO;
        BigDecimal kilometragemMinima = BigDecimal.ZERO;
        BigDecimal kilometragemPercorrida = BigDecimal.ZERO;
        if (kmMaximo.isPresent()){
            kilometragemMaxima= kmMaximo.get().getKmAtual();
        }
        if (kmMinimo.isPresent()){
            kilometragemMinima= kmMinimo.get().getKmAtual();
            kilometragemPercorrida = kilometragemMaxima.subtract(kilometragemMinima);
        }
        return kilometragemPercorrida;
    }

    public BigDecimal calculaConsumo(BigDecimal kmPercorrido, Long cdVeiculo){
        List<Abastecimento> abastecimentoGeral = repository.todosOsAbastecimentos(cdVeiculo);
        BigDecimal consumo = BigDecimal.ZERO;
        if (!abastecimentoGeral.isEmpty()){
           BigDecimal litros = abastecimentoGeral.stream().map(Abastecimento ::getLitros).reduce(BigDecimal.ZERO, BigDecimal::add);
           consumo = kmPercorrido.divide(litros,3,RoundingMode.HALF_UP);
        }
        return consumo;
    }

    public Map<YearMonth, Double> totalizakmPorMes( List<Abastecimento> abastecimentos){
        Map<YearMonth, Double> kmPorMes = abastecimentos.stream()
                .collect(Collectors.groupingBy(
                        a -> YearMonth.from(a.getDtAbastecimento()), // Agrupa por Ano-Mês
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                lista -> {
                                    // Encontra o KM máximo e mínimo usando compareTo de BigDecimal
                                    BigDecimal minKm = lista.stream()
                                            .map(Abastecimento::getKmAtual)
                                            .min(BigDecimal::compareTo)
                                            .orElse(BigDecimal.ZERO);

                                    BigDecimal maxKm = lista.stream()
                                            .map(Abastecimento::getKmAtual)
                                            .max(BigDecimal::compareTo)
                                            .orElse(BigDecimal.ZERO);

                                    // Retorna a diferença como Double
                                    return maxKm.subtract(minKm).doubleValue();
                                }
                        )
                ));
        return kmPorMes;
    }
    public Map<YearMonth, Double> totalizaLitrosPorMes(List<Abastecimento> abastecimentos) {
        return abastecimentos.stream()
                .collect(Collectors.groupingBy(
                        a -> YearMonth.from(a.getDtAbastecimento()), // Chave: Ano-Mês
                        Collectors.summingDouble(a -> a.getLitros().doubleValue()) // Soma como Double
                ));
    }

    private  List<BigDecimal>retornaConsumoPorVeiculoLitros(List<ViewConsumo> evolucaoDeConsumo) {
        List<BigDecimal> consumoLitros = new ArrayList<>();
        for (int i = 0; i < evolucaoDeConsumo.size(); i++) {
            consumoLitros.add(evolucaoDeConsumo.get(i).getLitros());
        }
        return consumoLitros;
    }

}

