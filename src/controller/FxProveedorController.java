package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ProveedorADO;
import model.ProveedorTB;

public class FxProveedorController implements Initializable {

    @FXML
    private VBox window;
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
    private TableColumn<ProveedorTB, String> tcContacto;
    @FXML
    private TableColumn<ProveedorTB, String> tcState;
    @FXML
    private TableColumn<ProveedorTB, String> tcFechaRegistro;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        window.setOnKeyReleased((KeyEvent event) -> {
            if (null != event.getCode()) {
                switch (event.getCode()) {
                    case F1:
                        openInsertWindow();
                        event.consume();
                        break;
                    case F2:
                        openUpdateWindow();
                        event.consume();
                        break;
                    case F5:
                        fillCustomersTable("");
                        break;
                    case DELETE:

                        event.consume();
                        break;
                    default:

                        break;
                }
            }
        });

        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocumentType.setCellValueFactory(cellData -> cellData.getValue().getTipoDocumentoName());
        tcDocument.setCellValueFactory(cellData -> cellData.getValue().getNumeroDocumento());
        tcBusinessName.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getRazonSocial().get() + "\n" + cellData.getValue().getNombreComercial().get()
        )
        );
        tcContacto.setCellValueFactory(cellData
                -> Bindings.concat(
                        !Tools.isText(cellData.getValue().getTelefono())
                        ? "TEL: " + cellData.getValue().getTelefono() + "\n" + "CEL: " + cellData.getValue().getCelular()
                        : "CEL: " + cellData.getValue().getCelular()
                )
        );
        tcState.setCellValueFactory(cellData -> cellData.getValue().getEstadoName());
        tcFechaRegistro.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaRegistro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

    }

    private void InitializationTransparentBackground() {
        Session.pane.setStyle("-fx-background-color: black");
        Session.pane.setTranslateX(0);
        Session.pane.setTranslateY(0);
        Session.pane.setPrefWidth(Session.WIDTH_WINDOW);
        Session.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.pane.setOpacity(0.7f);
        content.getChildren().add(Session.pane);
    }

    public void fillCustomersTable(String value) {
        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<List<ProveedorTB>> task = new Task<List<ProveedorTB>>() {
            @Override
            public ObservableList<ProveedorTB> call() {
                return ProveedorADO.ListProveedor(value);
            }
        };

        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems((ObservableList<ProveedorTB>) task.getValue());
            lblLoad.setVisible(false);
        });
        task.setOnFailed((WorkerStateEvent event) -> {
            lblLoad.setVisible(false);
        });

        task.setOnScheduled((WorkerStateEvent event) -> {
            lblLoad.setVisible(true);
        });
        exec.execute(task);

        if (!exec.isShutdown()) {
            exec.shutdown();
        }

    }

    private void openInsertWindow() {
        try {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_PROVEEDOREPROCESO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxProveedorProcesoController controller = fXMLLoader.getController();
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Proveedor", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();
            controller.setValueAdd();
        } catch (IOException ix) {
            System.out.println("Error en Proveedor Controller:" + ix.getLocalizedMessage());
        }
    }

    private void openUpdateWindow() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            try {
                InitializationTransparentBackground();
                URL url = getClass().getResource(Tools.FX_FILE_PROVEEDOREPROCESO);
                FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
                Parent parent = fXMLLoader.load(url.openStream());
                //Controlller here
                FxProveedorProcesoController controller = fXMLLoader.getController();
                //
                Stage stage = FxWindow.StageLoaderModal(parent, "Editr Proveedor", window.getScene().getWindow());
                stage.setResizable(false);
                stage.sizeToScene();
                stage.setOnHiding((WindowEvent WindowEvent) -> {
                    content.getChildren().remove(Session.pane);
                });
                stage.show();
                controller.setValueUpdate(tvList.getSelectionModel().getSelectedItem().getNumeroDocumento().get());
                controller.fillCustomersTable("");
            } catch (IOException ix) {
                System.out.println("Error en Proveedor Controller:" + ix.getLocalizedMessage());
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Proveedor", "Debe seleccionar un proveedor para editarlo", false);
            tvList.requestFocus();
            if (!tvList.getItems().isEmpty()) {
                tvList.getSelectionModel().select(0);
            }
        }
    }

    private void executeEventRemove() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Proveedor", "¿Esta seguro de eliminar al proveedor?", true);
            if (confirmation == 1) {
                String result = ProveedorADO.RemoveProveedor(tvList.getSelectionModel().getSelectedItem().getIdProveedor().get());
                if (result.equalsIgnoreCase("removed")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Proveedor", "Eliminado correctamente.", false);
                    fillCustomersTable("");
                } else if (result.equalsIgnoreCase("exists")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Proveedor", "No se puede eliminar el proveedor ya que está ligado a una compra.", false);
                } else {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Proveedor", result, false);
                }
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Proveedor", "Debe seleccionar un proveedor para eliminarlo", false);
            tvList.requestFocus();
            if (!tvList.getItems().isEmpty()) {
                tvList.getSelectionModel().select(0);
            }
        }
    }

    @FXML
    private void onActionSearch(ActionEvent event) {
        fillCustomersTable(txtSearch.getText());
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        openInsertWindow();
    }

    @FXML
    private void onKeyPressedAdd(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            openInsertWindow();
        }
    }

    @FXML
    private void onActionEdit(ActionEvent event) throws IOException {
        openUpdateWindow();
    }

    @FXML
    private void onKeyPressedEdit(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            openUpdateWindow();
        }
    }

    @FXML
    private void onKeyPressedRemove(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            executeEventRemove();
        }
    }

    @FXML
    private void onActionRemove(ActionEvent event) {
        executeEventRemove();
    }

    @FXML
    private void onKeyPressedList(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            openUpdateWindow();
        }
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
        if (event.getClickCount() == 2) {
            openUpdateWindow();
        }
    }

    @FXML
    private void onActionReload(ActionEvent event) {
        fillCustomersTable("");
    }

    @FXML
    private void onKeyPressedReload(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            fillCustomersTable("");
        }
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
