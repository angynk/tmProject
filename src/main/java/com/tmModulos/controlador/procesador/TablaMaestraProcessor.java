package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.GisCargaService;
import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import com.tmModulos.modelo.dao.tmData.GisCargaDao;
import com.tmModulos.modelo.entity.tmData.GisCarga;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("TablaMaestraProcessor")
public class TablaMaestraProcessor {

    @Autowired
    GisCargaService gisCargaService;

    @Autowired
    MatrizDistanciaService matrizDistanciaService;

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
}
