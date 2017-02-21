package com.tmModulos.controlador.procesador;


import com.tmModulos.controlador.servicios.GisCargaService;
import com.tmModulos.controlador.servicios.NodoService;
import com.tmModulos.controlador.servicios.TipoDiaService;
import com.tmModulos.controlador.utils.GisCargaDefinition;
import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.controlador.utils.TipoLog;
import com.tmModulos.modelo.entity.tmData.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
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
    private boolean exitoso;

    private static Logger log = Logger.getLogger(DataProcesorImpl.class);


    public DataProcesorImpl() {

    }

    private List<String> serviciosNoEncontrados;
    private List<LogDatos> logDatos;



    public List<LogDatos> processDataFromFile(String fileName, InputStream in, Date fechaProgrmacion, Date fechaVigencia, String tipoDia, String descripcion) {
        logDatos = new ArrayList<>();
        exitoso=true;
        log.info("<< GIS Carga Incio de Procesamiento >>");
        logDatos.add(new LogDatos("GIS Carga Incio de Procesamiento", TipoLog.INFO));
        serviciosNoEncontrados = new ArrayList<>();
        processorUtils.copyFile(fileName,in,destination);
        destination=destination+fileName;
        GisCarga gisCarga = saveGisCarga(fechaProgrmacion,fechaVigencia,descripcion,tipoDia);
        try {
            readExcelAndSaveData(destination,gisCarga,tipoDia);
            log.info("<<GIS Carga Fin de Procesamiento>>");
            logDatos.add(new LogDatos("GIS Carga Fin de Procesamiento", TipoLog.INFO));
        } catch (IOException e) {
            log.error("Error al leer el archivo");
            log.equals(e.getMessage());
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
            exitoso =false;
        }
        return logDatos;

    }

    private void printServiciosNoEncontrados() {
        for (String servicio:serviciosNoEncontrados
             ) {
            System.out.println(servicio);

        }


    }

    public void roollback(){
        exitoso =false;
    }

    public GisCarga saveGisCarga(Date fechaProgrmacion, Date fechaVigencia,String descripcion,String tipoDia){
       TipoDia dia= tipoDiaService.getTipoDia(tipoDia);
       GisCarga gisCarga = new GisCarga(new Date(),fechaProgrmacion,fechaVigencia,descripcion,dia);
       gisCargaService.addGisCarga(gisCarga);
       log.info("GIS Carga para día: "+tipoDia + " Descripción: "+descripcion);
       log.info("Fecha de Programación: "+fechaProgrmacion);
       return gisCarga;
    }

    public GisServicio findOrSaveServicio(Row row,Nodo nodoInicial,Nodo nodoFinal){
        Integer trayectoId = Integer.parseInt( row.getCell(GisCargaDefinition.TRAYECTO).getStringCellValue());
        int linea = Integer.parseInt( row.getCell(GisCargaDefinition.LINEA).getStringCellValue());
            GisServicio servicio = gisCargaService.getGisServicioByTrayectoLinea(linea,trayectoId,nodoInicial.getNombre());
            if( servicio== null ){
                servicio = new GisServicio(trayectoId,linea,nodoInicial.getNombre(),nodoFinal.getNombre());
                try{
                    gisCargaService.addGisServicio(servicio);
                }catch (Exception e){
                    log.error("Error en la inserción de base de datos del servicio");
                    log.error(e.getMessage());
                    logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
                    exitoso =false;
                }

            }

        return servicio;

    }


    public void readExcelAndSaveData(String destination,GisCarga gisCarga, String tipoDiaD)throws IOException{
        try {
            log.info("Inicio de recorrido de archivo");
            FileInputStream fileInputStream = new FileInputStream(destination);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = worksheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                if( row.getCell(0) != null ){
                    Nodo nodoInicial = findOrSaveNodo(row, GisCargaDefinition.NODOINICIO);
                    Nodo nodoFinal = findOrSaveNodo(row, GisCargaDefinition.NODOFINAL);
                    if(nodoInicial!=null){
                        if(nodoFinal!=null){
                            GisServicio servicio = findOrSaveServicio(row,nodoInicial,nodoFinal);
                            if( servicio!= null ){
                                TipoDiaDetalle tipoDia = findOrSaveTipoDia(row,tipoDiaD);
                                saveArcoTiempo(row,gisCarga,servicio,tipoDia);
                                log.info("Servicio relacionado con Trayecto: "+ servicio.getTrayecto()+" y Linea: "+servicio.getLinea());
                            }
                        }else{
                            log.warn("Nodo Final no encontrado: "+row.getCell(GisCargaDefinition.NODOFINAL).getStringCellValue());
                            log.warn("Servicio no relacionado con Trayecto: "+row.getCell(GisCargaDefinition.TRAYECTO).getStringCellValue());
                            logDatos.add(new LogDatos("Nodo Final no encontrado: "+row.getCell(GisCargaDefinition.NODOFINAL).getStringCellValue()
                                    +" para servicio con Trayecto: "+row.getCell(GisCargaDefinition.TRAYECTO).getStringCellValue() , TipoLog.ERROR));
                            exitoso =false;
                        }
                    }else{
                        log.warn("Nodo Inicio no encontrado: "+row.getCell(GisCargaDefinition.NODOINICIO).getStringCellValue());
                        log.warn("Servicio no relacionado con Trayecto: "+row.getCell(GisCargaDefinition.TRAYECTO).getStringCellValue());
                        logDatos.add(new LogDatos("Nodo Inicio no encontrado: "+row.getCell(GisCargaDefinition.NODOFINAL).getStringCellValue()
                                +" para servicio con Trayecto: "+row.getCell(GisCargaDefinition.TRAYECTO).getStringCellValue(), TipoLog.ERROR));
                        exitoso =false;
                    }


                }else{
                    break;
                }
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
            exitoso =false;
        } catch (IOException e) {
           log.equals(e.getMessage());
            logDatos.add(new LogDatos(e.getMessage(), TipoLog.ERROR));
            exitoso =false;
        }
    }

    private void saveArcoTiempo(Row row,GisCarga gisCarga, GisServicio servicio, TipoDiaDetalle tipoDia) {

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
                gisCarga,servicio,tipoDia
        );

        gisCargaService.addArcoTiempo( arcoTiempo );

    }



    private Nodo findOrSaveNodo(Row row, int nodoinicio) {
         String nodoNombre = row.getCell(nodoinicio).getStringCellValue();
         List<Nodo> nodos = nodoService.getNodo( nodoNombre );
        if( nodos.size() == 0 ){
            return null;
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

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }
}
