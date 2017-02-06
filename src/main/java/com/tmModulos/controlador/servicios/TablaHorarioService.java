package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.saeBogota.HorarioDao;
import com.tmModulos.modelo.dao.saeBogota.VigenciasDao;
import com.tmModulos.modelo.entity.saeBogota.Horario;
import com.tmModulos.modelo.entity.saeBogota.Vigencias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("TablaHorarioService")
@Transactional(readOnly = true)
public class TablaHorarioService {

    @Autowired
    VigenciasDao vigenciasDao;

    @Autowired
    HorarioDao horarioDao;

    public List<Vigencias> getVigenciasDaoByDate(Date date) {
        return vigenciasDao.getVigenciasDaoByDate( date );
    }

    public List<Horario> getHorarioByDate(String date) { return horarioDao.getHorarioByDate(date);}

    public List<Horario> getHorarioByDateIndentificador(String date,int macro, int linea, int seccion, int punto) {
        return horarioDao.getHorarioByDateIndentificador(date,macro,linea,seccion,punto);
    }
}
