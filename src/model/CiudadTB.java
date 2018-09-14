package model;

public class CiudadTB {

    private int ciudadID;
    private String ciudadNombre;
    private String paisCodigo;
    private String ciudadDistrito;
    private int iudadPoblacion;

    public CiudadTB() {
    }

    public CiudadTB(int ciudadID, String ciudadDistrito) {
        this.ciudadID = ciudadID;
        this.ciudadDistrito = ciudadDistrito;
    }

    public CiudadTB(int ciudadID, String ciudadNombre, String paisCodigo, String ciudadDistrito, int iudadPoblacion) {
        this.ciudadID = ciudadID;
        this.ciudadNombre = ciudadNombre;
        this.paisCodigo = paisCodigo;
        this.ciudadDistrito = ciudadDistrito;
        this.iudadPoblacion = iudadPoblacion;
    }

    public int getCiudadID() {
        return ciudadID;
    }

    public void setCiudadID(int ciudadID) {
        this.ciudadID = ciudadID;
    }

    public String getCiudadNombre() {
        return ciudadNombre;
    }

    public void setCiudadNombre(String ciudadNombre) {
        this.ciudadNombre = ciudadNombre;
    }

    public String getPaisCodigo() {
        return paisCodigo;
    }

    public void setPaisCodigo(String paisCodigo) {
        this.paisCodigo = paisCodigo;
    }

    public String getCiudadDistrito() {
        return ciudadDistrito;
    }

    public void setCiudadDistrito(String ciudadDistrito) {
        this.ciudadDistrito = ciudadDistrito;
    }

    public int getIudadPoblacion() {
        return iudadPoblacion;
    }

    public void setIudadPoblacion(int iudadPoblacion) {
        this.iudadPoblacion = iudadPoblacion;
    }

    @Override
    public String toString() {
        return ciudadDistrito;
    }

}
