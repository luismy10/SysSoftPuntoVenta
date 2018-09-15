package model;

public class RepresentanteTB {
    private int id;
    private String idProveedor;
    private String idPersona;

    public RepresentanteTB() {
        
    }

    public RepresentanteTB(String idProveedor, String idPersona) {
        this.idProveedor = idProveedor;
        this.idPersona = idPersona;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }
    
    
}
