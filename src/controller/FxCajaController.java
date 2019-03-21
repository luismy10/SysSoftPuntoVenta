package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.CajaADO;
import model.CajaTB;
import model.MovimientoCajaADO;
import model.MovimientoCajaTB;

public class FxCajaController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblCargo;
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblFondoCaja;
    @FXML
    private Label lblDevoluciones;
    @FXML
    private Label lblEfectivo;
    @FXML
    private Label lblVentasEfectivo;
    @FXML
    private Label lblVentasCredito;
    @FXML
    private Label lblAbonosEfectivo;
    @FXML
    private Label lblEntradas;
    @FXML
    private Label lblSalidas;
    @FXML
    private Label lblTotalDineroCaja;
    @FXML
    private Button btnTerminarTurno;

    private AnchorPane content;

    private double salidas;

    private double totalDineroCaja;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void corteCaja() {
        CajaTB cajaTB = CajaADO.ValidarAperturaCaja(Session.CAJA_ID);
        if (cajaTB != null) {
            lblCargo.setText(Session.USER_NAME);
            lblFecha.setText(Tools.getDate("dd-MM-yyyy"));
            MovimientoCajaTB fondoCaja = MovimientoCajaADO.FondoCaja(cajaTB.getIdCaja());
            if (fondoCaja != null) {
                lblFondoCaja.setText(Tools.roundingValue(fondoCaja.getSaldo(), 2));
                totalDineroCaja = fondoCaja.getSaldo();
            }
            MovimientoCajaTB ventasEfectivo = MovimientoCajaADO.VentasEfectivo(cajaTB.getIdCaja());
            if (ventasEfectivo != null) {
                lblEfectivo.setText(Tools.roundingValue(ventasEfectivo.getSaldo(), 2));
                lblVentasEfectivo.setText(Tools.roundingValue(ventasEfectivo.getSaldo(), 2));
                totalDineroCaja = totalDineroCaja + ventasEfectivo.getSaldo();
            }
            MovimientoCajaTB ventasCredito = MovimientoCajaADO.VentasCredito(cajaTB.getIdCaja());
            if (ventasCredito != null) {
                lblVentasCredito.setText(Tools.roundingValue(ventasCredito.getSaldo(), 2));
            }
            MovimientoCajaTB egresosEfectivoCompra = MovimientoCajaADO.EgresosEfectivoCompra(cajaTB.getIdCaja());
            if (egresosEfectivoCompra != null) {
                salidas += egresosEfectivoCompra.getSaldo();
                lblSalidas.setText("-" + Tools.roundingValue(salidas, 2));
                totalDineroCaja = totalDineroCaja - egresosEfectivoCompra.getSaldo();
            }
            MovimientoCajaTB egresosEfectivo = MovimientoCajaADO.EgresosEfectivo(cajaTB.getIdCaja());
            if (egresosEfectivo != null) {
                salidas += egresosEfectivo.getSaldo();
                lblSalidas.setText("-" + Tools.roundingValue(salidas, 2));
                totalDineroCaja = totalDineroCaja - egresosEfectivo.getSaldo();
            }

            lblTotalDineroCaja.setText(Session.MONEDA + " " + Tools.roundingValue(Math.abs(totalDineroCaja), 2));
            btnTerminarTurno.setDisable(false);
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Corte de caja", "No está aperturado la caja.", false);
            btnTerminarTurno.setDisable(true);
        }
    }

    @FXML
    private void onActionCorte(ActionEvent event) {
        corteCaja();
    }

    @FXML
    private void onKeyPressedCorte(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            corteCaja();
        }
    }

    @FXML
    private void onKeyPressedImprimir(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionImprimir(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedTerminarTurno(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionTerminarTurno(ActionEvent event) {

    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
