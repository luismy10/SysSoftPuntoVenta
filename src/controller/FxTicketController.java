package controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FxTicketController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private VBox hbEncabezado;
    @FXML
    private VBox hbDetalleCabecera;
    @FXML
    private VBox hbPie;
    @FXML
    private TextField txtAnchoColumna;
    @FXML
    private ComboBox<String> cbOrdenar;
    @FXML
    private ComboBox<Integer> cbMultilinea;
    @FXML
    private ListView<?> lvLista;

    private AnchorPane content;

    private TextFieldTicket reference;

    private int sheetWidth;

    private double pointWidth;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pointWidth = 7.825;
        sheetWidth = 40;
        cbOrdenar.getItems().addAll("Encabezado", "Detalle", "Pie");
        cbMultilinea.getItems().addAll(1, 2);
    }

    private void InitializationTransparentBackground() {
        Session.pane.setStyle("-fx-background-color: black");
        Session.pane.setTranslateX(0);
        Session.pane.setTranslateY(0);
        Session.pane.setPrefWidth(Session.WIDTH_WINDOW);
        Session.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.pane.setOpacity(0.7f);
        content.getChildren().add(Session.pane);
    }

    private void editTicket() {
        File file = new File("./archivos/ticketventa.json");
        if (file.exists()) {
            JSONObject cabecera = obtenerObjetoJSON(leerArchivoTexto(file.getAbsolutePath()));
            hbEncabezado.getChildren().clear();
            hbDetalleCabecera.getChildren().clear();
            hbPie.getChildren().clear();
            if (cabecera.get("cabecera") != null) {
                JSONObject cabeceraObjects = obtenerObjetoJSON(cabecera.get("cabecera").toString());
                for (int i = 0; i < cabeceraObjects.size(); i++) {
                    HBox box = generateElement(hbEncabezado, "cb");
                    JSONObject objectObtener = obtenerObjetoJSON(cabeceraObjects.get("cb_" + (i + 1)).toString());
                    if (objectObtener.get("text") != null) {
                        JSONObject object = obtenerObjetoJSON(objectObtener.get("text").toString());
                        TextFieldTicket field = addElementTextField("iu", object.get("value").toString(), Boolean.valueOf(object.get("multiline").toString()), Integer.parseInt(object.get("lines").toString()), Integer.parseInt(object.get("width").toString()), getAlignment(object.get("align").toString()), false);
                        box.getChildren().add(field);
                    } else if (objectObtener.get("list") != null) {
                        JSONArray array = obtenerArrayJSON(objectObtener.get("list").toString());
                        Iterator it = array.iterator();
                        while (it.hasNext()) {
                            JSONObject object = obtenerObjetoJSON(it.next().toString());
                            TextFieldTicket field = addElementTextField("iu", object.get("value").toString(), Boolean.valueOf(object.get("multiline").toString()), Integer.parseInt(object.get("lines").toString()), Integer.parseInt(object.get("width").toString()), getAlignment(object.get("align").toString()), false);
                            box.getChildren().add(field);
                        }
                    }
                }
            }
        }
    }

    private Pos getAlignment(String align) {
        switch (align) {
            case "CENTER":
                return Pos.CENTER;
            case "CENTER_LEFT":
                return Pos.CENTER_LEFT;
            case "CENTER_RIGHT":
                return Pos.CENTER_RIGHT;
            default:
                return Pos.CENTER_LEFT;
        }
    }

    private void saveTicket() {
        try {
            JSONObject sampleObject = new JSONObject();
            JSONObject cabecera = new JSONObject();
            for (int i = 0; i < hbEncabezado.getChildren().size(); i++) {
                HBox hBox = (HBox) hbEncabezado.getChildren().get(i);
                JSONObject cb = new JSONObject();
                if (hBox.getChildren().size() > 1) {
                    JSONArray kids = new JSONArray();
                    for (int v = 0; v < hBox.getChildren().size(); v++) {
                        TextFieldTicket field = (TextFieldTicket) hBox.getChildren().get(v);
                        JSONObject cbkid = new JSONObject();
                        cbkid.put("value", field.getText());
                        cbkid.put("width", field.getColumnWidth());
                        cbkid.put("align", field.getAlignment().toString());
                        cbkid.put("multiline", field.isMultilineas());
                        cbkid.put("lines", field.getLines());
                        kids.add(cbkid);
                    }
                    cb.put("list", kids);
                } else {
                    TextFieldTicket field = (TextFieldTicket) hBox.getChildren().get(0);
                    JSONObject cbkid = new JSONObject();
                    cbkid.put("value", field.getText());
                    cbkid.put("width", field.getColumnWidth());
                    cbkid.put("align", field.getAlignment().toString());
                    cbkid.put("multiline", field.isMultilineas());
                    cbkid.put("lines", field.getLines());
                    cb.put("text", cbkid);
                }
                cabecera.put("cb_" + (i + 1), cb);
            }

            JSONObject detalle = new JSONObject();

            JSONObject pie = new JSONObject();
            for (int i = 0; i < hbPie.getChildren().size(); i++) {
                HBox hBox = (HBox) hbPie.getChildren().get(i);
                JSONObject cb = new JSONObject();
                if (hBox.getChildren().size() > 1) {
                    JSONArray kids = new JSONArray();
                    for (int v = 0; v < hBox.getChildren().size(); v++) {
                        TextFieldTicket field = (TextFieldTicket) hBox.getChildren().get(v);
                        JSONObject cbkid = new JSONObject();
                        cbkid.put("value", field.getText());
                        cbkid.put("width", field.getColumnWidth());
                        cbkid.put("align", field.getAlignment().toString());
                        cbkid.put("multiline", field.isMultilineas());
                        cbkid.put("lines", field.getLines());
                        kids.add(cbkid);
                    }
                    cb.put("list", kids);
                } else {
                    TextFieldTicket field = (TextFieldTicket) hBox.getChildren().get(0);
                    JSONObject cbkid = new JSONObject();
                    cbkid.put("value", field.getText());
                    cbkid.put("width", field.getColumnWidth());
                    cbkid.put("align", field.getAlignment().toString());
                    cbkid.put("multiline", field.isMultilineas());
                    cbkid.put("lines", field.getLines());
                    cb.put("text", cbkid);
                }
                pie.put("cp_" + (i + 1), cb);
            }

            sampleObject.put("cabecera", cabecera);
            sampleObject.put("detalle", detalle);
            sampleObject.put("pie", pie);

            Files.write(Paths.get("./archivos/ticketventa.json"), sampleObject.toJSONString().getBytes());
        } catch (IOException ex) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Ticket", "No se pudo guardar la hoja con problemas de formato.", false);
            System.out.println(ex.getLocalizedMessage());
            System.out.println(ex);
        }
    }

    private void searchTicket() {
        try {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_TICKETBUSQUEDA);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxTicketBusquedaController controller = fXMLLoader.getController();
            controller.setInitTicketController(this);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Seleccionar formato", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();

        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private void windowTextMultilinea(HBox hBox) {
        try {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_TICKETMULTILINEA);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxTicketMultilineaController controller = fXMLLoader.getController();
            controller.setInitTicketController(this);
            controller.setLoadComponent(hBox, sheetWidth);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Agregar texto multilinea", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();

        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private HBox addElement(VBox contenedor, String id) {
        ImageView imgRemove = new ImageView(new Image("/view/remove-item.png"));
        imgRemove.setFitWidth(20);
        imgRemove.setFitHeight(20);

        ImageView imgText = new ImageView(new Image("/view/text.png"));
        imgText.setFitWidth(20);
        imgText.setFitHeight(20);

        ImageView imgTextLines = new ImageView(new Image("/view/text-lines.png"));
        imgTextLines.setFitWidth(20);
        imgTextLines.setFitHeight(20);

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
            contenedor.getChildren().remove(hBox);
        });
        MenuItem text = new MenuItem("Agregar Texto");
        text.setGraphic(imgText);
        text.setOnAction((ActionEvent event) -> {
            TextFieldTicket field = addElementTextField("iu", "Escriba aqui.", false, 0, 13, Pos.CENTER_LEFT, true);
            hBox.getChildren().add(field);
        });
        MenuItem textMultilinea = new MenuItem("Agregar Texto Multilínea");
        textMultilinea.setGraphic(imgTextLines);
        textMultilinea.setOnAction((ActionEvent event) -> {
            windowTextMultilinea(hBox);
        });
        MenuItem textUnaLinea = new MenuItem("Agregar Línea");
        textUnaLinea.setGraphic(imgUnaLinea);
        textUnaLinea.setOnAction((ActionEvent event) -> {
            String value = "";
            for (int i = 0; i < sheetWidth; i++) {
                value += "-";
            }
            TextFieldTicket field = addElementTextField("iug", value, false, 0, sheetWidth, Pos.CENTER_LEFT, false);
            hBox.getChildren().add(field);
        });
        MenuItem textDosLineas = new MenuItem("Agregar Doble Línea");
        textDosLineas.setGraphic(imgDobleLinea);
        textDosLineas.setOnAction((ActionEvent event) -> {
            String value = "";
            for (int i = 0; i < sheetWidth; i++) {
                value += "=";
            }
            TextFieldTicket field = addElementTextField("iugd", value, false, 0, sheetWidth, Pos.CENTER_LEFT, false);
            hBox.getChildren().add(field);
        });

        contextMenu.getItems().addAll(text, textMultilinea, textUnaLinea, textDosLineas, remove);
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

            int widthNew = sheetWidth - widthContent;
//            System.out.println(widthContent + " - " + widthNew);
            if ((widthContent + widthNew) <= sheetWidth) {
                textMultilinea.setDisable(false);
            } else {
                textMultilinea.setDisable(true);
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
        contenedor.getChildren().add(hBox);
        return hBox;
    }

    public TextFieldTicket addElementTextField(String id, String titulo, boolean multilinea, int lines, int widthColumn, Pos align, boolean editable) {
        TextFieldTicket field = new TextFieldTicket(titulo, id);
        field.setMultilineas(multilinea);
        field.setLines(lines);
        field.setColumnWidth(widthColumn);
        field.setEditable(editable);
        field.setPreferredSize((double) widthColumn * pointWidth, 30);
        field.getStyleClass().add("text-field-ticket");
        field.setAlignment(align);
        field.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (newPropertyValue) {
                field.setStyle("-fx-background-color: #cecece;");
                txtAnchoColumna.setText(field.getColumnWidth() + "");
                cbMultilinea.setDisable(!field.isMultilineas());
                reference = field;
            } else {
                field.setStyle("-fx-background-color: white;");
            }
        });

        field.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (!field.isMultilineas()) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (field.getText().length() >= field.getColumnWidth()) {
                        field.setText(field.getText().substring(0, field.getColumnWidth()));
                    }
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
            if (field.isMultilineas()) {
                textLeft.setDisable(true);
                textCenter.setDisable(true);
                textRight.setDisable(true);
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

    private JSONArray obtenerArrayJSON(final String codigoJSON) {
        JSONParser lector = new JSONParser();
        JSONArray arrayJSON = null;

        try {
            Object recuperado = lector.parse(codigoJSON);
            arrayJSON = (JSONArray) recuperado;
        } catch (ParseException e) {
            System.out.println("Posicion: " + e.getPosition());
            System.out.println(e);
        }

        return arrayJSON;
    }

    private void printTicket() {
        BillPrintable billPrintable = new BillPrintable();
        if (!hbEncabezado.getChildren().isEmpty() && !hbDetalleCabecera.getChildren().isEmpty() && !hbPie.getChildren().isEmpty()) {
            ArrayList<HBox> object = new ArrayList<>();
            int rows = 0;
            int lines = 0;
            for (int i = 0; i < hbEncabezado.getChildren().size(); i++) {
                object.add((HBox) hbEncabezado.getChildren().get(i));
                HBox box = ((HBox) hbEncabezado.getChildren().get(i));
                rows++;
                for (int j = 0; j < box.getChildren().size(); j++) {
                    lines += ((TextFieldTicket) box.getChildren().get(j)).getLines();
                }
            }
            for (int i = 0; i < hbDetalleCabecera.getChildren().size(); i++) {
                object.add((HBox) hbDetalleCabecera.getChildren().get(i));
                HBox box = ((HBox) hbDetalleCabecera.getChildren().get(i));
                rows++;
                for (int j = 0; j < box.getChildren().size(); j++) {
                    lines += ((TextFieldTicket) box.getChildren().get(j)).getLines();
                }
            }
            for (int i = 0; i < hbPie.getChildren().size(); i++) {
                object.add((HBox) hbPie.getChildren().get(i));
                HBox box = ((HBox) hbPie.getChildren().get(i));
                rows++;
                for (int j = 0; j < box.getChildren().size(); j++) {
                    lines += ((TextFieldTicket) box.getChildren().get(j)).getLines();
                }
            }

            billPrintable.modelTicket(window.getScene().getWindow(), sheetWidth, rows + lines + 1 + 5, lines, object, "Ticket", "Error el imprimir el ticket.");
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
                    if (!reference.isMultilineas()) {
                        if (reference.getText().length() < Integer.parseInt(txtAnchoColumna.getText())) {
                            reference.setColumnWidth(Integer.parseInt(txtAnchoColumna.getText()));
                            reference.setPrefWidth(((double) reference.getColumnWidth() * pointWidth));
                        } else {
                            reference.setColumnWidth(Integer.parseInt(txtAnchoColumna.getText()));
                            reference.setPrefWidth(((double) reference.getColumnWidth() * pointWidth));
                            reference.setText(reference.getText().substring(0, reference.getColumnWidth()));
                        }
                    } else {
                        if (reference.getText().length() < Integer.parseInt(txtAnchoColumna.getText())) {
                            reference.setColumnWidth(Integer.parseInt(txtAnchoColumna.getText()));
                            reference.setPrefWidth(((double) reference.getColumnWidth() * pointWidth));
                        } else {
                            reference.setColumnWidth(Integer.parseInt(txtAnchoColumna.getText()));
                            reference.setPrefWidth(((double) reference.getColumnWidth() * pointWidth));
                        }
                    }
                }
            }
        }
    }

    private HBox generateElement(VBox contenedor, String id) {
        if (contenedor.getChildren().isEmpty()) {
            return addElement(contenedor, id + "1");
        } else {
            HBox hBox = (HBox) contenedor.getChildren().get(contenedor.getChildren().size() - 1);
            String idGenerate = hBox.getId();
            String codigo = idGenerate.substring(2);
            int valor = Integer.parseInt(codigo) + 1;
            String newCodigo = id + valor;
            return addElement(contenedor, newCodigo);
        }
    }

    @FXML
    private void onActionMultilinea(ActionEvent event) {

    }

    @FXML
    private void onKeyPressEditar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            editTicket();
        }
    }

    @FXML
    private void onActionEditar(ActionEvent event) {
        editTicket();
    }

    @FXML
    private void onKeyPressedSave(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            saveTicket();
        }
    }

    @FXML
    private void onActionSave(ActionEvent event) {
        saveTicket();
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
        generateElement(hbEncabezado, "cb");
    }

    @FXML
    private void onMouseClickedPieAdd(MouseEvent event) {
        if (hbPie.getChildren().isEmpty()) {
            addElement(hbPie, "cp1");
        } else {
            HBox hBox = (HBox) hbPie.getChildren().get(hbPie.getChildren().size() - 1);
            String idGenerate = hBox.getId();
            String codigo = idGenerate.substring(2);
            int valor = Integer.parseInt(codigo) + 1;
            String newCodigo = "cp" + valor;
            addElement(hbPie, newCodigo);
        }
    }

    @FXML
    private void onMouseClickedDetalleCabeceraAdd(MouseEvent event) {
        if (hbDetalleCabecera.getChildren().isEmpty()) {
            addElement(hbDetalleCabecera, "dr1");
        } else {
            HBox hBox = (HBox) hbDetalleCabecera.getChildren().get(hbDetalleCabecera.getChildren().size() - 1);
            String idGenerate = hBox.getId();
            String codigo = idGenerate.substring(2);
            int valor = Integer.parseInt(codigo) + 1;
            String newCodigo = "dr" + valor;
            addElement(hbDetalleCabecera, newCodigo);
        }
    }

    @FXML
    private void onMouseClickedDetalleCuerpoAdd(MouseEvent event) {

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

    @FXML
    private void onKeyPressedSearch(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            searchTicket();
        }
    }

    @FXML
    private void onActionSearch(ActionEvent event) {
        searchTicket();
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
