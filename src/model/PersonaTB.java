package model;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PersonaTB extends FacturacionTB implements Serializable {

    private SimpleLongProperty id;
    private Long idPersona;
    private int tipoDocumento;
    private SimpleStringProperty numeroDocumento;
    private SimpleStringProperty apellidoPaterno;
    private String apellidoMaterno;
    private String primerNombre;
    private String segundoNombre;
    private int sexo;
    private Date fechaNacimiento;
    private String usuarioRegistro;
    private ObjectProperty<LocalDate> fechaRegistro;
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

    public PersonaTB(Long idPersona, SimpleLongProperty id, int tipoDocumento, SimpleStringProperty numeroDocumento, SimpleStringProperty apellidoPaterno, String apellidoMaterno, String primerNombre, String segundoNombre, int sexo, String usuarioRegistro, ObjectProperty<LocalDate> fechaRegistro) {
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

    public SimpleLongProperty getId() {
        return id;
    }

    public void setId(long id) {
        this.id = new SimpleLongProperty(id);
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

    public SimpleStringProperty getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = new SimpleStringProperty(numeroDocumento);
    }

    public SimpleStringProperty getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = new SimpleStringProperty(apellidoPaterno);
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

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro.get();
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = new SimpleObjectProperty<>(fechaRegistro);
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

    public ObjectProperty<LocalDate> fechaRegistroProperty() {
        return fechaRegistro;
    }
}
