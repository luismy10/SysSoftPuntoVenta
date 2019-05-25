
package controller;

import java.awt.Font;
import javafx.scene.image.ImageView;

public class CodBar extends ImageView {
    private String texto;
    
    private Font font;
    
    public CodBar(String value,double x,double y,Font f){
        texto=value;
        setTranslateX(x);
        setTranslateY(y);
        font=f;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
    
    
}
