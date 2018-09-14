package controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.DBUtil;
import model.ProveedorADO;
import model.ProveedorTB;

public class FxProveedoresController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ImageView imState;
    @FXML
    private Text lblEstado;
    @FXML
    private TextField txtSearch;
    @FXML
    private Label lblLoad;
    @FXML
    private TableView<ProveedorTB> tvList;
    @FXML
    private TableColumn<ProveedorTB, Long> tcId;
    @FXML
    private TableColumn<ProveedorTB, String> tcDocumentType;
    @FXML
    private TableColumn<ProveedorTB, String> tcDocument;
    @FXML
    private TableColumn<ProveedorTB, String> tcBusinessName;
    @FXML
    private TableColumn<?, ?> tcState;

    private boolean stateconnect;

    private Executor exec;

    private boolean proccess;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        proccess = false;
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
        tcId.setCellValueFactory(cellData -> cellData.getValue().getIdProveedor().asObject());
        tcDocumentType.setCellValueFactory(cellData -> cellData.getValue().getTipoDocumentoName());
        tcDocument.setCellValueFactory(cellData -> cellData.getValue().getNumeroDocumento());
        tcBusinessName.setCellValueFactory(cellData -> 
            cellData.getValue().getRazonSocial().concat(" / "+"\n"+cellData.getValue().getNombreComercial().get())
        );
    }

    public void fillCustomersTable(String value) {
        if (DBUtil.StateConnection()) {
            Task<List<ProveedorTB>> task = new Task<List<ProveedorTB>>() {
                @Override
                public ObservableList<ProveedorTB> call() {
                    return ProveedorADO.ListProveedor(value);
                }
            };

            task.setOnSucceeded((WorkerStateEvent e) -> {
                tvList.setItems((ObservableList<ProveedorTB>) task.getValue());
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
        fillCustomersTable(txtSearch.getText());
    }

    @FXML
    private void onMouseClickedAdd(MouseEvent event) throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_PROVEEDOREPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxProveedorProcesoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Proveedor", window.getScene().getWindow());
        stage.setResizable(false);
        stage.show();
        controller.setValueAdd();

    }

    @FXML
    private void onMouseClickedEdit(MouseEvent event) throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_PROVEEDOREPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxProveedorProcesoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Editr Proveedor", window.getScene().getWindow());
        stage.setResizable(false);
        stage.show();
        controller.setValueUpdate();
    }

}
