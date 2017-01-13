package com.journaldev.spring.service;

import com.journaldev.hibernate.data.entity.GisCarga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("BusquedaService")
public class BusquedaService {


    @Autowired
    private GisCargaService gisCargaService;


    public List<GisCarga> busquedaFecha(Date fecha, String tipoFecha){
        List<GisCarga> gisCargaByFecha = gisCargaService.getGisCargaByFecha(tipoFecha, fecha);
        return  gisCargaByFecha;
    }

    public List<GisCarga> busquedaRangos(Date fechaInicio,Date fechaFin,String tipoFecha){
        List<GisCarga> gisCargaByFecha = gisCargaService.getGisCargaBetwenFechas(tipoFecha, fechaInicio,fechaFin);
        return  gisCargaByFecha;
    }

    public GisCargaService getGisCargaService() {
        return gisCargaService;
    }

    public void setGisCargaService(GisCargaService gisCargaService) {
        this.gisCargaService = gisCargaService;
    }
}
