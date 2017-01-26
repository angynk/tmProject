package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.TablaMaestraProcessor;
import com.tmModulos.modelo.entity.tmData.GisCarga;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="tablaMaestra")
@SessionScoped
public class NuevaTablaMaestra {

    private String tipoGeneracion;
    private String descripcion;
    private boolean archivoVisible;
    private boolean automaticoVisible;
    private Date fechaDeProgramacion;
    private Date fechaDeVigencia;
    private UploadedFile tablaMaestra;

    private String gisCarga;
    private List<GisCarga> gisCargaList;

    private String matrizDistancia;
    private List<MatrizDistancia> matrizDistanciasList;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    @ManagedProperty("#{TablaMaestraProcessor}")
    private TablaMaestraProcessor tablaMaestraProcessor;


    @PostConstruct
    public void init() {
        tipoGeneracion = "2";
        automaticoVisible=false;
        archivoVisible=true;
        gisCargaList = tablaMaestraProcessor.getGisCargaList();
        matrizDistanciasList = tablaMaestraProcessor.getMatrizDistanciaAll();
    }


    public void calcularTablaMaestra(){
        if(valid()){
            boolean resultado=tablaMaestraProcessor.calcularTablaMaestra(fechaDeProgramacion,
                    descripcion,gisCarga,matrizDistancia);
            if(resultado){
                messagesView.info(Messages.MENSAJE_CALCULO_EXITOSO,Messages.ACCION_TABLA_MAESTRA_ALMACENADA);
            }

        }else{
            messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS, Messages.ACCION_CAMPOS_INCOMPLETOS);
        }



    }

    public boolean valid(){
        if(descripcion!=null){
            if(fechaDeProgramacion!=null){
                    return true;

            }
        }
        return false;
    }


    public String getTipoGeneracion() {
        return tipoGeneracion;
    }

    public void setTipoGeneracion(String tipoGeneracion) {
        this.tipoGeneracion = tipoGeneracion;
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

    public UploadedFile getTablaMaestra() {
        return tablaMaestra;
    }

    public void setTablaMaestra(UploadedFile tablaMaestra) {
        this.tablaMaestra = tablaMaestra;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public List<GisCarga> getGisCargaList() {
        return gisCargaList;
    }

    public void setGisCargaList(List<GisCarga> gisCargaList) {
        this.gisCargaList = gisCargaList;
    }

    public TablaMaestraProcessor getTablaMaestraProcessor() {
        return tablaMaestraProcessor;
    }

    public void setTablaMaestraProcessor(TablaMaestraProcessor tablaMaestraProcessor) {
        this.tablaMaestraProcessor = tablaMaestraProcessor;
    }


    public List<MatrizDistancia> getMatrizDistanciasList() {
        return matrizDistanciasList;
    }

    public void setMatrizDistanciasList(List<MatrizDistancia> matrizDistanciasList) {
        this.matrizDistanciasList = matrizDistanciasList;
    }

    public String getMatrizDistancia() {
        return matrizDistancia;
    }

    public void setMatrizDistancia(String matrizDistancia) {
        this.matrizDistancia = matrizDistancia;
    }

    public String getGisCarga() {
        return gisCarga;
    }

    public void setGisCarga(String gisCarga) {
        this.gisCarga = gisCarga;
    }
}
