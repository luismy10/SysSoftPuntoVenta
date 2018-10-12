
package model;

import javafx.beans.property.SimpleLongProperty;


public class ClienteTB {
    
    private SimpleLongProperty id;
    private long idCliente;
    private String telefono;
    private String celular;
    private String email;
    private String direccion;
    private int estado;
    private String estadoName;
    private String idPersona;
    private PersonaTB personaTB;

    public ClienteTB() {
    }
    
    

    public SimpleLongProperty getId() {
        return id;
    }

    public void setId(long id) {
        this.id = new SimpleLongProperty(id);
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
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
    
    
}
