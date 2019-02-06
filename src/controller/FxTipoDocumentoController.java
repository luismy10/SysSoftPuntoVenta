package controller;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.TipoDocumentoADO;
import model.TipoDocumentoTB;

public class FxTipoDocumentoController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblLoad;
    @FXML
    private TableView<TipoDocumentoTB> tvList;
    @FXML
    private TableColumn<TipoDocumentoTB, String> tcMoneda;
    @FXML
    private TableColumn<TipoDocumentoTB, ImageView> tcPredeterminado;

    private boolean stateRequest;

    private boolean stateUpdate;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcMoneda.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNombre()));
        tcPredeterminado.setCellValueFactory(new PropertyValueFactory<>("imagePredeterminado"));
        stateRequest = false;
        stateUpdate = false;
    }

    public void fillTabletTipoDocumento() {
        ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<ObservableList<TipoDocumentoTB>> task = new Task<ObservableList<TipoDocumentoTB>>() {
            @Override
            public ObservableList<TipoDocumentoTB> call() {
                return TipoDocumentoADO.ListTipoDocumento();
            }
        };
        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems(task.getValue());
            lblLoad.setVisible(false);
            stateRequest = true;
            if (stateUpdate) {
                List<TipoDocumentoTB> list = TipoDocumentoADO.GetDocumentoCombBox();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isPredeterminado() == true) {
                        Session.DEFAULT_COMPROBANTE = i;
                        break;
                    }
                }
                stateUpdate=false;
            }

        });
        task.setOnFailed((WorkerStateEvent event) -> {
            lblLoad.setVisible(false);
            stateRequest = false;
        });

        task.setOnScheduled((WorkerStateEvent event) -> {
            lblLoad.setVisible(true);
            stateRequest = false;
        });
        exec.execute(task);
        if (!exec.isShutdown()) {
            exec.shutdown();
        }

    }

    private void onEventPredeterminado() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            String result = TipoDocumentoADO.ChangeDefaultState(true, tvList.getSelectionModel().getSelectedItem().getIdTipoDocumento());
            if (result.equalsIgnoreCase("updated")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Tipo de comprobante", "Se cambio el estado correctamente.", false);
                stateUpdate = true;
                fillTabletTipoDocumento();
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Tipo de comprobante", "Error: " + result, false);
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Tipo de comprobante", "Seleccione un elemento de la lista.", false);
        }
    }

    @FXML
    private void onKeyPressedEdit(KeyEvent event) {

    }

    @FXML
    private void onActionEdit(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedPredetermined(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onEventPredeterminado();
        }
    }

    @FXML
    private void onActionPredetermined(ActionEvent event) {
        onEventPredeterminado();
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {

    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
