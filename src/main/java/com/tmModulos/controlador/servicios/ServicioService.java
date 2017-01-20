package com.tmModulos.controlador.servicios;


import com.tmModulos.modelo.TipoDia;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.*;

@ManagedBean(name = "servicioService")
@ApplicationScoped
public class ServicioService {

    @ManagedProperty("#{TablaMaestraService}")
    private TablaMaestraService tablaMaestraService;

    @ManagedProperty("#{DistanciaNodosService}")
    private DistanciaNodosService distanciaNodosService;


    private final static TipoDia[] tipoDia;

//    private final static String[] tipologia;

    static {
        tipoDia = new TipoDia[3];
        tipoDia[0] = TipoDia.HABIL;
        tipoDia[1] = TipoDia.SABADO;
        tipoDia[2] = TipoDia.FESTIVO;

//        brands = new String[10];
//        brands[0] = "BMW";
//        brands[1] = "Mercedes";
//        brands[2] = "Volvo";
//        brands[3] = "Audi";
//        brands[4] = "Renault";
//        brands[5] = "Fiat";
//        brands[6] = "Volkswagen";
//        brands[7] = "Honda";
//        brands[8] = "Jaguar";
//        brands[9] = "Ford";
    }



    private String getRandomId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private int getRandomYear() {
        return (int) (Math.random() * 50 + 1960);
    }

    private TipoDia getRandomColor() {
        return tipoDia[(int) (Math.random() * 3)];
    }

//    private String getRandomBrand() {
//        return brands[(int) (Math.random() * 10)];
//    }

    public int  getRandomNumber() {
        return (int) (Math.random() * 10);
    }

    public boolean getRandomSoldState() {
        return (Math.random() > 0.5) ? true: false;
    }

    public List<TipoDia> getTipoDia() {
        return Arrays.asList(tipoDia);
    }

    public void test() {
//        TablaMaestra tablaMaestra= new TablaMaestra();
//        tablaMaestra.setEstaVigente(true);
//        tablaMaestra.setVersion(1);
//        tablaMaestra.setFechaCreacion(new Date());
//        tablaMaestra.setId(1);
//        tablaMaestraService.addCustomer(tablaMaestra);

//        List<TablaMaestra> customers = tablaMaestraService.getCustomers();
//        customers.size();
        // tablaMaestraService.getVigenciasDao().size();
//        List<NodosSeccion> lineasByMacroAndLinea = distanciaNodosService.getNodosSeccionesByMacroLineaAndConfig(9, 30,17);
//        System.out.println(lineasByMacroAndLinea.size());
    }

    public TablaMaestraService getTablaMaestraService() {
        return tablaMaestraService;
    }

    public void setTablaMaestraService(TablaMaestraService tablaMaestraService) {
        this.tablaMaestraService = tablaMaestraService;
    }

    public DistanciaNodosService getDistanciaNodosService() {
        return distanciaNodosService;
    }

    public void setDistanciaNodosService(DistanciaNodosService distanciaNodosService) {
        this.distanciaNodosService = distanciaNodosService;
    }
}