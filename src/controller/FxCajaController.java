package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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

    private int idActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idActual = 0;
    }

    private void corteCaja() {
        CajaTB cajaTB = CajaADO.ValidarAperturaCaja(Session.CAJA_ID);
        if (cajaTB != null) {
            idActual = cajaTB.getIdCaja();
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
            MovimientoCajaTB devolucionesEfectivo = MovimientoCajaADO.DevolucionesEfectivo(cajaTB.getIdCaja());
            if (devolucionesEfectivo != null) {
                salidas += devolucionesEfectivo.getSaldo();
                lblDevoluciones.setText("-" + Tools.roundingValue(salidas, 2));
                totalDineroCaja = totalDineroCaja - devolucionesEfectivo.getSaldo();
            }
            

            lblTotalDineroCaja.setText(Session.MONEDA + " " + Tools.roundingValue(Math.abs(totalDineroCaja), 2));
            btnTerminarTurno.setDisable(false);
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Corte de caja", "No está aperturado la caja.", false);
            btnTerminarTurno.setDisable(true);
        }
    }

    private void terminarTurno() throws IOException {
        short option = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Corte de caja", "¿Está seguro de cerrar su turno?", true);
        if (option == 1) {
            String result = CajaADO.CerarAperturaCaja(idActual, LocalDateTime.now(), false);
            if (result.equalsIgnoreCase("completed")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Corte de caja", "Se cerro correctamente la caja.", false);
                Tools.Dispose(content);
                URL urllogin = getClass().getResource(Tools.FX_FILE_LOGIN);
                FXMLLoader fXMLLoaderLogin = FxWindow.LoaderWindow(urllogin);
                Parent parent = fXMLLoaderLogin.load(urllogin.openStream());
                Scene scene = new Scene(parent);
                Stage primaryStage = new Stage();
                primaryStage.getIcons().add(new Image(Tools.FX_LOGO));
                primaryStage.setScene(scene);
                primaryStage.initStyle(StageStyle.DECORATED);
                primaryStage.setTitle("Sys Soft");
                primaryStage.centerOnScreen();
                primaryStage.setMaximized(true);
                primaryStage.show();
                primaryStage.requestFocus();
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Corte de caja", result, false);
            }
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
    private void onKeyPressedTerminarTurno(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            terminarTurno();
        }
    }

    @FXML
    private void onActionTerminarTurno(ActionEvent event) throws IOException {
        terminarTurno();
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
