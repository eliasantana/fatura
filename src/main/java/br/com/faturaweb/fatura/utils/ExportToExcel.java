package br.com.faturaweb.fatura.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import br.com.faturaweb.fatura.model.Lancamento;

public class ExportToExcel {

	private String nomePlanilha;
	
	@SuppressWarnings("null")
	public ExportToExcel( String nomePlanilha, List<Lancamento>dados, List<?>listaColunasCabecalho, String path) {
				this.nomePlanilha = nomePlanilha;
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet hssfSheet = workbook.createSheet(nomePlanilha);
				hssfSheet.setDefaultColumnWidth(15);
				hssfSheet.setDefaultRowHeight((short) 400);
				int rownum = 0;
				int cellnum=0;
				Cell cell;
				Row row;
				HSSFDataFormat numberFormat  = workbook.createDataFormat();
				CellStyle hederStyle = workbook.createCellStyle();
				hederStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				hederStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				hederStyle.setAlignment(HorizontalAlignment.CENTER);
				hederStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				CellStyle textStyle = workbook.createCellStyle();
				textStyle.setAlignment(HorizontalAlignment.CENTER);
				textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				CellStyle numberStyle = workbook.createCellStyle();
			   numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00"));
			   numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			   CellStyle dataStyle = workbook.createCellStyle();
			   CreationHelper creationHelper = workbook.getCreationHelper();
			   short format = creationHelper.createDataFormat().getFormat("dd/mm/yyyy");
			   dataStyle.setDataFormat(format);
			   CellStyle dataAbreviadaStyle = workbook.createCellStyle();			   
			   short dataAbreviadaFormat = creationHelper.createDataFormat().getFormat("m/yy");
			   dataAbreviadaStyle.setDataFormat(dataAbreviadaFormat);
			   row = hssfSheet.createRow(0);
			   for (int i=0; i < listaColunasCabecalho.size(); i++) {				  
				  cell = row.createCell(i);
				  cell.setCellStyle(hederStyle);
				  cell.setCellValue( listaColunasCabecalho.get(i).toString().toUpperCase());
			  }
			  List<Lancamento> lancamentos = dados;
			  rownum =1;
			  for (Lancamento lancamento : lancamentos) {
				  cellnum =0;				  
				  row = hssfSheet.createRow(rownum++);
				  cell=row.createCell(cellnum++);
				  cell.setCellStyle(textStyle);
				  cell.setCellValue(lancamento.getCdLancamento());
				  cell=row.createCell(cellnum++);
				  cell.setCellStyle(textStyle);
				  cell.setCellValue(lancamento.getDsLancamento());
				  cell=row.createCell(cellnum++);
				  cell.setCellStyle(dataStyle);
				  cell.setCellValue(lancamento.getDtCadastro());
				  cell=row.createCell(cellnum++);
				  cell.setCellStyle(dataAbreviadaStyle);
				  cell.setCellValue(lancamento.getDtCompetencia());
				  cell=row.createCell(cellnum++);
				  cell.setCellStyle(textStyle);
				  if (lancamento.getSnPago().equalsIgnoreCase("S")) {
					  cell.setCellValue("sim".toUpperCase());					  
				  }else {
					  cell.setCellValue("não".toUpperCase());	
				  }				  
				  cell=row.createCell(cellnum++);
				  cell.setCellStyle(numberStyle);
				  cell.setCellValue(lancamento.getVlPago().doubleValue());
				  cell=row.createCell(cellnum++);
				  cell.setCellStyle(textStyle);
				  cell.setCellValue(lancamento.getFormaDePagamento().getDescricao().toString());
				  cell=row.createCell(cellnum++);
				  cell.setCellStyle(textStyle);
				  cell.setCellValue(lancamento.getTipoLancamento().getDsTipoLancamento().toString());
				   cell=row.createCell(cellnum++);
				  cell.setCellStyle(textStyle);
				  cell.setCellValue(lancamento.getUsuario().getNome());
				  cell=row.createCell(cellnum++);
				  cell.setCellStyle(textStyle);
				  cell.setCellValue(lancamento.getNrParcela());
				  cell=row.createCell(cellnum++);
				  cell.setCellStyle(textStyle);
				  cell.setCellValue(lancamento.getObservacao());
			  }
			  try {
				 FileOutputStream out = new FileOutputStream(new File(path+"\\"+nomePlanilha+".xlsx"));
				 workbook.write(out);
				 out.close();
				 workbook.close();
			} catch (Exception e) {
				e.getStackTrace();
			}
	}

	public String getNomePlanilha() {
		return nomePlanilha;
	}

	public void setNomePlanilha(String nomePlanilha) {
		this.nomePlanilha = nomePlanilha;
	}
}
