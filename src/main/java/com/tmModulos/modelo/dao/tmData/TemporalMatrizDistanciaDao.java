package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import com.tmModulos.modelo.entity.tmData.TemporalMatrizDistancia;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class TemporalMatrizDistanciaDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<TemporalMatrizDistancia> getMatrizDistanciaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  TemporalMatrizDistancia ").list();
        return list;
    }

}
