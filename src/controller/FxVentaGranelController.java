package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.ArticuloTB;

public class FxVentaGranelController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtImporte;
    @FXML
    private Label lblArticulo;

    private FxVentaController ventaController;

    private ArticuloTB articuloTB;

    private int index;

    private boolean opcion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
    }

    public void initComponents(String value, ArticuloTB articuloTB, int index, boolean opcion) {
        lblTitle.setText(value);
        this.articuloTB = articuloTB;
        this.index = index;
        this.opcion = opcion;
        lblArticulo.setText(articuloTB.getNombreMarca());
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        if (opcion) {
            articuloTB.setPrecioVenta(Double.parseDouble(txtImporte.getText()) + articuloTB.getTotalImporte());
            articuloTB.setTotalImporte(
                    (articuloTB.getCantidad() * articuloTB.getPrecioVenta())
                    - articuloTB.getDescuento()
            );
            ventaController.getTvList().getItems().set(index, articuloTB);
            ventaController.calculateTotales();
            Tools.Dispose(window);
            ventaController.getTxtSearch().requestFocus();
            ventaController.getTxtSearch().clear();
        } else {
            articuloTB.setPrecioVenta(Double.parseDouble(txtImporte.getText()));
            articuloTB.setTotalImporte(
                    (articuloTB.getCantidad() * articuloTB.getPrecioVenta())
                    - articuloTB.getDescuento()
            );
            ventaController.getTvList().getItems().set(index, articuloTB);
            ventaController.calculateTotales();
            Tools.Dispose(window);
            ventaController.getTxtSearch().requestFocus();
            ventaController.getTxtSearch().clear();
        }
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
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
    private void onKeyTypedImporte(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtImporte.getText().contains(".") || c == '-' && txtImporte.getText().contains("-")) {
            event.consume();
        }
    }

    public void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

}
