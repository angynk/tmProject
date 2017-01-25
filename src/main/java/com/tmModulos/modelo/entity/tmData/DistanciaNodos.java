package com.tmModulos.modelo.entity.tmData;

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

    @Column(name = "macro")
    private int macro;

    @Column(name = "linea")
    private int linea;

    @Column(name = "seccion")
    private int seccion;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nodo", nullable = false)
    private Nodo nodo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matriz_distancia", nullable = false)
    private MatrizDistancia matrizDistancia;

    public DistanciaNodos() {
    }

    public DistanciaNodos(String ruta, int distancia, int macro, int linea, int seccion, Nodo nodo,MatrizDistancia matrizDistancia) {
        this.ruta = ruta;
        this.distancia = distancia;
        this.macro = macro;
        this.linea = linea;
        this.seccion = seccion;
        this.nodo = nodo;
        this.matrizDistancia = matrizDistancia;
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

    public MatrizDistancia getMatrizDistancia() {
        return matrizDistancia;
    }

    public void setMatrizDistancia(MatrizDistancia matrizDistancia) {
        this.matrizDistancia = matrizDistancia;
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
}
