package controller;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Text extends Label {

    private boolean bold;

    private boolean italic;
    
    private FontWeight fontWight;
    
    private FontPosture fontPosture;

    public Text(String text, double x, double y) {
        setText(text);
        setCursor(Cursor.MOVE);
        setLayoutX(x);
        setLayoutY(y);
        setTextFill(Color.BLACK);
        setFont(Font.font("Lucida Sans Typewriter", FontWeight.BOLD, FontPosture.REGULAR,16));
        bold = true;
        italic = false;
        fontWight=FontWeight.BOLD;
        fontPosture=FontPosture.REGULAR;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public FontWeight getFontWight() {
        return fontWight;
    }

    public FontPosture getFontPosture() {
        return fontPosture;
    }

    public void setFontWight(FontWeight fontWight) {
        this.fontWight = fontWight;
    }

    public void setFontPosture(FontPosture fontPosture) {
        this.fontPosture = fontPosture;
    }
    
    
    
}
