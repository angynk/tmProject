package com.journaldev.spring.service;

import com.journaldev.hibernate.data.dao.TipoDiaDao;
import com.journaldev.hibernate.data.dao.TipoDiaDetalleDao;
import com.journaldev.hibernate.data.entity.TipoDia;
import com.journaldev.hibernate.data.entity.TipoDiaDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TipoDiaService {

    @Autowired
    TipoDiaDao tipoDiaDao;

    @Autowired
    TipoDiaDetalleDao tipoDiaDetalleDao;

    public void addTipoDia(TipoDia tipoDia) { tipoDiaDao.addTipoDia(tipoDia);    }

    public void deleteTipoDia(TipoDia tipoDia) {
        tipoDiaDao.deleteTipoDia(tipoDia);
    }

    public void updateTipoDia(TipoDia tipoDia) {
        tipoDiaDao.updateTipoDia(tipoDia);
    }

    public List<TipoDia> getTipoDiaAll() {  return tipoDiaDao.getTipoDiaAll(); }

    public void addTipoDiaDetalle(TipoDiaDetalle  tipoDiaDetalle) { tipoDiaDetalleDao.addTipoDiaDetalle(tipoDiaDetalle);    }

    public void deleteTipoDiaDetalle(TipoDiaDetalle  tipoDiaDetalle) {
        tipoDiaDetalleDao.deleteTipoDiaDetalle(tipoDiaDetalle);
    }

    public void updateTipoDiaDetalle(TipoDiaDetalle  tipoDiaDetalle) {
        tipoDiaDetalleDao.updateTipoDiaDetalle(tipoDiaDetalle);
    }

    public List<TipoDiaDetalle> getTipoDiaDetalleAll() {  return tipoDiaDetalleDao.getTipoDiaDetalleAll(); }
}
