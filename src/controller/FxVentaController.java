package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ArticuloTB;
import model.DetalleADO;
import model.DetalleTB;

public class FxVentaController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private ComboBox<DetalleTB> cbComprobante;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, String> tcArticulo;
    @FXML
    private TableColumn<ArticuloTB, String> tcCantidad;
    @FXML
    private TableColumn<ArticuloTB, String> tcPrecio;
    @FXML
    private TableColumn<ArticuloTB, String> tcDescuento;
    @FXML
    private TableColumn<ArticuloTB, String> tcImporte;
    @FXML
    private Text lblSubTotal;
    @FXML
    private Text lblDescuento;
    @FXML
    private Text lblGravada;
    @FXML
    private Text lblIgv;
    @FXML
    private Text lblTotal;
    @FXML
    private TextField txtArticulo;
    @FXML
    private Text lblSerie;
    @FXML
    private Text lblNumeracion;
    @FXML
    private TextField txtCliente;
    @FXML
    private Text lblTotalPagar;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initTable();
    }
    
    public void loadWindow(){
        DetalleADO.GetDetailIdName("2", "0009", "").forEach(e -> {
            cbComprobante.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        cbComprobante.getSelectionModel().select(0);
        txtCliente.setText(Session.DATOSCLIENTE);
        txtArticulo.requestFocus();
    }

    private void initTable() {
        tcArticulo.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClave().get() + "\n" + cellData.getValue().getNombre().get()
        ));
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getCantidad().get(), 2)));
        tcPrecio.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getPrecioCompra(), 2)));
        tcDescuento.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getDescuento().get(), 2)));
        tcImporte.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getImporte().get(), 2)));
    }

    private void InitializationTransparentBackground() {
        SysSoft.pane.setStyle("-fx-background-color: black");
        SysSoft.pane.setTranslateX(0);
        SysSoft.pane.setTranslateY(0);
        SysSoft.pane.setPrefWidth(Session.WIDTH_WINDOW);
        SysSoft.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        SysSoft.pane.setOpacity(0.7f);
        content.getChildren().add(SysSoft.pane);
    }

    private void openWindowArticulos() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_ARTICULOLISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxArticuloListaController controller = fXMLLoader.getController();
        controller.setInitVentasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Seleccione un Artículo", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(SysSoft.pane);
        });
        stage.show();
        controller.fillProvidersTable("");
    }

    private void openWindowVentaProceso() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_VENTAPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxVentaProcesoController controller = fXMLLoader.getController();
        controller.setInitVentasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Completar la venta", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(SysSoft.pane);
        });
        stage.show();
    }
    
    private void openWindowCliente() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_CLIENTELISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxClienteListaController controller = fXMLLoader.getController();
        controller.setInitVentasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Elija un cliente", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(SysSoft.pane);
        });
        stage.show();
        controller.fillCustomersTable("");
    }

    @FXML
    private void onKeyPressedRegister(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowVentaProceso();
        }
    }

    @FXML
    private void onActionRegister(ActionEvent event) throws IOException {
        openWindowVentaProceso();
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        openWindowArticulos();
    }

    @FXML
    private void onActionEdit(ActionEvent event) {
        
    }

    @FXML
    private void onActionRemover(ActionEvent event) {
        
    }

    @FXML
    private void onActionCancel(ActionEvent event) {
        
    }

    @FXML
    private void onActionVendedor(ActionEvent event) {
        
    }

    @FXML
    private void onActionCliente(ActionEvent event) throws IOException {
        openWindowCliente();
    }

    void setContent(AnchorPane content) {
        this.content = content;
    }

}
