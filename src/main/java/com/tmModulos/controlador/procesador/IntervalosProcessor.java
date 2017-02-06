package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.HorariosProvisionalServicio;
import com.tmModulos.controlador.servicios.TablaHorarioService;
import com.tmModulos.controlador.servicios.TipoDiaService;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.entity.saeBogota.Horario;
import com.tmModulos.modelo.entity.saeBogota.Vigencias;
import com.tmModulos.modelo.entity.tmData.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("IntervalosProcessor")
public class IntervalosProcessor {


    @Autowired
    private TablaHorarioService tablaHorarioService;

    @Autowired
    private HorariosProvisionalServicio horariosProvisionalServicio;

    @Autowired
    private TipoDiaService tipoDiaService;



    public void generarIntervalos(Date fechaVigencia, String descripcion, String tipoDia) {

        // Traer valor cuadro por fecha de vigencia
        List<Vigencias> vigencias = tablaHorarioService.getVigenciasDaoByDate(fechaVigencia);
        if(vigencias.size()>0){

        // Traer servicios disponibles por tipo Dia
            TipoDia dia = tipoDiaService.getTipoDia(tipoDia);
            List<ServicioTipoDia> serviciosTipoDia = horariosProvisionalServicio.getServiciosByTipoDia(dia);

            // Crear Gis intervalos
            GisIntervalos gisIntervalos = new GisIntervalos(new Date(),fechaVigencia,descripcion,vigencias.get(0).getTipoDia());
            horariosProvisionalServicio.addGisIntervalo(gisIntervalos);

            //Recorrer servicios
            for (ServicioTipoDia servicio: serviciosTipoDia ) {
                String [] valoresId = servicio.getIdentificador().split("-");
                List<Horario> tablaHorario = tablaHorarioService.getHorarioByDateIndentificador(vigencias.get(0).getTipoDia(),
                        Integer.parseInt(valoresId[0]),
                        Integer.parseInt(valoresId[1]),
                                Integer.parseInt(valoresId[2]),
                                Integer.parseInt(valoresId[3]));
                procesarInformacionTablaHorario(tablaHorario,gisIntervalos,servicio);
                break;

            }

            calcularValorIntervaloPorFranja(vigencias.get(0).getTipoDia(),serviciosTipoDia,gisIntervalos);

          // GisIntervalos gisIntervalos = new GisIntervalos(new Date(),fechaVigencia,descripcion,vigencias.get(0).getTipoDia());
          //  GisIntervalos gisIntervalos = horariosProvisionalServicio.getGisIntervalosAll().get(0);
          // horariosProvisionalServicio.addGisIntervalo(gisIntervalos);
          //  List<Horario> tablaHorario = tablaHorarioService.getHorarioByDate(vigencias.get(0).getTipoDia());
          //  procesarInformacionTablaHorario(tablaHorario,gisIntervalos);
        }

    }

    private void procesarInformacionTablaHorario(List<Horario> tablaHorario,GisIntervalos gisIntervalos,ServicioTipoDia servicioTipoDia) {

      //  copiarInformacionATablaProvisional(tablaHorario);
       extraerDiferenciaIntervalos(tablaHorario,servicioTipoDia,gisIntervalos.getCuadro());


    }

    private void calcularValorIntervaloPorFranja(String cuadro,List<ServicioTipoDia> servicioTipoDia,GisIntervalos gisIntervalos) {

        TipoFranja franjaIncio = horariosProvisionalServicio.getTipoFranjaByNombre("Inicio");
        TipoFranja franjaPicoAM = horariosProvisionalServicio.getTipoFranjaByNombre("Pico AM");
        TipoFranja franjaValle = horariosProvisionalServicio.getTipoFranjaByNombre("Valle");
        TipoFranja franjaPicoPM = horariosProvisionalServicio.getTipoFranjaByNombre("Pico PM");
        TipoFranja franjaCierre = horariosProvisionalServicio.getTipoFranjaByNombre("Cierre");

        List<IntervalosProgramacion> intervalosFranjaInicio= horariosProvisionalServicio.getIntervaloByFranja(franjaIncio);
        List<IntervalosProgramacion> intervalosFranjaPicoAM= horariosProvisionalServicio.getIntervaloByFranja(franjaPicoAM);
        List<IntervalosProgramacion> intervalosFranjaValle= horariosProvisionalServicio.getIntervaloByFranja(franjaValle);
        List<IntervalosProgramacion> intervalosFranjaPicoPM= horariosProvisionalServicio.getIntervaloByFranja(franjaPicoPM);
        List<IntervalosProgramacion> intervalosFranjaCierre= horariosProvisionalServicio.getIntervaloByFranja(franjaCierre);

        for (ServicioTipoDia servicio: servicioTipoDia ) {
               List<TiempoIntervalos> tiemposFranjaInciio = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaInicio,servicio);
               List<TiempoIntervalos> tiemposFranjaAM = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaPicoAM,servicio);
               List<TiempoIntervalos> tiemposFranjaValle = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaValle,servicio);
               List<TiempoIntervalos> tiemposFranjaPM = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaPicoPM,servicio);
               List<TiempoIntervalos> tiemposFranjaCierre = horariosProvisionalServicio.getTiempoIntervalosByServicio(intervalosFranjaCierre,servicio);

                calcularPromedio(servicio,tiemposFranjaInciio,tiemposFranjaAM,tiemposFranjaValle,tiemposFranjaCierre,tiemposFranjaPM,gisIntervalos);


        }


    }

    private void calcularPromedio(ServicioTipoDia id, List<TiempoIntervalos> tiemposFranjaInciio, List<TiempoIntervalos> tiemposFranjaAM, List<TiempoIntervalos> tiemposFranjaValle, List<TiempoIntervalos> tiemposFranjaCierre, List<TiempoIntervalos> tiemposFranjaPM,GisIntervalos gisIntervalos) {
        double promedioInicio = promedio(tiemposFranjaInciio);
        double promedioPicoAm = promedio(tiemposFranjaAM);
        double promedioValle = promedio(tiemposFranjaValle);
        double promedioPicoPM = promedio(tiemposFranjaPM);
        double promedioCierre = promedio(tiemposFranjaCierre);
        Intervalos intervalos = new Intervalos(ProcessorUtils.CALCULO_PROMEDIO,promedioInicio,promedioPicoAm,promedioValle,promedioPicoPM,promedioCierre,id,gisIntervalos);
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

    private double convertirTimeToDouble(double s) {
        double hor = s / 3600;
        double min=(s-(3600*hor))/60;
        double seg=s-((hor*3600)+(min*60));
        String converter= min+"."+seg;
        return Double.parseDouble(converter);

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
//                else{
//                    Time  tiempo= getTime(horarioA.getInstante());
//                    String identificador = macroA + "-"+lineaA+"-"+seccionA+"-"+puntoA;
//                    IntervalosIdentificador intervalosIdentificador= buscarOTraerIdentificador(identificador,cuadro);
//                    TiempoIntervalos tiempoIntervalos = new TiempoIntervalos(tiempo,intervalosIdentificador,intervaloA);
//                    horariosProvisionalServicio.addTiempoIntervalos(tiempoIntervalos);
//                }
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

    private IntervalosIdentificador buscarOTraerIdentificador(String identificador,String cuadro) {
        IntervalosIdentificador intervalosIdentificador = horariosProvisionalServicio.getIntervalosIdentificadorByServicio(identificador,cuadro);
        if( intervalosIdentificador == null ){
            intervalosIdentificador = new IntervalosIdentificador(identificador,cuadro);
            horariosProvisionalServicio.addIntervalosId(intervalosIdentificador);
        }
        return intervalosIdentificador;
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

    private void copiarInformacionATablaProvisional(List<Horario> tablaHorario) {

        long timeIni=System.currentTimeMillis();
        int r=0;
        for (Horario horario:tablaHorario) {
            System.out.println("horario "+horario.getMacro());
            System.out.println("s "+r);
            r++;
            Time instante= calcularInstante(horario.getInstante());
            String identificador= horario.getMacro()+"-"+horario.getLinea()+"-"+horario.getSeccion()+"-"+horario.getPunto();
            IntervalosProgramacion intervalosProgramacion= calcularIntervalo(instante);
            TemporalHorarios temporalHorarios = new TemporalHorarios(horario.getCuadro(),instante,
                    horario.getServBus(),identificador,horario.getMacro(),horario.getLinea(),horario.getSeccion(),horario.getPunto()
            ,intervalosProgramacion);
           horariosProvisionalServicio.addTemporalHorarios(temporalHorarios);
        }
        System.out.println(System.currentTimeMillis()-timeIni);


    }

    private IntervalosProgramacion calcularIntervalo(Time instante) {
        IntervalosProgramacion intervalosProgramacion = horariosProvisionalServicio.getIntervaloForDate(instante);
        return intervalosProgramacion;
    }

    private Time calcularInstante(int instante) {
        int hor = instante / 3600;
        int min=(instante-(3600*hor))/60;
        int seg=instante-((hor*3600)+(min*60));
//        DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
//        Date date = null;
//        try {
//            date = sdf.parse(instante);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        Time time= new Time(hor,seg,min);
        return time;
    }


    public TablaHorarioService getTablaHorarioService() {
        return tablaHorarioService;
    }

    public void setTablaHorarioService(TablaHorarioService tablaHorarioService) {
        this.tablaHorarioService = tablaHorarioService;
    }
}
