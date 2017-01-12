package com.journaldev.hibernate.data.dao;

import com.journaldev.hibernate.data.entity.GisCarga;
import com.journaldev.hibernate.data.entity.Trayecto;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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

    public List<Trayecto> getTrayectoByIdentifier(String trayectoId){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Trayecto.class);
        criteria.add(Restrictions.eq("trayecto", trayectoId));
        return criteria.list();
    }
}
