package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ArticuloADO;
import model.ArticuloTB;
import model.ImageADO;
import model.ImagenTB;
import model.LoteADO;

public class FxArticulosController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblLoad;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, Integer> tcId;
    @FXML
    private TableColumn<ArticuloTB, String> tcDocument;
    @FXML
    private TableColumn<ArticuloTB, String> tcMarca;
    @FXML
    private TableColumn<ArticuloTB, String> tcPresentacion;
    @FXML
    private TableColumn<ArticuloTB, String> tcUnidadVenta;
    @FXML
    private TableColumn<ArticuloTB, String> tcEstado;
    @FXML
    private TableColumn<ArticuloTB, ImageView> tcLote;
    @FXML
    private VBox vbOpciones;

    private AnchorPane content;
    
    private boolean stateRequest;

    private FxArticuloSeleccionadoController seleccionadoController;

    private FxArticuloDetalleController detalleController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocument.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClave().get() + "\n" + cellData.getValue().getNombreMarca().get()
        ));
        tcMarca.setCellValueFactory(cellData -> cellData.getValue().getMarcaName());
        tcPresentacion.setCellValueFactory(cellData -> cellData.getValue().getPresentacionName());
        tcUnidadVenta.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getUnidadVenta() == 1 ? "Por Unidad/Pza" : "A Granel(Peso)"
        ));
        tcEstado.setCellValueFactory(cellData -> cellData.getValue().getEstadoName());
        tcLote.setCellValueFactory(new PropertyValueFactory<>("imageLote"));

        changeViewArticuloSeleccionado();
        stateRequest=false;

    }

    public void fillArticlesTable(String value) {

            ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });

            Task<ObservableList<ArticuloTB>> task = new Task<ObservableList<ArticuloTB>>() {
                @Override
                public ObservableList<ArticuloTB> call() {
                    return ArticuloADO.ListArticulos(value);
                }
            };

            task.setOnSucceeded((WorkerStateEvent e) -> {
                tvList.setItems((ObservableList<ArticuloTB>) task.getValue());
                lblLoad.setVisible(false);
                stateRequest=true;
            });
            task.setOnFailed((WorkerStateEvent event) -> {
                lblLoad.setVisible(false);
                stateRequest=false;
            });

            task.setOnScheduled((WorkerStateEvent event) -> {
                lblLoad.setVisible(true);
                stateRequest=false;
            });
            exec.execute(task);

            if (!exec.isShutdown()) {
                exec.shutdown();
            }
        

    }

    private void loadViewImage(String idRepresentante) {
        ImagenTB imagenTB = ImageADO.GetImage(idRepresentante, false);
        seleccionadoController.getIvPrincipal().setImage(imagenTB.getImagen());
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

    private void onViewArticuloAdd() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_ARTICULOPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxArticuloProcesoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Artículo", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
        controller.setInitArticulo();
    }

    private void onViewArticuloEdit() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_ARTICULOPROCESO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxArticuloProcesoController controller = fXMLLoader.getController();
            controller.initControllerArticulos(this);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Artículo", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();
            controller.setInitArticulo();
            controller.setValueEdit(tvList.getSelectionModel().getSelectedItem().getClave().get());
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Artículo", "Debe seleccionar un artículo para editarlo", false);
            tvList.requestFocus();
        }

    }

    private void onViewArticuloClone() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_ARTICULOPROCESO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxArticuloProcesoController controller = fXMLLoader.getController();
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Artículo", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();
            controller.setInitArticulo();
            controller.setValueClone(tvList.getSelectionModel().getSelectedItem().getClave().get());
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Artículo", "Debe seleccionar un artículo para clonarlo", false);
            tvList.requestFocus();
        }

    }

    private void onViewArticuloUpdateStock() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_ACTUALIZAR_STOCK);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxArticuloActualizarStockController controller = fXMLLoader.getController();
        controller.setInitArticuloUpdateStock(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Actualizar Stock", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
        controller.initComponents();
    }

    public void changeViewArticuloSeleccionado() {
        try {
            FXMLLoader fXMLSeleccionado = new FXMLLoader(getClass().getResource("/view/articulo/FxArticuloSeleccionado.fxml"));
            VBox seleccionado = fXMLSeleccionado.load();
            VBox.setVgrow(seleccionado, Priority.SOMETIMES);
            seleccionadoController = fXMLSeleccionado.getController();
            seleccionadoController.setArticuloController(this);

            vbOpciones.getChildren().clear();
            vbOpciones.getChildren().add(seleccionado);
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void changeViewArticuloDetalle() {
        try {
            FXMLLoader fXMLDetall = new FXMLLoader(getClass().getResource("/view/articulo/FxArticuloDetalle.fxml"));
            VBox detalle = fXMLDetall.load();
            VBox.setVgrow(detalle, Priority.SOMETIMES);
            detalleController = fXMLDetall.getController();
            detalleController.setArticuloController(this);

            vbOpciones.getChildren().clear();
            vbOpciones.getChildren().add(detalle);
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        onViewArticuloAdd();
    }

    @FXML
    private void onKeyPressedAdd(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewArticuloAdd();
        }
    }

    @FXML
    private void onActionEdit(ActionEvent event) throws IOException {
        onViewArticuloEdit();
    }

    @FXML
    private void onKeyPressedEdit(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewArticuloEdit();
        }
    }

    @FXML
    private void onActionReload(ActionEvent event) {
        if(stateRequest)fillArticlesTable("");
    }

    @FXML
    private void onKeyPressedReload(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            if(stateRequest)fillArticlesTable("");
        }
    }

    @FXML
    private void onKerPressedSearch(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!tvList.getItems().isEmpty()) {
                tvList.requestFocus();
                tvList.getSelectionModel().select(0);
            }
        }
    }

    @FXML
    private void onKeyReleasedSearch(KeyEvent event) {
        if(stateRequest)fillArticlesTable(txtSearch.getText().trim());
    }

    @FXML
    private void onKeyPressedClone(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewArticuloClone();
        }
    }

    @FXML
    private void onActionClone(ActionEvent event) throws IOException {
        onViewArticuloClone();
    }

    public void onViewDetailArticulo() {
        loadViewImage(tvList.getSelectionModel().getSelectedItem().getIdArticulo());
        ArrayList<ArticuloTB> list = ArticuloADO.GetArticulosByIdView(tvList.getSelectionModel().getSelectedItem().getIdArticulo());
        if (!list.isEmpty()) {
            ArticuloTB articuloTB = list.get(0);
            seleccionadoController.getLblName().setText(articuloTB.getNombreMarca().get());
            seleccionadoController.getLblName().setText(articuloTB.getNombreMarca().get());
            seleccionadoController.getLblPrice().setText("S/. " + Tools.roundingValue(articuloTB.getPrecioVenta(), 2));
            seleccionadoController.getLblQuantity().setText(articuloTB.getCantidad() % 1 == 0
                    ? Tools.roundingValue(articuloTB.getCantidad(), 0)
                    : Tools.roundingValue(articuloTB.getCantidad(), 2));
            if (detalleController != null) {
                detalleController.getTvList().setItems(LoteADO.ListByIdLote(tvList.getSelectionModel().getSelectedItem().getIdArticulo()));
            }
        }
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            if (event.getClickCount() == 1) {
                onViewDetailArticulo();
            } else if (event.getClickCount() == 2) {
                onViewArticuloEdit();
            }
        }

    }

    @FXML
    private void onKeyPressedList(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewArticuloEdit();
        }
    }

    @FXML
    private void onKeyRelasedList(KeyEvent event) {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            if (null != event.getCode()) {
                switch (event.getCode()) {
                    case UP:
                        onViewDetailArticulo();
                        break;
                    case DOWN:
                        onViewDetailArticulo();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @FXML
    private void onKeyPressedCantidad(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionCantidad(ActionEvent event) throws IOException {
        onViewArticuloUpdateStock();
    }

    public TableView<ArticuloTB> getTvList() {
        return tvList;
    }

    public TextField getTxtSearch() {
        return txtSearch;
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
