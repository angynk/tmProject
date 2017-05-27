package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.GisCarga;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class MatrizDistanciaDao {


    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addMatrizDistancia(MatrizDistancia matrizDistancia) {
        Serializable save = getSessionFactory().getCurrentSession().save(matrizDistancia);

    }

    public void deleteMatrizDistancia(MatrizDistancia matrizDistancia) {
        getSessionFactory().getCurrentSession().delete(matrizDistancia);
    }


    public void updateMatrizDistancia(MatrizDistancia matrizDistancia) {
        getSessionFactory().getCurrentSession().update(matrizDistancia);
    }


    public List<MatrizDistancia> getMatrizDistanciaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  MatrizDistancia").list();
        return list;
    }

    public List<MatrizDistancia> getMatrizDistanciaByFecha(String tipoFecha,Date fecha){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(MatrizDistancia.class);
        criteria.add(Restrictions.eq(tipoFecha, fecha));
        return criteria.list();
    }

    public List<MatrizDistancia> getMatrizDistanciaBetwenFechas(String tipoFecha,Date fechaIni,Date fechaFin){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(MatrizDistancia.class);
        criteria.add(  Restrictions.between(tipoFecha, fechaIni, fechaFin)  );
        return criteria.list();
    }

    public MatrizDistancia getMatrizDistanciaById(String id){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(MatrizDistancia.class);
        criteria.add(Restrictions.eq("numeracion", id));
        return (MatrizDistancia) criteria.uniqueResult();
    }

    public void addMatrizTemporalByFile(String filename){
        SessionFactory factory = getSessionFactory();
        Session session = factory.getCurrentSession();
        SessionImplementor sessImpl = (SessionImplementor) session;
        Connection conn = null;
        conn = sessImpl.getJDBCContext().connection();
        CopyManager copyManager = null;
        try {
            copyManager = new CopyManager((BaseConnection) conn);
            FileReader fileReader = new FileReader(filename);
            copyManager.copyIn("COPY temp_matriz_distancia (ruta,macro,linea,seccion,nodo,abscisa,nombre) from  STDIN DELIMITER ';' CSV HEADER encoding 'windows-1251'", fileReader );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteMatrizTemporal(){
        getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM temp_matriz_distancia").executeUpdate();
    }
}
