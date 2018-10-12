package controller;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.CompraADO;
import model.CompraTB;
import model.DBUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FxComprasRealizadasController implements Initializable {

    @FXML
    private VBox window;

    private AnchorPane content;
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
    private TableColumn<CompraTB, String> tcProveedor;
    @FXML
    private TableColumn<CompraTB, String> tcTotal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcFechaCompra.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaCompra().get().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))));
        tcProveedor.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getProveedorTB().getNumeroDocumento().get() + "\n" + cellData.getValue().getProveedorTB().getRazonSocial().get()
        ));
        tcTotal.setCellValueFactory(cellData -> Bindings.concat("S/. "+cellData.getValue().getTotal().get()));
    }

    public void fillPurchasesTable(String value) {
        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<List<CompraTB>> task = new Task<List<CompraTB>>() {
            @Override
            public ObservableList<CompraTB> call() {
                return CompraADO.ListComprasRealizadas(value);
            }
        };
        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems((ObservableList<CompraTB>) task.getValue());
            lblLoad.setVisible(false);
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

    private void InitializationTransparentBackground() {
        SysSoft.pane.setStyle("-fx-background-color: black");
        SysSoft.pane.setTranslateX(0);
        SysSoft.pane.setTranslateY(0);
        SysSoft.pane.setPrefWidth(Session.WIDTH_WINDOW);
        SysSoft.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        SysSoft.pane.setOpacity(0.7f);
        content.getChildren().add(SysSoft.pane);
    }

    private void openWindowDetalleCompra() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_COMPRASDETALLE);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxCompraDetalleController controller = fXMLLoader.getController();
            controller.setInitComptrasController(this);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Detalle de compra", window.getScene().getWindow());
            stage.setResizable(false);
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(SysSoft.pane);
            });
            stage.show();
            controller.setLoadDetalle(tvList.getSelectionModel().getSelectedItem().getIdCompra());
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Debe seleccionar una compra de la lista", false);
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
        fillPurchasesTable("");
    }

    @FXML
    private void onActionSearch(ActionEvent event) {

    }

    void setContent(AnchorPane content) {
        this.content = content;
    }

    @FXML
    private void onActionReport(ActionEvent event) {
        try {
            DBUtil.dbConnect();
            InputStream inputStream = getClass().getResourceAsStream("/report/Compras.jasper");
  
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            Map map = new HashMap();

//            map.put("TOTAL", lblTotal.getText());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, DBUtil.getConnection());

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
        } finally {
            try {
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {
                Logger.getLogger(FxCompraDetalleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
