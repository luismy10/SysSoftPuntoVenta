package model;

import javafx.scene.image.ImageView;

public class TipoDocumentoTB {

    private int idTipoDocumento;
    private String nombre;
    private boolean predeterminado;
    private ImageView imagePredeterminado;

    public TipoDocumentoTB() {
    }

    public TipoDocumentoTB(int idTipoDocumento, String nombre) {
        this.idTipoDocumento = idTipoDocumento;
        this.nombre = nombre;
    }
    
    

    public TipoDocumentoTB(int idTipoDocumento, String nombre, boolean predeterminado) {
        this.idTipoDocumento = idTipoDocumento;
        this.nombre = nombre;
        this.predeterminado = predeterminado;
    }

    public TipoDocumentoTB(int idTipoDocumento, String nombre, boolean predeterminado, ImageView imagePredeterminado) {
        this.idTipoDocumento = idTipoDocumento;
        this.nombre = nombre;
        this.predeterminado = predeterminado;
        this.imagePredeterminado = imagePredeterminado;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isPredeterminado() {
        return predeterminado;
    }

    public void setPredeterminado(boolean predeterminado) {
        this.predeterminado = predeterminado;
    }

    public ImageView getImagePredeterminado() {
        return imagePredeterminado;
    }

    public void setImagePredeterminado(ImageView imagePredeterminado) {
        this.imagePredeterminado = imagePredeterminado;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
