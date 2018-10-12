package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.DBUtil;
import model.PersonaADO;
import model.PersonaTB;

public class FxRepresentanteListaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<PersonaTB> tvList;
    @FXML
    private TableColumn<PersonaTB, Long> tcId;
    @FXML
    private TableColumn<PersonaTB, String> tcDocument;
    @FXML
    private TableColumn<PersonaTB, String> tcRepresentative;

    private Executor exec;
    
    private FxProveedorProcesoController controller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
            exec = Executors.newCachedThreadPool((runnable) -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });
        

        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocument.setCellValueFactory(cellData -> cellData.getValue().getNumeroDocumento());
        tcRepresentative.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getApellidoPaterno() + " " + cellData.getValue().getApellidoMaterno() + " "
                + cellData.getValue().getPrimerNombre() + " " + cellData.getValue().getSegundoNombre()
        ));
    }
    
    public void setInitProveedorProcesoController(FxProveedorProcesoController controller) {
        this.controller = controller;
    }

    public void fillCustomersTable(String value) {
        if (DBUtil.StateConnection()) {
            Task<List<PersonaTB>> task = new Task<List<PersonaTB>>() {
                @Override
                public ObservableList<PersonaTB> call() {
                    return PersonaADO.ListPersonasRepresentantes(value);
                }
            };

            task.setOnSucceeded((WorkerStateEvent e) -> {
                tvList.setItems((ObservableList<PersonaTB>) task.getValue());
            });
            exec.execute(task);
        }

    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) {

    }

    @FXML
    private void onActionToRegister(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedToSearh(KeyEvent event) {
        fillCustomersTable(txtSearch.getText());
    }

    @FXML
    private void onActionToSearch(ActionEvent event) {
        tvList.requestFocus();
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            if (event.getClickCount() == 2) {
                controller.setValueAddRepresentante(tvList.getSelectionModel().getSelectedItem().getNumeroDocumento().get());
                Tools.Dispose(window);
            }
        }
    }

    

}
