
package model;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;


public class ClienteTB {
    
    private SimpleLongProperty id;
    private String idCliente;
    private String telefono;
    private String celular;
    private String email;
    private String direccion;
    private int estado;
    private String estadoName;
    private String usuarioRegistro;
    private ObjectProperty<LocalDate> fechaRegistro;
    private String usuarioActualizo;
    private ObjectProperty<LocalDate> fechaActualizo;
    private String idPersona;
    private PersonaTB personaTB;
    private long idFacturacion;
    private FacturacionTB facturacionTB;
    
    public ClienteTB() {
        
    }
    
    public SimpleLongProperty getId() {
        return id;
    }

    public void setId(long id) {
        this.id = new SimpleLongProperty(id);
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getEstadoName() {
        return estadoName;
    }

    public void setEstadoName(String estadoName) {
        this.estadoName = estadoName;
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
        this.fechaRegistro = new SimpleObjectProperty(fechaRegistro);
    }

    public String getUsuarioActualizo() {
        return usuarioActualizo;
    }

    public void setUsuarioActualizo(String usuarioActualizo) {
        this.usuarioActualizo = usuarioActualizo;
    }

    public ObjectProperty<LocalDate> getFechaActualizo() {
        return fechaActualizo;
    }

    public void setFechaActualizo(LocalDate fechaActualizo) {
        this.fechaActualizo = new SimpleObjectProperty(fechaActualizo);
    }

    public String getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }

    public PersonaTB getPersonaTB() {
        return personaTB;
    }

    public void setPersonaTB(PersonaTB personaTB) {
        this.personaTB = personaTB;
    }

    public long getIdFacturacion() {
        return idFacturacion;
    }

    public void setIdFacturacion(long idFacturacion) {
        this.idFacturacion = idFacturacion;
    }

    public FacturacionTB getFacturacionTB() {
        return facturacionTB;
    }

    public void setFacturacionTB(FacturacionTB facturacionTB) {
        this.facturacionTB = facturacionTB;
    }
    
    
}
