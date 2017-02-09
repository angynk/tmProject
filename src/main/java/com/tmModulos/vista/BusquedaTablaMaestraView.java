package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import com.tmModulos.controlador.servicios.TablaMaestraService;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.entity.tmData.DistanciaNodos;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import com.tmModulos.modelo.entity.tmData.TablaMaestra;
import com.tmModulos.modelo.entity.tmData.TablaMaestraServicios;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="busquedaTabla")
@SessionScoped
public class BusquedaTablaMaestraView {

    private String busqueda;
    private Date fechaInicial;
    private Date fechaFinal;
    private String tipoFecha;
    private boolean visibleRecords;
    private boolean fechaFinalVisible;

    private String tipoCiclo;
    private String tipoIntervalo;



    private List<TablaMaestra> tablaMaestraRecords;
    private TablaMaestra selectedTabla;
    private TablaMaestra nuevaTabla;
    private List<TablaMaestra> filteredTablaMaestra;


    private List<TablaMaestraServicios> tServiciosRecords;
    private List<TablaMaestraServicios> filteredTServiciosRecords;
    TablaMaestraServicios serviciosSelected;

    @ManagedProperty("#{TablaMaestraService}")
    private TablaMaestraService tablaMaestraService;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    public BusquedaTablaMaestraView() {
    }

    @PostConstruct
    public void init() {
        busqueda = "1";
        fechaFinalVisible=false;
        tablaMaestraRecords = new ArrayList<>();
        visibleRecords = false;
    }

    public void inicio(){

    }

    public void habilitarNuevo(){
        nuevaTabla = new TablaMaestra();
    }

    public void actualizar(){

    }

    public void cancelar(){

    }

    public void reinciar(){
        busqueda = "1";
        fechaFinalVisible=false;
        tablaMaestraRecords = new ArrayList<>();
        visibleRecords = false;
        fechaFinal = null;
        fechaInicial = null;
    }

    public void buscar(){
        visibleRecords=true;
        if (busqueda.equals("1")){
            if(fechaInicial!=null && tipoFecha!= null){
                tablaMaestraRecords = tablaMaestraService.getTablaMaestraByFecha(tipoFecha,fechaInicial);
            }else { messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);}
        }else if (busqueda.equals("2")){
            if( fechaInicial!= null && fechaFinal!=null && tipoFecha!= null){
                tablaMaestraRecords = tablaMaestraService.getTablaMaestraBetwenFechas(tipoFecha, fechaInicial,fechaFinal);
            }else { messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);}
        }else{
            messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);
        }

    }

    public void cambioTipoBusqueda(){
        if(busqueda.equals("1")){
            fechaFinalVisible= false;
        }else{
            fechaFinalVisible=true;
        }
    }

    public void atras(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/BuscarTablaMaestra.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void busquedaTablaMaestra(){

        List<TablaMaestraServicios> auxiliar =new ArrayList<>();
        visibleRecords=true;
        tServiciosRecords = tablaMaestraService.getServiciosByTabla(selectedTabla);

        for(TablaMaestraServicios tServicio:tServiciosRecords){
            tServicio.setTipoIntervalo(tipoIntervalo);
            auxiliar.add(tServicio);
        }

        tServiciosRecords=auxiliar;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/tablaMaestra.xhtml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getTipoFecha() {
        return tipoFecha;
    }

    public void setTipoFecha(String tipoFecha) {
        this.tipoFecha = tipoFecha;
    }

    public boolean isVisibleRecords() {
        return visibleRecords;
    }

    public void setVisibleRecords(boolean visibleRecords) {
        this.visibleRecords = visibleRecords;
    }

    public boolean isFechaFinalVisible() {
        return fechaFinalVisible;
    }

    public void setFechaFinalVisible(boolean fechaFinalVisible) {
        this.fechaFinalVisible = fechaFinalVisible;
    }

    public List<TablaMaestra> getTablaMaestraRecords() {
        return tablaMaestraRecords;
    }

    public void setTablaMaestraRecords(List<TablaMaestra> tablaMaestraRecords) {
        this.tablaMaestraRecords = tablaMaestraRecords;
    }

    public TablaMaestraServicios getServiciosSelected() {
        return serviciosSelected;
    }

    public void setServiciosSelected(TablaMaestraServicios serviciosSelected) {
        this.serviciosSelected = serviciosSelected;
    }

    public TablaMaestra getSelectedTabla() {
        return selectedTabla;
    }

    public void setSelectedTabla(TablaMaestra selectedTabla) {
        this.selectedTabla = selectedTabla;
    }

    public List<TablaMaestra> getFilteredTablaMaestra() {
        return filteredTablaMaestra;
    }

    public void setFilteredTablaMaestra(List<TablaMaestra> filteredTablaMaestra) {
        this.filteredTablaMaestra = filteredTablaMaestra;
    }

    public List<TablaMaestraServicios> gettServiciosRecords() {
        return tServiciosRecords;
    }

    public void settServiciosRecords(List<TablaMaestraServicios> tServiciosRecords) {
        this.tServiciosRecords = tServiciosRecords;
    }

    public List<TablaMaestraServicios> getFilteredTServiciosRecords() {
        return filteredTServiciosRecords;
    }

    public void setFilteredTServiciosRecords(List<TablaMaestraServicios> filteredTServiciosRecords) {
        this.filteredTServiciosRecords = filteredTServiciosRecords;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public TablaMaestraService getTablaMaestraService() {
        return tablaMaestraService;
    }

    public void setTablaMaestraService(TablaMaestraService tablaMaestraService) {
        this.tablaMaestraService = tablaMaestraService;
    }

    public TablaMaestra getNuevaTabla() {
        return nuevaTabla;
    }

    public void setNuevaTabla(TablaMaestra nuevaTabla) {
        this.nuevaTabla = nuevaTabla;
    }

    public String getTipoCiclo() {
        return tipoCiclo;
    }

    public void setTipoCiclo(String tipoCiclo) {
        this.tipoCiclo = tipoCiclo;
    }

    public String getTipoIntervalo() {
        return tipoIntervalo;
    }

    public void setTipoIntervalo(String tipoIntervalo) {
        this.tipoIntervalo = tipoIntervalo;
    }
}
