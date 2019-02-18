package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private TableColumn<LoteTB, String> tcCaducidad;
    @FXML
    private TableColumn<LoteTB, String> tcActual;
    @FXML
    private TextField txtSearch;
    @FXML
    private DatePicker dtFechaInicial;
    @FXML
    private DatePicker dtFechaFinal;
    @FXML
    private ComboBox<String> cbEstado;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcNumeroLote.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNumeroLote()));
        tcArticulo.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getArticuloTB().getClave() + "\n" + cellData.getValue().getArticuloTB().getNombreMarca()));
        tcCaducidad.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaCaducidad().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))));
        tcActual.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getExistenciaActual()));

        tcCaducidad.setCellFactory(column -> {
            return new TableCell<LoteTB, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        System.out.println();
                        setStyle("-fx-background-color: yellow");
                    }
                }
            };

        });
        Tools.actualDate(Tools.getDate(), dtFechaInicial);
        Tools.actualDate(Tools.getDate(), dtFechaFinal);
        cbEstado.getItems().addAll("Todos", "Proximos a caducar", "Caducados");
        cbEstado.getSelectionModel().select(0);
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

    private void onViewEditarLote() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_LOTECAMBIAR);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxLoteCambiarController controller = fXMLLoader.getController();
        controller.setLoteController(this);
        controller.setEditBatchRealizado(new String[]{tvList.getSelectionModel().getSelectedItem().getNumeroLote(),
            tvList.getSelectionModel().getSelectedItem().getIdLote() + "",
            tvList.getSelectionModel().getSelectedItem().getArticuloTB().getClave(),
            tvList.getSelectionModel().getSelectedItem().getArticuloTB().getNombreMarca(),
            tvList.getSelectionModel().getSelectedItem().getExistenciaActual() + "",
            tvList.getSelectionModel().getSelectedItem().getFechaCaducidad() + ""
        });
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Editar lote", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();

    }

    @FXML
    private void onActionEdit(ActionEvent event) throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            onViewEditarLote();
        }
    }

    @FXML
    private void onKeyPressedEdit(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
                onViewEditarLote();
            }
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

    @FXML
    private void onActionReporte(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionReporte(ActionEvent event) {

    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
