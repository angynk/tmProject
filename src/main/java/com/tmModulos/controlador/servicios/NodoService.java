package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.NodoDao;
import com.tmModulos.modelo.dao.tmData.ZonaDao;
import com.tmModulos.modelo.entity.tmData.Nodo;
import com.tmModulos.modelo.entity.tmData.Zona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("NodoService")
@Transactional(readOnly = true)
public class NodoService {

    @Autowired
    NodoDao nodoDao;

    @Autowired
    ZonaDao zonaDao;

    @Transactional(readOnly = false)
    public void addNodo(Nodo nodo) { nodoDao.addNodo(nodo); }

    @Transactional(readOnly = false)
    public void deleteNodo(Nodo nodo) { nodoDao.deleteNodo(nodo);}

    @Transactional(readOnly = false)
    public void updateNodo(Nodo nodo) { nodoDao.updateNodo(nodo);}

    @Transactional(readOnly = true)
    public List<Nodo> getNodosAll() { return  nodoDao.getNodosAll(); }

    public List<Nodo> getNodo(String nombre){ return nodoDao.getNodo( nombre );}

    public Nodo getNodoByCodigo(int codigo){ return nodoDao.getNodoByCodigo(codigo);}

    @Transactional(readOnly = false)
    public void addZona(Zona zona) {
         zonaDao.addZona(zona);
    }

    @Transactional(readOnly = false)
    public void deleteZona(Zona zona) {
        zonaDao.deleteZona(zona);
    }

    @Transactional(readOnly = false)
    public void updateZona(Zona zona) {
        zonaDao.updateZona(zona);
    }


    public List<Zona> getZonaAll() {
        return zonaDao.getZonaAll();
    }

    public List<Zona> getZonaByTipoZona(String tipoZona) {
        return zonaDao.getZonaByTipoZona(tipoZona);
    }

    public Zona getZonaByName(String nombre,String tipozona){
            return  zonaDao.getNombreByNombre(nombre,tipozona);
    }

}
