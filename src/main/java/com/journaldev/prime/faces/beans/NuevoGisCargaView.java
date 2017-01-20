package com.journaldev.prime.faces.beans;

import com.journaldev.processing.saveData.DataProcesorImpl;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean(name="gisCarga")
@ViewScoped
public class NuevoGisCargaView implements Serializable{

    @ManagedProperty("#{DataProcesorImpl}")
    private DataProcesorImpl dataProcesor;

    private Date fechaProgramacion;
    private Date fechaVigencia;
    private UploadedFile gisCarga;
    private String tipoDia;
    private String descripcion;
    private String messageContent="Fallo el GIS de carga";
    private boolean status;
    private double progress = 0d;





    public void upload() {
        if(isValid()){
        if(gisCarga.getSize()>0 && gisCarga.getContentType().equals("application/vnd.ms-excel")) {
            progress=20;
            try {
                status = true;
                progress=50;
                if(descripcion.isEmpty()){ descripcion="Sin descripcion";}
                dataProcesor.processDataFromFile(gisCarga.getFileName(),gisCarga.getInputstream(), fechaProgramacion, fechaVigencia,tipoDia,descripcion);
               status =false;
                progress=100;
                messageContent = "GIS de Carga Almacenado";
            } catch (IOException e) {
                messageContent= "Failed";
            }

        }
        }else{
            messageContent="Complete todos los campos";
        }

        FacesMessage message = new FacesMessage(messageContent);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private boolean isValid() {

        if(gisCarga!=null){
            if(fechaProgramacion!=null){
                if(fechaVigencia!=null){
                    if(tipoDia!= null){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Date getFechaProgramacion() {
        return fechaProgramacion;
    }

    public void setFechaProgramacion(Date fechaProgramacion) {
        this.fechaProgramacion = fechaProgramacion;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UploadedFile getGisCarga() {
        return gisCarga;
    }

    public void setGisCarga(UploadedFile gisCarga) {
        this.gisCarga = gisCarga;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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


    public DataProcesorImpl getDataProcesor() {
        return dataProcesor;
    }

    public void setDataProcesor(DataProcesorImpl dataProcesor) {
        this.dataProcesor = dataProcesor;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
