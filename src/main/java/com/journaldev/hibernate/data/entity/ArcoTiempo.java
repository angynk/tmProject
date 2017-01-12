package com.journaldev.hibernate.data.entity;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name="arco_tiempo")
public class ArcoTiempo {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "sentido")
    private int sentido;

    @Column(name = "secuencia")
    private int secuencia;

    @Column(name = "tipo_arco")
    private int tipoArco;

    @Column(name = "distancia")
    private Integer distancia;

    @Column(name = "hora_desde")
    private LocalTime horaDesde;

    @Column(name = "hora_hasta")
    private LocalTime horaHasta;

    @Column(name = "tiempo_minimo")
    private LocalTime tiempoMinimo;

    @Column(name = "tiempo_maximo")
    private LocalTime tiempoMaximo;

    @Column(name = "tiempo_optimo")
    private LocalTime tiempoOptimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gis_carga", nullable = false)
    private GisCarga gisCargaArco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trayecto_linea", nullable = false)
    private Trayecto trayectoLinea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_dia", nullable = false)
    private TipoDiaDetalle tipoDiaByArco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nodo_inicial", nullable = false)
    private Nodo nodoInicial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nodo_final", nullable = false)
    private Nodo nodoFinal;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSentido() {
        return sentido;
    }

    public void setSentido(int sentido) {
        this.sentido = sentido;
    }

    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public int getTipoArco() {
        return tipoArco;
    }

    public void setTipoArco(int tipoArco) {
        this.tipoArco = tipoArco;
    }

    public Integer getDistancia() {
        return distancia;
    }

    public void setDistancia(Integer distancia) {
        this.distancia = distancia;
    }

    public LocalTime getHoraDesde() {
        return horaDesde;
    }

    public void setHoraDesde(LocalTime horaDesde) {
        this.horaDesde = horaDesde;
    }

    public LocalTime getHoraHasta() {
        return horaHasta;
    }

    public void setHoraHasta(LocalTime horaHasta) {
        this.horaHasta = horaHasta;
    }

    public LocalTime getTiempoMinimo() {
        return tiempoMinimo;
    }

    public void setTiempoMinimo(LocalTime tiempoMinimo) {
        this.tiempoMinimo = tiempoMinimo;
    }

    public LocalTime getTiempoMaximo() {
        return tiempoMaximo;
    }

    public void setTiempoMaximo(LocalTime tiempoMaximo) {
        this.tiempoMaximo = tiempoMaximo;
    }

    public LocalTime getTiempoOptimo() {
        return tiempoOptimo;
    }

    public void setTiempoOptimo(LocalTime tiempoOptimo) {
        this.tiempoOptimo = tiempoOptimo;
    }

    public GisCarga getGisCargaArco() {
        return gisCargaArco;
    }

    public void setGisCargaArco(GisCarga gisCargaArco) {
        this.gisCargaArco = gisCargaArco;
    }

    public Trayecto getTrayectoLinea() {
        return trayectoLinea;
    }

    public void setTrayectoLinea(Trayecto trayectoLinea) {
        this.trayectoLinea = trayectoLinea;
    }

    public TipoDiaDetalle getTipoDiaByArco() {
        return tipoDiaByArco;
    }

    public void setTipoDiaByArco(TipoDiaDetalle tipoDiaByArco) {
        this.tipoDiaByArco = tipoDiaByArco;
    }

    public Nodo getNodoInicial() {
        return nodoInicial;
    }

    public void setNodoInicial(Nodo nodoInicial) {
        this.nodoInicial = nodoInicial;
    }

    public Nodo getNodoFinal() {
        return nodoFinal;
    }

    public void setNodoFinal(Nodo nodoFinal) {
        this.nodoFinal = nodoFinal;
    }
}
