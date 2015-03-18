package com.general.controladores;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
 
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
 
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import com.general.entidades.facDetalleDocumentoEntidad;

@ViewScoped
@ManagedBean(name="manager")
public class PropertyManagerConsulta {
	 
	ArrayList<facDetalleDocumentoEntidad>  cacheList  = new ArrayList ();
	 private String RFCREC; 
	 private String NOMREC; 
	 private String codDoc; 
	 private String TIPODOC; 
	 private String FOLFAC;
	 //VPI
	 //private Double TOTAL; 
	 private String TOTAL; 
	 private Date FECHA; 
	 private String EDOFAC; 
	 private String PDFARC; 
	 private String XMLARC; 
	 private String email; 
	 private String direccion; 
	 private String formatoString; 
	 private String codEstablecimiento; 
	 private String codPuntoEmision; 
	 private String codigoDocumento; 
	 private String secuencial; 
	 private String xmlAutorizacion; 
	 private String ambiente;
	 
	 public void save() {
		 facDetalleDocumentoEntidad tmp = new facDetalleDocumentoEntidad(RFCREC,
																		 NOMREC,
																		 codDoc,
																		 TIPODOC,
																		 FOLFAC,
																		 TOTAL,
																		 FECHA,
																		 EDOFAC,
																		 PDFARC,
																		 XMLARC,
																		 email,
																		 direccion,
																		 formatoString,
																		 codEstablecimiento,
																		 codPuntoEmision,
																		 codigoDocumento,
																		 secuencial, 
																		 xmlAutorizacion,
																		 ambiente);
	        cacheList.add(tmp);
	    }
	 
	 
	    public void clear() {
	        cacheList.clear();
	    }
	 
	    public void postProcessXLS(Object document) {
	        HSSFWorkbook wb = (HSSFWorkbook) document;
	        HSSFSheet sheet = wb.getSheetAt(0);
	        CellStyle style = wb.createCellStyle();
	        style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
	        int i = 0;
	        Row myRow = null;
	        Cell myCell = null;
	        
	        myRow = sheet.createRow(0);
        	myCell = myRow.createCell(0);
        	myCell.setCellValue("Identificacion");        	
        	myCell.setCellStyle(style);
        	myCell = myRow.createCell(1);
        	myCell.setCellValue("Razon Social");
        	myCell.setCellStyle(style);
        	myCell = myRow.createCell(2);
        	myCell.setCellValue("Tipo de Documento");
        	myCell.setCellStyle(style);
        	myCell = myRow.createCell(3);
            myCell.setCellValue("Secuencial");
            myCell.setCellStyle(style);
            myCell = myRow.createCell(4);
            myCell.setCellValue("Total");
            myCell.setCellStyle(style);
            myCell = myRow.createCell(5);
            myCell.setCellValue("Fecha");
            myCell.setCellStyle(style);
            myCell = myRow.createCell(6);
            myCell.setCellValue("Ambiente");
            myCell.setCellStyle(style);
            myCell = myRow.createCell(7);
            myCell.setCellValue("Estado");
            myCell.setCellStyle(style);
	        
	        for (Row row : sheet) {	        	
	            for (Cell cell : row) {
	                cell.setCellValue(cell.getStringCellValue().toUpperCase());
	                cell.setCellStyle(style);
	                i++;
	            }
	        }
	    }
}
