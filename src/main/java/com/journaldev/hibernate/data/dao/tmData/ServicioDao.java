package com.journaldev.hibernate.data.dao.tmData;

import com.journaldev.hibernate.data.entity.Nodo;
import com.journaldev.hibernate.data.entity.tmData.MatrizDistancia;
import com.journaldev.hibernate.data.entity.tmData.Servicio;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ServicioDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addServicio(Servicio servicio) {
        Serializable save = getSessionFactory().getCurrentSession().save(servicio);

    }

    public void deleteServicio(Servicio servicio) {
        getSessionFactory().getCurrentSession().delete(servicio);
    }


    public void updateServicio(Servicio servicio) {
        getSessionFactory().getCurrentSession().update(servicio);
    }


    public List<Servicio> getServicioAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  Servicio ").list();
        return list;
    }

    public Servicio getServicioBymacroLineaYseccion(int macro,int linea,int seccion){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Servicio.class);
        criteria.add(Restrictions.eq("macro", macro));
        criteria.add(Restrictions.eq("linea", linea));
        criteria.add(Restrictions.eq("seccion", seccion));
        return (Servicio) criteria.uniqueResult();
    }
}