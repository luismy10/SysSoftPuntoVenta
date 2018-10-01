package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.LoteTB;

public class FxLoteProcesoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TableView<LoteTB> tvList;
    @FXML
    private TableColumn<LoteTB, String> tcLote;
    @FXML
    private TableColumn<LoteTB, String> tcFabricacion;
    @FXML
    private TableColumn<LoteTB, LocalDate> tcCaducidad;
    @FXML
    private TableColumn<LoteTB, String> tcCantidad;
    @FXML
    private Text lblCantidad;
    @FXML
    private Text lblDiferencia;
    @FXML
    private Text lblTotal;

    private FxArticuloCompraController articuloCompraController;

    private double cantidad;

    private double diferencia;

    private double total;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        initTableView();
    }

    public void initTableView() {
        tcLote.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNumeroLote()));
        tcFabricacion.setCellValueFactory(cellData ->
             Bindings.concat(cellData.getValue().getFechaFabricacion().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)))
        );
        tcCaducidad.setCellValueFactory(cellData -> cellData.getValue().getFechaCaducidad());
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getExistenciaActual(), 2)
        ));
    }

    public void setLoadData(String... value) {
        lblCantidad.setText(lblCantidad.getText() + " " + value[0]);
        lblDiferencia.setText(lblDiferencia.getText() + " " + value[0]);
    }

    @FXML
    private void onKeyPressedGenerar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionGenerar(ActionEvent event) {

    }

    private void openWindowAgregar() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_LOTECAMBIAR);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxLoteCambiarController controller = fXMLLoader.getController();
        controller.setProcesoController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Lote", window.getScene().getWindow());
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onKeyPressedAgregar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowAgregar();
        }
    }

    @FXML
    private void onActionAgregar(ActionEvent event) throws IOException {
        openWindowAgregar();
    }

    @FXML
    private void onKeyPressedQuitar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionQuitar(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedToCancel(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionToCancel(ActionEvent event) {
        Tools.Dispose(window);
    }

    public void setLoteController(FxArticuloCompraController articuloCompraController) {
        this.articuloCompraController = articuloCompraController;
    }

    public TableView<LoteTB> getTvList() {
        return tvList;
    }

    
    
}
