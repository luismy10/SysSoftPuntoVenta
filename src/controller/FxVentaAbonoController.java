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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.CuentasClienteADO;
import model.CuentasClienteTB;
import model.CuentasHistorialClienteADO;
import model.CuentasHistorialClienteTB;

public class FxVentaAbonoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblPagado;
    @FXML
    private Label lblPendiente;
    @FXML
    private Label lblMontoInicial;
    @FXML
    private Label lblPlazos;
    @FXML
    private Label lblFechaVencimiento;
    @FXML
    private TableView<CuentasHistorialClienteTB> tvList;
    @FXML
    private TableColumn<CuentasHistorialClienteTB, String> tcFecha;
    @FXML
    private TableColumn<CuentasHistorialClienteTB, String> tcCuotas;
    @FXML
    private TableColumn<CuentasHistorialClienteTB, String> tcMonto;
    @FXML
    private TableColumn<CuentasHistorialClienteTB, String> tcReferencia;

    private FxVentaDetalleController ventaDetalleController;

    private String simboloMoneda;

    private int idCuentasCliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        simboloMoneda = "M";
        tcFecha.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaAbono().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))));
        tcCuotas.setCellValueFactory(cellData -> Bindings.concat());
        tcMonto.setCellValueFactory(cellData -> Bindings.concat(simboloMoneda + " " + Tools.roundingValue(cellData.getValue().getAbono(), 2)));
        tcReferencia.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getReferencia()));

    }

    public void loadInitData(String idVenta, String simboloMoneda) {
        this.simboloMoneda = simboloMoneda;
        CuentasClienteTB cuentasClienteTB = CuentasClienteADO.Get_CuentasCliente_ById(idVenta);
        if (cuentasClienteTB != null) {
            idCuentasCliente = cuentasClienteTB.getIdCuentasCliente();
            lblPlazos.setText(cuentasClienteTB.getPlazosName());
            lblFechaVencimiento.setText(cuentasClienteTB.getFechaVencimiento().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
            lblMontoInicial.setText(simboloMoneda + " " + Tools.roundingValue(cuentasClienteTB.getMontoInicial(), 2));
            lblPagado.setText(simboloMoneda + " 00.00");
            lblPendiente.setText(simboloMoneda + " 00.00");
            lblTotal.setText(simboloMoneda + " 00.00");
            tvList.setItems(CuentasHistorialClienteADO.ListarAbonos(idCuentasCliente));
        }
    }

    private void openWindowAbono() {
        try {
            URL url = getClass().getResource(Tools.FX_FILE_VENTAABONOPROCESO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //
            FxVentaAbonoProcesoController controller = fXMLLoader.getController();
            controller.setInitLoadVentaAbono(idCuentasCliente);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Abono", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {

            });
            stage.show();

        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @FXML
    private void onKeyPressedAbono(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowAbono();
        }
    }

    @FXML
    private void onActionAbono(ActionEvent event) {
        openWindowAbono();
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

    public void setInitVentaAbonoController(FxVentaDetalleController ventaDetalleController) {
        this.ventaDetalleController = ventaDetalleController;
    }

}
