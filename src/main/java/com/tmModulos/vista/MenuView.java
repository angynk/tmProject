package com.tmModulos.vista;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

@ManagedBean(name="menu")
@SessionScoped
public class MenuView {


    public void refreshServicios(){
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        try {
//            ec.redirect(ec.getRequestContextPath()
//                    + "/ServiciosParametrizacion.xhtml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void refreshServiciosporDia(){
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        try {
//            ec.redirect(ec.getRequestContextPath()
//                    + "/ServiciosPorDiaParametrizacion.xhtml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void refreshNodo(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/NodosParametrizacion.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshNuevaTablaMaestra(){
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        try {
//            ec.redirect(ec.getRequestContextPath()
//                    + "/NuevaTablaMaestra.xhtml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void refreshBuscarTablaMaestra(){
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        try {
//            ec.redirect(ec.getRequestContextPath()
//                    + "/BuscarTablaMaestra.xhtml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void refreshNuevoGISCarga(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/NewGISCarga.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshBuscarGISCarga(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/BuscarGISCarga.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshNuevaMatrizDistancia(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/NuevaMatrizDistancias.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshListaNegra(){
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        try {
//            ec.redirect(ec.getRequestContextPath()
//                    + "/ListaNegraMatriz.xhtml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void refreshBuscarMatrizDistancia(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/BuscarMatrizDistancia.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void refreshNuevosIntervalosGIS(){
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        try {
//            ec.redirect(ec.getRequestContextPath()
//                    + "/NuevoIntervalosGIS.xhtml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void refreshBuscarIntervalosGIS(){
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        try {
//            ec.redirect(ec.getRequestContextPath()
//                    + "/BuscarGisIntervalos.xhtml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
