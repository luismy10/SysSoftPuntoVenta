package controller;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
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
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.ArticuloTB;
import model.CompraADO;
import model.CompraTB;
import model.DBUtil;
import model.ImpuestoADO;
import model.ImpuestoTB;
import model.ProveedorTB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FxCompraDetalleController implements Initializable {

    @FXML
    private ScrollPane window;
    @FXML
    private Text lblFechaCompra;
    @FXML
    private Label lblDocumento;
    @FXML
    private Label lblProveedor;
    @FXML
    private Label lblDomicilio;
    @FXML
    private Label lblContacto;
    @FXML
    private Label lblComprobante;
    @FXML
    private Label lblNumeracion;
    @FXML
    private Text lblTotal;
    @FXML
    private GridPane gpList;
    @FXML
    private Label lblLoad;
    @FXML
    private Text lblSubTotal;
    @FXML
    private Text lblDescuento;
    @FXML
    private Text lblSubTotalNuevo;
    @FXML
    private VBox hbAgregarImpuesto;
    @FXML
    private Label lblObservacion;
    @FXML
    private Label lblNotas;
    @FXML
    private Label lblTotalCompra;

    private double subImporte;

    private double descuento;

    private double subTotalImporte;

    private double totalImporte;

    private FxComprasRealizadasController comprascontroller;

    private AnchorPane windowinit;

    private AnchorPane vbContent;
    
    private String idProveedor;

    private String idCompra;

    private String estadoCompra;
    
    private double total;

    private String simboloMoneda;

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

    private void fillArticlesTable(String value) {
        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<List<ArticuloTB>> task = new Task<List<ArticuloTB>>() {
            @Override
            public ObservableList<ArticuloTB> call() {
                return CompraADO.ListDetalleCompra(value);
            }
        };
        task.setOnSucceeded((WorkerStateEvent e) -> {
            arrList = (ObservableList<ArticuloTB>) task.getValue();
            for (int i = 0; i < arrList.size(); i++) {
                gpList.add(addElementGridPane("l1" + (i + 1), arrList.get(i).getId().get() + "", Pos.CENTER), 0, (i + 1));
                gpList.add(addElementGridPane("l2" + (i + 1), arrList.get(i).getClave() + "\n" + arrList.get(i).getNombreMarca(), Pos.CENTER_LEFT), 1, (i + 1));
                gpList.add(addElementGridPane("l3" + (i + 1), Tools.roundingValue(arrList.get(i).getCantidad(), 2), Pos.CENTER_RIGHT), 2, (i + 1));
                gpList.add(addElementGridPane("l4" + (i + 1), arrList.get(i).getUnidadCompraName(), Pos.CENTER_LEFT), 3, (i + 1));
                gpList.add(addElementGridPane("l5" + (i + 1), Tools.roundingValue(arrList.get(i).getDescuento(), 2) + "%", Pos.CENTER_RIGHT), 4, (i + 1));
                gpList.add(addElementGridPane("l6" + (i + 1), Tools.roundingValue(arrList.get(i).getImpuestoValor(), 2) + "%", Pos.CENTER_RIGHT), 5, (i + 1));
                gpList.add(addElementGridPane("l7" + (i + 1), simboloMoneda + "" + Tools.roundingValue(arrList.get(i).getPrecioCompra(), 2), Pos.CENTER_RIGHT), 6, (i + 1));
                gpList.add(addElementGridPane("l8" + (i + 1), simboloMoneda + "" + Tools.roundingValue(arrList.get(i).getTotalImporte(), 2), Pos.CENTER_RIGHT), 7, (i + 1));
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

    public void setLoadDetalle(String idCompra, String estadoCompra, double total) {
        this.idCompra = idCompra;
        this.estadoCompra = estadoCompra;
        this.total = total;
        ArrayList<Object> objects = CompraADO.ListCompletaDetalleCompra(idCompra);
        CompraTB compraTB = (CompraTB) objects.get(0);
        ProveedorTB proveedorTB = (ProveedorTB) objects.get(1);

        if (compraTB != null) {
            lblFechaCompra.setText(compraTB.getFechaCompra().get().format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy")));
            lblComprobante.setText(compraTB.getComprobanteName());
            lblNumeracion.setText(compraTB.getNumeracion());
            lblObservacion.setText(compraTB.getObservaciones().equalsIgnoreCase("") ? "No tiene ninguna observación" : compraTB.getObservaciones());
            lblNotas.setText(compraTB.getNotas().equalsIgnoreCase("") ? "No tiene ninguna nota" : compraTB.getNotas());
            lblTotalCompra.setText(compraTB.getTipoMonedaName() + " " + Tools.roundingValue(compraTB.getTotal().get(), 2));
            simboloMoneda = compraTB.getTipoMonedaName();
        }

        if (proveedorTB != null) {
            lblDocumento.setText(proveedorTB.getNumeroDocumento().get());
            lblProveedor.setText(proveedorTB.getRazonSocial().get());
            lblDomicilio.setText(proveedorTB.getDireccion().equalsIgnoreCase("")
                    ? "No tiene un domicilio registrado"
                    : proveedorTB.getDireccion());
            lblContacto.setText("Tel: " + proveedorTB.getTelefono() + " Cel: " + proveedorTB.getCelular());
            
            idProveedor = proveedorTB.getIdProveedor().get();
     
        }

        fillArticlesTable(idCompra);

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

    private void onEventReporte() {
        try {
            DBUtil.dbConnect();
            InputStream dir = getClass().getResourceAsStream("/report/DetalleCompra.jasper");
            InputStream imgInputStream
                    = getClass().getResourceAsStream("/view/logo.png");

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(dir);
            Map map = new HashMap();
            map.put("IDCOMPRA", idCompra);
            map.put("EMPRESA", Session.EMPRESA);
            map.put("LOGO", imgInputStream);
            map.put("EMAIL", "EMAIL" + Session.EMAIL);
            map.put("TELEFONOCELULAR", "TEL:" + Session.TELEFONO + " CEL:" + Session.CELULAR);
            map.put("DIRECCION", Session.DIRECCION);

            map.put("FECHACOMPRA", lblFechaCompra.getText());
            map.put("PROVEEDOR", lblProveedor.getText());
            map.put("PRODIRECCION", lblDomicilio.getText());
            map.put("PROTELEFONOCELULAR", lblContacto.getText());
            map.put("PROEMAIL", Session.EMAIL);
            map.put("TOTAL", lblTotal.getText());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, DBUtil.getConnection());

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setIconImage(new ImageIcon(getClass().getResource(Tools.FX_LOGO)).getImage());
            jasperViewer.setTitle("Detalle de compra");
            jasperViewer.setSize(840, 650);
            jasperViewer.setLocationRelativeTo(null);
            jasperViewer.setVisible(true);

        } catch (HeadlessException | JRException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println("Error al generar el reporte : " + ex);
        } finally {
            DBUtil.dbDisconnect();
        }
    }

    @FXML
    private void onKeyPressedImprimir(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onEventReporte();
        }
    }

    @FXML
    private void onActionImprimir(ActionEvent event) {
        onEventReporte();
    }

    @FXML
    private void onMouseClickedBehind(MouseEvent event) throws IOException {
        vbContent.getChildren().remove(window);
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_COMPRASREALIZADAS));
        VBox node = fXMLPrincipal.load();
        FxComprasRealizadasController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit, vbContent);
        vbContent.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        vbContent.getChildren().add(node);

        controller.fillPurchasesTable((short) 1, "", Tools.getDate(), Tools.getDate(), "");
    }

    private void openWindowHistorialPagos() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_HISTORIAL_PAGOS);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());

        FxHistorialPagosController controller = fXMLLoader.getController();
        controller.setInitHistorialPagosController(this, idProveedor , idCompra, total);

        Stage stage = FxWindow.StageLoaderModal(parent, "Historial de Pagos", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            windowinit.getChildren().remove(Session.pane);
        });
        stage.show();
        controller.initListHistorialPagos();

    }

    private void InitializationTransparentBackground() {
        Session.pane.setStyle("-fx-background-color: black");
        Session.pane.setTranslateX(0);
        Session.pane.setTranslateY(0);
        Session.pane.setPrefWidth(Session.WIDTH_WINDOW);
        Session.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.pane.setOpacity(0.7f);
        windowinit.getChildren().add(Session.pane);
    }

    @FXML
    private void onActionHistorialPagos(ActionEvent event) throws IOException {
        if (estadoCompra.equals("PENDIENTE".toUpperCase())) {
            openWindowHistorialPagos();
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle de Compra", "La compra no se hiso al credito", false);
        }
    }

    public void setInitComptrasController(FxComprasRealizadasController comprascontroller, AnchorPane windowinit, AnchorPane vbContent) {
        this.comprascontroller = comprascontroller;
        this.windowinit = windowinit;
        this.vbContent = vbContent;
    }
}
