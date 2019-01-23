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
    private TextField txtPrecioVenta;
    @FXML
    private CheckBox cbLote;
    @FXML
    private TextField txtDepartamento;
    @FXML
    private CheckBox cbInventario;
    @FXML
    private RadioButton rbUnidad;
    @FXML
    private RadioButton rbGranel;
    @FXML
    private TextField txtCantidadActual;
    @FXML
    private TextField txtPrecioMayoreo;
    @FXML
    private TextField txtMedida;

    private String idArticulo;

    private File selectFile;

    private int idPresentacion;

    private int idCategoria;

    private int idMarca;

    private int idDepartmento;

    private int idMedida;

    private FxArticulosController articulosController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idArticulo = "";
        idMedida = 0;
        idPresentacion = 0;
        idCategoria = 0;
        idMarca = 0;
        idDepartmento = 0;
        ToggleGroup group = new ToggleGroup();
        rbUnidad.setToggleGroup(group);
        rbGranel.setToggleGroup(group);

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
                txtCategoria.setText(articuloTB.getCategoriaName().get());
            }

            if (articuloTB.getMarcar() != 0) {
                idMarca = articuloTB.getMarcar();
                txtMarca.setText(articuloTB.getMarcaName().get());
            }

            if (articuloTB.getMarcar() != 0) {
                idMedida = articuloTB.getUnidadMedida();
                txtMedida.setText(articuloTB.getUnidadMedidaName().get());
            }

            if (articuloTB.getPresentacion() != 0) {
                idPresentacion = articuloTB.getPresentacion();
                txtPresentacion.setText(articuloTB.getPresentacionName().get());
            }

            if (articuloTB.getDepartamento() != 0) {
                idDepartmento = articuloTB.getDepartamento();
                txtDepartamento.setText(articuloTB.getDepartamentoName().get());
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
            txtPrecioVenta.setText(Tools.roundingValue(articuloTB.getPrecioVenta(), 2));

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
                txtCategoria.setText(articuloTB.getCategoriaName().get());
            }

            if (articuloTB.getMarcar() != 0) {
                idMarca = articuloTB.getMarcar();
                txtMarca.setText(articuloTB.getMarcaName().get());
            }

            if (articuloTB.getUnidadMedida() != 0) {
                idMedida = articuloTB.getUnidadMedida();
                txtMedida.setText(articuloTB.getUnidadMedidaName().get());
            }

            if (articuloTB.getPresentacion() != 0) {
                idPresentacion = articuloTB.getPresentacion();
                txtPresentacion.setText(articuloTB.getPresentacionName().get());
            }

            if (articuloTB.getDepartamento() != 0) {
                idDepartmento = articuloTB.getDepartamento();
                txtDepartamento.setText(articuloTB.getDepartamentoName().get());
            }

            if (articuloTB.getUnidadVenta() == 1) {
                rbUnidad.setSelected(true);
                txtCantidadActual.setText(Tools.roundingValue(articuloTB.getCantidad(), 2));
            } else {
                rbGranel.setSelected(true);
                txtCantidadActual.setText(Tools.roundingValue(articuloTB.getCantidadGranel(), 2));
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
            txtPrecioVenta.setText(Tools.roundingValue(articuloTB.getPrecioVenta(), 2));
            txtPrecioMayoreo.setText(Tools.roundingValue(articuloTB.getPrecioVentaMayoreo(), 2));

            if (articuloTB.getImagenTB().equalsIgnoreCase("")) {
                lnPrincipal.setImage(new Image("/view/no-image.png"));
            } else {
                lnPrincipal.setImage(new Image(new File("" + articuloTB.getImagenTB()).toURI().toString()));
            }

        }
    }

    private void aValidityProcess() {
        if (txtClave.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese la clave del artículo, por favor.", false);
            txtClave.requestFocus();
        } else if (txtNombreMarca.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el nombre del artículo, por favor.", false);
            txtNombreMarca.requestFocus();
        } else if (cbEstado.getSelectionModel().getSelectedIndex() < 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Selecciona el estado del artículo, por favor.", false);
            cbEstado.requestFocus();
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
                articuloTB.setUnidadMedida(idMedida != 0
                        ? idMedida
                        : 0);
                articuloTB.setPresentacion(idPresentacion != 0
                        ? idPresentacion
                        : 0);
                articuloTB.setDepartamento(idDepartmento != 0
                        ? idDepartmento
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

                articuloTB.setPrecioVenta(Tools.isNumeric(txtPrecioVenta.getText())
                        ? Double.parseDouble(txtPrecioVenta.getText())
                        : 0);
                if (articuloTB.getPrecioCompra() > 0) {
                    Double porcentaje = (articuloTB.getPrecioVenta() * 100) / articuloTB.getPrecioCompra();
                    int recalculado = (int) Math.abs((100
                            - (Double.parseDouble(
                                    Tools.roundingValue(Double.parseDouble(
                                            Tools.roundingValue(porcentaje, 2)), 0)))));

                    articuloTB.setMargen((short) recalculado);
                    articuloTB.setUtilidad(articuloTB.getPrecioVenta() - articuloTB.getPrecioCompra());

                } else {
                    articuloTB.setMargen((short) 0);
                    articuloTB.setUtilidad(0);
                }

                articuloTB.setPrecioVentaMayoreo(Tools.isNumeric(txtPrecioMayoreo.getText())
                        ? Double.parseDouble(txtPrecioMayoreo.getText())
                        : 0);

                if (articuloTB.getPrecioCompra() > 0) {
                    Double porcentajeMayoreo = (articuloTB.getPrecioVentaMayoreo() * 100) / articuloTB.getPrecioCompra();
                    int recalculadoMayoreo = (int) Math.abs((100
                            - (Double.parseDouble(
                                    Tools.roundingValue(Double.parseDouble(
                                            Tools.roundingValue(porcentajeMayoreo, 2)), 0)))));

                    articuloTB.setMargenMayoreo((short) recalculadoMayoreo);
                    articuloTB.setUtilidadMayoreo(articuloTB.getPrecioVentaMayoreo() - articuloTB.getPrecioCompra());

                } else {
                    articuloTB.setMargenMayoreo((short) 0);
                    articuloTB.setUtilidadMayoreo(0);

                }

                articuloTB.setEstado(cbEstado.getSelectionModel().getSelectedIndex() >= 0
                        ? cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get()
                        : 0);
                articuloTB.setUnidadVenta(rbUnidad.isSelected() ? 1 : 2);
                articuloTB.setLote(cbLote.isSelected());
                articuloTB.setInventario(cbInventario.isSelected());

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
    private void onKeyTypedPrecioVenta(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecioVenta.getText().contains(".") || c == '-' && txtPrecioVenta.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedPrecioVentaMayoreo(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtPrecioMayoreo.getText().contains(".") || c == '-' && txtPrecioMayoreo.getText().contains("-")) {
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

    private void openWindowDetalle(String title, String idDetalle) throws IOException {
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
        controller.initListDetalle(idDetalle, "");
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
            openWindowDetalle("Agregar Categoría", "0006");
        }
    }

    @FXML
    private void onKeyReleasedMarca(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.SPACE) {
            openWindowDetalle("Agregar Marca", "0007");
        }
    }

    @FXML
    private void onKeyReleasedPresentacion(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.SPACE) {
            openWindowDetalle("Agregar Presentación", "0008");
        }
    }

    @FXML
    private void onMouseClickedCategoria(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalle("Agregar Categoría", "0006");
        }
    }

    @FXML
    private void onMouseClickedMarca(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalle("Agregar Marca", "0007");
        }
    }

    @FXML
    private void onMouseClickedPresentacion(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalle("Agregar Presentación", "0008");
        }
    }

    @FXML
    private void onActionGenerar(ActionEvent event) throws IOException {
        openWindowGerarCodigoBarras();
    }

    @FXML
    private void onMouseClickedDepartamento(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalle("Agregar Departamento", "0014");
        }
    }

    @FXML
    private void onKeyReleasedDepartamento(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.SPACE) {
            openWindowDetalle("Agregar Departamento", "0014");
        }
    }

    @FXML
    private void onKeyTypedDepartamento(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (c != '\b') {
            event.consume();
        }
    }

    @FXML
    private void onMouseClickedMedida(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalle("Agregar Departamento", "0013");
        }
    }

    @FXML
    private void onKeyReleasedMedida(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.SPACE) {
            openWindowDetalle("Agregar Departamento", "0013");
        }
    }

    @FXML
    private void onKeyTypedMedida(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if (c != '\b') {
            event.consume();
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

    public void setIdDepartmento(int idDepartmento) {
        this.idDepartmento = idDepartmento;
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

    public TextField getTxtDepartamento() {
        return txtDepartamento;
    }

    public TextField getTxtMedida() {
        return txtMedida;
    }

    public void initControllerArticulos(FxArticulosController articulosController) {
        this.articulosController = articulosController;
    }

}
