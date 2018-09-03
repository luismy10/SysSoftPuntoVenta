package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.DBUtil;
import model.DetalleTB;
import model.MantenimientoADO;
import static model.MantenimientoADO.*;
import model.MantenimientoTB;

public class FxDetalleMantenimientoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtSearchMaintenance;
    @FXML
    private ListView<MantenimientoTB> lvMaintenance;
    @FXML
    private TextField txtSearchDetail;
    @FXML
    private TableView<DetalleTB> tvDetail;
    @FXML
    private Text lblEstado;
    @FXML
    private ImageView imState;
    @FXML
    private HBox hbProccess;
    @FXML
    private Text lblItems;

    @FXML
    private TableColumn<DetalleTB, Integer> tcNumero;
    @FXML
    private TableColumn<DetalleTB, String> tcNombre;
    @FXML
    private TableColumn<DetalleTB, String> tcDescripcion;
    @FXML
    private TableColumn<DetalleTB, String> tcEstado;

    private boolean stateconnect;
    @FXML
    private Label lblWarnings;
    @FXML
    private ImageView imWarning;

    private boolean onAnimationStart, onAnimationFinished;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onAnimationFinished=false;
        tcNumero.setCellValueFactory(cellData -> cellData.getValue().getIdDetalle().asObject());
        tcNombre.setCellValueFactory(cellData -> cellData.getValue().getNombre());
        tcDescripcion.setCellValueFactory(cellData -> cellData.getValue().getDescripcion());
        tcEstado.setCellValueFactory(cellData -> cellData.getValue().getEstado());
        initWindow();
    }

    private void initWindow() {
        stateconnect = DBUtil.StateConnection();
        lblEstado.setText(stateconnect == true ? "Estado: conectado" : "Estado: desconectado");
        if (stateconnect) {
            imState.setImage(new Image("/view/connected.png"));
            hbProccess.setDisable(false);
            try {
                lvMaintenance.getItems().clear();
                ListPrincipal().forEach(e -> {
                    lvMaintenance.getItems().add(e);
                });
                lblItems.setText(ListPrincipal().isEmpty() == true ? "Items (0)" : "Items (" + ListPrincipal().size() + ")");
                if (!lvMaintenance.getItems().isEmpty()) {
                    lvMaintenance.getSelectionModel().select(0);
                    tvDetail.setItems(ListDetail(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento()));
                }
            } catch (ClassNotFoundException | SQLException ex) {
                System.out.println(ex.getLocalizedMessage());
            } finally {
                stateconnect = false;
            }
        } else {
            imState.setImage(new Image("/view/disconnectd.png"));
            hbProccess.setDisable(true);
        }
    }

    @FXML
    private void onMouseClickedAgregar(MouseEvent event) throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_MANTENIMIENTO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxMantenimientoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModel(parent, "Agregar item de mantenimiento", window.getScene().getWindow());
        stage.setResizable(false);
        stage.show();
        controller.initWindow();
    }

    @FXML
    private void onMouseClickedEditar(MouseEvent event) throws IOException {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0 && lvMaintenance.isFocused()) {
            URL url = getClass().getResource(Tools.FX_FILE_MANTENIMIENTO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxMantenimientoController controller = fXMLLoader.getController();
            //
            Stage stage = FxWindow.StageLoaderModel(parent, "Editar item de mantenimiento", window.getScene().getWindow());
            stage.setResizable(false);
            stage.show();
            controller.setValues(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento(), lvMaintenance.getSelectionModel().getSelectedItem().getNombre());
        }

    }

    @FXML
    private void onMouseClickedRemover(MouseEvent event) {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0 && lvMaintenance.isFocused()) {
            if (DBUtil.StateConnection()) {
                short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Mantenimiento", "¿Esta seguro de continuar?", true);
                if (confirmation == 1) {
                    String result = MantenimientoADO.DeleteMantenimiento(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento());
                    switch (result) {
                        case "eliminado":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle mantenimiento", "Eliminado correctamente.", false);
                            reloadListView();
                            break;
                        case "error":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle mantenimiento", "No se puedo completar la ejecución.", false);
                            break;
                        default:
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Detalle mantenimiento", result, false);
                            break;
                    }
                } else {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle mantenimiento", "Se cancelo la petición.", false);

                }
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Detalle mantenimiento", "No hay conexión al servidor.", false);
            }
        }

    }

    @FXML
    private void onMouseClickedState(MouseEvent event) {
        initWindow();
    }

    @FXML
    private void onMouseClickedReload(MouseEvent event) {
        reloadListView();
    }

    public void reloadListView() {
        try {
            if (DBUtil.StateConnection()) {
                lvMaintenance.getItems().clear();
                ListPrincipal().forEach(e -> {
                    lvMaintenance.getItems().add(e);
                });
                lblItems.setText(ListPrincipal().isEmpty() == true ? "Items (0)" : "Items (" + ListPrincipal().size() + ")");
                if (!lvMaintenance.getItems().isEmpty()) {
                    lvMaintenance.getSelectionModel().select(0);
                    tvDetail.setItems(ListDetail(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento()));
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) throws ClassNotFoundException, SQLException {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0) {
            tvDetail.setItems(ListDetail(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento()));
        }
    }

    @FXML
    private void onKeyReleasedList(KeyEvent event) throws ClassNotFoundException, SQLException {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0) {
            tvDetail.setItems(ListDetail(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento()));
        }
    }

    @FXML
    private void onMouseClickedPlus(MouseEvent event) throws IOException {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0) {
            URL url = getClass().getResource(Tools.FX_FILE_DETALLE);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxDetalleController controller = fXMLLoader.getController();
            //
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.getIcons().add(new Image("/view/icon.png"));
            stage.setScene(scene);
            stage.setTitle("Agregar detalle del item");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(window.getScene().getWindow());
            stage.setResizable(false);
            stage.show();
            controller.setValueAdd(lvMaintenance.getSelectionModel().getSelectedItem().getNombre(),
                    lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento(),
                    tvDetail.getSelectionModel().getSelectedIndex() >= 0 ? tvDetail.getSelectionModel().getSelectedItem().getIdDetalle().getValue().toString() : "0");

        }

    }

    @FXML
    private void onMouseClickedEdit(MouseEvent event) throws IOException {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0 && tvDetail.getSelectionModel().getSelectedIndex() >= 0) {
            URL url = getClass().getResource(Tools.FX_FILE_DETALLE);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxDetalleController controller = fXMLLoader.getController();
            //
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.getIcons().add(new Image("/view/icon.png"));
            stage.setScene(scene);
            stage.setTitle("Editar detalle del item");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(window.getScene().getWindow());
            stage.setResizable(false);
            stage.show();
            controller.setValueUpdate(lvMaintenance.getSelectionModel().getSelectedItem().getNombre(),
                    lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento(),
                    tvDetail.getSelectionModel().getSelectedItem().getIdDetalle().getValue().toString(),
                    tvDetail.getSelectionModel().getSelectedItem().getNombre().get(),
                    tvDetail.getSelectionModel().getSelectedItem().getDescripcion().get());

        } else {            
            onAnimationStart=true;
            if (onAnimationStart && !onAnimationFinished) {   
                onAnimationFinished=true;
                lblWarnings.setText("Seleccione un item del detalle para actualizar.");
                lblWarnings.setStyle("-fx-text-fill:#da0505");
                imWarning.setImage(new Image("/view/warning.png"));
                tvDetail.requestFocus();
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), imWarning);
                scaleTransition.setByX(0.4f);
                scaleTransition.setByY(0.4f);
                scaleTransition.setCycleCount(4);
                scaleTransition.setAutoReverse(true);
                scaleTransition.setOnFinished((ActionEvent action) -> {
                    lblWarnings.setText("Las opciones del detalle están en el panel de los botonen.");
                    lblWarnings.setStyle("-fx-text-fill:#23283a");
                    imWarning.setImage(null);
                    onAnimationFinished=false;
                    onAnimationStart = false;
                });
                scaleTransition.play();               
                
            }
        }
    }


    @FXML
    private void onMouseClickedDetail(MouseEvent event) {
        if (tvDetail.getSelectionModel().getSelectedIndex() >= 0) {
            
        }
    }

}
