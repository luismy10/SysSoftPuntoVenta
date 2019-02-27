
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class FxCompraProcesoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Text lblComprobante;
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
    
    private FxComprasController comprasController;

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void onKeyTypedEfectivo(KeyEvent event) {
    }

    @FXML
    private void onKeyPressedProveedor(KeyEvent event) {
    }

    @FXML
    private void onActionProveedor(ActionEvent event) {
    }

    @FXML
    private void onKeyTypedCredito(KeyEvent event) {
    }

    @FXML
    private void onKeyTypedDiasCredito(KeyEvent event) {
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
    }

    public void setInitComprasController(FxComprasController comprasController) {
        this.comprasController=comprasController;
    }
    
}
