package controller;

import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.DBUtil;
import model.LoteADO;
import model.LoteTB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

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
    private ComboBox<String> cbEstado;
    @FXML
    private Text lblCaducados;
    @FXML
    private Text lblPorCaducar;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        tcNumeroLote.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNumeroLote()));
        tcArticulo.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getArticuloTB().getClave() + "\n" + cellData.getValue().getArticuloTB().getNombreMarca()));
        tcCaducidad.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaCaducidad().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        tcActual.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getExistenciaActual()));

        tcCaducidad.setCellFactory((TableColumn<LoteTB, String> column) -> {
            return new TableCell<LoteTB, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);

                        if (LocalDate.parse(item).isBefore(LocalDate.parse(Tools.getDate()))) {
                            setStyle("-fx-text-fill: #d00c0c;-fx-alignment: CENTER-LEFT;");
                        } else if (LocalDate.parse(item).isAfter(LocalDate.parse(Tools.getDate()))) {
                            setStyle("-fx-text-fill: #19790e;-fx-alignment: CENTER-LEFT;");
                        } else {
                            setStyle("-fx-text-fill: #000000;-fx-alignment: CENTER-LEFT;");
                        }
                    }
                }
            };

        });
        cbEstado.getItems().addAll("Todos", "Proximos a caducar", "Caducados");
        cbEstado.getSelectionModel().select(0);
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

    public void loadDataInitial() {
        try {
            DBUtil.dbConnect();
            Callable<String> callable = () -> LoteADO.GetTotalCaducados();
            Callable<String> callable1 = () -> LoteADO.GetTotalPorCaducar();
            Callable<List<LoteTB>> callable2 = () -> LoteADO.ListLote((short) 0, "");
            ExecutorService service = Executors.newFixedThreadPool(3, (runnable) -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });
            Future<String> future = service.submit(callable);
            Future<String> future1 = service.submit(callable1);
            Future<List<LoteTB>> future2 = service.submit(callable2);
            while (!future.isDone() && future1.isDone() && future2.isDone()) {
                lblLoad.setVisible(true);
            }

            lblLoad.setVisible(false);
            lblCaducados.setText("Lotes caducados - " + future.get());
            lblPorCaducar.setText("Lotes proximos a caducarse a 2 semanas - " + future1.get());
            tvList.setItems((ObservableList<LoteTB>) future2.get());
            if (!service.isShutdown()) {
                service.shutdown();
            }
            DBUtil.dbDisconnect();

        } catch (InterruptedException | ExecutionException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void fillLoteTable(short opcion, String value) {
        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<List<LoteTB>> task = new Task<List<LoteTB>>() {
            @Override
            public ObservableList<LoteTB> call() {
                return LoteADO.ListLote(opcion, value);
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
            content.getChildren().remove(Session.PANE);
        });
        stage.show();

    }

    private void onViewReporte() {
        try {
            ArrayList<LoteTB> list = new ArrayList();
            for (int i = 0; i < tvList.getItems().size(); i++) {
                list.add(new LoteTB(
                        tvList.getItems().get(i).getId(),
                        tvList.getItems().get(i).getNumeroLote(),
                        tvList.getItems().get(i).getArticuloTB().getClave() + "\n" + tvList.getItems().get(i).getArticuloTB().getNombreMarca(),
                        tvList.getItems().get(i).getFechaCaducidad().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        Tools.roundingValue(tvList.getItems().get(i).getExistenciaActual(), 2)));
            }

            Map map = new HashMap();
            map.put("TIPOFILTRO", String.valueOf(cbEstado.getSelectionModel().getSelectedItem()));

            JasperPrint jasperPrint = JasperFillManager.fillReport(FxArticuloReportesController.class.getResourceAsStream("/report/ListarLotes.jasper"), map, new JRBeanCollectionDataSource(list));

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setIconImage(new ImageIcon(getClass().getResource(Tools.FX_LOGO)).getImage());
            jasperViewer.setTitle("Lista de Lote(s)");
            jasperViewer.setSize(840, 650);
            jasperViewer.setLocationRelativeTo(null);
            jasperViewer.setVisible(true);

        } catch (HeadlessException | JRException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println("Error al generar el reporte : " + ex);
        }
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
        fillLoteTable((short) 0, txtSearch.getText().trim());
    }

    @FXML
    private void onActionReload(ActionEvent event) {
        fillLoteTable((short) 0, "");
    }

    @FXML
    private void onActionReporte(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onViewReporte();
        }
    }

    @FXML
    private void onActionReporte(ActionEvent event) {
        onViewReporte();
    }

    @FXML
    private void onActionEstado(ActionEvent event) {
        switch (cbEstado.getSelectionModel().getSelectedIndex()) {
            case 0:
                fillLoteTable((short) 0, "");
                break;
            case 1:
                fillLoteTable((short) 1, "");
                break;

            case 2:
                fillLoteTable((short) 2, "");
                break;
        }
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
