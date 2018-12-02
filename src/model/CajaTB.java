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
    
    private int idCajaTrabajador ;
    private double montoInicial;
    private double montoFinal;
    private String estado;
    private Timestamp fecha;
        
    public CajaTB(){}

    public CajaTB(int idCajaTrabajador, double montoInicial, double montoFinal, String estado, Timestamp fecha) {
        this.idCajaTrabajador = idCajaTrabajador;
        this.montoInicial = montoInicial;
        this.montoFinal = montoFinal;
        this.estado = estado;
        this.fecha = fecha;
    }

    public int getIdCajaTrabajador() {
        return idCajaTrabajador;
    }

    public void setIdCajaTrabajador(int idCajaTrabajador) {
        this.idCajaTrabajador = idCajaTrabajador;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
    
    
}
