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
    private Servicio nuevoServicio;

    private List<String> tipologias;
    private List<String> tipoServicio;

    private String console;


    @PostConstruct
    public void init() {
        serviciosRecords= servicioService.getServicioAll();
        tipologias = new ArrayList<>();
        tipologias.add("ARTICULADO");
        tipologias.add("BIARTICULADO");
        tipologias.add("MIXTO");
        tipoServicio= new ArrayList<>();
        tipoServicio.add("1");
        tipoServicio.add("1-1");
    }

    public ServiciosParametrizacionView() {
    }

    public void inicio(){

    }

    public void actualizar(){
        if(console==null || console.equals("")){
            console="ARTICULADO";
        }
        Tipologia tipologia = servicioService.getTipologiaByNombre(console);
        selectedServicio.setTipologia(tipologia);
        String identificador = selectedServicio.getMacro()+"-"+selectedServicio.getLinea()+"-"+selectedServicio.getSeccion()+"-"+selectedServicio.getPunto();
        selectedServicio.setIdentificador(identificador);
        servicioService.updateServicio(selectedServicio);

    }

    public void habilitarNuevo(){
        nuevoServicio= new Servicio();
    }

    public void nuevo(){
        if(console==null || console.equals("")){
            console="ARTICULADO";
        }
        Tipologia tipologia = servicioService.getTipologiaByNombre(console);
        nuevoServicio.setTipologia(tipologia);
        String identificador = nuevoServicio.getMacro()+"-"+nuevoServicio.getLinea()+"-"+nuevoServicio.getSeccion()+"-"+nuevoServicio.getPunto();
        nuevoServicio.setIdentificador(identificador);
        servicioService.addServicio(nuevoServicio);
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

    public List<String> getTipologias() {
        return tipologias;
    }

    public void setTipologias(List<String> tipologias) {
        this.tipologias = tipologias;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public List<String> getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(List<String> tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Servicio getNuevoServicio() {
        return nuevoServicio;
    }

    public void setNuevoServicio(Servicio nuevoServicio) {
        this.nuevoServicio = nuevoServicio;
    }
}
