package com.tmModulos.controlador.servicios;


import com.tmModulos.modelo.dao.tmData.*;
import com.tmModulos.modelo.entity.tmData.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service("TablaMaestraService")
@Transactional(readOnly = true)
public class TablaMaestraService {


    @Autowired
    TablaMaestraDao tablaMaestraDao;

    @Autowired
    TablaMaestraServiciosDao tablaMaestraServiciosDao;

    @Autowired
    TipoFranjaDao tipoFranjaDao;

    @Autowired
    CicloServicioDao cicloServicioDao;

    @Autowired
    IntervalosServicioDao intervalosServicioDao;



    @Transactional(readOnly = false)
    public void addCustomer(TablaMaestra tablaMaestra) {
        getTablaMaestraDao().addCustomer(tablaMaestra);
    }

    @Transactional(readOnly = false)
    public void deleteCustomer(TablaMaestra customer) {
        getTablaMaestraDao().deleteCustomer(customer);
    }

    @Transactional(readOnly = false)
    public void updateCustomer(TablaMaestra customer) {
        getTablaMaestraDao().updateCustomer(customer);
    }

    public TablaMaestra getCustomerById(int id) {
        return getTablaMaestraDao().getCustomerById(id);
    }

    @Transactional(readOnly = false)
    public List<TablaMaestra> getCustomers() {
        return getTablaMaestraDao().getCustomers();
    }

    public TablaMaestraDao getTablaMaestraDao() {
        return tablaMaestraDao;
    }

    public void setTablaMaestraDao(TablaMaestraDao tablaMaestraDao) {
        this.tablaMaestraDao = tablaMaestraDao;
    }

    @Transactional(readOnly = false)
    public void addTServicios(TablaMaestraServicios tablaMaestraServicios) {
        tablaMaestraServiciosDao.addTServicios(tablaMaestraServicios);
    }

    public void deleteTServicios(TablaMaestraServicios tablaMaestraServicios) {
        tablaMaestraServiciosDao.deleteTServicios(tablaMaestraServicios);
    }


    public void updateTServicios(TablaMaestraServicios tablaMaestraServicios) {
        tablaMaestraServiciosDao.updateTServicios(tablaMaestraServicios);
    }


    public List<TablaMaestraServicios> getTServicios() {
        return tablaMaestraServiciosDao.getTServicios();
    }


    public List<TablaMaestra> getTablaMaestraByFecha(String tipoFecha, Date fecha){
        return tablaMaestraDao.getTablaMaestraByFecha(tipoFecha,fecha);
    }

    public List<TablaMaestra> getTablaMaestraBetwenFechas(String tipoFecha,Date fechaIni,Date fechaFin){
        return tablaMaestraDao.getTablaMaestraBetwenFechas(tipoFecha,fechaIni,fechaFin);
    }

    public List<TablaMaestraServicios> getServiciosByTabla(TablaMaestra tablaMaestra){
        return tablaMaestraServiciosDao.getServiciosByTabla(tablaMaestra);
    }

    public List<TipoFranja> getTipoFranjaAll() {
        return tipoFranjaDao.getTipoFranjaAll();
    }

    public TipoFranja getTipoFranjaByHorario(String horaIncio,String horaFin){
        return  tipoFranjaDao.getTipoFranjaByHorario(horaIncio,horaFin);
    }

    @Transactional(readOnly = false)
    public void addIntervalosServicio(IntervalosServicio intervalosServicio) {
       intervalosServicioDao.addIntervalosServicio(intervalosServicio);
    }

    @Transactional(readOnly = false)
    public void deleteIntervalosServicios(IntervalosServicio intervalosServicio) {
        intervalosServicioDao.deleteIntervalosServicios(intervalosServicio);
    }

    @Transactional(readOnly = false)
    public void updateIntervalosServicio(IntervalosServicio intervalosServicio) {
        intervalosServicioDao.updateIntervalosServicio(intervalosServicio);
    }


    public List<IntervalosServicio> getIntervalosServicioAll() {
       return intervalosServicioDao.getIntervalosServicioAll();
    }

    public List<IntervalosServicio> getIntervalosServicioByTabla(TablaMaestraServicios tablaMaestra){
        return intervalosServicioDao.getIntervalosServicioByTabla(tablaMaestra);
    }

    @Transactional(readOnly = false)
    public void addCicloServicio(CicloServicio cicloServicio) {
        cicloServicioDao.addCicloServicio(cicloServicio);
    }

    @Transactional(readOnly = false)
    public void deleteCicloServicio(CicloServicio cicloServicio) {
       cicloServicioDao.deleteCicloServicio(cicloServicio);
    }

    @Transactional(readOnly = false)
    public void updateCicloServicio(CicloServicio cicloServicio) {
        cicloServicioDao.updateCicloServicio(cicloServicio);
    }


    public List<CicloServicio> getCicloServiciosAll() {
       return cicloServicioDao.getCicloServiciosAll();
    }

    public List<CicloServicio> getCicloServicioByTabla(TablaMaestraServicios tablaMaestra){
        return getCicloServicioByTabla(tablaMaestra);
    }

}
