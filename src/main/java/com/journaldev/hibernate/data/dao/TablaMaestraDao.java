package com.journaldev.hibernate.data.dao;

import com.journaldev.hibernate.data.entity.TablaMaestra;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TablaMaestraDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addCustomer(TablaMaestra customer) {
        getSessionFactory().getCurrentSession().save(customer);
    }

    public void deleteCustomer(TablaMaestra customer) {
        getSessionFactory().getCurrentSession().delete(customer);
    }


    public void updateCustomer(TablaMaestra customer) {
        getSessionFactory().getCurrentSession().update(customer);
    }


    public TablaMaestra getCustomerById(int id) {
        List list = getSessionFactory().getCurrentSession()
                .createQuery("from Customer  where id=?")
                .setParameter(0, id).list();
        return (TablaMaestra)list.get(0);
    }

    public List<TablaMaestra> getCustomers() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  TablaMaestra").list();
        return list;
    }

}
