package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import com.tmModulos.controlador.servicios.NodoService;
import com.tmModulos.modelo.entity.tmData.Nodo;
import com.tmModulos.modelo.entity.tmData.Zona;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="nodosP")
@SessionScoped
public class NodosParametrizacionView implements Serializable {


    @ManagedProperty("#{NodoService}")
    private NodoService nodoService;

    private List<Nodo> nodosRecords;
    private List<Nodo> filteredNodosRecords;

    private List<Zona> zonasProgramadasRecords;
    private String auxNombreZonaP;


    private String auxNombreZonaU;
    private List<Zona> zonasUsuariosRecords;


    public NodosParametrizacionView() {
    }

    @PostConstruct
    public void init() {
       nodosRecords= nodoService.getNodosAll();
        zonasProgramadasRecords = nodoService.getZonaByTipoZona("P");
        zonasUsuariosRecords = nodoService.getZonaByTipoZona("U");
    }

    public void inicio(){
    }

    public void onRowEdit(RowEditEvent event) {
        Nodo nodo = (Nodo) event.getObject();
        Zona zonaP = nodoService.getZonaByName(auxNombreZonaP,"P");
        nodo.setZonaProgramacion(zonaP);
        Zona zonaU = nodoService.getZonaByName(auxNombreZonaU,"U");
        nodo.setZonaUsuario(zonaU);
        nodoService.updateNodo(nodo);
        FacesMessage msg = new FacesMessage("Zona Actualizada", (nodo).getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void onRowCancel(RowEditEvent event) {

    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if(newValue != null && !newValue.equals(oldValue)) {
        }
    }

    public List<Nodo> getFilteredNodosRecords() {
        return filteredNodosRecords;
    }

    public void setFilteredNodosRecords(List<Nodo> filteredNodosRecords) {
        this.filteredNodosRecords = filteredNodosRecords;
    }

    public List<Nodo> getNodosRecords() {
        return nodosRecords;
    }

    public void setNodosRecords(List<Nodo> nodosRecords) {
        this.nodosRecords = nodosRecords;
    }

    public NodoService getNodoService() {
        return nodoService;
    }

    public void setNodoService(NodoService nodoService) {
        this.nodoService = nodoService;
    }

    public List<Zona> getZonasProgramadasRecords() {
        return zonasProgramadasRecords;
    }

    public void setZonasProgramadasRecords(List<Zona> zonasProgramadasRecords) {
        this.zonasProgramadasRecords = zonasProgramadasRecords;
    }

    public String getAuxNombreZonaP() {
        return auxNombreZonaP;
    }

    public void setAuxNombreZonaP(String auxNombreZonaP) {
        this.auxNombreZonaP = auxNombreZonaP;
    }

    public String getAuxNombreZonaU() {
        return auxNombreZonaU;
    }

    public void setAuxNombreZonaU(String auxNombreZonaU) {
        this.auxNombreZonaU = auxNombreZonaU;
    }

    public List<Zona> getZonasUsuariosRecords() {
        return zonasUsuariosRecords;
    }

    public void setZonasUsuariosRecords(List<Zona> zonasUsuariosRecords) {
        this.zonasUsuariosRecords = zonasUsuariosRecords;
    }
}
