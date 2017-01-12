package com.journaldev;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class Main {
	public static void main(String [] args) throws IOException, BiffException {
		 String EXCEL_FILE_LOCATION = "C:\\temp\\test2.xls";
//		Workbook wb = null;
//		try {
//			wb = WorkbookFactory.create(new File(EXCEL_FILE_LOCATION));
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InvalidFormatException e) {
//			e.printStackTrace();
//		}
//		Sheet mySheet = wb.getSheetAt(0);
//		Iterator<Row> rowIter = mySheet.rowIterator();
//		System.out.println(mySheet.getRow(1).getCell(0));
//		Workbook workbook = Workbook.getWorkbook(new File(EXCEL_FILE_LOCATION)); //Pasamos el excel que vamos a leer
//		Sheet sheet = workbook.getSheet(0); //Seleccionamos la hoja que vamos a leer
//		String nombre;
//
//		for (int fila = 1; fila < sheet.getRows(); fila++) { //recorremos las filas
//			for (int columna = 0; columna < sheet.getColumns(); columna++) { //recorremos las columnas
//				nombre = sheet.getCell(columna, fila).getContents(); //setear la celda leida a nombre
//				System.out.print(nombre +""); // imprimir nombre
//			}
//			System.out.println("\n");
//		}
		try {
			FileInputStream fileInputStream = new FileInputStream("C:\\temp\\test3.xls");
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			HSSFRow row1 = worksheet.getRow(0);
			HSSFCell cellA1 = row1.getCell((short) 0);
			String a1Val = cellA1.getStringCellValue();
			HSSFCell cellB1 = row1.getCell((short) 1);
			String b1Val = cellB1.getStringCellValue();
			System.out.println("A1: " + a1Val);
			System.out.println("B1: " + b1Val);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
