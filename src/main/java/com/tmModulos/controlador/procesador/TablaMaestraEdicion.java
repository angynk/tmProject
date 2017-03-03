package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.ServicioService;
import com.tmModulos.modelo.entity.tmData.Tipologia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("TablaMaestraEdicion")
public class TablaMaestraEdicion {

    @Autowired
    public ServicioService servicioService;

    public Tipologia obtenerTipologia(String nombre){
        Tipologia tipologiaByNombre = servicioService.getTipologiaByNombre(nombre);
//        if(tipologiaByNombre==null){
//            tipologiaByNombre= servicioService.getTipologiaByNombre("ARTICULADO");
//        }
        return tipologiaByNombre;
    }
}
