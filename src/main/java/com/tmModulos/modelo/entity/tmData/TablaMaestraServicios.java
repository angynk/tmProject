package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(name = "secuencia")
    private Integer secuencia;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "identificador_inicio")
    private String idInicio;

    @Column(name = "identificador_fin")
    private String idFin;

    @Column(name = "tipo_dia")
    private String tipoDia;

    @Column(name = "matriz_nombre")
    private String matrizNombre;

    @Column(name = "trayecto")
    private String trayecto;

    @Column(name = "macro")
    private int macro;

    @Column(name = "linea")
    private int linea;

    @Column(name = "seccion")
    private int seccion;

    @Column(name = "tipo_servicio")
    private String tipoServicio;

    @Column(name = "nombre_general_servicio")
    private String nombreGeneral;

    @Column(name = "nombre_especial_servicio")
    private String nombreEspecial;

    @Column(name = "estado")
    private boolean estado;

    @Column(name = "codigo_inicio")
    private Integer codigoInicio;

    @Column(name = "codigo_fin")
    private Integer codigoFin;

    @Column(name = "zonaT_inicio")
    private String zonaTInicio;

    @Column(name = "zonaP_inicio")
    private String zonaPInicio;

    @Column(name = "nombre_inicio")
    private String nombreInicio;

    @Column(name = "zonaT_fin")
    private String zonaTFin;

    @Column(name = "zonaP_fin")
    private String zonaPFin;

    @Column(name = "nombre_fin")
    private String nombreIFin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tabla_maestra", nullable = false)
    private TablaMaestra tablaMeestra;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipologia", nullable = false)
    private Tipologia tipologia;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ciclo_servicio", nullable = false)
    private CicloServicio cicloServicio;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tablaMaestraServicios")
    private Set<IntervalosServicio> intervalosServiciossaRecords= new HashSet<IntervalosServicio>(0);

    @Transient
    private String estadoFormato;



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

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
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

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getMatrizNombre() {
        return matrizNombre;
    }

    public void setMatrizNombre(String matrizNombre) {
        this.matrizNombre = matrizNombre;
    }

    public String getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(String trayecto) {
        this.trayecto = trayecto;
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

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getNombreGeneral() {
        return nombreGeneral;
    }

    public void setNombreGeneral(String nombreGeneral) {
        this.nombreGeneral = nombreGeneral;
    }

    public String getNombreEspecial() {
        return nombreEspecial;
    }

    public void setNombreEspecial(String nombreEspecial) {
        this.nombreEspecial = nombreEspecial;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Integer getCodigoInicio() {
        return codigoInicio;
    }

    public void setCodigoInicio(Integer codigoInicio) {
        this.codigoInicio = codigoInicio;
    }

    public Integer getCodigoFin() {
        return codigoFin;
    }

    public void setCodigoFin(Integer codigoFin) {
        this.codigoFin = codigoFin;
    }

    public String getZonaTInicio() {
        return zonaTInicio;
    }

    public void setZonaTInicio(String zonaTInicio) {
        this.zonaTInicio = zonaTInicio;
    }

    public String getZonaPInicio() {
        return zonaPInicio;
    }

    public void setZonaPInicio(String zonaPInicio) {
        this.zonaPInicio = zonaPInicio;
    }

    public String getNombreInicio() {
        return nombreInicio;
    }

    public void setNombreInicio(String nombreInicio) {
        this.nombreInicio = nombreInicio;
    }

    public String getZonaTFin() {
        return zonaTFin;
    }

    public void setZonaTFin(String zonaTFin) {
        this.zonaTFin = zonaTFin;
    }

    public String getZonaPFin() {
        return zonaPFin;
    }

    public void setZonaPFin(String zonaPFin) {
        this.zonaPFin = zonaPFin;
    }

    public String getNombreIFin() {
        return nombreIFin;
    }

    public void setNombreIFin(String nombreIFin) {
        this.nombreIFin = nombreIFin;
    }

    public TablaMaestra getTablaMeestra() {
        return tablaMeestra;
    }

    public void setTablaMeestra(TablaMaestra tablaMeestra) {
        this.tablaMeestra = tablaMeestra;
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }


    public Set<IntervalosServicio> getIntervalosServiciossaRecords() {
        return intervalosServiciossaRecords;
    }

    public void setIntervalosServiciossaRecords(Set<IntervalosServicio> intervalosServiciossaRecords) {
        this.intervalosServiciossaRecords = intervalosServiciossaRecords;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getEstadoFormato() {
        if( estado ){
            return "Activo";
        }
        return "Inactivo";
    }

    public void setEstadoFormato(String estadoFormato) {
        this.estadoFormato = estadoFormato;
    }

    public CicloServicio getCicloServicio() {
        return cicloServicio;
    }

    public void setCicloServicio(CicloServicio cicloServicio) {
        this.cicloServicio = cicloServicio;
    }
}
