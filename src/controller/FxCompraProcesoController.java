package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.ArticuloTB;
import model.CompraADO;
import model.CompraTB;
import model.LoteTB;

public class FxCompraProcesoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Text lblTotal;
    @FXML
    private TextField txtEfectivo;
    @FXML
    private TextField txtCredito;
    @FXML
    private TextField txtDiasCredito;
    @FXML
    private DatePicker fcVencimiento;
    @FXML
    private HBox hbPagoContado;
    @FXML
    private VBox vbPagoCredito;
    @FXML
    private VBox vbOtroPago;
    @FXML
    private RadioButton rbContado;
    @FXML
    private RadioButton tbCredito;
    @FXML
    private RadioButton tbOtroPago;
    @FXML
    private TextField txtProveedor;
    @FXML
    private TextField txtDescripcion;

    private FxCompraController compraController;

    private CompraTB compraTB = null;

    private TableView<ArticuloTB> tvList;

    private ObservableList<LoteTB> loteTBs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup group = new ToggleGroup();
        rbContado.setToggleGroup(group);
        tbCredito.setToggleGroup(group);
        tbOtroPago.setToggleGroup(group);
        System.out.println(hbPagoContado.isDisable());
        System.out.println(vbPagoCredito.isDisable());
        System.out.println(vbOtroPago.isDisable());
    }

    public void setLoadProcess(CompraTB compraTB, TableView<ArticuloTB> tvList, ObservableList<LoteTB> loteTBs, String total, String proveedor) {
        this.compraTB = compraTB;
        this.tvList = tvList;
        this.loteTBs = loteTBs;
        lblTotal.setText(total);
        txtProveedor.setText(proveedor);
    }

    private void executeCrud() {
        String result = CompraADO.CrudCompra(compraTB, tvList, loteTBs);
        if (result.equalsIgnoreCase("register")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Compra", "Se registró correctamente la compra.", false);
            Tools.Dispose(window);
            compraController.clearComponents();
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Compra", result, false);
        }
    }

    private void onEventProcess() {
        if (rbContado.isSelected()) {
            if (!Tools.isNumeric(txtEfectivo.getText())) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo efectivo.", false);
                txtEfectivo.requestFocus();
            } else {
                executeCrud();
            }
        } else if (tbCredito.isSelected()) {
            if (!Tools.isNumeric(txtCredito.getText())) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo crédito.", false);
                txtCredito.requestFocus();
            } else if (!Tools.isNumeric(txtDiasCredito.getText())) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo días de crédito.", false);
                txtDiasCredito.requestFocus();
            } else if (fcVencimiento.getValue() == null) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo fecha de vencimiento.", false);
                fcVencimiento.requestFocus();
            } else {
                executeCrud();
            }
        } else if (tbOtroPago.isSelected()) {
            if (txtDescripcion.getText().isEmpty()) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo descripción.", false);
                txtDescripcion.requestFocus();
            } else {
                executeCrud();
            }
        }

    }

    @FXML
    private void onKeyTypedEfectivo(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtEfectivo.getText().contains(".") || c == '-' && txtEfectivo.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedCredito(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtCredito.getText().contains(".") || c == '-' && txtCredito.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedDiasCredito(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b')) {
            event.consume();
        }
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onEventProcess();
        }
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        onEventProcess();
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
    private void onActionRbContado(ActionEvent event) {
        vbPagoCredito.setDisable(true);
        vbOtroPago.setDisable(true);
        hbPagoContado.setDisable(false);
    }

    @FXML
    private void onActionRbCredito(ActionEvent event) {
        hbPagoContado.setDisable(true);
        vbOtroPago.setDisable(true);
        vbPagoCredito.setDisable(false);
    }

    @FXML
    private void onActionRbOtroPago(ActionEvent event) {
        hbPagoContado.setDisable(true);
        vbPagoCredito.setDisable(true);
        vbOtroPago.setDisable(false);
    }

    public void setInitCompraController(FxCompraController compraController) {
        this.compraController = compraController;
    }

}
