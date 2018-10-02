package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.LoteADO;
import model.LoteTB;

public class FxLoteCambiarController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtArticulo;
    @FXML
    private TextField txtLote;
    @FXML
    private TextField txtCantidad;
    @FXML
    private DatePicker dtFabricacion;
    @FXML
    private DatePicker dtCaducidad;

    private FxLoteProcesoController procesoController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
    }

    public void generateBatch(String descripcion) {
        txtArticulo.setText(descripcion);
        txtLote.setDisable(true);
        txtLote.setText(LoteADO.GenerateBatch());
    }

    @FXML
    private void onActionAgregar(ActionEvent event) {

        if (txtLote.getText().trim().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Lotes", "Ingrese un lote ejemplo(LT0001,BT001,109029), por favor", false);
            txtLote.requestFocus();
        } else {
            if (!Tools.isNumeric(txtCantidad.getText())) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Lotes", "Ingrese la cantidad de unidades del lote, por favor", false);
                txtCantidad.requestFocus();
            } else {
                if (Double.parseDouble(txtCantidad.getText()) <1) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Lotes", "La cantidad debe ser mayor a 0, por favor", false);
                    txtCantidad.requestFocus();
                } else {
                    if (dtFabricacion.getValue() == null) {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Lotes", "Ingrese la fecha de fabricación, por favor", false);
                        dtFabricacion.requestFocus();
                    } else {
                        if (dtCaducidad.getValue() == null) {
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Lotes", "Ingrese la fecha de caducidad, por favor", false);
                            dtCaducidad.requestFocus();
                        } else {
                            LoteTB loteTB = new LoteTB();
                            loteTB.setNumeroLote(txtLote.getText());
                            loteTB.setExistenciaActual(Double.parseDouble(txtCantidad.getText()));
                            loteTB.setExistenciaInicial(Double.parseDouble(txtCantidad.getText()));
                            loteTB.setFechaFabricacion(dtFabricacion.getValue());
                            loteTB.setFechaCaducidad(dtCaducidad.getValue());
                            procesoController.getTvList().getItems().add(loteTB);
                            procesoController.getListLote()
                                    .add(loteTB);
                            procesoController.calculateBatch();
                            Tools.Dispose(window);
                        }
                    }
                }
            }
        }

    }

    @FXML
    private void onActionCerrar(ActionEvent event) {
        Tools.Dispose(window);
    }

    @FXML
    private void onKeyPressedAgregar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onKeyPressedCerrar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onKeyTypedCantidad(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtCantidad.getText().contains(".") || c == '-' && txtCantidad.getText().contains("-")) {
            event.consume();
        }
    }

    public void setProcesoController(FxLoteProcesoController procesoController) {
        this.procesoController = procesoController;
    }

}
