package model;

import java.sql.Date;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProveedorTB {

    private SimpleLongProperty idProveedor;
    private int tipoDocumento;
    private SimpleStringProperty tipoDocumentoName;
    private SimpleStringProperty numeroDocumento;
    private SimpleStringProperty razonSocial;
    private SimpleStringProperty nombreComercial;
    private String pais;
    private int ciudad;
    private int estado;
    private String usuarioRegistro;
    private Date fechaRegistro;
    private String usuarioActualizado;
    private Date fechaActualizado;

    public ProveedorTB() {
    }

    public ProveedorTB(SimpleLongProperty idProveedor, int tipoDocumento, SimpleStringProperty numeroDocumento, SimpleStringProperty razonSocial, SimpleStringProperty nombreComercial, String pais, int ciudad, int estado, String usuarioRegistro, Date fechaRegistro, String usuarioActualizado, Date fechaActualizado) {
        this.idProveedor = idProveedor;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.razonSocial = razonSocial;
        this.nombreComercial = nombreComercial;
        this.pais = pais;
        this.ciudad = ciudad;
        this.estado = estado;
        this.usuarioRegistro = usuarioRegistro;
        this.fechaRegistro = fechaRegistro;
        this.usuarioActualizado = usuarioActualizado;
        this.fechaActualizado = fechaActualizado;
    }

    public SimpleLongProperty getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(long idProveedor) {
        this.idProveedor = new SimpleLongProperty(idProveedor);
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

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

}
