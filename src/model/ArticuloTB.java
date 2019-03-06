package model;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
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
    private String categoriaName;
    private int marcar;
    private String marcaName;
    private int unidadCompra;
    private String unidadCompraName;
    private int unidadVenta;
    private String unidadVentaName;
    private int presentacion;
    private String presentacionName;

    private int estado;

    private double stockMinimo;
    private double stockMaximo;
    private double cantidad;
    private double precioCompra;
    private double precioCompraReal;

    private int precioVentaId;
    private String precioVentaNombre;
    private double precioVenta;
    private double precioVentaReal;
    private short margen;
    private double utilidad;

    private int precioVentaId2;
    private String precioVentaNombre2;
    private double precioVenta2;
    private short margen2;
    private double utilidad2;

    private int precioVentaId3;
    private String precioVentaNombre3;
    private double precioVenta3;
    private short margen3;
    private double utilidad3;

    private boolean lote;
    private boolean inventario;
    private ImageView imageLote;
    private SimpleStringProperty estadoName;
    private double descuento;
    private double descuentoSumado;
    private double subImporte;

    private double subImporteDescuento;

    private double totalImporte;
    private String imagenTB;
    private ObjectProperty<LocalDate> fechaRegistro;

    private int impuestoArticulo;
    private String impuestoArticuloName;
    private double impuestoValor;
    private double impuestoSumado;

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

    public ArticuloTB(String claveAlterna, String nombreMarca, boolean estado) {
        this.claveAlterna = claveAlterna;
        this.nombreMarca = nombreMarca;
    }

    public ArticuloTB(int impuestoArticulo, String impuestoArticuloName, double impuestoSumado) {
        this.impuestoArticulo = impuestoArticulo;
        this.impuestoArticuloName = impuestoArticuloName;
        this.impuestoSumado = impuestoSumado;
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

    public String getCategoriaName() {
        return categoriaName;
    }

    public void setCategoriaName(String categoriaName) {
        this.categoriaName = categoriaName == null ? "" : categoriaName;
    }

    public int getMarcar() {
        return marcar;
    }

    public void setMarcar(int marcar) {
        this.marcar = marcar;
    }

    public String getMarcaName() {
        return marcaName;
    }

    public void setMarcaName(String marcaName) {
        this.marcaName = marcaName == null ? "" : marcaName;
    }

    public int getUnidadCompra() {
        return unidadCompra;
    }

    public void setUnidadCompra(int unidadCompra) {
        this.unidadCompra = unidadCompra;
    }

    public String getUnidadCompraName() {
        return unidadCompraName;
    }

    public void setUnidadCompraName(String unidadCompraName) {
        this.unidadCompraName = unidadCompraName == null ? "" : unidadCompraName;
    }

    public int getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(int presentacion) {
        this.presentacion = presentacion;
    }

    public String getPresentacionName() {
        return presentacionName;
    }

    public void setPresentacionName(String presentacionName) {
        this.presentacionName = presentacionName;
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

    public double getPrecioVentaReal() {
        return precioVentaReal;
    }

    public void setPrecioVentaReal(double precioVentaReal) {
        this.precioVentaReal = precioVentaReal;
    }

    public short getMargen() {
        return margen;
    }

    public void setMargen(short margen) {
        this.margen = margen;
    }

    public double getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(double utilidad) {
        this.utilidad = utilidad;
    }

    public double getPrecioVenta2() {
        return precioVenta2;
    }

    public void setPrecioVenta2(double precioVenta2) {
        this.precioVenta2 = precioVenta2;
    }

    public short getMargen2() {
        return margen2;
    }

    public void setMargen2(short margen2) {
        this.margen2 = margen2;
    }

    public double getUtilidad2() {
        return utilidad2;
    }

    public void setUtilidad2(double utilidad2) {
        this.utilidad2 = utilidad2;
    }

    public double getPrecioVenta3() {
        return precioVenta3;
    }

    public void setPrecioVenta3(double precioVenta3) {
        this.precioVenta3 = precioVenta3;
    }

    public short getMargen3() {
        return margen3;
    }

    public void setMargen3(short margen3) {
        this.margen3 = margen3;
    }

    public double getUtilidad3() {
        return utilidad3;
    }

    public void setUtilidad3(double utilidad3) {
        this.utilidad3 = utilidad3;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getSubImporte() {
        return subImporte;
    }

    public void setSubImporte(double subImporte) {
        this.subImporte = subImporte;
    }

    public double getSubImporteDescuento() {
        return subImporteDescuento;
    }

    public void setSubImporteDescuento(double subImporteDescuento) {
        this.subImporteDescuento = subImporteDescuento;
    }

    public double getTotalImporte() {
        return totalImporte;
    }

    public void setTotalImporte(double totalImporte) {
        this.totalImporte = totalImporte;
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

    public String getUnidadVentaName() {
        return unidadVentaName;
    }

    public void setUnidadVentaName(String unidadVentaName) {
        this.unidadVentaName = unidadVentaName;
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

    public double getImpuestoValor() {
        return impuestoValor;
    }

    public void setImpuestoValor(double impuestoValor) {
        this.impuestoValor = impuestoValor;
    }

    public double getImpuestoSumado() {
        return impuestoSumado;
    }

    public void setImpuestoSumado(double impuestoSumado) {
        this.impuestoSumado = impuestoSumado;
    }

    public double getDescuentoSumado() {
        return descuentoSumado;
    }

    public void setDescuentoSumado(double descuentoSumado) {
        this.descuentoSumado = descuentoSumado;
    }

    public int getPrecioVentaId() {
        return precioVentaId;
    }

    public void setPrecioVentaId(int precioVentaId) {
        this.precioVentaId = precioVentaId;
    }

    public String getPrecioVentaNombre() {
        return precioVentaNombre;
    }

    public void setPrecioVentaNombre(String precioVentaNombre) {
        this.precioVentaNombre = precioVentaNombre;
    }

    public int getPrecioVentaId2() {
        return precioVentaId2;
    }

    public void setPrecioVentaId2(int precioVentaId2) {
        this.precioVentaId2 = precioVentaId2;
    }

    public String getPrecioVentaNombre2() {
        return precioVentaNombre2;
    }

    public void setPrecioVentaNombre2(String precioVentaNombre2) {
        this.precioVentaNombre2 = precioVentaNombre2;
    }

    public int getPrecioVentaId3() {
        return precioVentaId3;
    }

    public void setPrecioVentaId3(int precioVentaId3) {
        this.precioVentaId3 = precioVentaId3;
    }

    public String getPrecioVentaNombre3() {
        return precioVentaNombre3;
    }

    public void setPrecioVentaNombre3(String precioVentaNombre3) {
        this.precioVentaNombre3 = precioVentaNombre3;
    }

}
