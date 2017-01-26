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

    @Column(name = "distancia")
    private int distancia;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nodo", nullable = false)
    private Nodo nodo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matriz_distancia", nullable = false)
    private MatrizDistancia matrizDistancia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servicio_distancia", nullable = false)
    private ServicioDistancia servicioDistancia;

    public DistanciaNodos() {
    }

    public DistanciaNodos(int distancia, Nodo nodo, MatrizDistancia matrizDistancia, ServicioDistancia servicioDistancia) {
        this.distancia = distancia;
        this.nodo = nodo;
        this.matrizDistancia = matrizDistancia;
        this.servicioDistancia = servicioDistancia;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public ServicioDistancia getServicioDistancia() {
        return servicioDistancia;
    }

    public void setServicioDistancia(ServicioDistancia servicioDistancia) {
        this.servicioDistancia = servicioDistancia;
    }
}
