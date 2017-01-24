package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="zona")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="ZonaGenerator")
    @SequenceGenerator(name="ZonaGenerator", sequenceName = "zona_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "zonaId")
    private Set<Nodo> zonaRecords= new HashSet<Nodo>(0);


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

    public Set<Nodo> getZonaRecords() {
        return zonaRecords;
    }

    public void setZonaRecords(Set<Nodo> zonaRecords) {
        this.zonaRecords = zonaRecords;
    }


    public String toString() {
        return nombre;
    }

}
