package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.ArticuloTB;

public class FxVentaDescuentoController implements Initializable {
    
    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtPrecioVenta;
    @FXML
    private TextField txtPorcentajeDescuento;
    @FXML
    private TextField txtPrecioDescuento;
    
    private FxVentaController ventaController;
    
    private ArticuloTB articuloTB;
    
    private int index;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
    }
    
    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            
        }
    }
    
    @FXML
    private void onActionAceptar(ActionEvent event) {
        
    }
    
    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }
    
    @FXML
    private void onActionCancelar(ActionEvent event) {
        Tools.Dispose(window);
    }
    
    @FXML
    private void onKeyReleasedPorcentajeDescuento(KeyEvent event) {
        if (Tools.isNumeric(txtPrecioVenta.getText()) && Tools.isNumeric(txtPorcentajeDescuento.getText())) {
            Double precio = Double.parseDouble(txtPrecioVenta.getText());
            Double porcentaje = Double.parseDouble(txtPorcentajeDescuento.getText()) / 100.00;
            Double descuento = precio * porcentaje;
            Double total = precio - descuento;
            txtPrecioDescuento.setText(Tools.roundingValue(total, 2));
        }
    }
    
    @FXML
    private void onKeyTypedPorcentajeDescuento(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.')) {
            event.consume();
        }
        if (c == '.' && txtPorcentajeDescuento.getText().contains(".")) {
            event.consume();
        }
    }
    
    @FXML
    private void onKeyReleasedPrecioDescuento(KeyEvent event) {
        if (Tools.isNumeric(txtPrecioVenta.getText()) && Tools.isNumeric(txtPrecioDescuento.getText())) {
            Double valor = Double.parseDouble(txtPrecioVenta.getText());
            Double precio = Double.parseDouble(txtPrecioDescuento.getText());
            Double porcentaje = (precio * 100) / valor;
            int recalculado = (int) (100 - (Double.parseDouble(Tools.roundingValue(
                    Double.parseDouble(Tools.roundingValue(porcentaje, 2)),
                    0))));
            txtPorcentajeDescuento.setText(String.valueOf(recalculado)); 
        }
    }
    
    @FXML
    private void onKeyTypedPrecioDescuento(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.')) {
            event.consume();
        }
        if (c == '.' && txtPrecioDescuento.getText().contains(".")) {
            event.consume();
        }
    }
    
    public void initComponents(ArticuloTB articuloTB, int index) {
        this.articuloTB = articuloTB;
        txtPrecioVenta.setText(Tools.roundingValue(this.articuloTB.getTotalImporte(), 2));
        this.index = index;
    }
    
    public void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }
    
}
