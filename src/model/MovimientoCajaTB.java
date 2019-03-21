package model;

import java.time.LocalDateTime;

public class MovimientoCajaTB {

    private int idMovimientoCaja;
    private int idCaja;
    private String idUsuario;
    private LocalDateTime FechaMovimiento;
    private String comentario;
    private String movimiento;
    private double entrada;
    private double salidas;
    private double saldo;

    public MovimientoCajaTB() {
    }

    public int getIdMovimientoCaja() {
        return idMovimientoCaja;
    }

    public void setIdMovimientoCaja(int idMovimientoCaja) {
        this.idMovimientoCaja = idMovimientoCaja;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public LocalDateTime getFechaMovimiento() {
        return FechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime FechaMovimiento) {
        this.FechaMovimiento = FechaMovimiento;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public double getEntrada() {
        return entrada;
    }

    public void setEntrada(double entrada) {
        this.entrada = entrada;
    }

    public double getSalidas() {
        return salidas;
    }

    public void setSalidas(double salidas) {
        this.salidas = salidas;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

}
