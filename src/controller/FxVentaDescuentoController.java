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

    private FxVentaController ventaController;

    private ArticuloTB articuloTB;

    private int index;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
    }

    public void initComponents(ArticuloTB articuloTB, int index) {
        this.articuloTB = articuloTB;
        txtPrecioVenta.setText(Tools.roundingValue(articuloTB.getPrecioVenta(), 2));
        this.index = index;
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (Tools.isNumeric(txtPrecioVenta.getText()) && Tools.isNumeric(txtPorcentajeDescuento.getText())) {
                double precio = Double.parseDouble(txtPrecioVenta.getText());
                double descuento = Double.parseDouble(txtPorcentajeDescuento.getText());
                double porcentajeDecimal = descuento / 100.00;
                double porcentajeRestante = precio * porcentajeDecimal;

                articuloTB.setPrecioVentaReal(articuloTB.getPrecioVenta());
                articuloTB.setPrecioVenta(articuloTB.getPrecioVenta() - porcentajeRestante);

                articuloTB.setDescuento(descuento);
                articuloTB.setDescuentoSumado(porcentajeRestante * articuloTB.getCantidad());

                articuloTB.setSubImporte(articuloTB.getCantidad() * articuloTB.getPrecioVentaReal());
                articuloTB.setTotalImporte(articuloTB.getCantidad() * articuloTB.getPrecioVenta());

                articuloTB.setImpuestoSumado(articuloTB.getCantidad() * (articuloTB.getPrecioVenta() * (ventaController.getTaxValue(articuloTB.getImpuestoArticulo()) / 100.00)));

                ventaController.getTvList().getItems().set(index, articuloTB);
                ventaController.calculateTotales();
                Tools.Dispose(window);
                ventaController.getTxtSearch().requestFocus();
                ventaController.getTxtSearch().clear();
            }

        }
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        if (Tools.isNumeric(txtPrecioVenta.getText()) && Tools.isNumeric(txtPorcentajeDescuento.getText())) {
            double precio = Double.parseDouble(txtPrecioVenta.getText());
            double descuento = Double.parseDouble(txtPorcentajeDescuento.getText());
            double porcentajeDecimal = descuento / 100.00;
            double porcentajeRestante = precio * porcentajeDecimal;

            articuloTB.setPrecioVentaReal(articuloTB.getPrecioVenta());
            articuloTB.setPrecioVenta(articuloTB.getPrecioVenta() - porcentajeRestante);

            articuloTB.setDescuento(descuento);
            articuloTB.setDescuentoSumado(porcentajeRestante * articuloTB.getCantidad());

            articuloTB.setSubImporte(articuloTB.getCantidad() * articuloTB.getPrecioVentaReal());
            articuloTB.setTotalImporte(articuloTB.getCantidad() * articuloTB.getPrecioVenta());

            articuloTB.setImpuestoSumado(articuloTB.getCantidad() * (articuloTB.getPrecioVenta() * (ventaController.getTaxValue(articuloTB.getImpuestoArticulo()) / 100.00)));

            ventaController.getTvList().getItems().set(index, articuloTB);
            ventaController.calculateTotales();
            Tools.Dispose(window);
            ventaController.getTxtSearch().requestFocus();
            ventaController.getTxtSearch().clear();
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
    private void onKeyTypedPorcentajeDescuento(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.')) {
            event.consume();
        }       
    }

    public void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

}
