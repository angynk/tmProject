package com.journaldev.spring.service;


import com.journaldev.hibernate.data.Servicio;
import com.journaldev.hibernate.data.entity.TablaMaestra;
import com.journaldev.hibernate.data.TipoDia;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ManagedBean(name = "servicioService")
@ApplicationScoped
public class ServicioService {

    @ManagedProperty("#{TablaMaestraService}")
    private TablaMaestraService tablaMaestraService;

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

    public List<Servicio> createServicio(int size) {
        List<Servicio> list = new ArrayList<Servicio>();
        for(int i = 0 ; i < size ; i++) {
            list.add(new Servicio(getRandomId(), getRandomColor(), getRandomId(), getRandomNumber(), getRandomNumber(), getRandomNumber()));
        }

        return list;
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

        List<TablaMaestra> customers = tablaMaestraService.getCustomers();
        customers.size();

       // tablaMaestraService.getVigenciasDao().size();
    }


    public void setTablaMaestraService(TablaMaestraService tablaMaestraService) {
        this.tablaMaestraService = tablaMaestraService;
    }
}