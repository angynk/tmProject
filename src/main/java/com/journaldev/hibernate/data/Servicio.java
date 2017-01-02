package com.journaldev.hibernate.data;


public class Servicio {

    private String id;
    private TipoDia tipoDia;
    private String trayecto;
    private int macro;
    private int linea;
    private int seccion;

    public Servicio(String id, TipoDia tipoDia, String trayecto, int macro, int linea, int seccion) {
        this.id = id;
        this.tipoDia = tipoDia;
        this.trayecto = trayecto;
        this.macro = macro;
        this.linea = linea;
        this.seccion = seccion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TipoDia getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(TipoDia tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(String trayecto) {
        this.trayecto = trayecto;
    }

    public int getMacro() {
        return macro;
    }

    public void setMacro(int macro) {
        this.macro = macro;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getSeccion() {
        return seccion;
    }

    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }
}
