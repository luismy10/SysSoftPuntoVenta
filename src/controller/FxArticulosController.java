package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
import model.DetalleADO;
import model.DetalleTB;

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
    private TableColumn<ArticuloTB, String> tcUnidadVenta;
    @FXML
    private TableColumn<ArticuloTB, String> tcCategoria;
    @FXML
    private TableColumn<ArticuloTB, String> tcEstado;
    @FXML
    private VBox vbOpciones;
    @FXML
    private ComboBox<DetalleTB> cbCategoria;

    private AnchorPane content;

    private boolean status;

    private FxArticuloSeleccionadoController seleccionadoController;

    private FxArticuloDetalleController detalleController;
    @FXML
    private TextField txtSearchCode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        window.setOnKeyReleased((KeyEvent event) -> {
            try {
                if (null != event.getCode()) {
                    switch (event.getCode()) {

                        case F1:
                            onViewArticuloAdd();
                            event.consume();
                            break;
                        case F2:
                            onViewArticuloEdit();
                            event.consume();
                            break;
                        case F3:
                            onViewArticuloUpdateStock();
                            event.consume();
                            break;
                        case F4:
                            onViewArticuloClone();
                            event.consume();
                            break;
                        case F5:
                            if (!status) {
                                fillArticlesTable((short) 1, "", 0);
                                if (!tvList.getItems().isEmpty()) {
                                    tvList.getSelectionModel().select(0);
                                }
                            }
                            event.consume();
                            break;
                        case F6:

                            event.consume();
                            break;
                        case F7:
                            txtSearchCode.requestFocus();
                            txtSearchCode.selectAll();
                            event.consume();
                            break;
                        case F8:
                            txtSearch.requestFocus();
                            txtSearch.selectAll();
                            event.consume();
                            break;
                        case F9:
                            cbCategoria.requestFocus();
                            break;
                        case DELETE:
                            removedArticulo();
                            break;
                        default:

                            break;
                    }
                }
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        });

        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocument.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClave() + "\n" + cellData.getValue().getNombreMarca()
        ));
        tcMarca.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getMarcaName()));
        tcUnidadVenta.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getUnidadVenta() == 1 ? "Por Unidad/Pza" : "A Granel(Peso)"
        ));
        tcCategoria.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getCategoriaName()));
        tcEstado.setCellValueFactory(cellData -> cellData.getValue().getEstadoName());

        cbCategoria.getItems().add(new DetalleTB(new SimpleIntegerProperty(0), new SimpleStringProperty("-- Seleccione --")));
        DetalleADO.GetDetailId("0006").forEach(e -> {
            cbCategoria.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        cbCategoria.getSelectionModel().select(0);
        changeViewArticuloSeleccionado();
        status = false;
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

    public void fillArticlesTable(short option, String value, int categoria) {
        ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<ObservableList<ArticuloTB>> task = new Task<ObservableList<ArticuloTB>>() {
            @Override
            public ObservableList<ArticuloTB> call() {
                return ArticuloADO.ListArticulos(option, value, categoria);
            }
        };

        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems((ObservableList<ArticuloTB>) task.getValue());
            lblLoad.setVisible(false);
            if (!tvList.getItems().isEmpty()) {
                tvList.getSelectionModel().select(0);
                onViewDetailArticulo();
            }
            status = false;
        });
        task.setOnFailed((WorkerStateEvent event) -> {
            lblLoad.setVisible(false);
            status = false;
        });

        task.setOnScheduled((WorkerStateEvent event) -> {
            lblLoad.setVisible(true);
            status = true;
        });
        exec.execute(task);

        if (!exec.isShutdown()) {
            exec.shutdown();
        }

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
            content.getChildren().remove(Session.PANE);
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
                content.getChildren().remove(Session.PANE);
            });
            stage.show();
            controller.setInitArticulo();
            controller.setValueEdit(tvList.getSelectionModel().getSelectedItem().getClave());
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
                content.getChildren().remove(Session.PANE);
            });
            stage.show();
            controller.setInitArticulo();
            controller.setValueClone(tvList.getSelectionModel().getSelectedItem().getClave());
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Artículo", "Debe seleccionar un artículo para clonarlo", false);
            tvList.requestFocus();
        }

    }

    private void onViewArticuloUpdateStock() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_ACTUALIZAR_STOCK);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxArticuloActualizarStockController controller = fXMLLoader.getController();
            controller.setInitArticuloController(this);
            controller.loadArticuloStock(tvList.getSelectionModel().getSelectedItem().getIdArticulo(), tvList.getSelectionModel().getSelectedItem().getNombreMarca());
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Ajuste de inventario", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.PANE);
            });
            stage.show();
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Artículo", "Debe seleccionar un artículo para realizar el ajuste de inventario", false);
            tvList.requestFocus();
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

    private void removedArticulo() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            short value = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Artículo", "Está seguro de eliminar el artículo", true);
            if (value == 1) {
                String result = ArticuloADO.RemoveArticulo(tvList.getSelectionModel().getSelectedItem().getIdArticulo());
                if (result.equalsIgnoreCase("compra")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Artículo", "El artículo esta ligado a una compra, no se puede eliminar hasta que la compra sea borrada", false);
                } else if (result.equalsIgnoreCase("venta")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Artículo", "El artículo esta ligado a una venta, no se puede eliminar hasta que la venta sea borrada", false);
                } else if (result.equalsIgnoreCase("removed")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Artículo", "Se elimino correctamente", false);
                    if (!status) {
                        fillArticlesTable((short) 1, txtSearch.getText().trim(), 0);
                    }
                } else {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Artículo", result, false);

                }
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Artículo", "Seleccione un item para eliminarlo", false);
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
        if (!status) {
            fillArticlesTable((short) 1, "", 0);
            if (!tvList.getItems().isEmpty()) {
                tvList.getSelectionModel().select(0);
            }
        }
    }

    @FXML
    private void onKeyPressedReload(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            if (!status) {
                fillArticlesTable((short) 1, "", 0);
                if (!tvList.getItems().isEmpty()) {
                    tvList.getSelectionModel().select(0);
                }
            }
        }
    }

    @FXML
    private void onActionSearch(ActionEvent event) {
        if (!tvList.getItems().isEmpty()) {
            tvList.requestFocus();
            tvList.getSelectionModel().select(0);
        }
    }

    @FXML
    private void onKeyReleasedSearch(KeyEvent event) {
        if (event.getCode() != KeyCode.ESCAPE
                && event.getCode() != KeyCode.F1
                && event.getCode() != KeyCode.F2
                && event.getCode() != KeyCode.F3
                && event.getCode() != KeyCode.F4
                && event.getCode() != KeyCode.F5
                && event.getCode() != KeyCode.F6
                && event.getCode() != KeyCode.F7
                && event.getCode() != KeyCode.F8
                && event.getCode() != KeyCode.F9
                && event.getCode() != KeyCode.F10
                && event.getCode() != KeyCode.F11
                && event.getCode() != KeyCode.F12
                && event.getCode() != KeyCode.ALT
                && event.getCode() != KeyCode.CONTROL
                && event.getCode() != KeyCode.UP
                && event.getCode() != KeyCode.DOWN
                && event.getCode() != KeyCode.RIGHT
                && event.getCode() != KeyCode.LEFT
                && event.getCode() != KeyCode.TAB
                && event.getCode() != KeyCode.CAPS
                && event.getCode() != KeyCode.SHIFT
                && event.getCode() != KeyCode.HOME
                && event.getCode() != KeyCode.WINDOWS
                && event.getCode() != KeyCode.ALT_GRAPH
                && event.getCode() != KeyCode.CONTEXT_MENU
                && event.getCode() != KeyCode.END
                && event.getCode() != KeyCode.INSERT
                && event.getCode() != KeyCode.PAGE_UP
                && event.getCode() != KeyCode.PAGE_DOWN
                && event.getCode() != KeyCode.NUM_LOCK
                && event.getCode() != KeyCode.PRINTSCREEN
                && event.getCode() != KeyCode.SCROLL_LOCK
                && event.getCode() != KeyCode.PAUSE
                && event.getCode() != KeyCode.ENTER) {
            if (!status) {
                fillArticlesTable((short) 1, txtSearch.getText().trim(), 0);
            }
        }
    }

    @FXML
    private void onKeyReleasedSearchCode(KeyEvent event) {
        if (event.getCode() != KeyCode.ESCAPE
                && event.getCode() != KeyCode.F1
                && event.getCode() != KeyCode.F2
                && event.getCode() != KeyCode.F3
                && event.getCode() != KeyCode.F4
                && event.getCode() != KeyCode.F5
                && event.getCode() != KeyCode.F6
                && event.getCode() != KeyCode.F7
                && event.getCode() != KeyCode.F8
                && event.getCode() != KeyCode.F9
                && event.getCode() != KeyCode.F10
                && event.getCode() != KeyCode.F11
                && event.getCode() != KeyCode.F12
                && event.getCode() != KeyCode.ALT
                && event.getCode() != KeyCode.CONTROL
                && event.getCode() != KeyCode.UP
                && event.getCode() != KeyCode.DOWN
                && event.getCode() != KeyCode.RIGHT
                && event.getCode() != KeyCode.LEFT
                && event.getCode() != KeyCode.TAB
                && event.getCode() != KeyCode.CAPS
                && event.getCode() != KeyCode.SHIFT
                && event.getCode() != KeyCode.HOME
                && event.getCode() != KeyCode.WINDOWS
                && event.getCode() != KeyCode.ALT_GRAPH
                && event.getCode() != KeyCode.CONTEXT_MENU
                && event.getCode() != KeyCode.END
                && event.getCode() != KeyCode.INSERT
                && event.getCode() != KeyCode.PAGE_UP
                && event.getCode() != KeyCode.PAGE_DOWN
                && event.getCode() != KeyCode.NUM_LOCK
                && event.getCode() != KeyCode.PRINTSCREEN
                && event.getCode() != KeyCode.SCROLL_LOCK
                && event.getCode() != KeyCode.PAUSE
                && event.getCode() != KeyCode.ENTER) {
            if (!status) {
                fillArticlesTable((short) 2, txtSearchCode.getText().trim(), 0);
            }
        }
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
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            ArticuloTB articuloTB = tvList.getSelectionModel().getSelectedItem();
            seleccionadoController.getIvPrincipal().setImage(new Image(articuloTB.getImagenTB().equalsIgnoreCase("")
                    ? "/view/no-image.png"
                    : new File(articuloTB.getImagenTB()).toURI().toString()));
            seleccionadoController.getLblName().setText(articuloTB.getNombreMarca());
            seleccionadoController.getLblPrice().setText(Session.MONEDA + " " + Tools.roundingValue(articuloTB.getPrecioVentaGeneral(), 2));
            seleccionadoController.getLblQuantity().setText(Tools.roundingValue(articuloTB.getCantidad(), 2)+" "+articuloTB.getUnidadCompraName());

//            if (detalleController != null) {
//                detalleController.getTvList().setItems(LoteADO.ListByIdLote(tvList.getSelectionModel().getSelectedItem().getIdArticulo()));
//            }
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
    private void onKeyPressedCantidad(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewArticuloUpdateStock();
        }
    }

    @FXML
    private void onActionCantidad(ActionEvent event) throws IOException {
        onViewArticuloUpdateStock();
    }

    @FXML
    private void onKeyPressedRemove(KeyEvent event) {
        removedArticulo();
    }

    @FXML
    private void onActionRemove(ActionEvent event) {
        removedArticulo();
    }

    @FXML
    private void onActionCategoria(ActionEvent event) {
        if (cbCategoria.getSelectionModel().getSelectedIndex() >= 1) {
            if (!status) {
                fillArticlesTable((short) 0, "", cbCategoria.getSelectionModel().getSelectedItem().getIdDetalle().get());
            }
        }
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
