package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="gis_intervalos")
public class GisIntervalos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="GisIGenerator")
    @SequenceGenerator(name="GisIGenerator", sequenceName = "gis_intervalos_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    @Column(name = "fecha_programacion")
    private Date fechaProgramacion;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "cuadro")
    private String cuadro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_dia", nullable = false)
    private TipoDia tipoDia;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gisIntervalos")
    private Set<Intervalos> intervalosRecords = new HashSet<Intervalos>(0);

    @Transient
    private String fechaCreacionFormato;
    @Transient
    private String fechaProgramacionFormato;

    public GisIntervalos() {
    }

    public GisIntervalos(Date fechaCreacion, Date fechaProgramacion, String descripcion, String cuadro,TipoDia tipoDia) {
        this.fechaCreacion = fechaCreacion;
        this.fechaProgramacion = fechaProgramacion;
        this.descripcion = descripcion;
        this.cuadro = cuadro;
        this.tipoDia=tipoDia;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Intervalos> getIntervalosRecords() {
        return intervalosRecords;
    }

    public void setIntervalosRecords(Set<Intervalos> intervalosRecords) {
        this.intervalosRecords = intervalosRecords;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaProgramacion() {
        return fechaProgramacion;
    }

    public void setFechaProgramacion(Date fechaProgramacion) {
        this.fechaProgramacion = fechaProgramacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCuadro() {
        return cuadro;
    }

    public void setCuadro(String cuadro) {
        this.cuadro = cuadro;
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
        return dt1.format(fechaProgramacion);
    }

    public void setFechaProgramacionFormato(String fechaProgramacionFormato) {
        this.fechaProgramacionFormato = fechaProgramacionFormato;
    }

    public TipoDia getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(TipoDia tipoDia) {
        this.tipoDia = tipoDia;
    }
}
