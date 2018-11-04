/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Ruberfc
 */

import java.sql.Date;

public class ComprobanteTB {
    
    private byte[] serie;
    private String numeracion;
    private Date facha;

    public ComprobanteTB() {
    }

    public ComprobanteTB(byte[] serie, String numeracion) {
        this.serie = serie;
        this.numeracion = numeracion;
    }

    public ComprobanteTB(byte[] serie, String numeracion, Date facha) {
        this.serie = serie;
        this.numeracion = numeracion;
        this.facha = facha;
    }

    
    public byte[] getSerie() {
        return serie;
    }

    public void setSerie(byte[] serie) {
        this.serie = serie;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public Date getFacha() {
        return facha;
    }

    public void setFacha(Date facha) {
        this.facha = facha;
    }
    
    
}
