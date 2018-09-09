package controller;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.DBUtil;
import static model.DirectorioADO.*;
import model.DirectorioTB;

public class FxDirectorioController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TableView<DirectorioTB> tvList;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableColumn<DirectorioTB, Long> tcId;
    @FXML
    private TableColumn<DirectorioTB, String> tcDocumento;
    @FXML
    private TableColumn<DirectorioTB, String> tcPersona;
    @FXML
    private TableColumn<DirectorioTB, Date> tcFechaRegistro;

    private boolean stateconnect;

    private Executor exec;

    @FXML
    private ImageView imState;
    @FXML
    private Text lblEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        tcPersona.setCellValueFactory(cellData -> cellData.getValue().getPersona().getApellidoPaterno());

    }

    public void fillEmployeeTable() {
        if (DBUtil.StateConnection()) {
            Task<List<DirectorioTB>> task = new Task<List<DirectorioTB>>() {
                @Override
                public ObservableList<DirectorioTB> call() {
                    try {
                        return ListPrincipal();
                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getLocalizedMessage());
                    }
                    return null;
                }
            };

            task.setOnSucceeded(e -> tvList.setItems((ObservableList<DirectorioTB>) task.getValue()));
            exec.execute(task);
        }

    }

    @FXML
    private void onMouseClickedReload(MouseEvent event) {
        
    }

}
