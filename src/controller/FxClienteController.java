package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.DBUtil;
import model.PersonaADO;
import model.PersonaTB;

public class FxClienteController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<PersonaTB> tvList;
    @FXML
    private TableColumn<PersonaTB, Long> tcId;
    @FXML
    private TableColumn<PersonaTB, String> tcDocumento;
    @FXML
    private TableColumn<PersonaTB, String> tcPersona;
    @FXML
    private TableColumn<PersonaTB, String> tcEstado;
    @FXML
    private TableColumn<PersonaTB, LocalDate> tcFechaRegistro;

    private boolean stateconnect;

    private Executor exec;

    private boolean proccess;
    @FXML
    private Label lblLoad;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        proccess = false;
        stateconnect = DBUtil.StateConnection();
        if (stateconnect) {

            exec = Executors.newCachedThreadPool((runnable) -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });
        }

        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocumento.setCellValueFactory(cellData -> cellData.getValue().getNumeroDocumento());
        tcPersona.setCellValueFactory(cellData -> cellData.getValue().getApellidoPaterno());
        tcEstado.setCellValueFactory(cellData -> cellData.getValue().getEstadoName());
        tcFechaRegistro.setCellValueFactory(cellData -> cellData.getValue().fechaRegistroProperty());

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

    public void fillCustomersTable(String value) {
        if (DBUtil.StateConnection()) {
            Task<List<PersonaTB>> task = new Task<List<PersonaTB>>() {
                @Override
                public ObservableList<PersonaTB> call() {
                    return PersonaADO.ListPersonas(value);
                }
            };

            task.setOnSucceeded((WorkerStateEvent e) -> {
                tvList.setItems((ObservableList<PersonaTB>) task.getValue());
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
        if (proccess) {
            fillCustomersTable(txtSearch.getText());
        }
    }


    void setContent(AnchorPane content) {
        this.content = content;
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_PERSONA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxPersonaController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Cliente", window.getScene().getWindow());
        stage.setResizable(false);
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(SysSoft.pane);
        });
        stage.show();
        controller.setValueAdd();
    }

    @FXML
    private void onActionEdit(ActionEvent event) throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_PERSONA);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxPersonaController controller = fXMLLoader.getController();
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Editar cliente", window.getScene().getWindow());
            stage.setResizable(false);
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(SysSoft.pane);
            });
            stage.show();
            controller.setValueUpdate(tvList.getSelectionModel().getSelectedItem().getNumeroDocumento().get());
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Clientes", "Seleccione un cliente para actualizar.", false);
            tvList.requestFocus();
        }
    }

    @FXML
    private void onActionReload(ActionEvent event) {
        if (proccess) {
            fillCustomersTable("");
        }
    }

}
