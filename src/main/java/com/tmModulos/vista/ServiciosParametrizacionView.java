package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.NodoService;
import com.tmModulos.controlador.servicios.ServicioService;
import com.tmModulos.modelo.entity.tmData.Nodo;
import com.tmModulos.modelo.entity.tmData.Servicio;
import com.tmModulos.modelo.entity.tmData.Tipologia;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="serviciosP")
@SessionScoped
public class ServiciosParametrizacionView {


    @ManagedProperty("#{ServicioService}")
    private ServicioService servicioService;

    private List<Servicio> serviciosRecords;
    private List<Servicio> filteredServiciosRecords;
    private Servicio selectedServicio;

    private List<Tipologia> tipologias;
    private List<String> tipoServicio;

    private String auxTipologia;


    @PostConstruct
    public void init() {
        serviciosRecords= servicioService.getServicioAll();
        tipologias = servicioService.getTipologiaAll();
        tipoServicio= new ArrayList<>();
        tipoServicio.add("1");
        tipoServicio.add("1-1");
    }

    public ServiciosParametrizacionView() {
    }

    public void inicio(){

    }

    public void actualizar(){
        if(auxTipologia!=null){
            Tipologia tipologia = servicioService.getTipologiaByNombre(auxTipologia);
            selectedServicio.setTipologia(tipologia);
        }
        String identificador = selectedServicio.getMacro()+"-"+selectedServicio.getLinea()+"-"+selectedServicio.getSeccion()+"-"+selectedServicio.getPunto();
        selectedServicio.setIdentificador(identificador);
        servicioService.updateServicio(selectedServicio);

    }

    public void habilitarNuevo(){
        selectedServicio= new Servicio();
    }

    public void nuevo(){
        if(auxTipologia!=null){
            Tipologia tipologia = servicioService.getTipologiaByNombre(auxTipologia);
            selectedServicio.setTipologia(tipologia);
        }
        String identificador = selectedServicio.getMacro()+"-"+selectedServicio.getLinea()+"-"+selectedServicio.getSeccion()+"-"+selectedServicio.getPunto();
        selectedServicio.setIdentificador(identificador);
        servicioService.addServicio(selectedServicio);
        serviciosRecords= servicioService.getServicioAll();
    }

    public void cancelar(){

    }

    public ServicioService getServicioService() {
        return servicioService;
    }

    public void setServicioService(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    public List<Servicio> getServiciosRecords() {
        return serviciosRecords;
    }

    public void setServiciosRecords(List<Servicio> serviciosRecords) {
        this.serviciosRecords = serviciosRecords;
    }

    public List<Servicio> getFilteredServiciosRecords() {
        return filteredServiciosRecords;
    }

    public void setFilteredServiciosRecords(List<Servicio> filteredServiciosRecords) {
        this.filteredServiciosRecords = filteredServiciosRecords;
    }

    public Servicio getSelectedServicio() {
        return selectedServicio;
    }

    public void setSelectedServicio(Servicio selectedServicio) {
        this.selectedServicio = selectedServicio;
    }

    public List<Tipologia> getTipologias() {
        return tipologias;
    }

    public void setTipologias(List<Tipologia> tipologias) {
        this.tipologias = tipologias;
    }

    public String getAuxTipologia() {
        return auxTipologia;
    }

    public void setAuxTipologia(String auxTipologia) {
        this.auxTipologia = auxTipologia;
    }

    public List<String> getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(List<String> tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
}
