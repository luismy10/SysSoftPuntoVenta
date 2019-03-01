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
public class CuentasCobrarProveedoresTB {

    private int idCuentasCobrarProveedores;
    private double cuotaTotal;
    private double cuotaActual;
    private int letraTotal;
    private int letraActual;
    private Timestamp fechaInicial;
    private Timestamp fechaActual;
    private Timestamp fechaFinal;
    private String observacion;
    private String estado;
    private ProveedorTB proveedorTB;
    private CompraTB compraTB;
    
    public CuentasCobrarProveedoresTB(){}

    public CuentasCobrarProveedoresTB(int idCuentasCobrarProveedores, double cuotaTotal, double cuotaActual, int letraTotal, int letraActual, Timestamp fechaInicial, Timestamp fechaActual, Timestamp fechaFinal, String observacion, String estado, ProveedorTB proveedorTB, CompraTB compraTB) {
        this.idCuentasCobrarProveedores = idCuentasCobrarProveedores;
        this.cuotaTotal = cuotaTotal;
        this.cuotaActual = cuotaActual;
        this.letraTotal = letraTotal;
        this.letraActual = letraActual;
        this.fechaInicial = fechaInicial;
        this.fechaActual = fechaActual;
        this.fechaFinal = fechaFinal;
        this.observacion = observacion;
        this.estado = estado;
        this.proveedorTB = proveedorTB;
        this.compraTB = compraTB;
    }

    public int getIdCuentasCobrarProveedores() {
        return idCuentasCobrarProveedores;
    }

    public void setIdCuentasCobrarProveedores(int idCuentasCobrarProveedores) {
        this.idCuentasCobrarProveedores = idCuentasCobrarProveedores;
    }

    public double getCuotaTotal() {
        return cuotaTotal;
    }

    public void setCuotaTotal(double cuotaTotal) {
        this.cuotaTotal = cuotaTotal;
    }

    public double getCuotaActual() {
        return cuotaActual;
    }

    public void setCuotaActual(double cuotaActual) {
        this.cuotaActual = cuotaActual;
    }

    public int getLetraTotal() {
        return letraTotal;
    }

    public void setLetraTotal(int letraTotal) {
        this.letraTotal = letraTotal;
    }

    public int getLetraActual() {
        return letraActual;
    }

    public void setLetraActual(int letraActual) {
        this.letraActual = letraActual;
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

    public ProveedorTB getProveedorTB() {
        return proveedorTB;
    }

    public void setProveedorTB(ProveedorTB proveedorTB) {
        this.proveedorTB = proveedorTB;
    }

    public CompraTB getCompraTB() {
        return compraTB;
    }

    public void setCompraTB(CompraTB compraTB) {
        this.compraTB = compraTB;
    }
    
}
