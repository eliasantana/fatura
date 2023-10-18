package br.com.faturaweb.fatura.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class QueryServices {

	/**
	 * Query que retorna os lancamentos da competencia informada. 
	 * Caso nenhuma competencia seja informada (NULL) retornará os lançamentos da competência atual.
	 * Esta query é utilizada pelo relatório Excel de Lançamentos
	 * @author elias
	 * @since14/09/2023
	 * @param mesano - Mês e ano no formado MMYYYY
	 * @return - Query com os lançamentos da competência informada
	 * */
	public String getLancamentosCompetencia(String mesano) {
		String competencia = null;
		if (mesano==null) {
			competencia = "(date_format(CURDATE(),'%m%Y' ))";
		}else {
			competencia = "'"+mesano+"'";
		}
		String sql = "SELECT                                                                          																"
				+ "	l.cd_lancamento,                                                           																	"
				+ "    l.ds_lancamento,                                                            																	"
				+ "    l.dt_cadastro,                                                              																		"
				+ "    l.dt_competencia,                                                          																	"
				+ "    l.sn_pago,                                                                  																		"
				+ "    l.vl_pago,                                                                 																		    "
				+ "    fp.descricao,                                                               																		"
				+ "    tl.ds_tipo_lancamento,                                                      																"
				+ "    u.nome,                                                                    																		    "
				+ "    nr_parcela,                                                                 																		"
				+ "    l.ds_anexo,                                                                 																		"
				+ "    l.observacao,                                                               																		"
				+ "    c.ds_cartao                                                                 																		"
				+ " FROM lancamento l,                                                              																"
				+ "	 forma_pagto fp,                                                            																	"
				+ "     tipo_lancamento tl,                                                        																	"
				+ "     cartao c,                                                                  																	 		"
				+ "     usuario u                                                                  																	 		"
				+ " where date_format(dt_competencia,'%m%Y') = "+competencia
				+ " and l.forma_de_pagamento_cd_forma_pgamento = fp.cd_forma_pgamento              		 		"
				+ " and l.tipo_lancamento_cd_tipo_lancamento = tl.cd_tipo_lancamento                					 	"
				+ " and l.cartao_cd_cartao = c.cd_cartao                                            												 	"
				+ " and l.usuario_cd_usuario = u.cd_usuario                                         												";

		return sql;
	}
	
	
}
