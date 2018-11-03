package controller;

import java.io.IOException;
import java.net.URL;
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
import model.DBUtil;
import model.EmpleadoADO;
import model.EmpleadoTB;

public class FxEmpleadosController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblLoad;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<EmpleadoTB> tvList;
    @FXML
    private TableColumn<EmpleadoTB, Integer> tcId;
    @FXML
    private TableColumn<EmpleadoTB, String> tcDocument;
    @FXML
    private TableColumn<EmpleadoTB, String> tcCompleteData;
    @FXML
    private TableColumn<EmpleadoTB, String> tcContact;
    @FXML
    private TableColumn<EmpleadoTB, String> tcMarketStall;
    @FXML
    private TableColumn<EmpleadoTB, String> tcState;

    private AnchorPane content;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocument.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNumeroDocumento()));
        tcCompleteData.setCellValueFactory(cellData
                -> Bindings.concat(
                        cellData.getValue().getApellidos() + " ",
                        cellData.getValue().getNombres()
                )
        );
        tcContact.setCellValueFactory(cellData
                -> Bindings.concat(
                        !Tools.isText(cellData.getValue().getTelefono())
                        ? "TEL: " + cellData.getValue().getTelefono() + "\n" + "CEL: " + cellData.getValue().getCelular()
                        : "CEL: " + cellData.getValue().getCelular()
                )
        );
        tcMarketStall.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getPuestoName()));
        tcState.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getEstadoName()));

    }

    public void fillEmpleadosTable(String value) {
        if (DBUtil.StateConnection()) {

            ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });

            Task<List<EmpleadoTB>> task = new Task<List<EmpleadoTB>>() {
                @Override
                public ObservableList<EmpleadoTB> call() {
                    return EmpleadoADO.ListEmpleados(value);
                }
            };

            task.setOnSucceeded((WorkerStateEvent e) -> {
                tvList.setItems((ObservableList<EmpleadoTB>) task.getValue());

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

    private void openWindowEmpleadosAdd() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_EMPLEADOSPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxEmpleadosProcesoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Empleado", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(SysSoft.pane);
        });
        stage.show();
    }

    private void openWindowEmpleadosEdit(String value) throws IOException {

        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_EMPLEADOSPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxEmpleadosProcesoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Editar Empleado", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(SysSoft.pane);
        });
        stage.show();
        controller.setEditEmpleado(value);
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        openWindowEmpleadosAdd();
    }

    @FXML
    private void onActionEdit(ActionEvent event) throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            openWindowEmpleadosEdit(tvList.getSelectionModel().getSelectedItem().getIdEmpleado());
        }

    }

    @FXML
    private void onActionReload(ActionEvent event) {
        fillEmpleadosTable("");
    }

    @FXML
    private void onKeyPressedSearch(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            txtSearch.requestFocus();
        }
    }

    @FXML
    private void onKeyReleasedSearch(KeyEvent event) {
        fillEmpleadosTable(txtSearch.getText().trim());
    }

    @FXML
    private void onKeyPressedList(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
                openWindowEmpleadosEdit(tvList.getSelectionModel().getSelectedItem().getIdEmpleado());
            }
        }
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            if (event.getClickCount() == 2) {
                openWindowEmpleadosEdit(tvList.getSelectionModel().getSelectedItem().getIdEmpleado());
            }
        }
    }

    void setContent(AnchorPane content) {
        this.content = content;
    }

}
