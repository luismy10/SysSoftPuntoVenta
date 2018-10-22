package model;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class LoteTB {
    
    private SimpleIntegerProperty id;
    private long idLote;
    private boolean tipoLote;
    private String numeroLote;
    private ObjectProperty<LocalDate> fechaFabricacion;
    private ObjectProperty<LocalDate> fechaCaducidad;
    private double existenciaInicial;
    private double existenciaActual;
    private String idArticulo;
    private String idCompra;
    private ArticuloTB articuloTB;
    
    public LoteTB() {

    }

    public SimpleIntegerProperty getId() {
        return id;
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
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

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad.get();
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

    public boolean getTipoLote() {
        return tipoLote;
    }

    public void setTipoLote(boolean tipoLote) {
        this.tipoLote = tipoLote;
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

    public ArticuloTB getArticuloTB() {
        return articuloTB;
    }

    public void setArticuloTB(ArticuloTB articuloTB) {
        this.articuloTB = articuloTB;
    }
    

}
