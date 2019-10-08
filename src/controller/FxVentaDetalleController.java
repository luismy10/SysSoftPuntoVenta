package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ArticuloTB;
import model.EmpleadoTB;
import model.ImpuestoADO;
import model.ImpuestoTB;
import model.VentaADO;
import model.VentaTB;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FxVentaDetalleController implements Initializable {

    @FXML
    private ScrollPane window;
    @FXML
    private Label lblLoad;
    @FXML
    private Text lblFechaVenta;
    @FXML
    private Text lblComprobante;
    @FXML
    private Text lblTotal;
    @FXML
    private Text lblCliente;
    @FXML
    private Text lblTipo;
    @FXML
    private Text lblEstado;
    @FXML
    private Text lblObservaciones;
    @FXML
    private Text lblVendedor;
    @FXML
    private Text lblSerie;
    @FXML
    private GridPane gpList;
    @FXML
    private Text lblValorVenta;
    @FXML
    private Text lblDescuento;
    @FXML
    private Text lblSubTotal;
    @FXML
    private VBox hbAgregarImpuesto;
    @FXML
    private Text lblTotalVenta;

    private AnchorPane windowinit;

    private AnchorPane vbContent;

    private String idVenta;

    private double subImporte;

    private double descuento;

    private double subTotalImporte;

    private double totalImporte;

    private String simboloMoneda;

    private FxVentaRealizadasController ventaRealizadasController;

    private ObservableList<ArticuloTB> arrList = null;

    private ArrayList<ImpuestoTB> arrayArticulos;

    private BillPrintable billPrintable;

    private VBox hbEncabezado;

    private VBox hbDetalleCabecera;

    private VBox hbPie;

    private int sheetWidth;

    private double pointWidth;

    private String nombreTicketImpresion;

    private double totalVenta;

    private double efectivo, vuelto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        arrayArticulos = new ArrayList<>();
        ImpuestoADO.GetTipoImpuestoCombBox().forEach(e -> {
            arrayArticulos.add(new ImpuestoTB(e.getIdImpuesto(), e.getNombre(), e.getValor(), e.getPredeterminado()));
        });
        simboloMoneda = "M";
        billPrintable = new BillPrintable();
        hbEncabezado = new VBox();
        hbDetalleCabecera = new VBox();
        hbPie = new VBox();
        pointWidth = 7.825;
        sheetWidth = 40;
    }

    private void fillVentasDetalleTable(String value) {
        arrList = VentaADO.ListVentasDetalle(value);
        for (int i = 0; i < arrList.size(); i++) {
            gpList.add(addElementGridPane("l1" + (i + 1), arrList.get(i).getId().get() + "", Pos.CENTER), 0, (i + 1));
            gpList.add(addElementGridPane("l2" + (i + 1), arrList.get(i).getClave() + "\n" + arrList.get(i).getNombreMarca(), Pos.CENTER_LEFT), 1, (i + 1));
            gpList.add(addElementGridPane("l3" + (i + 1), Tools.roundingValue(arrList.get(i).getCantidad(), 2), Pos.CENTER_RIGHT), 2, (i + 1));
            gpList.add(addElementGridPane("l4" + (i + 1), arrList.get(i).getUnidadCompraName(), Pos.CENTER_LEFT), 3, (i + 1));
            gpList.add(addElementGridPane("l5" + (i + 1), Tools.roundingValue(arrList.get(i).getDescuento(), 2) + "%", Pos.CENTER_RIGHT), 4, (i + 1));
            gpList.add(addElementGridPane("l6" + (i + 1), Tools.roundingValue(arrList.get(i).getImpuestoValor(), 2) + "%", Pos.CENTER_RIGHT), 5, (i + 1));
            gpList.add(addElementGridPane("l7" + (i + 1), simboloMoneda + "" + Tools.roundingValue(arrList.get(i).getPrecioVentaGeneral(), 2), Pos.CENTER_RIGHT), 6, (i + 1));
            gpList.add(addElementGridPane("l8" + (i + 1), simboloMoneda + "" + Tools.roundingValue(arrList.get(i).getTotalImporte(), 2), Pos.CENTER_RIGHT), 7, (i + 1));
        }
        calcularTotales();

    }

    private void InitializationTransparentBackground() {
        Session.PANE.setStyle("-fx-background-color: black");
        Session.PANE.setTranslateX(0);
        Session.PANE.setTranslateY(0);
        Session.PANE.setPrefWidth(Session.WIDTH_WINDOW);
        Session.PANE.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.PANE.setOpacity(0.7f);
        windowinit.getChildren().add(Session.PANE);
    }

    public void setInitComponents(String idVenta) {
        this.idVenta = idVenta;
        VentaTB ventaTB = VentaADO.GetVenta(idVenta);
        if (ventaTB != null) {
            lblFechaVenta.setText(ventaTB.getFechaVenta().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
            lblCliente.setText(ventaTB.getCliente());
            lblComprobante.setText(ventaTB.getComprobanteName());
            nombreTicketImpresion = ventaTB.getComproabanteNameImpresion();
            lblSerie.setText(ventaTB.getSerie() + "-" + ventaTB.getNumeracion());
            lblObservaciones.setText(ventaTB.getObservaciones());
            lblTipo.setText(ventaTB.getTipoName());
            lblEstado.setText(ventaTB.getEstadoName());
            lblTotalVenta.setText(ventaTB.getMonedaName() + " " + Tools.roundingValue(ventaTB.getTotal(), 2));
            simboloMoneda = ventaTB.getMonedaName();
            efectivo = ventaTB.getEfectivo();
            vuelto = ventaTB.getVuelto();
            totalVenta = ventaTB.getTotal();
        }
        EmpleadoTB empleadoTB = VentaADO.GetEmpleadoVenta(idVenta);
        if (empleadoTB != null) {
            lblVendedor.setText(empleadoTB.getApellidos() + " " + empleadoTB.getNombres());
        }
        fillVentasDetalleTable(idVenta);

    }

    private void calcelVenta() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_VENTADEVOLUCION);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxVentaDevolucionController controller = fXMLLoader.getController();
        controller.setInitVentaDetalle(this);
        controller.setLoadVentaDevolucion(idVenta, arrList, lblComprobante.getText(), lblTotalVenta.getText(), totalVenta);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Cancelar la venta", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            windowinit.getChildren().remove(Session.PANE);
        });
        stage.show();
    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            calcelVenta();
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) throws IOException {
        calcelVenta();
    }

    public void imprimirVenta(String ticket) {
        if (Session.ESTADO_IMPRESORA && Session.NOMBRE_IMPRESORA != null && Session.CORTAPAPEL_IMPRESORA != null) {
            loadTicket();
            ArrayList<HBox> object = new ArrayList<>();
            int rows = 0;
            int lines = 0;
            for (int i = 0; i < hbEncabezado.getChildren().size(); i++) {
                object.add((HBox) hbEncabezado.getChildren().get(i));
                HBox box = ((HBox) hbEncabezado.getChildren().get(i));
                rows++;
                for (int j = 0; j < box.getChildren().size(); j++) {
                    TextFieldTicket fieldTicket = ((TextFieldTicket) box.getChildren().get(j));
                    if (fieldTicket.getVariable().equalsIgnoreCase("repeempresa")) {
                        fieldTicket.setText(Session.REPRESENTANTE_EMPRESA);
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("telempresa")) {
                        fieldTicket.setText(Session.TELEFONO_EMPRESA);
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("celempresa")) {
                        fieldTicket.setText(Session.CELULAR_EMPRESA);
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("pagwempresa")) {
                        fieldTicket.setText(Session.PAGINAWEB_EMPRESA);
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("emailempresa")) {
                        fieldTicket.setText(Session.EMAIL_EMPRESA);
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("direcempresa")) {
                        fieldTicket.setText(Session.DIRECCION_EMPRESA);
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("rucempresa")) {
                        fieldTicket.setText(Session.RUC_EMPRESA);
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("razoempresa")) {
                        fieldTicket.setText(Session.RAZONSOCIAL_EMPRESA);
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("nomcomempresa")) {
                        fieldTicket.setText(Session.NOMBRECOMERCIAL_EMPRESA);
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("fchactual")) {
                        fieldTicket.setText(Tools.getDate("dd/MM/yyyy"));
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("horactual")) {
                        fieldTicket.setText(Tools.getHour("hh:mm:ss aa"));
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("docventa")) {
                        fieldTicket.setText(nombreTicketImpresion);
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("numventa")) {
                        fieldTicket.setText(ticket);
                    }
                    lines += fieldTicket.getLines();
                }
            }

            for (int m = 0; m < arrList.size(); m++) {
                for (int i = 0; i < hbDetalleCabecera.getChildren().size(); i++) {
                    HBox hBox = new HBox();
                    hBox.setId("dc_" + m + "" + i);
                    HBox box = ((HBox) hbDetalleCabecera.getChildren().get(i));
                    rows++;
                    for (int j = 0; j < box.getChildren().size(); j++) {
                        TextFieldTicket fieldTicket = ((TextFieldTicket) box.getChildren().get(j));
                        if (fieldTicket.getVariable().equalsIgnoreCase("codbarrasarticulo")) {
                            fieldTicket.setText(arrList.get(m).getClave());
                        } else if (fieldTicket.getVariable().equalsIgnoreCase("nombretarticulo")) {
                            fieldTicket.setText(arrList.get(m).getNombreMarca());
                        } else if (fieldTicket.getVariable().equalsIgnoreCase("cantarticulo")) {
                            fieldTicket.setText(Tools.roundingValue(arrList.get(m).getCantidad(), 2));
                        } else if (fieldTicket.getVariable().equalsIgnoreCase("precarticulo")) {
                            fieldTicket.setText(Tools.roundingValue(arrList.get(m).getPrecioVentaGeneral(), 2));
                        } else if (fieldTicket.getVariable().equalsIgnoreCase("descarticulo")) {
                            fieldTicket.setText(Tools.roundingValue(arrList.get(m).getDescuento(), 0) + "%");
                        } else if (fieldTicket.getVariable().equalsIgnoreCase("impoarticulo")) {
                            fieldTicket.setText(Tools.roundingValue(arrList.get(m).getTotalImporte(), 2));
                        }
                        hBox.getChildren().add(addElementTextField("iu", fieldTicket.getText(),
                                fieldTicket.isMultilineas(), fieldTicket.getLines(), fieldTicket.getColumnWidth(), fieldTicket.getAlignment(), fieldTicket.isEditable(), fieldTicket.getVariable()));
                        lines += fieldTicket.getLines();
                    }
                    object.add(hBox);
                }
            }

            for (int i = 0; i < hbPie.getChildren().size(); i++) {
                object.add((HBox) hbPie.getChildren().get(i));
                HBox box = ((HBox) hbPie.getChildren().get(i));
                rows++;
                for (int j = 0; j < box.getChildren().size(); j++) {
                    TextFieldTicket fieldTicket = ((TextFieldTicket) box.getChildren().get(j));
                    if (fieldTicket.getVariable().equalsIgnoreCase("subtotal")) {
                        fieldTicket.setText(lblValorVenta.getText());
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("dscttotal")) {
                        fieldTicket.setText(lblDescuento.getText());
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("imptotal")) {
                        fieldTicket.setText(lblSubTotal.getText());
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("totalpagar")) {
                        fieldTicket.setText(lblTotal.getText());
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("efectivo")) {
                        fieldTicket.setText(simboloMoneda + " " + Tools.roundingValue(efectivo, 2));
                    } else if (fieldTicket.getVariable().equalsIgnoreCase("vuelto")) {
                        fieldTicket.setText(simboloMoneda + " " + Tools.roundingValue(vuelto, 2));
                    }
                    lines += fieldTicket.getLines();
                }
            }
            billPrintable.modelTicket(window.getScene().getWindow(), sheetWidth, rows + lines + 1 + 5, lines, object, "Ticket", "Error el imprimir el ticket.");

        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle de venta", "No esta configurado la impresora :D", false);
        }
    }

    private void loadTicket() {
//        File file = new File("./archivos/ticketventa.json");
        JSONObject jSONObject = Tools.obtenerObjetoJSON(Session.RUTA_TICKET_VENTA);
        hbEncabezado.getChildren().clear();
        hbDetalleCabecera.getChildren().clear();
        hbPie.getChildren().clear();
        if (jSONObject.get("cabecera") != null) {
            JSONObject cabeceraObjects = Tools.obtenerObjetoJSON(jSONObject.get("cabecera").toString());
            for (int i = 0; i < cabeceraObjects.size(); i++) {
                HBox box = generateElement(hbEncabezado, "cb");
                JSONObject objectObtener = Tools.obtenerObjetoJSON(cabeceraObjects.get("cb_" + (i + 1)).toString());
                if (objectObtener.get("text") != null) {
                    JSONObject object = Tools.obtenerObjetoJSON(objectObtener.get("text").toString());
                    TextFieldTicket field = addElementTextField("iu", object.get("value").toString(), Boolean.valueOf(object.get("multiline").toString()), Integer.parseInt(object.get("lines").toString()), Integer.parseInt(object.get("width").toString()), getAlignment(object.get("align").toString()), Boolean.parseBoolean(object.get("editable").toString()), String.valueOf(object.get("variable").toString()));
                    box.getChildren().add(field);
                } else if (objectObtener.get("list") != null) {
                    JSONArray array = Tools.obtenerArrayJSON(objectObtener.get("list").toString());
                    Iterator it = array.iterator();
                    while (it.hasNext()) {
                        JSONObject object = Tools.obtenerObjetoJSON(it.next().toString());
                        TextFieldTicket field = addElementTextField("iu", object.get("value").toString(), Boolean.valueOf(object.get("multiline").toString()), Integer.parseInt(object.get("lines").toString()), Integer.parseInt(object.get("width").toString()), getAlignment(object.get("align").toString()), Boolean.parseBoolean(object.get("editable").toString()), String.valueOf(object.get("variable").toString()));
                        box.getChildren().add(field);
                    }
                }
            }
        }
        if (jSONObject.get("detalle") != null) {
            JSONObject detalleObjects = Tools.obtenerObjetoJSON(jSONObject.get("detalle").toString());
            for (int i = 0; i < detalleObjects.size(); i++) {
                HBox box = generateElement(hbDetalleCabecera, "dr");
                JSONObject objectObtener = Tools.obtenerObjetoJSON(detalleObjects.get("dr_" + (i + 1)).toString());
                if (objectObtener.get("text") != null) {
                    JSONObject object = Tools.obtenerObjetoJSON(objectObtener.get("text").toString());
                    TextFieldTicket field = addElementTextField("iu", object.get("value").toString(), Boolean.valueOf(object.get("multiline").toString()), Integer.parseInt(object.get("lines").toString()), Integer.parseInt(object.get("width").toString()), getAlignment(object.get("align").toString()), Boolean.parseBoolean(object.get("editable").toString()), String.valueOf(object.get("variable").toString()));
                    box.getChildren().add(field);
                } else if (objectObtener.get("list") != null) {
                    JSONArray array = Tools.obtenerArrayJSON(objectObtener.get("list").toString());
                    Iterator it = array.iterator();
                    while (it.hasNext()) {
                        JSONObject object = Tools.obtenerObjetoJSON(it.next().toString());
                        TextFieldTicket field = addElementTextField("iu", object.get("value").toString(), Boolean.valueOf(object.get("multiline").toString()), Integer.parseInt(object.get("lines").toString()), Integer.parseInt(object.get("width").toString()), getAlignment(object.get("align").toString()), Boolean.parseBoolean(object.get("editable").toString()), String.valueOf(object.get("variable").toString()));
                        box.getChildren().add(field);
                    }
                }
            }
        }

        if (jSONObject.get("pie") != null) {
            JSONObject pieObjects = Tools.obtenerObjetoJSON(jSONObject.get("pie").toString());
            for (int i = 0; i < pieObjects.size(); i++) {
                HBox box = generateElement(hbPie, "cp");
                JSONObject objectObtener = Tools.obtenerObjetoJSON(pieObjects.get("cp_" + (i + 1)).toString());
                if (objectObtener.get("text") != null) {
                    JSONObject object = Tools.obtenerObjetoJSON(objectObtener.get("text").toString());
                    TextFieldTicket field = addElementTextField("iu", object.get("value").toString(), Boolean.valueOf(object.get("multiline").toString()), Integer.parseInt(object.get("lines").toString()), Integer.parseInt(object.get("width").toString()), getAlignment(object.get("align").toString()), Boolean.parseBoolean(object.get("editable").toString()), String.valueOf(object.get("variable").toString()));
                    box.getChildren().add(field);
                } else if (objectObtener.get("list") != null) {
                    JSONArray array = Tools.obtenerArrayJSON(objectObtener.get("list").toString());
                    Iterator it = array.iterator();
                    while (it.hasNext()) {
                        JSONObject object = Tools.obtenerObjetoJSON(it.next().toString());
                        TextFieldTicket field = addElementTextField("iu", object.get("value").toString(), Boolean.valueOf(object.get("multiline").toString()), Integer.parseInt(object.get("lines").toString()), Integer.parseInt(object.get("width").toString()), getAlignment(object.get("align").toString()), Boolean.parseBoolean(object.get("editable").toString()), String.valueOf(object.get("variable").toString()));
                        box.getChildren().add(field);
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

    private HBox addElement(VBox contenedor, String id) {
        HBox hBox = new HBox();
        hBox.setId(id);
        hBox.setPrefHeight(30);
        contenedor.getChildren().add(hBox);
        return hBox;
    }

    public TextFieldTicket addElementTextField(String id, String titulo, boolean multilinea, int lines, int widthColumn, Pos align, boolean editable, String variable) {
        TextFieldTicket field = new TextFieldTicket(titulo, id);
        field.setMultilineas(multilinea);
        field.setLines(lines);
        field.setColumnWidth(widthColumn);
        field.setVariable(variable);
        field.setEditable(editable);
        field.setPreferredSize((double) widthColumn * pointWidth, 30);
        field.setAlignment(align);
        return field;
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

    private void openWindowAbonos() {
        try {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_VENTAABONO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());

            FxVentaAbonoController controller = fXMLLoader.getController();
            controller.setInitVentaAbonoController(this);

            Stage stage = FxWindow.StageLoaderModal(parent, "Historial de abonos", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                windowinit.getChildren().remove(Session.PANE);
            });
            stage.show();
            controller.loadInitData(idVenta, simboloMoneda);

        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    private void calcularTotales() {
        if (arrList != null) {
            arrList.forEach(e -> subImporte += e.getSubImporte());
            lblValorVenta.setText(simboloMoneda + " " + Tools.roundingValue(subImporte, 2));
            subImporte = 0;

            arrList.forEach(e -> descuento += e.getDescuentoSumado());
            lblDescuento.setText(simboloMoneda + " -" + Tools.roundingValue(descuento, 2));
            descuento = 0;

            arrList.forEach(e -> subTotalImporte += e.getSubImporteDescuento());
            lblSubTotal.setText(simboloMoneda + " " + Tools.roundingValue(subTotalImporte, 2));
            subTotalImporte = 0;

            hbAgregarImpuesto.getChildren().clear();
            boolean addElement = false;
            double sumaElement = 0;
            double totalImpuestos = 0;
            for (int k = 0; k < arrayArticulos.size(); k++) {
                for (int i = 0; i < arrList.size(); i++) {
                    if (arrayArticulos.get(k).getIdImpuesto() == arrList.get(i).getImpuestoArticulo()) {
                        addElement = true;
                        sumaElement += arrList.get(i).getImpuestoSumado();
                    }
                }
                if (addElement) {
                    addElementImpuesto(arrayArticulos.get(k).getIdImpuesto() + "", arrayArticulos.get(k).getNombre(), simboloMoneda + " " + Tools.roundingValue(sumaElement, 2));
                    totalImpuestos += sumaElement;
                    addElement = false;
                    sumaElement = 0;
                }
            }

            arrList.forEach(e -> totalImporte += e.getTotalImporte());
            lblTotal.setText(simboloMoneda + " " + Tools.roundingValue(totalImporte + totalImpuestos, 2));
            totalImporte = 0;
        }

    }

    private Label addElementGridPane(String id, String nombre, Pos pos) {
        Label label = new Label(nombre);
        label.setId(id);
        label.setStyle("-fx-text-fill:#020203;-fx-background-color: #dddddd;-fx-padding: 0.4166666666666667em 0.8333333333333334em 0.4166666666666667em 0.8333333333333334em;");
        label.getStyleClass().add("labelRoboto14");
        label.setAlignment(pos);
        label.setWrapText(true);
        label.setPrefWidth(Control.USE_COMPUTED_SIZE);
        label.setPrefHeight(Control.USE_COMPUTED_SIZE);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        return label;
    }

    private void addElementImpuesto(String id, String titulo, String total) {
        Text text = new Text(titulo);
        text.setStyle("-fx-fill:#020203;");
        text.getStyleClass().add("labelOpenSansRegular14");

        Text text1 = new Text(total);
        text1.setStyle("-fx-fill:#1976d2;");
        text1.getStyleClass().add("labelOpenSansRegular14");

        HBox hBox = new HBox(text, text1);
        hBox.setStyle("-fx-padding: 0.5em 0  0.5em 0;-fx-spacing:1em");
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setId(id);

        hbAgregarImpuesto.getChildren().add(hBox);
    }

    @FXML
    private void onKeyPressedImprimir(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            imprimirVenta(lblSerie.getText());
        }
    }

    @FXML
    private void onActionImprimir(ActionEvent event) {
        imprimirVenta(lblSerie.getText());
    }

    @FXML
    private void onMouseClickedBehind(MouseEvent event) throws IOException {
        vbContent.getChildren().remove(window);
        vbContent.getChildren().clear();
        AnchorPane.setLeftAnchor(ventaRealizadasController.getWindow(), 0d);
        AnchorPane.setTopAnchor(ventaRealizadasController.getWindow(), 0d);
        AnchorPane.setRightAnchor(ventaRealizadasController.getWindow(), 0d);
        AnchorPane.setBottomAnchor(ventaRealizadasController.getWindow(), 0d);
        vbContent.getChildren().add(ventaRealizadasController.getWindow());

    }

    @FXML
    private void onKeyPressedHistorialPagos(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowAbonos();
        }
    }

    @FXML
    private void onActionHistorialPagos(ActionEvent event) {
        openWindowAbonos();
    }

    public void setInitVentasController(FxVentaRealizadasController ventaRealizadasController, AnchorPane windowinit, AnchorPane vbContent) {
        this.ventaRealizadasController = ventaRealizadasController;
        this.windowinit = windowinit;
        this.vbContent = vbContent;
    }

}
