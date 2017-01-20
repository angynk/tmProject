package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="ServicioGenerator")
    @SequenceGenerator(name="ServicioGenerator", sequenceName = "servicio_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "macro")
    private int macro;

    @Column(name = "linea")
    private int linea;

    @Column(name = "seccion")
    private int seccion;

    @Column(name = "config")
    private int config;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "servicio")
    private Set<DistanciaNodos> distanciaNodosRecords= new HashSet<DistanciaNodos>(0);

    public Servicio() {
    }

    public Servicio(int macro, int linea, int seccion, int config) {
        this.macro = macro;
        this.linea = linea;
        this.seccion = seccion;
        this.config = config;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMacro() {
        return macro;
    }

    public void setMacro(int macro) {
        this.macro = macro;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getSeccion() {
        return seccion;
    }

    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }

    public Set<DistanciaNodos> getDistanciaNodosRecords() {
        return distanciaNodosRecords;
    }

    public void setDistanciaNodosRecords(Set<DistanciaNodos> distanciaNodosRecords) {
        this.distanciaNodosRecords = distanciaNodosRecords;
    }
}
