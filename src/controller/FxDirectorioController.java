package controller;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
    @FXML
    private Text lblEstado;
    @FXML
    private ImageView imState;

    private Executor exec;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcPersona.setCellValueFactory(cellData -> cellData.getValue().getIdPersona().getApellidoPaterno());

    }

    public void fillEmployeeTable() {
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

    @FXML
    private void onMouseClickedState(MouseEvent event) throws IOException {

    }

    @FXML
    private void onMouseClickedAdd(MouseEvent event) throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_PERSONA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //controller
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Persona", window.getScene().getWindow());
        stage.setResizable(false);
        stage.show();
    }

}
