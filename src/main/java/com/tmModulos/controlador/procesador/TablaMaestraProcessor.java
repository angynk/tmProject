package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.GisCargaService;
import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import com.tmModulos.controlador.servicios.TablaMaestraService;
import com.tmModulos.modelo.dao.tmData.GisCargaDao;
import com.tmModulos.modelo.entity.tmData.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean calcularTablaMaestra(Date fechaDeProgramacion, String descripcion, String gisCarga, String matrizDistancia) {
        serviciosIncluidos= new HashMap<String,String>();
        GisCarga gis= gisCargaService.getGisCargaById(gisCarga);
//        String tipoDia = gis.
        MatrizDistancia matriz= matrizDistanciaService.getMatrizDistanciaById(matrizDistancia);
        TablaMaestra tablaMaestra = crearTablaMaestra(fechaDeProgramacion,new Date(),descripcion,gis,matriz);
        tablaMaestraService.addCustomer(tablaMaestra);
        //Obtener servicios disponibles
        List<Servicio> serviciosDisponibles = gisCargaService.getServicioAll();

        //List<ArcoTiempo> programacionRutas = gisCargaService.getArcoTiempoByGisCarga(gis);
        for (Servicio servicio: serviciosDisponibles   ) {

            List<ArcoTiempo> arcoTiempoRecords = gisCargaService.getArcoTiempoByGisCargaAndServicio(gis,servicio);
            if(arcoTiempoRecords.size()>0){
                ArcoTiempo arcoTiempoBase = arcoTiempoRecords.get(0);
                TablaMaestraServicios tablaMaestraServicios = new TablaMaestraServicios();


                tablaMaestraServicios.setTipoDia(arcoTiempoBase.getTipoDiaByArco().getTipoDia().getNombre());
                tablaMaestraServicios.setSecuencia(arcoTiempoBase.getSecuencia());
                //Copiar informacion del servicio
                tablaMaestraServicios.setTrayecto(servicio.getTrayecto()+"");
                tablaMaestraServicios.setMacro(servicio.getMacro());
                tablaMaestraServicios.setLinea(servicio.getLinea());
                tablaMaestraServicios.setSeccion(servicio.getSeccion());
                tablaMaestraServicios.setTipoServicio(servicio.getTipoServicio());
                tablaMaestraServicios.setNombreEspecial(servicio.getNombreEspecial());
                tablaMaestraServicios.setNombreGeneral(servicio.getNombreGeneral());
                tablaMaestraServicios.setEstado(servicio.isEstado());
                tablaMaestraServicios.setIdentificador(servicio.getIdentificador());

                //Informacion Nodo Inicio
                tablaMaestraServicios.setCodigoInicio(arcoTiempoBase.getNodoInicial().getCodigo());
                tablaMaestraServicios.setNombreInicio(arcoTiempoBase.getNodoInicial().getNombre());
                tablaMaestraServicios.setZonaTInicio(arcoTiempoBase.getNodoInicial().getZonaId().getNombre());
                tablaMaestraServicios.setZonaPInicio(arcoTiempoBase.getNodoInicial().getZonaId().getNombre());
                tablaMaestraServicios.setIdInicio(calcularId(servicio,arcoTiempoBase.getNodoInicial().getCodigo()));
                //Informacion Nodo Final
                tablaMaestraServicios.setCodigoFin(arcoTiempoBase.getNodoFinal().getCodigo());
                tablaMaestraServicios.setNombreIFin(arcoTiempoBase.getNodoFinal().getNombre());
                tablaMaestraServicios.setZonaTFin(arcoTiempoBase.getNodoFinal().getZonaId().getNombre());
                tablaMaestraServicios.setZonaPFin(arcoTiempoBase.getNodoFinal().getZonaId().getNombre());
                tablaMaestraServicios.setIdFin(calcularId(servicio,arcoTiempoBase.getNodoFinal().getCodigo()));

                //CalcularDistnacia
                tablaMaestraServicios= calcularDistancia(tablaMaestraServicios,arcoTiempoBase.getNodoInicial(),arcoTiempoBase.getNodoFinal(),matriz);
                tablaMaestraServicios.setTablaMeestra(tablaMaestra);
                tablaMaestraServicios.setTipologia(servicio.getTipologia());
                tablaMaestraService.addTServicios(tablaMaestraServicios);

            }else{
                System.out.println("No hay información de carga para el servicio "+servicio.getIdentificador());
            }

        }
        return true;
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
