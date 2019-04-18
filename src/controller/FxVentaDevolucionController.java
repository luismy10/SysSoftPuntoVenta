package controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.ArticuloTB;
import model.MovimientoCajaTB;
import model.VentaADO;

public class FxVentaDevolucionController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Text lblComprobante;
    @FXML
    private Label lblTotal;
    @FXML
    private RadioButton rbMovimiento;
    @FXML
    private TextField txtEfectivo;
    @FXML
    private TextField txtObservacion;

    private FxVentaDetalleController ventaDetalleController;

    private String idVenta;

    private ObservableList<ArticuloTB> arrList;

    private double totalVenta;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setLoadVentaDevolucion(String idVenta, ObservableList<ArticuloTB> arrList, String comprobante, String total, double totalVenta) {
        this.idVenta = idVenta;
        this.arrList = arrList;
        this.totalVenta = totalVenta;
        lblComprobante.setText(comprobante);
        lblTotal.setText(total);
        txtEfectivo.setText(Tools.roundingValue(totalVenta, 2));
    }

    private void eventAceptar() {
        if (rbMovimiento.isSelected()) {
            if (!Tools.isNumeric(txtEfectivo.getText())) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle de venta", "Ingrese el monto a devolver", false);
                txtEfectivo.requestFocus();
            } else if (txtObservacion.getText().trim().isEmpty()) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle de venta", "Ingrese un comentario", false);
                txtObservacion.requestFocus();
            } else {
                short validate = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Detalle de ventas", "¿Está seguro de cancelar la venta?", true);
                if (validate == 1) {
                    MovimientoCajaTB cajaTB = new MovimientoCajaTB();
                    cajaTB.setIdCaja(Session.CAJA_ID);
                    cajaTB.setIdUsuario(Session.USER_ID);
                    cajaTB.setFechaMovimiento(LocalDateTime.now());
                    cajaTB.setComentario(txtObservacion.getText().trim());
                    cajaTB.setMovimiento("VENCAN");
                    cajaTB.setSaldo(Double.parseDouble(txtEfectivo.getText()));

                    String result = VentaADO.CancelTheSale(idVenta, arrList, totalVenta, cajaTB);
                    if (result.equalsIgnoreCase("update")) {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle de venta", "Se ha cancelado con éxito", false);
                        Tools.Dispose(window);
                        ventaDetalleController.setInitComponents(idVenta);
                    } else if (result.equalsIgnoreCase("scrambled")) {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle de venta", "Ya está cancelada la venta!", false);
                    } else {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle de venta", result, false);
                    }
                }
            }
        } else {

            if (txtObservacion.getText().trim().isEmpty()) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle de venta", "Ingrese un comentario", false);
                txtObservacion.requestFocus();
            } else {
                short validate = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Detalle de ventas", "¿Está seguro de cancelar la venta?", true);
                if (validate == 1) {
                    MovimientoCajaTB cajaTB = new MovimientoCajaTB();
                    cajaTB.setIdCaja(Session.CAJA_ID);
                    cajaTB.setIdUsuario(Session.USER_ID);
                    cajaTB.setFechaMovimiento(LocalDateTime.now());
                    cajaTB.setComentario(txtObservacion.getText().trim());
                    cajaTB.setMovimiento("VENCAN");
                    cajaTB.setSaldo(0);

                    String result = VentaADO.CancelTheSale(idVenta, arrList, totalVenta, cajaTB);
                    if (result.equalsIgnoreCase("update")) {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle de venta", "Se ha cancelado con éxito", false);
                        Tools.Dispose(window);
                        ventaDetalleController.setInitComponents(idVenta);
                    } else if (result.equalsIgnoreCase("scrambled")) {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle de venta", "Ya está cancelada la venta!", false);
                    } else {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle de venta", result, false);
                    }
                }
            }

        }

    }

    @FXML
    private void onActionMovimiento(ActionEvent event) {
        txtEfectivo.setDisable(!rbMovimiento.isSelected());
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        eventAceptar();
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            eventAceptar();
        }
    }

    @FXML
    private void onKeyTypedEfectivo(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.')) {
            event.consume();
        }
        if (c == '.' && txtEfectivo.getText().contains(".")) {
            event.consume();
        }
    }

    public void setInitVentaDetalle(FxVentaDetalleController ventaDetalleController) {
        this.ventaDetalleController = ventaDetalleController;
    }

}
