package com.journaldev.hibernate.data.dao.saeBogota;

import com.journaldev.hibernate.data.entity.saeBogota.NodosSeccion;
import com.journaldev.hibernate.data.entity.saeBogota.Secciones;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class NodosSeccionDao implements Serializable {

    private SessionFactory sessionFactoryServer = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    Session session = null;
    Transaction transaction = null;
    Query query = null;;

    public SessionFactory getSessionFactoryServer() {
        return sessionFactoryServer;
    }

    public void setSessionFactoryServer(SessionFactory sessionFactoryServer) {
        this.sessionFactoryServer = sessionFactoryServer;
    }

    public List<NodosSeccion> getNodosSeccionesByMacroLineaAndConfig(int macro, int linea, int config,int tipoNodo) {
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(NodosSeccion.class);
        criteria.add(Restrictions.eq("macro",macro));
        criteria.add(Restrictions.eq("linea",linea));
        criteria.add(Restrictions.eq("configLinea",config));
        criteria.add(Restrictions.eq("tipo",tipoNodo));
        List list = criteria.list();
        session.close();
        return list;
    }
}
