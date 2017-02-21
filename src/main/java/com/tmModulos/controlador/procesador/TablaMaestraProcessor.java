package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.*;
import com.tmModulos.modelo.dao.tmData.GisCargaDao;
import com.tmModulos.modelo.entity.tmData.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.bean.ManagedProperty;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public boolean calcularTablaMaestra(Date fechaDeProgramacion, String descripcion, String gisCarga, String matrizDistancia,Date fechaIntervalos,String tipoDia) {
        serviciosIncluidos= new HashMap<String,String>();
        GisCarga gis= gisCargaService.getGisCargaById(gisCarga);
//        String tipoDia = gis.
        MatrizDistancia matriz= matrizDistanciaService.getMatrizDistanciaById(matrizDistancia);
        TablaMaestra tablaMaestra = crearTablaMaestra(fechaDeProgramacion,new Date(),descripcion,gis,matriz);
        tablaMaestraService.addCustomer(tablaMaestra);

        // Traer servicios disponibles por tipo Dia
        TipoDia dia = tipoDiaService.getTipoDia(tipoDia);
        List<ServicioTipoDia> serviciosTipoDia = horariosProvisionalServicio.getServiciosByTipoDia(dia);

//        //Calcular intervalos
        GisIntervalos gisIntervalos= generarIntervalosDeTiempo(fechaIntervalos,descripcion,tipoDia,tablaMaestra);
        intervalosProcessor.precalcularIntervalosProgramacion();

int i=0;
        for (ServicioTipoDia servicio: serviciosTipoDia   ) {
            System.out.println(i+"  "+servicio.getServicio().getMacro()+" "+servicio.getServicio().getLinea());
            i++;
            //CalcularDistnacia
            Nodo nodo = nodoService.getNodoByCodigo(servicio.getServicio().getPunto());
            GisServicio gisServicio=null;
            if(nodo!=null){
                gisServicio=gisCargaService.getGisServicioByTrayectoLinea(servicio.getServicio().getLineaCompuesta(),servicio.getServicio().getTrayecto(),nodo.getNombre());
            }

            if(gisServicio!=null){
                List<ArcoTiempo> arcoTiempoRecords = gisCargaService.getArcoTiempoByGisCargaAndServicio(gis,gisServicio);
                if(arcoTiempoRecords.size()>0){
                    ArcoTiempo arcoTiempoBase = arcoTiempoRecords.get(0);
                    TablaMaestraServicios tablaMaestraServicios = new TablaMaestraServicios();

                    //Informacion Nodo Inicio
                    Nodo nodoInicio = getNodoInicio(arcoTiempoBase.getServicio().getNodoIncial());
                    if(nodoInicio!=null){
                        tablaMaestraServicios.setCodigoInicio(nodoInicio.getCodigo());
                        tablaMaestraServicios.setNombreInicio(nodoInicio.getNombre());
                        tablaMaestraServicios.setZonaTInicio(nodoInicio.getZonaUsuario().getNombre());
                        tablaMaestraServicios.setZonaPInicio(nodoInicio.getZonaProgramacion().getNombre());
                        tablaMaestraServicios.setIdInicio(calcularId(servicio.getServicio(),nodoInicio.getCodigo()));

                        Nodo nodoFinal = getNodoInicio(arcoTiempoBase.getServicio().getNodoFinal());
                        if(nodoFinal!=null){
                            tablaMaestraServicios.setCodigoFin(nodoFinal.getCodigo());
                            tablaMaestraServicios.setNombreIFin(nodoFinal.getNombre());
                            tablaMaestraServicios.setZonaTFin(nodoFinal.getZonaUsuario().getNombre());
                            tablaMaestraServicios.setZonaPFin(nodoFinal.getZonaProgramacion().getNombre());
                            tablaMaestraServicios.setIdFin(calcularId(servicio.getServicio(),nodoFinal.getCodigo()));

                            //Calcular datos basicos matriz
                            tablaMaestraServicios.setTipoDia(arcoTiempoBase.getTipoDiaByArco().getTipoDia().getNombre());
                            tablaMaestraServicios.setSecuencia(arcoTiempoBase.getSecuencia());

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



                            tablaMaestraServicios= calcularDistancia(tablaMaestraServicios,nodoInicio,nodoFinal,matriz);
                            if(tablaMaestraServicios!=null){
                                tablaMaestraServicios.setTablaMeestra(tablaMaestra);
                                tablaMaestraServicios.setTipologia(servicio.getServicio().getTipologia());

                                //Calcular ciclos
                                CicloServicio cicloServicio = calcularCiclos(tablaMaestraServicios,arcoTiempoRecords);
                                tablaMaestraServicios.setCicloServicio(cicloServicio);

                                tablaMaestraService.addTServicios(tablaMaestraServicios);

                                //Calcular Intervalos de tiempo
                                 List<Intervalos> intervaloses = intervalosProcessor.calcularValorIntervaloPorFranja(tablaMaestraServicios, servicio, gisIntervalos);

                            }

                        }else{
                            System.out.println("nodo no encontrado");
                        }

                    }else{
                        System.out.println("nodo no encontrado");
                    }


                }else{
                    System.out.println("No hay información de carga para el servicio "+servicio.getIdentificador());
                }
            }else{
                System.out.println("Servicio no encontrado en el GIS de carga ");
                System.out.println("No es posible encontrar valores para "+servicio.getServicio().getNombreEspecial()+" "+servicio.getServicio().getMacro()+"-"
                        +servicio.getServicio().getLinea()+"-"+servicio.getServicio().getSeccion()+" nodo: "+servicio.getServicio().getPunto());
            }


        }

        return true;
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
                    System.out.println("Servicio no creado");
                }

            }
            if(distanciaNodosB==null){
                seccionAux = getSeccionAux(seccion);
                servicioDistanciaAux  = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccionAux);
                if(servicioDistanciaAux!=null){
                    distanciaNodosB= matrizDistanciaService.getUltimoDistanciaNodosByServicioAndPunto(servicioDistanciaAux,matrizDistancia);
                    calculoEspecialFin =true;
                }else{
                    System.out.println("Servicio no creado");
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
                    System.out.println("Error en la busqueda de la matriz de distancia");
                    System.out.println("No es posible encontrar valores para "+servicioDistancia.getMacro()+"-"
                    +servicioDistancia.getLinea()+"-"+servicioDistancia.getSeccion()+" nodo: "+nodoFin.getCodigo()+"-"+nodoFin.getNombre());
                    return null;
                }

            }else{
                System.out.println("Error en la busqueda de la matriz de distancia");
                System.out.println("No es posible encontrar valores para "+servicioDistancia.getMacro()+"-"
                        +servicioDistancia.getLinea()+"-"+servicioDistancia.getSeccion()+" nodo: "+nodoIni.getCodigo()+"-"+nodoIni.getNombre());
                return null;
            }

        }else{
            tablaMaestraServicios.setDistancia(-1);
            tablaMaestraServicios.setMatrizNombre("ERROR");
        }



        return tablaMaestraServicios;
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
        return tablaMaestra;
    }
}
