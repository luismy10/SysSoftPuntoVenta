package controller;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.CompraADO;
import model.CompraTB;
import model.DetalleADO;
import model.DetalleTB;
import model.PlazosADO;
import model.PlazosTB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FxComprasRealizadasController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private DatePicker dtFechaInicial;
    @FXML
    private DatePicker dtFechaFinal;
    @FXML
    private Label lblLoad;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<CompraTB> tvList;
    @FXML
    private TableColumn<CompraTB, Integer> tcId;
    @FXML
    private TableColumn<CompraTB, String> tcFechaCompra;
    @FXML
    private TableColumn<CompraTB, String> tcNumeracion;
    @FXML
    private TableColumn<CompraTB, String> tcProveedor;
    @FXML
    private TableColumn<CompraTB, String> tcEstadoCompra;
    @FXML
    private TableColumn<CompraTB, String> tcTotal;
    @FXML
    private ComboBox<DetalleTB> cbEstadoCompra;

    private AnchorPane vbContent;

    private AnchorPane windowinit;

    private boolean validationSearch;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        tcFechaCompra.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaCompra().get().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))));
        tcNumeracion.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNumeracion()));
        tcProveedor.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getProveedorTB().getNumeroDocumento().get() + "\n" + cellData.getValue().getProveedorTB().getRazonSocial().get()
        ));
        tcEstadoCompra.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getEstadoCompra()+ ""));
        tcTotal.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getTipoMonedaName() + " " + Tools.roundingValue(cellData.getValue().getTotal().get(), 2)));

        Tools.actualDate(Tools.getDate(), dtFechaInicial);
        Tools.actualDate(Tools.getDate(), dtFechaFinal);
        validationSearch = false;
        
        cbEstadoCompra.getItems().add(new DetalleTB(new SimpleIntegerProperty(0), new SimpleStringProperty("TODOS")));
        DetalleADO.GetDetailId("0009").forEach(e -> {
            cbEstadoCompra.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        cbEstadoCompra.getSelectionModel().select(0);
        
    }

    public void fillPurchasesTable(short opcion, String value, String fechaInicial, String fechaFinal, String estadoCompra) {
        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<List<CompraTB>> task = new Task<List<CompraTB>>() {
            @Override
            public ObservableList<CompraTB> call() {
                return CompraADO.ListComprasRealizadas(opcion, value, fechaInicial, fechaFinal,estadoCompra);
            }
        };
        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems((ObservableList<CompraTB>) task.getValue());
            lblLoad.setVisible(false);
            validationSearch = false;
        });
        task.setOnFailed((WorkerStateEvent event) -> {
            lblLoad.setVisible(false);
            validationSearch = false;
        });

        task.setOnScheduled((WorkerStateEvent event) -> {
            lblLoad.setVisible(true);
            validationSearch = true;
        });
        exec.execute(task);
        if (!exec.isShutdown()) {
            exec.shutdown();
        }
    }

    private void openWindowDetalleCompra() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_COMPRASDETALLE));
            ScrollPane node = fXMLPrincipal.load();
            FxCompraDetalleController controller = fXMLPrincipal.getController();
            controller.setInitComptrasController(this, windowinit, vbContent);
            controller.setLoadDetalle(tvList.getSelectionModel().getSelectedItem().getIdCompra());
            vbContent.getChildren().clear();
            AnchorPane.setLeftAnchor(node, 0d);
            AnchorPane.setTopAnchor(node, 0d);
            AnchorPane.setRightAnchor(node, 0d);
            AnchorPane.setBottomAnchor(node, 0d);
            vbContent.getChildren().add(node);

        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Debe seleccionar una compra de la lista", false);
        }
    }

    private void openWindowReporte() {
        try {

            ArrayList<CompraTB> list = new ArrayList();
            for (int i = 0; i < tvList.getItems().size(); i++) {
                list.add(new CompraTB(
                        tvList.getItems().get(i).getId(),
                        tvList.getItems().get(i).getFechaCompra().get().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                        tvList.getItems().get(i).getNumeracion(),
                        tvList.getItems().get(i).getProveedorTB().getNumeroDocumento().get() + "\n" + tvList.getItems().get(i).getProveedorTB().getRazonSocial().get(),
                        tvList.getItems().get(i).getTipoMonedaName() + " " + Tools.roundingValue(tvList.getItems().get(i).getTotal().get(), 2)
                ));
            }

            InputStream inputStream = getClass().getResourceAsStream("/report/Compras.jasper");

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);

            Map map = new HashMap();
            map.put("PARAMETROFECHA", "Periodo: " + Tools.getDatePicker(dtFechaInicial) + " al " + Tools.getDatePicker(dtFechaFinal));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(list));

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setIconImage(new ImageIcon(getClass().getResource(Tools.FX_LOGO)).getImage());
            jasperViewer.setTitle("Reporte general de compras");
            jasperViewer.setSize(840, 650);
            jasperViewer.setLocationRelativeTo(null);
            jasperViewer.setVisible(true);

        } catch (HeadlessException | JRException ex) {
            System.out.println("Error al generar el reporte : " + ex);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(FxCompraDetalleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActionView(ActionEvent event) throws IOException {
        openWindowDetalleCompra();
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalleCompra();
        }

    }

    @FXML
    private void onActionReload(ActionEvent event) {
        fillPurchasesTable((short) 1, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal), "");
        this.txtSearch.setText("");
        cbEstadoCompra.getSelectionModel().select(0);
    }

    @FXML
    private void onKeyPressedReload(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            fillPurchasesTable((short) 1, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal), "");
            cbEstadoCompra.getSelectionModel().select(0);
            this.txtSearch.setText("");
        }
    }

    @FXML
    private void onActionReport(ActionEvent event) {
        openWindowReporte();
    }

    @FXML
    private void onKeyPressedReporte(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowReporte();
        }
    }

    @FXML
    private void onActionSearch(ActionEvent event) {
        if (!tvList.getItems().isEmpty()) {
            tvList.requestFocus();
            tvList.getSelectionModel().select(0);
        }
    }

    @FXML
    private void onKeyReleasedSeach(KeyEvent event) {
        if (event.getCode() != KeyCode.ESCAPE
                && event.getCode() != KeyCode.F1
                && event.getCode() != KeyCode.F2
                && event.getCode() != KeyCode.F3
                && event.getCode() != KeyCode.F4
                && event.getCode() != KeyCode.F5
                && event.getCode() != KeyCode.F6
                && event.getCode() != KeyCode.F7
                && event.getCode() != KeyCode.F8
                && event.getCode() != KeyCode.F9
                && event.getCode() != KeyCode.F10
                && event.getCode() != KeyCode.F11
                && event.getCode() != KeyCode.F12
                && event.getCode() != KeyCode.ALT
                && event.getCode() != KeyCode.CONTROL
                && event.getCode() != KeyCode.UP
                && event.getCode() != KeyCode.DOWN
                && event.getCode() != KeyCode.RIGHT
                && event.getCode() != KeyCode.LEFT
                && event.getCode() != KeyCode.TAB
                && event.getCode() != KeyCode.CAPS
                && event.getCode() != KeyCode.SHIFT
                && event.getCode() != KeyCode.HOME
                && event.getCode() != KeyCode.WINDOWS
                && event.getCode() != KeyCode.ALT_GRAPH
                && event.getCode() != KeyCode.CONTEXT_MENU
                && event.getCode() != KeyCode.END
                && event.getCode() != KeyCode.INSERT
                && event.getCode() != KeyCode.PAGE_UP
                && event.getCode() != KeyCode.PAGE_DOWN
                && event.getCode() != KeyCode.NUM_LOCK
                && event.getCode() != KeyCode.PRINTSCREEN
                && event.getCode() != KeyCode.SCROLL_LOCK
                && event.getCode() != KeyCode.PAUSE
                && event.getCode() != KeyCode.ENTER) {
            if (!validationSearch) {
                fillPurchasesTable((short) 0, txtSearch.getText().trim(), "", "","");
                cbEstadoCompra.getSelectionModel().select(0);
            }
        }
    }

    @FXML
    private void onActionFechaInicial(ActionEvent actionEvent) {
        if (dtFechaInicial.getValue() != null && dtFechaFinal.getValue() != null) {
            fillPurchasesTable((short) 1, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal), "");
            cbEstadoCompra.getSelectionModel().select(0);
            this.txtSearch.setText("");
        }
    }

    @FXML
    private void onActionFechaFinal(ActionEvent actionEvent) {
        if (dtFechaInicial.getValue() != null && dtFechaFinal.getValue() != null) {
            fillPurchasesTable((short) 1, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal),"");
            cbEstadoCompra.getSelectionModel().select(0);
            this.txtSearch.setText("");
        }
    }

    public void setContent(AnchorPane windowinit, AnchorPane vbContent) {
        this.windowinit = windowinit;
        this.vbContent = vbContent;
    }

    @FXML
    private void OnActionEstadoCompra(ActionEvent event) {
        if(cbEstadoCompra.getSelectionModel().getSelectedIndex()!= 0){
            fillPurchasesTable((short) 2, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal), cbEstadoCompra.getSelectionModel().getSelectedItem().getNombre().get());
            this.txtSearch.setText("");
        }
        else{
            fillPurchasesTable((short) 1, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal), "");
            cbEstadoCompra.getSelectionModel().select(0);
            this.txtSearch.setText("");
        }
    }
    
    

}
