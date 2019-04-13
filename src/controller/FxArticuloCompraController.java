package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.ArticuloTB;
import model.ImpuestoADO;
import model.ImpuestoTB;
import model.LoteTB;

public class FxArticuloCompraController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtCosto;
    @FXML
    private Text lblClave;
    @FXML
    private Text lblDescripcion;
    @FXML
    private TextField txtDescuento;
    @FXML
    private ComboBox<ImpuestoTB> cbImpuesto;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtMargen;
    @FXML
    private TextField txtUtilidad;
    @FXML
    private TextField txtPrecioVentaAgregado;
    @FXML
    private TextField txtObservacion;

    private FxCompraController compraController;

    private boolean editarArticulo;

    private String idArticulo;

    private boolean lote;

    private boolean validarlote;

    private boolean loteedit;

    private int indexcompra;

    private ObservableList<LoteTB> loteTBs;

    private double cantidadinicial;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        editarArticulo = false;
        lote = loteedit = false;
        validarlote = false;
        txtMargen.setText("30");
        idArticulo = "";
        indexcompra = 0;
        cantidadinicial = 0;
        cargarComboBox();
    }

    public void cargarComboBox() {
        cbImpuesto.getItems().clear();
        ImpuestoADO.GetTipoImpuestoCombBox().forEach(e -> {
            this.cbImpuesto.getItems().add(new ImpuestoTB(e.getIdImpuesto(), e.getNombre(), e.getValor(), e.getPredeterminado()));
        });
    }

    private void openWindowLote(ArticuloTB articuloTB) throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_LOTEPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxLoteProcesoController controller = fXMLLoader.getController();
        controller.setLoteController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Lote", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        if (!loteedit) {
            controller.setLoadData(articuloTB.getIdArticulo(), articuloTB.getClave(),
                    articuloTB.getNombreMarca(),
                    String.valueOf(articuloTB.getCantidad()));
        } else {
            controller.setEditData(new String[]{articuloTB.getIdArticulo(), articuloTB.getClave(),
                articuloTB.getNombreMarca(),
                String.valueOf(articuloTB.getCantidad())},
                    loteTBs);
        }
    }

    public void setLoadData(String value[], boolean lote) {
        idArticulo = value[0];
        lblClave.setText(value[1]);
        lblDescripcion.setText(value[2]);
        txtCosto.setText("" + value[3]);
        txtPrecio.setText("" + value[4]);
        txtMargen.setText("" + value[5]);
        txtUtilidad.setText("" + value[6]);
        int impuesto = Integer.parseInt(value[7]);
        if (impuesto != 0) {
            for (int i = 0; i < cbImpuesto.getItems().size(); i++) {
                if (cbImpuesto.getItems().get(i).getIdImpuesto() == impuesto) {
                    cbImpuesto.getSelectionModel().select(i);
                    break;
                }
            }
        } else {
            cbImpuesto.getSelectionModel().select(0);
        }

        validarlote = lote;
        this.lote = lote;
    }

    public void setLoadEdit(ArticuloTB articuloTB, int index, ObservableList<LoteTB> loteTBs) {
        idArticulo = articuloTB.getIdArticulo();
        lblClave.setText(articuloTB.getClave());
        lblDescripcion.setText(articuloTB.getNombreMarca());
        txtCantidad.setText("" + articuloTB.getCantidad());
        txtCosto.setText(Tools.roundingValue(articuloTB.getPrecioCompraReal(), 2));
        txtDescuento.setText(Tools.roundingValue(articuloTB.getDescuento(), 2));

        txtPrecio.setText(Tools.roundingValue(articuloTB.getPrecioVentaGeneral(), 2));
        txtMargen.setText("" + articuloTB.getPrecioMargenGeneral());
        txtUtilidad.setText(Tools.roundingValue(articuloTB.getPrecioUtilidadGeneral(), 2));

        int impuesto = articuloTB.getImpuestoArticulo();
        if (impuesto != 0) {
            for (int i = 0; i < cbImpuesto.getItems().size(); i++) {
                if (cbImpuesto.getItems().get(i).getIdImpuesto() == impuesto) {
                    cbImpuesto.getSelectionModel().select(i);
                    break;
                }
            }
        } else {
            cbImpuesto.getSelectionModel().select(0);
        }
        editarArticulo = true;
        validarlote = articuloTB.isLote();
        lote = articuloTB.isLote();
        indexcompra = index;
        loteedit = true;
        this.loteTBs = loteTBs;
        cantidadinicial = articuloTB.getCantidad();
        txtDescuento.setText(articuloTB.getDescripcion());
    }

    private void addArticulo(double costo) throws IOException {
        ArticuloTB articuloTB = new ArticuloTB();
        articuloTB.setIdArticulo(idArticulo);
        articuloTB.setClave(lblClave.getText());
        articuloTB.setNombreMarca(lblDescripcion.getText());
        articuloTB.setCantidad(Double.parseDouble(txtCantidad.getText()));

        articuloTB.setDescuento(!Tools.isNumeric(txtDescuento.getText()) ? 0
                : Double.parseDouble(txtDescuento.getText()));

        double porcentajeDecimal = articuloTB.getDescuento() / 100.00;
        double porcentajeRestante = costo * porcentajeDecimal;

        articuloTB.setDescuentoSumado(porcentajeRestante * articuloTB.getCantidad());

        articuloTB.setPrecioCompra(costo - porcentajeRestante);
        articuloTB.setPrecioCompraReal(costo);

        articuloTB.setSubImporte(articuloTB.getCantidad() * articuloTB.getPrecioCompraReal());
        articuloTB.setTotalImporte(articuloTB.getCantidad() * articuloTB.getPrecioCompra());

        articuloTB.setImpuestoArticulo(cbImpuesto.getSelectionModel().getSelectedItem().getIdImpuesto());
        articuloTB.setImpuestoArticuloName(cbImpuesto.getSelectionModel().getSelectedItem().getNombre());
        articuloTB.setImpuestoValor(cbImpuesto.getSelectionModel().getSelectedItem().getValor());
        articuloTB.setImpuestoSumado(articuloTB.getCantidad() * (articuloTB.getPrecioCompra() * (articuloTB.getImpuestoValor() / 100.00)));

        articuloTB.setPrecioVentaGeneral(Double.parseDouble(txtPrecio.getText()));
        articuloTB.setPrecioMargenGeneral(Short.parseShort(txtMargen.getText()));
        articuloTB.setPrecioUtilidadGeneral(Double.parseDouble(txtUtilidad.getText()));

        articuloTB.setLote(lote);
        articuloTB.setDescripcion(txtObservacion.getText().isEmpty() ? "" : txtObservacion.getText());

        if (!validateStock(compraController.getTvList(), articuloTB) && !editarArticulo) {
            if (validarlote && cantidadinicial != Double.parseDouble(txtCantidad.getText())) {
                openWindowLote(articuloTB);
            } else {
                compraController.getTvList().getItems().add(articuloTB);
                compraController.calculateTotals();
                Tools.Dispose(window);
            }
        } else if (editarArticulo) {
            if (validarlote && cantidadinicial != Double.parseDouble(txtCantidad.getText())) {
                openWindowLote(articuloTB);
            } else {
                compraController.getTvList().getItems().set(indexcompra, articuloTB);
                compraController.calculateTotals();
                Tools.Dispose(window);
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ya hay un artículo con las mismas características.", false);
        }
    }

    private boolean validateStock(TableView<ArticuloTB> view, ArticuloTB articuloTB) throws IOException {
        boolean ret = false;
        for (int i = 0; i < view.getItems().size(); i++) {
            if (view.getItems().get(i).getClave().equals(articuloTB.getClave())) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public void addArticuloList() throws IOException {
        if (!Tools.isNumeric(txtCantidad.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico en la cantidad", false);
            txtCantidad.requestFocus();
        } else if (Double.parseDouble(txtCantidad.getText()) <= 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "La cantidad no puede ser menor o igual a 0", false);
            txtCantidad.requestFocus();
        } else if (cbImpuesto.getSelectionModel().getSelectedIndex() < 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Seleccione el impuesto", false);
            cbImpuesto.requestFocus();
        } else if (!Tools.isNumeric(txtCosto.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico en el costo", false);
            txtCosto.requestFocus();
        } else if (Double.parseDouble(txtCosto.getText()) <= 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "El costo no puede ser menor o igual a 0", false);
            txtCosto.requestFocus();
        } else if (!Tools.isNumeric(txtPrecio.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico en el precio 1", false);
            txtPrecio.requestFocus();
        } else if (Double.parseDouble(txtPrecio.getText()) <= 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "El precio de venta 1 no puede ser menor o igual a 0", false);
            txtPrecio.requestFocus();
        } else {
            addArticulo(Double.parseDouble(txtCosto.getText()));
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        addArticuloList();
    }

    @FXML
    private void onKeyPressedAdd(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            addArticuloList();
        }
    }

    private void onKeyPressedPreviousPrices(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionCancel(ActionEvent event) {
        Tools.Dispose(window);
    }

    @FXML
    private void onKeyPressedCancel(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onKeyTypedCantidad(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtCantidad.getText().contains(".") || c == '-' && txtCantidad.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedCosto(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtCosto.getText().contains(".") || c == '-' && txtCosto.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedDescuento(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.')) {
            event.consume();
        }
        if (c == '.' && txtDescuento.getText().contains(".")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedMargen(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b')) {
            event.consume();
        }
    }

    @FXML
    private void onKeyReleasedCosto(KeyEvent event) {
        if (Tools.isNumeric(txtCosto.getText()) && Tools.isNumeric(txtMargen.getText())) {
            //toma el valor del impuesto del combo box

            double costo = Double.parseDouble(txtCosto.getText());
            int margen = Integer.parseInt(txtMargen.getText());

            double precio = Tools.calculateAumento(margen, costo);

            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    precio);
            double precioimpuesto = (precio + impuesto);

            txtPrecio.setText(Tools.roundingValue(precio, 2));
            txtUtilidad.setText(Tools.roundingValue((precio - costo), 2));
            txtPrecioVentaAgregado.setText(Tools.roundingValue(precioimpuesto, 2));

        }
    }

    @FXML
    private void onActionImpuesto(ActionEvent event) {
        if (Tools.isNumeric(txtCosto.getText()) && Tools.isNumeric(txtMargen.getText())) {
            //toma el valor del impuesto del combo box

            double costo = Double.parseDouble(txtCosto.getText());
            int margen = Integer.parseInt(txtMargen.getText());

            double precio = Tools.calculateAumento(margen, costo);

            double porcentaje = (precio * 100.00) / costo;
            int recalculado = (int) Math.abs(100
                    - Double.parseDouble(
                            Tools.roundingValue(Double.parseDouble(
                                    Tools.roundingValue(porcentaje, 2)), 0)));
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    precio);
            double precioimpuesto = (precio + impuesto);

            txtMargen.setText(String.valueOf(recalculado));
            txtUtilidad.setText(Tools.roundingValue((precio - costo), 2));
            txtPrecioVentaAgregado.setText(Tools.roundingValue(precioimpuesto, 2));

        }
    }

    @FXML
    private void onKeyRealesdPrecio(KeyEvent event) {
        if (Tools.isNumeric(txtPrecio.getText()) && Tools.isNumeric(txtCosto.getText())) {
            double costo = Double.parseDouble(txtCosto.getText());
            double precio = Double.parseDouble(txtPrecio.getText());

            double porcentaje = (precio * 100.00) / costo;

            int recalculado = (int) Math.abs(100
                    - Double.parseDouble(
                            Tools.roundingValue(Double.parseDouble(
                                    Tools.roundingValue(porcentaje, 2)), 0)));

            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    precio);

            double precioimpuesto = (precio + impuesto);

            txtMargen.setText(String.valueOf(recalculado));
            txtUtilidad.setText(Tools.roundingValue((precio - costo), 2));
            txtPrecioVentaAgregado.setText(Tools.roundingValue(precioimpuesto, 2));
        }
    }

    @FXML
    private void onKeyTypedPrecio(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecio.getText().contains(".") || c == '-' && txtPrecio.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyReleasedMargen(KeyEvent event) {
        if (Tools.isNumeric(txtMargen.getText()) && Tools.isNumeric(txtCosto.getText())) {
            //toma el valor del impuesto del combo box
            double costo = Double.parseDouble(txtCosto.getText());
            int margen = Integer.parseInt(txtMargen.getText());

            double precio = Tools.calculateAumento(margen, costo);

            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    precio);
            double precioimpuesto = (precio + impuesto);

            txtPrecio.setText(Tools.roundingValue(precio, 2));
            txtUtilidad.setText(Tools.roundingValue((precio - costo), 2));
            txtPrecioVentaAgregado.setText(Tools.roundingValue(precioimpuesto, 2));
        }
    }

    public void setValidarlote(boolean validarlote) {
        this.validarlote = validarlote;
    }

    public void setCantidadInicial(double cantidadinicial) {
        this.cantidadinicial = cantidadinicial;
    }

    public void setInitCompraController(FxCompraController compraController) {
        this.compraController = compraController;
    }

    public FxCompraController getCompraController() {
        return compraController;
    }
}
