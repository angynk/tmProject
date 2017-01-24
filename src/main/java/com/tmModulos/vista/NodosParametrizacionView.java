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

    private List<Zona> zonasRecords;
    private String auxNombreZona;


    public NodosParametrizacionView() {
    }

    @PostConstruct
    public void init() {
       nodosRecords= nodoService.getNodosAll();
        zonasRecords = nodoService.getZonaAll();
    }

    public void inicio(){
    }

    public void onRowEdit(RowEditEvent event) {
        Nodo nodo = (Nodo) event.getObject();
        Zona zona = nodoService.getZonaByName(auxNombreZona);
        nodo.setZonaId(zona);
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

    public String getAuxNombreZona() {
        return auxNombreZona;
    }

    public void setAuxNombreZona(String auxNombreZona) {
        this.auxNombreZona = auxNombreZona;
    }

    public List<Zona> getZonasRecords() {
        return zonasRecords;
    }

    public void setZonasRecords(List<Zona> zonasRecords) {
        this.zonasRecords = zonasRecords;
    }


}
