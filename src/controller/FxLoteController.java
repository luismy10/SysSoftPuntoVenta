package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.LoteADO;
import model.LoteTB;

public class FxLoteController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblLoad;
    @FXML
    private TableView<LoteTB> tvList;
    @FXML
    private TableColumn<LoteTB, Integer> tcId;
    @FXML
    private TableColumn<LoteTB, String> tcNumeroLote;
    @FXML
    private TableColumn<LoteTB, String> tcArticulo;
    @FXML
    private TableColumn<LoteTB, String> tcFabricacion;
    @FXML
    private TableColumn<LoteTB, String> tcCaducidad;
    @FXML
    private TableColumn<LoteTB, String> tcInicial;
    @FXML
    private TableColumn<LoteTB, String> tcActual;
    @FXML
    private TextField txtSearch;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcNumeroLote.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNumeroLote()));
        tcArticulo.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getArticuloTB().getClave().get() + "\n" + cellData.getValue().getArticuloTB().getNombre().get()));
        tcFabricacion.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaFabricacion().format(DateTimeFormatter.ofPattern("E, MMM dd yyyy"))));
        tcCaducidad.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaCaducidad().format(DateTimeFormatter.ofPattern("E, MMM dd yyyy"))));
        tcInicial.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getExistenciaInicial()));
        tcActual.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getExistenciaActual()));
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

    private void onViewArticuloAdd() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_LOTECAMBIAR);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxLoteCambiarController controller = fXMLLoader.getController();
        controller.setEditBatchRealizado(new String[]{tvList.getSelectionModel().getSelectedItem().getNumeroLote(),
            tvList.getSelectionModel().getSelectedItem().getArticuloTB().getClave().get(),
            tvList.getSelectionModel().getSelectedItem().getArticuloTB().getNombre().get(),
            tvList.getSelectionModel().getSelectedItem().getExistenciaActual() + "",
            tvList.getSelectionModel().getSelectedItem().getFechaCaducidad() + "",
            tvList.getSelectionModel().getSelectedItem().getFechaFabricacion() + ""
        });
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Editar lote", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(SysSoft.pane);
        });
        stage.show();

    }

    public void fillLoteTable(String value) {

        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<List<LoteTB>> task = new Task<List<LoteTB>>() {
            @Override
            public ObservableList<LoteTB> call() {
                return LoteADO.ListLote(value);
            }
        };

        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems((ObservableList<LoteTB>) task.getValue());
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

    @FXML
    private void onActionEdit(ActionEvent event) throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            onViewArticuloAdd();
        }
    }

    @FXML
    private void onActionSearch(ActionEvent event) {
        fillLoteTable(txtSearch.getText().trim());
    }

    @FXML
    private void onActionReload(ActionEvent event) {
        fillLoteTable("");
    }

    void setContent(AnchorPane content) {
        this.content = content;
    }

}
