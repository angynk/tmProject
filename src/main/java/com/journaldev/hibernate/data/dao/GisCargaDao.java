package com.journaldev.hibernate.data.dao;

import com.journaldev.hibernate.data.entity.GisCarga;
import com.journaldev.hibernate.data.entity.TablaMaestra;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GisCargaDao {


    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addGisCarga(GisCarga gisCarga) {
        getSessionFactory().getCurrentSession().save(gisCarga);
    }

    public void deleteGisCarga(GisCarga gisCarga) {
        getSessionFactory().getCurrentSession().delete(gisCarga);
    }


    public void updateGisCarga(GisCarga gisCarga) {
        getSessionFactory().getCurrentSession().update(gisCarga);
    }


    public List<GisCarga> getGisCargaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  GisCarga").list();
        return list;
    }
}
