package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ArticuloTB;
import model.ComprobanteADO;
import model.DetalleADO;
import model.DetalleTB;

public class FxVentaController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private ComboBox<DetalleTB> cbComprobante;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, String> tcArticulo;
    @FXML
    private TableColumn<ArticuloTB, String> tcCantidad;
    @FXML
    private TableColumn<ArticuloTB, String> tcPrecio;
    @FXML
    private TableColumn<ArticuloTB, String> tcDescuento;
    @FXML
    private TableColumn<ArticuloTB, String> tcImporte;
    @FXML
    private Text lblSubTotal;
    @FXML
    private Text lblDescuento;
    @FXML
    private Text lblGravada;
    @FXML
    private Text lblIgv;
    @FXML
    private Text lblTotal;
    @FXML
    private TextField txtArticulo;
    @FXML
    private Text lblSerie;
    @FXML
    private Text lblNumeracion;
    @FXML
    private TextField txtCliente;
    @FXML
    private Text lblTotalPagar;

    private AnchorPane content;

    private double subTotal;

    private double descuento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        window.setOnKeyPressed((KeyEvent event) -> {
            try {
                if (event.getCode() == KeyCode.F5) {
                    openWindowGranel("Cambiar precio al Artículo",false);
                } else if (event.getCode() == KeyCode.F7) {
                    openWindowGranel("Sumar precio al Artículo",true);
                }
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }

        });
        initTable();
    }

    public void loadWindow() {
        DetalleADO.GetDetailIdName("2", "0009", "").forEach(e -> {
            cbComprobante.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        cbComprobante.getSelectionModel().select(0);
        txtCliente.setText(Session.DATOSCLIENTE);
        txtArticulo.requestFocus();

        String[] array = ComprobanteADO.GetSerieNumeracion().split("-");
        lblSerie.setText(array[0]);
        lblNumeracion.setText(array[1]);

    }

    private void initTable() {
        tcArticulo.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClave().get() + "\n" + cellData.getValue().getNombreMarca().get()
        ));
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getCantidad(), 2)));
        tcPrecio.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getPrecioVenta(), 2)));
        tcDescuento.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getDescuento().get(), 2)));
        tcImporte.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getImporte().get(), 2)));
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
        controller.setInitVentasController(this);
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

    private void openWindowVentaProceso() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_VENTAPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxVentaProcesoController controller = fXMLLoader.getController();
        controller.setInitVentasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Completar la venta", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
    }

    private void openWindowCliente() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_CLIENTELISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxClienteListaController controller = fXMLLoader.getController();
        controller.setInitVentasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Elija un cliente", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
        controller.fillCustomersTable("");
    }

    private void openWindowDescuento() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_VENTADESCUENTO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxVentaDescuentoController controller = fXMLLoader.getController();
            controller.setInitVentasController(this);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Descuento del Artículo", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();
            controller.initComponents(tvList.getSelectionModel().getSelectedItem(), tvList.getSelectionModel().getSelectedIndex());
        } else {
            tvList.requestFocus();
        }

    }

    private void openWindowGranel(String title, boolean opcion) throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_VENTAGRANEL);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxVentaGranelController controller = fXMLLoader.getController();
            controller.setInitVentasController(this);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, title, window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();
            controller.initComponents(
                    title,
                    tvList.getSelectionModel().getSelectedItem().getNombreMarca().get(),
                    opcion
            );
        } else {
            tvList.requestFocus();
        }

    }

    public void getAddArticulo(ArticuloTB articuloTB) {
        tvList.getItems().add(articuloTB);
        calculateTotales();
    }

    public void resetVenta() {
        String[] array = ComprobanteADO.GetSerieNumeracion().split("-");
        lblSerie.setText(array[0]);
        lblNumeracion.setText(array[1]);
    }

    @FXML
    private void onKeyPressedRegister(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowVentaProceso();
        }
    }

    @FXML
    private void onActionDescuento(ActionEvent event) throws IOException {
        openWindowDescuento();
    }

    @FXML
    private void onActionRegister(ActionEvent event) throws IOException {
        openWindowVentaProceso();
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        openWindowArticulos();
    }

    @FXML
    private void onActionRemover(ActionEvent event) {

    }

    @FXML
    private void onActionCancel(ActionEvent event) {

    }

    @FXML
    private void onActionVendedor(ActionEvent event) {

    }

    @FXML
    private void onActionCliente(ActionEvent event) throws IOException {
        openWindowCliente();
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {

    }

    @FXML
    private void onKeyReleasedList(KeyEvent event) {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {

            if (event.getCode() == KeyCode.PLUS || event.getCode() == KeyCode.ADD) {
                ObservableList<ArticuloTB> articuloTBs;
                articuloTBs = tvList.getSelectionModel().getSelectedItems();
                int index = tvList.getSelectionModel().getSelectedIndex();
                articuloTBs.forEach(e -> {
                    ArticuloTB articuloTB = new ArticuloTB();
                    articuloTB.setIdArticulo(e.getIdArticulo());
                    articuloTB.setClave(e.getClave().get());
                    articuloTB.setNombreMarca(e.getNombreMarca().get());
                    articuloTB.setCantidad(e.getCantidad() + 1);
                    articuloTB.setPrecioVenta(e.getPrecioVenta());
                    articuloTB.setDescuento(e.getDescuento().get());
                    articuloTB.setSubTotal(articuloTB.getCantidad() * e.getPrecioVenta());
                    articuloTB.setImporte(
                            articuloTB.getSubTotal().get()
                            - articuloTB.getDescuento().get()
                    );

                    tvList.getItems().set(index, articuloTB);
                    tvList.getSelectionModel().select(index);
                    calculateTotales();
                });

            } else if (event.getCode() == KeyCode.MINUS || event.getCode() == KeyCode.SUBTRACT) {
                ObservableList<ArticuloTB> articuloTBs;
                articuloTBs = tvList.getSelectionModel().getSelectedItems();
                int index = tvList.getSelectionModel().getSelectedIndex();

                articuloTBs.forEach(e -> {
                    ArticuloTB articuloTB = new ArticuloTB();
                    articuloTB.setIdArticulo(e.getIdArticulo());
                    articuloTB.setClave(e.getClave().get());
                    articuloTB.setNombreMarca(e.getNombreMarca().get());
                    articuloTB.setCantidad(e.getCantidad() - 1);
                    articuloTB.setPrecioVenta(e.getPrecioVenta());
                    if (articuloTB.getCantidad() < 1) {
                        return;
                    }
                    articuloTB.setDescuento(e.getDescuento().get());
                    articuloTB.setSubTotal(articuloTB.getCantidad() * e.getPrecioVenta());
                    articuloTB.setImporte(
                            articuloTB.getSubTotal().get()
                            - articuloTB.getDescuento().get()
                    );
                    tvList.getItems().set(index, articuloTB);
                    tvList.getSelectionModel().select(index);
                    calculateTotales();
                });
            }
        }
    }

    public void calculateTotales() {
        double subTotalInterno, descuentoInterno;

        tvList.getItems().forEach(e -> subTotal += e.getSubTotal().get());
        lblSubTotal.setText(Tools.roundingValue(subTotal, 2));
        subTotalInterno = subTotal;
        subTotal = 0;

        tvList.getItems().forEach(e -> descuento += e.getDescuento().get());
        lblDescuento.setText(Tools.roundingValue(descuento, 2));
        descuentoInterno = descuento;
        descuento = 0;

        double total = subTotalInterno - descuentoInterno;
        lblTotal.setText(Tools.roundingValue(total, 2));
        lblTotalPagar.setText(Tools.roundingValue(total, 2));

        double gravada = Tools.calculateValueNeto(Session.IMPUESTO, total);
        lblGravada.setText(Tools.roundingValue(gravada, 2));

        double impuesto = Tools.calculateTax(Session.IMPUESTO, gravada);
        lblIgv.setText(Tools.roundingValue(impuesto, 2));

    }

    @FXML
    private void onKeyPressedPrecio(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowGranel("Cambiar precio al Artículo",false);
        }
    }

    @FXML
    private void onActionPrecio(ActionEvent event) throws IOException {
        openWindowGranel("Cambiar precio al Artículo",false);
    }

    @FXML
    private void onKeyPressedPrecioSumar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowGranel("Sumar precio al Artículo",true);
        }
    }

    @FXML
    private void onActionPrecioSumar(ActionEvent event) throws IOException {
        openWindowGranel("Sumar precio al Artículo",true);
    }

    void setContent(AnchorPane content) {
        this.content = content;
    }

}
