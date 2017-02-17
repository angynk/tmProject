package com.tmModulos.controlador.procesador;


import com.tmModulos.controlador.servicios.DistanciaNodosService;
import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import com.tmModulos.controlador.servicios.NodoService;
import com.tmModulos.controlador.servicios.TablaHorarioService;
import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.controlador.utils.MatrizDistanciaDefinicion;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.controlador.utils.TipoLog;
import com.tmModulos.modelo.dao.saeBogota.GroupedHorario;
import com.tmModulos.modelo.entity.tmData.*;
import com.tmModulos.modelo.entity.saeBogota.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service("MatrizProcessor")
public class MatrizProcessor {

    @Autowired
    private MatrizDistanciaService matrizDistanciaService;

    @Autowired
    private DistanciaNodosService distanciaNodosService;

    @Autowired
    private NodoService nodoService;

    @Autowired
    private TablaHorarioService tablaHorarioService;

    @Autowired
    private ProcessorUtils processorUtils;

    private List<LogDatos> logDatos;
    private static Logger log = Logger.getLogger(MatrizProcessor.class);
    private String destination="C:\\temp\\";


    public List<LogDatos> calcularMatrizDistancia(Date fechaHabil,String numeracion,Date fechaFestivos, Date fechaSabado){
        logDatos = new ArrayList<>();
        logDatos.add(new LogDatos("<<Inicio Calculo Matriz Distancias>>", TipoLog.INFO));
        log.info("<<Inicio Calculo Matriz Distancias>>");
        MatrizDistancia matrizDistancia = guardarMatrizDistancia(fechaHabil,numeracion,fechaFestivos,fechaSabado);
         calcularMatrizPorFecha(fechaHabil, matrizDistancia);
         calcularMatrizPorFecha(fechaSabado, matrizDistancia);
         calcularMatrizPorFecha(fechaFestivos, matrizDistancia);
        logDatos.add(new LogDatos("<<Fin Calculo Matriz Distancias>>", TipoLog.INFO));
        log.info("<<Fin Calculo Matriz Distancias>>");
        return logDatos;
    }

    private boolean calcularMatrizPorFecha(Date fecha, MatrizDistancia matrizDistancia) {
        List<Vigencias> vigenciasDaoByDate = encontrarVigencias(fecha);
        if(vigenciasDaoByDate.size()>0){
            List<GroupedHorario> horarioByTipoDia = tablaHorarioService.getHorarioByTipoDia(vigenciasDaoByDate.get(0).getTipoDia());
            for (Vigencias vigencia:vigenciasDaoByDate ) {
                int macro = vigencia.getMacro();
                int linea = vigencia.getLinea();
                int config=0;
                int seccion=0;
                String nombreMatriz= "";
                int distancia=0;
                Nodos nodos=null;
                List<GroupedHorario> horarioActual =macroLineaEnHorario(macro,linea,horarioByTipoDia);
                if(horarioActual.size()>0){

                for( GroupedHorario tablaHorario: horarioActual ){

                List<Lineas> lineasObj = encontrarLineas(macro, linea);
                if(lineasObj.size()>0){
                    config= lineasObj.get(0).getConfig();
                }
                List<NodosSeccion> nodosSeccions = encontrarNodosSeccion(macro, linea, tablaHorario.getSeccion(), config,1);
                if(nodosSeccions.size()>0){
                    for (NodosSeccion nodoSec:nodosSeccions) {
                        seccion= nodoSec.getSeccion();
                        nombreMatriz= encontrarNombreMatriz(macro,linea,config,seccion);
                        distancia=nodoSec.getDistancia();
                        nodos= encontrarNodo(nodoSec.getNodo(),nodoSec.getTipo());
                        Nodo nodo= findOrSaveNodo(nodos.getId(),nodos.getNombre());
                        ServicioDistancia servicioDistancia= crearOBuscarServicioDistancia(macro,linea,seccion,nombreMatriz);
                        guardarDistanciaNodos(matrizDistancia,nodo,distancia,servicioDistancia);
                    }

                }else{
                    log.error("No información de NodosSeccion para el servicio con macro: "+macro+" linea: "+linea+" y seccion: "+tablaHorario.getSeccion());
                    logDatos.add(new LogDatos("No información de NodosSeccion para el servicio con macro: "+macro+" linea: "+linea+" y seccion: "+tablaHorario.getSeccion(), TipoLog.WARN));
                }
            }
                }
            }
        }else{
            log.error("No se econtro información para la fecha: "+fecha.toString());
            logDatos.add(new LogDatos("No se econtro información para la fecha: "+fecha.toString(), TipoLog.ERROR));
            return false;
        }
        return true;
    }

    private List<GroupedHorario> macroLineaEnHorario(int macro, int linea, List<GroupedHorario> horarioByTipoDia) {
        List<GroupedHorario> horarios = new ArrayList<>();
        for(GroupedHorario horario:horarioByTipoDia){
            if(macro == horario.getMacro() && linea == horario.getLinea() ){
                horarios.add(horario);
            }
        }
        return horarios;
    }

    private ServicioDistancia crearOBuscarServicioDistancia(int macro, int linea, int seccion, String nombreMatriz) {
        ServicioDistancia servicioDistancia = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccion);
        if(servicioDistancia==null){
            servicioDistancia = new ServicioDistancia(nombreMatriz,macro,linea,seccion);
            matrizDistanciaService.addServicioDistancia(servicioDistancia);
        }
        return servicioDistancia;
    }


    public List<LogDatos> processDataFromFile(String fileName, InputStream in, Date fechaProgramacion,String numeracion, Date fechaHabil, Date fechaSabado, Date fechaFestivo){
       logDatos = new ArrayList<>();
        logDatos.add(new LogDatos("<<Inicio Calculo Matriz Distancias con Archivo>>", TipoLog.INFO));
        log.info("<<Inicio Calculo Matriz Distancias con Archivo>>");
        processorUtils.copyFile(fileName,in,destination);
        destination=destination+fileName;
        MatrizDistancia matrizDistancia = guardarMatrizDistancia(fechaHabil,numeracion, fechaSabado,fechaFestivo);
        try {
            readExcelAndSaveData(destination,matrizDistancia);

        } catch (IOException e) {
           log.error( e.getMessage());
            logDatos.add(new LogDatos(e.getMessage(),TipoLog.ERROR));
        }
        logDatos.add(new LogDatos("<<Fin Calculo Matriz Distancias con Archivo>>", TipoLog.INFO));
        return logDatos;
    }

    public void readExcelAndSaveData(String destination, MatrizDistancia matrizDistancia)throws IOException {
        try {

            FileInputStream fileInputStream = new FileInputStream(destination);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = worksheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if( row.getCell(0) != null ){
                    int codigoNodo=convertirAInt(row, MatrizDistanciaDefinicion.NODO_CODIGO);


                    Nodo nodo= findOrSaveNodo(codigoNodo
                            ,row.getCell(MatrizDistanciaDefinicion.NOMBRE_NODO).getStringCellValue());

                    ServicioDistancia servicioDistancia= crearOBuscarServicioDistancia(convertirAInt(row,MatrizDistanciaDefinicion.MACRO)
                            , convertirAInt(row,MatrizDistanciaDefinicion.LINEA)
                            , convertirAInt(row,MatrizDistanciaDefinicion.SECCION)
                            ,row.getCell(MatrizDistanciaDefinicion.RUTA).getStringCellValue());
                    guardarDistanciaNodos(matrizDistancia
                            ,nodo,
                            convertirAInt(row,MatrizDistanciaDefinicion.ABSICSA),
                            servicioDistancia);
                }else{
                    break;
                }
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
           log.error( e.getMessage());
            logDatos.add(new LogDatos(e.getMessage(),TipoLog.ERROR));
        } catch (IOException e) {
            log.error( e.getMessage());
            logDatos.add(new LogDatos(e.getMessage(),TipoLog.ERROR));
        }
    }

    private int convertirAInt(Row row,int numberCell) {
        Cell cell = row.getCell(numberCell);
        if(cell.getCellType()==0){
            return (int) cell.getNumericCellValue();
        }

        return Integer.parseInt(cell.getStringCellValue());
    }

    public List<Vigencias> encontrarVigencias(Date fecha){
        List<Vigencias> vigenciasDaoByDate = distanciaNodosService.getVigenciasDaoByDate(fecha);
        return vigenciasDaoByDate;
    }

    public List<Lineas> encontrarLineas(int macro,int linea){
        return distanciaNodosService.getLineasByMacroAndLinea( macro,linea );
    }

    public List<NodosSeccion> encontrarNodosSeccion(int macro,int linea, int seccion,int config,int tipoNodo){
        return distanciaNodosService.getNodosSeccionesByMacroLineaAndConfig(macro,linea, seccion,config,tipoNodo);
    }

    public String encontrarNombreMatriz(int macro,int linea,int config,int seccion){
        List<Secciones> seccionesByMacroLineaAndConfig = distanciaNodosService.getSeccionesByMacroLineaAndConfig(macro, linea, config, seccion);
        if(seccionesByMacroLineaAndConfig.size()>0){
            return seccionesByMacroLineaAndConfig.get(0).getNombre();
        }
        return "Sin nombre";
    }


    private Nodo findOrSaveNodo(int nodoCodigo,String nodoNombre) {
        List<Nodo> nodos = nodoService.getNodo( nodoNombre );
        if( nodos.size() == 0 ){
            Nodo nodo = new Nodo(nodoNombre,nodoCodigo);
            nodoService.addNodo( nodo );
            return nodo;
        }else if (nodos.get(0).getCodigo()==null){
                nodos.get(0).setCodigo(nodoCodigo);
                nodoService.updateNodo(nodos.get(0));
        }
        return  nodos.get(0);
    }



    private MatrizDistancia guardarMatrizDistancia(Date fecha,String numeracion,Date fechaFestivos, Date fechaSabado){
        MatrizDistancia matrizDistancia= new MatrizDistancia(new Date(),fecha,fechaSabado,fechaFestivos,numeracion);
        matrizDistanciaService.addMatrizDistancia(matrizDistancia);
        return matrizDistancia;
    }

    private void guardarDistanciaNodos(MatrizDistancia matrizDistancia, Nodo nodo, int distancia,ServicioDistancia servicioDistancia){
        DistanciaNodos distanciaNodosByServicioAndPunto = matrizDistanciaService.getDistanciaNodosByServicioAndPunto(servicioDistancia, nodo, matrizDistancia);
        if(distanciaNodosByServicioAndPunto==null){
            DistanciaNodos distanciaNodos=new DistanciaNodos(distancia,nodo,matrizDistancia,servicioDistancia);
            matrizDistanciaService.addDistanciaNodos(distanciaNodos);
        }
    }

    public Nodos encontrarNodo(int id,int tipo){
        Nodos nodosByTipoandCode = distanciaNodosService.getNodosByTipoandCode(id, tipo);
        return nodosByTipoandCode;
    }

    public MatrizDistanciaService getMatrizDistanciaService() {
        return matrizDistanciaService;
    }

    public void setMatrizDistanciaService(MatrizDistanciaService matrizDistanciaService) {
        this.matrizDistanciaService = matrizDistanciaService;
    }

    public DistanciaNodosService getDistanciaNodosService() {
        return distanciaNodosService;
    }

    public void setDistanciaNodosService(DistanciaNodosService distanciaNodosService) {
        this.distanciaNodosService = distanciaNodosService;
    }

    public NodoService getNodoService() {
        return nodoService;
    }

    public void setNodoService(NodoService nodoService) {
        this.nodoService = nodoService;
    }

    public ProcessorUtils getProcessorUtils() {
        return processorUtils;
    }

    public void setProcessorUtils(ProcessorUtils processorUtils) {
        this.processorUtils = processorUtils;
    }
}
