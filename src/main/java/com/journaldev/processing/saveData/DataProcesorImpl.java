package com.journaldev.processing.saveData;


import com.journaldev.spring.service.ExcelReader;
import com.journaldev.spring.service.GisCargaService;
import com.journaldev.spring.service.TipoDiaService;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.bean.ManagedProperty;
import java.io.*;

@Service("DataProcesorImpl")
public class DataProcesorImpl {


    private ExcelReader excelReader;

    @Autowired
    private GisCargaService gisCargaService;

    @Autowired
    private TipoDiaService tipoDiaService;

    private String destination="C:\\temp\\";

    public DataProcesorImpl() {
        excelReader = new ExcelReader();
    }

    public boolean processDataFromFile(String fileName, InputStream in) {
//        copyFile(fileName,in);
//        try {
//            return excelReader.procesarGisCarga(destination);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println( gisCargaService.getGisCargaAll().size());
        System.out.println( gisCargaService.getTrayectoAll().size());
        System.out.println( tipoDiaService.getTipoDiaAll().size());
        System.out.println( tipoDiaService.getTipoDiaDetalleAll().size());
        return false;

    }

    public void copyFile(String fileName, InputStream in) {
        try {

            destination= destination+fileName;
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destination));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public ExcelReader getExcelReader() {
        return excelReader;
    }

    public void setExcelReader(ExcelReader excelReader) {
        this.excelReader = excelReader;
    }

    public GisCargaService getGisCargaService() {
        return gisCargaService;
    }

    public void setGisCargaService(GisCargaService gisCargaService) {
        this.gisCargaService = gisCargaService;
    }
}
