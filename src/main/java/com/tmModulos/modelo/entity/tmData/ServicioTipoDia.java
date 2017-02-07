package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="servicio_tipo_dia")
public class ServicioTipoDia {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="ServicioTipoDiaGenerator")
    @SequenceGenerator(name="ServicioTipoDiaGenerator", sequenceName = "servicio_tipo_dia_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "nombre_servicio")
    private String nombre;

    @Column(name = "punto_inicio")
    private String puntoInicio;

    @Column(name = "punto_final")
    private String puntoFinal;

    @Column(name = "zona_inicio")
    private String zonaInicio;

    @Column(name = "zona_fin")
    private String zonaFinal;

    @Column(name = "orden")
    private int orden;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_dia", nullable = false)
    private TipoDia tipoDia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio", nullable = false)
    private Servicio servicio;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idServicio")
    private Set<TiempoIntervalos> tiempoIntervalosRecords = new HashSet<TiempoIntervalos>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idServicio")
    private Set<Intervalos> serviciosRecords = new HashSet<Intervalos>(0);

    public ServicioTipoDia() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuntoInicio() {
        return puntoInicio;
    }

    public void setPuntoInicio(String puntoInicio) {
        this.puntoInicio = puntoInicio;
    }

    public String getPuntoFinal() {
        return puntoFinal;
    }

    public void setPuntoFinal(String puntoFinal) {
        this.puntoFinal = puntoFinal;
    }

    public String getZonaInicio() {
        return zonaInicio;
    }

    public void setZonaInicio(String zonaInicio) {
        this.zonaInicio = zonaInicio;
    }

    public String getZonaFinal() {
        return zonaFinal;
    }

    public void setZonaFinal(String zonaFinal) {
        this.zonaFinal = zonaFinal;
    }

    public TipoDia getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(TipoDia tipoDia) {
        this.tipoDia = tipoDia;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public Set<TiempoIntervalos> getTiempoIntervalosRecords() {
        return tiempoIntervalosRecords;
    }

    public void setTiempoIntervalosRecords(Set<TiempoIntervalos> tiempoIntervalosRecords) {
        this.tiempoIntervalosRecords = tiempoIntervalosRecords;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }
}
