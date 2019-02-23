package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class VentaTB {
    
    /*
    Atributos para las vistas en general
    */
    private int id;
    private String idVenta;
    private String cliente;
    private String vendedor;
    private int comprobante;
    private int moneda;
    private String monedaName;
    private String comprobanteName;
    private String serie;
    private String numeracion;
    private Timestamp fechaVenta;
    private LocalDateTime fechaRegistro;
    private double subTotal;
    private double gravada;
    private double descuento;
    private double igv;
    private double total;
    private int estado;
    private String estadoName;
    private String observaciones;
    private ArticuloTB articuloTB;
    
    /*
    Atributos para el reporte
    */
    private String documentoReporte;
    private String fechaVentaReporte;
    private String clienteReporte;
    private String totalReporte;

    public VentaTB() {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public int getComprobante() {
        return comprobante;
    }

    public void setComprobante(int comprobante) {
        this.comprobante = comprobante;
    }

    public int getMoneda() {
        return moneda;
    }

    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    public String getMonedaName() {
        return monedaName;
    }

    public void setMonedaName(String monedaName) {
        this.monedaName = monedaName;
    }
    
    public String getComprobanteName() {
        return comprobanteName;
    }

    public void setComprobanteName(String comprobanteName) {
        this.comprobanteName = comprobanteName;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public Timestamp getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Timestamp fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getGravada() {
        return gravada;
    }

    public void setGravada(double gravada) {
        this.gravada = gravada;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getIgv() {
        return igv;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArticuloTB getArticuloTB() {
        return articuloTB;
    }

    public void setArticuloTB(ArticuloTB articuloTB) {
        this.articuloTB = articuloTB;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDocumentoReporte() {
        return documentoReporte;
    }

    public void setDocumentoReporte(String documentoReporte) {
        this.documentoReporte = documentoReporte;
    }

    public String getFechaVentaReporte() {
        return fechaVentaReporte;
    }

    public void setFechaVentaReporte(String fechaVentaReporte) {
        this.fechaVentaReporte = fechaVentaReporte;
    }

    public String getClienteReporte() {
        return clienteReporte;
    }

    public void setClienteReporte(String clienteReporte) {
        this.clienteReporte = clienteReporte;
    }

    public String getTotalReporte() {
        return totalReporte;
    }

    public void setTotalReporte(String totalReporte) {
        this.totalReporte = totalReporte;
    }
    
    
}
