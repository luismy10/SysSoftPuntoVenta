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
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
import model.RepresentanteTB;
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
    private Label lblRepresentante;
    @FXML
    private Label lblComprobante;
    @FXML
    private Label lblNumeracion;
    @FXML
    private Text lblTotal;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, Integer> tcId;
    @FXML
    private TableColumn<ArticuloTB, String> tcDescripcion;
    @FXML
    private TableColumn<ArticuloTB, String> tcCantidad;
    @FXML
    private TableColumn<ArticuloTB, String> tcMedidad;
    @FXML
    private TableColumn<ArticuloTB, String> tcPrecioCompra;
    @FXML
    private TableColumn<ArticuloTB, String> tcDescuento;
    @FXML
    private TableColumn<ArticuloTB, String> tcImpuesto;
    @FXML
    private TableColumn<ArticuloTB, String> tcImporte;
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

    private String idCompra;

    private String simboloMoneda;

    private ArrayList<ImpuestoTB> arrayArticulos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        tcId.setCellValueFactory(callData -> callData.getValue().getId().asObject());

        tcDescripcion.setCellValueFactory(callData -> Bindings.concat(
                callData.getValue().getClave() + "\n" + callData.getValue().getNombreMarca()
        ));

        tcMedidad.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getUnidadVenta() == 1 ? "Por Unidad/Pza" : "A Granel"
        ));

        tcCantidad.setCellValueFactory(callData -> Bindings.concat(Tools.roundingValue(callData.getValue().getCantidad(), 2)));

        tcPrecioCompra.setCellValueFactory(cellData -> Bindings.concat(
                simboloMoneda + "" + Tools.roundingValue(cellData.getValue().getPrecioCompra(), 2)));

        tcDescuento.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getDescuento(), 2) + "%"));

        tcImpuesto.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getImpuestoValor(), 2) + "%"));

        tcImporte.setCellValueFactory(cellData -> Bindings.concat(
                simboloMoneda + "" + Tools.roundingValue(cellData.getValue().getTotalImporte(), 2)));

        arrayArticulos = new ArrayList<>();
        ImpuestoADO.GetTipoImpuestoCombBox().forEach(e -> {
            arrayArticulos.add(new ImpuestoTB(e.getIdImpuesto(), e.getNombre(), e.getValor(), e.getPredeterminado()));
        });

        simboloMoneda = "M";
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
            tvList.setItems((ObservableList<ArticuloTB>) task.getValue());
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

    public void setLoadDetalle(String idCompra) {
        this.idCompra = idCompra;
        ArrayList<Object> objects = CompraADO.ListCompletaDetalleCompra(idCompra);
        CompraTB compraTB = (CompraTB) objects.get(0);
        ProveedorTB proveedorTB = (ProveedorTB) objects.get(1);
        RepresentanteTB representanteTB = (RepresentanteTB) objects.get(2);

        if (compraTB != null) {
            lblFechaCompra.setText(compraTB.getFechaCompra().get().format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy")));
            lblComprobante.setText(compraTB.getComprobanteName());
            lblNumeracion.setText(compraTB.getNumeracion());
            lblObservacion.setText(compraTB.getObservaciones());
            lblNotas.setText(compraTB.getNotas());
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
        }

        if (representanteTB != null) {
            lblRepresentante.setText(
                    representanteTB.getApellidos() + " " + representanteTB.getNombres() + " - "
                    + representanteTB.getTelefono() + " " + representanteTB.getCelular()
            );
        } else {
            lblRepresentante.setText("No tiene un representante registrado");
        }

        fillArticlesTable(idCompra);

    }

    private void calcularTotales() {

        tvList.getItems().forEach(e -> subImporte += e.getSubImporte());
        lblSubTotal.setText(simboloMoneda + " " + Tools.roundingValue(subImporte, 2));
        subImporte = 0;

        tvList.getItems().forEach(e -> descuento += e.getDescuentoSumado());
        lblDescuento.setText(simboloMoneda + " " + Tools.roundingValue(descuento, 2));
        descuento = 0;

        tvList.getItems().forEach(e -> subTotalImporte += e.getSubImporteDescuento());
        lblSubTotalNuevo.setText(simboloMoneda + " " + Tools.roundingValue(subTotalImporte, 2));
        subTotalImporte = 0;

        hbAgregarImpuesto.getChildren().clear();
        boolean addElement = false;
        double sumaElement = 0;
        for (int k = 0; k < arrayArticulos.size(); k++) {
            for (int i = 0; i < tvList.getItems().size(); i++) {
                if (arrayArticulos.get(k).getIdImpuesto() == tvList.getItems().get(i).getImpuestoArticulo()) {
                    addElement = true;
                    sumaElement += tvList.getItems().get(i).getImpuestoSumado();
                }
            }
            if (addElement) {
                addElementImpuesto(arrayArticulos.get(k).getIdImpuesto() + "", arrayArticulos.get(k).getNombre(), simboloMoneda + " " + Tools.roundingValue(sumaElement, 2));
                addElement = false;
                sumaElement = 0;
            }
        }

        tvList.getItems().forEach(e -> totalImporte += e.getTotalImporte());
        lblTotal.setText(simboloMoneda + " " + Tools.roundingValue(totalImporte, 2));
        totalImporte = 0;

    }

    private void addElementImpuesto(String id, String titulo, String total) {
        Text text = new Text(titulo);
        text.setStyle("-fx-fill:#020203;");
        text.getStyleClass().add("labelRobotoMedium16");

        Text text1 = new Text(total);
        text1.setStyle("-fx-fill:#004865;");
        text1.getStyleClass().add("labelRobotoMedium16");

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
            map.put("REPRESENTANTE", lblRepresentante.getText());

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

        } catch (HeadlessException | JRException ex) {
            System.out.println("Error al generar el reporte : " + ex);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
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
        controller.fillPurchasesTable("");
    }

    public void setInitComptrasController(FxComprasRealizadasController comprascontroller, AnchorPane windowinit, AnchorPane vbContent) {
        this.comprascontroller = comprascontroller;
        this.windowinit = windowinit;
        this.vbContent = vbContent;
    }

}
