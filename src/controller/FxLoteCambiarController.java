package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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

    @FXML
    private void onActionAgregar(ActionEvent event) {
        LoteTB loteTB = new LoteTB();
        loteTB.setNumeroLote("00001");
        LocalDate localDate = LocalDate.now();
        loteTB.setFechaFabricacion(localDate);
        procesoController.getTvList().getItems().add(loteTB);
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

    public void setProcesoController(FxLoteProcesoController procesoController) {
        this.procesoController = procesoController;
    }

}
