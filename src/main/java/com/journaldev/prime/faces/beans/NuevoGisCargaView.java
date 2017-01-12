package com.journaldev.prime.faces.beans;

import com.journaldev.processing.saveData.DataProcesorImpl;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean(name="gisCarga")
@SessionScoped
public class NuevoGisCargaView implements Serializable{

    @ManagedProperty("#{DataProcesorImpl}")
    private DataProcesorImpl dataProcesor;

    private Date fechaProgramacion;
    private Date fechaVigencia;
    private UploadedFile gisCarga;
    private String messageContent="Failed";

    public void upload() {
        System.out.println("hola");
        if(gisCarga.getSize()>0) {
            try {
                dataProcesor.processDataFromFile(gisCarga.getFileName(),gisCarga.getInputstream());

            } catch (IOException e) {
                messageContent= "Failed";
            }
            FacesMessage message = new FacesMessage(messageContent);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
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


    public UploadedFile getGisCarga() {
        return gisCarga;
    }

    public void setGisCarga(UploadedFile gisCarga) {
        this.gisCarga = gisCarga;
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
}
