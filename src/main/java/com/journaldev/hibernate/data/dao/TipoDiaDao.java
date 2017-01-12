package com.journaldev.hibernate.data.dao;

import com.journaldev.hibernate.data.entity.TipoDia;
import com.journaldev.hibernate.data.entity.Trayecto;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TipoDiaDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addTipoDia(TipoDia tipoDia) {
        getSessionFactory().getCurrentSession().save(tipoDia);
    }

    public void deleteTipoDia(TipoDia tipoDia) {
        getSessionFactory().getCurrentSession().delete(tipoDia);
    }


    public void updateTipoDia(TipoDia tipoDia) {
        getSessionFactory().getCurrentSession().update(tipoDia);
    }


    public List<TipoDia> getTipoDiaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  TipoDia").list();
        return list;
    }
}
