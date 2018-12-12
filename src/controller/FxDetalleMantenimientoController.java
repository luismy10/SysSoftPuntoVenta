package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.DetalleADO;
import model.DetalleTB;
import static model.MantenimientoADO.*;
import static model.DetalleADO.*;
import model.MantenimientoTB;

public class FxDetalleMantenimientoController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private TextField txtSearchMaintenance;
    @FXML
    private ListView<MantenimientoTB> lvMaintenance;
    @FXML
    private TextField txtSearchDetail;
    @FXML
    private TableView<DetalleTB> tvDetail;
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
    @FXML
    private Label lblWarnings;
    @FXML
    private ImageView imWarning;
    @FXML
    private Text lblDetail;

    private boolean onAnimationStart, onAnimationFinished;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onAnimationFinished = false;
        tcNumero.setCellValueFactory(cellData -> cellData.getValue().getIdDetalle().asObject());
        tcNombre.setCellValueFactory(cellData -> cellData.getValue().getNombre());
        tcDescripcion.setCellValueFactory(cellData -> cellData.getValue().getDescripcion());
        tcEstado.setCellValueFactory(cellData -> cellData.getValue().getEstado());

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


    private void initMaintenance(String... value) {
        try {
            lvMaintenance.getItems().clear();
            ObservableList<MantenimientoTB> listMaintenance = ListPrincipal(value[0]);
            listMaintenance.forEach(e -> {
                lvMaintenance.getItems().add(e);
            });
            lblItems.setText(listMaintenance.isEmpty() == true ? "Items (0)" : "Items (" + listMaintenance.size() + ")");
            if (!lvMaintenance.getItems().isEmpty()) {
                lvMaintenance.getSelectionModel().select(0);
                initDetail(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento(), "");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        } finally {
            
        }
    }

    public void initDetail(String... value) {
        ObservableList<DetalleTB> listDetail = ListDetail(value);
        tvDetail.setItems(listDetail);
        lblDetail.setText(listDetail.isEmpty() == true ? "Ingrese el nombre del detalle (0)" : "Ingrese el nombre del detalle (" + listDetail.size() + ")");

    }

    @FXML
    private void onMouseClickedAgregar(MouseEvent event) throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_MANTENIMIENTO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxMantenimientoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar item de mantenimiento", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
        controller.initWindow();
    }

    @FXML
    private void onMouseClickedEditar(MouseEvent event) throws IOException {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0 && lvMaintenance.isFocused()) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_MANTENIMIENTO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxMantenimientoController controller = fXMLLoader.getController();
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Editar item de mantenimiento", window.getScene().getWindow());
            stage.setResizable(false);
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();
            controller.setValues(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento(), lvMaintenance.getSelectionModel().getSelectedItem().getNombre());
        }

    }

    @FXML
    private void onMouseClickedRemover(MouseEvent event) {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0 && lvMaintenance.isFocused()) {
            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Mantenimiento", "¿Esta seguro de continuar?", true);
            if (confirmation == 1) {
                String result = DeleteMantenimiento(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento());
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

    public void reloadListView() {
        initMaintenance("");
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) throws ClassNotFoundException, SQLException {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0) {
            initDetail(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento(), "");
        }
    }

    @FXML
    private void onKeyReleasedList(KeyEvent event) throws ClassNotFoundException, SQLException {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0) {
            initDetail(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento(), "");
        }
    }

    private void onActionEditDetail() throws IOException {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0 && tvDetail.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_DETALLE);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxDetalleController controller = fXMLLoader.getController();
            controller.initConfiguracion(this);
            //
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.getIcons().add(new Image("/view/icon.png"));
            stage.setScene(scene);
            stage.setTitle("Editar detalle del item");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();
            controller.setValueUpdate(lvMaintenance.getSelectionModel().getSelectedItem().getNombre(),
                    lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento(),
                    tvDetail.getSelectionModel().getSelectedItem().getIdDetalle().getValue().toString(),
                    tvDetail.getSelectionModel().getSelectedItem().getNombre().get(),
                    tvDetail.getSelectionModel().getSelectedItem().getDescripcion().get(),
                    tvDetail.getSelectionModel().getSelectedItem().getEstado().get());

        } else {
            onAnimationStart = true;
            if (onAnimationStart && !onAnimationFinished) {
                onAnimationFinished = true;
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
                    onAnimationFinished = false;
                    onAnimationStart = false;
                });
                scaleTransition.play();

            }
        }
    }

    @FXML
    private void onMouseClickedDetail(MouseEvent event) throws IOException {
        if (tvDetail.getSelectionModel().getSelectedIndex() >= 0) {
            if (event.getClickCount() == 2) {
                onActionEditDetail();
            }
        }
    }

    @FXML
    private void onActionSearchItems(ActionEvent event) {
        lvMaintenance.requestFocus();
    }

    @FXML
    private void onKeyReleasedSearchItems(KeyEvent event) {

        initMaintenance(txtSearchMaintenance.getText());

    }

    @FXML
    private void onActionSearchDetail(ActionEvent event) {
        tvDetail.requestFocus();
    }

    @FXML
    private void onKeyReleasedSearchDetail(KeyEvent event) throws ClassNotFoundException, SQLException {
        initDetail(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento(), txtSearchDetail.getText());

    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
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
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();
            controller.setValueAdd(lvMaintenance.getSelectionModel().getSelectedItem().getNombre(),
                    lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento(),
                    tvDetail.getSelectionModel().getSelectedIndex() >= 0 ? tvDetail.getSelectionModel().getSelectedItem().getIdDetalle().getValue().toString() : "0");

        }
    }

    @FXML
    private void onActionEdit(ActionEvent event) throws IOException {
        onActionEditDetail();
    }

    @FXML
    private void onActionReload(ActionEvent event) {
        reloadListView();
    }

    @FXML
    private void onActionRemover(ActionEvent event) {
        if (lvMaintenance.getSelectionModel().getSelectedIndex() >= 0 && tvDetail.getSelectionModel().getSelectedIndex() >= 0) {
            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Mantenimiento", "¿Esta seguro de continuar?", true);
            if (confirmation == 1) {
                DetalleTB detalleTB = new DetalleTB();
                detalleTB.setIdDetalle(tvDetail.getSelectionModel().getSelectedItem().getIdDetalle().get());
                detalleTB.setIdMantenimiento(lvMaintenance.getSelectionModel().getSelectedItem().getIdMantenimiento());
                String result = DetalleADO.DeleteDetail(detalleTB);
                switch (result) {
                    case "eliminado":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle mantenimiento", "Eliminado correctamente.", false);

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

    void setContent(AnchorPane content) {
        this.content = content;
    }

}
