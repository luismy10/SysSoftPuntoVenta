package controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FxTicketController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private VBox hbEncabezado;
    @FXML
    private TextField txtAnchoColumna;
    @FXML
    private ComboBox<String> cbOrdenar;

    private AnchorPane content;

    private TextFieldTicket reference;

    private int sheetWidth;

    private double pointWidth;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pointWidth = 7.825;
        sheetWidth = 40;
        cbOrdenar.getItems().addAll("Encabezado","Detalle","Pie");
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

        HBox hBox = new HBox();
        hBox.setId(id);
        hBox.setPrefHeight(30);
        hBox.setStyle("-fx-padding:0 10 0 0;-fx-border-width: 1 1 1 1;-fx-border-color: #0066ff;");
        hBox.addEventHandler(MouseEvent.MOUSE_PRESSED, (Event event) -> {
            hBox.setStyle("-fx-padding:0 10 0 0;-fx-border-width: 1 1 1 1;-fx-border-color: #0066ff;-fx-background-color: rgb(250, 198, 203);");
        });
        hBox.addEventHandler(MouseEvent.MOUSE_EXITED, (Event event) -> {
            hBox.setStyle("-fx-padding:0 10 0 0;-fx-border-width: 1 1 1 1;-fx-border-color: #0066ff;-fx-background-color: white;");
        });

        ContextMenu contextMenu = new ContextMenu();
        MenuItem remove = new MenuItem("Remover Renglón");
        remove.setGraphic(imgRemove);
        remove.setOnAction((ActionEvent event) -> {
            hbEncabezado.getChildren().remove(hBox);
        });
        MenuItem text = new MenuItem("Agregar Texto");
        text.setGraphic(imgText);
        text.setOnAction((ActionEvent event) -> {
            TextField field = addElementTextField("iu", "Escriba aqui.", 13, Pos.CENTER_LEFT, true);
            hBox.getChildren().add(field);
        });
        MenuItem textUnaLinea = new MenuItem("Agregar Línea");
        textUnaLinea.setGraphic(imgUnaLinea);
        textUnaLinea.setOnAction((ActionEvent event) -> {
            TextField field = addElementTextField("iug", "----------------------------------------", sheetWidth, Pos.CENTER_LEFT, false);
            hBox.getChildren().add(field);
        });
        MenuItem textDosLineas = new MenuItem("Agregar Doble Línea");
        textDosLineas.setGraphic(imgDobleLinea);
        textDosLineas.setOnAction((ActionEvent event) -> {
            TextField field = addElementTextField("iugd", "========================================", sheetWidth, Pos.CENTER_LEFT, false);
            hBox.getChildren().add(field);
        });

        contextMenu.getItems().addAll(text, textUnaLinea, textDosLineas, remove);
        hBox.setOnContextMenuRequested((ContextMenuEvent event) -> {
            contextMenu.show(hBox, event.getSceneX(), event.getSceneY());
            int widthContent = 0;
            for (int i = 0; i < hBox.getChildren().size(); i++) {
                widthContent += ((TextFieldTicket) hBox.getChildren().get(i)).getColumnWidth();
            }
            if ((widthContent + 13) > sheetWidth) {
                text.setDisable(true);
            } else {
                text.setDisable(false);
            }
            if ((widthContent + sheetWidth) > sheetWidth) {
                textUnaLinea.setDisable(true);
            } else {
                textUnaLinea.setDisable(false);
            }
            if ((widthContent + sheetWidth) > sheetWidth) {
                textDosLineas.setDisable(true);
            } else {
                textDosLineas.setDisable(false);
            }
        });
        hbEncabezado.getChildren().add(hBox);
    }

    private TextField addElementTextField(String id, String titulo, int widthColumn, Pos align, boolean editable) {
        TextFieldTicket field = new TextFieldTicket(titulo, id);
        field.setColumnWidth(widthColumn);
        field.setEditable(editable);
        field.setPreferredSize(((double) field.getText().length() * pointWidth), 30);
        field.getStyleClass().add("text-field-ticket");
        field.setAlignment(align);
        field.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (newPropertyValue) {
                field.setStyle("-fx-background-color: #cecece;");
                txtAnchoColumna.setText(field.getColumnWidth() + "");
                reference = field;
            } else {
                field.setStyle("-fx-background-color: white;");
            }
        });

        field.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (field.getText().length() >= field.getColumnWidth()) {
                    field.setText(field.getText().substring(0, field.getColumnWidth()));
                }
            }
        });

        ImageView imgRemove = new ImageView(new Image("/view/remove-item.png"));
        imgRemove.setFitWidth(20);
        imgRemove.setFitHeight(20);

        ImageView imgAdaptParentWidth = new ImageView(new Image("/view/width-parent.png"));
        imgAdaptParentWidth.setFitWidth(20);
        imgAdaptParentWidth.setFitHeight(20);

        ImageView imgTextLeft = new ImageView(new Image("/view/text-left.png"));
        imgTextLeft.setFitWidth(20);
        imgTextLeft.setFitHeight(20);

        ImageView imgTextCenter = new ImageView(new Image("/view/text-center.png"));
        imgTextCenter.setFitWidth(20);
        imgTextCenter.setFitHeight(20);

        ImageView imgTextRight = new ImageView(new Image("/view/text-right.png"));
        imgTextRight.setFitWidth(20);
        imgTextRight.setFitHeight(20);

        ContextMenu contextMenu = new ContextMenu();

        MenuItem remove = new MenuItem("Remover Campo de Texto");
        remove.setGraphic(imgRemove);
        remove.setOnAction((ActionEvent event) -> {
            ((HBox) field.getParent()).getChildren().remove(field);
        });
        MenuItem textAdaptWidth = new MenuItem("Adaptar el Ancho del Padre");
        textAdaptWidth.setGraphic(imgAdaptParentWidth);
        textAdaptWidth.setOnAction((ActionEvent event) -> {
            field.setColumnWidth(sheetWidth);
            field.setPrefWidth(((double) field.getColumnWidth() * pointWidth));
        });
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
        contextMenu.getItems().addAll(remove, textAdaptWidth, new SeparatorMenuItem(), textLeft, textCenter, textRight);
        field.setContextMenu(contextMenu);
        field.setOnContextMenuRequested((ContextMenuEvent event) -> {
            if (((HBox) field.getParent()).getChildren().size() > 1) {
                textAdaptWidth.setDisable(true);
            }
        });
        return field;
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
            ArrayList<HBox> object = new ArrayList<>();
            for (int i = 0; i < hbEncabezado.getChildren().size(); i++) {
                object.add((HBox) hbEncabezado.getChildren().get(i));
            }
            billPrintable.modelTicket(window.getScene().getWindow(), sheetWidth, object.size() + 1 + 5, object, "Ticket", "Error el imprimir el ticket.");
        }
    }

    private void actionAnchoColumnas() {
        if (reference != null) {
            if (Tools.isNumeric(txtAnchoColumna.getText())) {
                int widthContent = 0;
                for (int i = 0; i < ((HBox) reference.getParent()).getChildren().size(); i++) {
                    TextFieldTicket fieldTicket = ((TextFieldTicket) ((HBox) reference.getParent()).getChildren().get(i));
                    if (fieldTicket != reference) {
                        widthContent += fieldTicket.getColumnWidth();
                    }
                }
                if ((widthContent + Integer.parseInt(txtAnchoColumna.getText())) > sheetWidth) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Ticket", "No puede sobrepasar al ancho de la hoja.", false);
                } else {
                    if (reference.getText().length() < Integer.parseInt(txtAnchoColumna.getText())) {
                        reference.setColumnWidth(Integer.parseInt(txtAnchoColumna.getText()));
                        reference.setPrefWidth(((double) reference.getColumnWidth() * pointWidth));
                    } else {
                        reference.setColumnWidth(Integer.parseInt(txtAnchoColumna.getText()));
                        reference.setPrefWidth(((double) reference.getColumnWidth() * pointWidth));
                        reference.setText(reference.getText().substring(0, reference.getColumnWidth()));
                    }
                }
            }
        }
    }

    @FXML
    private void onKeyPressedPrint(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            printTicket();
        }
    }

    @FXML
    private void onActionPrint(ActionEvent event) {
        printTicket();
    }

    @FXML
    private void onMouseClickedEncabezadoAdd(MouseEvent event) {
        if (hbEncabezado.getChildren().isEmpty()) {
            addElement("cb1", "", Pos.CENTER_LEFT, false);
        } else {
            HBox hBox = (HBox) hbEncabezado.getChildren().get(hbEncabezado.getChildren().size() - 1);
            String idGenerate = hBox.getId();
            String codigo = idGenerate.substring(2);
            int valor = Integer.parseInt(codigo) + 1;
            String newCodigo = "cb" + valor;
            addElement(newCodigo, "", Pos.CENTER_LEFT, false);
        }
    }

    @FXML
    private void onActionAnchoColumna(ActionEvent event) {
        actionAnchoColumnas();
    }

    @FXML
    private void onKeyTypedAnchoColumna(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b')) {
            event.consume();
        }
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
