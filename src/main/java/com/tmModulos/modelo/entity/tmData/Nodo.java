package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="nodo")
public class Nodo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="NodoGenerator")
    @SequenceGenerator(name="NodoGenerator", sequenceName = "nodo_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigo")
    private Integer codigo;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zona", nullable = false)
    private Zona zonaId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "nodoInicial")
    private Set<ArcoTiempo> arcoTiempoNodoInicialRecords = new HashSet<ArcoTiempo>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "nodoFinal")
    private Set<ArcoTiempo> arcoTiempoNodoFinalRecords = new HashSet<ArcoTiempo>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "nodo")
    private Set<DistanciaNodos> distanciaNodosRecords= new HashSet<DistanciaNodos>(0);



    public Nodo() {
    }

    public Nodo(String nombre) {
        this.nombre = nombre;
    }

    public Nodo(String nombre, Integer codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

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

    public Integer getCodigo() {
        return codigo;
    }


    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Set<ArcoTiempo> getArcoTiempoNodoInicialRecords() {
        return arcoTiempoNodoInicialRecords;
    }

    public void setArcoTiempoNodoInicialRecords(Set<ArcoTiempo> arcoTiempoNodoInicialRecords) {
        this.arcoTiempoNodoInicialRecords = arcoTiempoNodoInicialRecords;
    }

    public Set<ArcoTiempo> getArcoTiempoNodoFinalRecords() {
        return arcoTiempoNodoFinalRecords;
    }

    public void setArcoTiempoNodoFinalRecords(Set<ArcoTiempo> arcoTiempoNodoFinalRecords) {
        this.arcoTiempoNodoFinalRecords = arcoTiempoNodoFinalRecords;
    }

    public Set<DistanciaNodos> getDistanciaNodosRecords() {
        return distanciaNodosRecords;
    }

    public void setDistanciaNodosRecords(Set<DistanciaNodos> distanciaNodosRecords) {
        this.distanciaNodosRecords = distanciaNodosRecords;
    }

    public Zona getZonaId() {
        return zonaId;
    }

    public void setZonaId(Zona zonaId) {
        this.zonaId = zonaId;
    }




}
