package com.journaldev.hibernate.data.entity.tmData;

import com.journaldev.hibernate.data.entity.Nodo;

import javax.persistence.*;

@Entity
@Table(name="distancia_nodos")
public class DistanciaNodos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="DistanciaGenerator")
    @SequenceGenerator(name="DistanciaGenerator", sequenceName = "matriz_distancia_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "ruta")
    private String ruta;

    @Column(name = "distancia")
    private int distancia;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nodo", nullable = false)
    private Nodo nodo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servicio", nullable = false)
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matriz_distancia", nullable = false)
    private Servicio matrizDistancia;

    public DistanciaNodos() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public Nodo getNodo() {
        return nodo;
    }

    public void setNodo(Nodo nodo) {
        this.nodo = nodo;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Servicio getMatrizDistancia() {
        return matrizDistancia;
    }

    public void setMatrizDistancia(Servicio matrizDistancia) {
        this.matrizDistancia = matrizDistancia;
    }
}
