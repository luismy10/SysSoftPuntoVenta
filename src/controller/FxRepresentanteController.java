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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.RepresentanteADO;
import model.RepresentanteTB;

public class FxRepresentanteController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<RepresentanteTB> tvList;
    @FXML
    private TableColumn<RepresentanteTB, Integer> tcId;
    @FXML
    private TableColumn<RepresentanteTB, String> tcDocument;
    @FXML
    private TableColumn<RepresentanteTB, String> tcRepresentative;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocument.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNumeroDocumento()));
        tcRepresentative.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getApellidos() + " " + cellData.getValue().getNombres()
        ));

    }

    public void fillCustomersTable(String value) {
        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<List<RepresentanteTB>> task = new Task<List<RepresentanteTB>>() {
            @Override
            public ObservableList<RepresentanteTB> call() {
                return RepresentanteADO.ListRepresentantes(value);
            }
        };
        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems((ObservableList<RepresentanteTB>) task.getValue());
        });
        exec.execute(task);
        if (!exec.isShutdown()) {
            exec.shutdown();
        }

    }

    @FXML
    private void onKeyReleasedToSearh(KeyEvent event) {
        fillCustomersTable(txtSearch.getText());
    }

    @FXML
    private void onKeyPressedToSearh(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            tvList.requestFocus();
        }
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {

    }

    public TextField getTxtSearch() {
        return txtSearch;
    }
    
    

}
