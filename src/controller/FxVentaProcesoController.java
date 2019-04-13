package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.ArticuloTB;
import model.CuentasClienteTB;
import model.PlazosADO;
import model.PlazosTB;
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
    @FXML
    private ComboBox<PlazosTB> cbPlazos;
    @FXML
    private DatePicker dtVencimiento;

    private TableView<ArticuloTB> tvList;

    private VentaTB ventaTB;

    private String tipo_comprobante;

    private String moneda_simbolo;

    private double vuelto;

    private double tota_venta;

    private boolean state_view_pago;

    private String documento, subTotal, descuento, importeTotal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        state_view_pago = false;
        tota_venta = 0;
        cbPlazos.getItems().clear();
        PlazosADO.GetTipoPlazoCombBox().forEach(e -> {
            this.cbPlazos.getItems().add(new PlazosTB(e.getIdPlazos(), e.getNombre(), e.getDias(), e.getEstado(), e.getPredeterminado()));
        });
        cbPlazos.getSelectionModel().select(0);
        if (cbPlazos.getSelectionModel().getSelectedIndex() >= 0) {
            dtVencimiento.setValue(LocalDate.now().plusDays(cbPlazos.getSelectionModel().getSelectedItem().getDias()));
        }
    }

    public void setInitComponents(VentaTB ventaTB, String cliente, String documento, TableView<ArticuloTB> tvList, String subTotal, String descuento, String importeTotal, String total) {
        this.ventaTB = ventaTB;
        this.tvList = tvList;
        this.documento = documento;
        moneda_simbolo = ventaTB.getMonedaName();
        lblComprobante.setText(ventaTB.getComprobanteName());
        lblCliente.setText("Cliente: " + cliente);
        lblTotal.setText(moneda_simbolo + " " + total);
        lblVuelto.setText(moneda_simbolo + " " + Tools.roundingValue(0, 2));
        tota_venta = Double.parseDouble(total);
        this.subTotal = moneda_simbolo + " " + Tools.roundingValue(Double.parseDouble(subTotal), 2);
        this.descuento = moneda_simbolo + " " + Tools.roundingValue(Double.parseDouble(descuento), 2);
        this.importeTotal = moneda_simbolo + " " + Tools.roundingValue(Double.parseDouble(importeTotal), 2);
        txtEfectivo.requestFocus();
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        if (state_view_pago) {
            if (cbPlazos.getSelectionModel().getSelectedIndex() < 0) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Venta", "Seleccionar el plazo.", false);
                cbPlazos.requestFocus();
            } else if (dtVencimiento.getValue() == null) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Venta", "El formato de la fecha no es correcto.", false);
                dtVencimiento.requestFocus();
            } else {
                ventaTB.setObservaciones(txtObservacion.getText().trim());
                ventaTB.setTipo(2);
                ventaTB.setEstado(2);
                ventaTB.setEfectivo(0);
                ventaTB.setVuelto(0);
                CuentasClienteTB cuentasCliente = new CuentasClienteTB();
                cuentasCliente.setPlazos(cbPlazos.getSelectionModel().getSelectedItem().getIdPlazos());
                cuentasCliente.setFechaVencimiento(LocalDateTime.of(dtVencimiento.getValue(), LocalTime.now()));
                short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Venta", "¿Esta seguro de continuar?", true);
                if (confirmation == 1) {
                    tipo_comprobante = ventaController.obtenerTipoComprobante().toLowerCase();
                    String[] result = VentaADO.CrudVenta(ventaTB, tvList, tipo_comprobante, cuentasCliente).split("/");
                    switch (result[0]) {
                        case "register":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Venta", "Se guardo correctamente la venta al crédito.", false);
                            ventaController.resetVenta();
                            Tools.Dispose(window);
                            break;
                        default:
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Venta", result[0], false);
                            break;
                    }
                }
            }

        } else {
            if (Tools.isNumeric(txtEfectivo.getText().trim())) {
                ventaTB.setObservaciones(txtObservacion.getText().trim());
                ventaTB.setTipo(1);
                ventaTB.setEstado(1);
                ventaTB.setEfectivo(Double.parseDouble(txtEfectivo.getText()));
                ventaTB.setVuelto(vuelto);
                if (vuelto < 0) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Venta", "Su cambio no puede ser negativo.", false);
                } else {
                    short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Venta", "¿Esta seguro de continuar?", true);
                    if (confirmation == 1) {
                        tipo_comprobante = ventaController.obtenerTipoComprobante().toLowerCase();
                        String[] result = VentaADO.CrudVenta(ventaTB, tvList, tipo_comprobante, new CuentasClienteTB()).split("/");
                        switch (result[0]) {
                            case "register":
                                short value = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Venta", "Se realiazo la venta con éxito, ¿Desea imprimir el comprobante?");
                                if (value == 1) {
                                    ventaController.imprimirVenta(documento, tvList, subTotal, descuento, importeTotal, lblTotal.getText(), moneda_simbolo + " " + Tools.roundingValue(Double.parseDouble(txtEfectivo.getText()), 2), lblVuelto.getText(), result[1]);
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
        }
    }

    @FXML
    private void onKeyReleasedEfectivo(KeyEvent event) {
        if (Tools.isNumeric(txtEfectivo.getText())) {
            vuelto = Double.parseDouble(txtEfectivo.getText()) - tota_venta;
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

    @FXML
    private void onActionPlazos(ActionEvent event) {
        if (cbPlazos.getSelectionModel().getSelectedIndex() >= 0) {
            dtVencimiento.setValue(LocalDate.now().plusDays(cbPlazos.getSelectionModel().getSelectedItem().getDias()));
        }
    }

    public void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

}
