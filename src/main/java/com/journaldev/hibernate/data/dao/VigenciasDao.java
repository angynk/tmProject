package com.journaldev.hibernate.data.dao;


import com.journaldev.hibernate.data.Nodos;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VigenciasDao {

    @Autowired
    private SessionFactory sessionFactorySql;


    public SessionFactory getSessionFactory() {
        return sessionFactorySql;
    }

    public void setSessionFactory(SessionFactory sessionFactorySql) {
        this.sessionFactorySql = sessionFactorySql;
    }

    public List<Nodos> getCustomers() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  Nodos").list();
        return list;
    }
}
