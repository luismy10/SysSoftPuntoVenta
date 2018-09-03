
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class MantenimientoTB implements Serializable {

    private String idMantenimiento;
    private String nombre;
    private Character estado;
    private String usuarioRegistro;
    private Date fechaRegistro;

    public MantenimientoTB() {
    }

    public MantenimientoTB(String idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public MantenimientoTB(String idMantenimiento, String nombre) {
        this.idMantenimiento = idMantenimiento;
        this.nombre = nombre;
    }
    
    
    
    public MantenimientoTB(String idMantenimiento, String nombre, Character estado, String usuarioRegistro) {
        this.idMantenimiento = idMantenimiento;
        this.nombre = nombre;
        this.estado = estado;
        this.usuarioRegistro = usuarioRegistro;
    }

    public String getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(String idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMantenimiento != null ? idMantenimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MantenimientoTB other = (MantenimientoTB) obj;
        return Objects.equals(this.idMantenimiento, other.idMantenimiento);
    }


    @Override
    public String toString() {
        return nombre ;
    }

}
