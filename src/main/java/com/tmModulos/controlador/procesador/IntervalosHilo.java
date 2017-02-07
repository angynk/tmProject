package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.HorariosProvisionalServicio;
import com.tmModulos.controlador.servicios.TablaHorarioService;
import com.tmModulos.modelo.entity.saeBogota.Horario;
import com.tmModulos.modelo.entity.tmData.GisIntervalos;
import com.tmModulos.modelo.entity.tmData.IntervalosProgramacion;
import com.tmModulos.modelo.entity.tmData.ServicioTipoDia;
import com.tmModulos.modelo.entity.tmData.TiempoIntervalos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.List;

@Component
@Scope("prototype")
public class IntervalosHilo  implements Runnable{

    ServicioTipoDia servicio;
    String cuadro;
    GisIntervalos gisIntervalos;

    @Autowired
    private TablaHorarioService tablaHorarioService;

    @Autowired
    private HorariosProvisionalServicio horariosProvisionalServicio;


    public IntervalosHilo( ServicioTipoDia servicio, String cuadro,GisIntervalos gisIntervalos) {
        this.servicio = servicio;
        this.cuadro=cuadro;
        this.gisIntervalos =gisIntervalos;
    }

    public ServicioTipoDia getServicio() {
        return servicio;
    }

    public void setServicio(ServicioTipoDia servicio) {
        this.servicio = servicio;
    }

    public String getCuadro() {
        return cuadro;
    }

    public void setCuadro(String cuadro) {
        this.cuadro = cuadro;
    }

    public GisIntervalos getGisIntervalos() {
        return gisIntervalos;
    }

    public void setGisIntervalos(GisIntervalos gisIntervalos) {
        this.gisIntervalos = gisIntervalos;
    }

    @Override
    public void run() {

        System.out.println(servicio.getIdentificador() + "  is running");
        String [] valoresId = servicio.getIdentificador().split("-");
        List<Horario> tablaHorario = tablaHorarioService.getHorarioByDateIndentificador(cuadro,
                Integer.parseInt(valoresId[0]),
                Integer.parseInt(valoresId[1]),
                Integer.parseInt(valoresId[2]),
                Integer.parseInt(valoresId[3]));
        procesarInformacionTablaHorario(tablaHorario,gisIntervalos,servicio);


    }

    private void procesarInformacionTablaHorario(List<Horario> tablaHorario, GisIntervalos gisIntervalos, ServicioTipoDia servicioTipoDia) {

        //  copiarInformacionATablaProvisional(tablaHorario);
        extraerDiferenciaIntervalos(tablaHorario,servicioTipoDia,gisIntervalos.getCuadro());


    }

    private void extraerDiferenciaIntervalos(List<Horario> tablaHorario,ServicioTipoDia servicioTipoDia,String cuadr) {
        String cuadro = tablaHorario.get(0).getCuadro();
        Horario horarioA= tablaHorario.get(0);
        Horario horarioB= null;
        int macroA= horarioA.getMacro();
        int lineaA=horarioA.getLinea();
        int seccionA=horarioA.getSeccion();
        int puntoA=horarioA.getPunto();
        Time horaA= getTime(horarioA.getInstante());
        IntervalosProgramacion intervaloA= calcularIntervaloInstante(horaA);
        int macroB,lineaB,seccionB,puntoB;
        Time horaB;
        IntervalosProgramacion intervaloB=null;
        int aux=0;
        for (int i=1;i<tablaHorario.size();i++) {
            horarioB = tablaHorario.get(i);
            macroB=horarioB.getMacro();
            lineaB=horarioB.getLinea();
            seccionB=horarioB.getSeccion();
            puntoB=horarioB.getPunto();
            horaB=getTime(horarioB.getInstante());
            intervaloB=calcularIntervaloInstante(horaB);

            if( macroA==macroB && lineaA==lineaB && seccionA==seccionB && puntoA==puntoB){
                if(intervaloA.getId() == intervaloB.getId() ){
                    int diferencia = calcularDiferencia(horarioA.getInstante(),horarioB.getInstante());
                    TiempoIntervalos tiempoIntervalos = new TiempoIntervalos(getTime(diferencia),servicioTipoDia,intervaloB,diferencia,cuadro);
                    horariosProvisionalServicio.addTiempoIntervalos(tiempoIntervalos);
                    aux++;
                }else{
                    horarioA=horarioB;
                    macroA=macroB;
                    lineaA=lineaB;
                    seccionA=seccionB;
                    puntoA=puntoB;
                    horaA=horaB;
                    intervaloA=intervaloB;
                    aux=0;
                }
            }else{
                horarioA=horarioB;
                macroA=macroB;
                lineaA=lineaB;
                seccionA=seccionB;
                puntoA=puntoB;
                horaA=horaB;
                intervaloB=intervaloA;
                aux=0;
            }
        }
    }

    private Time getTime(int instante) {
        int hor = instante / 3600;
        int min=(instante-(3600*hor))/60;
        int seg=instante-((hor*3600)+(min*60));
        Time time= new Time(hor,min,seg);
        return time;
    }

    private int calcularDiferencia(int horaA, int horaB) {
        int diferencia= horaB-horaA;
        return diferencia;
    }

    private IntervalosProgramacion calcularIntervaloInstante(Time time){
        IntervalosProgramacion intervalosProgramacion = horariosProvisionalServicio.getIntervaloForDate(time);
        return intervalosProgramacion;
    }

}
