package com.journaldev.hibernate.data.dao;

import com.journaldev.hibernate.data.entity.GisCarga;
import com.journaldev.hibernate.data.entity.Trayecto;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrayectoDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addTrayecto(Trayecto trayecto) {
        getSessionFactory().getCurrentSession().save(trayecto);
    }

    public void deleteTrayecto(Trayecto trayecto) {
        getSessionFactory().getCurrentSession().delete(trayecto);
    }


    public void updateTrayecto(Trayecto trayecto) {
        getSessionFactory().getCurrentSession().update(trayecto);
    }


    public List<Trayecto> getTrayectoAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  Trayecto").list();
        return list;
    }
}
