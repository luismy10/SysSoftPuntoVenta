package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
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
    @FXML
    private TextField txtDescuento;

    private FxComprasController comprasController;

    private boolean validationelemnt;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        validationelemnt = false;
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
        if (!Tools.isNumeric(txtCantidad.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico en la cantidad", false);
            txtCantidad.requestFocus();
        } else if (!Tools.isNumeric(txtCosto.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico en el costo", false);
            txtCosto.requestFocus();
        } else if (!Tools.isNumeric(txtPrecio.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico en el precio", false);
            txtPrecio.requestFocus();
        } else {
            ArticuloTB articuloTB = new ArticuloTB();
            articuloTB.setClave(lblClave.getText());
            articuloTB.setNombre(lblDescripcion.getText());
            articuloTB.setCantidad(Double.parseDouble(txtCantidad.getText()));
            articuloTB.setPrecioCompra(Double.parseDouble(txtCosto.getText()));
            articuloTB.setPrecioVenta(Double.parseDouble(txtPrecio.getText()));
            articuloTB.setDescuento(txtDescuento.getText().isEmpty() ? 0
                    : Double.parseDouble(txtDescuento.getText()));
            articuloTB.setTotal(
                    (Double.parseDouble(txtCantidad.getText()) * Double.parseDouble(txtCosto.getText()))
                    - Double.parseDouble(txtDescuento.getText().isEmpty() ? "0"
                            : txtDescuento.getText()
                    ));

            if (validateStock(comprasController.getTvList(), articuloTB) && !validationelemnt) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ya existe en la lista el artículo", false);
            } else if (validationelemnt) {
                ObservableList<ArticuloTB> observableList, articuloTBs;
                observableList = comprasController.getTvList().getItems();
                articuloTBs = comprasController.getTvList().getSelectionModel().getSelectedItems();
                articuloTBs.forEach(e -> observableList.remove(e));
                comprasController.setCalculateTotals();
                validateStock(comprasController.getTvList(), articuloTB);
                comprasController.setCalculateTotals();
            } else {
                comprasController.setCalculateTotals();
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

    void setLoadEdit(ArticuloTB articuloTB) {
        lblClave.setText(articuloTB.getClave().get());
        lblDescripcion.setText(articuloTB.getNombre().get());
        txtCantidad.setText("" + articuloTB.getCantidad().get());
        txtCosto.setText("" + articuloTB.getPrecioCompra());
        txtDescuento.setText("" + articuloTB.getDescuento().get());
        txtPrecio.setText("" + articuloTB.getPrecioVenta().get());
        validationelemnt = true;
    }

    private void generationPrice() {
        double importe = Double.parseDouble(txtImporte.getText());
        double cantidad = Double.parseDouble(txtCantidad.getText());

        double preciocompra = importe / cantidad;
        txtCosto.setText(Tools.roundingValue(preciocompra, 2));

        double valorNeto = Tools.calculateValueNeto(18, preciocompra);
        txtCostoImpuesto.setText(Tools.roundingValue(valorNeto, 2));

        txtPrecio.setText(Tools.calculateAumento(30, preciocompra));

        double valorNetoVenta = Tools.calculateValueNeto(18, Double.parseDouble(txtPrecio.getText()));

        txtPrecioImpuesto.setText(Tools.roundingValue(valorNetoVenta, 2));

        txtUtilidad.setText(Tools.roundingValue((Double.parseDouble(txtPrecio.getText()) - preciocompra), 2));
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

    @FXML
    private void onKeyTypedCantidad(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtCantidad.getText().contains(".") || c == '-' && txtCantidad.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedCosto(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtCosto.getText().contains(".") || c == '-' && txtCosto.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedDescuento(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtDescuento.getText().contains(".") || c == '-' && txtDescuento.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedPrecio(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecio.getText().contains(".") || c == '-' && txtPrecio.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onActionImporte(ActionEvent event) {
        if (Tools.isNumeric(txtImporte.getText()) && Tools.isNumeric(txtCantidad.getText())) {
            generationPrice();
        }

    }

}
