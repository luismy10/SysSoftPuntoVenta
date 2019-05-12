/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author Ruberfc
 */
public class PagoProveedoresTB {

    private int idPagoProveedores;
    private double montoTotal;
    private double montoActual;
    private int cuotaActual;
    private String plazos;
    private int Dias;
    private Timestamp fechaActual;
    private String observacion;
    private String estado;
    private String idProveedor;
    private String idCompra;
    private String IdEmpleado;

    public PagoProveedoresTB(){}

    public PagoProveedoresTB(int idPagoProveedores, double montoTotal, double montoActual, int cuotaActual, String plazos, int Dias, Timestamp fechaActual, String observacion, String estado, String idProveedor, String idCompra, String IdEmpleado) {
        this.idPagoProveedores = idPagoProveedores;
        this.montoTotal = montoTotal;
        this.montoActual = montoActual;
        this.cuotaActual = cuotaActual;
        this.plazos = plazos;
        this.Dias = Dias;
        this.fechaActual = fechaActual;
        this.observacion = observacion;
        this.estado = estado;
        this.idProveedor = idProveedor;
        this.idCompra = idCompra;
        this.IdEmpleado = IdEmpleado;
    }

    public int getIdPagoProveedores() {
        return idPagoProveedores;
    }

    public void setIdPagoProveedores(int idPagoProveedores) {
        this.idPagoProveedores = idPagoProveedores;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public double getMontoActual() {
        return montoActual;
    }

    public void setMontoActual(double montoActual) {
        this.montoActual = montoActual;
    }

    public int getCuotaActual() {
        return cuotaActual;
    }

    public void setCuotaActual(int cuotaActual) {
        this.cuotaActual = cuotaActual;
    }

    public String getPlazos() {
        return plazos;
    }

    public void setPlazos(String plazos) {
        this.plazos = plazos;
    }

    public int getDias() {
        return Dias;
    }

    public void setDias(int Dias) {
        this.Dias = Dias;
    }

    public Timestamp getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Timestamp fechaActual) {
        this.fechaActual = fechaActual;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public String getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(String IdEmpleado) {
        this.IdEmpleado = IdEmpleado;
    }     
    
}

