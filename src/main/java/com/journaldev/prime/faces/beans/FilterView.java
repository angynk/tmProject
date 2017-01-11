package com.journaldev.prime.faces.beans;

import com.journaldev.hibernate.data.Servicio;
import com.journaldev.hibernate.data.TipoDia;
import com.journaldev.spring.service.ServicioService;
import com.journaldev.spring.service.TablaMaestraService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="dtFilterView")
@ViewScoped
public class FilterView implements Serializable {

    private List<Servicio> servicios;

    private List<Servicio> filteredServicios;



    @ManagedProperty("#{servicioService}")
    private ServicioService service;

    @PostConstruct
    public void init() {
        servicios = service.createServicio(10);
       service.test();

    }

    public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }

        if(value == null) {
            return false;
        }

        return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
    }

//    public List<String> getBrands() {
//        return service.getBrands();
//    }

    public List<TipoDia> getTipoDia() {
        return service.getTipoDia();
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public List<Servicio> getFilteredServicios() {
        return filteredServicios;
    }

    public void setFilteredServicios(List<Servicio> filteredServicios) {
        this.filteredServicios = filteredServicios;
    }

    public void setService(ServicioService service) {
        this.service = service;
    }


}