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
    
    //select dv.IdArticulo, a.Clave, a.NombreMarca, dv.Cantidad, dv.CantidadGranel, dv.CostoVenta, dv.PrecioVenta, dv.PrecioVentaGranel, a.ValorInventario, dbo.Fc_Obtener_Nombre_Detalle(a.UnidadCompra,'0013') as UnidadCompra, m.Simbolo
    
    private int id;
    private String IdArticulo;
    private String Clave;
    private String NombreMarca;
    private double Cantidad;
    private double CantidadGranel;
    private double CostoVenta;
    private double PrecioVenta;
    private double PrecioVentaGranel;
    private boolean ValorInventario;
    private String UnidadCompra;
    private String SimboloMoneda;
    
    private double Utilidad;
    
    public Utilidad(){}

    public Utilidad(int id, String IdArticulo, String Clave, String NombreMarca, double Cantidad, double CantidadGranel, double CostoVenta, double PrecioVenta, double PrecioVentaGranel, boolean ValorInventario, String UnidadCompra, String SimboloMoneda, double Utilidad) {
        this.id = id;
        this.IdArticulo = IdArticulo;
        this.Clave = Clave;
        this.NombreMarca = NombreMarca;
        this.Cantidad = Cantidad;
        this.CantidadGranel = CantidadGranel;
        this.CostoVenta = CostoVenta;
        this.PrecioVenta = PrecioVenta;
        this.PrecioVentaGranel = PrecioVentaGranel;
        this.ValorInventario = ValorInventario;
        this.UnidadCompra = UnidadCompra;
        this.SimboloMoneda = SimboloMoneda;
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

    public double getCostoVenta() {
        return CostoVenta;
    }

    public void setCostoVenta(double CostoVenta) {
        this.CostoVenta = CostoVenta;
    }

    public double getPrecioVenta() {
        return PrecioVenta;
    }

    public void setPrecioVenta(double PrecioVenta) {
        this.PrecioVenta = PrecioVenta;
    }

    public double getPrecioVentaGranel() {
        return PrecioVentaGranel;
    }

    public void setPrecioVentaGranel(double PrecioVentaGranel) {
        this.PrecioVentaGranel = PrecioVentaGranel;
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

    public double getUtilidad() {
        return Utilidad;
    }

    public void setUtilidad(double Utilidad) {
        this.Utilidad = Utilidad;
    }

         
}
