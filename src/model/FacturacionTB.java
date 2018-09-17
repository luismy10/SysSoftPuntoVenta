package model;

public class FacturacionTB {

    private Long idFacturacion;
    private int tipoDocumentoFacturacion;
    private String numeroDocumentoFacturacion;
    private String razonSocial;
    private String nombreComercial;
    private int pais;
    private int region;
    private int provincia;
    private int distrito;
    private int modena;

    public FacturacionTB() {
    }


    public Long getIdFacturacion() {
        return idFacturacion;
    }

    public void setIdFacturacion(Long idFacturacion) {
        this.idFacturacion = idFacturacion;
    }


    public int getTipoDocumentoFacturacion() {
        return tipoDocumentoFacturacion;
    }

    public void setTipoDocumentoFacturacion(int tipoDocumentoFacturacion) {
        this.tipoDocumentoFacturacion = tipoDocumentoFacturacion;
    }

    public String getNumeroDocumentoFacturacion() {
        return numeroDocumentoFacturacion;
    }

    public void setNumeroDocumentoFacturacion(String numeroDocumentoFacturacion) {
        this.numeroDocumentoFacturacion = numeroDocumentoFacturacion;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public int getPais() {
        return pais;
    }

    public void setPais(int pais) {
        this.pais = pais;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getProvincia() {
        return provincia;
    }

    public void setProvincia(int provincia) {
        this.provincia = provincia;
    }

    public int getDistrito() {
        return distrito;
    }

    public void setDistrito(int distrito) {
        this.distrito = distrito;
    }

    public int getModena() {
        return modena;
    }

    public void setModena(int modena) {
        this.modena = modena;
    }

}
