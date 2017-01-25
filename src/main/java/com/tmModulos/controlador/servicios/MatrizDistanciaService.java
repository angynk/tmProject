package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.DistanciaNodosDao;
import com.tmModulos.modelo.dao.tmData.MatrizDistanciaDao;
import com.tmModulos.modelo.dao.tmData.ServicioDao;
import com.tmModulos.modelo.entity.tmData.DistanciaNodos;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import com.tmModulos.modelo.entity.tmData.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("MatrizDistanciaService")
@Transactional(readOnly = true)
public class MatrizDistanciaService {

    @Autowired
    MatrizDistanciaDao matrizDistanciaDao;

    @Autowired
    ServicioDao servicioDao;

    @Autowired
    DistanciaNodosDao distanciaNodosDao;

    @Transactional(readOnly = false)
    public void addMatrizDistancia(MatrizDistancia matrizDistancia) {
        matrizDistanciaDao.addMatrizDistancia( matrizDistancia );

    }

    public void deleteMatrizDistancia(MatrizDistancia matrizDistancia) {
        matrizDistanciaDao.deleteMatrizDistancia(matrizDistancia);
    }

    @Transactional(readOnly = false)
    public void updateMatrizDistancia(MatrizDistancia matrizDistancia) {
        matrizDistanciaDao.updateMatrizDistancia(matrizDistancia);
    }


    public List<MatrizDistancia> getMatrizDistanciaAll() {
        return matrizDistanciaDao.getMatrizDistanciaAll();
    }

    public List<MatrizDistancia> getMatrizDistanciaByFecha(String tipoFecha,Date fecha){
        return matrizDistanciaDao.getMatrizDistanciaByFecha(tipoFecha,fecha);
    }

    public List<MatrizDistancia> getMatrizDistanciaBetwenFechas(String tipoFecha,Date fechaIni,Date fechaFin){
        return matrizDistanciaDao.getMatrizDistanciaBetwenFechas(tipoFecha,fechaIni,fechaFin);
    }
    @Transactional(readOnly = false)
    public void addServicio(Servicio servicio) {
        servicioDao.addServicio( servicio );

    }

    public void deleteServicio(Servicio servicio) {
        servicioDao.deleteServicio( servicio );
    }

    @Transactional(readOnly = false)
    public void updateServicio(Servicio servicio) {
        servicioDao.updateServicio( servicio );
    }


    public List<Servicio> getServicioAll() {
        return servicioDao.getServicioAll();
    }

    @Transactional(readOnly = false)
    public void addDistanciaNodos(DistanciaNodos distanciaNodos) {
        distanciaNodosDao.addDistanciaNodos(distanciaNodos);

    }

    public void deleteDistanciaNodos(DistanciaNodos distanciaNodos) {
        distanciaNodosDao.deleteDistanciaNodos(distanciaNodos);
    }

    @Transactional(readOnly = false)
    public void updateDistanciaNodos(DistanciaNodos distanciaNodos) {
        distanciaNodosDao.updateDistanciaNodos(distanciaNodos);
    }


    public List<DistanciaNodos> getDistanciaNodosAll() {
        return distanciaNodosDao.getDistanciaNodosAll();
    }

    public List<DistanciaNodos> getDistanciaNodosByMatriz(MatrizDistancia matrizDistancia){
        return distanciaNodosDao.getDistanciaNodosByMatriz( matrizDistancia );
    }

    public Servicio getServicioBymacroLineaYseccion(int macro,int linea,int seccion, int nodo){
        return servicioDao.getServicioBymacroLineaYseccion( macro,linea,seccion,nodo );
    }

}
