package model;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class ArticuloTB {

    private SimpleIntegerProperty id;
    private String idArticulo;
    private String clave;
    private String claveAlterna;
    private String nombreMarca;
    private String nombreGenerico;
    private int categoria;
    private SimpleStringProperty categoriaName;
    private int marcar;
    private SimpleStringProperty marcaName;
    private int unidadMedida;
    private SimpleStringProperty unidadMedidaName;
    private int presentacion;
    private SimpleStringProperty presentacionName;
    private int unidadVenta;
    private SimpleStringProperty unidadVentaName;
    private int estado;

    private double stockMinimo;
    private double stockMaximo;
    private double cantidad;
    private double cantidadGranel;
    private double precioCompra;
    private double precioCompraReal;

    private double precioVenta;
    private short margen;
    private double utilidad;

    private boolean lote;
    private boolean inventario;
    private ImageView imageLote;
    private SimpleStringProperty estadoName;
    private SimpleDoubleProperty descuento;
    private SimpleDoubleProperty subTotal;
    private SimpleDoubleProperty importe;   
    private String imagenTB;
    private ObjectProperty<LocalDate> fechaRegistro;
    
    private int impuestoArticulo;
    private String impuestoArticuloName;

    public ArticuloTB() {

    }

    public ArticuloTB(String nombreMarca, int unidadVenta) {
        this.nombreMarca = nombreMarca;
        this.unidadVenta = unidadVenta;
    }

    public ArticuloTB(String idArticulo, String nombreMarca, int unidadVenta) {
        this.idArticulo = idArticulo;
        this.nombreMarca = nombreMarca;
        this.unidadVenta = unidadVenta;
    }

    public ArticuloTB(String clave, String nombreMarca) {
        this.clave = clave;
        this.nombreMarca = nombreMarca;
    }
    
    public ArticuloTB(String claveAlterna, String nombreMarca,boolean estado) {
        this.claveAlterna = claveAlterna;
        this.nombreMarca = nombreMarca;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClaveAlterna() {
        return claveAlterna;
    }

    public void setClaveAlterna(String claveAlterna) {
        this.claveAlterna = claveAlterna;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public String getNombreGenerico() {
        return nombreGenerico;
    }

    public void setNombreGenerico(String nombreGenerico) {
        this.nombreGenerico = nombreGenerico;
    }

    public String getImagenTB() {
        return imagenTB;
    }

    public void setImagenTB(String imagenTB) {
        this.imagenTB = imagenTB == null ? "" : imagenTB;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
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

    public int getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(int unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public SimpleStringProperty getUnidadMedidaName() {
        return unidadMedidaName;
    }

    public void setUnidadMedidaName(String unidadMedidaName) {
        this.unidadMedidaName = new SimpleStringProperty(unidadMedidaName);
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

    public boolean isLote() {
        return lote;
    }

    public void setLote(boolean lote) {
        this.lote = lote;
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

    public double getPrecioCompraReal() {
        return precioCompraReal;
    }

    public void setPrecioCompraReal(double precioCompraReal) {
        this.precioCompraReal = precioCompraReal;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getCantidadGranel() {
        return cantidadGranel;
    }

    public void setCantidadGranel(double cantidadGranel) {
        this.cantidadGranel = cantidadGranel;
    }

    public SimpleDoubleProperty getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = new SimpleDoubleProperty(descuento);
    }

    public SimpleDoubleProperty getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = new SimpleDoubleProperty(importe);
    }

    public SimpleDoubleProperty getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = new SimpleDoubleProperty(subTotal);
    }

    public double getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(double utilidad) {
        this.utilidad = utilidad;
    }

    public ImageView getImageLote() {
        return imageLote;
    }

    public void setImageLote(ImageView imageLote) {
        this.imageLote = imageLote;
    }

    public ObjectProperty<LocalDate> getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = new SimpleObjectProperty(fechaRegistro);
    }

    public boolean isInventario() {
        return inventario;
    }

    public void setInventario(boolean inventario) {
        this.inventario = inventario;
    }

    public int getUnidadVenta() {
        return unidadVenta;
    }

    public void setUnidadVenta(int unidadVenta) {
        this.unidadVenta = unidadVenta;
    }

    public SimpleStringProperty getUnidadVentaName() {
        return unidadVentaName;
    }

    public void setUnidadVentaName(String unidadVentaName) {
        this.unidadVentaName = new SimpleStringProperty(unidadVentaName);
    }

    public short getMargen() {
        return margen;
    }

    public void setMargen(short margen) {
        this.margen = margen;
    }

    public int getImpuestoArticulo() {
        return impuestoArticulo;
    }

    public void setImpuestoArticulo(int impuestoArticulo) {
        this.impuestoArticulo = impuestoArticulo;
    }

    public String getImpuestoArticuloName() {
        return impuestoArticuloName;
    }

    public void setImpuestoArticuloName(String impuestoArticuloName) {
        this.impuestoArticuloName = impuestoArticuloName;
    }
    
    

}
