/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;

/**
 *
 * @author Ruberfc
 */
public class CajaTB {

    private int idCaja;
    private double montoInicial;
    private double montoFinal;
    private double ingresos;
    private double egresos;
    private double devoluciones;
    private double entradas;
    private double salidas;
    private Timestamp fechaApertura;
    private Timestamp fechaCierre;
    private String estado;
    private String idEmpleado;

    public CajaTB() {
    }

    public CajaTB(int idCaja, double montoInicial, double montoFinal, double ingresos, double egresos, double devoluciones, double entradas, double salidas, Timestamp fechaApertura, Timestamp fechaCierre, String estado, String idEmpleado) {
        this.idCaja = idCaja;
        this.montoInicial = montoInicial;
        this.montoFinal = montoFinal;
        this.ingresos = ingresos;
        this.egresos = egresos;
        this.devoluciones = devoluciones;
        this.entradas = entradas;
        this.salidas = salidas;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.estado = estado;
        this.idEmpleado = idEmpleado;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public double getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(double montoInicial) {
        this.montoInicial = montoInicial;
    }

    public double getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(double montoFinal) {
        this.montoFinal = montoFinal;
    }

    public double getIngresos() {
        return ingresos;
    }

    public void setIngresos(double ingresos) {
        this.ingresos = ingresos;
    }

    public double getEgresos() {
        return egresos;
    }

    public void setEgresos(double egresos) {
        this.egresos = egresos;
    }

    public double getDevoluciones() {
        return devoluciones;
    }

    public void setDevoluciones(double devoluciones) {
        this.devoluciones = devoluciones;
    }

    public double getEntradas() {
        return entradas;
    }

    public void setEntradas(double entradas) {
        this.entradas = entradas;
    }

    public double getSalidas() {
        return salidas;
    }

    public void setSalidas(double salidas) {
        this.salidas = salidas;
    }

    public Timestamp getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Timestamp fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Timestamp getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Timestamp fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    

}
