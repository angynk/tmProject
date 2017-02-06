package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.ServicioTipoDia;
import com.tmModulos.modelo.entity.tmData.TipoDia;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServicioTipoDiaDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<ServicioTipoDia> getServiciosByTipoDia(TipoDia tipoDia){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ServicioTipoDia.class);
        criteria.add(Restrictions.eq("tipoDia", tipoDia));
       return criteria.list();
    }

}
