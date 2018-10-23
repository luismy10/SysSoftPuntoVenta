package model;

public class RolTB {

    private int idRol;
    private String nombre;

    public RolTB() {
        
    }

    public RolTB(int idRol, String nombre) {
        this.idRol = idRol;
        this.nombre = nombre;
    }
    
    

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return  nombre ;
    }
    
    

}
