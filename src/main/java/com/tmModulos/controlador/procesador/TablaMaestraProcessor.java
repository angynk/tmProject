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
        List<ArcoTiempo> programacionRutas = gisCargaService.getArcoTiempoByGisCarga(gis);
        for (ArcoTiempo arcoTiempo: programacionRutas   ) {
            if(!serviciosIncluidos.containsKey(arcoTiempo.getServicio().getIdentificador())){
                TablaMaestraServicios tablaMaestraServicios = new TablaMaestraServicios();
                tablaMaestraServicios.setServicio(arcoTiempo.getServicio());
                tablaMaestraServicios.setNodoIncial(arcoTiempo.getNodoInicial());
                tablaMaestraServicios.setNodoFinal(arcoTiempo.getNodoFinal());
                tablaMaestraServicios.setIdInicio(calcularId(arcoTiempo.getServicio(),arcoTiempo.getNodoInicial().getCodigo()));
                tablaMaestraServicios.setIdFin(calcularId(arcoTiempo.getServicio(),arcoTiempo.getNodoFinal().getCodigo()));
                tablaMaestraServicios.setDistancia(10);
                tablaMaestraServicios.setTablaMeestra(tablaMaestra);
                tablaMaestraServicios.setTipoDia(arcoTiempo.getTipoDiaByArco().getTipoDia().getNombre());
                tablaMaestraService.addTServicios(tablaMaestraServicios);
                serviciosIncluidos.put(arcoTiempo.getServicio().getIdentificador(),"");
            }

        }
        return true;
    }

    private String calcularId(Servicio servicio, Integer codigo) {
        return servicio.getMacro()+"-"+servicio.getLinea()+"-"+servicio.getSeccion()+"-"+codigo;
    }

    private TablaMaestra crearTablaMaestra(Date fechaDeProgramacion, Date fechaCreacion, String descripcion, GisCarga gisCarga, MatrizDistancia matrizDistancia) {
       TablaMaestra tablaMaestra = new TablaMaestra(fechaCreacion,fechaDeProgramacion,descripcion,matrizDistancia,gisCarga);
        return tablaMaestra;
    }
}
