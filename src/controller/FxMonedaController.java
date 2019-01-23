package controller;

import java.io.IOException;
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
import model.MonedaADO;
import model.MonedaTB;

public class FxMonedaController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private TableView<MonedaTB> tvList;
    @FXML
    private TableColumn<MonedaTB, String> tcMoneda;
    @FXML
    private TableColumn<MonedaTB, String> tcTipoCambio;
    @FXML
    private TableColumn<MonedaTB, String> tcAbreviatura;
    @FXML
    private TableColumn<MonedaTB, ImageView> tcPredeterminado;
    @FXML
    private Label lblLoad;

    private AnchorPane content;

    private boolean stateRequest;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcMoneda.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getSimbolo() + " - " + cellData.getValue().getNombre()
        ));
        tcTipoCambio.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getTipoCambio()));
        tcAbreviatura.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getAbreviado()));
        tcPredeterminado.setCellValueFactory(new PropertyValueFactory<>("imagePredeterminado"));
        stateRequest = false;
    }

    public void fillTableMonedas() {
        ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<ObservableList<MonedaTB>> task = new Task<ObservableList<MonedaTB>>() {
            @Override
            public ObservableList<MonedaTB> call() {
                return MonedaADO.ListMonedas();
            }
        };

        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems(task.getValue());
            lblLoad.setVisible(false);
            stateRequest = true;
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

    private void InitializationTransparentBackground() {
        Session.pane.setStyle("-fx-background-color: black");
        Session.pane.setTranslateX(0);
        Session.pane.setTranslateY(0);
        Session.pane.setPrefWidth(Session.WIDTH_WINDOW);
        Session.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.pane.setOpacity(0.7f);
        content.getChildren().add(Session.pane);
    }

    private void openWindowMoneyRegister() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_MONEDAPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxMonedaProcesoController controller = fXMLLoader.getController();
        controller.setInitMoneyController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Registre su moneda", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();

    }

    private void openWindowMoneyUpdate() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_MONEDAPROCESO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxMonedaProcesoController controller = fXMLLoader.getController();
            controller.setInitMoneyController(this);
            controller.setUpdateMoney(tvList.getSelectionModel().getSelectedItem().getIdMoneda(),
                    tvList.getSelectionModel().getSelectedItem().getNombre(),
                    tvList.getSelectionModel().getSelectedItem().getAbreviado(),
                    tvList.getSelectionModel().getSelectedItem().getSimbolo(),
                    tvList.getSelectionModel().getSelectedItem().getTipoCambio()
            );
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Actualizar su moneda", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();

        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Moneda", "Seleccione un elemento de la lista.", false);

        }

    }

    private void onEventProdeteminado() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            if (stateRequest) {
                String result = MonedaADO.ChangeDefaultState(true, tvList.getSelectionModel().getSelectedItem().getIdMoneda());
                if (result.equalsIgnoreCase("updated")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Moneda", "Se cambio el estado correctamente.", false);
                    fillTableMonedas();
                    List<MonedaTB> list = MonedaADO.GetMonedasCombBox();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getPredeterminado() == true) {
                            Session.DEFAULT_MONEDA = i;
                            break;
                        }
                    }
                } else {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Moneda", "Error: " + result, false);
                }
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Moneda", "Seleccione un elemento de la lista.", false);
        }
    }

    private void onEventRemover() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            String result = MonedaADO.RemoveElement(tvList.getSelectionModel().getSelectedItem().getIdMoneda());
            if (result.equalsIgnoreCase("error")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Moneda", "No se puedo eliminar ya que está predeterminado la moneda.", false);
                fillTableMonedas();
            } else if (result.equalsIgnoreCase("removed")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Moneda", "Se eliminó correctamente la moneda.", false);
                fillTableMonedas();
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Moneda", "Error: " + result, false);
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Moneda", "Seleccione un elemento de la lista.", false);
        }
    }

    @FXML
    private void onKeyPressedAdd(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowMoneyRegister();
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        openWindowMoneyRegister();
    }

    @FXML
    private void onKeyPressedEdit(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowMoneyUpdate();

        }
    }

    @FXML
    private void onActionEdit(ActionEvent event) throws IOException {
        openWindowMoneyUpdate();

    }

    @FXML
    private void onKeyPressedRemove(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onEventRemover();
        }
    }

    @FXML
    private void onActionRemove(ActionEvent event) {
        onEventRemover();
    }

    @FXML
    private void onKeyPressedPredetermined(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onEventProdeteminado();
        }
    }

    @FXML
    private void onActionPredetermined(ActionEvent event) {
        onEventProdeteminado();
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowMoneyUpdate();
        }

    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
