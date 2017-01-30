package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="ciclo_servicio")
public class CicloServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="CicloGenerator")
    @SequenceGenerator(name="CicloGenerator", sequenceName = "ciclo_servicio_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;


    @Column(name = "promedio")
    private Integer promedio;

    @Column(name = "minimo")
    private Integer minimo;

    @Column(name = "maximo")
    private Integer maximo;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_franja", nullable = false)
    private TipoFranja tipoFranja;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tabla_servicios", nullable = false)
    private TablaMaestraServicios tablaMaestraServicios;


    public CicloServicio() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getPromedio() {
        return promedio;
    }

    public void setPromedio(Integer promedio) {
        this.promedio = promedio;
    }

    public Integer getMinimo() {
        return minimo;
    }

    public void setMinimo(Integer minimo) {
        this.minimo = minimo;
    }

    public Integer getMaximo() {
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

    public TipoFranja getTipoFranja() {
        return tipoFranja;
    }

    public void setTipoFranja(TipoFranja tipoFranja) {
        this.tipoFranja = tipoFranja;
    }

    public TablaMaestraServicios getTablaMaestraServicios() {
        return tablaMaestraServicios;
    }

    public void setTablaMaestraServicios(TablaMaestraServicios tablaMaestraServicios) {
        this.tablaMaestraServicios = tablaMaestraServicios;
    }
}
