package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.ArticuloTB;

public class FxArticuloCompraController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtImporte;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtCosto;
    @FXML
    private TextField txtCostoImpuesto;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtPrecioImpuesto;
    @FXML
    private TextField txtUtilidad;
    @FXML
    private Text lblClave;
    @FXML
    private Text lblDescripcion;
    @FXML
    private CheckBox cbImpuesto;
    
    private FxComprasController comprasController;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
    }

    private boolean validateStock(TableView<ArticuloTB> view, ArticuloTB articuloTB) {
        boolean ret = false;
        for (int i = 0; i < view.getItems().size(); i++) {
            if (view.getItems().get(i).getClave().get().equals(articuloTB.getClave().get())) {
                ret = true;
                break;
            }
        }
        if (!ret) {
            view.getItems().add(articuloTB);
        }
        return ret;
    }

    @FXML
    private void onActionAdd(ActionEvent event) {
        if (txtCantidad.getText().isEmpty()) {

            txtCantidad.requestFocus();
        } else if (txtCosto.getText().isEmpty()) {

            txtCosto.requestFocus();
        } else if (txtPrecio.getText().isEmpty()) {

            txtPrecio.requestFocus();
        } else {
            ArticuloTB articuloTB = new ArticuloTB();
            articuloTB.setId(comprasController.getCount());
            articuloTB.setClave(lblClave.getText());
            articuloTB.setNombre(lblDescripcion.getText());
            articuloTB.setCantidad(Double.parseDouble(txtCantidad.getText()));
            articuloTB.setPrecioCompra(Double.parseDouble(txtCosto.getText()));
            articuloTB.setPrecioVenta(Double.parseDouble(txtPrecio.getText()));
            articuloTB.setDescuento(0);
            articuloTB.setTotal(Double.parseDouble(txtCantidad.getText()) * Double.parseDouble(txtCosto.getText()));

            if (validateStock(comprasController.getTvList(), articuloTB)) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ya existe en la lista el artículo", false);
            } else {
                comprasController.setCount(comprasController.getCount() + 1);
            }

            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionCancel(ActionEvent event) {
        Tools.Dispose(window);
    }

    void setInitCompraController(FxComprasController comprasController) {
        this.comprasController = comprasController;
    }

    void setLoadData(String... value) {
        lblClave.setText(value[0]);
        lblDescripcion.setText(value[1]);
    }

}
