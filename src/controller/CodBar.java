
package controller;

import javafx.scene.image.ImageView;

public class CodBar extends ImageView {
    private String texto;
    
    public CodBar(String value,double x,double y){
        texto=value;
        setLayoutX(x);
        setLayoutY(y);
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    
    
    
}
