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
    private TextField txtPrecioVenta1;
    @FXML
    private TextField txtMargen1;
    @FXML
    private TextField txtUtilidad1;
    @FXML
    private TextField txtPrecioVenta2;
    @FXML
    private TextField txtPrecioVenta3;
    @FXML
    private TextField txtMargen2;
    @FXML
    private TextField txtMargen3;
    @FXML
    private TextField txtUtilidad2;
    @FXML
    private TextField txtUtilidad3;

    private FxComprasController comprasController;

    private boolean editarArticulo;

    private String idArticulo;

    private boolean lote;

    private boolean validarlote;

    private boolean loteedit;

    private int indexcompra;

    private int unidadventa;

    private ObservableList<LoteTB> loteTBs;

    private double cantidadinicial;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        editarArticulo = false;
        lote = loteedit = false;
        validarlote = false;
        txtMargen1.setText("30");
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
        unidadventa = Integer.parseInt(value[3]);
        txtCosto.setText("" + Double.parseDouble(value[4]));

        txtPrecioVenta1.setText("" + Double.parseDouble(value[5]));
        txtMargen1.setText("" + Short.parseShort(value[6]));
        txtUtilidad1.setText("" + Double.parseDouble(value[7]));
        txtPrecioVenta2.setText("" + Double.parseDouble(value[8]));
        txtMargen2.setText("" + Short.parseShort(value[9]));
        txtUtilidad2.setText("" + Double.parseDouble(value[10]));
        txtPrecioVenta3.setText("" + Double.parseDouble(value[11]));
        txtMargen3.setText("" + Short.parseShort(value[12]));
        txtUtilidad3.setText("" + Double.parseDouble(value[13]));

        int impuesto = Integer.parseInt(value[14]);
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
        unidadventa = articuloTB.getUnidadVenta();
        txtCosto.setText(Tools.roundingValue(articuloTB.getPrecioCompraReal(), 2));
        txtDescuento.setText(Tools.roundingValue(articuloTB.getDescuento(), 2));

        txtPrecioVenta1.setText(Tools.roundingValue(articuloTB.getPrecioVenta(), 2));
        txtMargen1.setText("" + articuloTB.getMargen());
        txtUtilidad1.setText(Tools.roundingValue(articuloTB.getUtilidad(), 2));
        txtPrecioVenta2.setText(Tools.roundingValue(articuloTB.getPrecioVenta2(), 2));
        txtMargen2.setText("" + articuloTB.getMargen2());
        txtUtilidad2.setText(Tools.roundingValue(articuloTB.getUtilidad2(), 2));
        txtPrecioVenta3.setText(Tools.roundingValue(articuloTB.getPrecioVenta3(), 2));
        txtMargen3.setText("" + articuloTB.getMargen3());
        txtUtilidad3.setText(Tools.roundingValue(articuloTB.getUtilidad3(), 2));

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
    }

    private void addArticulo(double costo) throws IOException {
        ArticuloTB articuloTB = new ArticuloTB();
        articuloTB.setIdArticulo(idArticulo);
        articuloTB.setClave(lblClave.getText());
        articuloTB.setNombreMarca(lblDescripcion.getText());
        articuloTB.setCantidad(Double.parseDouble(txtCantidad.getText()));
        articuloTB.setUnidadVenta(unidadventa);

        articuloTB.setDescuento(!Tools.isNumeric(txtDescuento.getText()) ? 0
                : Double.parseDouble(txtDescuento.getText()));

        double porcentajeDecimal = articuloTB.getDescuento() / 100.00;
        double porcentajeRestante = costo * porcentajeDecimal;

        articuloTB.setDescuentoSumado(porcentajeRestante * articuloTB.getCantidad());

        articuloTB.setPrecioCompra(costo - porcentajeRestante);

        articuloTB.setPrecioCompraReal(costo);

        articuloTB.setSubImporte(articuloTB.getCantidad() * articuloTB.getPrecioCompraReal());
        articuloTB.setTotalImporte(articuloTB.getCantidad() * articuloTB.getPrecioCompra());

        articuloTB.setUtilidad(Tools.isNumeric(txtUtilidad1.getText())
                ? Double.parseDouble(txtUtilidad1.getText()) : 0
        );

        articuloTB.setImpuestoArticulo(cbImpuesto.getSelectionModel().getSelectedItem().getIdImpuesto());
        articuloTB.setImpuestoArticuloName(cbImpuesto.getSelectionModel().getSelectedItem().getNombre());
        articuloTB.setImpuestoValor(cbImpuesto.getSelectionModel().getSelectedItem().getValor());
        articuloTB.setImpuestoSumado(articuloTB.getCantidad() * (articuloTB.getPrecioCompra() * (articuloTB.getImpuestoValor() / 100.00)));

        articuloTB.setPrecioVenta(Double.parseDouble(txtPrecioVenta1.getText()));
        articuloTB.setMargen(Short.parseShort(txtMargen1.getText()));
        articuloTB.setUtilidad(Double.parseDouble(txtUtilidad1.getText()));
        articuloTB.setPrecioVenta2(Double.parseDouble(txtPrecioVenta2.getText()));
        articuloTB.setMargen2(Short.parseShort(txtMargen2.getText()));
        articuloTB.setUtilidad2(Double.parseDouble(txtUtilidad2.getText()));
        articuloTB.setPrecioVenta3(Double.parseDouble(txtPrecioVenta3.getText()));
        articuloTB.setMargen3(Short.parseShort(txtMargen3.getText()));
        articuloTB.setUtilidad3(Double.parseDouble(txtUtilidad3.getText()));


        articuloTB.setLote(lote);

        if (!validateStock(comprasController.getTvList(), articuloTB) && !editarArticulo) {
            if (validarlote && cantidadinicial != Double.parseDouble(txtCantidad.getText())) {
                openWindowLote(articuloTB);
            } else {
                comprasController.getTvList().getItems().add(articuloTB);
                comprasController.calculateTotals();
                Tools.Dispose(window);
            }

        } else if (editarArticulo) {
            if (validarlote && cantidadinicial != Double.parseDouble(txtCantidad.getText())) {
                openWindowLote(articuloTB);
            } else {
                comprasController.getTvList().getItems().set(indexcompra, articuloTB);
                comprasController.calculateTotals();
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
        } else if (cbImpuesto.getSelectionModel().getSelectedIndex() < 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Seleccione el impuesto", false);
            cbImpuesto.requestFocus();
        } else if (!Tools.isNumeric(txtCosto.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico en el costo", false);
            txtCosto.requestFocus();
        } else if (!Tools.isNumeric(txtPrecioVenta1.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico en el precio", false);
            txtPrecioVenta1.requestFocus();
        } else {
            addArticulo(Double.parseDouble(txtCosto.getText()));
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        addArticuloList();
    }

    @FXML
    private void onActionCancel(ActionEvent event) {
        Tools.Dispose(window);
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
    private void onKeyTypedPrecio(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecioVenta1.getText().contains(".") || c == '-' && txtPrecioVenta1.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyReleasedCosto(KeyEvent event) {

        if (Tools.isNumeric(txtCosto.getText()) && Tools.isNumeric(txtMargen1.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            //se suma el impuesto al costo del articulo
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta1.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen1.getText())
                    ? Double.parseDouble(txtMargen1.getText()) : 0,
                    preciocompra));
            txtUtilidad1.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta1.getText()) - preciocompra), 2));
        }

        if (Tools.isNumeric(txtCosto.getText()) && Tools.isNumeric(txtMargen2.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            //se suma el impuesto al costo del articulo
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta2.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen2.getText())
                    ? Double.parseDouble(txtMargen2.getText()) : 0,
                    preciocompra));
            txtUtilidad2.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta2.getText()) - preciocompra), 2));
        }

        if (Tools.isNumeric(txtCosto.getText()) && Tools.isNumeric(txtMargen3.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            //se suma el impuesto al costo del articulo
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta3.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen3.getText())
                    ? Double.parseDouble(txtMargen3.getText()) : 0,
                    preciocompra));
            txtUtilidad3.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta3.getText()) - preciocompra), 2));
        }

    }

    @FXML
    private void onKeyReleasedMargen(KeyEvent event) {
        if (Tools.isNumeric(txtMargen1.getText()) && Tools.isNumeric(txtCosto.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta1.setText(Tools.calculateAumento(Double.parseDouble(txtMargen1.getText()), preciocompra));
            txtUtilidad1.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta1.getText()) - preciocompra), 2));
        }
    }

    @FXML
    private void onKeyReleasedPrecio(KeyEvent event) {
        if (Tools.isNumeric(txtPrecioVenta1.getText()) && Tools.isNumeric(txtCosto.getText())) {
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);

            Double valor = precioimpuesto;
            Double precio = Double.parseDouble(txtPrecioVenta1.getText());
            Double porcentaje = (precio * 100) / valor;

            int recalculado = (int) Math.abs((100
                    - (Double.parseDouble(
                            Tools.roundingValue(Double.parseDouble(
                                    Tools.roundingValue(porcentaje, 2)), 0)))));

            txtMargen1.setText(String.valueOf(recalculado));
            txtUtilidad1.setText(Tools.roundingValue((precio - valor), 2));
        }
    }

    @FXML
    private void onActionImpuesto(ActionEvent event) {
        if (Tools.isNumeric(txtCosto.getText()) && Tools.isNumeric(txtMargen1.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            //se suma el impuesto al costo del articulo
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta1.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen1.getText())
                    ? Double.parseDouble(txtMargen1.getText()) : 0,
                    preciocompra));
            txtUtilidad1.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta1.getText()) - preciocompra), 2));
        }
        if (Tools.isNumeric(txtCosto.getText()) && Tools.isNumeric(txtMargen2.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            //se suma el impuesto al costo del articulo
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta2.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen2.getText())
                    ? Double.parseDouble(txtMargen2.getText()) : 0,
                    preciocompra));
            txtUtilidad2.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta2.getText()) - preciocompra), 2));
        }
        if (Tools.isNumeric(txtCosto.getText()) && Tools.isNumeric(txtMargen3.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            //se suma el impuesto al costo del articulo
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta3.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen3.getText())
                    ? Double.parseDouble(txtMargen3.getText()) : 0,
                    preciocompra));
            txtUtilidad3.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta3.getText()) - preciocompra), 2));
        }
    }

    public void setValidarlote(boolean validarlote) {
        this.validarlote = validarlote;
    }

    public void setCantidadInicial(double cantidadinicial) {
        this.cantidadinicial = cantidadinicial;
    }

    public void setInitCompraController(FxComprasController comprasController) {
        this.comprasController = comprasController;
    }

    public FxComprasController getComprasController() {
        return comprasController;
    }

    @FXML
    private void onKeyTypedPrecio2(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecioVenta2.getText().contains(".") || c == '-' && txtPrecioVenta2.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedPrecio3(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecioVenta3.getText().contains(".") || c == '-' && txtPrecioVenta3.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyReleasedPrecio2(KeyEvent event) {

        if (Tools.isNumeric(txtPrecioVenta2.getText()) && Tools.isNumeric(txtCosto.getText())) {
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);

            Double valor = precioimpuesto;
            Double precio = Double.parseDouble(txtPrecioVenta2.getText());
            Double porcentaje = (precio * 100) / valor;

            int recalculado = (int) Math.abs((100
                    - (Double.parseDouble(
                            Tools.roundingValue(Double.parseDouble(
                                    Tools.roundingValue(porcentaje, 2)), 0)))));

            txtMargen2.setText(String.valueOf(recalculado));
            txtUtilidad2.setText(Tools.roundingValue((precio - valor), 2));
        }

    }

    @FXML
    private void onKeyReleasedPrecio3(KeyEvent event) {

        if (Tools.isNumeric(txtPrecioVenta3.getText()) && Tools.isNumeric(txtCosto.getText())) {
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);

            Double valor = precioimpuesto;
            Double precio = Double.parseDouble(txtPrecioVenta3.getText());
            Double porcentaje = (precio * 100) / valor;

            int recalculado = (int) Math.abs((100
                    - (Double.parseDouble(
                            Tools.roundingValue(Double.parseDouble(
                                    Tools.roundingValue(porcentaje, 2)), 0)))));

            txtMargen3.setText(String.valueOf(recalculado));
            txtUtilidad3.setText(Tools.roundingValue((precio - valor), 2));
        }

    }

    @FXML
    private void onKeyTypedMargen2(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b')) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedMargen3(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b')) {
            event.consume();
        }
    }

    @FXML
    private void onKeyReleasedMargen2(KeyEvent event) {
        if (Tools.isNumeric(txtMargen2.getText()) && Tools.isNumeric(txtCosto.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta2.setText(Tools.calculateAumento(Double.parseDouble(txtMargen2.getText()), preciocompra));
            txtUtilidad2.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta2.getText()) - preciocompra), 2));
        }
    }

    @FXML
    private void onKeyReleasedMargen3(KeyEvent event) {
        if (Tools.isNumeric(txtMargen3.getText()) && Tools.isNumeric(txtCosto.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtCosto.getText()));
            double precioimpuesto = (Double.parseDouble(txtCosto.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta3.setText(Tools.calculateAumento(Double.parseDouble(txtMargen3.getText()), preciocompra));
            txtUtilidad3.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta3.getText()) - preciocompra), 2));
        }
    }

}
