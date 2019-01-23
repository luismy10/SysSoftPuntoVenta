package controller;

import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.ArticuloADO;
import model.ArticuloTB;
import model.DetalleADO;
import model.DetalleTB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class FxArticuloReportesController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblLoad;
    @FXML
    private ComboBox<String> cbUnidadVenta;
    @FXML
    private ComboBox<DetalleTB> cbCategoria;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, String> tcClave;
    @FXML
    private TableColumn<ArticuloTB, String> tcClaveAlterna;
    @FXML
    private TableColumn<ArticuloTB, String> tcDocument;
    @FXML
    private TableColumn<ArticuloTB, String> tcUnidadVenta;
    @FXML
    private TextField txtTitulo;

    private AnchorPane content;

    private boolean stateRequest;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcClave.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClave()
        ));
        tcClaveAlterna.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClaveAlterna()
        ));
        tcDocument.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getNombreMarca()
        ));
        tcUnidadVenta.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getUnidadVenta() == 1 ? "Por Unidad/Pza" : "A Granel(Peso)"
        ));
        stateRequest = true;
        cbUnidadVenta.getItems().addAll("Todos", "Por Unidad/Pza", "A Granel(Peso)");
        cbUnidadVenta.getSelectionModel().select(0);
        DetalleADO.GetDetailId("0006").forEach(e -> {
            cbCategoria.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
    }

    public void fillArticlesTable(int unidadVenta, int categoria) {

        ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<ObservableList<ArticuloTB>> task = new Task<ObservableList<ArticuloTB>>() {
            @Override
            public ObservableList<ArticuloTB> call() {
                return ArticuloADO.ListArticulosCodBar(unidadVenta, categoria);
            }
        };

        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems((ObservableList<ArticuloTB>) task.getValue());
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

    private void openWindowReporte(boolean validar) {
        if (txtTitulo.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Reporte artículos", "Ingrese el título del documento", false);
            txtTitulo.requestFocus();
        } else {
            try {
                ArrayList<ArticuloTB> list = new ArrayList();
                for (int i = 0; i < tvList.getItems().size(); i++) {
                    list.add(validar
                            ? new ArticuloTB(tvList.getItems().get(i).getClave(), tvList.getItems().get(i).getNombreMarca())
                            : new ArticuloTB(tvList.getItems().get(i).getClaveAlterna(), tvList.getItems().get(i).getNombreMarca(), true));
                }

                Map map = new HashMap();
                map.put("TITLE", txtTitulo.getText());

                JasperPrint jasperPrint = JasperFillManager.fillReport(FxArticuloReportesController.class.getResourceAsStream(validar ? "/report/GenerarCodBar.jasper" : "/report/GenerarClaveAlt.jasper"), map, new JRBeanCollectionDataSource(list));

                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setIconImage(new ImageIcon(getClass().getResource(Tools.FX_LOGO)).getImage());
                jasperViewer.setTitle("Lista de atículos");
                jasperViewer.setSize(840, 650);
                jasperViewer.setLocationRelativeTo(null);
                jasperViewer.setVisible(true);

            } catch (HeadlessException | JRException ex) {
                System.out.println("Error al generar el reporte : " + ex);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(FxCompraDetalleController.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    private void openWindowArticulos() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_ARTICULOLISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxArticuloListaController controller = fXMLLoader.getController();
        controller.setInitReporteArticuloController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Seleccione un Artículo", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
        controller.fillProvidersTable("");
    }

    @FXML
    private void onKeyPressedArticulo(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowArticulos();
        }
    }

    @FXML
    private void onActionArticulo(ActionEvent event) throws IOException {
        openWindowArticulos();
    }

    @FXML
    private void onKeyPressedGenerar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (stateRequest) {
                switch (cbUnidadVenta.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        fillArticlesTable(0,
                                cbCategoria.getSelectionModel().getSelectedIndex() >= 0
                                ? cbCategoria.getSelectionModel().getSelectedItem().getIdDetalle().get()
                                : 0
                        );
                        break;
                    case 1:
                        fillArticlesTable(1,
                                cbCategoria.getSelectionModel().getSelectedIndex() >= 0
                                ? cbCategoria.getSelectionModel().getSelectedItem().getIdDetalle().get()
                                : 0
                        );
                        break;
                    case 2:
                        fillArticlesTable(2,
                                cbCategoria.getSelectionModel().getSelectedIndex() >= 0
                                ? cbCategoria.getSelectionModel().getSelectedItem().getIdDetalle().get()
                                : 0
                        );
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @FXML
    private void onActionGenerar(ActionEvent event) {
        if (stateRequest) {
            switch (cbUnidadVenta.getSelectionModel().getSelectedIndex()) {
                case 0:
                    fillArticlesTable(0,
                            cbCategoria.getSelectionModel().getSelectedIndex() >= 0
                            ? cbCategoria.getSelectionModel().getSelectedItem().getIdDetalle().get()
                            : 0
                    );
                    break;
                case 1:
                    fillArticlesTable(1,
                            cbCategoria.getSelectionModel().getSelectedIndex() >= 0
                            ? cbCategoria.getSelectionModel().getSelectedItem().getIdDetalle().get()
                            : 0
                    );
                    break;
                case 2:
                    fillArticlesTable(2,
                            cbCategoria.getSelectionModel().getSelectedIndex() >= 0
                            ? cbCategoria.getSelectionModel().getSelectedItem().getIdDetalle().get()
                            : 0
                    );
                    break;
                default:
                    break;
            }
        }
    }

    @FXML
    private void onKeyPressedVizualizarAlterna(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!tvList.getItems().isEmpty()) {
                openWindowReporte(false);
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Reporte", "La lista está vacía", false);
            }
        }
    }

    @FXML
    private void onActionVizualizarAlterna(ActionEvent event) {
        if (!tvList.getItems().isEmpty()) {
            openWindowReporte(false);
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Reporte", "La lista está vacía", false);
        }
    }

    @FXML
    private void onKeyPressedVisualizar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (cbUnidadVenta.getSelectionModel().getSelectedIndex() >= 0) {
                if (!tvList.getItems().isEmpty()) {
                    openWindowReporte(true);
                } else {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Reporte", "La lista está vacía", false);
                }

            }
        }
    }

    @FXML
    private void onActionVisualizar(ActionEvent event) {
        if (cbUnidadVenta.getSelectionModel().getSelectedIndex() >= 0) {
            if (!tvList.getItems().isEmpty()) {
                openWindowReporte(true);
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Reporte", "La lista está vacía", false);
            }
        }
    }

    @FXML
    private void onKeyPressedQuitar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
                ObservableList<ArticuloTB> articuloSelect, allArticulos;
                allArticulos = tvList.getItems();
                articuloSelect = tvList.getSelectionModel().getSelectedItems();
                articuloSelect.forEach(allArticulos::remove);
            }
        }
    }

    @FXML
    private void onActionQuitar(ActionEvent event) {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            ObservableList<ArticuloTB> articuloSelect, allArticulos;
            allArticulos = tvList.getItems();
            articuloSelect = tvList.getSelectionModel().getSelectedItems();
            articuloSelect.forEach(allArticulos::remove);
        }
    }

    public TableView<ArticuloTB> getTvList() {
        return tvList;
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
