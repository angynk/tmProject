package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name="temp_horario")
public class TempHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="tempHorario")
    @SequenceGenerator(name="tempHorario", sequenceName = "temp_horario_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "sublinea")
    private int sublinea;

    @Column(name = "linea")
    private int linea;

    @Column(name = "instante")
    private Time instante;

    @Column(name = "seccion")
    private int seccion;

    @Column(name = "macro")
    private int macro;

    @Column(name = "punto")
    private int punto;

    @Column(name = "evento")
    private int evento;

    @Column(name = "identificador")
    private String identificador;


    public void TempHorario(){

    }

    public int getSublinea() {
        return sublinea;
    }

    public void setSublinea(int sublinea) {
        this.sublinea = sublinea;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public Time getInstante() {
        return instante;
    }

    public void setInstante(Time instante) {
        this.instante = instante;
    }

    public int getSeccion() {
        return seccion;
    }

    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }

    public int getMacro() {
        return macro;
    }

    public void setMacro(int macro) {
        this.macro = macro;
    }

    public int getPunto() {
        return punto;
    }

    public void setPunto(int punto) {
        this.punto = punto;
    }

    public int getEvento() {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
