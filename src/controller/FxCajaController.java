package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
    private Label lblEntradas;
    @FXML
    private Label lblSalidas;
    @FXML
    private Label lblTotalDineroCaja;
    @FXML
    private Button btnTerminarTurno;

    private AnchorPane content;

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

            MovimientoCajaTB ingresosEfectivo = MovimientoCajaADO.IngresosEfectivo(cajaTB.getIdCaja());
            if (ingresosEfectivo != null) {
                lblEntradas.setText(Tools.roundingValue(ingresosEfectivo.getSaldo(), 2));
                totalDineroCaja = totalDineroCaja + ingresosEfectivo.getSaldo();
            }

            MovimientoCajaTB egresosEfectivoCompra = MovimientoCajaADO.EgresosEfectivoCompra(cajaTB.getIdCaja());
            if (egresosEfectivoCompra != null) {
                lblSalidas.setText("-" + Tools.roundingValue(egresosEfectivoCompra.getSaldo(), 2));
                totalDineroCaja = totalDineroCaja - egresosEfectivoCompra.getSaldo();
            }

            MovimientoCajaTB egresosEfectivo = MovimientoCajaADO.EgresosEfectivo(cajaTB.getIdCaja());
            if (egresosEfectivo != null) {
                lblSalidas.setText("-" + Tools.roundingValue(egresosEfectivo.getSaldo(), 2));
                totalDineroCaja = totalDineroCaja - egresosEfectivo.getSaldo();
            }

            MovimientoCajaTB devolucionesEfectivo = MovimientoCajaADO.DevolucionesEfectivo(cajaTB.getIdCaja());
            if (devolucionesEfectivo != null) {
                lblDevoluciones.setText("-" + Tools.roundingValue(devolucionesEfectivo.getSaldo(), 2));
                totalDineroCaja = totalDineroCaja - devolucionesEfectivo.getSaldo();
            }

            lblTotalDineroCaja.setText(Session.MONEDA + " " + Tools.roundingValue(Math.abs(totalDineroCaja), 2));
            btnTerminarTurno.setDisable(false);
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Corte de caja", "No está aperturado la caja.", false);
            btnTerminarTurno.setDisable(true);
        }
    }

    private void InitializationTransparentBackground() {
        Session.PANE.setStyle("-fx-background-color: black");
        Session.PANE.setTranslateX(0);
        Session.PANE.setTranslateY(0);
        Session.PANE.setPrefWidth(Session.WIDTH_WINDOW);
        Session.PANE.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.PANE.setOpacity(0.7f);
        content.getChildren().add(Session.PANE);
    }

    private void terminarTurno() throws IOException {
        try {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_CAJACERRARCAJA);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());

            FxCajaCerrarCajaController controller = fXMLLoader.getController();
            controller.setInitCajaController(idActual, totalDineroCaja,content);

            Stage stage = FxWindow.StageLoaderModal(parent, "Realizar corte de caja", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.PANE);
            });
            stage.show();

        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
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
