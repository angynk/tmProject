package com.journaldev.spring.service;

import com.journaldev.hibernate.data.dao.GisCargaDao;
import com.journaldev.hibernate.data.dao.TrayectoDao;
import com.journaldev.hibernate.data.entity.GisCarga;
import com.journaldev.hibernate.data.entity.Trayecto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GisCargaService {

    @Autowired
    GisCargaDao gisCargaDao;

    @Autowired
    TrayectoDao trayectoDao;

    public void addGisCarga(GisCarga gisCarga) {
        gisCargaDao.addGisCarga(gisCarga);
    }

    public void deleteGisCarga(GisCarga gisCarga) {
        gisCargaDao.deleteGisCarga(gisCarga);
    }

    public void updateGisCarga(GisCarga gisCarga) {
        gisCargaDao.updateGisCarga(gisCarga);
    }

    public List<GisCarga> getGisCargaAll() {
        return gisCargaDao.getGisCargaAll();
    }

    public void addTrayecto(Trayecto trayecto) {
        trayectoDao.addTrayecto(trayecto);
    }

    public void deleteTrayecto(Trayecto trayecto) {
        trayectoDao.deleteTrayecto(trayecto);
    }

    public void updateTrayecto(Trayecto trayecto) {
        trayectoDao.updateTrayecto(trayecto);
    }

    public List<Trayecto> getTrayectoAll() {
        return trayectoDao.getTrayectoAll();
    }
}
