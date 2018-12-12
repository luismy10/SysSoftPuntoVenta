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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.ArticuloTB;
import model.LoteTB;

public class FxArticuloCompraController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtImporte;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtCosto;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtUtilidad;
    @FXML
    private Text lblClave;
    @FXML
    private Text lblDescripcion;
    @FXML
    private CheckBox cbImpuesto;
    @FXML
    private TextField txtDescuento;
    @FXML
    private TextField txtMargen;
    @FXML
    private TextField txtMargenMayoreo;
    @FXML
    private TextField txtPrecioMayoreo;
    @FXML
    private TextField txtUtilidadMayoreo;

    private FxComprasController comprasController;

    private boolean validationelemnt;

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
        validationelemnt = false;
        lote = loteedit = false;
        validarlote = false;
        txtMargen.setText("30");
        txtMargenMayoreo.setText("20");
        idArticulo = "";
        indexcompra = 0;
        cantidadinicial = 0;
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
            controller.setLoadData(articuloTB.getIdArticulo(), articuloTB.getClave().get(),
                    articuloTB.getNombreMarca().get(),
                    String.valueOf(articuloTB.getCantidad()));
        } else {
            controller.setEditData(new String[]{articuloTB.getIdArticulo(), articuloTB.getClave().get(),
                articuloTB.getNombreMarca().get(),
                String.valueOf(articuloTB.getCantidad())},
                    loteTBs);
        }
    }

    public void setLoadData(String value[], boolean lote) {
        idArticulo = value[0];
        lblClave.setText(value[1]);
        lblDescripcion.setText(value[2]);
        unidadventa = Integer.parseInt(value[3]);
        validarlote = lote;
        this.lote = lote;
        
    }

    public void setLoadEdit(ArticuloTB articuloTB, int index, ObservableList<LoteTB> loteTBs) {
        idArticulo = articuloTB.getIdArticulo();
        lblClave.setText(articuloTB.getClave().get());
        lblDescripcion.setText(articuloTB.getNombreMarca().get());
        txtCantidad.setText("" + articuloTB.getCantidad());
        unidadventa = articuloTB.getUnidadVenta();
        txtCosto.setText(Tools.roundingValue(articuloTB.getPrecioCompraReal(), 2));
        txtDescuento.setText(Tools.roundingValue(articuloTB.getDescuento().get(), 2));

        txtPrecio.setText(Tools.roundingValue(articuloTB.getPrecioVenta(), 2));
        txtMargen.setText("" + articuloTB.getMargen());
        txtUtilidad.setText(Tools.roundingValue(articuloTB.getUtilidad(), 2));

        txtPrecioMayoreo.setText(Tools.roundingValue(articuloTB.getPrecioVentaMayoreo(), 2));
        txtMargenMayoreo.setText("" + articuloTB.getMargenMayoreo());
        txtUtilidadMayoreo.setText(Tools.roundingValue(articuloTB.getUtilidadMayoreo(), 2));

        cbImpuesto.setSelected(articuloTB.isImpuesto());
        validationelemnt = true;
        validarlote = articuloTB.isLote();
        this.lote = articuloTB.isLote();
        indexcompra = index;
        loteedit = true;
        this.loteTBs = loteTBs;
        cantidadinicial = articuloTB.getCantidad();
    }

    private void addArticulo(double costo, double costoreal) throws IOException {
        ArticuloTB articuloTB = new ArticuloTB();
        articuloTB.setIdArticulo(idArticulo);
        articuloTB.setClave(lblClave.getText());
        articuloTB.setNombreMarca(lblDescripcion.getText());
        articuloTB.setCantidad(Double.parseDouble(txtCantidad.getText()));        
        articuloTB.setUnidadVenta(unidadventa);
        articuloTB.setPrecioCompra(costo);
        articuloTB.setPrecioCompraReal(costoreal);

        articuloTB.setDescuento(txtDescuento.getText().isEmpty() ? 0
                : Double.parseDouble(txtDescuento.getText()) * articuloTB.getCantidad());

        articuloTB.setSubTotal(articuloTB.getCantidad() * articuloTB.getPrecioCompra());

        articuloTB.setImporte(
                articuloTB.getSubTotal().get()
                - articuloTB.getDescuento().get());
        articuloTB.setCantidadGranel(articuloTB.getImporte().get());

        articuloTB.setUtilidad(Tools.isNumeric(txtUtilidad.getText())
                ? Double.parseDouble(txtUtilidad.getText()) : 0
        );

        articuloTB.setImpuesto(cbImpuesto.isSelected());

        articuloTB.setPrecioVenta(Double.parseDouble(txtPrecio.getText()));
        articuloTB.setMargen(Short.parseShort(txtMargen.getText()));
        articuloTB.setUtilidad(Double.parseDouble(txtUtilidad.getText()));

        articuloTB.setPrecioVentaMayoreo(Double.parseDouble(txtPrecioMayoreo.getText()));
        articuloTB.setMargenMayoreo(Short.parseShort(txtMargenMayoreo.getText()));
        articuloTB.setUtilidadMayoreo(Double.parseDouble(txtUtilidadMayoreo.getText()));

        articuloTB.setLote(lote);
        if (validateStock(comprasController.getTvList(), articuloTB) && !validationelemnt) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ya existe en la lista el artículo", false);
        } else if (validationelemnt) {
            if (validarlote && cantidadinicial != Double.parseDouble(txtCantidad.getText())) {
                openWindowLote(articuloTB);
            } else {
                comprasController.getTvList().getItems().set(indexcompra, articuloTB);
                comprasController.setCalculateTotals();
                Tools.Dispose(window);
            }
        } else {
            comprasController.setCalculateTotals();
        }
    }

    private void generationPrice() {
        double importe = Double.parseDouble(txtImporte.getText());
        double cantidad = Double.parseDouble(txtCantidad.getText());

        double preciocompra = importe / cantidad;
        txtCosto.setText(Tools.roundingValue(preciocompra, 2));

        txtMargen.setText("30");
        txtPrecio.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen.getText())
                ? Double.parseDouble(txtMargen.getText()) : 30,
                preciocompra));
        txtUtilidad.setText(Tools.roundingValue((Double.parseDouble(txtPrecio.getText()) - preciocompra), 2));

        txtMargenMayoreo.setText("20");
        txtPrecioMayoreo.setText(Tools.calculateAumento(Tools.isNumeric(txtMargenMayoreo.getText())
                ? Double.parseDouble(txtMargenMayoreo.getText()) : 20,
                preciocompra));
        txtUtilidadMayoreo.setText(Tools.roundingValue((Double.parseDouble(txtPrecioMayoreo.getText()) - preciocompra), 2));

    }

    private boolean validateStock(TableView<ArticuloTB> view, ArticuloTB articuloTB) throws IOException {
        boolean ret = false;
        for (int i = 0; i < view.getItems().size(); i++) {
            if (view.getItems().get(i).getClave().get().equals(articuloTB.getClave().get())) {
                ret = true;
                break;
            }
        }
        if (!ret) {
            if (validarlote) {
                openWindowLote(articuloTB);
            } else {
                view.getItems().add(articuloTB);
                Tools.Dispose(window);
            }
        }
        return ret;
    }

    public void addArticuloList() throws IOException {
        if (!Tools.isNumeric(txtCantidad.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico en la cantidad", false);
            txtCantidad.requestFocus();
        } else if (!Tools.isNumeric(txtCosto.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico en el costo", false);
            txtCosto.requestFocus();
        } else if (!Tools.isNumeric(txtPrecio.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico en el precio", false);
            txtPrecio.requestFocus();
        } else {
            if (cbImpuesto.isSelected()) {
                addArticulo(Double.parseDouble(txtCosto.getText()), Double.parseDouble(txtCosto.getText()));
            } else {
                double igv = Tools.calculateTax(Session.IMPUESTO, Double.parseDouble(txtCosto.getText()));
                double precioigv = (Double.parseDouble(txtCosto.getText()) + igv);
                addArticulo(precioigv, Double.parseDouble(txtCosto.getText()));
            }
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
    private void onKeyTypedImporte(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtImporte.getText().contains(".") || c == '-' && txtImporte.getText().contains("-")) {
            event.consume();
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
    private void onKeyTypedMargenMayoreo(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b')) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedPrecioMayoreo(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecioMayoreo.getText().contains(".") || c == '-' && txtPrecioMayoreo.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onActionImporte(ActionEvent event) {
        if (Tools.isNumeric(txtImporte.getText()) && Tools.isNumeric(txtCantidad.getText())) {
            generationPrice();
        }

    }

    @FXML
    private void onActionCantidad(ActionEvent event) {
        if (Tools.isNumeric(txtImporte.getText()) && Tools.isNumeric(txtCantidad.getText())) {
            generationPrice();
        }
    }

    @FXML
    private void onKeyReleasedCosto(KeyEvent event) {
        if (Tools.isNumeric(txtCosto.getText()) && Tools.isNumeric(txtMargen.getText())) {
            if (cbImpuesto.isSelected()) {
                double preciocompra = Double.parseDouble(txtCosto.getText());
                //-------------------first
                txtPrecio.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen.getText())
                        ? Double.parseDouble(txtMargen.getText()) : 0,
                        preciocompra));
                txtUtilidad.setText(Tools.roundingValue((Double.parseDouble(txtPrecio.getText()) - preciocompra), 2));
                //-------------------second
                txtMargenMayoreo.setText(Tools.isNumeric(txtMargenMayoreo.getText())
                        ? txtMargenMayoreo.getText() : "0");

                txtPrecioMayoreo.setText(Tools.calculateAumento(Double.parseDouble(txtMargenMayoreo.getText()),
                        preciocompra));
                txtUtilidadMayoreo.setText(Tools.roundingValue((Double.parseDouble(txtPrecioMayoreo.getText()) - preciocompra), 2));
            } else {
                double igv = Tools.calculateTax(Session.IMPUESTO, Double.parseDouble(txtCosto.getText()));
                double precioigv = (Double.parseDouble(txtCosto.getText()) + igv);

                double preciocompra = precioigv;
                //-------------------first
                txtPrecio.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen.getText())
                        ? Double.parseDouble(txtMargen.getText()) : 0,
                        preciocompra));
                txtUtilidad.setText(Tools.roundingValue((Double.parseDouble(txtPrecio.getText()) - preciocompra), 2));
                //-------------------second
                txtMargenMayoreo.setText(Tools.isNumeric(txtMargenMayoreo.getText())
                        ? txtMargenMayoreo.getText() : "0");

                txtPrecioMayoreo.setText(Tools.calculateAumento(Double.parseDouble(txtMargenMayoreo.getText()),
                        preciocompra));
                txtUtilidadMayoreo.setText(Tools.roundingValue((Double.parseDouble(txtPrecioMayoreo.getText()) - preciocompra), 2));
            }
        }
    }

    @FXML
    private void onKeyReleasedMargen(KeyEvent event) {
        if (Tools.isNumeric(txtMargen.getText()) && Tools.isNumeric(txtCosto.getText())) {
            if (cbImpuesto.isSelected()) {
                double preciocompra = Double.parseDouble(txtCosto.getText());

                txtPrecio.setText(Tools.calculateAumento(Double.parseDouble(txtMargen.getText()), preciocompra));

                txtUtilidad.setText(Tools.roundingValue((Double.parseDouble(txtPrecio.getText()) - preciocompra), 2));

            } else {
                double igv = Tools.calculateTax(Session.IMPUESTO, Double.parseDouble(txtCosto.getText()));
                double precioigv = (Double.parseDouble(txtCosto.getText()) + igv);

                double preciocompra = precioigv;

                txtPrecio.setText(Tools.calculateAumento(Double.parseDouble(txtMargen.getText()), preciocompra));

                txtUtilidad.setText(Tools.roundingValue((Double.parseDouble(txtPrecio.getText()) - preciocompra), 2));
            }
        }
    }

    @FXML
    private void onActionImpuesto(ActionEvent event) {
        if (Tools.isNumeric(txtMargen.getText()) && Tools.isNumeric(txtCosto.getText())) {
            if (cbImpuesto.isSelected()) {
                double preciocompra = Double.parseDouble(txtCosto.getText());

                txtPrecio.setText(Tools.calculateAumento(Double.parseDouble(txtMargen.getText()), preciocompra));

                txtUtilidad.setText(Tools.roundingValue((Double.parseDouble(txtPrecio.getText()) - preciocompra), 2));

            } else {
                double igv = Tools.calculateTax(Session.IMPUESTO, Double.parseDouble(txtCosto.getText()));
                double precioigv = (Double.parseDouble(txtCosto.getText()) + igv);

                double preciocompra = precioigv;

                txtPrecio.setText(Tools.calculateAumento(Double.parseDouble(txtMargen.getText()), preciocompra));

                txtUtilidad.setText(Tools.roundingValue((Double.parseDouble(txtPrecio.getText()) - preciocompra), 2));
            }
        }
    }

    @FXML
    private void onKeyReleasedPrecio(KeyEvent event) {
        if (Tools.isNumeric(txtPrecio.getText()) && Tools.isNumeric(txtCosto.getText())) {
            if (cbImpuesto.isSelected()) {
                Double valor = Double.parseDouble(txtCosto.getText());
                Double precio = Double.parseDouble(txtPrecio.getText());
                Double porcentaje = (precio * 100) / valor;
                int recalculado = (int) Math.abs((100
                        - (Double.parseDouble(
                                Tools.roundingValue(Double.parseDouble(
                                        Tools.roundingValue(porcentaje, 2)), 0)))));
                txtMargen.setText(String.valueOf(recalculado));

                txtUtilidad.setText(Tools.roundingValue((precio - valor), 2));
            } else {
                double igv = Tools.calculateTax(Session.IMPUESTO, Double.parseDouble(txtCosto.getText()));
                double precioigv = (Double.parseDouble(txtCosto.getText()) + igv);

                Double valor = precioigv;
                Double precio = Double.parseDouble(txtPrecio.getText());
                Double porcentaje = (precio * 100) / valor;
                int recalculado = (int) Math.abs((100 - (Double.parseDouble(Tools.roundingValue(
                        Double.parseDouble(Tools.roundingValue(porcentaje, 2)),
                        0)))));
                txtMargen.setText(String.valueOf(recalculado));

                txtUtilidad.setText(Tools.roundingValue((precio - valor), 2));
            }

        }
    }

    @FXML
    private void onKeyReleasedMargenMayoreo(KeyEvent event) {
        if (Tools.isNumeric(txtMargenMayoreo.getText()) && Tools.isNumeric(txtCosto.getText())) {
            if (cbImpuesto.isSelected()) {
                double preciocompra = Double.parseDouble(txtCosto.getText());

                txtPrecioMayoreo.setText(Tools.calculateAumento(Double.parseDouble(txtMargenMayoreo.getText()), preciocompra));

                txtUtilidadMayoreo.setText(Tools.roundingValue((Double.parseDouble(txtPrecioMayoreo.getText()) - preciocompra), 2));

            } else {
                double igv = Tools.calculateTax(Session.IMPUESTO, Double.parseDouble(txtCosto.getText()));
                double precioigv = (Double.parseDouble(txtCosto.getText()) + igv);

                double preciocompra = precioigv;

                txtPrecioMayoreo.setText(Tools.calculateAumento(Double.parseDouble(txtMargenMayoreo.getText()), preciocompra));

                txtUtilidadMayoreo.setText(Tools.roundingValue((Double.parseDouble(txtPrecioMayoreo.getText()) - preciocompra), 2));
            }
        }
    }

    @FXML
    private void onKeyReleasedPrecioMayoreo(KeyEvent event) {
        if (Tools.isNumeric(txtPrecioMayoreo.getText()) && Tools.isNumeric(txtCosto.getText())) {
            if (cbImpuesto.isSelected()) {
                Double valor = Double.parseDouble(txtCosto.getText());
                Double precio = Double.parseDouble(txtPrecioMayoreo.getText());
                Double porcentaje = (precio * 100) / valor;
                int recalculado = (int) Math.abs((100 - (Double.parseDouble(Tools.roundingValue(
                        Double.parseDouble(Tools.roundingValue(porcentaje, 2)),
                        0)))));
                txtMargenMayoreo.setText(String.valueOf(recalculado));

                txtUtilidadMayoreo.setText(Tools.roundingValue((precio - valor), 2));

            } else {
                double igv = Tools.calculateTax(Session.IMPUESTO, Double.parseDouble(txtCosto.getText()));
                double precioigv = (Double.parseDouble(txtCosto.getText()) + igv);

                Double valor = precioigv;
                Double precio = Double.parseDouble(txtPrecioMayoreo.getText());
                Double porcentaje = (precio * 100) / valor;
                int recalculado = (int) Math.abs((100 - (Double.parseDouble(Tools.roundingValue(
                        Double.parseDouble(Tools.roundingValue(porcentaje, 2)),
                        0)))));
                txtMargenMayoreo.setText(String.valueOf(recalculado));

                txtUtilidadMayoreo.setText(Tools.roundingValue((precio - valor), 2));
            }

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

}
