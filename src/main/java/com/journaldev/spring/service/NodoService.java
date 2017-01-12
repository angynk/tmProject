package com.journaldev.spring.service;

import com.journaldev.hibernate.data.dao.NodoDao;
import com.journaldev.hibernate.data.entity.Nodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class NodoService {

    @Autowired
    NodoDao nodoDao;

    public void addNodo(Nodo nodo) { nodoDao.addNodo(nodo); }

    public void deleteNodo(Nodo nodo) { nodoDao.deleteNodo(nodo);}


    public void updateNodo(Nodo nodo) { nodoDao.updateNodo(nodo);}


    public List<Nodo> getNodosAll() { return  nodoDao.getNodosAll(); }
}
