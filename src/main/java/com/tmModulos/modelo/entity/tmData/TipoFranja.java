package com.tmModulos.modelo.entity.tmData;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tipo_franja")
public class TipoFranja {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TipoFranjaGenerator")
    @SequenceGenerator(name="TipoFranjaGenerator", sequenceName = "tipo_franja_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "hora_inicio")
    private String horaInicio;

    @Column(name = "hora_fin")
    private String horaFin;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoFranja")
    private Set<CicloServicio> cicloServiciosRecords = new HashSet<CicloServicio>(0);


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoFranja")
    private Set<IntervalosServicio> intervalosServiciossRecords = new HashSet<IntervalosServicio>(0);


    public TipoFranja() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public Set<CicloServicio> getCicloServiciosRecords() {
        return cicloServiciosRecords;
    }

    public void setCicloServiciosRecords(Set<CicloServicio> cicloServiciosRecords) {
        this.cicloServiciosRecords = cicloServiciosRecords;
    }

    public Set<IntervalosServicio> getIntervalosServiciossRecords() {
        return intervalosServiciossRecords;
    }

    public void setIntervalosServiciossRecords(Set<IntervalosServicio> intervalosServiciossRecords) {
        this.intervalosServiciossRecords = intervalosServiciossRecords;
    }
}
