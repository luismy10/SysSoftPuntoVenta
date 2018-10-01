package model;

public class ProvinciaTB {

    private int idProvincia;
    private String provincia;
    private String idCiudad;

    public ProvinciaTB() {
    }

    public ProvinciaTB(int idProvincia, String provincia) {
        this.idProvincia = idProvincia;
        this.provincia = provincia;
    }
    
    

    public int getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(int idProvincia) {
        this.idProvincia = idProvincia;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(String idCiudad) {
        this.idCiudad = idCiudad;
    }
    
      @Override
    public String toString() {
        return provincia;
    }
    
}
