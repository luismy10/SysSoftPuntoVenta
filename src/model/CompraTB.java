package model;

import java.sql.Timestamp;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CompraTB {

    /*
    Atributos para las vistas en general
     */
    private int id;
    private String idCompra;
    private String proveedor;
    private int comprobante;
    private String comprobanteName;
    private String numeracion;
    private int tipoMoneda;
    private String tipoMonedaName;
    private ObjectProperty<LocalDate> fechaCompra;
    private Timestamp fechaRegistro;
    private double subTotal;
    private double descuento;
    private SimpleDoubleProperty total;
    private String observaciones;
    private String notas;
    
    private String TipoCompra;
    private String EstadoCompra;
    
    private ProveedorTB proveedorTB;
    private ArticuloTB articuloTB;

    /*
     Atributos para el reporte
     */
    private String fechaCompraReporte;
    private String serienumeracionReporte;
    private String proveedorReporte;
    private String totalReporte;

    public CompraTB() {

    }

    public CompraTB(int id, String fechaCompraReporte, String serienumeracionReporte, String proveedorReporte, String totalReporte) {
        this.id = id;
        this.fechaCompraReporte = fechaCompraReporte;
        this.serienumeracionReporte = serienumeracionReporte;
        this.proveedorReporte = proveedorReporte;
        this.totalReporte = totalReporte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public int getComprobante() {
        return comprobante;
    }

    public void setComprobante(int comprobante) {
        this.comprobante = comprobante;
    }

    public String getComprobanteName() {
        return comprobanteName;
    }

    public void setComprobanteName(String comprobanteName) {
        this.comprobanteName = comprobanteName;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public int getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(int tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public String getTipoMonedaName() {
        return tipoMonedaName;
    }

    public void setTipoMonedaName(String tipoMonedaName) {
        this.tipoMonedaName = tipoMonedaName == null ? "" : tipoMonedaName;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public ObjectProperty<LocalDate> getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = new SimpleObjectProperty<>(fechaCompra);
    }

    public ObjectProperty<LocalDate> fechaCompraProperty() {
        return fechaCompra;
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

    public SimpleDoubleProperty getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = new SimpleDoubleProperty(total);
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones == null ? "" : observaciones;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas == null ? "" : notas;
    }

    public ProveedorTB getProveedorTB() {
        return proveedorTB;
    }

    public void setProveedorTB(ProveedorTB proveedorTB) {
        this.proveedorTB = proveedorTB;
    }

    public ArticuloTB getArticuloTB() {
        return articuloTB;
    }

    public void setArticuloTB(ArticuloTB articuloTB) {
        this.articuloTB = articuloTB;
    }

    public String getSerienumeracionReporte() {
        return serienumeracionReporte;
    }

    public void setSerienumeracionReporte(String serienumeracionReporte) {
        this.serienumeracionReporte = serienumeracionReporte;
    }

    public String getProveedorReporte() {
        return proveedorReporte;
    }

    public void setProveedorReporte(String proveedorReporte) {
        this.proveedorReporte = proveedorReporte;
    }

    public String getFechaCompraReporte() {
        return fechaCompraReporte;
    }

    public void setFechaCompraReporte(String fechaCompraReporte) {
        this.fechaCompraReporte = fechaCompraReporte;
    }

    public String getTotalReporte() {
        return totalReporte;
    }

    public void setTotalReporte(String totalReporte) {
        this.totalReporte = totalReporte;
    }

    public String getTipoCompra() {
        return TipoCompra;
    }

    public void setTipoCompra(String TipoCompra) {
        this.TipoCompra = TipoCompra;
    }

    public String getEstadoCompra() {
        return EstadoCompra;
    }

    public void setEstadoCompra(String EstadoCompra) {
        this.EstadoCompra = EstadoCompra;
    }
}
