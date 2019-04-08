package controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.ArticuloADO;
import model.ArticuloTB;

public class FxArticuloListaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, Integer> tcId;
    @FXML
    private TableColumn<ArticuloTB, String> tcArticulo;
    @FXML
    private TableColumn<ArticuloTB, String> tcMarca;
    @FXML
    private TableColumn<ArticuloTB, String> tcExistencias;
    @FXML
    private TableColumn<ArticuloTB, String> tcPrecio;
    @FXML
    private TableColumn<ArticuloTB, String> tcUnidadVenta;

    private boolean stateRequest;

    private FxCompraController compraController;

    private FxVentaController ventaController;

    private FxArticuloHistorialController articuloHistorialController;

    private FxArticuloReportesController articuloReportesController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);

        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcArticulo.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClave() + "\n" + cellData.getValue().getNombreMarca()
        )
        );
        tcMarca.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getMarcaName()));
        tcExistencias.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getCantidad(), 2)));
        tcPrecio.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getPrecioVentaGeneral(), 2)));
        tcUnidadVenta.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getUnidadVenta() == 1 ? "Por Unidad/Pza" : "A Granel"
        ));
        stateRequest = false;
    }

    public void fillProvidersTable(String value) {
        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<ObservableList<ArticuloTB>> task = new Task<ObservableList<ArticuloTB>>() {
            @Override
            public ObservableList<ArticuloTB> call() {
                return ArticuloADO.ListArticulosListaView(value);
            }
        };

        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems(task.getValue());
            stateRequest = true;
        });

        task.setOnFailed((WorkerStateEvent e) -> {
            stateRequest = false;
        });
        task.setOnScheduled((WorkerStateEvent e) -> {
            stateRequest = false;
        });
        exec.execute(task);
        if (!exec.isShutdown()) {
            exec.shutdown();
        }
    }

    private void openWindowAddArticulo() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_ARTICULOPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxArticuloProcesoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Artículo", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        controller.setInitArticulo();
    }

    private void openWindowCompra() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            try {
                URL url = getClass().getResource(Tools.FX_FILE_ARTICULOCOMPRA);
                FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
                Parent parent = fXMLLoader.load(url.openStream());
                //Controlller here
                FxArticuloCompraController controller = fXMLLoader.getController();
                controller.setInitCompraController(compraController);
                controller.setLoadData(new String[]{
                    tvList.getSelectionModel().getSelectedItem().getIdArticulo(),
                    tvList.getSelectionModel().getSelectedItem().getClave(),
                    tvList.getSelectionModel().getSelectedItem().getNombreMarca(),
                    "" + tvList.getSelectionModel().getSelectedItem().getPrecioCompra(),
                    "" + tvList.getSelectionModel().getSelectedItem().getPrecioVentaGeneral(),
                    "" + tvList.getSelectionModel().getSelectedItem().getPrecioMargenGeneral(),
                    "" + tvList.getSelectionModel().getSelectedItem().getPrecioUtilidadGeneral(),
                    "" + tvList.getSelectionModel().getSelectedItem().getImpuestoArticulo()
                },
                        tvList.getSelectionModel().getSelectedItem().isLote()
                );
                //
                Stage stage = FxWindow.StageLoaderModal(parent, "Agregar artículo", window.getScene().getWindow());
                stage.setResizable(false);
                stage.sizeToScene();
                stage.show();
            } catch (IOException ix) {
                System.out.println("Error Articulo Lista Controller:" + ix.getLocalizedMessage());
            }

        }
    }

    private void addArticuloToList() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            ArticuloTB articuloTB = new ArticuloTB();
            articuloTB.setIdArticulo(tvList.getSelectionModel().getSelectedItem().getIdArticulo());
            articuloTB.setClave(tvList.getSelectionModel().getSelectedItem().getClave());
            articuloTB.setNombreMarca(tvList.getSelectionModel().getSelectedItem().getNombreMarca());
            articuloTB.setCantidad(1);

            articuloTB.setDescuento(0);
            articuloTB.setDescuentoSumado(0);

            articuloTB.setPrecioVentaGeneralReal(tvList.getSelectionModel().getSelectedItem().getPrecioVentaGeneral());
            articuloTB.setPrecioVentaGeneral(tvList.getSelectionModel().getSelectedItem().getPrecioVentaGeneral());

            articuloTB.setSubImporte(1 * tvList.getSelectionModel().getSelectedItem().getPrecioVentaGeneral());
            articuloTB.setTotalImporte(1 * tvList.getSelectionModel().getSelectedItem().getPrecioVentaGeneral());

            articuloTB.setInventario(tvList.getSelectionModel().getSelectedItem().isInventario());
            articuloTB.setUnidadVenta(tvList.getSelectionModel().getSelectedItem().getUnidadVenta());

            articuloTB.setImpuestoArticulo(tvList.getSelectionModel().getSelectedItem().getImpuestoArticulo());
            articuloTB.setImpuestoArticuloName(ventaController.getTaxName(tvList.getSelectionModel().getSelectedItem().getImpuestoArticulo()));
            articuloTB.setImpuestoValor(ventaController.getTaxValue(tvList.getSelectionModel().getSelectedItem().getImpuestoArticulo()));
            articuloTB.setImpuestoSumado(articuloTB.getCantidad() * (articuloTB.getPrecioVentaGeneral()* (articuloTB.getImpuestoValor() / 100.00)));

            Tools.Dispose(window);
            ventaController.getAddArticulo(articuloTB);
        }
    }

    private void executeEventObject() {
        if (compraController != null) {
            openWindowCompra();
            txtSearch.requestFocus();
        } else if (ventaController != null) {
            addArticuloToList();
        } else if (articuloHistorialController != null) {
            if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
                articuloHistorialController.getElemetsArticulo(
                        new String[]{tvList.getSelectionModel().getSelectedItem().getIdArticulo(),
                            tvList.getSelectionModel().getSelectedItem().getClave(),
                            tvList.getSelectionModel().getSelectedItem().getNombreMarca(),
                            "" + tvList.getSelectionModel().getSelectedItem().getCantidad(),
                            "" + tvList.getSelectionModel().getSelectedItem().getPrecioVentaGeneral()});
                Tools.Dispose(window);
            }

        } else if (articuloReportesController != null) {
            if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
                ArticuloTB articuloTB = new ArticuloTB();
                articuloTB.setClave(tvList.getSelectionModel().getSelectedItem().getClave());
                articuloTB.setNombreMarca(tvList.getSelectionModel().getSelectedItem().getNombreMarca());
                articuloTB.setUnidadVenta(tvList.getSelectionModel().getSelectedItem().getUnidadVenta());
                articuloReportesController.getTvList().getItems().add(articuloTB);
                Tools.Dispose(window);
            }

        }
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
        if (event.getClickCount() == 2) {
            executeEventObject();
        }
    }

    @FXML
    private void onKeyPressedList(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            executeEventObject();
        }
    }

    @FXML
    private void onKeyPressedAdd(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowAddArticulo();
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        openWindowAddArticulo();
    }

    @FXML
    private void onKeyReleasedToSearch(KeyEvent event) {
        if (stateRequest) {
            fillProvidersTable(txtSearch.getText());
        }
    }

    @FXML
    private void onKeyPressedToSearh(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!tvList.getItems().isEmpty()) {
                tvList.requestFocus();
                tvList.getSelectionModel().select(0);
            }
        }
    }

    @FXML
    private void onKeyPressedReload(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (stateRequest) {
                fillProvidersTable("");
            }
        }
    }

    @FXML
    private void onActionReload(ActionEvent event) {
        if (stateRequest) {
            fillProvidersTable("");
        }
    }

    public void setInitCompraController(FxCompraController comprasController) {
        this.compraController = comprasController;
    }

    public void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

    public void setInitArticuloHistorialController(FxArticuloHistorialController articuloHistorialController) {
        this.articuloHistorialController = articuloHistorialController;
    }

    public void setInitReporteArticuloController(FxArticuloReportesController articuloReportesController) {
        this.articuloReportesController = articuloReportesController;
    }

}
