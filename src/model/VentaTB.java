package model;

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
    private String comproabanteNameImpresion;
    private String serie;
    private String numeracion;
    private LocalDateTime fechaVenta;
    private double subTotal;
    private double descuento;
    private double total;
    private int tipo;
    private String tipoName;
    private int estado;
    private String estadoName;
    private String observaciones;
    private double efectivo;
    private double vuelto;
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

    public String getComproabanteNameImpresion() {
        return comproabanteNameImpresion;
    }

    public void setComproabanteNameImpresion(String comproabanteNameImpresion) {
        this.comproabanteNameImpresion = comproabanteNameImpresion;
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

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getTipoName() {
        return tipoName;
    }

    public void setTipoName(String tipoName) {
        this.tipoName = tipoName;
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

    public double getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(double efectivo) {
        this.efectivo = efectivo;
    }

    public double getVuelto() {
        return vuelto;
    }

    public void setVuelto(double vuelto) {
        this.vuelto = vuelto;
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
