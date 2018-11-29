package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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
import model.VentaTB;

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

    private String idCliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        window.setOnKeyReleased((KeyEvent event) -> {
            try {
                if (null != event.getCode()) {
                    switch (event.getCode()) {
                        case F5:
                            openWindowGranel("Cambiar precio al Artículo", false);
                            break;
                        case F7:
                            openWindowGranel("Sumar precio al Artículo", true);
                            break;
                        case F1:
                            openWindowVentaProceso();
                            break;
                        case F2:
                            openWindowArticulos();
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }

        });
        idCliente = "";
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
        if (!tvList.getItems().isEmpty()) {
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
            VentaTB ventaTB = new VentaTB();
            ventaTB.setCliente(idCliente);
            ventaTB.setVendedor(Session.USER_ID);
            ventaTB.setComprobante(cbComprobante.getSelectionModel().getSelectedIndex() >= 0
                    ? cbComprobante.getSelectionModel().getSelectedItem().getIdDetalle().get()
                    : 0
            );
            ventaTB.setSerie(lblSerie.getText());
            ventaTB.setNumeracion(lblNumeracion.getText());
            ventaTB.setFechaVenta(Timestamp.valueOf(Tools.getDate() + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
            ventaTB.setSubTotal(Double.parseDouble(lblSubTotal.getText()));
            ventaTB.setGravada(Double.parseDouble(lblGravada.getText()));
            ventaTB.setDescuento(Double.parseDouble(lblDescuento.getText()));
            ventaTB.setIgv(Double.parseDouble(lblIgv.getText()));
            ventaTB.setTotal(Double.parseDouble(lblTotalPagar.getText()));
            controller.setInitComponents(ventaTB, tvList);
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Ventas", "Debes agregar artículos a la venta", false);
        }

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
                    tvList.getSelectionModel().getSelectedItem(),
                    tvList.getSelectionModel().getSelectedIndex(),
                    opcion
            );
        } else {
            tvList.requestFocus();
        }

    }

    public void getAddArticulo(ArticuloTB articuloTB) {

        if (articuloTB.getUnidadVenta() == 2) {
            try {
                tvList.getItems().add(articuloTB);
                int index = tvList.getItems().size() - 1;
                tvList.requestFocus();
                tvList.getSelectionModel().select(index);

                openWindowGranel("Cambiar precio al Artículo", false);
            } catch (IOException ex) {
            }
        } else {
            tvList.getItems().add(articuloTB);
            calculateTotales();
        }

    }

    public void resetVenta() {
        String[] array = ComprobanteADO.GetSerieNumeracion().split("-");
        lblSerie.setText(array[0]);
        lblNumeracion.setText(array[1]);

        this.tvList.getItems().clear();
        lblTotal.setText("0.00");
        lblSubTotal.setText("0.00");
        lblDescuento.setText("0.00");
        lblGravada.setText("0.00");
        lblIgv.setText("0.00");
        lblTotalPagar.setText("0.00");
    }

    private void removeArticulo() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Venta", "¿Esta seguro de quitar el artículo?", true);
            if (confirmation == 1) {
                ObservableList<ArticuloTB> articuloSelect, allArticulos;
                allArticulos = tvList.getItems();
                articuloSelect = tvList.getSelectionModel().getSelectedItems();
                articuloSelect.forEach(allArticulos::remove);
                calculateTotales();
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Venta", "Seleccione un artículo para quitarlo", false);
        }
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
    private void onKeyPressedRemover(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionRemover(ActionEvent event) {
        removeArticulo();
    }

    @FXML
    private void onActionCancel(ActionEvent event) {
        removeArticulo();
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
                    articuloTB.setInventario(e.isInventario());
                    articuloTB.setUnidadVenta(e.getUnidadVenta());
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
                    articuloTB.setInventario(e.isInventario());
                    articuloTB.setUnidadVenta(e.getUnidadVenta());
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
            openWindowGranel("Cambiar precio al Artículo", false);
        }
    }

    @FXML
    private void onActionPrecio(ActionEvent event) throws IOException {
        openWindowGranel("Cambiar precio al Artículo", false);
    }

    @FXML
    private void onKeyPressedPrecioSumar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowGranel("Sumar precio al Artículo", true);
        }
    }

    @FXML
    private void onActionPrecioSumar(ActionEvent event) throws IOException {
        openWindowGranel("Sumar precio al Artículo", true);
    }

    public TableView<ArticuloTB> getTvList() {
        return tvList;
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
