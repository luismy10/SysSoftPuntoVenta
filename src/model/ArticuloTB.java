package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ArticuloTB {

    private SimpleIntegerProperty id;
    private String idArticulo;
    private SimpleStringProperty clave;
    private String claveAlterna;
    private SimpleStringProperty nombre;
    private String nombreGenerico;
    private String descripcion;
    private int categorio;
    private SimpleStringProperty categoriaName;
    private int marcar;
    private SimpleStringProperty marcaName;
    private int presentacion;
    private SimpleStringProperty presentacionName;
    private double stockMinimo;
    private double stockMaximo;
    private double precioCompra;
    private SimpleDoubleProperty precioVenta;
    private SimpleDoubleProperty cantidad;
    private int estado;
    private SimpleStringProperty estadoName;
    private SimpleDoubleProperty descuento;
    private SimpleDoubleProperty subTotal;
    private SimpleDoubleProperty total;
    private SimpleDoubleProperty utilidad;
    private boolean impuesto;
    private ImagenTB imagenTB;

    public ArticuloTB() {

    }

    public SimpleIntegerProperty getId() {
        return id;
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public SimpleStringProperty getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = new SimpleStringProperty(clave);
    }

    public String getClaveAlterna() {
        return claveAlterna;
    }

    public void setClaveAlterna(String claveAlterna) {
        this.claveAlterna = claveAlterna;
    }

    public SimpleStringProperty getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = new SimpleStringProperty(nombre);
    }

    public String getNombreGenerico() {
        return nombreGenerico;
    }

    public void setNombreGenerico(String nombreGenerico) {
        this.nombreGenerico = nombreGenerico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ImagenTB getImagenTB() {
        return imagenTB;
    }

    public void setImagenTB(ImagenTB imagenTB) {
        this.imagenTB = imagenTB;
    }

    public int getCategorio() {
        return categorio;
    }

    public void setCategorio(int categorio) {
        this.categorio = categorio;
    }

    public SimpleStringProperty getCategoriaName() {
        return categoriaName;
    }

    public void setCategoriaName(String categoriaName) {
        this.categoriaName = new SimpleStringProperty(categoriaName);
    }

    public int getMarcar() {
        return marcar;
    }

    public void setMarcar(int marcar) {
        this.marcar = marcar;
    }

    public SimpleStringProperty getMarcaName() {
        return marcaName;
    }

    public void setMarcaName(String marcaName) {
        this.marcaName = new SimpleStringProperty(marcaName);
    }

    public int getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(int presentacion) {
        this.presentacion = presentacion;
    }

    public SimpleStringProperty getPresentacionName() {
        return presentacionName;
    }

    public void setPresentacionName(String presentacionName) {
        this.presentacionName = new SimpleStringProperty(presentacionName);
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

    public double getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(double stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public double getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(double stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public SimpleDoubleProperty getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = new SimpleDoubleProperty(precioVenta);
    }

    public SimpleDoubleProperty getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = new SimpleDoubleProperty(cantidad);
    }

    public SimpleDoubleProperty getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = new SimpleDoubleProperty(descuento);
    }

    public SimpleDoubleProperty getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = new SimpleDoubleProperty(total);
    }

    public SimpleDoubleProperty getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = new SimpleDoubleProperty(subTotal);
    }

    public SimpleDoubleProperty getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(double utilidad) {
        this.utilidad = new SimpleDoubleProperty(utilidad);
    }

    public boolean isImpuesto() {
        return impuesto;
    }

    public void setImpuesto(boolean impuesto) {
        this.impuesto = impuesto;
    }
    
    
    
}
