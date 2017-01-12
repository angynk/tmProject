package com.journaldev.spring.service;


import com.journaldev.hibernate.data.Nodos;
import com.journaldev.hibernate.data.TablaMaestra;
import com.journaldev.hibernate.data.dao.TablaMaestraDao;
import com.journaldev.hibernate.data.dao.VigenciasDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("TablaMaestraService")
@Transactional(readOnly = true)
public class TablaMaestraService {


    @Autowired
    TablaMaestraDao tablaMaestraDao;

    @Autowired
    VigenciasDao vigenciasDao;

    @Transactional(readOnly = false)
    public void addCustomer(TablaMaestra tablaMaestra) {
        getTablaMaestraDao().addCustomer(tablaMaestra);
    }

    @Transactional(readOnly = false)
    public void deleteCustomer(TablaMaestra customer) {
        getTablaMaestraDao().deleteCustomer(customer);
    }

    @Transactional(readOnly = false)
    public void updateCustomer(TablaMaestra customer) {
        getTablaMaestraDao().updateCustomer(customer);
    }

    public TablaMaestra getCustomerById(int id) {
        return getTablaMaestraDao().getCustomerById(id);
    }

    @Transactional(readOnly = false)
    public List<TablaMaestra> getCustomers() {
        return getTablaMaestraDao().getCustomers();
    }

    public TablaMaestraDao getTablaMaestraDao() {
        return tablaMaestraDao;
    }

    public void setTablaMaestraDao(TablaMaestraDao tablaMaestraDao) {
        this.tablaMaestraDao = tablaMaestraDao;
    }

    public List<Nodos> getVigenciasDao(){
        return vigenciasDao.getCustomers();
    }

}
