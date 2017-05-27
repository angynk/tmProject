package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="temp_matriz_distancia")
public class TemporalMatrizDistancia {

    public TemporalMatrizDistancia() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TemporalGenerators")
    @SequenceGenerator(name="TemporalGenerators", sequenceName = "temp_matriz_distancia_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "ruta")
    private String ruta;

    @Column(name = "macro")
    private int macro;


    @Column(name = "linea")
    private int linea;

    @Column(name = "seccion")
    private int seccion;

    @Column(name = "nodo")
    private int nodo;

    @Column(name = "abscisa")
    private int abscisa;

    @Column(name = "nombre")
    private String nombre;

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
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

    public int getNodo() {
        return nodo;
    }

    public void setNodo(int nodo) {
        this.nodo = nodo;
    }

    public int getAbscisa() {
        return abscisa;
    }

    public void setAbscisa(int abscisa) {
        this.abscisa = abscisa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
