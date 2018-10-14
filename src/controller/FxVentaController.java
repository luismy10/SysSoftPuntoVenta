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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Ruberfc
 */
public class FxVentaController implements Initializable {

    @FXML
    private Button btnArticulo;
    @FXML
    private TextField txtProveedor;
    @FXML
    private DatePicker tpFechaCompra;
    @FXML
    private ComboBox<?> cbComprobante;
    @FXML
    private TextField txtProveedor1;
    @FXML
    private TableView<?> tvList;
    @FXML
    private TableColumn<?, ?> tcArticulo;
    @FXML
    private TableColumn<?, ?> tcCantidad;
    @FXML
    private TableColumn<?, ?> tcPrecio;
    @FXML
    private TableColumn<?, ?> tcDescuento;
    @FXML
    private TableColumn<?, ?> tcImporte;
    @FXML
    private Text lblSubTotal;
    @FXML
    private Text lblDescuento;
    @FXML
    private Text lblGravada;
    @FXML
    private Text lblIgv;
    @FXML
    private Text lblTotal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onKeyPressedRegister(KeyEvent event) {
    }

    @FXML
    private void onActionRegister(ActionEvent event) {
    }

    @FXML
    private void onKeyPressedAdd(KeyEvent event) {
    }

    @FXML
    private void onActionAdd(ActionEvent event) {
    }

    @FXML
    private void onKeyPressedEdit(KeyEvent event) {
    }

    @FXML
    private void onActionEdit(ActionEvent event) {
    }

    @FXML
    private void onKeyPressedRemover(KeyEvent event) {
    }

    @FXML
    private void onActionRemover(ActionEvent event) {
    }

    @FXML
    private void onKeyPressedProviders(KeyEvent event) {
    }

    @FXML
    private void onActionProviders(ActionEvent event) {
    }
    
}
