package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.HorariosProvisionalServicio;
import com.tmModulos.controlador.servicios.TablaHorarioService;
import com.tmModulos.controlador.servicios.TipoDiaService;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.entity.saeBogota.Horario;
import com.tmModulos.modelo.entity.saeBogota.Vigencias;
import com.tmModulos.modelo.entity.tmData.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Tie;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Service("IntervalosProcessor")
public class IntervalosProcessor {


    @Autowired
    private TablaHorarioService tablaHorarioService;

    @Autowired
    private HorariosProvisionalServicio horariosProvisionalServicio;

    @Autowired
    private TipoDiaService tipoDiaService;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    TipoFranja franjaIncio ;
    TipoFranja franjaPicoAM;
    TipoFranja franjaValle;
    TipoFranja franjaPicoPM;
    TipoFranja franjaCierre;
    List<IntervalosProgramacion> intervalosFranjaInicio;
    List<IntervalosProgramacion> intervalosFranjaPicoAM;
    List<IntervalosProgramacion> intervalosFranjaValle;
    List<IntervalosProgramacion> intervalosFranjaPicoPM;
    List<IntervalosProgramacion> intervalosFranjaCierre;



    public GisIntervalos generarIntervalos(Date fechaVigencia, String descripcion, String tipoDia, TablaMaestra tablaMaestra) {
        GisIntervalos gisIntervalos = null;
        // Traer valor cuadro por fecha de vigencia
        List<Vigencias> vigencias = tablaHorarioService.getVigenciasDaoByDate(fechaVigencia);
        if(vigencias.size()>0){

        // Traer servicios disponibles por tipo Dia
            TipoDia dia = tipoDiaService.getTipoDia(tipoDia);
            List<ServicioTipoDia> serviciosTipoDia = horariosProvisionalServicio.getServiciosByTipoDia(dia);

            // Crear Gis intervalos
            gisIntervalos = new GisIntervalos(new Date(),fechaVigencia,descripcion,vigencias.get(0).getTipoDia(),dia,tablaMaestra);
            horariosProvisionalServicio.addGisIntervalo(gisIntervalos);


            long time=System.currentTimeMillis();
            List<Horario> tablaHorario = tablaHorarioService.getHorarioByDate(vigencias.get(0).getTipoDia());
            procesarInformacionTablaHorario(tablaHorario,gisIntervalos,serviciosTipoDia);
            //Recorrer servicios
//            for (ServicioTipoDia servicio: serviciosTipoDia ) {
//                System.out.println("Servicio No."+servicio.getId());
//
//                String [] valoresId = servicio.getIdentificador().split("-");
//                List<Horario> tablaHorario = tablaHorarioService.getHorarioByDateIndentificador(vigencias.get(0).getTipoDia(),
//                        Integer.parseInt(valoresId[0]),
//                        Integer.parseInt(valoresId[1]),
//                                Integer.parseInt(valoresId[2]),
//                                Integer.parseInt(valoresId[3]));
//                procesarInformacionTablaHorario(tablaHorario,gisIntervalos,servicio);
//
//            }

         //   calcularValorIntervaloPorFranja(vigencias.get(0).getTipoDia(),serviciosTipoDia,gisIntervalos);

            System.out.println("Tiempo Total: "+(getTime((int) (System.currentTimeMillis()-time))));

          // GisIntervalos gisIntervalos = new GisIntervalos(new Date(),fechaVigencia,descripcion,vigencias.get(0).getTipoDia());
          //  GisIntervalos gisIntervalos = horariosProvisionalServicio.getGisIntervalosAll().get(0);
          // horariosProvisionalServicio.addGisIntervalo(gisIntervalos);
          //  List<Horario> tablaHorario = tablaHorarioService.getHorarioByDate(vigencias.get(0).getTipoDia());
          //  procesarInformacionTablaHorario(tablaHorario,gisIntervalos);
        }
        return gisIntervalos;
    }

    private void procesarInformacionTablaHorario(List<Horario> tablaHorario,GisIntervalos gisIntervalos,List<ServicioTipoDia> servicioTipoDia) {

       extraerDiferenciaIntervalos(tablaHorario, servicioTipoDia, gisIntervalos);



    }

    public void precalcularIntervalosProgramacion(){
        franjaIncio = horariosProvisionalServicio.getTipoFranjaByNombre("Inicio");
        franjaPicoAM = horariosProvisionalServicio.getTipoFranjaByNombre("Pico AM");
        franjaValle = horariosProvisionalServicio.getTipoFranjaByNombre("Valle");
        franjaPicoPM = horariosProvisionalServicio.getTipoFranjaByNombre("Pico PM");
        franjaCierre = horariosProvisionalServicio.getTipoFranjaByNombre("Cierre");

        intervalosFranjaInicio= horariosProvisionalServicio.getIntervaloByFranja(franjaIncio);
        intervalosFranjaPicoAM= horariosProvisionalServicio.getIntervaloByFranja(franjaPicoAM);
        intervalosFranjaValle= horariosProvisionalServicio.getIntervaloByFranja(franjaValle);
        intervalosFranjaPicoPM= horariosProvisionalServicio.getIntervaloByFranja(franjaPicoPM);
        intervalosFranjaCierre= horariosProvisionalServicio.getIntervaloByFranja(franjaCierre);

    }

    public void calcularValorIntervaloPorFranja(TablaMaestraServicios tablaMaestraServicios,ServicioTipoDia servicio,GisIntervalos gisIntervalos) {

               List<TiempoIntervalos> tiemposFranjaInciio = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaInicio,servicio,gisIntervalos);
               List<TiempoIntervalos> tiemposFranjaAM = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaPicoAM,servicio,gisIntervalos);
               List<TiempoIntervalos> tiemposFranjaValle = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaValle,servicio,gisIntervalos);
               List<TiempoIntervalos> tiemposFranjaPM = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaPicoPM,servicio,gisIntervalos);
               List<TiempoIntervalos> tiemposFranjaCierre = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaCierre,servicio,gisIntervalos);

               calcularPromedio(servicio,tiemposFranjaInciio,tiemposFranjaAM,tiemposFranjaValle,tiemposFranjaCierre,tiemposFranjaPM,tablaMaestraServicios);



    }

    private void calcularPromedio(ServicioTipoDia id, List<TiempoIntervalos> tiemposFranjaInciio, List<TiempoIntervalos> tiemposFranjaAM, List<TiempoIntervalos> tiemposFranjaValle, List<TiempoIntervalos> tiemposFranjaCierre, List<TiempoIntervalos> tiemposFranjaPM,TablaMaestraServicios tservicios) {
        double promedioInicio = promedio(tiemposFranjaInciio);
        double promedioPicoAm = promedio(tiemposFranjaAM);
        double promedioValle = promedio(tiemposFranjaValle);
        double promedioPicoPM = promedio(tiemposFranjaPM);
        double promedioCierre = promedio(tiemposFranjaCierre);
        Intervalos intervalos = new Intervalos(ProcessorUtils.CALCULO_PROMEDIO,promedioInicio,promedioPicoAm,promedioValle,promedioPicoPM,promedioCierre,id,tservicios);
        horariosProvisionalServicio.addIntervalos(intervalos);

    }

    private double promedio(List<TiempoIntervalos> tiempos) {
        double tiempoTotal=0;
        int i=0;
        for (TiempoIntervalos tiempoInt:tiempos ) {
          tiempoTotal= tiempoTotal+ tiempoInt.getInstante();
            i++;
        }
        if(tiempoTotal!=0){
            tiempoTotal= tiempoTotal/i;
           return transformarAFormatoTiempo(tiempoTotal);
        }


        return tiempoTotal;
    }

    private double transformarAFormatoTiempo(double tiempoTotal) {
        Time time = getTime((int) tiempoTotal);
        SimpleDateFormat format = new SimpleDateFormat("mm.ss");
        String date = format.format(new Date(time.getTime()));
        if (date.split("")[0].equals("0")) {
            String [] fecha = date.split("");
            date = fecha[1]+fecha[2]+fecha[3]+fecha[4];
        }
        return Double.parseDouble(date);
    }



    private List<TiempoIntervalos> extraerDiferenciaIntervalos(List<Horario> tablaHorario,List<ServicioTipoDia> servicioTipoDia,GisIntervalos gisIntervalos) {
        List<TiempoIntervalos> tiempoIntervalosLista = new ArrayList<>();
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
                    ServicioTipoDia  servicio = getServicioById(servicioTipoDia,macroB+"-"+lineaB+"-"+seccionB+"-"+puntoB);
                    if(servicio!=null){
                        int diferencia = calcularDiferencia(horarioA.getInstante(),horarioB.getInstante());
                        TiempoIntervalos tiempoIntervalos = new TiempoIntervalos(getTime(diferencia),servicio,intervaloB,diferencia,gisIntervalos);
                       if(tiempoIntervalosLista.size()<200){
                           tiempoIntervalosLista.add(tiempoIntervalos);
                       }else{
                           taskExecutor.execute(new IntervalosHilo(tiempoIntervalosLista));
                           tiempoIntervalosLista = new ArrayList<>();
                       }


                    }
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
        return tiempoIntervalosLista;
    }

    private ServicioTipoDia getServicioById(List<ServicioTipoDia> servicioTipoDia, String id) {
        for (ServicioTipoDia servicio: servicioTipoDia ) {
            if(servicio.getIdentificador().equals(id)){
                return servicio;
            }
        }
        return null;
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





    public TablaHorarioService getTablaHorarioService() {
        return tablaHorarioService;
    }

    public void setTablaHorarioService(TablaHorarioService tablaHorarioService) {
        this.tablaHorarioService = tablaHorarioService;
    }
}
