package com.journaldev.spring.service;

import com.journaldev.hibernate.data.dao.saeBogota.*;
import com.journaldev.hibernate.data.entity.saeBogota.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("DistanciaNodosService")
@Transactional(readOnly = true)
public class DistanciaNodosService {


    @Autowired
    NodosDao nodosDao;

    @Autowired
    VigenciasDao vigenciasDao;

    @Autowired
    LineasDao lineasDao;

    @Autowired
    SeccionesDao seccionesDao;

    @Autowired
    NodosSeccionDao nodosSeccionDao;

    public List<Nodos> getArcoTiempoAll() {
        return nodosDao.getArcoTiempoAll();
    }

    public List<Vigencias> getVigenciasDaoByDate(Date date) {
        return vigenciasDao.getVigenciasDaoByDate( date );
    }

    public List<Lineas> getLineasByMacroAndLinea(int macro, int linea) {
        return lineasDao.getLineasByMacroAndLinea(macro,linea);
    }

    public List<Secciones> getSeccionesByMacroLineaAndConfig(int macro, int linea, int config,int seccion) {
        return  seccionesDao.getSeccionesByMacroLineaAndConfig( macro,linea,config,seccion );
    }

    public List<NodosSeccion> getNodosSeccionesByMacroLineaAndConfig(int macro, int linea, int config,int tipoNodo) {
        return nodosSeccionDao.getNodosSeccionesByMacroLineaAndConfig(macro,linea,config,tipoNodo);
    }

    public Nodos getNodosByTipoandCode(int id,int tipo) {
        return nodosDao.getNodosByTipoandCode(id,tipo);
    }
}
