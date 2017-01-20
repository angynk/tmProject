package com.tmModulos.controlador.procesador;


import com.tmModulos.controlador.servicios.GisCargaService;
import com.tmModulos.controlador.servicios.NodoService;
import com.tmModulos.controlador.servicios.TipoDiaService;
import com.tmModulos.controlador.utils.GisCargaDefinition;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.entity.tmData.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service("DataProcesorImpl")
public class DataProcesorImpl {




    @Autowired
    private GisCargaService gisCargaService;

    @Autowired
    private TipoDiaService tipoDiaService;

    @Autowired
    private NodoService nodoService;

    @Autowired
    private ProcessorUtils processorUtils;

    private String destination="C:\\temp\\";

    public DataProcesorImpl() {

    }

    public boolean processDataFromFile(String fileName, InputStream in, Date fechaProgrmacion, Date fechaVigencia, String tipoDia, String descripcion) {
        processorUtils.copyFile(fileName,in,destination);
        destination=destination+fileName;
        GisCarga gisCarga = saveGisCarga(fechaProgrmacion,fechaVigencia,descripcion);
        try {
            readExcelAndSaveData(destination,gisCarga,tipoDia);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    public GisCarga saveGisCarga(Date fechaProgrmacion, Date fechaVigencia,String descripcion){
        GisCarga gisCarga = new GisCarga(new Date(),fechaProgrmacion,fechaVigencia,descripcion);
       gisCargaService.addGisCarga(gisCarga);
        return gisCarga;
    }

    public Trayecto findOrSaveTrayecto(Row row){
        String trayectoId =  row.getCell(GisCargaDefinition.TRAYECTO).getStringCellValue();
        List<Trayecto> trayectoByIdentifier = gisCargaService.getTrayectoByIdentifier(trayectoId);
        if( trayectoByIdentifier.size() == 0 ){
            int linea = Integer.parseInt( row.getCell(GisCargaDefinition.LINEA ).getStringCellValue());
            Trayecto trayecto = new Trayecto( trayectoId, linea );
            gisCargaService.addTrayecto(trayecto);
            return trayecto;
        }
        return trayectoByIdentifier.get(0);
    }


    public void readExcelAndSaveData(String destination,GisCarga gisCarga, String tipoDiaD)throws IOException{
        try {
            FileInputStream fileInputStream = new FileInputStream(destination);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = worksheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if( row.getCell(0) != null ){
                    Trayecto trayecto = findOrSaveTrayecto(row);
                    TipoDiaDetalle tipoDia = findOrSaveTipoDia(row,tipoDiaD);
                    Nodo nodoInicial = findOrSaveNodo(row, GisCargaDefinition.NODOINICIO);
                    Nodo nodoFinal = findOrSaveNodo(row, GisCargaDefinition.NODOFINAL);
                    saveArcoTiempo(row,gisCarga,trayecto,tipoDia,nodoInicial,nodoFinal);
                }else{
                    break;
                }
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveArcoTiempo(Row row,GisCarga gisCarga, Trayecto trayecto, TipoDiaDetalle tipoDia, Nodo nodoInicial, Nodo nodoFinal) {

        int distancia = Integer.parseInt(row.getCell( GisCargaDefinition.DISTANCIA ).getStringCellValue());
        int secuencia = Integer.parseInt(row.getCell( GisCargaDefinition.SECUENCIA ).getStringCellValue());
        int sentido = Integer.parseInt(row.getCell( GisCargaDefinition.SENTIDO ).getStringCellValue());
        int tipoArco = Integer.parseInt(row.getCell( GisCargaDefinition.TIPOARCO ).getStringCellValue());
        String horaDesde = row.getCell( GisCargaDefinition.HORADESDE ).getStringCellValue();
        String horaHasta = row.getCell( GisCargaDefinition.HORAHASTA).getStringCellValue();
        String tiempoMinimo = row.getCell( GisCargaDefinition.TIEMPOMINIMO ).getStringCellValue();
        String tiempoMaximo = row.getCell( GisCargaDefinition.TIEMPOMAXIMO ).getStringCellValue();
        String tiempoOptimo = row.getCell( GisCargaDefinition.TIEMPOOPTIMO ).getStringCellValue();

        ArcoTiempo arcoTiempo = new ArcoTiempo(
                sentido,secuencia,tipoArco,
                distancia,horaDesde,horaHasta,
                tiempoMinimo,tiempoMaximo,tiempoOptimo,
                gisCarga,trayecto,tipoDia,
                nodoInicial,nodoFinal
        );

        gisCargaService.addArcoTiempo( arcoTiempo );

    }



    private Nodo findOrSaveNodo(Row row, int nodoinicio) {
         String nodoNombre = row.getCell(nodoinicio).getStringCellValue();
         List<Nodo> nodos = nodoService.getNodo( nodoNombre );
        if( nodos.size() == 0 ){
            Nodo nodo = new Nodo(nodoNombre);
            nodoService.addNodo( nodo );
            return nodo;
        }
        return  nodos.get(0);
    }

    private TipoDiaDetalle findOrSaveTipoDia(Row row,String tipoDiaD) {
        String tipoDiaNombre = row.getCell(GisCargaDefinition.TIPODIA).getStringCellValue();
        List<TipoDiaDetalle> tipoDiaByDetalle =  tipoDiaService.getTipoDiaByDetalle( tipoDiaNombre );
        if( tipoDiaByDetalle.size() == 0 ){
            TipoDia tipoDia = tipoDiaService.getTipoDia( tipoDiaD );
            TipoDiaDetalle tipoDiaDetalle = new TipoDiaDetalle( tipoDiaNombre, tipoDia );
            tipoDiaService.addTipoDiaDetalle( tipoDiaDetalle );
            return tipoDiaDetalle;
        }
        return tipoDiaByDetalle.get(0);
    }




    public GisCargaService getGisCargaService() {
        return gisCargaService;
    }

    public void setGisCargaService(GisCargaService gisCargaService) {
        this.gisCargaService = gisCargaService;
    }

    public ProcessorUtils getProcessorUtils() {
        return processorUtils;
    }

    public void setProcessorUtils(ProcessorUtils processorUtils) {
        this.processorUtils = processorUtils;
    }
}
