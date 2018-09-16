package model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PersonaTB extends FacturacionTB {

    private SimpleLongProperty id;
    private String idPersona;
    private int tipoDocumento;
    private SimpleStringProperty numeroDocumento;
    private SimpleStringProperty apellidoPaterno;
    private String apellidoMaterno;
    private String primerNombre;
    private String segundoNombre;
    private int sexo;
    private Date fechaNacimiento;
    private int estado;
    private SimpleStringProperty estadoName;
    private String usuarioRegistro;
    private ObjectProperty<LocalDate> fechaRegistro;
    private String usuarioActualizo;
    private String fechaActualizo;
    private Collection<DirectorioTB> directorioTBCollection;

    public PersonaTB() {
    }

    public PersonaTB(String idPersona) {
        this.idPersona = idPersona;
    }

   

    public SimpleLongProperty getId() {
        return id;
    }

    public void setId(long id) {
        this.id = new SimpleLongProperty(id);
    }

    public String getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(String idPersona) {
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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
