CREATE DEFINER=`root`@`localhost` PROCEDURE `PRC_PROCESSA_FECHAMENTO_CONTABIL`(IN P_MESANO char(20))
BEGIN
########################################################################################################
# OBJETO: PRC_PROCESSA_FECHAMENTO_CONTABIL															   #
# DESCRIÇÃO: Realiza a criação do lote contabil e dos seus itens 									   #
# PARAMETRO: IN P_MESANO - Mês ano 'MM/YYYY'                                                           #
# DATA DA ALTERAÇÃO: 17/08/2022																		   #
# ALTERAÇÃO: 																						   #
# VERSÃO: 1.1																						   #
# VERSÃO DO SISTEMA: v.1.64																			   #
# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

#Localizando os lancamentos da competencia (MM/YYY)
	DECLARE v_finished integer default 0;
    DECLARE v_vl_total_receita decimal(19,2) default 0;   
    DECLARE v_ds_lote char(255);
    DECLARE v_total_lote decimal (19,2);
    DECLARE v_qtd_lancamento integer;
    DECLARE v_competencia char(20);
	DECLARE v_status char (1) default "A";
    DECLARE v_cd_lote_localizado integer;
    
   #Coletando informação para o lote
	SELECT 'FECHAMENTO CONTABIL ' DSLOTE,
	   sum(vl_pago) total_lote, 
	   count(cd_lancamento) qtd_lancamento,
       P_MESANO competencia
	INTO v_ds_lote, v_total_lote, v_qtd_lancamento, v_competencia
	FROM lancamento  
		where date_format(dt_competencia,'%m%Y') = P_MESANO;
        
   SELECT cd_lote INTO v_cd_lote_localizado 
		FROM LOTE WHERE COMPETENCIA = P_MESANO AND STATUS='A';
    
    IF v_cd_lote_localizado IS NOT NULL THEN 
		DELETE FROM LOTE WHERE CD_LOTE = v_cd_lote_localizado;
        COMMIT;
    END IF;
  
    #Buscando o total de receitas     
    select sal_liquido  into v_vl_total_receita
		from receita where date_format(dt_recebimento,'%m%Y') = P_MESANO;
	
    INSERT INTO LOTE (					
                     ds_lote
                    , competencia
                    , qtd_lancamentos
                    , vl_total_lote
                    , vl_total_receita
                    , vl_saldo
                    , status)
				values (
					concat(v_ds_lote, ' - ' ,P_MESANO) 
                    ,P_MESANO
                    ,v_qtd_lancamento
                    ,v_total_lote
                    ,v_vl_total_receita
                    ,v_vl_total_receita - v_total_lote
                    ,v_status
                );
    commit;
     Select * from lote;
    
END