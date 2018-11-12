/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Ruberfc
 */
public class HistorialTB {

    private SimpleIntegerProperty idHistorial;
    private String tipoOperacion;
    private String idOperacion;
    private String idArticulo;
    private Timestamp fechaRegistro;
    private SimpleIntegerProperty entrada;
    private SimpleIntegerProperty salida;
    private String usuarioRegistro;

    public HistorialTB() {
    }

    public HistorialTB(SimpleIntegerProperty idHistorial, String tipoOperacion, String idOperacion, String idArticulo, Timestamp fechaRegistro, SimpleIntegerProperty entrada, SimpleIntegerProperty salida, String usuarioRegistro) {
        this.idHistorial = idHistorial;
        this.tipoOperacion = tipoOperacion;
        this.idOperacion = idOperacion;
        this.idArticulo = idArticulo;
        this.fechaRegistro = fechaRegistro;
        this.entrada = entrada;
        this.salida = salida;
        this.usuarioRegistro = usuarioRegistro;
    }

    public SimpleIntegerProperty getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = new SimpleIntegerProperty(idHistorial);
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getIdOperacion() {
        return idOperacion;
    }

    public void setIdOperacion(String idOperacion) {
        this.idOperacion = idOperacion;
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public SimpleIntegerProperty getEntrada() {
        return entrada;
    }

    public void setEntrada(int entrada) {
        this.entrada = new SimpleIntegerProperty(entrada);
    }

    public SimpleIntegerProperty getSalida() {
        return salida;
    }

    public void setSalida(int salida) {
        this.salida = new SimpleIntegerProperty(salida);
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }
    
    


   
    
    
    
}
