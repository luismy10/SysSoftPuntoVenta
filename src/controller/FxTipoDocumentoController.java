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
import model.TipoDocumentoADO;
import model.TipoDocumentoTB;

public class FxTipoDocumentoController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblLoad;
    @FXML
    private TableView<TipoDocumentoTB> tvList;
    @FXML
    private TableColumn<TipoDocumentoTB, String> tcTipoComprobante;
    @FXML
    private TableColumn<TipoDocumentoTB, String> tcNombreImpresion;
    @FXML
    private TableColumn<TipoDocumentoTB, ImageView> tcPredeterminado;

    private boolean stateUpdate;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcTipoComprobante.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNombre()));
        tcNombreImpresion.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNombreDocumento()));
        tcPredeterminado.setCellValueFactory(new PropertyValueFactory<>("imagePredeterminado"));
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

    public void fillTabletTipoDocumento() {
        ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<ObservableList<TipoDocumentoTB>> task = new Task<ObservableList<TipoDocumentoTB>>() {
            @Override
            public ObservableList<TipoDocumentoTB> call() {
                return TipoDocumentoADO.ListTipoDocumento();
            }
        };
        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems(task.getValue());
            lblLoad.setVisible(false);
            if (stateUpdate) {
                List<TipoDocumentoTB> list = TipoDocumentoADO.GetDocumentoCombBox();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isPredeterminado() == true) {
                        Session.DEFAULT_COMPROBANTE = i;
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

    private void openWindowEdit() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_TIPODOCUMENTOPROCESO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxTipoDocumentoProcesoController controller = fXMLLoader.getController();
//            controller.setInitMoneyController(this);
            controller.initUpdate(tvList.getSelectionModel().getSelectedItem().getIdTipoDocumento(),
                    tvList.getSelectionModel().getSelectedItem().getNombre());
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Actualizar el comprobante", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.PANE);
            });
            stage.show();
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Tipo de comprobante", "Seleccione un elemento de la lista.", false);
        }
    }

    private void onEventPredeterminado() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            String result = TipoDocumentoADO.ChangeDefaultState(true, tvList.getSelectionModel().getSelectedItem().getIdTipoDocumento());
            if (result.equalsIgnoreCase("updated")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Tipo de comprobante", "Se cambio el estado correctamente.", false);
                stateUpdate = true;
                fillTabletTipoDocumento();
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Tipo de comprobante", "Error: " + result, false);
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Tipo de comprobante", "Seleccione un elemento de la lista.", false);
        }
    }

    @FXML
    private void onKeyPressedEdit(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowEdit();
        }
    }

    @FXML
    private void onActionEdit(ActionEvent event) throws IOException {
        openWindowEdit();
    }

    @FXML
    private void onKeyPressedPredetermined(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onEventPredeterminado();
        }
    }

    @FXML
    private void onActionPredetermined(ActionEvent event) {
        onEventPredeterminado();
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {

    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

    @FXML
    private void onKeyPressedReload(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
             fillTabletTipoDocumento();
        }
    }

    @FXML
    private void onActionReload(ActionEvent event) {
         fillTabletTipoDocumento();
    }

}
