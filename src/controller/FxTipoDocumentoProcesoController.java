package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.TipoDocumentoADO;
import model.TipoDocumentoTB;

public class FxTipoDocumentoProcesoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtValor;
    @FXML
    private Button btnGuardar;
    
    private int idTipoDocumento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
    }

    public void initUpdate(int codigo) {
        idTipoDocumento = codigo;
        btnGuardar.setText("Actualizar");
        btnGuardar.getStyleClass().add("buttonLightWarning");
    }

    private void saveTipoImpuesto() {
        if (txtNombre.getText().trim().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Tipo de documento", "Ingrese el nombre del comprobante.", false);
            txtNombre.requestFocus();
        } else if (txtValor.getText().trim().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Tipo de documento", "Ingrese un nombre para la impresión", false);
            txtValor.requestFocus();
        } else {
            TipoDocumentoTB documentoTB = new TipoDocumentoTB();
            documentoTB.setIdTipoDocumento(idTipoDocumento);
            documentoTB.setNombre(txtNombre.getText().toUpperCase().trim());
            documentoTB.setPredeterminado(false);
            documentoTB.setNombreDocumento(txtValor.getText().toUpperCase().trim());
            
            String result = TipoDocumentoADO.CrudTipoDocumento(documentoTB);
            if (result.equalsIgnoreCase("updated")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Tipo de documento", "Se actualizado correctamente", false);
                Tools.Dispose(window);
            } else if (result.equalsIgnoreCase("duplicate")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Tipo de documento", "Ya existe comprobante con el mismo nombre", false);
                txtNombre.requestFocus();
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Tipo de documento", result, false);
            }
        }
    }

    @FXML
    private void onActionGuardar(ActionEvent event) {
        saveTipoImpuesto();
    }

    @FXML
    private void onKeyPressedGuardar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            saveTipoImpuesto();
        }
    }

    @FXML
    private void onKeyCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Tools.Dispose(window);
    }

}
