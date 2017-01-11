package com.journaldev.prime.faces.beans;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean(name="NuevoGisCargaView")
@SessionScoped
public class NuevoGisCargaView implements Serializable{

    private Date fechaProgramacion;
    private Date fechaVigencia;
    private UploadedFile gisCarga;

    public void upload() {
        System.out.println("hola");
        if(gisCarga != null) {
            FacesMessage message = new FacesMessage("Succesful", gisCarga.getFileName() + " is uploaded.");
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
}
