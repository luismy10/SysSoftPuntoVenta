package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.ArticuloTB;
import model.VentaADO;
import model.VentaTB;

public class FxVentaProcesoController implements Initializable {

    private FxVentaController ventaController;
    @FXML
    private AnchorPane window;
    @FXML
    private Label lblTotal;
    @FXML
    private TextField txtEfectivo;
    @FXML
    private Label lblVuelto;
    @FXML
    private Text lblComprobante;
    @FXML
    private TextField txtObservacion;
    @FXML
    private Label lblCliente;
    @FXML
    private VBox vbEfectivo;
    @FXML
    private VBox vbCredito;
    @FXML
    private VBox vbViewEfectivo;
    @FXML
    private VBox vbViewCredito;
    @FXML
    private Label lblEfectivo;
    @FXML
    private Label lblCredito;

    private TableView<ArticuloTB> tvList;

    private VentaTB ventaTB;

    private String tipo_comprobante;

    private String moneda_simbolo;

    private double tota_venta;

    private boolean state_view_pago;

    private String subTotal, descuento, importeTotal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        state_view_pago = false;
        tota_venta = 0;
    }

    public void setInitComponents(VentaTB ventaTB, String cliente, TableView<ArticuloTB> tvList, String subTotal, String descuento, String importeTotal, String total) {
        this.ventaTB = ventaTB;
        this.tvList = tvList;
        this.subTotal = moneda_simbolo + " " + Tools.roundingValue(Double.parseDouble(subTotal), 2);
        this.descuento = moneda_simbolo + " " + Tools.roundingValue(Double.parseDouble(descuento), 2);
        this.importeTotal = moneda_simbolo + " " + Tools.roundingValue(Double.parseDouble(importeTotal), 2);
        moneda_simbolo = ventaTB.getMonedaName();
        lblComprobante.setText(ventaTB.getComprobanteName());
        lblCliente.setText("Cliente: " + cliente);
        lblTotal.setText(moneda_simbolo + " " + total);
        lblVuelto.setText(moneda_simbolo + " " + Tools.roundingValue(0, 2));
        tota_venta = Double.parseDouble(total);
        txtEfectivo.requestFocus();
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        if (Tools.isNumeric(txtEfectivo.getText())) {
            ventaTB.setObservaciones(txtObservacion.getText().trim());
            ventaTB.setEstado(state_view_pago ? 3 : 1);
            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Venta", "¿Esta seguro de continuar?", true);
            if (confirmation == 1) {
                tipo_comprobante = ventaController.obtenerTipoComprobante().toLowerCase();
                String[] result = VentaADO.CrudVenta(ventaTB, tvList, tipo_comprobante).split("/");
                switch (result[0]) {
                    case "register":
                        short value = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Venta", "Se realiazo la venta con éxito, ¿Desea imprimir el comprobante?");
                        if (value == 1) {
                            ventaController.imprimirVenta(subTotal,descuento,importeTotal,lblTotal.getText(), moneda_simbolo + " " + Tools.roundingValue(Double.parseDouble(txtEfectivo.getText()), 2), lblVuelto.getText(), result[1]);
                            ventaController.resetVenta();
                            Tools.Dispose(window);
                        } else {
                            ventaController.resetVenta();
                            Tools.Dispose(window);
                        }

                        break;
                    default:
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Venta", result[0], false);
                        break;
                }
            }

        }

    }

    @FXML
    private void onKeyReleasedEfectivo(KeyEvent event) {
        if (Tools.isNumeric(txtEfectivo.getText())) {
            double vuelto = Double.parseDouble(txtEfectivo.getText()) - tota_venta;
            lblVuelto.setText(moneda_simbolo + " " + Tools.roundingValue(vuelto, 2));
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

    @FXML
    private void onMouseClickedEfectivo(MouseEvent event) {
        if (state_view_pago) {
            vbCredito.setStyle("-fx-background-color: white;-fx-cursor:hand;-fx-padding: 0.8333333333333334em;");
            vbEfectivo.setStyle("-fx-background-color: #265B7C;-fx-cursor:hand;-fx-padding: 0.8333333333333334em;");

            lblCredito.setStyle("-fx-text-fill:#1a2226;");
            lblEfectivo.setStyle("-fx-text-fill:white;");

            vbViewCredito.setVisible(false);
            vbViewEfectivo.setVisible(true);

            txtEfectivo.requestFocus();

            state_view_pago = false;
        }
    }

    @FXML
    private void onMouseClickedCredito(MouseEvent event) {
        if (!state_view_pago) {
            vbEfectivo.setStyle("-fx-background-color: white;-fx-cursor:hand;-fx-padding: 0.8333333333333334em;");
            vbCredito.setStyle("-fx-background-color: #265B7C;-fx-cursor:hand;-fx-padding: 0.8333333333333334em;");

            lblEfectivo.setStyle("-fx-text-fill:#1a2226;");
            lblCredito.setStyle("-fx-text-fill:white;");

            vbViewEfectivo.setVisible(false);
            vbViewCredito.setVisible(true);
            state_view_pago = true;
        }
    }

    public void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

}
