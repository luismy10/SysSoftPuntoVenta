package controller;

import javafx.scene.control.TextField;

public class TextFieldTicket extends TextField {
    
    private int columnWidth;

    public TextFieldTicket(String title, String id) {
        super(title);
        setId(id);
    }

    public void setPreferredSize(double width, double height) {
        setPrefWidth(width);
        setPrefHeight(height);
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }
    
    
}
