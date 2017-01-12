package com.journaldev.hibernate.data.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tipodia")
public class TipoDia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoDia")
    private Set<TipoDiaDetalle> tipoDiaDetalleRecords = new HashSet<TipoDiaDetalle>(0);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<TipoDiaDetalle> getTipoDiaDetalleRecords() {
        return tipoDiaDetalleRecords;
    }

    public void setTipoDiaDetalleRecords(Set<TipoDiaDetalle> tipoDiaDetalleRecords) {
        this.tipoDiaDetalleRecords = tipoDiaDetalleRecords;
    }
}
