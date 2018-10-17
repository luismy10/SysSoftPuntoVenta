/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Ruberfc
 */
public class FxVentaDetalleController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblLoad;
    @FXML
    private Text lblFechaVenta;
    @FXML
    private Text lblDocumento;
    @FXML
    private Text lblNumeracion;
    @FXML
    private Text lblProveedor;
    @FXML
    private Text lblComprobante;
    @FXML
    private Text lblDomicilio;
    @FXML
    private Text lblRepresentante;
    @FXML
    private TableView<?> tvList;
    @FXML
    private TableColumn<?, ?> tcId;
    @FXML
    private TableColumn<?, ?> tcDescripcion;
    @FXML
    private TableColumn<?, ?> tcCantidad;
    @FXML
    private TableColumn<?, ?> tcPrecioCompra;
    @FXML
    private TableColumn<?, ?> tcImporte;
    @FXML
    private Text lblTotal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
