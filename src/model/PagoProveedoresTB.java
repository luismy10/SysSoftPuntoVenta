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

    private int idagoProveedores;
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
    private String idproveedor;
    private String idcompra;

    public PagoProveedoresTB(){}

    public PagoProveedoresTB(int idagoProveedores, double montoTotal, double montoActual, int cuotaTotal, int cuotaActual, String plazos, Timestamp fechaInicial, Timestamp fechaActual, Timestamp fechaFinal, String observacion, String estado, String idproveedor, String idcompra) {
        this.idagoProveedores = idagoProveedores;
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
        this.idproveedor = idproveedor;
        this.idcompra = idcompra;
    }

    public int getIdagoProveedores() {
        return idagoProveedores;
    }

    public void setIdagoProveedores(int idagoProveedores) {
        this.idagoProveedores = idagoProveedores;
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

    public String getIdproveedor() {
        return idproveedor;
    }

    public void setIdproveedor(String idproveedor) {
        this.idproveedor = idproveedor;
    }

    public String getIdcompra() {
        return idcompra;
    }

    public void setIdcompra(String idcompra) {
        this.idcompra = idcompra;
    }
    

}

