package com.journaldev.prime.faces.beans;

import com.journaldev.hibernate.data.entity.ArcoTiempo;
import com.journaldev.hibernate.data.entity.GisCarga;
import com.journaldev.spring.service.BusquedaService;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="busquedaGis")
@SessionScoped
public class BuscarGisCargaView implements Serializable {

    private String busqueda;
    private String tipoDia;
    private String tipoFecha;
    private Date fechaInicial;
    private Date fechaFinal;
    private boolean visibleRecords;

    private List<GisCarga> gisCargaRecords;
    private GisCarga selectedCarga;
    private List<GisCarga> selectedGisCarga;

    private List<ArcoTiempo> arcoTiempoRecords;
    private List<ArcoTiempo> filteredArcoTiempoRecords;

    @ManagedProperty("#{BusquedaService}")
    private BusquedaService busquedaService;


    public void buscar(){
        boolean resultado = true;
        if (busqueda.equals("1")){
            gisCargaRecords = busquedaService.busquedaFecha(fechaInicial, tipoFecha);
        }else{
            if( fechaInicial!= null && fechaFinal!=null){
                gisCargaRecords = busquedaService.busquedaRangos( fechaInicial,fechaFinal,tipoFecha );
            }
        }

        visibleRecords = resultado;
    }

    public void busquedaCargaGis(){
        System.out.println("visible");
        visibleRecords=true;
        arcoTiempoRecords = busquedaService.busquedaArcos(selectedCarga);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/GISCargaTabla.xhtml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //  externalContext.redirect("foo.xhtml");
    }

    @PostConstruct
    public void init() {
        gisCargaRecords = new ArrayList<>();
        selectedGisCarga = new ArrayList<>();
        visibleRecords = false;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public List<ArcoTiempo> getFilteredArcoTiempoRecords() {
        return filteredArcoTiempoRecords;
    }

    public void setFilteredArcoTiempoRecords(List<ArcoTiempo> filteredArcoTiempoRecords) {
        this.filteredArcoTiempoRecords = filteredArcoTiempoRecords;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getTipoFecha() {
        return tipoFecha;
    }

    public boolean isVisibleRecords() {
        return visibleRecords;
    }

    public void setVisibleRecords(boolean visibleRecords) {
        this.visibleRecords = visibleRecords;
    }

    public void setTipoFecha(String tipoFecha) {
        this.tipoFecha = tipoFecha;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public List<GisCarga> getGisCargaRecords() {
        return gisCargaRecords;
    }

    public void setGisCargaRecords(List<GisCarga> gisCargaRecords) {
        this.gisCargaRecords = gisCargaRecords;
    }

    public GisCarga getSelectedCarga() {
        return selectedCarga;
    }

    public void setSelectedCarga(GisCarga selectedCarga) {
        this.selectedCarga = selectedCarga;
    }

    public List<GisCarga> getSelectedGisCarga() {
        return selectedGisCarga;
    }

    public void setSelectedGisCarga(List<GisCarga> selectedGisCarga) {
        this.selectedGisCarga = selectedGisCarga;
    }

    public List<ArcoTiempo> getArcoTiempoRecords() {
        return arcoTiempoRecords;
    }

    public void setArcoTiempoRecords(List<ArcoTiempo> arcoTiempoRecords) {
        this.arcoTiempoRecords = arcoTiempoRecords;
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public void click() {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        requestContext.update("form:display");
        requestContext.execute("PF('dlg').show()");
    }

    public BusquedaService getBusquedaService() {
        return busquedaService;
    }

    public void setBusquedaService(BusquedaService busquedaService) {
        this.busquedaService = busquedaService;
    }
}
