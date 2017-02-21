package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Nodo;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NodoDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addNodo(Nodo nodo) {
        getSessionFactory().getCurrentSession().save(nodo);
    }

    public void deleteNodo(Nodo nodo) {
        getSessionFactory().getCurrentSession().delete(nodo);
    }


    public void updateNodo(Nodo nodo) {
        getSessionFactory().getCurrentSession().update(nodo);
    }


    public List<Nodo> getNodosAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  Nodo").list();
        return list;
    }

    public List<Nodo> getNodo(String nombre){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Nodo.class);
        criteria.add(Restrictions.eq("nombre", nombre));
        return criteria.list();
    }

    public Nodo getNodoByCodigo(int codigo){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Nodo.class);
        criteria.add(Restrictions.eq("codigo", codigo));
        Nodo nodo =null;
        try{
           nodo = (Nodo) criteria.uniqueResult();
        }catch (Exception e){
            System.out.println();
        }
        return (Nodo) criteria.uniqueResult();
    }
}
