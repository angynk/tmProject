package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="matriz_distancia")
public class MatrizDistancia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="MatrizGenerator")
    @SequenceGenerator(name="MatrizGenerator", sequenceName = "matriz_distancia_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "fecha_aplicacion")
    private Date fechaAplicacion;

    @Column(name = "numeracion")
    private String numeracion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "matrizDistancia")
    private Set<DistanciaNodos> distanciaNodosRecords= new HashSet<DistanciaNodos>(0);

    @Transient
    private String fechaCreacionFormato;
    @Transient
    private String fechaProgramacionFormato;

    public MatrizDistancia() {
    }

    public MatrizDistancia(Date fechaCreacion, Date fechaAplicacion, String numeracion) {
        this.fechaCreacion = fechaCreacion;
        this.fechaAplicacion = fechaAplicacion;
        this.numeracion = numeracion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public Set<DistanciaNodos> getDistanciaNodosRecords() {
        return distanciaNodosRecords;
    }

    public void setDistanciaNodosRecords(Set<DistanciaNodos> distanciaNodosRecords) {
        this.distanciaNodosRecords = distanciaNodosRecords;
    }

    public String getFechaCreacionFormato() {
        SimpleDateFormat dt1 = new SimpleDateFormat("YYYY-MM-DD");
        return dt1.format(fechaCreacion);
    }

    public void setFechaCreacionFormato(String fechaCreacionFormato) {
        this.fechaCreacionFormato = fechaCreacionFormato;
    }

    public String getFechaProgramacionFormato() {
        SimpleDateFormat dt1 = new SimpleDateFormat("YYYY-MM-DD");
        return dt1.format(fechaAplicacion);
    }

    public void setFechaProgramacionFormato(String fechaProgramacionFormato) {
        this.fechaProgramacionFormato = fechaProgramacionFormato;
    }
}
