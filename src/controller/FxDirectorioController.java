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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static model.DirectorioADO.*;
import model.DirectorioTB;

public class FxDirectorioController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private TableView<DirectorioTB> tvList;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableColumn<DirectorioTB, Long> tcId;
    @FXML
    private TableColumn<DirectorioTB, String> tcCodigo;
    @FXML
    private TableColumn<DirectorioTB, String> tcTipoDocumento;
    @FXML
    private TableColumn<DirectorioTB, String> tcDocumento;
    @FXML
    private TableColumn<DirectorioTB, String> tcPersona;
    @FXML
    private Label lblLoad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcCodigo.setCellValueFactory(cellData -> cellData.getValue().getPersona().getIdPersona());
        tcTipoDocumento.setCellValueFactory(cellData -> cellData.getValue().getPersona().getTipoDocumentoName());
        tcDocumento.setCellValueFactory(cellData -> cellData.getValue().getPersona().getNumeroDocumento());
        tcPersona.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getPersona().getApellidoPaterno()));

    }

    public void fillEmployeeTable(String value) {

        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<List<DirectorioTB>> task = new Task<List<DirectorioTB>>() {
            @Override
            public ObservableList<DirectorioTB> call() {
                return ListDirectory(value);
            }
        };

        task.setOnSucceeded(e -> {
            tvList.setItems((ObservableList<DirectorioTB>) task.getValue());
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

    private void onViewPerfil() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            URL url = getClass().getResource(Tools.FX_FILE_PERFIL);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxPerfilController controller = fXMLLoader.getController();
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Perfil", window.getScene().getWindow());
            stage.setResizable(false);
            stage.show();
            controller.setLoadView(tvList.getSelectionModel().getSelectedItem().getPersona().getIdPersona().get(),
                    tvList.getSelectionModel().getSelectedItem().getPersona().getApellidoPaterno());

        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Directorio", "Seleccione un item para mostrar su datos", false);
        }
    }

    @FXML
    private void onKeyPressedView(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewPerfil();
        }
    }

    @FXML
    private void onActionView(ActionEvent event) throws IOException {
        onViewPerfil();
    }

    @FXML
    private void onKeyPressedReload(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            fillEmployeeTable("");
        }
    }

    @FXML
    private void onActionReload(ActionEvent event) {
         fillEmployeeTable("");
    }

    @FXML
    private void onActionSearch(ActionEvent event) {
        fillEmployeeTable(txtSearch.getText());
    }

}
