package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.DBUtil;
import model.ProveedorADO;
import model.ProveedorTB;

public class FxProveedoresController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ImageView imState;
    @FXML
    private Text lblEstado;
    @FXML
    private TextField txtSearch;
    @FXML
    private Label lblLoad;
    @FXML
    private TableView<ProveedorTB> tvList;
    @FXML
    private TableColumn<ProveedorTB, Integer> tcId;
    @FXML
    private TableColumn<ProveedorTB, String> tcDocumentType;
    @FXML
    private TableColumn<ProveedorTB, String> tcDocument;
    @FXML
    private TableColumn<ProveedorTB, String> tcBusinessName;
    @FXML
    private TableColumn<ProveedorTB, String> tcState;
    @FXML
    private TableColumn<ProveedorTB, LocalDate> tcFechaRegistro;

    private boolean stateconnect;

    private Executor exec;

    private boolean proccess;

    private Pane pane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        pane = new Pane();
        window.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Session.WIDTH_WINDOW = (double) newValue;
            pane.setPrefWidth(Session.WIDTH_WINDOW);
        });
        window.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Session.HEIGHT_WINDOW = (double) newValue;
            pane.setPrefHeight(Session.HEIGHT_WINDOW);
        });
        proccess = false;
        stateconnect = DBUtil.StateConnection();
        lblEstado.setText(stateconnect == true ? "Conectado" : "Desconectado");
        if (stateconnect) {
            imState.setImage(new Image("/view/connected.png"));
            exec = Executors.newCachedThreadPool((runnable) -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });
        } else {
            imState.setImage(new Image("/view/disconnected.png"));
        }
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocumentType.setCellValueFactory(cellData -> cellData.getValue().getTipoDocumentoName());
        tcDocument.setCellValueFactory(cellData -> cellData.getValue().getNumeroDocumento());
        tcBusinessName.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getRazonSocial().get() + "\n" + cellData.getValue().getNombreComercial().get()
        )
        );
        tcState.setCellValueFactory(cellData -> cellData.getValue().getEstadoName());
        tcFechaRegistro.setCellValueFactory(cellData -> cellData.getValue().fechaRegistroProperty());

    }

    private void InitializationTransparentBackground() {
        pane.setStyle("-fx-background-color: black");
        pane.setTranslateX(0);
        pane.setTranslateY(0);
        pane.setPrefWidth(Session.WIDTH_WINDOW);
        pane.setPrefHeight(Session.HEIGHT_WINDOW);
        pane.setOpacity(0.7f);
        window.getChildren().add(pane);
    }

    public void fillCustomersTable(String value) {
        if (DBUtil.StateConnection()) {
            Task<List<ProveedorTB>> task = new Task<List<ProveedorTB>>() {
                @Override
                public ObservableList<ProveedorTB> call() {
                    return ProveedorADO.ListProveedor(value);
                }
            };

            task.setOnSucceeded((WorkerStateEvent e) -> {
                tvList.setItems((ObservableList<ProveedorTB>) task.getValue());
                proccess = true;
                lblLoad.setVisible(false);
            });
            task.setOnFailed((WorkerStateEvent event) -> {
                proccess = true;
                lblLoad.setVisible(false);
            });

            task.setOnScheduled((WorkerStateEvent event) -> {
                lblLoad.setVisible(true);
            });
            exec.execute(task);
        }

    }

    @FXML
    private void onActionSearch(ActionEvent event) {
        fillCustomersTable(txtSearch.getText());
    }

    @FXML
    private void onMouseClickedAdd(MouseEvent event) throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_PROVEEDOREPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxProveedorProcesoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Proveedor", window.getScene().getWindow());
        stage.setResizable(false);
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            window.getChildren().remove(pane);
        });
        stage.show();
        controller.setValueAdd();

    }

    @FXML
    private void onMouseClickedEdit(MouseEvent event) throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_PROVEEDOREPROCESO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxProveedorProcesoController controller = fXMLLoader.getController();
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Editr Proveedor", window.getScene().getWindow());
            stage.setResizable(false);
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                window.getChildren().remove(pane);
            });
            stage.show();
            controller.setValueUpdate(tvList.getSelectionModel().getSelectedItem().getNumeroDocumento().get());
            controller.fillCustomersTable("");
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Proveedor", "Debe seleccionar un proveedor para editarlo", false);
            tvList.requestFocus();
        }

    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {

    }

    @FXML
    private void onMouseClickedReload(MouseEvent event) {
        fillCustomersTable("");
    }

}
