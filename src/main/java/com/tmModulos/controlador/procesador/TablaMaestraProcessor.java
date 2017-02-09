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

        //Calcular intervalos
        GisIntervalos gisIntervalos= generarIntervalosDeTiempo(fechaIntervalos,descripcion,tipoDia,tablaMaestra);
        intervalosProcessor.precalcularIntervalosProgramacion();

        for (ServicioTipoDia servicio: serviciosTipoDia   ) {

            List<ArcoTiempo> arcoTiempoRecords = gisCargaService.getArcoTiempoByGisCargaAndServicio(gis,servicio.getServicio());
            if(arcoTiempoRecords.size()>0){
                ArcoTiempo arcoTiempoBase = arcoTiempoRecords.get(0);
                TablaMaestraServicios tablaMaestraServicios = new TablaMaestraServicios();

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

                //Informacion Nodo Inicio
                tablaMaestraServicios.setCodigoInicio(arcoTiempoBase.getNodoInicial().getCodigo());
                tablaMaestraServicios.setNombreInicio(arcoTiempoBase.getNodoInicial().getNombre());
                tablaMaestraServicios.setZonaTInicio(arcoTiempoBase.getNodoInicial().getZonaId().getNombre());
                tablaMaestraServicios.setZonaPInicio(arcoTiempoBase.getNodoInicial().getZonaId().getNombre());
                tablaMaestraServicios.setIdInicio(calcularId(servicio.getServicio(),arcoTiempoBase.getNodoInicial().getCodigo()));

                //Informacion Nodo Final
                tablaMaestraServicios.setCodigoFin(arcoTiempoBase.getNodoFinal().getCodigo());
                tablaMaestraServicios.setNombreIFin(arcoTiempoBase.getNodoFinal().getNombre());
                tablaMaestraServicios.setZonaTFin(arcoTiempoBase.getNodoFinal().getZonaId().getNombre());
                tablaMaestraServicios.setZonaPFin(arcoTiempoBase.getNodoFinal().getZonaId().getNombre());
                tablaMaestraServicios.setIdFin(calcularId(servicio.getServicio(),arcoTiempoBase.getNodoFinal().getCodigo()));

                //CalcularDistnacia
                tablaMaestraServicios= calcularDistancia(tablaMaestraServicios,arcoTiempoBase.getNodoInicial(),arcoTiempoBase.getNodoFinal(),matriz);
                tablaMaestraServicios.setTablaMeestra(tablaMaestra);
                tablaMaestraServicios.setTipologia(servicio.getServicio().getTipologia());

                //Calcular ciclos
                CicloServicio cicloServicio = calcularCiclos(tablaMaestraServicios,arcoTiempoRecords);
                tablaMaestraServicios.setCicloServicio(cicloServicio);

                tablaMaestraService.addTServicios(tablaMaestraServicios);

                //Calcular Intervalos de tiempo
                intervalosProcessor.calcularValorIntervaloPorFranja(tablaMaestraServicios,servicio,gisIntervalos);




            }else{
                System.out.println("No hay información de carga para el servicio "+servicio.getIdentificador());
            }

        }

        return true;
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


        ServicioDistancia servicioDistancia = matrizDistanciaService.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccion);
        if (servicioDistancia!=null){
            DistanciaNodos distanciaNodosA= matrizDistanciaService.getDistanciaNodosByServicioAndPunto(servicioDistancia,nodoFin,matrizDistancia);
            DistanciaNodos distanciaNodosB= matrizDistanciaService.getDistanciaNodosByServicioAndPunto(servicioDistancia,nodoIni,matrizDistancia);

            if( distanciaNodosA!=null && distanciaNodosB!=null){
                tablaMaestraServicios.setDistancia(distanciaNodosA.getDistancia()-distanciaNodosB.getDistancia());
            }else{
                tablaMaestraServicios.setDistancia(-1);
            }
            tablaMaestraServicios.setMatrizNombre(servicioDistancia.getRuta());
        }else{
            tablaMaestraServicios.setDistancia(-1);
            tablaMaestraServicios.setMatrizNombre("ERROR");
        }



        return tablaMaestraServicios;
    }

    private String calcularId(Servicio servicio, Integer codigo) {
        return servicio.getMacro()+"-"+servicio.getLinea()+"-"+servicio.getSeccion()+"-"+codigo;
    }

    private TablaMaestra crearTablaMaestra(Date fechaDeProgramacion, Date fechaCreacion, String descripcion, GisCarga gisCarga, MatrizDistancia matrizDistancia) {
       TablaMaestra tablaMaestra = new TablaMaestra(fechaCreacion,fechaDeProgramacion,descripcion,matrizDistancia,gisCarga);
        return tablaMaestra;
    }
}
