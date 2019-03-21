package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ImpuestoADO;
import model.ImpuestoTB;

public class FxImpuestoController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblLoad;
    @FXML
    private TableView<ImpuestoTB> tvList;
    @FXML
    private TableColumn<ImpuestoTB, String> tcNombre;
    @FXML
    private TableColumn<ImpuestoTB, String> tcValor;
    @FXML
    private TableColumn<ImpuestoTB, ImageView> tcPredeterminado;
    @FXML
    private TableColumn<ImpuestoTB, String> tcCodigoAlterno;

    private AnchorPane content;

    private boolean stateUpdate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcNombre.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNombre()));
        tcValor.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getValor()));
        tcPredeterminado.setCellValueFactory(new PropertyValueFactory<>("imagePredeterminado"));
        tcCodigoAlterno.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getCodigoAlterno()));
        stateUpdate = false;
    }

    private void InitializationTransparentBackground() {
        Session.PANE.setStyle("-fx-background-color: black");
        Session.PANE.setTranslateX(0);
        Session.PANE.setTranslateY(0);
        Session.PANE.setPrefWidth(Session.WIDTH_WINDOW);
        Session.PANE.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.PANE.setOpacity(0.7f);
        content.getChildren().add(Session.PANE);
    }

    public void fillTabletTax() {
        ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<ObservableList<ImpuestoTB>> task = new Task<ObservableList<ImpuestoTB>>() {
            @Override
            public ObservableList<ImpuestoTB> call() {
                return ImpuestoADO.ListImpuestos();
            }
        };
        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems(task.getValue());
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

    private void openWindowImpuestoRegister() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_IMPUESTOPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxImpuestoProcesoController controller = fXMLLoader.getController();
        controller.setInitImpuestoController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Registre su impuesto", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.PANE);
        });
        stage.show();

    }

    private void openWindowImpuestoUpdate() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_IMPUESTOPROCESO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxImpuestoProcesoController controller = fXMLLoader.getController();
            controller.setInitImpuestoController(this);
            controller.setUpdateImpuesto(tvList.getSelectionModel().getSelectedItem().getIdImpuesto(),
                    tvList.getSelectionModel().getSelectedItem().getNombre(),
                    String.valueOf(tvList.getSelectionModel().getSelectedItem().getValor()),
                    tvList.getSelectionModel().getSelectedItem().getCodigoAlterno()
            );
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Actualizar su impuesto", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.PANE);
            });
            stage.show();

        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Impuesto", "Seleccione un elemento de la lista.", false);
        }

    }

    private void onEventProdeteminado() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            String result = ImpuestoADO.ChangeDefaultStateImpuesto(true, tvList.getSelectionModel().getSelectedItem().getIdImpuesto());
            if (result.equalsIgnoreCase("updated")) {
                System.out.println("Entre acaaaaa");
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Impuesto", "Se cambio el estado correctamente.", false);
                stateUpdate = true;
                this.fillTableImpuesto();
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Impuesto", "Error: " + result, false);
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Impuesto", "Seleccione un elemento de la lista.", false);
        }
    }

    private void onEventDelete() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Impuesto", "¿Está seguro de eliminar el impuesto?", true);
            if (confirmation == 1) {
                String result = ImpuestoADO.DeleteImpuestoById(tvList.getSelectionModel().getSelectedItem().getIdImpuesto());
                if (result.equalsIgnoreCase("deleted")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Impuesto", "Se eliminó el impuesto correctamente.", false);
                    fillTabletTax();
                } else if (result.equalsIgnoreCase("sistema")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Impuesto", "No se puede eliminar el impuesto, ya que es del sistema.", false);
                } else if (result.equalsIgnoreCase("articulo")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Impuesto", "No se puede eliminar el impuesto, está ligado a un artículo.", false);
                } else {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Impuesto", result, false);
                }
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Impuesto", "Seleccione un elemento de la lista.", false);
        }
    }

    public void fillTableImpuesto() {
        ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<ObservableList<ImpuestoTB>> task = new Task<ObservableList<ImpuestoTB>>() {
            @Override
            public ObservableList<ImpuestoTB> call() {
                return ImpuestoADO.ListImpuestos();
            }
        };

        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems(task.getValue());
            lblLoad.setVisible(false);
            if (stateUpdate) {
                List<ImpuestoTB> list = ImpuestoADO.GetTipoImpuestoCombBox();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPredeterminado() == true) {
                        Session.DEFAULT_IMPUESTO = i;
                        break;
                    }
                }
                stateUpdate = false;
            }
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
    private void onMouseClickedList(MouseEvent event) {

    }

    @FXML
    private void onKeyPressedAdd(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowImpuestoRegister();
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        openWindowImpuestoRegister();
    }

    @FXML
    private void onKeyPressedEdit(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowImpuestoUpdate();
        }
    }

    @FXML
    private void onActionEdit(ActionEvent event) throws IOException {
        openWindowImpuestoUpdate();
    }

    @FXML
    private void onKeyPressedPredetermined(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            this.onEventProdeteminado();
        }
    }

    @FXML
    private void onActionPredetermined(ActionEvent event) {
        this.onEventProdeteminado();
    }

    @FXML
    private void onKeyPressedDelete(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onEventDelete();
        }
    }

    @FXML
    private void onActionDelete(ActionEvent event) {
        onEventDelete();
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
