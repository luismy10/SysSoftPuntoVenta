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

    private FxComprasController comprasController;

    private boolean validationelemnt;

    private String idArticulo;

    private boolean validarlote;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        validationelemnt = false;
        validarlote = false;
        txtMargen.setText("30");
        idArticulo = "";
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
                URL url = getClass().getResource(Tools.FX_FILE_LOTEPROCESO);
                FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
                Parent parent = fXMLLoader.load(url.openStream());
                //Controlller here
                FxLoteProcesoController controller = fXMLLoader.getController();
                controller.setLoteController(this);
                //
                Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Lote", window.getScene().getWindow());
                stage.setResizable(false);
                stage.show();
                controller.setLoadData(articuloTB.getClave().get(),
                        articuloTB.getNombre().get(),
                        String.valueOf(articuloTB.getCantidad().get()));
            } else {
                view.getItems().add(articuloTB);
            }

        }
        return ret;
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
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

    private void addArticulo(double costo, double costoreal) throws IOException {
        ArticuloTB articuloTB = new ArticuloTB();
        articuloTB.setIdArticulo(idArticulo);
        articuloTB.setClave(lblClave.getText());
        articuloTB.setNombre(lblDescripcion.getText());
        articuloTB.setCantidad(Double.parseDouble(txtCantidad.getText()));
        articuloTB.setPrecioCompra(costo);
        articuloTB.setPrecioCompraReal(costoreal);
        articuloTB.setDescuento(txtDescuento.getText().isEmpty() ? 0
                : Double.parseDouble(txtDescuento.getText()));

        articuloTB.setPrecioVenta(Double.parseDouble(txtPrecio.getText()));

        articuloTB.setSubTotal((Double.parseDouble(txtCantidad.getText()) * costo));

        articuloTB.setImporte(
                (Double.parseDouble(txtCantidad.getText()) * costo)
                - Double.parseDouble(txtDescuento.getText().isEmpty() ? "0"
                        : txtDescuento.getText()
                ));

        articuloTB.setUtilidad(Tools.isNumeric(txtUtilidad.getText())
                ? Double.parseDouble(txtUtilidad.getText()) : 0
        );

        articuloTB.setImpuesto(cbImpuesto.isSelected());
        if (validateStock(comprasController.getTvList(), articuloTB) && !validationelemnt) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ya existe en la lista el artículo", false);
        } else if (validationelemnt) {
            ObservableList<ArticuloTB> observableList, articuloTBs;
            observableList = comprasController.getTvList().getItems();
            articuloTBs = comprasController.getTvList().getSelectionModel().getSelectedItems();
            articuloTBs.forEach(e -> observableList.remove(e));
            comprasController.setCalculateTotals();
            validateStock(comprasController.getTvList(), articuloTB);
            comprasController.setCalculateTotals();
        } else {
            comprasController.setCalculateTotals();
        }

//        Tools.Dispose(window);
    }

    @FXML
    private void onActionCancel(ActionEvent event) {
        Tools.Dispose(window);
    }

    void setLoadData(String value[], boolean lote) {
        idArticulo = value[0];
        lblClave.setText(value[1]);
        lblDescripcion.setText(value[2]);
        validarlote = lote;
    }

    void setLoadEdit(ArticuloTB articuloTB) {
        idArticulo = articuloTB.getIdArticulo();
        lblClave.setText(articuloTB.getClave().get());
        lblDescripcion.setText(articuloTB.getNombre().get());
        txtCantidad.setText("" + articuloTB.getCantidad().get());
        txtCosto.setText(Tools.roundingValue(articuloTB.getPrecioCompraReal(), 2));
        txtDescuento.setText(Tools.roundingValue(articuloTB.getDescuento().get(), 2));
        txtPrecio.setText(Tools.roundingValue(articuloTB.getPrecioVenta().get(), 2));
        txtUtilidad.setText(Tools.roundingValue(articuloTB.getUtilidad().get(), 2));
        cbImpuesto.setSelected(articuloTB.isImpuesto());
        validationelemnt = true;
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
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtDescuento.getText().contains(".") || c == '-' && txtDescuento.getText().contains("-")) {
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
        if (Tools.isNumeric(txtCosto.getText())) {
            if (cbImpuesto.isSelected()) {
                double preciocompra = Double.parseDouble(txtCosto.getText());

                txtPrecio.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen.getText())
                        ? Double.parseDouble(txtMargen.getText()) : 0,
                        preciocompra));

                txtUtilidad.setText(Tools.roundingValue((Double.parseDouble(txtPrecio.getText()) - preciocompra), 2));

            } else {
                double igv = Tools.calculateTax(Session.IMPUESTO, Double.parseDouble(txtCosto.getText()));
                double precioigv = (Double.parseDouble(txtCosto.getText()) + igv);

                double preciocompra = precioigv;

                txtPrecio.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen.getText())
                        ? Double.parseDouble(txtMargen.getText()) : 0,
                        preciocompra));

                txtUtilidad.setText(Tools.roundingValue((Double.parseDouble(txtPrecio.getText()) - preciocompra), 2));
            }

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

    void setInitCompraController(FxComprasController comprasController) {
        this.comprasController = comprasController;
    }

    public FxComprasController getComprasController() {
        return comprasController;
    }
    
    

}
