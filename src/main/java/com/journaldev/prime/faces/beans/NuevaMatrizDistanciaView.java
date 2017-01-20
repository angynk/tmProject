package com.journaldev.prime.faces.beans;

import com.journaldev.hibernate.data.entity.tmData.DistanciaNodos;
import com.journaldev.hibernate.data.entity.tmData.MatrizDistancia;
import com.journaldev.hibernate.data.entity.tmData.Servicio;
import com.journaldev.processing.saveData.MatrizProcessor;
import com.journaldev.spring.service.BusquedaService;
import com.journaldev.spring.service.MatrizDistanciaService;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
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
    private String messageContent="Fallo la generacion de la matriz de distancia";

    @ManagedProperty("#{MatrizProcessor}")
    private MatrizProcessor matrizProcessor;

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
        if(isValid()){
            if(matrizDistancias.getSize()>0 && matrizDistancias.getContentType().equals("application/vnd.ms-excel")) {
                try {
                    matrizProcessor.processDataFromFile(matrizDistancias.getFileName(),matrizDistancias.getInputstream(), fechaDeProgramacion,numeracion);
                    messageContent = "Matriz de distancias almacenada";
                } catch (IOException e) {
                    messageContent= "Fallo la carga del archivo";
                }

            }
        }else{
            messageContent="Complete todos los campos";
        }
        FacesMessage message = new FacesMessage(messageContent);
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    private boolean isValid() {

        if(matrizDistancias!=null){
            if(fechaDeProgramacion!=null){
                if(numeracion!=null){
                    return true;
                }
            }
        }
        return false;
    }

    public void calcularMatrizDistancias(){
        if( numeracion!=null ){
            if(fechaDeVigencia!=null ){
                   boolean resultado= matrizProcessor.calcularMatrizDistancia(fechaDeVigencia,numeracion);
                   if(resultado){
                       messageContent = "GIS de Carga Almacenado";
                   }else{
                       messageContent = "No es posible generar GIS de Carga para la fecha seleccionada";
                   }
                FacesMessage message = new FacesMessage(messageContent);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
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

    public MatrizProcessor getMatrizProcessor() {
        return matrizProcessor;
    }

    public void setMatrizProcessor(MatrizProcessor matrizProcessor) {
        this.matrizProcessor = matrizProcessor;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
