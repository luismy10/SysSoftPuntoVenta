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
    private int cuotaTotal;
    private int cuotaActual;
    private String plazos;
    private Timestamp fechaInicial;
    private Timestamp fechaActual;
    private Timestamp fechaFinal;
    private String observacion;
    private String estado;
    private String idProveedor;
    private String idCompra;

    public PagoProveedoresTB(){}

    public PagoProveedoresTB(int idPagoProveedores, double montoTotal, double montoActual, int cuotaTotal, int cuotaActual, String plazos, Timestamp fechaInicial, Timestamp fechaActual, Timestamp fechaFinal, String observacion, String estado, String idProveedor, String idCompra) {
        this.idPagoProveedores = idPagoProveedores;
        this.montoTotal = montoTotal;
        this.montoActual = montoActual;
        this.cuotaTotal = cuotaTotal;
        this.cuotaActual = cuotaActual;
        this.plazos = plazos;
        this.fechaInicial = fechaInicial;
        this.fechaActual = fechaActual;
        this.fechaFinal = fechaFinal;
        this.observacion = observacion;
        this.estado = estado;
        this.idProveedor = idProveedor;
        this.idCompra = idCompra;
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

    public int getCuotaTotal() {
        return cuotaTotal;
    }

    public void setCuotaTotal(int cuotaTotal) {
        this.cuotaTotal = cuotaTotal;
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

    public Timestamp getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Timestamp fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Timestamp getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Timestamp fechaActual) {
        this.fechaActual = fechaActual;
    }

    public Timestamp getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Timestamp fechaFinal) {
        this.fechaFinal = fechaFinal;
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
    
    
    
}

