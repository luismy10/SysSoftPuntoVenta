/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * controller.FxVentasUtilidadesController
 */
public class FxVentasUtilidadesController implements Initializable {

    @FXML
    private Label lblLoad;
    @FXML
    private DatePicker dtFechaInicial;
    @FXML
    private DatePicker dtFechaFinal;
    @FXML
    private ComboBox<?> cbCategoria;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<?> tvList;
    @FXML
    private TableColumn<?, ?> tcId;
    @FXML
    private TableColumn<?, ?> tcDescripcion;
    @FXML
    private TableColumn<?, ?> tcCantidad;
    @FXML
    private TableColumn<?, ?> tcCosto;
    @FXML
    private TableColumn<?, ?> tcPrecio;
    @FXML
    private TableColumn<?, ?> tcImporte;
    
    private AnchorPane windowinit;
    @FXML
    private Text lblImporteC;
    @FXML
    private Text lblImporteCompra;
    @FXML
    private Text lblMonedaImporteV;
    @FXML
    private Text lblImporteVenta;
    @FXML
    private HBox hbAgregarImpuesto;
    @FXML
    private Text lblMonedaUtilidad;
    @FXML
    private Text lblUtilidad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void onKeyPressedRecargar(KeyEvent event) {
    }

    @FXML
    private void onActionRecargar(ActionEvent event) {
    }

    @FXML
    private void onKeyPressedReporte(KeyEvent event) {
    }

    @FXML
    private void onActionReporte(ActionEvent event) {
    }

    @FXML
    private void onActionFechaInicial(ActionEvent event) {
    }

    @FXML
    private void onActionFechaFinal(ActionEvent event) {
    }

    @FXML
    private void onActionCategoria(ActionEvent event) {
    }

    @FXML
    private void onKeyPressedSearch(KeyEvent event) {
    }

    @FXML
    private void onKeyRelasedSearch(KeyEvent event) {
    }

    @FXML
    private void onKeyPressedList(KeyEvent event) {
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
    }
    
    public void setContent(AnchorPane windowinit) {
        this.windowinit = windowinit;
    }
    
}
