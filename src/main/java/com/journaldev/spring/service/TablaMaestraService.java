package com.journaldev.spring.service;

import com.journaldev.hibernate.data.Employee;
import com.journaldev.hibernate.data.TablaMaestra;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TablaMaestraService {

    @Autowired
    private SessionFactory sessionFactoryPostgresSql;

    public SessionFactory getSessionFactory() {
        return sessionFactoryPostgresSql;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactoryPostgresSql = sessionFactory;
    }

    @Transactional
    public int find(){
        // Acquire session
        Session session = sessionFactoryPostgresSql.getCurrentSession();
        // Save employee, saving behavior get done in a transactional manner
        List<TablaMaestra> list = session.createCriteria(TablaMaestra.class).list();
       return list.size();
    }
}
