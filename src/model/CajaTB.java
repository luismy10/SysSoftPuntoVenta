package model;

import java.time.LocalDateTime;

public class CajaTB {

    private int idCaja;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private boolean estado;
    private double contado;
    private double calculado;
    private double diferencia;
    private String idUsuario;
    private LocalDateTime fechaRegistro;
    private EmpleadoTB empleadoTB;
    
    public CajaTB() {
        
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public double getContado() {
        return contado;
    }

    public void setContado(double contado) {
        this.contado = contado;
    }

    public double getCalculado() {
        return calculado;
    }

    public void setCalculado(double calculado) {
        this.calculado = calculado;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public EmpleadoTB getEmpleadoTB() {
        return empleadoTB;
    }

    public void setEmpleadoTB(EmpleadoTB empleadoTB) {
        this.empleadoTB = empleadoTB;
    } 
    
}
