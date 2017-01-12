package com.journaldev.hibernate.data.entity;

import javax.persistence.*;

@Entity
@Table(name="tipo_dia_detalle")
public class TipoDiaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "detalle")
    private String detalle;

    @Column(name = "codigo")
    private Integer codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_dia", nullable = false)
    private TipoDia tipoDia;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public TipoDia getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(TipoDia tipoDia) {
        this.tipoDia = tipoDia;
    }
}
