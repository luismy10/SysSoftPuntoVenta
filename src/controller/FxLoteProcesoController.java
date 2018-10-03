package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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
    private Text lblClave;
    @FXML
    private Text lblDescripcion;
    @FXML
    private TableView<LoteTB> tvList;
    @FXML
    private TableColumn<LoteTB, String> tcLote;
    @FXML
    private TableColumn<LoteTB, String> tcFabricacion;
    @FXML
    private TableColumn<LoteTB, String> tcCaducidad;
    @FXML
    private TableColumn<LoteTB, String> tcCantidad;
    @FXML
    private Text lblCantidad;
    @FXML
    private Text lblDiferencia;
    @FXML
    private Text lblTotal;

    private FxArticuloCompraController articuloCompraController;

    private ArrayList<LoteTB> listLote;

    private double cantidad;

    private double cantidadlote;

    private double diferencia;

    private double total;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        initTableView();
        listLote = new ArrayList<>();
    }

    public void initTableView() {
        tcLote.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNumeroLote()));
        tcFabricacion.setCellValueFactory(cellData
                -> Bindings.concat(cellData.getValue().getFechaFabricacion().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)))
        );
        tcCaducidad.setCellValueFactory(cellData
                -> Bindings.concat(cellData.getValue().getFechaCaducidad().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)))
        );
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getExistenciaActual(), 2)
        ));
    }

    public void setLoadData(String... value) {
        lblClave.setText(value[0]);
        lblDescripcion.setText(value[1]);
        lblCantidad.setText(lblCantidad.getText() + " " + value[2]);
        cantidadlote = Double.parseDouble(value[2]);
        lblDiferencia.setText(lblDiferencia.getText() + " " + value[2]);
        diferencia = Double.parseDouble(value[2]);
    }

    private void openWindowAgregar(boolean generate) throws IOException {
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
        if (generate) {
            controller.generateBatch(lblDescripcion.getText());
        }
    }

    private void toRegisterBatch() {
        if (cantidadlote == cantidad) {
            listLote.forEach((loteTB) -> {
                articuloCompraController.getComprasController().getLoteTBs().add(loteTB);
            });
            articuloCompraController.setValidarlote(false);
            Tools.Dispose(window);
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Lotes", "La cantidad y la entrada total deben ser la misma", false);
        }
    }

    @FXML
    private void onKeyPressedGenerar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowAgregar(true);
        }
    }

    @FXML
    private void onActionGenerar(ActionEvent event) throws IOException {
        openWindowAgregar(true);
    }

    @FXML
    private void onKeyPressedAgregar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowAgregar(false);
        }
    }

    @FXML
    private void onActionAgregar(ActionEvent event) throws IOException {
        openWindowAgregar(false);
    }

    @FXML
    private void onKeyPressedQuitar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionQuitar(ActionEvent event) throws IOException {

    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            toRegisterBatch();
        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) {
        toRegisterBatch();
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

    public TableView<LoteTB> getTvList() {
        return tvList;
    }

    public ArrayList<LoteTB> getListLote() {
        return listLote;
    }

    public void calculateBatch() {
        double entrada = 0;
        for (int i = 0; i < tvList.getItems().size(); i++) {
            entrada += tvList.getItems().get(i).getExistenciaActual();
        }
        diferencia -= entrada;
        lblTotal.setText("Entrada total " + entrada);
        lblDiferencia.setText("Diferencia " + diferencia);
        cantidad += entrada;
        diferencia = cantidadlote;
    }

    public void setLoteController(FxArticuloCompraController articuloCompraController) {
        this.articuloCompraController = articuloCompraController;
    }

}