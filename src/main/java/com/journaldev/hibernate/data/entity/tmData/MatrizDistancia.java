package com.journaldev.hibernate.data.entity.tmData;

import javax.persistence.*;
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

    public MatrizDistancia() {
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
}
