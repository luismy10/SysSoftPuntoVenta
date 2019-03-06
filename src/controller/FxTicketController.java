package controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FxTicketController implements Initializable {

    @FXML
    private ScrollPane window;
    @FXML
    private VBox hbEncabezado;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        File file = new File("c:\\temp\\modelo.json");
//        if (file.exists()) {
//            JSONObject ticket = obtenerObjetoJSON(leerArchivoTexto(file.getAbsolutePath()));
//            JSONObject cabecera = obtenerObjetoJSON(ticket.get("Ticket").toString());
//            if (cabecera.get("Cabecera") != null) {
//                JSONObject cabeceraObjects = obtenerObjetoJSON(cabecera.get("Cabecera").toString());
//                addElementoWithId("cb", cabeceraObjects.get("cb_1").toString(), Pos.CENTER, true);
//                addElementoWithId("cb", cabeceraObjects.get("cb_2").toString(), Pos.CENTER, true);
//                addElementoWithId("cb", cabeceraObjects.get("cb_3").toString(), Pos.CENTER, true);
//                addElementoWithId("cb", cabeceraObjects.get("cb_4").toString(), Pos.CENTER, true);
//                addElementoWithId("cb", cabeceraObjects.get("cb_5").toString(), Pos.CENTER, true);
//                addElementoWithId("cb", cabeceraObjects.get("cb_6").toString(), Pos.CENTER, true);
//                addElementoWithId("cb", cabeceraObjects.get("cb_7").toString(), Pos.CENTER, true);
//                addElementoWithId("cb", cabeceraObjects.get("cb_8").toString(), Pos.CENTER_LEFT, false);
//            }
//        }
    }

    private void addElement(String id, String titulo, Pos align, boolean editable) {
        ImageView imgRemove = new ImageView(new Image("/view/remove-item.png"));
        imgRemove.setFitWidth(20);
        imgRemove.setFitHeight(20);

        ImageView imgText = new ImageView(new Image("/view/text.png"));
        imgText.setFitWidth(20);
        imgText.setFitHeight(20);

        ImageView imgUnaLinea = new ImageView(new Image("/view/linea.png"));
        imgUnaLinea.setFitWidth(20);
        imgUnaLinea.setFitHeight(20);

        ImageView imgDobleLinea = new ImageView(new Image("/view/doble-linea.png"));
        imgDobleLinea.setFitWidth(20);
        imgDobleLinea.setFitHeight(20);

        ImageView imgTextLeft = new ImageView(new Image("/view/text-left.png"));
        imgTextLeft.setFitWidth(20);
        imgTextLeft.setFitHeight(20);

        ImageView imgTextCenter = new ImageView(new Image("/view/text-center.png"));
        imgTextCenter.setFitWidth(20);
        imgTextCenter.setFitHeight(20);

        ImageView imgTextRight = new ImageView(new Image("/view/text-right.png"));
        imgTextRight.setFitWidth(20);
        imgTextRight.setFitHeight(20);

        TextField field = new TextField(titulo);
        field.setId(id);
        field.setEditable(editable);
        field.setPrefHeight(30);
        field.setStyle("-fx-border-width: 1 1 1 1;-fx-border-color: #0066ff;" + (editable ? "-fx-text-fill: #009900;" : "-fx-text-fill: black;"));
        field.getStyleClass().add("labelMonospaced12");
        field.setAlignment(align);
        field.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (newPropertyValue) {
                field.setStyle("-fx-border-width: 1 1 1 1;-fx-border-color: #0066ff;-fx-text-fill: black;-fx-background-color: rgb(250, 198, 203);");
            } else {
                field.setStyle("-fx-border-width: 1 1 1 1;-fx-border-color: #0066ff;" + (editable ? "-fx-text-fill: #009900;" : "-fx-text-fill:black;") + "-fx-background-color: white;");
            }
        });
        ContextMenu contextMenu = new ContextMenu();
        MenuItem remove = new MenuItem("Remover Renglón");
        remove.setGraphic(imgRemove);
        remove.setOnAction((ActionEvent event) -> {
            hbEncabezado.getChildren().remove(field);
        });
        MenuItem text = new MenuItem("Agregar Texto");
        text.setGraphic(imgText);
        text.setOnAction((ActionEvent event) -> {
            field.setText("Escriba aqui");
            field.setEditable(true);
        });
        MenuItem textUnaLinea = new MenuItem("Agregar Línea");
        textUnaLinea.setGraphic(imgUnaLinea);
        textUnaLinea.setOnAction((ActionEvent event) -> {
            field.setText("----------------------------------------");
            field.setEditable(false);
        });
        MenuItem textDosLineas = new MenuItem("Agregar Doble Línea");
        textDosLineas.setGraphic(imgDobleLinea);
        textDosLineas.setOnAction((ActionEvent event) -> {
            field.setText("========================================");
            field.setEditable(false);
        });

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

        MenuItem textLeft = new MenuItem("Alineación Izquierda");
        textLeft.setGraphic(imgTextLeft);
        textLeft.setOnAction((ActionEvent event) -> {
            if (!field.getText().isEmpty()) {
                field.setAlignment(Pos.CENTER_LEFT);
            }
        });
        MenuItem textCenter = new MenuItem("Alineación Central");
        textCenter.setGraphic(imgTextCenter);
        textCenter.setOnAction((ActionEvent event) -> {
            if (!field.getText().isEmpty()) {
                field.setAlignment(Pos.CENTER);
            }
        });
        MenuItem textRight = new MenuItem("Alineación Derecha");
        textRight.setGraphic(imgTextRight);
        textRight.setOnAction((ActionEvent event) -> {
            if (!field.getText().isEmpty()) {
                field.setAlignment(Pos.CENTER_RIGHT);
            }
        });

        contextMenu.getItems().addAll(text, textUnaLinea, textDosLineas, remove, separatorMenuItem, textLeft, textCenter, textRight);
        field.setContextMenu(contextMenu);
        hbEncabezado.getChildren().add(field);
    }

    private void addElementoWithId(String idText, String value, Pos align, boolean editable) {
        if (hbEncabezado.getChildren().isEmpty()) {
            addElement(idText + "1", value, align, editable);
        } else {
            TextField field = (TextField) hbEncabezado.getChildren().get(hbEncabezado.getChildren().size() - 1);
            String idGenerate = field.getId();
            String codigo = idGenerate.substring(2);
            int valor = Integer.parseInt(codigo) + 1;
            String newCodigo = idText + valor;
            addElement(newCodigo, value, align, editable);
        }
    }

    private String leerArchivoTexto(String ruta) {
        String contenido = "";
        InputStream inStream = null;
        BufferedInputStream bis = null;
        try {
            inStream = new FileInputStream(ruta);
            bis = new BufferedInputStream(inStream);
            while (bis.available() > 0) {
                char c = (char) bis.read();
                contenido += c;
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
        return contenido;
    }

    private JSONObject obtenerObjetoJSON(final String codigoJSON) {
        JSONParser lector = new JSONParser();
        JSONObject objectJSON = null;
        try {
            Object recuperado = lector.parse(codigoJSON);
            objectJSON = (JSONObject) recuperado;
        } catch (ParseException ex) {
            System.out.println("Posicion:" + ex.getPosition());
            System.out.println(ex);
        }
        return objectJSON;
    }

    private void printTicket() {
        BillPrintable billPrintable = new BillPrintable();
        if (!hbEncabezado.getChildren().isEmpty()) {
            ArrayList<TextField> object = new ArrayList<>();
            for (int i = 0; i < hbEncabezado.getChildren().size(); i++) {
                object.add((TextField) hbEncabezado.getChildren().get(i));
            }
            billPrintable.modelTicket(window.getScene().getWindow(), object.size() + 1, object, "Tipo Documento", "Error el imprimir el ticket.");
        }
    }

    @FXML
    private void onKeyPressedPrint(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionPrint(ActionEvent event) {
    }

    @FXML
    private void onMouseClickedEncabezadoAdd(MouseEvent event) {
        if (hbEncabezado.getChildren().isEmpty()) {
            addElement("cb1", "", Pos.CENTER_LEFT, false);
        } else {
            TextField field = (TextField) hbEncabezado.getChildren().get(hbEncabezado.getChildren().size() - 1);
            String idGenerate = field.getId();
            String codigo = idGenerate.substring(2);
            int valor = Integer.parseInt(codigo) + 1;
            String newCodigo = "cb" + valor;
            addElement(newCodigo, "", Pos.CENTER_LEFT, false);
        }
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
