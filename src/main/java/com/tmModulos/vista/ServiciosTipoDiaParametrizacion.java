package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.ServicioService;
import com.tmModulos.modelo.entity.tmData.Servicio;
import com.tmModulos.modelo.entity.tmData.ServicioTipoDia;
import com.tmModulos.modelo.entity.tmData.TipoDia;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="serviciosTipoDiaP")
@SessionScoped
public class ServiciosTipoDiaParametrizacion {

    @ManagedProperty("#{ServicioService}")
    private ServicioService servicioService;

    private List<ServicioTipoDia> serviciosRecords;
    private List<ServicioTipoDia> filteredServiciosRecords;

    private List<Servicio> serviciosPrincipalesRecords;
    private String nombreEspecialServicio;

    private ServicioTipoDia selectedServicio;
    private ServicioTipoDia nuevoServicio;
    private boolean tablaVisible;
    private String tipoDiaSeleccionado;
    private TipoDia tipoDia;


    public void inicio(){


    }

    private boolean ordenNoExiste() {

        int ordenNuevo = selectedServicio.getOrden();
        for(ServicioTipoDia servicioTipoDia: serviciosRecords){
            if(servicioTipoDia.getOrden()==ordenNuevo){
                if(servicioTipoDia.getIdentificador()!=selectedServicio.getIdentificador()){
                    return false;
                }
            }
        }
        return true;
    }

    public void actualizar(){
        if(ordenNoExiste()){
            servicioService.updateServicioTipoDia(selectedServicio);
            addMessage(FacesMessage.SEVERITY_INFO,"Servicio Actualizado", "");
            serviciosRecords = servicioService.getServiciosByTipoDia(tipoDia);
        }else{
            addMessage(FacesMessage.SEVERITY_ERROR,"Error", "Revise el orden de los servicios");
            serviciosRecords = servicioService.getServiciosByTipoDia(tipoDia);
        }
    }

    public void eliminar(){
        servicioService.deleteServicioTipoDia(selectedServicio);
        addMessage(FacesMessage.SEVERITY_INFO,"Servicio Eliminado", "");
        serviciosRecords = servicioService.getServiciosByTipoDia(tipoDia);
    }

    public void cancelar(){

    }

    public void crear(){
        if(nombreEspecialServicio!=null && nuevoServicio!=null){
            if(nuevoServicio.getIdentificador()!=null  ){
                Servicio servicio = servicioService.getServicioByNombreEspecial(nombreEspecialServicio);
                nuevoServicio.setServicio(servicio);
                nuevoServicio.setTipoDia(tipoDia);
                servicioService.addServicio(nuevoServicio);
                addMessage(FacesMessage.SEVERITY_INFO,"Servicio Creado", "");
                serviciosRecords = servicioService.getServiciosByTipoDia(tipoDia);
            }

        }else{
            System.out.println("Error");
        }


    }

    public void buscar(){
        tipoDia = servicioService.getTipoDia(tipoDiaSeleccionado);
        serviciosRecords = servicioService.getServiciosByTipoDia(tipoDia);
        tablaVisible=true;
    }

    public void habilitarNuevo(){
        nuevoServicio= new ServicioTipoDia();
        nuevoServicio.setOrden(serviciosRecords.get(serviciosRecords.size()-1).getOrden()+1);
    }

    public void addMessage(FacesMessage.Severity severity , String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    @PostConstruct
    public void init() {
        serviciosPrincipalesRecords = servicioService.getServicioAll();
    }


    public List<Servicio> getServiciosPrincipalesRecords() {
        return serviciosPrincipalesRecords;
    }

    public void setServiciosPrincipalesRecords(List<Servicio> serviciosPrincipalesRecords) {
        this.serviciosPrincipalesRecords = serviciosPrincipalesRecords;
    }

    public String getNombreEspecialServicio() {
        return nombreEspecialServicio;
    }

    public void setNombreEspecialServicio(String nombreEspecialServicio) {
        this.nombreEspecialServicio = nombreEspecialServicio;
    }

    public String getTipoDiaSeleccionado() {
        return tipoDiaSeleccionado;
    }

    public void setTipoDiaSeleccionado(String tipoDiaSeleccionado) {
        this.tipoDiaSeleccionado = tipoDiaSeleccionado;
    }

    public ServicioService getServicioService() {
        return servicioService;
    }

    public void setServicioService(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    public List<ServicioTipoDia> getServiciosRecords() {
        return serviciosRecords;
    }

    public void setServiciosRecords(List<ServicioTipoDia> serviciosRecords) {
        this.serviciosRecords = serviciosRecords;
    }

    public List<ServicioTipoDia> getFilteredServiciosRecords() {
        return filteredServiciosRecords;
    }

    public void setFilteredServiciosRecords(List<ServicioTipoDia> filteredServiciosRecords) {
        this.filteredServiciosRecords = filteredServiciosRecords;
    }

    public ServicioTipoDia getSelectedServicio() {
        return selectedServicio;
    }

    public void setSelectedServicio(ServicioTipoDia selectedServicio) {
        this.selectedServicio = selectedServicio;
    }

    public ServicioTipoDia getNuevoServicio() {
        return nuevoServicio;
    }

    public void setNuevoServicio(ServicioTipoDia nuevoServicio) {
        this.nuevoServicio = nuevoServicio;
    }

    public boolean isTablaVisible() {
        return tablaVisible;
    }

    public void setTablaVisible(boolean tablaVisible) {
        this.tablaVisible = tablaVisible;
    }
}
