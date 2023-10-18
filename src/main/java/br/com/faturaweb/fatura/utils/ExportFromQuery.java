package br.com.faturaweb.fatura.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.DateFormatConverter;
import org.springframework.beans.factory.annotation.Autowired;

import ch.qos.logback.core.subst.Token.Type;

public class ExportFromQuery {
	
	@Autowired
	Connection conn;
	
	/**
	 * @author elias.silva
	 * @since 25-08-2023
	 * @param connection - Conexão BAnco
	 * @param sql - Query com os dados da planilha
	 * @param caminho - Caminho onde o arquivo será gerado ex: c:\\pasta_destino\\nome_arquivo
	 * */
	public ExportFromQuery(Connection connection, String caminho, String sql) throws SQLException {
		HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheetAlunos = workbook.createSheet("teste");
        HSSFDataFormat numberFormat = workbook.createDataFormat();
        HSSFDataFormat data = workbook.createDataFormat();
        
        CreationHelper creationHelper = workbook.getCreationHelper();
        
        CellStyle cellStyleNumber = workbook.createCellStyle();
        cellStyleNumber.setDataFormat(numberFormat.getFormat("#,##0.00"));
        cellStyleNumber.setAlignment(HorizontalAlignment.RIGHT);
        
//        CellStyle cellDataStyle = workbook.createCellStyle();
//        short formatData = creationHelper.createDataFormat().getFormat("dd/mm/yyyy");
//        cellDataStyle.setDataFormat(formatData);
        
        CellStyle cellDataStyle = workbook.createCellStyle();
        cellDataStyle.setDataFormat(numberFormat.getFormat("dd/mm/yyyy"));
        
        CellStyle cellDataStyleAbreviada = workbook.createCellStyle();        
        short formatDataAbreviada = creationHelper.createDataFormat().getFormat("m/yy");
        cellDataStyleAbreviada.setDataFormat(formatDataAbreviada);
        
		PreparedStatement pst=null;
		
		pst = connection.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		rs = pst.getResultSet();
		
		int coluna = 0;
		int totalReg=0;
		
		//Adicionando as colunas
        int rownum =0;
        int cellnum = 0;
        ResultSetMetaData t = rs.getMetaData();
        int columnCount = t.getColumnCount();	
        
		Row row = sheetAlunos.createRow(0);
		int x = 0;
		int yy=0; //Para verificação do tipo
		for (int i=0; i < columnCount ; i++) {
			Cell cell = row.createCell(i);
			if (x==0) {
				x=1;
			}
			cell.setCellValue(t.getColumnName(x++));						
		}
		
		rownum=1;
		int y=1;
		x=0;

		//Adicionando Dados as Colunas
		while(rs.next()) {
			int c=0;
			Row linha = sheetAlunos.createRow(rownum++);
			for (int i=1; i <=columnCount; i++) {
				Cell cell = linha.createCell(c++);
				cell.setCellValue(rs.getString(i));
				if (t.getColumnType(i)==Types.INTEGER || t.getColumnType(i)==Types.DOUBLE || t.getColumnType(i)==Types.DECIMAL ||t.getColumnType(i)==Types.NUMERIC ||t.getColumnType(i)==Types.BIGINT ) {
					cell.setCellStyle(cellStyleNumber);						
				}else if (t.getColumnType(i)==91) {					
					cell.setCellStyle(cellDataStyle);
				}else {
					cell.setCellValue(rs.getString(i));
				}
			}
		}
		
		try {
            FileOutputStream out =  new FileOutputStream(new File(caminho));
            workbook.write(out);
            workbook.close();
            out.close();
            System.out.println("Arquivo Excel criado com sucesso!");
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
               System.out.println("Arquivo não encontrado!");
        } catch (IOException e) {
            e.printStackTrace();
               System.out.println("Erro na edição do arquivo!");
        }  
		
		
	}
	
}
