/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Utilidad {
    private int id;
    private String IdArticulo;
    private String Clave;
    private String NombreMarca;
    private double Cantidad;
    private double CantidadGranel;
    private double CostoUnitario;
    private double PrecioUnitario;
    private double CostoTotal;
    private double PrecioTotal;
    private double Utilidad;
    private boolean ValorInventario;
    private String UnidadCompra;
    private String SimboloMoneda;
    
    public Utilidad(){}

    public Utilidad(String IdArticulo, String Clave, String NombreMarca, double Cantidad, double CantidadGranel, double CostoUnitario, double PrecioUnitario, double CostoTotal, double PrecioTotal, double Utilidad) {
        this.IdArticulo = IdArticulo;
        this.Clave = Clave;
        this.NombreMarca = NombreMarca;
        this.Cantidad = Cantidad;
        this.CantidadGranel = CantidadGranel;
        this.CostoUnitario = CostoUnitario;
        this.PrecioUnitario = PrecioUnitario;
        this.CostoTotal = CostoTotal;
        this.PrecioTotal = PrecioTotal;
        this.Utilidad = Utilidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdArticulo() {
        return IdArticulo;
    }

    public void setIdArticulo(String IdArticulo) {
        this.IdArticulo = IdArticulo;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String Clave) {
        this.Clave = Clave;
    }

    public String getNombreMarca() {
        return NombreMarca;
    }

    public void setNombreMarca(String NombreMarca) {
        this.NombreMarca = NombreMarca;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double Cantidad) {
        this.Cantidad = Cantidad;
    }

    public double getCantidadGranel() {
        return CantidadGranel;
    }

    public void setCantidadGranel(double CantidadGranel) {
        this.CantidadGranel = CantidadGranel;
    }

    public double getCostoUnitario() {
        return CostoUnitario;
    }

    public void setCostoUnitario(double CostoUnitario) {
        this.CostoUnitario = CostoUnitario;
    }

    public double getPrecioUnitario() {
        return PrecioUnitario;
    }

    public void setPrecioUnitario(double PrecioUnitario) {
        this.PrecioUnitario = PrecioUnitario;
    }

    public double getCostoTotal() {
        return CostoTotal;
    }

    public void setCostoTotal(double CostoTotal) {
        this.CostoTotal = CostoTotal;
    }

    public double getPrecioTotal() {
        return PrecioTotal;
    }

    public void setPrecioTotal(double PrecioTotal) {
        this.PrecioTotal = PrecioTotal;
    }

    public double getUtilidad() {
        return Utilidad;
    }

    public void setUtilidad(double Utilidad) {
        this.Utilidad = Utilidad;
    }

    public boolean isValorInventario() {
        return ValorInventario;
    }

    public void setValorInventario(boolean ValorInventario) {
        this.ValorInventario = ValorInventario;
    }    

    public String getUnidadCompra() {
        return UnidadCompra;
    }

    public void setUnidadCompra(String UnidadCompra) {
        this.UnidadCompra = UnidadCompra;
    }

    public String getSimboloMoneda() {
        return SimboloMoneda;
    }

    public void setSimboloMoneda(String SimboloMoneda) {
        this.SimboloMoneda = SimboloMoneda;
    }
      
}
