package controller;

import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.LoteTB;
import model.TipoDocumentoADO;
import model.TipoDocumentoTB;
import model.VentaADO;
import model.VentaTB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class FxVentaReporteController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private DatePicker dpFechaInicial;
    @FXML
    private DatePicker dpFechaFinal;
    @FXML
    private ComboBox<TipoDocumentoTB> cbDocumentos;
    @FXML
    private CheckBox cbDocumentosSeleccionar;
    @FXML
    private TextField txtClientes;
    
    private AnchorPane content;
    
    private String idCliente;
    @FXML
    private Button btnClientes;
    @FXML
    private CheckBox cbClientesSeleccionar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.actualDate(Tools.getDate(), dpFechaInicial);
        Tools.actualDate(Tools.getDate(), dpFechaFinal);
        cbDocumentos.getItems().clear();
        TipoDocumentoADO.GetDocumentoCombBox().forEach(e -> {
            cbDocumentos.getItems().add(new TipoDocumentoTB(e.getIdTipoDocumento(), e.getNombre(), e.isPredeterminado()));
        });
        idCliente = "";
    }

    private void InitializationTransparentBackground() {
        Session.PANE.setStyle("-fx-background-color: black");
        Session.PANE.setTranslateX(0);
        Session.PANE.setTranslateY(0);
        Session.PANE.setPrefWidth(Session.WIDTH_WINDOW);
        Session.PANE.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.PANE.setOpacity(0.7f);
        content.getChildren().add(Session.PANE);
    }

    private void openWindowCliente() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_CLIENTELISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxClienteListaController controller = fXMLLoader.getController();
        controller.setInitVentaReporteController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Elija un cliente", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.PANE);
        });
        stage.show();
        controller.fillCustomersTable("");
    }

    private void openViewReporteGeneral() {
        try {
            if (!cbDocumentosSeleccionar.isSelected() && cbDocumentos.getSelectionModel().getSelectedIndex() < 0) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Reporte General de Ventas", "Seleccione un documento para generar el reporte.", false);
                cbDocumentos.requestFocus();
            } else {
                ArrayList<VentaTB> list = VentaADO.GetReporteGenetalVentas(Tools.getDatePicker(dpFechaInicial), Tools.getDatePicker(dpFechaFinal),
                        cbDocumentosSeleccionar.isSelected() ? 0
                        : cbDocumentos.getSelectionModel().getSelectedItem().getIdTipoDocumento());
                if (list.isEmpty()) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Reporte General de Ventas", "No hay registros para mostrar en el reporte.", false);
                    return;
                }
                Map map = new HashMap();
                map.put("PERIODO", "Periodo: " + dpFechaInicial.getValue().format(DateTimeFormatter.ofPattern("dd/MM/YYYY"))+ " - " + dpFechaFinal.getValue().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
                map.put("DOCUMENTO", "Todos");
                map.put("ORDEN", "Fecha");

                JasperPrint jasperPrint = JasperFillManager.fillReport(FxArticuloReportesController.class.getResourceAsStream("/report/VentaGeneral.jasper"), map, new JRBeanCollectionDataSource(list));

                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setIconImage(new ImageIcon(getClass().getResource(Tools.FX_LOGO)).getImage());
                jasperViewer.setTitle("Reporte General de Ventas");
                jasperViewer.setSize(840, 650);
                jasperViewer.setLocationRelativeTo(null);
                jasperViewer.setVisible(true);
            }

        } catch (HeadlessException | JRException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Reporte General de Ventas", "Error al generar el reporte : " + ex.getLocalizedMessage(), false);

        }
    }

    @FXML
    private void onActionCbDocumentosSeleccionar(ActionEvent event) {
        cbDocumentos.setDisable(cbDocumentosSeleccionar.isSelected());
    }
    
       @FXML
    private void onActionCbClientesSeleccionar(ActionEvent event) {
        btnClientes.setDisable(cbClientesSeleccionar.isSelected());
    }

    @FXML
    private void onKeyPressedReporteGeneral(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            openViewReporteGeneral();
        }
    }

    @FXML
    private void onActionReporteGeneral(ActionEvent event) {
        openViewReporteGeneral();
    }

    @FXML
    private void onKeyPressedClientes(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowCliente();
        }
    }

    @FXML
    private void onActionClientes(ActionEvent event) throws IOException {
            openWindowCliente();
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

    public void setClienteVentaReporte(String idCliente, String datos) {
        this.idCliente = idCliente;
        txtClientes.setText(datos);
    }

}
