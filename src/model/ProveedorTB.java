package model;

import java.sql.Date;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProveedorTB {
    
    private SimpleIntegerProperty id;
    private SimpleStringProperty idProveedor;
    private int tipoDocumento;
    private SimpleStringProperty tipoDocumentoName;
    private SimpleStringProperty numeroDocumento;
    private SimpleStringProperty razonSocial;
    private SimpleStringProperty nombreComercial;
    private String pais;
    private int ciudad;
    private int ambito;
    private int estado;
    private SimpleStringProperty estadoName;
    private String usuarioRegistro;
    private ObjectProperty<LocalDate> fechaRegistro;
    private String usuarioActualizado;
    private Date fechaActualizado;

    public ProveedorTB() {
        
    }

    public ProveedorTB(String numeroDocumento, String razonSocial) {
        this.numeroDocumento = new SimpleStringProperty(numeroDocumento);
        this.razonSocial = new SimpleStringProperty(razonSocial);
    }
    
    

    public SimpleIntegerProperty getId() {
        return id;
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }
    
    public SimpleStringProperty getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = new SimpleStringProperty(idProveedor);
    }

    public int getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public SimpleStringProperty getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = new SimpleStringProperty(numeroDocumento);
    }

    public SimpleStringProperty getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = new SimpleStringProperty(razonSocial);
    }

    public SimpleStringProperty getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = new SimpleStringProperty(nombreComercial != null ? nombreComercial : "");
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getCiudad() {
        return ciudad;
    }

    public void setCiudad(int ciudad) {
        this.ciudad = ciudad;
    }

    public int getAmbito() {
        return ambito;
    }

    public void setAmbito(int ambito) {
        this.ambito = ambito;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public ObjectProperty<LocalDate> getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = new SimpleObjectProperty<>(fechaRegistro);
    }

    public String getUsuarioActualizado() {
        return usuarioActualizado;
    }

    public void setUsuarioActualizado(String usuarioActualizado) {
        this.usuarioActualizado = usuarioActualizado;
    }

    public Date getFechaActualizado() {
        return fechaActualizado;
    }

    public void setFechaActualizado(Date fechaActualizado) {
        this.fechaActualizado = fechaActualizado;
    }

    public SimpleStringProperty getTipoDocumentoName() {
        return tipoDocumentoName;
    }

    public void setTipoDocumentoName(String tipoDocumentoName) {
        this.tipoDocumentoName = new SimpleStringProperty(tipoDocumentoName);
    }

    public SimpleStringProperty getEstadoName() {
        return estadoName;
    }

    public void setEstadoName(String estadoName) {
        this.estadoName = new SimpleStringProperty(estadoName);
    }
    
    public ObjectProperty<LocalDate> fechaRegistroProperty() {
        return fechaRegistro;
    }

}
