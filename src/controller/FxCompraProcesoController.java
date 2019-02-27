package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.ArticuloTB;
import model.CompraADO;
import model.CompraTB;
import model.LoteTB;

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

    private FxCompraController compraController;

    private CompraTB compraTB = null;

    private TableView<ArticuloTB> tvList;

    private ObservableList<LoteTB> loteTBs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setLoadProcess(CompraTB compraTB, TableView<ArticuloTB> tvList, ObservableList<LoteTB> loteTBs) {
        this.compraTB = compraTB;
        this.tvList = tvList;
        this.loteTBs = loteTBs;
    }

    private void onEventProcess() {
        if (compraTB != null) {
            String result = CompraADO.CrudCompra(compraTB, tvList, loteTBs);
            if (result.equalsIgnoreCase("register")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Compra", "Se registró correctamente la compra.", false);
                Tools.Dispose(window);
                compraController.clearComponents();
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Compra", result, false);

            }
        }
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
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
    }

    public void setInitCompraController(FxCompraController compraController) {
        this.compraController = compraController;
    }

}
