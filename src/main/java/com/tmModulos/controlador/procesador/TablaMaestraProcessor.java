package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.*;
import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.controlador.utils.TipoLog;
import com.tmModulos.modelo.dao.tmData.GisCargaDao;
import com.tmModulos.modelo.dao.tmData.IntervalosDao;
import com.tmModulos.modelo.entity.tmData.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.bean.ManagedProperty;
import java.util.*;

@Service("TablaMaestraProcessor")
public class TablaMaestraProcessor {

    @Autowired
    GisCargaService gisCargaService;

    @Autowired
    MatrizDistanciaService matrizDistanciaService;

    @Autowired
    TablaMaestraService tablaMaestraService;

    @Autowired
    private IntervalosProcessor intervalosProcessor;

    @Autowired
    private TipoDiaService tipoDiaService;

    @Autowired
    private NodoService nodoService;



    @Autowired
    private HorariosProvisionalServicio horariosProvisionalServicio;

    private List<LogDatos> logDatos;
    private static Logger log = Logger.getLogger(TablaMaestraServicios.class);


    private Map serviciosIncluidos;


    public List<GisCarga> getGisCargaList() {
        return gisCargaService.getGisCargaAll();
    }

    public List<MatrizDistancia> getMatrizDistanciaAll() {
        return matrizDistanciaService.getMatrizDistanciaAll();
    }

    public GisCargaService getGisCargaService() {
        return gisCargaService;
    }

    public void setGisCargaService(GisCargaService gisCargaService) {
        this.gisCargaService = gisCargaService;
    }

    public MatrizDistanciaService getMatrizDistanciaService() {
        return matrizDistanciaService;
    }

    public void setMatrizDistanciaService(MatrizDistanciaService matrizDistanciaService) {
        this.matrizDistanciaService = matrizDistanciaService;
    }

    public List<LogDatos> calcularTablaMaestra(Date fechaDeProgramacion, String descripcion, String gisCarga, String matrizDistancia,Date fechaIntervalos,String tipoDia) {
        logDatos = new ArrayList<>();
        logDatos.add(new LogDatos("<<Inicio Calculo Tabla Maestra>>", TipoLog.INFO));

        //Encontrar parametros para la generacion de la tabla maestra
        GisCarga gis= gisCargaService.getGisCargaById(gisCarga);
        MatrizDistancia matriz= matrizDistanciaService.getMatrizDistanciaById(matrizDistancia);

        //Crear tabla maestra con los parametros encontrados
        TablaMaestra tablaMaestra = crearTablaMaestra(fechaDeProgramacion,new Date(),descripcion,gis,matriz);
        tablaMaestra.setTipoDia(tipoDia);
        tablaMaestraService.addCustomer(tablaMaestra);

        // Traer servicios disponibles por tipo Dia
        TipoDia dia = tipoDiaService.getTipoDia(tipoDia);
        List<ServicioTipoDia> serviciosTipoDia = horariosProvisionalServicio.getServiciosByTipoDia(dia);

       //Calcular intervalos
        GisIntervalos gisIntervalos= generarIntervalosDeTiempo(fechaIntervalos,descripcion,tipoDia,tablaMaestra);
        intervalosProcessor.precalcularIntervalosProgramacion();

        // Calcular tabla maestra para cada servicio encontrado
        for (ServicioTipoDia servicio: serviciosTipoDia   ) {

            //Encontrar nodo Inicio del servicio por el codigo
            Nodo nodo = nodoService.getNodoByCodigo(servicio.getServicio().getPunto());
            GisServicio gisServicio = obtenerGisServicio(servicio, nodo);

            TablaMaestraServicios tablaMaestraServicios = new TablaMaestraServicios();
            tablaMaestraServicios= copiarInfoBasicaServicio(servicio, tablaMaestraServicios,tablaMaestra);


            if(gisServicio!=null){

                //Obtener información de los arco tiempo del GIS de carga
                List<ArcoTiempo> arcoTiempoRecords = gisCargaService.getArcoTiempoByGisCargaAndServicio(gis,gisServicio);
                if(arcoTiempoRecords.size()>0){

                    ArcoTiempo arcoTiempoBase = arcoTiempoRecords.get(0);


                    //Obtener Nodo Inicio basado en la informacion del GIS de carga
                    Nodo nodoInicio = getNodoInicio(arcoTiempoBase.getServicio().getNodoIncial());
                    if(nodoInicio!=null){
                        tablaMaestraServicios = agregarInfoNodo(servicio, tablaMaestraServicios, nodoInicio);

                        Nodo nodoFinal = getNodoInicio(arcoTiempoBase.getServicio().getNodoFinal());
                        if(nodoFinal!=null){
                           tablaMaestraServicios =agregarInfoNodoFin(servicio,tablaMaestraServicios,nodoFinal);

                            //Calcular datos basicos matriz
                            tablaMaestraServicios.setTipoDia(arcoTiempoBase.getTipoDiaByArco().getTipoDia().getNombre());
                            tablaMaestraServicios.setSecuencia(arcoTiempoBase.getSecuencia());
                            tablaMaestraServicios= calcularDistancia(tablaMaestraServicios,nodoInicio,nodoFinal,matriz);
                                //Calcular ciclos
                                CicloServicio cicloServicio = calcularCiclos(tablaMaestraServicios,arcoTiempoRecords);
                                tablaMaestraServicios.setCicloServicio(cicloServicio);
                                VelocidadProgramada  velocidadProgramada = calcularVelocidadProgramada(cicloServicio,tablaMaestraServicios.getDistancia());
                                tablaMaestraServicios.setVelocidadProgramada(velocidadProgramada);


                                tablaMaestraService.addTServicios(tablaMaestraServicios);

                                //Calcular Intervalos de tiempo
                                 List<Intervalos> intervaloses = intervalosProcessor.calcularValorIntervaloPorFranja(tablaMaestraServicios, servicio, gisIntervalos);


                        }else{
                            log.warn("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoFinal()+" No encontrado");
                            logDatos.add(new LogDatos("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoFinal()+" No encontrado", TipoLog.WARN));
                            tablaMaestraServicios= addCicloServicio(tablaMaestraServicios);
                            tablaMaestraService.addTServicios(tablaMaestraServicios);
                        }

                    }else{
                        log.warn("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoIncial()+" No encontrado");
                        logDatos.add(new LogDatos("Nodo con nombre "+arcoTiempoBase.getServicio().getNodoIncial()+" No encontrado", TipoLog.WARN));
                        tablaMaestraServicios= addCicloServicio(tablaMaestraServicios);
                        tablaMaestraService.addTServicios(tablaMaestraServicios);
                    }


                }else{
                    servicioNoExisteEnGISCarga(servicio);
                }
            }else{
                tablaMaestraServicios= addCicloServicio(tablaMaestraServicios);
                tablaMaestraService.addTServicios(tablaMaestraServicios);
                servicioNoExisteEnGISCarga(servicio);
            }


        }
        logDatos.add(new LogDatos("<<Fin Calculo Tabla Maestra>>", TipoLog.INFO));
        return logDatos;
    }

    private VelocidadProgramada calcularVelocidadProgramada(CicloServicio cicloServicio, Integer distancia) {
        VelocidadProgramada  velocidadProgramada = new VelocidadProgramada();
        Integer distanciaKM;
        if(distancia!=-1){
            distanciaKM = distancia/1000;
            velocidadProgramada.setOptimoAM(calcularVelocidad(distanciaKM,cicloServicio.getOptimoAM()));
            velocidadProgramada.setOptimoPM(calcularVelocidad(distanciaKM,cicloServicio.getOptimoPM()));
            velocidadProgramada.setOptimoValle(calcularVelocidad(distanciaKM,cicloServicio.getOptimoValle()));
            velocidadProgramada.setMaximoAM(calcularVelocidad(distanciaKM,cicloServicio.getMaximoAM()));
            velocidadProgramada.setMaximoPM(calcularVelocidad(distanciaKM,cicloServicio.getMaximoPM()));
            velocidadProgramada.setMaximoValle(calcularVelocidad(distanciaKM,cicloServicio.getMaximoValle()));
            velocidadProgramada.setMinimoAM(calcularVelocidad(distanciaKM,cicloServicio.getMinimoAM()));
            velocidadProgramada.setMinimoPM(calcularVelocidad(distanciaKM,cicloServicio.getMinimoPM()));
            velocidadProgramada.setMinimoValle(calcularVelocidad(distanciaKM,cicloServicio.getMinimoValle()));
        }
        tablaMaestraService.addVelocidadProgramada(velocidadProgramada);
        return velocidadProgramada;
    }

    private double calcularVelocidad(Integer distanciaKM, String optimoAM) {
        if(optimoAM!=null){
            String[] datos = optimoAM.split(":");
            double horas= Integer.parseInt(datos[0]);
            double minutos= Integer.parseInt(datos[1]);
            double segundos= Integer.parseInt(datos[2]);
            horas=horas + (minutos/60) + ( segundos/3600 );

            if(horas!=0){
                return distanciaKM/horas;
            }

        }
        return 0;
    }

    private TablaMaestraServicios addCicloServicio(TablaMaestraServicios tablaMaestraServicios) {
        CicloServicio cicloServicio = new CicloServicio();
        tablaMaestraService.addCicloServicio(cicloServicio);
        tablaMaestraServicios.setCicloServicio(cicloServicio);

        VelocidadProgramada velocidadProgramada = new VelocidadProgramada();
        tablaMaestraService.addVelocidadProgramada(velocidadProgramada);
        tablaMaestraServicios.setVelocidadProgramada(velocidadProgramada);

        return tablaMaestraServicios;
    }

    private TablaMaestraServicios copiarInfoBasicaServicio(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios, TablaMaestra tablaMaestra) {
        //Copiar informacion del servicio
        tablaMaestraServicios.setTrayecto(servicio.getServicio().getTrayecto()+"");
        tablaMaestraServicios.setMacro(servicio.getServicio().getMacro());
        tablaMaestraServicios.setLinea(servicio.getServicio().getLinea());
        tablaMaestraServicios.setSeccion(servicio.getServicio().getSeccion());
        tablaMaestraServicios.setTipoServicio(servicio.getServicio().getTipoServicio());
        tablaMaestraServicios.setNombreEspecial(servicio.getServicio().getNombreEspecial());
        tablaMaestraServicios.setNombreGeneral(servicio.getServicio().getNombreGeneral());
        tablaMaestraServicios.setEstado(servicio.getServicio().isEstado());
        tablaMaestraServicios.setIdentificador(servicio.getServicio().getIdentificador());
        tablaMaestraServicios.setTablaMeestra(tablaMaestra);
        tablaMaestraServicios.setTipologia(servicio.getServicio().getTipologia());
        tablaMaestraServicios.setDistancia(-1);
        tablaMaestraServicios.setSecuencia(0);
        return tablaMaestraServicios;
    }

    private TablaMaestraServicios agregarInfoNodo(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios, Nodo nodoInicio) {
        tablaMaestraServicios.setCodigoInicio(nodoInicio.getCodigo());
        tablaMaestraServicios.setNombreInicio(nodoInicio.getNombre());
        tablaMaestraServicios.setZonaTInicio(nodoInicio.getZonaUsuario().getNombre());
        tablaMaestraServicios.setZonaPInicio(nodoInicio.getZonaProgramacion().getNombre());
        tablaMaestraServicios.setIdInicio(calcularId(servicio.getServicio(),nodoInicio.getCodigo()));
        return  tablaMaestraServicios;
    }

    private TablaMaestraServicios agregarInfoNodoFin(ServicioTipoDia servicio, TablaMaestraServicios tablaMaestraServicios, Nodo nodoFinal) {
        tablaMaestraServicios.setCodigoFin(nodoFinal.getCodigo());
        tablaMaestraServicios.setNombreIFin(nodoFinal.getNombre());
        tablaMaestraServicios.setZonaTFin(nodoFinal.getZonaUsuario().getNombre());
        tablaMaestraServicios.setZonaPFin(nodoFinal.getZonaProgramacion().getNombre());
        tablaMaestraServicios.setIdFin(calcularId(servicio.getServicio(),nodoFinal.getCodigo()));
        return  tablaMaestraServicios;
    }

    private void servicioNoExisteEnGISCarga(ServicioTipoDia servicio) {
        log.warn("Servicio no encontrado en el GIS de carga ");
        log.warn("No es posible encontrar valores para "+servicio.getServicio().getNombreEspecial()+" "+servicio.getServicio().getMacro()+"-"
                +servicio.getServicio().getLinea()+"-"+servicio.getServicio().getSeccion()+" nodo: "+servicio.getServicio().getPunto());
        logDatos.add(new LogDatos("Servicio no encontrado en el GIS de carga ", TipoLog.WARN));
        logDatos.add(new LogDatos("No es posible encontrar valores para "+servicio.getServicio().getNombreEspecial()+" "+servicio.getServicio().getMacro()+"-"
                +servicio.getServicio().getLinea()+"-"+servicio.getServicio().getSeccion()+" nodo: "+servicio.getServicio().getPunto(), TipoLog.WARN));
    }

    // Obtener el servicio asociado en el GIS de Carga a partir de la linea, trayecto y nodo
    private GisServicio obtenerGisServicio(ServicioTipoDia servicio, Nodo nodo) {
        GisServicio gisServicio=null;
        if(nodo!=null){
            gisServicio=gisCargaService.getGisServicioByTrayectoLinea(servicio.getServicio().getLineaCompuesta(),servicio.getServicio().getTrayecto(),nodo.getNombre());
        }else{
            log.error("El nodo "+servicio.getServicio().getPunto()+" No existe para el servicio: "+servicio.getIdentificador());
            logDatos.add(new LogDatos("El nodo "+servicio.getServicio().getPunto()+" No existe para el servicio: "+servicio.getIdentificador(), TipoLog.ERROR));
        }
        return gisServicio;
    }

    private Nodo getNodoInicio(String nodoIncial) {
        List<Nodo> nodo = nodoService.getNodo(nodoIncial);
        if(nodo.size()>0){
            return nodo.get(0);
        }
        return null;
    }

    private GisIntervalos generarIntervalosDeTiempo(Date fechaIntervalos,String descripcion, String tipoDia, TablaMaestra tablaMaestra) {
       return intervalosProcessor.generarIntervalos(fechaIntervalos,descripcion,tipoDia,tablaMaestra);
    }

    //Calcular ciclos de tiempos de recorrido - en base al GIS de carga
    private CicloServicio calcularCiclos(TablaMaestraServicios tablaMaestraServicios, List<ArcoTiempo> arcoTiempoRecords) {
        CicloServicio cicloServicio = new CicloServicio();
        for (ArcoTiempo arcoTiempo: arcoTiempoRecords){
            TipoFranja tipoFranja = tablaMaestraService.getTipoFranjaByHorario(arcoTiempo.getHoraDesde(),arcoTiempo.getHoraHasta());
            if(tipoFranja!=null){
                if(tipoFranja.getNombre().equals("Inicio")){
                    cicloServicio.setMinimoInicio(arcoTiempo.getTiempoMinimo());
                    cicloServicio.setMaximoInicio(arcoTiempo.getTiempoMaximo());
                    cicloServicio.setOptimoInicio(arcoTiempo.getTiempoOptimo());
                }else if(tipoFranja.getNombre().equals("Pico AM")){
                    cicloServicio.setMinimoAM(arcoTiempo.getTiempoMinimo());
                    cicloServicio.setMaximoAM(arcoTiempo.getTiempoMaximo());
                    cicloServicio.setOptimoAM(arcoTiempo.getTiempoOptimo());
                }else if(tipoFranja.getNombre().equals("Pico PM")){
                    cicloServicio.setMinimoPM(arcoTiempo.getTiempoMinimo());
                    cicloServicio.setMaximoPM(arcoTiempo.getTiempoMaximo());
                    cicloServicio.setOptimoPM(arcoTiempo.getTiempoOptimo());
                }else if(tipoFranja.getNombre().equals("Valle")){
                    cicloServicio.setMinimoValle(arcoTiempo.getTiempoMinimo());
                    cicloServicio.setMaximoValle(arcoTiempo.getTiempoMaximo());
                    cicloServicio.setOptimoValle(arcoTiempo.getTiempoOptimo());
                }else {
                    cicloServicio.setMinimoCierre(arcoTiempo.getTiempoMinimo());
                    cicloServicio.setMaximoCierre(arcoTiempo.getTiempoMaximo());
                    cicloServicio.setOptimoCierre(arcoTiempo.getTiempoOptimo());
                }

            }else{
                System.out.println("Tipo de franja no existente");
            }

        }

        tablaMaestraService.addCicloServicio(cicloServicio);
        return cicloServicio;
    }

    private TablaMaestraServicios calcularDistancia(TablaMaestraServicios tablaMaestraServicios, Nodo nodoIni,Nodo nodoFin, MatrizDistancia matrizDistancia) {

        int macro = tablaMaestraServicios.getMacro();
        int linea = tablaMaestraServicios.getLinea();
        int seccion = tablaMaestraServicios.getSeccion();
        int seccionAux = 4;
        boolean calculoEspecialInicio=false;
        boolean calculoEspecialFin=false;

        // Obtener servicio asociado a la matriz de Distancia
        ServicioDistancia servicioDistancia = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccion);
        ServicioDistancia servicioDistanciaAux=null ;

        if (servicioDistancia!=null){
            DistanciaNodos distanciaNodosA= matrizDistanciaService.getDistanciaNodosByServicioAndPunto(servicioDistancia,nodoIni,matrizDistancia);
            DistanciaNodos distanciaNodosB= matrizDistanciaService.getDistanciaNodosByServicioAndPunto(servicioDistancia,nodoFin,matrizDistancia);

            if( distanciaNodosA==null){
                seccionAux = getSeccionAux(seccion);
                servicioDistanciaAux = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccionAux);
                if(servicioDistanciaAux!=null){
                    distanciaNodosA= matrizDistanciaService.getUltimoDistanciaNodosByServicioAndPunto(servicioDistanciaAux,matrizDistancia);
                    calculoEspecialInicio =true;
                }else{
                    serviciosNoEncontradosMatriz(macro, linea, seccion);
                }

            }
            if(distanciaNodosB==null){
                seccionAux = getSeccionAux(seccion);
                servicioDistanciaAux  = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccionAux);
                if(servicioDistanciaAux!=null){
                    distanciaNodosB= matrizDistanciaService.getUltimoDistanciaNodosByServicioAndPunto(servicioDistanciaAux,matrizDistancia);
                    calculoEspecialFin =true;
                }else{
                    serviciosNoEncontradosMatriz(macro, linea, seccion);
                }

            }
            if( distanciaNodosA!=null ){
                if(distanciaNodosB!=null){
                    int disFinal=distanciaNodosB.getDistancia();
                    if(calculoEspecialInicio){
                        disFinal= calcularDistanciaEspecial(seccion,distanciaNodosA,distanciaNodosB,servicioDistancia,servicioDistanciaAux,matrizDistancia);
                    }else if(calculoEspecialFin){
                        disFinal= calcularDistanciaEspecial(seccion,distanciaNodosA,distanciaNodosB,servicioDistancia,servicioDistanciaAux,matrizDistancia);
                    }
                    tablaMaestraServicios.setDistancia(disFinal-distanciaNodosA.getDistancia());
                    tablaMaestraServicios.setMatrizNombre(servicioDistancia.getRuta());
                }else{
                    noHayValoresenMatriz(nodoFin, servicioDistancia);
                    tablaMaestraServicios.setDistancia(-1);
                    return tablaMaestraServicios;
                }

            }else{
                noHayValoresenMatriz(nodoIni, servicioDistancia);
                tablaMaestraServicios.setDistancia(-1);
                return tablaMaestraServicios;
            }

        }else{
            serviciosNoEncontradosMatriz(macro, linea, seccion);
            tablaMaestraServicios.setDistancia(-1);
            return tablaMaestraServicios;
        }

        return tablaMaestraServicios;
    }

    private void noHayValoresenMatriz(Nodo nodoFin, ServicioDistancia servicioDistancia) {
        log.warn("Error en la busqueda de la matriz de distancia");
        log.warn("No es posible encontrar valores para "+servicioDistancia.getMacro()+"-"
        +servicioDistancia.getLinea()+"-"+servicioDistancia.getSeccion()+" nodo: "+nodoFin.getCodigo()+"-"+nodoFin.getNombre());
        logDatos.add(new LogDatos("No es posible encontrar valores para "+servicioDistancia.getMacro()+"-"
                +servicioDistancia.getLinea()+"-"+servicioDistancia.getSeccion()+" nodo: "+nodoFin.getCodigo()+"-"+nodoFin.getNombre(), TipoLog.WARN));
    }

    private void serviciosNoEncontradosMatriz(int macro, int linea, int seccion) {
        log.warn("Servicio no encontrado en la Matriz de Distancias con Macro "+macro+" Linea "+linea+" Seccion "+seccion);
        logDatos.add(new LogDatos("Servicio no encontrado en en la Matriz de Distancias con Macro "+macro+" Linea "+linea+" Seccion "+seccion, TipoLog.WARN));
    }

    private int getSeccionAux(int seccion) {
        int seccionAux;
        if(seccion==1){
            seccionAux=3;
        }else{
            seccionAux=4;
        }
        return seccionAux;
    }

    private int calcularDistanciaEspecial(int seccion, DistanciaNodos distanciaNodosA, DistanciaNodos distanciaNodosB,ServicioDistancia servicioDistancia, ServicioDistancia servicioDistanciaAux,MatrizDistancia matrizDistancia ) {
        DistanciaNodos ultimoNodoSeccion = matrizDistanciaService.getUltimoDistanciaNodosByServicioAndPunto(servicioDistancia,matrizDistancia);
     return   distanciaNodosB.getDistancia()+ultimoNodoSeccion.getDistancia();
    }

    private String calcularId(Servicio servicio, Integer codigo) {
        return servicio.getMacro()+"-"+servicio.getLinea()+"-"+servicio.getSeccion()+"-"+codigo;
    }

    private TablaMaestra crearTablaMaestra(Date fechaDeProgramacion, Date fechaCreacion, String descripcion, GisCarga gisCarga, MatrizDistancia matrizDistancia) {
       TablaMaestra tablaMaestra = new TablaMaestra(fechaCreacion,fechaDeProgramacion,descripcion,matrizDistancia,gisCarga);
        tablaMaestra.setEsDefinitiva(true);
        return tablaMaestra;
    }

    public List<LogDatos> getLogDatos() {
        return logDatos;
    }

    public void setLogDatos(List<LogDatos> logDatos) {
        this.logDatos = logDatos;
    }

    public List<LogDatos> copiarUltimaTablaMaestra(Date fechaDeProgramacion, String descripcion,String tipoDia) {
        TablaMaestra ultimaTablaMaestra = obtenerUltimaTablaMaestra(tipoDia);
        logDatos = new ArrayList<>();
        logDatos.add(new LogDatos("<<Inicio Calculo Tabla Maestra>>", TipoLog.INFO));
        if(ultimaTablaMaestra!=null){
            copiarTabla(fechaDeProgramacion,descripcion,tipoDia,ultimaTablaMaestra);
        }else{
            logDatos.add(new LogDatos("<<No existe una tabla maestra previa>>", TipoLog.ERROR));
        }
        logDatos.add(new LogDatos("<<Fin Calculo Tabla Maestra>>", TipoLog.INFO));
        return logDatos;
    }

    private void copiarTabla(Date fechaDeProgramacion, String descripcion, String tipoDia, TablaMaestra ultimaTablaMaestra) {
        TablaMaestra nuevaTablaMaestra = new TablaMaestra();
        nuevaTablaMaestra.setFechaCreacion(new Date());
        nuevaTablaMaestra.setFechaVigencia(fechaDeProgramacion);
        nuevaTablaMaestra.setEsDefinitiva(false);
        nuevaTablaMaestra.setDescripcion(descripcion);
        nuevaTablaMaestra.setTipoDia(tipoDia);
        nuevaTablaMaestra.setGisCarga(ultimaTablaMaestra.getGisCarga());
        nuevaTablaMaestra.setMatrizDistancia(ultimaTablaMaestra.getMatrizDistancia());

        tablaMaestraService.addCustomer(nuevaTablaMaestra);

        List<TablaMaestraServicios> serviciosByTabla = tablaMaestraService.getServiciosByTabla(ultimaTablaMaestra);
        for(TablaMaestraServicios tablaMaestraServicios: serviciosByTabla){
            copiarInformacionTablaMaestraServicios(nuevaTablaMaestra, tablaMaestraServicios);

        }

    }

    private void copiarInformacionTablaMaestraServicios(TablaMaestra nuevaTablaMaestra, TablaMaestraServicios tablaMaestraServicios) {
        TablaMaestraServicios nuevaTablaMaestraServicios = new TablaMaestraServicios();
        nuevaTablaMaestraServicios.setDistancia(tablaMaestraServicios.getDistancia());
        nuevaTablaMaestraServicios.setSecuencia(tablaMaestraServicios.getSecuencia());
        nuevaTablaMaestraServicios.setIdentificador(tablaMaestraServicios.getIdentificador());
        nuevaTablaMaestraServicios.setIdInicio(tablaMaestraServicios.getIdInicio());
        nuevaTablaMaestraServicios.setIdFin(tablaMaestraServicios.getIdFin());
        nuevaTablaMaestraServicios.setTipoDia(tablaMaestraServicios.getTipoDia());
        nuevaTablaMaestraServicios.setMatrizNombre(tablaMaestraServicios.getMatrizNombre());
        nuevaTablaMaestraServicios.setTrayecto(tablaMaestraServicios.getTrayecto());
        nuevaTablaMaestraServicios.setMacro(tablaMaestraServicios.getMacro());
        nuevaTablaMaestraServicios.setLinea(tablaMaestraServicios.getLinea());
        nuevaTablaMaestraServicios.setSeccion(tablaMaestraServicios.getSeccion());
        nuevaTablaMaestraServicios.setTipoServicio(tablaMaestraServicios.getTipoServicio());
        nuevaTablaMaestraServicios.setNombreGeneral(tablaMaestraServicios.getNombreGeneral());
        nuevaTablaMaestraServicios.setNombreEspecial(tablaMaestraServicios.getNombreEspecial());
        nuevaTablaMaestraServicios.setEstado(tablaMaestraServicios.isEstado());
        nuevaTablaMaestraServicios.setCodigoInicio(tablaMaestraServicios.getCodigoInicio());
        nuevaTablaMaestraServicios.setCodigoFin(tablaMaestraServicios.getCodigoFin());
        nuevaTablaMaestraServicios.setZonaTInicio(tablaMaestraServicios.getZonaTInicio());
        nuevaTablaMaestraServicios.setZonaTFin(tablaMaestraServicios.getZonaTFin());
        nuevaTablaMaestraServicios.setZonaPInicio(tablaMaestraServicios.getZonaPInicio());
        nuevaTablaMaestraServicios.setZonaPFin(tablaMaestraServicios.getZonaPFin());
        nuevaTablaMaestraServicios.setNombreInicio(tablaMaestraServicios.getNombreInicio());
        nuevaTablaMaestraServicios.setNombreIFin(tablaMaestraServicios.getNombreIFin());
        nuevaTablaMaestraServicios.setTipologia(tablaMaestraServicios.getTipologia());

        CicloServicio cicloServicio = new CicloServicio();
        cicloServicio.setOptimoInicio(tablaMaestraServicios.getCicloServicio().getOptimoInicio());
        cicloServicio.setOptimoAM(tablaMaestraServicios.getCicloServicio().getOptimoAM());
        cicloServicio.setOptimoValle(tablaMaestraServicios.getCicloServicio().getOptimoValle());
        cicloServicio.setOptimoPM(tablaMaestraServicios.getCicloServicio().getOptimoPM());
        cicloServicio.setOptimoCierre(tablaMaestraServicios.getCicloServicio().getOptimoCierre());
        cicloServicio.setMaximoInicio(tablaMaestraServicios.getCicloServicio().getMaximoInicio());
        cicloServicio.setMaximoAM(tablaMaestraServicios.getCicloServicio().getMaximoAM());
        cicloServicio.setMaximoValle(tablaMaestraServicios.getCicloServicio().getMaximoValle());
        cicloServicio.setMaximoPM(tablaMaestraServicios.getCicloServicio().getMaximoPM());
        cicloServicio.setMaximoCierre(tablaMaestraServicios.getCicloServicio().getMaximoCierre());
        cicloServicio.setMinimoInicio(tablaMaestraServicios.getCicloServicio().getMinimoInicio());
        cicloServicio.setMinimoAM(tablaMaestraServicios.getCicloServicio().getMinimoAM());
        cicloServicio.setMinimoValle(tablaMaestraServicios.getCicloServicio().getMinimoValle());
        cicloServicio.setMinimoPM(tablaMaestraServicios.getCicloServicio().getMinimoPM());
        cicloServicio.setMinimoCierre(tablaMaestraServicios.getCicloServicio().getMinimoCierre());
        tablaMaestraService.addCicloServicio(cicloServicio);
        nuevaTablaMaestraServicios.setCicloServicio(cicloServicio);

        VelocidadProgramada velocidadProgramada = new VelocidadProgramada();
        velocidadProgramada.setMaximoAM(tablaMaestraServicios.getVelocidadProgramada().getMaximoAM());
        velocidadProgramada.setMaximoValle(tablaMaestraServicios.getVelocidadProgramada().getMaximoValle());
        velocidadProgramada.setMaximoPM(tablaMaestraServicios.getVelocidadProgramada().getMaximoPM());
        velocidadProgramada.setMinimoAM(tablaMaestraServicios.getVelocidadProgramada().getMinimoAM());
        velocidadProgramada.setMinimoPM(tablaMaestraServicios.getVelocidadProgramada().getMinimoPM());
        velocidadProgramada.setMinimoValle(tablaMaestraServicios.getVelocidadProgramada().getMinimoValle());
        velocidadProgramada.setOptimoAM(tablaMaestraServicios.getVelocidadProgramada().getOptimoAM());
        velocidadProgramada.setOptimoValle(tablaMaestraServicios.getVelocidadProgramada().getOptimoValle());
        velocidadProgramada.setOptimoPM(tablaMaestraServicios.getVelocidadProgramada().getOptimoPM());
        tablaMaestraService.addVelocidadProgramada(velocidadProgramada);
        nuevaTablaMaestraServicios.setVelocidadProgramada(velocidadProgramada);

        nuevaTablaMaestraServicios.setTablaMeestra(nuevaTablaMaestra);
        tablaMaestraService.addTServicios(nuevaTablaMaestraServicios);

        List<Intervalos> intervalos = tablaMaestraServicios.getServiciosRecords();
        for( Intervalos intervalo:intervalos ){
            Intervalos nuevoInter = new Intervalos(intervalo.getTipoCalculo(),intervalo.getValorInicio(),intervalo.getValorAM(),
                    intervalo.getValorValle(),intervalo.getValorPM(),intervalo.getValorCierre(),intervalo.getBusesInicio(),
                    intervalo.getBusesAM(),intervalo.getBusesValle(),intervalo.getBusesPM(),intervalo.getBusesCierre(),
                    intervalo.getIdServicio(), nuevaTablaMaestraServicios);
            tablaMaestraService.addIntervalos(nuevoInter);

        }


    }

    private TablaMaestra obtenerUltimaTablaMaestra(String tipoDia) {
        return tablaMaestraService.getUltimaTablaMaestraByaTipoDia(tipoDia);
    }
}
