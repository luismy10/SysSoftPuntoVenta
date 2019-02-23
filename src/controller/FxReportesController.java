package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class FxReportesController implements Initializable {

    @FXML
    private TextField txtSearch;

    private AnchorPane windowinit;

    private AnchorPane content;

    //ARTICULO REPORTE
    private FXMLLoader fXMLArticuloReporte;

    private VBox nodeArticulo;

    private FxArticuloReportesController articuloReportesController;

    //VENTA REPORTE    
    private FXMLLoader fXMLVentaReporte;

    private VBox nodeVenta;

    private FxVentaReporteController ventaReporteController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            fXMLArticuloReporte = new FXMLLoader(getClass().getResource(Tools.FX_FILE_ARTICULOREPORTES));
            nodeArticulo = fXMLArticuloReporte.load();
            articuloReportesController = fXMLArticuloReporte.getController();

            fXMLVentaReporte = new FXMLLoader(getClass().getResource(Tools.FX_FILE_VENTAREPORTE));
            nodeVenta = fXMLVentaReporte.load();
            ventaReporteController = fXMLVentaReporte.getController();
        } catch (IOException ex) {
            Logger.getLogger(FxReportesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openWindowArticuloReportes() throws IOException {
        articuloReportesController.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(nodeArticulo, 0d);
        AnchorPane.setTopAnchor(nodeArticulo, 0d);
        AnchorPane.setRightAnchor(nodeArticulo, 0d);
        AnchorPane.setBottomAnchor(nodeArticulo, 0d);
        content.getChildren().add(nodeArticulo);
    }

    private void openWindowVentaReportes() throws IOException {
        ventaReporteController.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(nodeVenta, 0d);
        AnchorPane.setTopAnchor(nodeVenta, 0d);
        AnchorPane.setRightAnchor(nodeVenta, 0d);
        AnchorPane.setBottomAnchor(nodeVenta, 0d);
        content.getChildren().add(nodeVenta);
    }

    @FXML
    private void onKeyPressedArticulos(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowArticuloReportes();
        }
    }

    @FXML
    private void onActionArticulos(ActionEvent event) throws IOException {
        openWindowArticuloReportes();
    }

    @FXML
    private void onKeyPressedVentas(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowVentaReportes();
        }
    }

    @FXML
    private void onActionVentas(ActionEvent event) throws IOException {
        openWindowVentaReportes();
    }

    public void setContent(AnchorPane windowinit, AnchorPane content) {
        this.windowinit = windowinit;
        this.content = content;
    }

}
