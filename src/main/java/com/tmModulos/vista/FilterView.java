package com.tmModulos.vista;

import com.tmModulos.modelo.TipoDia;
import com.tmModulos.controlador.servicios.ServicioService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

@ManagedBean(name="dtFilterView")
@ViewScoped
public class FilterView implements Serializable {





    @ManagedProperty("#{servicioService}")
    private ServicioService service;

    @PostConstruct
    public void init() {
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

    public void setService(ServicioService service) {
        this.service = service;
    }

    public ServicioService getService() {
        return service;
    }
}