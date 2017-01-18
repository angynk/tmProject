package com.journaldev.prime.faces.beans;

import com.journaldev.hibernate.data.entity.tmData.DistanciaNodos;
import com.journaldev.hibernate.data.entity.tmData.MatrizDistancia;
import com.journaldev.hibernate.data.entity.tmData.Servicio;
import com.journaldev.spring.service.BusquedaService;
import com.journaldev.spring.service.MatrizDistanciaService;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.util.Date;
import java.util.List;

@ManagedBean(name="matrizDis")
@SessionScoped
public class NuevaMatrizDistanciaView {


    private String tipoGeneracion;
    private String numeracion;
    private boolean archivoVisible;
    private boolean automaticoVisible;
    private Date fechaDeProgramacion;
    private Date fechaDeVigencia;
    private UploadedFile matrizDistancias;

    @ManagedProperty("#{MatrizDistanciaService}")
    private MatrizDistanciaService matrizDistanciaService;

    @PostConstruct
    public void init() {
        tipoGeneracion = "1";
        automaticoVisible=true;
        archivoVisible=false;
    }

    public void cambioTipoGeneracion(){
        if(tipoGeneracion.equals("1")){
           automaticoVisible=true;
           archivoVisible=false;
        }else{
            automaticoVisible=false;
            archivoVisible=true;
        }
    }

    public void cargarArchivo(){
        long sle= 10000;
        try {
            Thread.sleep(sle);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void calcularMatrizDistancias(){

        List<DistanciaNodos> distanciaNodosAll = matrizDistanciaService.getDistanciaNodosAll();

    }

    public String getTipoGeneracion() {
        return tipoGeneracion;
    }

    public void setTipoGeneracion(String tipoGeneracion) {
        this.tipoGeneracion = tipoGeneracion;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public boolean isArchivoVisible() {
        return archivoVisible;
    }

    public void setArchivoVisible(boolean archivoVisible) {
        this.archivoVisible = archivoVisible;
    }

    public boolean isAutomaticoVisible() {
        return automaticoVisible;
    }

    public void setAutomaticoVisible(boolean automaticoVisible) {
        this.automaticoVisible = automaticoVisible;
    }

    public Date getFechaDeProgramacion() {
        return fechaDeProgramacion;
    }

    public void setFechaDeProgramacion(Date fechaDeProgramacion) {
        this.fechaDeProgramacion = fechaDeProgramacion;
    }

    public Date getFechaDeVigencia() {
        return fechaDeVigencia;
    }

    public void setFechaDeVigencia(Date fechaDeVigencia) {
        this.fechaDeVigencia = fechaDeVigencia;
    }

    public UploadedFile getMatrizDistancias() {
        return matrizDistancias;
    }

    public void setMatrizDistancias(UploadedFile matrizDistancias) {
        this.matrizDistancias = matrizDistancias;
    }

    public MatrizDistanciaService getMatrizDistanciaService() {
        return matrizDistanciaService;
    }

    public void setMatrizDistanciaService(MatrizDistanciaService matrizDistanciaService) {
        this.matrizDistanciaService = matrizDistanciaService;
    }
}
