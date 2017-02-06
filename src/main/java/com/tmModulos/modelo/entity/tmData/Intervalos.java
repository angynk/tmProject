package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="intervalos")
public class Intervalos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="InteGenerator")
    @SequenceGenerator(name="InteGenerator", sequenceName = "intervalos_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "tipo_calculo")
    private String tipoCalculo;

    @Column(name = "valor_inicio")
    private Double valorInicio;

    @Column(name = "valor_am")
    private Double valorAM;

    @Column(name = "valor_valle")
    private Double valorValle;

    @Column(name = "valor_pm")
    private Double valorPM;

    @Column(name = "valor_cierre")
    private Double valorCierre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "identificador", nullable = false)
    private ServicioTipoDia idServicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gis_intervalos", nullable = false)
    private GisIntervalos gisIntervalos;

    public Intervalos(String tipoCalculo, Double valorInicio, Double valorAM, Double valorValle, Double valorPM, Double valorCierre,ServicioTipoDia idServicio,GisIntervalos gisIntervalos) {
        this.tipoCalculo = tipoCalculo;
        this.valorInicio = valorInicio;
        this.valorAM = valorAM;
        this.valorValle = valorValle;
        this.valorPM = valorPM;
        this.valorCierre = valorCierre;
        this.idServicio = idServicio;
        this.gisIntervalos=gisIntervalos;
    }



    public Intervalos() {
    }

    public GisIntervalos getGisIntervalos() {
        return gisIntervalos;
    }

    public void setGisIntervalos(GisIntervalos gisIntervalos) {
        this.gisIntervalos = gisIntervalos;
    }

    public ServicioTipoDia getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(ServicioTipoDia idServicio) {
        this.idServicio = idServicio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipoCalculo() {
        return tipoCalculo;
    }

    public void setTipoCalculo(String tipoCalculo) {
        this.tipoCalculo = tipoCalculo;
    }

    public Double getValorInicio() {
        return valorInicio;
    }

    public void setValorInicio(Double valorInicio) {
        this.valorInicio = valorInicio;
    }

    public Double getValorAM() {
        return valorAM;
    }

    public void setValorAM(Double valorAM) {
        this.valorAM = valorAM;
    }

    public Double getValorValle() {
        return valorValle;
    }

    public void setValorValle(Double valorValle) {
        this.valorValle = valorValle;
    }

    public Double getValorPM() {
        return valorPM;
    }

    public void setValorPM(Double valorPM) {
        this.valorPM = valorPM;
    }

    public Double getValorCierre() {
        return valorCierre;
    }

    public void setValorCierre(Double valorCierre) {
        this.valorCierre = valorCierre;
    }
}
