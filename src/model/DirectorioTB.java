package model;

import java.io.Serializable;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleLongProperty;

public class DirectorioTB implements Serializable {

    private SimpleLongProperty id;
    private Long idDirectorio;
    private int atributo;
    private String valor1;
    private String valor2;
    private String valor3;
    private String usuarioRegistro;
    private Date fechaRegistro;
    private String usuarioActualizo;
    private SimpleStringProperty fechaActualizo;
    private PersonaTB idPersona;

    public DirectorioTB() {
    }

    public DirectorioTB(long id, PersonaTB idPersona) {
        this.id = new SimpleLongProperty(id);
        this.idPersona = idPersona;
    }

    public DirectorioTB(Long idDirectorio) {
        this.idDirectorio = idDirectorio;
    }

    public DirectorioTB(Long idDirectorio, SimpleLongProperty id, int atributo, String valor1, String usuarioRegistro, Date fechaRegistro) {
        this.idDirectorio = idDirectorio;
        this.id = id;
        this.atributo = atributo;
        this.valor1 = valor1;
        this.usuarioRegistro = usuarioRegistro;
        this.fechaRegistro = fechaRegistro;
    }

    public SimpleLongProperty getId() {
        return id;
    }

    public void setId(long id) {
        this.id = new SimpleLongProperty(id);
    }

    public Long getIdDirectorio() {
        return idDirectorio;
    }

    public void setIdDirectorio(Long idDirectorio) {
        this.idDirectorio = idDirectorio;
    }

    public int getAtributo() {
        return atributo;
    }

    public void setAtributo(int atributo) {
        this.atributo = atributo;
    }

    public String getValor1() {
        return valor1;
    }

    public void setValor1(String valor1) {
        this.valor1 = valor1;
    }

    public String getValor2() {
        return valor2;
    }

    public void setValor2(String valor2) {
        this.valor2 = valor2;
    }

    public String getValor3() {
        return valor3;
    }

    public void setValor3(String valor3) {
        this.valor3 = valor3;
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

    public SimpleStringProperty getFechaActualizo() {
        return fechaActualizo;
    }

    public void setFechaActualizo(SimpleStringProperty fechaActualizo) {
        this.fechaActualizo = fechaActualizo;
    }

    public PersonaTB getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(PersonaTB idPersona) {
        this.idPersona = idPersona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDirectorio != null ? idDirectorio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DirectorioTB)) {
            return false;
        }
        DirectorioTB other = (DirectorioTB) object;
        if ((this.idDirectorio == null && other.idDirectorio != null) || (this.idDirectorio != null && !this.idDirectorio.equals(other.idDirectorio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "main.DirectorioTB[ idDirectorio=" + idDirectorio + " ]";
    }

}
