package controller;

import java.awt.HeadlessException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
    
    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.actualDate(Tools.getDate(), dpFechaInicial);
        Tools.actualDate(Tools.getDate(), dpFechaFinal);
        cbDocumentos.getItems().clear();
        TipoDocumentoADO.GetDocumentoCombBox().forEach(e -> {
            cbDocumentos.getItems().add(new TipoDocumentoTB(e.getIdTipoDocumento(), e.getNombre(), e.isPredeterminado()));
        });
       
    }

    private void openViewReporte() {
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
                map.put("PERIODO", "Periodo: " +LocalDate.from(DateTimeFormatter.ofPattern("dd/MM/YYYY").parse(Tools.getDatePicker(dpFechaInicial))) + " - " +LocalDate.from(DateTimeFormatter.ofPattern("dd/MM/YYYY").parse(Tools.getDatePicker(dpFechaFinal))));
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
    private void onKeyPressedVisualizar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            openViewReporte();
        }
    }

    @FXML
    private void onActionVisualizar(ActionEvent event) {
        openViewReporte();
    }

    @FXML
    private void onActionCbDocumentosSeleccionar(ActionEvent event) {
        cbDocumentos.setDisable(cbDocumentosSeleccionar.isSelected());
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
