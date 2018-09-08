package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.PersonaADO;
import model.PersonaTB;

public class FxClienteController implements Initializable {

    @FXML
    private AnchorPane window;
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
    private TableColumn<PersonaTB, LocalDate> tcFechaRegistro;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocumento.setCellValueFactory(cellData -> cellData.getValue().getNumeroDocumento());
        tcPersona.setCellValueFactory(cellData -> cellData.getValue().getApellidoPaterno());
        tcFechaRegistro.setCellValueFactory(cellData -> cellData.getValue().fechaRegistroProperty());
        tvList.setItems(PersonaADO.ListPersonas(""));
    }

    @FXML
    private void onMouseClickedAdd(MouseEvent event) throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_PERSONA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Cliente", window.getScene().getWindow());
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onActionSearch(ActionEvent event) {
        tvList.requestFocus();
    }

    @FXML
    private void onKeyReleasedSearch(KeyEvent event) {
        tvList.setItems(PersonaADO.ListPersonas(txtSearch.getText()));
    }

    @FXML
    private void onMouseClickedEdit(MouseEvent event) throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            URL url = getClass().getResource(Tools.FX_FILE_PERSONA);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxPersonaController controller = fXMLLoader.getController();
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Editar cliente", window.getScene().getWindow());
            stage.setResizable(false);
            stage.show();
            controller.setValueUpdate(tvList.getSelectionModel().getSelectedItem().getNumeroDocumento().get());
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Clientes", "Seleccione un cliente para actualizar.", false);
            tvList.requestFocus();
        }

    }

}
