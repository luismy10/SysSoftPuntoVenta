
package model;

public class PreciosTB {
    private int idPrecios;
    private String nombre;
    private double precio;
    private double precioReal;
    private short margen;
    private double utilidad;

    public PreciosTB() {
    }

    public int getIdPrecios() {
        return idPrecios;
    }

    public void setIdPrecios(int idPrecios) {
        this.idPrecios = idPrecios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioReal() {
        return precioReal;
    }

    public void setPrecioReal(double precioReal) {
        this.precioReal = precioReal;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
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
    
    
}
