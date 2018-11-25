package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.ArticuloTB;
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
    
    private TableView<ArticuloTB> tvList;

    private VentaTB ventaTB;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        if (Tools.isNumeric(txtEfectivo.getText())) {
            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Venta", "¿Esta seguro de continuar?", true);
            if (confirmation == 1) {
                String result = VentaADO.CrudVenta(ventaTB,tvList);
                switch (result) {
                    case "register":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Venta", "Se realiazo la venta con éxito", false);
                        ventaController.resetVenta();
                        break;
                    default:
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Venta", result, false);
                        break;
                }
            }

        }

    }

    public void setInitComponents(VentaTB ventaTB,TableView<ArticuloTB> tvList) {
        this.ventaTB = ventaTB;
        this.tvList=tvList;
        lblTotal.setText("S/ " + Tools.roundingValue(ventaTB.getTotal(), 2));
        txtEfectivo.requestFocus();
    }

    @FXML
    private void onKeyReleasedEfectivo(KeyEvent event) {
        if (Tools.isNumeric(txtEfectivo.getText())) {
            double vuelto = Double.parseDouble(txtEfectivo.getText()) - ventaTB.getTotal();
            lblVuelto.setText(Tools.roundingValue(vuelto, 2));
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

    public void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

}
