package model;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class LoteTB {

    private long idLote;
    private String numeroLote;
    private ObjectProperty<LocalDate> fechaFabricacion;
    private ObjectProperty<LocalDate> fechaCaducidad;
    private double existenciaInicial;
    private double existenciaActual;
    private int estado;
    private String idArticulo;
    private String idCompra;

    public LoteTB() {

    }

    public long getIdLote() {
        return idLote;
    }

    public void setIdLote(long idLote) {
        this.idLote = idLote;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public LocalDate getFechaFabricacion() {
        return fechaFabricacion.get();
    }

    public void setFechaFabricacion(LocalDate fechaFabricacion) {
        this.fechaFabricacion = new SimpleObjectProperty(fechaFabricacion);
    }

    public ObjectProperty<LocalDate> getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = new SimpleObjectProperty(fechaCaducidad);
    }

    public double getExistenciaInicial() {
        return existenciaInicial;
    }

    public void setExistenciaInicial(double existenciaInicial) {
        this.existenciaInicial = existenciaInicial;
    }

    public double getExistenciaActual() {
        return existenciaActual;
    }

    public void setExistenciaActual(double existenciaActual) {
        this.existenciaActual = existenciaActual;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }
    
     public ObjectProperty<LocalDate> fechaFabricacionProperty() {
        return fechaFabricacion;
    }
    
    public ObjectProperty<LocalDate> fechaCaducidadProperty() {
        return fechaCaducidad;
    }

}
