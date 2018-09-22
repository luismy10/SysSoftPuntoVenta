package model;

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
    
    
}
