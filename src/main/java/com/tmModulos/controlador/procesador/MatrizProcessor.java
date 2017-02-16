package com.tmModulos.controlador.procesador;


import com.tmModulos.controlador.servicios.DistanciaNodosService;
import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import com.tmModulos.controlador.servicios.NodoService;
import com.tmModulos.controlador.servicios.TablaHorarioService;
import com.tmModulos.controlador.utils.MatrizDistanciaDefinicion;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.dao.saeBogota.GroupedHorario;
import com.tmModulos.modelo.entity.tmData.*;
import com.tmModulos.modelo.entity.saeBogota.*;
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

    private String destination="C:\\temp\\";


    public boolean calcularMatrizDistancia(Date fecha,String numeracion){
        int vigenciad=0;
        List<Vigencias> vigenciasDaoByDate = encontrarVigencias(fecha);
        List<GroupedHorario> horarioByTipoDia = tablaHorarioService.getHorarioByTipoDia(vigenciasDaoByDate.get(0).getTipoDia());
        MatrizDistancia matrizDistancia = guardarMatrizDistancia(fecha,numeracion);
        if(vigenciasDaoByDate.size()>0){
            for (Vigencias vigencia:vigenciasDaoByDate ) {
                vigenciad++;
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

                }
            }
                }
            }
        }else{
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
//        if(seccion == 3){
//            seccion=1;
//        }else if(seccion==4){
//            seccion=2;
//        }

        ServicioDistancia servicioDistancia = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccion);
        if(servicioDistancia==null){
            servicioDistancia = new ServicioDistancia(nombreMatriz,macro,linea,seccion);
            matrizDistanciaService.addServicioDistancia(servicioDistancia);
        }
        return servicioDistancia;
    }


    public boolean processDataFromFile(String fileName, InputStream in, Date fechaProgramacion,String numeracion){
        processorUtils.copyFile(fileName,in,destination);
        destination=destination+fileName;
        MatrizDistancia matrizDistancia = guardarMatrizDistancia(fechaProgramacion,numeracion);
        try {
            readExcelAndSaveData(destination,matrizDistancia);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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



    private MatrizDistancia guardarMatrizDistancia(Date fecha,String numeracion){
        MatrizDistancia matrizDistancia= new MatrizDistancia(new Date(),fecha,numeracion);
        matrizDistanciaService.addMatrizDistancia(matrizDistancia);
        return matrizDistancia;
    }

    private void guardarDistanciaNodos(MatrizDistancia matrizDistancia, Nodo nodo, int distancia,ServicioDistancia servicioDistancia){
        DistanciaNodos distanciaNodos=new DistanciaNodos(distancia,nodo,matrizDistancia,servicioDistancia);
        matrizDistanciaService.addDistanciaNodos(distanciaNodos);
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
