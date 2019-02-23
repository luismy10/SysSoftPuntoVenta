package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ArticuloADO;
import model.ArticuloTB;
import model.DetalleADO;
import model.DetalleTB;
import model.ImpuestoADO;
import model.ImpuestoTB;

public class FxArticuloProcesoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtClave;
    @FXML
    private TextField txtClaveAlterna;
    @FXML
    private TextField txtNombreMarca;
    @FXML
    private TextField txtNombreGenerico;
    @FXML
    private TextField txtCategoria;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtPresentacion;
    @FXML
    private ComboBox<DetalleTB> cbEstado;
    @FXML
    private ImageView lnPrincipal;
    @FXML
    private Button btnRegister;
    @FXML
    private TextField txtStockMinimo;
    @FXML
    private TextField txtStockMaximo;
    @FXML
    private TextField txtPrecioCompra;
    @FXML
    private CheckBox cbLote;
    @FXML
    private CheckBox cbInventario;
    @FXML
    private RadioButton rbUnidad;
    @FXML
    private RadioButton rbGranel;
    @FXML
    private TextField txtCantidadActual;
    @FXML
    private TextField txtMedida;
    @FXML
    private TextField txtPrecioVenta1;
    @FXML
    private TextField txtMargen1;
    @FXML
    private TextField txtUtilidad1;
    @FXML
    private TextField txtPrecioVenta2;
    @FXML
    private TextField txtMargen2;
    @FXML
    private TextField txtUtilidad2;
    @FXML
    private TextField txtPrecioVenta3;
    @FXML
    private TextField txtMargen3;
    @FXML
    private TextField txtUtilidad3;

    @FXML
    private ComboBox<ImpuestoTB> cbImpuesto;

    private String idArticulo;

    private File selectFile;

    private int idPresentacion;

    private int idCategoria;

    private int idMarca;

    private int idMedida;

    private FxArticulosController articulosController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idArticulo = "";
        idMedida = 0;
        idPresentacion = 0;
        idCategoria = 0;
        idMarca = 0;
        ToggleGroup group = new ToggleGroup();
        rbUnidad.setToggleGroup(group);
        rbGranel.setToggleGroup(group);
        txtMargen1.setText("30");
        txtMargen2.setText("20");
        txtMargen3.setText("10");
        cbImpuesto.getItems().clear();
        ImpuestoADO.GetTipoImpuestoCombBox().forEach(e -> {
            this.cbImpuesto.getItems().add(new ImpuestoTB(e.getIdImpuesto(), e.getNombre(), e.getValor(), e.getPredeterminado()));
        });
    }

    public void setInitArticulo() {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        txtClave.requestFocus();
        DetalleADO.GetDetailIdName("2", "0001", "").forEach(e -> {
            cbEstado.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        cbEstado.getSelectionModel().select(0);

    }

    public void setValueClone(String value) {
        ArrayList<ArticuloTB> list = ArticuloADO.GetArticulosById(value);
        if (!list.isEmpty()) {
            ArticuloTB articuloTB = list.get(0);
            txtNombreMarca.setText(articuloTB.getNombreMarca());
            txtNombreGenerico.setText(articuloTB.getNombreGenerico());

            if (articuloTB.getCategoria() != 0) {
                idCategoria = articuloTB.getCategoria();
                txtCategoria.setText(articuloTB.getCategoriaName());
            }

            if (articuloTB.getMarcar() != 0) {
                idMarca = articuloTB.getMarcar();
                txtMarca.setText(articuloTB.getMarcaName());
            }

            if (articuloTB.getUnidadCompra() != 0) {
                idMedida = articuloTB.getUnidadCompra();
                txtMedida.setText(articuloTB.getUnidadCompraName());
            }

            if (articuloTB.getPresentacion() != 0) {
                idPresentacion = articuloTB.getPresentacion();
                txtPresentacion.setText(articuloTB.getPresentacionName());
            }

            if (articuloTB.getUnidadVenta() == 1) {
                rbUnidad.setSelected(true);
            } else {
                rbGranel.setSelected(true);
            }

            ObservableList<DetalleTB> lsest = cbEstado.getItems();
            if (articuloTB.getEstado() != 0) {
                for (int i = 0; i < lsest.size(); i++) {
                    if (articuloTB.getEstado() == lsest.get(i).getIdDetalle().get()) {
                        cbEstado.getSelectionModel().select(i);
                        break;
                    }
                }
            }

            txtStockMinimo.setText(Tools.roundingValue(articuloTB.getStockMinimo(), 2));
            txtStockMaximo.setText(Tools.roundingValue(articuloTB.getStockMaximo(), 2));
            txtPrecioCompra.setText(Tools.roundingValue(articuloTB.getPrecioCompra(), 2));
            //agregar la lista de precio
            txtPrecioVenta1.setText(Tools.roundingValue(articuloTB.getPrecioVenta(), 2));
            txtMargen1.setText(articuloTB.getMargen() + "");
            txtUtilidad1.setText(Tools.roundingValue(articuloTB.getUtilidad(), 2));

            txtPrecioVenta2.setText(Tools.roundingValue(articuloTB.getPrecioVenta2(), 2));
            txtMargen2.setText(articuloTB.getMargen2() + "");
            txtUtilidad2.setText(Tools.roundingValue(articuloTB.getUtilidad2(), 2));

            txtPrecioVenta3.setText(Tools.roundingValue(articuloTB.getPrecioVenta3(), 2));
            txtMargen3.setText(articuloTB.getMargen3() + "");
            txtUtilidad3.setText(Tools.roundingValue(articuloTB.getUtilidad3(), 2));

        }
    }

    public void setValueEdit(String value) {
        btnRegister.setText("Actualizar");
        btnRegister.getStyleClass().add("buttonLightWarning");
        ArrayList<ArticuloTB> list = ArticuloADO.GetArticulosById(value);
        if (!list.isEmpty()) {
            ArticuloTB articuloTB = list.get(0);
            idArticulo = articuloTB.getIdArticulo();
            txtClave.setText(articuloTB.getClave());
            txtClaveAlterna.setText(articuloTB.getClaveAlterna());
            txtNombreMarca.setText(articuloTB.getNombreMarca());
            txtNombreGenerico.setText(articuloTB.getNombreGenerico());

            if (articuloTB.getCategoria() != 0) {
                idCategoria = articuloTB.getCategoria();
                txtCategoria.setText(articuloTB.getCategoriaName());
            }

            if (articuloTB.getMarcar() != 0) {
                idMarca = articuloTB.getMarcar();
                txtMarca.setText(articuloTB.getMarcaName());
            }

            if (articuloTB.getUnidadCompra() != 0) {
                idMedida = articuloTB.getUnidadCompra();
                txtMedida.setText(articuloTB.getUnidadCompraName());
            }

            if (articuloTB.getPresentacion() != 0) {
                idPresentacion = articuloTB.getPresentacion();
                txtPresentacion.setText(articuloTB.getPresentacionName());
            }

            if (articuloTB.getUnidadVenta() == 1) {
                rbUnidad.setSelected(true);
                txtCantidadActual.setText(Tools.roundingValue(articuloTB.getCantidad(), 2));
            } else {
                rbGranel.setSelected(true);
                txtCantidadActual.setText(Tools.roundingValue(articuloTB.getCantidad(), 2));
            }

            ObservableList<DetalleTB> lsest = cbEstado.getItems();
            if (articuloTB.getEstado() != 0) {
                for (int i = 0; i < lsest.size(); i++) {
                    if (articuloTB.getEstado() == lsest.get(i).getIdDetalle().get()) {
                        cbEstado.getSelectionModel().select(i);
                        break;
                    }
                }
            }

            cbLote.setSelected(articuloTB.isLote());
            cbInventario.setSelected(articuloTB.isInventario());

            txtStockMinimo.setText(Tools.roundingValue(articuloTB.getStockMinimo(), 2));
            txtStockMaximo.setText(Tools.roundingValue(articuloTB.getStockMaximo(), 2));
            txtPrecioCompra.setText(Tools.roundingValue(articuloTB.getPrecioCompra(), 2));
            //agregar la lista de precio
            txtPrecioVenta1.setText(Tools.roundingValue(articuloTB.getPrecioVenta(), 2));
            txtMargen1.setText(articuloTB.getMargen() + "");
            txtUtilidad1.setText(Tools.roundingValue(articuloTB.getUtilidad(), 2));

            txtPrecioVenta2.setText(Tools.roundingValue(articuloTB.getPrecioVenta2(), 2));
            txtMargen2.setText(articuloTB.getMargen2() + "");
            txtUtilidad2.setText(Tools.roundingValue(articuloTB.getUtilidad2(), 2));

            txtPrecioVenta3.setText(Tools.roundingValue(articuloTB.getPrecioVenta3(), 2));
            txtMargen3.setText(articuloTB.getMargen3() + "");
            txtUtilidad3.setText(Tools.roundingValue(articuloTB.getUtilidad3(), 2));

            if (articuloTB.getImagenTB().equalsIgnoreCase("")) {
                lnPrincipal.setImage(new Image("/view/no-image.png"));
            } else {
                lnPrincipal.setImage(new Image(new File("" + articuloTB.getImagenTB()).toURI().toString()));
            }

            if (articuloTB.getImpuestoArticulo() != 0) {
                for (int i = 0; i < cbImpuesto.getItems().size(); i++) {
                    if (cbImpuesto.getItems().get(i).getIdImpuesto() == articuloTB.getImpuestoArticulo()) {
                        cbImpuesto.getSelectionModel().select(i);
                        break;
                    }
                }
            }

        }
    }

    private void aValidityProcess() {
        if (txtClave.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese la clave del artículo, por favor.", false);
            txtClave.requestFocus();
        } else {
            if (txtNombreMarca.getText().isEmpty()) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el nombre del artículo, por favor.", false);
                txtNombreMarca.requestFocus();
            } else {
                if (cbEstado.getSelectionModel().getSelectedIndex() < 0) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Selecciona el estado del artículo, por favor.", false);
                    cbEstado.requestFocus();
                } else {
                    if (txtMedida.getText().isEmpty()) {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el nombre de la unidad de medida, por favor.", false);
                        txtMedida.requestFocus();
                    } else {
                        if (cbImpuesto.getSelectionModel().getSelectedIndex() < 0) {
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el nombre del impuesto, por favor.", false);
                            cbImpuesto.requestFocus();
                        } else {
                            if (idMedida <= 0) {
                                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el nombre de la unidad de medida, por favor.", false);
                                txtMedida.requestFocus();
                            } else {
                                if (!Tools.isNumeric(txtPrecioCompra.getText())) {
                                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el precio de compra, por favor.", false);
                                    txtPrecioCompra.requestFocus();
                                } else {
                                    if (!Tools.isNumeric(txtPrecioVenta1.getText())) {
                                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el primer precio de venta, por favor.", false);
                                        txtPrecioVenta1.requestFocus();
                                    } else {

                                        if (!Tools.isNumeric(txtPrecioVenta2.getText())) {
                                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el segundo precio de venta, por favor.", false);
                                            txtPrecioVenta2.requestFocus();
                                        } else {
                                            if (!Tools.isNumeric(txtPrecioVenta3.getText())) {
                                                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el tercer precio de venta, por favor.", false);
                                                txtPrecioVenta3.requestFocus();
                                            } else {
                                                if (!Tools.isNumeric(txtMargen1.getText())) {
                                                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el primer margen, por favor.", false);
                                                    txtMargen1.requestFocus();
                                                } else {
                                                    if (!Tools.isNumeric(txtMargen2.getText())) {
                                                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el segundo margen, por favor.", false);
                                                        txtMargen2.requestFocus();
                                                    } else {
                                                        if (!Tools.isNumeric(txtMargen3.getText())) {
                                                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el tercer margen, por favor.", false);
                                                            txtMargen3.requestFocus();
                                                        } else {

                                                            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Articulo", "¿Esta seguro de continuar?", true);
                                                            if (confirmation == 1) {
                                                                ArticuloTB articuloTB = new ArticuloTB();
                                                                articuloTB.setIdArticulo(idArticulo);
                                                                articuloTB.setClave(txtClave.getText().trim());
                                                                articuloTB.setClaveAlterna(txtClaveAlterna.getText().trim());
                                                                articuloTB.setNombreMarca(txtNombreMarca.getText().trim());
                                                                articuloTB.setNombreGenerico(txtNombreGenerico.getText().trim());
                                                                articuloTB.setImagenTB(selectFile != null
                                                                        ? "./img/" + selectFile.getName()
                                                                        : "");
                                                                articuloTB.setCategoria(idCategoria != 0
                                                                        ? idCategoria
                                                                        : 0);
                                                                articuloTB.setMarcar(idMarca != 0
                                                                        ? idMarca
                                                                        : 0);
                                                                articuloTB.setUnidadCompra(idMedida != 0
                                                                        ? idMedida
                                                                        : 0);
                                                                articuloTB.setPresentacion(idPresentacion != 0
                                                                        ? idPresentacion
                                                                        : 0);

                                                                articuloTB.setStockMinimo(Tools.isNumeric(txtStockMinimo.getText())
                                                                        ? Double.parseDouble(txtStockMinimo.getText().trim())
                                                                        : 0);

                                                                articuloTB.setStockMaximo(Tools.isNumeric(txtStockMaximo.getText())
                                                                        ? Double.parseDouble(txtStockMaximo.getText().trim())
                                                                        : 0);

                                                                articuloTB.setPrecioCompra(Tools.isNumeric(txtPrecioCompra.getText())
                                                                        ? Double.parseDouble(txtPrecioCompra.getText())
                                                                        : 0);

                                                                //agregar lista de precios
                                                                articuloTB.setPrecioVenta(Double.parseDouble(txtPrecioVenta1.getText()));
                                                                articuloTB.setMargen(Short.valueOf(txtMargen1.getText()));
                                                                articuloTB.setUtilidad(Double.parseDouble(txtUtilidad1.getText()));

                                                                articuloTB.setPrecioVenta2(Double.parseDouble(txtPrecioVenta2.getText()));
                                                                articuloTB.setMargen2(Short.valueOf(txtMargen2.getText()));
                                                                articuloTB.setUtilidad2(Double.parseDouble(txtUtilidad2.getText()));

                                                                articuloTB.setPrecioVenta3(Double.parseDouble(txtPrecioVenta3.getText()));
                                                                articuloTB.setMargen3(Short.valueOf(txtMargen3.getText()));
                                                                articuloTB.setUtilidad3(Double.parseDouble(txtUtilidad3.getText()));

                                                                articuloTB.setEstado(cbEstado.getSelectionModel().getSelectedIndex() >= 0
                                                                        ? cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get()
                                                                        : 0);
                                                                articuloTB.setUnidadVenta(rbUnidad.isSelected() ? 1 : 2);
                                                                articuloTB.setLote(cbLote.isSelected());
                                                                articuloTB.setInventario(cbInventario.isSelected());
                                                                articuloTB.setImpuestoArticulo(cbImpuesto.getSelectionModel().getSelectedIndex() >= 0 ? cbImpuesto.getSelectionModel().getSelectedItem().getIdImpuesto() : 0);

                                                                String result = ArticuloADO.CrudArticulo(articuloTB);
                                                                switch (result) {
                                                                    case "registered":
                                                                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Articulo", "Registrado correctamente el artículo.", false);
                                                                        Tools.Dispose(window);
                                                                        break;
                                                                    case "updated":
                                                                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Articulo", "Actualizado correctamente el artículo.", false);
                                                                        Tools.Dispose(window);
                                                                        articulosController.getTxtSearch().requestFocus();
                                                                        break;
                                                                    case "duplicate":
                                                                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "No se puede haber 2 artículos con la misma clave.", false);
                                                                        txtClave.requestFocus();
                                                                        break;
                                                                    default:
                                                                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Articulo", result, false);
                                                                        break;
                                                                }

                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) {
        aValidityProcess();
    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            aValidityProcess();
        }
    }

    @FXML
    private void onKeyPressedToCancel(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionToCancel(ActionEvent event) {
        Tools.Dispose(window);
    }

    @FXML
    private void onActionPhoto(ActionEvent event) {
        //idArticulo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar una imagen");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Elija una imagen", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        selectFile = fileChooser.showOpenDialog(window.getScene().getWindow());
        if (selectFile != null) {
            selectFile = new File(selectFile.getAbsolutePath());
            if (selectFile.getName().endsWith("png") || selectFile.getName().endsWith("jpg") || selectFile.getName().endsWith("jpeg") || selectFile.getName().endsWith(".gif")) {
                lnPrincipal.setImage(new Image(selectFile.toURI().toString()));
                FileInputStream inputStream = null;
                byte[] buffer = new byte[1024];
                try (FileOutputStream outputStream = new FileOutputStream("." + File.separator + "img" + File.separator + selectFile.getName())) {
                    inputStream = new FileInputStream(selectFile.getAbsolutePath());
                    int byteRead;
                    while ((byteRead = inputStream.read(buffer)) != 1) {
                        outputStream.write(buffer, 0, byteRead);
                    }
                } catch (Exception e) {
                    if (e.getMessage() != null) {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Artículo", e.getLocalizedMessage() + ": Consulte con el proveedor del sistema del problema :D", false);
                    }
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException ex) {
                            Logger.getLogger(FxArticuloProcesoController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void onActionRemovePhoto(ActionEvent event) throws IOException {
        lnPrincipal.setImage(new Image("/view/no-image.png"));
        selectFile = null;

    }

    @FXML
    private void onKeyReleasedPrecioCompra(KeyEvent event) {
        if (Tools.isNumeric(txtPrecioCompra.getText()) && Tools.isNumeric(txtMargen1.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));
            //se suma el impuesto al costo del articulo
            double preciocompra = Double.parseDouble(txtPrecioCompra.getText());
            double precioimpuesto = (preciocompra + impuesto);

            txtPrecioVenta1.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen1.getText())
                    ? Double.parseDouble(txtMargen1.getText()) : 0,
                    precioimpuesto));
            txtUtilidad1.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta1.getText()) - preciocompra), 2));
        }
        if (Tools.isNumeric(txtPrecioCompra.getText()) && Tools.isNumeric(txtMargen2.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));
            //se suma el impuesto al costo del articulo
            double preciocompra = Double.parseDouble(txtPrecioCompra.getText());
            double precioimpuesto = (preciocompra + impuesto);

            txtPrecioVenta2.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen2.getText())
                    ? Double.parseDouble(txtMargen2.getText()) : 0,
                    precioimpuesto));
            txtUtilidad2.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta2.getText()) - preciocompra), 2));
        }
        if (Tools.isNumeric(txtPrecioCompra.getText()) && Tools.isNumeric(txtMargen3.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));
            //se suma el impuesto al costo del articulo
            double preciocompra = Double.parseDouble(txtPrecioCompra.getText());
            double precioimpuesto = (preciocompra + impuesto);

            txtPrecioVenta3.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen3.getText())
                    ? Double.parseDouble(txtMargen3.getText()) : 0,
                    precioimpuesto));
            txtUtilidad3.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta3.getText()) - preciocompra), 2));
        }

    }

    @FXML
    private void onKeyTypedClave(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c < 'a' || c > 'z')) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedClaveAlterna(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c < 'a' || c > 'z')) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedMinimo(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtStockMinimo.getText().contains(".") || c == '-' && txtStockMinimo.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedMaxino(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtStockMaximo.getText().contains(".") || c == '-' && txtStockMaximo.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedPrecioCompra(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecioCompra.getText().contains(".") || c == '-' && txtPrecioCompra.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedPrecioVenta1(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecioVenta1.getText().contains(".") || c == '-' && txtPrecioVenta1.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedPrecioVenta2(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecioVenta2.getText().contains(".") || c == '-' && txtPrecioVenta2.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedPrecioVenta3(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecioVenta3.getText().contains(".") || c == '-' && txtPrecioVenta3.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedDetalle(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (c != '\b') {
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

    private void openWindowDetalle(String title, String idDetalle, boolean valor) throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_DETALLELISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxDetalleListaController controller = fXMLLoader.getController();
        controller.setControllerArticulo(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, title, window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        if (valor == true) {
            controller.initListNameImpuesto(idDetalle);
        } else {
            controller.initListDetalle(idDetalle, "");
        }

    }

    private void openWindowGerarCodigoBarras() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_CODIGOBARRAS);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxCodigoBarrasController controller = fXMLLoader.getController();
        controller.setControllerArticulo(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Generar codigo de barras", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
//        controller.initListDetalle(idDetalle, "");
    }

    @FXML
    private void onKeyReleasedCategoria(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.SPACE) {
            openWindowDetalle("Agregar Categoría", "0006", false);
        }
    }

    @FXML
    private void onKeyReleasedMarca(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.SPACE) {
            openWindowDetalle("Agregar Marca", "0007", false);
        }
    }

    @FXML
    private void onKeyReleasedPresentacion(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.SPACE) {
            openWindowDetalle("Agregar Presentación", "0008", false);
        }
    }

    @FXML
    private void onMouseClickedCategoria(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalle("Agregar Categoría", "0006", false);
        }
    }

    @FXML
    private void onMouseClickedMarca(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalle("Agregar Marca", "0007", false);
        }
    }

    @FXML
    private void onMouseClickedPresentacion(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalle("Agregar Presentación", "0008", false);
        }
    }

    @FXML
    private void onActionGenerar(ActionEvent event) throws IOException {
        openWindowGerarCodigoBarras();
    }

    @FXML
    private void onMouseClickedMedida(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalle("Agregar Departamento", "0013", false);
        }
    }

    @FXML
    private void onKeyReleasedMedida(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.SPACE) {
            openWindowDetalle("Agregar Departamento", "0013", false);
        }
    }

    @FXML
    private void onKeyTypedMedida(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (c != '\b') {
            event.consume();
        }
    }

    private void onMouseClickedImpuesto(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalle("Agregar Impuestoto", "0", true);
        }
    }

    private void onKeyReleasedImpuesto(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.SPACE) {
            openWindowDetalle("Agregar Impuesto", "0", true);
        }
    }

    private void onKeyTypedImpuesto(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (c != '\b') {
            event.consume();
        }
    }

    @FXML
    private void onActionImpuesto(ActionEvent event) {
        if (Tools.isNumeric(txtPrecioCompra.getText()) && Tools.isNumeric(txtMargen1.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));
            //se suma el impuesto al costo del articulo
            double precioimpuesto = (Double.parseDouble(txtPrecioCompra.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta1.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen1.getText())
                    ? Double.parseDouble(txtMargen1.getText()) : 0,
                    preciocompra));
            txtUtilidad1.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta1.getText()) - preciocompra), 2));
        }
        if (Tools.isNumeric(txtPrecioCompra.getText()) && Tools.isNumeric(txtMargen2.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));
            //se suma el impuesto al costo del articulo
            double precioimpuesto = (Double.parseDouble(txtPrecioCompra.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta2.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen2.getText())
                    ? Double.parseDouble(txtMargen2.getText()) : 0,
                    preciocompra));
            txtUtilidad2.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta2.getText()) - preciocompra), 2));
        }
        if (Tools.isNumeric(txtPrecioCompra.getText()) && Tools.isNumeric(txtMargen3.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));
            //se suma el impuesto al costo del articulo
            double precioimpuesto = (Double.parseDouble(txtPrecioCompra.getText()) + impuesto);
            double preciocompra = precioimpuesto;
            txtPrecioVenta3.setText(Tools.calculateAumento(Tools.isNumeric(txtMargen3.getText())
                    ? Double.parseDouble(txtMargen3.getText()) : 0,
                    preciocompra));
            txtUtilidad3.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta3.getText()) - preciocompra), 2));
        }
    }

    @FXML
    private void onKeyRealesdPrecioVenta1(KeyEvent event) {
        if (Tools.isNumeric(txtPrecioVenta1.getText()) && Tools.isNumeric(txtPrecioCompra.getText())) {
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));

            double preciocompra = Double.parseDouble(txtPrecioCompra.getText());
            double precioimpuesto = (preciocompra + impuesto);

            Double precio = Double.parseDouble(txtPrecioVenta1.getText());
            Double porcentaje = (precio * 100) / precioimpuesto;

            int recalculado = (int) Math.abs((100
                    - (Double.parseDouble(
                            Tools.roundingValue(Double.parseDouble(
                                    Tools.roundingValue(porcentaje, 2)), 0)))));

            txtMargen1.setText(String.valueOf(recalculado));
            txtUtilidad1.setText(Tools.roundingValue((precio - preciocompra), 2));
        }
    }

    @FXML
    private void onKeyRealesdPrecioVenta2(KeyEvent event) {
        if (Tools.isNumeric(txtPrecioVenta2.getText()) && Tools.isNumeric(txtPrecioCompra.getText())) {
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));

            double preciocompra = Double.parseDouble(txtPrecioCompra.getText());
            double precioimpuesto = (preciocompra + impuesto);

            Double precio = Double.parseDouble(txtPrecioVenta2.getText());
            Double porcentaje = (precio * 100) / precioimpuesto;

            int recalculado = (int) Math.abs((100
                    - (Double.parseDouble(
                            Tools.roundingValue(Double.parseDouble(
                                    Tools.roundingValue(porcentaje, 2)), 0)))));

            txtMargen2.setText(String.valueOf(recalculado));
            txtUtilidad2.setText(Tools.roundingValue((precio - preciocompra), 2));
        }
    }

    @FXML
    private void onKeyRealesdPrecioVenta3(KeyEvent event) {
        if (Tools.isNumeric(txtPrecioVenta3.getText()) && Tools.isNumeric(txtPrecioCompra.getText())) {
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));

            double preciocompra = Double.parseDouble(txtPrecioCompra.getText());
            double precioimpuesto = (preciocompra + impuesto);

            Double precio = Double.parseDouble(txtPrecioVenta3.getText());
            Double porcentaje = (precio * 100) / precioimpuesto;

            int recalculado = (int) Math.abs((100
                    - (Double.parseDouble(
                            Tools.roundingValue(Double.parseDouble(
                                    Tools.roundingValue(porcentaje, 2)), 0)))));

            txtMargen3.setText(String.valueOf(recalculado));
            txtUtilidad3.setText(Tools.roundingValue((precio - preciocompra), 2));
        }
    }

    @FXML
    private void onKeyReleasedMargen1(KeyEvent event) {
        if (Tools.isNumeric(txtMargen1.getText()) && Tools.isNumeric(txtPrecioCompra.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));
            
            double preciocompra = Double.parseDouble(txtPrecioCompra.getText());
            double precioimpuesto = (Double.parseDouble(txtPrecioCompra.getText()) + impuesto);
            
            txtPrecioVenta1.setText(Tools.calculateAumento(Double.parseDouble(txtMargen1.getText()), precioimpuesto));
            txtUtilidad1.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta1.getText()) - preciocompra), 2));

        }
    }

    @FXML
    private void onKeyReleasedMargen2(KeyEvent event) {
        if (Tools.isNumeric(txtMargen2.getText()) && Tools.isNumeric(txtPrecioCompra.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));
            
            double preciocompra = Double.parseDouble(txtPrecioCompra.getText());
            double precioimpuesto = (Double.parseDouble(txtPrecioCompra.getText()) + impuesto);
            
            txtPrecioVenta2.setText(Tools.calculateAumento(Double.parseDouble(txtMargen2.getText()), precioimpuesto));
            txtUtilidad2.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta2.getText()) - preciocompra), 2));

        }
    }

    @FXML
    private void onKeyReleasedMargen3(KeyEvent event) {
        if (Tools.isNumeric(txtMargen3.getText()) && Tools.isNumeric(txtPrecioCompra.getText())) {
            //toma el valor del impuesto del combo box
            double impuesto = Tools.calculateTax(
                    cbImpuesto.getSelectionModel().getSelectedIndex() >= 0
                    ? cbImpuesto.getSelectionModel().getSelectedItem().getValor()
                    : 0,
                    Double.parseDouble(txtPrecioCompra.getText()));
            
            double preciocompra = Double.parseDouble(txtPrecioCompra.getText());
            double precioimpuesto = (Double.parseDouble(txtPrecioCompra.getText()) + impuesto);
            
            txtPrecioVenta3.setText(Tools.calculateAumento(Double.parseDouble(txtMargen3.getText()), precioimpuesto));
            txtUtilidad3.setText(Tools.roundingValue((Double.parseDouble(txtPrecioVenta3.getText()) - preciocompra), 2));

        }
    }

    public void setIdPresentacion(int idPresentacion) {
        this.idPresentacion = idPresentacion;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public void setIdMedida(int idMedida) {
        this.idMedida = idMedida;
    }

    public TextField getTxtPresentacion() {
        return txtPresentacion;
    }

    public TextField getTxtCategoria() {
        return txtCategoria;
    }

    public TextField getTxtMarca() {
        return txtMarca;
    }

    public TextField getTxtClave() {
        return txtClave;
    }

    public TextField getTxtMedida() {
        return txtMedida;
    }

    public void initControllerArticulos(FxArticulosController articulosController) {
        this.articulosController = articulosController;
    }

}
