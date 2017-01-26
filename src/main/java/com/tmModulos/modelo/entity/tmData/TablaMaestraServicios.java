package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="tabla_maestra_servicios")
public class TablaMaestraServicios {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TablaSGenerator")
    @SequenceGenerator(name="TablaSGenerator", sequenceName = "tabla_maestra_servicios_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "distancia")
    private Integer distancia;

    @Column(name = "identificador_inicio")
    private String idInicio;

    @Column(name = "identificador_fin")
    private String idFin;

    @Column(name = "tipo_dia")
    private String tipoDia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servicio", nullable = false)
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tabla_maestra", nullable = false)
    private TablaMaestra tablaMeestra;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nodo_inicial", nullable = false)
    private Nodo nodoIncial;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nodo_final", nullable = false)
    private Nodo nodoFinal;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getDistancia() {
        return distancia;
    }

    public void setDistancia(Integer distancia) {
        this.distancia = distancia;
    }

    public String getIdInicio() {
        return idInicio;
    }

    public void setIdInicio(String idInicio) {
        this.idInicio = idInicio;
    }

    public String getIdFin() {
        return idFin;
    }

    public void setIdFin(String idFin) {
        this.idFin = idFin;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public TablaMaestra getTablaMeestra() {
        return tablaMeestra;
    }

    public void setTablaMeestra(TablaMaestra tablaMeestra) {
        this.tablaMeestra = tablaMeestra;
    }

    public Nodo getNodoIncial() {
        return nodoIncial;
    }

    public void setNodoIncial(Nodo nodoIncial) {
        this.nodoIncial = nodoIncial;
    }

    public Nodo getNodoFinal() {
        return nodoFinal;
    }

    public void setNodoFinal(Nodo nodoFinal) {
        this.nodoFinal = nodoFinal;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }
}
