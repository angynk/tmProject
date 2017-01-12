package com.journaldev.hibernate.data.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="trayecto")
public class Trayecto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TrayectoGenerator")
    @SequenceGenerator(name="TrayectoGenerator", sequenceName = "trayecto_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;
    @Column(name = "trayecto")
    private String trayecto;
    @Column(name = "linea")
    private int linea;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trayectoLinea")
    private Set<ArcoTiempo> arcoTiempoRecords = new HashSet<ArcoTiempo>(0);

    public Trayecto(String trayecto, int linea) {
        this.trayecto = trayecto;
        this.linea = linea;
    }

    public Trayecto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(String trayecto) {
        this.trayecto = trayecto;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public Set<ArcoTiempo> getArcoTiempoRecords() {
        return arcoTiempoRecords;
    }

    public void setArcoTiempoRecords(Set<ArcoTiempo> arcoTiempoRecords) {
        this.arcoTiempoRecords = arcoTiempoRecords;
    }
}
