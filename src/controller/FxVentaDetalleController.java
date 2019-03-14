package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import model.ArticuloTB;
import model.DBUtil;
import model.EmpleadoTB;
import model.ImpuestoADO;
import model.ImpuestoTB;
import model.VentaADO;
import model.VentaTB;

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
    private Text lblSubTotal;
    @FXML
    private Text lblDescuento;
    @FXML
    private Text lblSubTotalNuevo;
    @FXML
    private VBox hbAgregarImpuesto;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        arrayArticulos = new ArrayList<>();
        ImpuestoADO.GetTipoImpuestoCombBox().forEach(e -> {
            arrayArticulos.add(new ImpuestoTB(e.getIdImpuesto(), e.getNombre(), e.getValor(), e.getPredeterminado()));
        });
        simboloMoneda = "M";
    }

    private void fillVentasDetalleTable(String value) {
        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<List<ArticuloTB>> task = new Task<List<ArticuloTB>>() {
            @Override
            public ObservableList<ArticuloTB> call() {
                return VentaADO.ListVentasDetalle(value);
            }
        };
        task.setOnSucceeded((WorkerStateEvent e) -> {
            arrList = VentaADO.ListVentasDetalle(value);
            for (int i = 0; i < arrList.size(); i++) {
                gpList.add(addElementGridPane("l1" + (i + 1), arrList.get(i).getId() + "", Pos.CENTER), 0, (i + 1));
                gpList.add(addElementGridPane("l2" + (i + 1), arrList.get(i).getClave() + "\n" + arrList.get(i).getNombreMarca(), Pos.CENTER_LEFT), 1, (i + 1));
                gpList.add(addElementGridPane("l3" + (i + 1), Tools.roundingValue(arrList.get(i).getCantidad(), 2), Pos.CENTER_RIGHT), 2, (i + 1));
                gpList.add(addElementGridPane("l4" + (i + 1), arrList.get(i).getUnidadCompraName(), Pos.CENTER_LEFT), 3, (i + 1));
                gpList.add(addElementGridPane("l5" + (i + 1), Tools.roundingValue(arrList.get(i).getPrecioVenta(), 2), Pos.CENTER_RIGHT), 4, (i + 1));
                gpList.add(addElementGridPane("l6" + (i + 1), Tools.roundingValue(arrList.get(i).getDescuento(), 2) + "%", Pos.CENTER_RIGHT), 5, (i + 1));
                gpList.add(addElementGridPane("l7" + (i + 1), simboloMoneda + "" + Tools.roundingValue(arrList.get(i).getTotalImporte(), 2), Pos.CENTER_RIGHT), 6, (i + 1));
            }
            lblLoad.setVisible(false);
            calcularTotales();
        });
        task.setOnFailed((WorkerStateEvent event) -> {
            lblLoad.setVisible(false);
        });

        task.setOnScheduled((WorkerStateEvent event) -> {
            lblLoad.setVisible(true);
        });
        exec.execute(task);
        if (!exec.isShutdown()) {
            exec.shutdown();
        }
    }

    public void setInitComponents(LocalDateTime fechaRegistro, String cliente, String comprobanteName, String serie, String numeracion, String observaciones, String idVenta) {
        lblFechaVenta.setText(fechaRegistro.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
        lblCliente.setText(cliente);
        lblComprobante.setText(comprobanteName);
        lblSerie.setText(serie + "-" + numeracion);
        lblObservaciones.setText(observaciones);
        this.idVenta = idVenta;

        VentaTB ventaTB = VentaADO.GetVenta(idVenta);
        if (ventaTB != null) {
            lblEstado.setText(ventaTB.getEstadoName());
            lblTotal.setText(ventaTB.getMonedaName() + " " + Tools.roundingValue(ventaTB.getTotal(), 2));
        }
        EmpleadoTB empleadoTB = VentaADO.GetEmpleadoVenta(idVenta);
        if (empleadoTB != null) {
            lblVendedor.setText(empleadoTB.getApellidos() + " " + empleadoTB.getNombres());
        }
        fillVentasDetalleTable(idVenta);

    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        short validate = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Detalle de ventas", "¿Está seguro de cancelar la venta?", true);
        if (validate == 1) {
            String result = VentaADO.CancelTheSale(idVenta, arrList);
            if (result.equalsIgnoreCase("update")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle de venta", "Se ha cancelado con éxito", false);
            } else if (result.equalsIgnoreCase("scrambled")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle de venta", "Ya está cancelada la venta!", false);
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle de venta", result, false);
            }
        }
    }

    public void imprimirVenta(String ticket) {
        if (Session.ESTADO_IMPRESORA && Session.NOMBRE_IMPRESORA != null && Session.CORTAPAPEL_IMPRESORA != null) {

        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Venta", "No esta configurado la impresora :D", false);

        }

    }

    private void calcularTotales() {
        if (arrList != null) {
            arrList.forEach(e -> subImporte += e.getSubImporte());
            lblSubTotal.setText(simboloMoneda + " " + Tools.roundingValue(subImporte, 2));
            subImporte = 0;

            arrList.forEach(e -> descuento += e.getDescuentoSumado());
            lblDescuento.setText(simboloMoneda + " " + Tools.roundingValue(descuento, 2));
            descuento = 0;

            arrList.forEach(e -> subTotalImporte += e.getSubImporteDescuento());
            lblSubTotalNuevo.setText(simboloMoneda + " " + Tools.roundingValue(subTotalImporte, 2));
            subTotalImporte = 0;

            hbAgregarImpuesto.getChildren().clear();
            boolean addElement = false;
            double sumaElement = 0;
            for (int k = 0; k < arrayArticulos.size(); k++) {
                for (int i = 0; i < arrList.size(); i++) {
                    if (arrayArticulos.get(k).getIdImpuesto() == arrList.get(i).getImpuestoArticulo()) {
                        addElement = true;
                        sumaElement += arrList.get(i).getImpuestoSumado();
                    }
                }
                if (addElement) {
                    addElementImpuesto(arrayArticulos.get(k).getIdImpuesto() + "", arrayArticulos.get(k).getNombre(), simboloMoneda + " " + Tools.roundingValue(sumaElement, 2));
                    addElement = false;
                    sumaElement = 0;
                }
            }

            arrList.forEach(e -> totalImporte += e.getTotalImporte());
            lblTotal.setText(simboloMoneda + " " + Tools.roundingValue(totalImporte, 2));
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
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_VENTAREALIZADAS));
        VBox node = fXMLPrincipal.load();
        FxVentaRealizadasController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit, vbContent);
        vbContent.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        vbContent.getChildren().add(node);

    }

    public void setInitVentasController(FxVentaRealizadasController ventaRealizadasController, AnchorPane windowinit, AnchorPane vbContent) {
        this.ventaRealizadasController = ventaRealizadasController;
        this.windowinit = windowinit;
        this.vbContent = vbContent;
    }

}
