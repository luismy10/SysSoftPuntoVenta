package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private ObservableList<LoteTB> listLote;

    private ObservableList<LoteTB> listComprasLote;

    private String idArticulo;

    private double cantidad;

    private double cantidadlote;

    private double diferencia;

    private boolean editbatch;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        listLote = FXCollections.observableArrayList();
        listComprasLote = FXCollections.observableArrayList();
        initTableView();
        cantidad = 0;
        editbatch = false;
    }

    public void initTableView() {
        tcLote.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNumeroLote()));       
        tcCaducidad.setCellValueFactory(cellData
                -> Bindings.concat(cellData.getValue().getFechaCaducidad().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)))
        );
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getExistenciaActual(), 2)
        ));
        tvList.setItems(listLote);

    }

    public void setLoadData(String... value) {
        idArticulo = value[0];
        lblClave.setText(value[1]);
        lblDescripcion.setText(value[2]);
        lblCantidad.setText(lblCantidad.getText() + " " + value[3]);
        cantidadlote = Double.parseDouble(value[3]);
        lblDiferencia.setText(lblDiferencia.getText() + " " + value[3]);
        diferencia = Double.parseDouble(value[3]);
    }

    public void setEditData(String value[], ObservableList<LoteTB> loteTBs) {
        idArticulo = value[0];
        lblClave.setText(value[1]);
        lblDescripcion.setText(value[2]);
        lblCantidad.setText(lblCantidad.getText() + " " + value[3]);
        cantidadlote = Double.parseDouble(value[3]);
        lblDiferencia.setText(lblDiferencia.getText() + " " + value[3]);
        diferencia = Double.parseDouble(value[3]);
        listComprasLote = loteTBs;
        listComprasLote.forEach((ltb) -> {
            if (ltb.getIdArticulo().equals(idArticulo)) {
                listLote.add(ltb);
            }
        });
        editbatch = true;
        calculateBatch();
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
        stage.sizeToScene();
        stage.show();       
    }

    private void openWindowEditar() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            URL url = getClass().getResource(Tools.FX_FILE_LOTECAMBIAR);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxLoteCambiarController controller = fXMLLoader.getController();
            controller.setProcesoController(this);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Lote", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
            controller.setEditBatch(new String[]{
                String.valueOf(tvList.getSelectionModel().getSelectedIndex()),               
                String.valueOf(tvList.getSelectionModel().getSelectedItem().getNumeroLote()),
                String.valueOf(tvList.getSelectionModel().getSelectedItem().getExistenciaInicial()),               
                String.valueOf(tvList.getSelectionModel().getSelectedItem().getFechaCaducidad())
            });
        }

    }

    private void toRegisterBatch() throws IOException {
        if (cantidadlote == cantidad) {
            if (editbatch) {
                for (int i = 0; i < listComprasLote.size(); i++) {
                    if (listComprasLote.get(i).getIdArticulo().equals(idArticulo)) {
                        listComprasLote.remove(i);
                        i--;
                    }
                }
            }
            listLote.forEach((loteTB) -> {
                articuloCompraController.getCompraController().getLoteTBs().add(loteTB);
            });
            articuloCompraController.setValidarlote(false);
            articuloCompraController.setCantidadInicial(cantidad);
            Tools.Dispose(window);
            articuloCompraController.addArticuloList();
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Lotes", "La cantidad y la entrada total deben ser la misma", false);
        }
    }


    @FXML
    private void onKeyPressedAgregar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowAgregar();
        }
    }

    @FXML
    private void onKeyPressedEditar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowEditar();
        }
    }

    @FXML
    private void onActionEditar(ActionEvent event) throws IOException {
        openWindowEditar();
    }

    @FXML
    private void onActionAgregar(ActionEvent event) throws IOException {
        openWindowAgregar();
    }

    @FXML
    private void onKeyPressedQuitar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
                listLote.remove(tvList.getSelectionModel().getSelectedIndex());
                calculateBatch();
            }
        }
    }

    @FXML
    private void onActionQuitar(ActionEvent event) throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            listLote.remove(tvList.getSelectionModel().getSelectedIndex());
            calculateBatch();
        }
    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            toRegisterBatch();
        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) throws IOException {
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

    public ObservableList<LoteTB> getListLote() {
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
        cantidad = entrada;
        diferencia = cantidadlote;
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setLoteController(FxArticuloCompraController articuloCompraController) {
        this.articuloCompraController = articuloCompraController;
    }

}
