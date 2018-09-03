/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;

public class PersonaTB implements Serializable {

    private long id;
    private Long idPersona;
    private int tipoDocumento;
    private String numeroDocumento;
    private SimpleStringProperty apellidoPaterno;
    private String apellidoMaterno;
    private String primerNombre;
    private String segundoNombre;
    private int sexo;
    private Date fechaNacimiento;
    private Integer idFacturacion;
    private String foto1;
    private String foto2;
    private String foto3;
    private String usuarioRegistro;
    private Date fechaRegistro;
    private String usuarioActualizo;
    private String fechaActualizo;
    private Collection<DirectorioTB> directorioTBCollection;

    public PersonaTB() {
    }

    public PersonaTB(Long idPersona) {
        this.idPersona = idPersona;
    }

    public PersonaTB(String apellidoPaterno) {
        this.apellidoPaterno = new SimpleStringProperty(apellidoPaterno);
    }

    public PersonaTB(Long idPersona, long id, int tipoDocumento, String numeroDocumento, SimpleStringProperty apellidoPaterno, String apellidoMaterno, String primerNombre, String segundoNombre, int sexo, String usuarioRegistro, Date fechaRegistro) {
        this.idPersona = idPersona;
        this.id = id;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.sexo = sexo;
        this.usuarioRegistro = usuarioRegistro;
        this.fechaRegistro = fechaRegistro;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public int getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public SimpleStringProperty getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(SimpleStringProperty apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getIdFacturacion() {
        return idFacturacion;
    }

    public void setIdFacturacion(Integer idFacturacion) {
        this.idFacturacion = idFacturacion;
    }

    public String getFoto1() {
        return foto1;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    public String getFoto2() {
        return foto2;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }

    public String getFoto3() {
        return foto3;
    }

    public void setFoto3(String foto3) {
        this.foto3 = foto3;
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

    public String getUsuarioActualizo() {
        return usuarioActualizo;
    }

    public void setUsuarioActualizo(String usuarioActualizo) {
        this.usuarioActualizo = usuarioActualizo;
    }

    public String getFechaActualizo() {
        return fechaActualizo;
    }

    public void setFechaActualizo(String fechaActualizo) {
        this.fechaActualizo = fechaActualizo;
    }

    public Collection<DirectorioTB> getDirectorioTBCollection() {
        return directorioTBCollection;
    }

    public void setDirectorioTBCollection(Collection<DirectorioTB> directorioTBCollection) {
        this.directorioTBCollection = directorioTBCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersona != null ? idPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonaTB)) {
            return false;
        }
        PersonaTB other = (PersonaTB) object;
        if ((this.idPersona == null && other.idPersona != null) || (this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "main.PersonaTB[ idPersona=" + idPersona + " ]";
    }

}
