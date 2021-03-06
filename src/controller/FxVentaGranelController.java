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

    private void executeEventAceptar() {
        if (Tools.isNumeric(txtImporte.getText())) {
            if (opcion) {
                double precio = Double.parseDouble(txtImporte.getText()) + articuloTB.getPrecioVentaGeneral();
                double descuento = articuloTB.getDescuento();
                double porcentajeDecimal = descuento / 100.00;
                double porcentajeRestante = precio * porcentajeDecimal;

                articuloTB.setDescuentoSumado(porcentajeRestante * articuloTB.getCantidad());

                articuloTB.setPrecioVentaGeneralReal(precio);
                articuloTB.setPrecioVentaGeneral(precio - porcentajeRestante);

                articuloTB.setSubImporte(articuloTB.getCantidad() * articuloTB.getPrecioVentaGeneralReal());
                articuloTB.setTotalImporte(articuloTB.getCantidad() * articuloTB.getPrecioVentaGeneral());

                articuloTB.setImpuestoArticuloName(ventaController.getTaxName(articuloTB.getImpuestoArticulo()));
                articuloTB.setImpuestoValor(ventaController.getTaxValue(articuloTB.getImpuestoArticulo()));
                articuloTB.setImpuestoSumado(articuloTB.getCantidad() * (articuloTB.getPrecioVentaGeneral() * (articuloTB.getImpuestoValor() / 100.00)));

                articuloTB.setSubImporteDescuento(articuloTB.getSubImporte() - articuloTB.getDescuentoSumado());

                ventaController.getTvList().getItems().set(index, articuloTB);
                ventaController.calculateTotales();
                Tools.Dispose(window);
                ventaController.getTxtSearch().requestFocus();
                ventaController.getTxtSearch().clear();
            } else {
                double precio = Double.parseDouble(txtImporte.getText());
                double descuento = articuloTB.getDescuento();
                double porcentajeDecimal = descuento / 100.00;
                double porcentajeRestante = precio * porcentajeDecimal;

                articuloTB.setDescuentoSumado(porcentajeRestante * articuloTB.getCantidad());

                articuloTB.setPrecioVentaGeneralReal(precio);
                articuloTB.setPrecioVentaGeneral(precio - porcentajeRestante);

                articuloTB.setSubImporte(articuloTB.getCantidad() * articuloTB.getPrecioVentaGeneralReal());
                articuloTB.setTotalImporte(articuloTB.getCantidad() * articuloTB.getPrecioVentaGeneral());

                articuloTB.setImpuestoArticuloName(ventaController.getTaxName(articuloTB.getImpuestoArticulo()));
                articuloTB.setImpuestoValor(ventaController.getTaxValue(articuloTB.getImpuestoArticulo()));
                articuloTB.setImpuestoSumado(articuloTB.getCantidad() * (articuloTB.getPrecioVentaGeneral() * (articuloTB.getImpuestoValor() / 100.00)));

                articuloTB.setSubImporteDescuento(articuloTB.getSubImporte() - articuloTB.getDescuentoSumado());

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
        executeEventAceptar();
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            executeEventAceptar();
        }
    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
            ventaController.getTxtSearch().requestFocus();
            ventaController.getTxtSearch().clear();
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Tools.Dispose(window);
        ventaController.getTxtSearch().requestFocus();
        ventaController.getTxtSearch().clear();
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
