package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.MovimientoCajaADO;
import model.MovimientoCajaTB;

public class FxCajaConsultasController implements Initializable {

    @FXML
    private ScrollPane window;
    @FXML
    private Label lblTrabajador;
    @FXML
    private Label lblFechaCorte;
    @FXML
    private Label lblFondoCaja;
    @FXML
    private Label lblVentasEfectivo;
    @FXML
    private Label lblEntradas;
    @FXML
    private Label lblSalidas;
    @FXML
    private Label lblDevoluciones;
    @FXML
    private Label lblTotalDineroCaja;
    @FXML
    private Label lblEfectivo;
    @FXML
    private Label lblVentasCredito;
    @FXML
    private TableView<MovimientoCajaTB> tvLista;
    @FXML
    private TableColumn<MovimientoCajaTB, String> tcFechaRegistro;
    @FXML
    private TableColumn<MovimientoCajaTB, String> tcComentario;
    @FXML
    private TableColumn<MovimientoCajaTB, String> tcMov;
    @FXML
    private TableColumn<MovimientoCajaTB, String> tcEntrada;
    @FXML
    private TableColumn<MovimientoCajaTB, String> tcSalida;

    private AnchorPane windowinit;

    private double totalDineroCaja;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcFechaRegistro.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getFechaMovimiento().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + "\n"
                + cellData.getValue().getFechaMovimiento().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
        ));
        tcComentario.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getComentario()
        ));
        tcMov.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getMovimiento()
        ));
        tcEntrada.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getEntrada(), 2)
        ));
        tcSalida.setCellValueFactory(cellData -> Bindings.concat(
                 Tools.roundingValue(cellData.getValue().getSalidas(), 2)
        ));

        tcFechaRegistro.prefWidthProperty().bind(tvLista.widthProperty().multiply(0.18));
        tcComentario.prefWidthProperty().bind(tvLista.widthProperty().multiply(0.25));
        tcMov.prefWidthProperty().bind(tvLista.widthProperty().multiply(0.15));
        tcEntrada.prefWidthProperty().bind(tvLista.widthProperty().multiply(0.20));
        tcSalida.prefWidthProperty().bind(tvLista.widthProperty().multiply(0.20));
    }

    public void loadDataCorteCaja(String fecha, String usuario, int idCaja) {
        clearListas();
        lblFechaCorte.setText(fecha);
        lblTrabajador.setText(usuario);
        MovimientoCajaTB fondoCaja = MovimientoCajaADO.FondoCaja(idCaja);
        if (fondoCaja != null) {
            lblFondoCaja.setText(Tools.roundingValue(fondoCaja.getSaldo(), 2));
            totalDineroCaja = fondoCaja.getSaldo();
        }

        MovimientoCajaTB ventasEfectivo = MovimientoCajaADO.VentasEfectivo(idCaja);
        if (ventasEfectivo != null) {
            lblEfectivo.setText(Tools.roundingValue(ventasEfectivo.getSaldo(), 2));
            lblVentasEfectivo.setText(Tools.roundingValue(ventasEfectivo.getSaldo(), 2));
            totalDineroCaja = totalDineroCaja + ventasEfectivo.getSaldo();
        }

        MovimientoCajaTB ventasCredito = MovimientoCajaADO.VentasCredito(idCaja);
        if (ventasCredito != null) {
            lblVentasCredito.setText(Tools.roundingValue(ventasCredito.getSaldo(), 2));
        }

        MovimientoCajaTB ingresosEfectivo = MovimientoCajaADO.IngresosEfectivo(idCaja);
        if (ingresosEfectivo != null) {
            lblEntradas.setText(Tools.roundingValue(ingresosEfectivo.getSaldo(), 2));
            totalDineroCaja = totalDineroCaja + ingresosEfectivo.getSaldo();
        }

        MovimientoCajaTB egresosEfectivoCompra = MovimientoCajaADO.EgresosEfectivoCompra(idCaja);
        if (egresosEfectivoCompra != null) {
            lblSalidas.setText("-" + Tools.roundingValue(egresosEfectivoCompra.getSaldo(), 2));
            totalDineroCaja = totalDineroCaja - egresosEfectivoCompra.getSaldo();
        }

        MovimientoCajaTB egresosEfectivo = MovimientoCajaADO.EgresosEfectivo(idCaja);
        if (egresosEfectivo != null) {
            lblSalidas.setText("-" + Tools.roundingValue(egresosEfectivo.getSaldo(), 2));
            totalDineroCaja = totalDineroCaja - egresosEfectivo.getSaldo();
        }

        MovimientoCajaTB devolucionesEfectivo = MovimientoCajaADO.DevolucionesEfectivo(idCaja);
        if (devolucionesEfectivo != null) {
            lblDevoluciones.setText("-" + Tools.roundingValue(devolucionesEfectivo.getSaldo(), 2));
            totalDineroCaja = totalDineroCaja - devolucionesEfectivo.getSaldo();
        }

        lblTotalDineroCaja.setText(Session.MONEDA + " " + Tools.roundingValue(Math.abs(totalDineroCaja), 2));
        
        tvLista.setItems(MovimientoCajaADO.ListarCajasAperturadas(idCaja));

    }

    private void clearListas() {
        lblFondoCaja.setText("00.00");
        lblVentasEfectivo.setText("00.00");
        lblEntradas.setText("00.00");
        lblSalidas.setText("00.00");
        lblDevoluciones.setText("00.00");
        lblEfectivo.setText("00.00");
        lblTotalDineroCaja.setText("00.00");
    }

    private void InitializationTransparentBackground() {
        Session.PANE.setStyle("-fx-background-color: black");
        Session.PANE.setTranslateX(0);
        Session.PANE.setTranslateY(0);
        Session.PANE.setPrefWidth(Session.WIDTH_WINDOW);
        Session.PANE.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.PANE.setOpacity(0.7f);
        windowinit.getChildren().add(Session.PANE);
    }

    private void openWindowCaja() {
        try {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_CAJABUSQUEDA);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxCajaBusquedaController controller = fXMLLoader.getController();
            controller.setInitCajaConsultasController(this);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Seleccionar corte de caja", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                windowinit.getChildren().remove(Session.PANE);
            });
            stage.show();

        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
            System.out.println(ex);

        }
    }

    @FXML
    private void onKeyPressedSearch(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowCaja();
        }
    }

    @FXML
    private void onActionSearch(ActionEvent event) {
        openWindowCaja();
    }

    @FXML
    private void onKeyPressedReport(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionReport(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedTicket(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionTicket(ActionEvent event) {

    }

    private void onKeyPressedDetalleMovimiento(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }


    public void setContent(AnchorPane windowinit) {
        this.windowinit = windowinit;
    }

    @FXML
    private void onMouseClickedLista(MouseEvent event) {
    }
}
