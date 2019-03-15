package controller;

import javafx.scene.control.TextField;

public class TextFieldTicket extends TextField {

    private int columnWidth;

    private boolean multilineas;

    private int lines;
    
    private String variable;

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

    public boolean isMultilineas() {
        return multilineas;
    }

    public void setMultilineas(boolean multilineas) {
        this.multilineas = multilineas;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }


}
