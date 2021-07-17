package org.luisluch.bean;

import java.util.Date;


public class Servicios_has_Empleados {
    private int Servicios_codigoServicio;
    private Date fechaEvento;
    private String horaEvento;
    private String lugarEvento;
    private int codigoServicio;
    private int codigoEmpleado;

    public Servicios_has_Empleados() {
    }

    public Servicios_has_Empleados(int Servicios_codigoServicio, Date fechaEvento, String horaEvento, String lugarEvento, int codigoServicio, int codigoEmpleado) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
        this.fechaEvento = fechaEvento;
        this.horaEvento = horaEvento;
        this.lugarEvento = lugarEvento;
        this.codigoServicio = codigoServicio;
        this.codigoEmpleado = codigoEmpleado;
    }

    public int getServicios_codigoServicio() {
        return Servicios_codigoServicio;
    }

    public void setServicios_codigoServicio(int Servicios_codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(String horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getLugarEvento() {
        return lugarEvento;
    }

    public void setLugarEvento(String lugarEvento) {
        this.lugarEvento = lugarEvento;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }
    
    
    
}
