package com.tmModulos.modelo.entity.tmData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="tablamaestra")
public class TablaMaestra {

    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "version")
    private int version;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    @Column(name = "esta_vigente")
    private Boolean estaVigente;

    public TablaMaestra() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getEstaVigente() {
        return estaVigente;
    }

    public void setEstaVigente(Boolean estaVigente) {
        this.estaVigente = estaVigente;
    }
}
