package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.HistorialADO;
import model.HistorialTB;

public class FxArticuloHistorialController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblLoad;
    @FXML
    private TableView<HistorialTB> tvListaHistorial;
    @FXML
    private TableColumn<HistorialTB, Integer> tcId;
    @FXML
    private TableColumn<HistorialTB, String> tcOperacion;
    @FXML
    private TableColumn<HistorialTB, String> tcFecha;
    @FXML
    private TableColumn<HistorialTB, String> tcEntrada;
    @FXML
    private TableColumn<HistorialTB, String> tcSalida;
    @FXML
    private TableColumn<HistorialTB, String> tcSaldo;
    @FXML
    private TableColumn<HistorialTB, String> tcUsuario;
    @FXML
    private TextField txtClave;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtExistencias;
    @FXML
    private TextField txtPrecio;
    
    private AnchorPane content;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tcId.setCellValueFactory(cellData -> cellData.getValue().getIdHistorial().asObject());
        tcFecha.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaRegistro().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))));
        tcOperacion.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getTipoOperacion()));
        tcEntrada.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getEntrada()));
        tcSalida.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getSalida()));
        tcSaldo.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getSaldo()));
        tcUsuario.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getUsuarioRegistro()));

    }

    private void fillHistorialTable(String value) {
        ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<ObservableList<HistorialTB>> task = new Task<ObservableList<HistorialTB>>() {
            @Override
            public ObservableList<HistorialTB> call() {
                return HistorialADO.ListArticulos(value);
            }
        };

        task.setOnSucceeded((WorkerStateEvent e) -> {
            this.tvListaHistorial.setItems((ObservableList<HistorialTB>) task.getValue());
            this.lblLoad.setVisible(false);
        });
        task.setOnFailed((WorkerStateEvent event) -> {
            this.lblLoad.setVisible(false);
        });

        task.setOnScheduled((WorkerStateEvent event) -> {
            this.lblLoad.setVisible(true);
            //this.tvListaHistorial.itemsProperty().bind(task.valueProperty());
        });
        exec.execute(task);

        if (!exec.isShutdown()) {
            exec.shutdown();
        }
    }

    private void InitializationTransparentBackground() {
        Session.pane.setStyle("-fx-background-color: black");
        Session.pane.setTranslateX(0);
        Session.pane.setTranslateY(0);
        Session.pane.setPrefWidth(Session.WIDTH_WINDOW);
        Session.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.pane.setOpacity(0.7f);
        content.getChildren().add(Session.pane);
    }

    private void openWindowArticulos() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_ARTICULOLISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxArticuloListaController controller = fXMLLoader.getController();
        controller.setInitArticuloHistorialController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Seleccione un Artículo", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
        controller.fillProvidersTable("");
    }

    public void getElemetsArticulo(String value[]) {
        fillHistorialTable(value[0] == null ? "" : value[0]);
        txtClave.setText(value[1]);
        txtDescripcion.setText(value[2]);
        txtExistencias.setText(value[3]);
        txtPrecio.setText(value[4]);
    }

    @FXML
    private void onKeyPressedBuscar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowArticulos();
        }
    }

    @FXML
    private void onActionBuscar(ActionEvent event) throws IOException {
        openWindowArticulos();
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
