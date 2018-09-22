package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import model.ArticuloADO;
import model.ArticuloTB;
import model.DetalleADO;
import model.DetalleTB;
import model.ImageADO;
import model.ImagenTB;

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
    private ComboBox<DetalleTB> cbCategoria;
    @FXML
    private ComboBox<DetalleTB> cbMarca;
    @FXML
    private ComboBox<DetalleTB> cbPresentacion;
    @FXML
    private ComboBox<DetalleTB> cbEstado;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private ImageView lnPrincipal;
    @FXML
    private Button btnRegister;

    private String idArticulo;

    private long idImagen;

    private File selectFile;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idArticulo = "";
        idImagen = 0;
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        DetalleADO.GetDetailIdName("2", "0006", "").forEach(e -> {
            cbCategoria.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        DetalleADO.GetDetailIdName("2", "0007", "").forEach(e -> {
            cbMarca.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        DetalleADO.GetDetailIdName("2", "0001", "").forEach(e -> {
            cbEstado.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        DetalleADO.GetDetailIdName("2", "0008", "").forEach(e -> {
            cbPresentacion.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
    }

    public void setValueEdit(String value) {
        btnRegister.setText("Actualizar");
        ArrayList<ArticuloTB> list = ArticuloADO.GetArticulosById(value);
        if (!list.isEmpty()) {
            ArticuloTB articuloTB = list.get(0);
            idArticulo = articuloTB.getIdArticulo();
            txtClave.setText(articuloTB.getClave().get());
            txtClaveAlterna.setText(articuloTB.getClaveAlterna());
            txtNombreMarca.setText(articuloTB.getNombre().get());
            txtNombreGenerico.setText(articuloTB.getNombreGenerico());
            txtDescripcion.setText(articuloTB.getDescripcion());

            ObservableList<DetalleTB> lscate = cbCategoria.getItems();
            if (articuloTB.getCategorio() != 0) {
                for (int i = 0; i < lscate.size(); i++) {
                    if (articuloTB.getCategorio() == lscate.get(i).getIdDetalle().get()) {
                        cbCategoria.getSelectionModel().select(i);
                        break;
                    }
                }
            }

            ObservableList<DetalleTB> lsmar = cbMarca.getItems();
            if (articuloTB.getMarcar() != 0) {
                for (int i = 0; i < lsmar.size(); i++) {
                    if (articuloTB.getMarcar() == lsmar.get(i).getIdDetalle().get()) {
                        cbMarca.getSelectionModel().select(i);
                        break;
                    }
                }
            }

            loadViewImage(idArticulo);

        }
    }

    private void loadViewImage(String idRepresentante) {
        ImagenTB imagenTB = ImageADO.GetImage(idRepresentante);
        if (imagenTB.getIdImage() > 0) {
            idImagen = imagenTB.getIdImage();
            lnPrincipal.setImage(imagenTB.getImagen());
        }
    }

    private void aValidityProcess() {
        if (txtClave.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese la clave del artículo, por favor.", false);

            txtClave.requestFocus();
        } else if (txtNombreMarca.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "Ingrese el nombre del artículo, por favor.", false);

            txtNombreMarca.requestFocus();
        } else {
            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Articulo", "¿Esta seguro de continuar?", true);
            if (confirmation == 1) {
                ArticuloTB articuloTB = new ArticuloTB();
                articuloTB.setIdArticulo(idArticulo);
                articuloTB.setClave(txtClave.getText());
                articuloTB.setClaveAlterna(txtClaveAlterna.getText());
                articuloTB.setNombre(txtNombreMarca.getText());
                articuloTB.setNombreGenerico(txtNombreGenerico.getText());
                articuloTB.setDescripcion(txtDescripcion.getText());
                articuloTB.setImagenTB(new ImagenTB(selectFile != null
                        ? getFileResources(selectFile)
                        : getFileResources(null)));
                articuloTB.setCategorio(cbCategoria.getSelectionModel().getSelectedIndex() >= 0
                        ? cbCategoria.getSelectionModel().getSelectedItem().getIdDetalle().get()
                        : 0);
                articuloTB.setMarcar(cbMarca.getSelectionModel().getSelectedIndex() >= 0
                        ? cbMarca.getSelectionModel().getSelectedItem().getIdDetalle().get()
                        : 0);
                articuloTB.setPresentacion(cbPresentacion.getSelectionModel().getSelectedIndex() >= 0
                        ? cbPresentacion.getSelectionModel().getSelectedItem().getIdDetalle().get()
                        : 0);
                
                String result = ArticuloADO.CrudEntity(articuloTB);
                switch (result) {
                    case "registered":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Articulo", "Registrado correctamente el artículo.", false);
                        Tools.Dispose(window);
                        break;
                    case "updated":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Articulo", "Actualizado correctamente el artículo.", false);
                        break;
                    case "duplicate":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "No se puede haber 2 artículos con la misma clave.", false);
                        txtClave.requestFocus();
                        break;
                    case "error":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "No se puedo completar la ejecución.", false);
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

    private void inProcessImage(InputStream inputStream) {
        ImagenTB imagenTB = new ImagenTB();
        imagenTB.setIdImage(idImagen);
        imagenTB.setFile(inputStream);
        imagenTB.setIdRelacionado(idArticulo);
        String result = ImageADO.CrudImage(imagenTB);
        if (result.equalsIgnoreCase("insert")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Articulo", "Registrado correctamente la imagen.", false);
            loadViewImage(idArticulo);
        } else if (result.equalsIgnoreCase("update")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Articulo", "Actualizado correctamente la imagen.", false);
        } else if (result.equalsIgnoreCase("error")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Articulo", "No se puedo completar la ejecución.", false);
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Articulo", result, false);
        }
    }

    @FXML
    private void onActionPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar una imagen");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Elija una imagen", "*.png", "*.jpg", "*.jpeg"));
        selectFile = fileChooser.showOpenDialog(window.getScene().getWindow());
        if (selectFile != null) {
            selectFile = new File(selectFile.getAbsolutePath());
            if (selectFile.getName().endsWith("png") || selectFile.getName().endsWith("jpg") || selectFile.getName().endsWith("jpeg")) {
                lnPrincipal.setImage(new Image(selectFile.toURI().toString()));
                if (!idArticulo.equalsIgnoreCase("")) {
                    try (InputStream inputStream = new FileInputStream(selectFile)) {
                        inProcessImage(inputStream);
                    } catch (Exception ex) {
                    }
                }

            }
        }
    }

    @FXML
    private void onActionRemovePhoto(ActionEvent event) throws IOException {
        lnPrincipal.setImage(new Image("/view/no-image.png"));
        InputStream is = getClass().getResourceAsStream("/view/no-image.png");
        inProcessImage(is);
    }

    private InputStream getFileResources(File file) {
        InputStream inputStream = null;
        try {
            if (file != null) {
                inputStream = new FileInputStream(file);
            } else {
                inputStream = getClass().getResourceAsStream("/view/no-image.png");
            }
        } catch (FileNotFoundException ex) {
        }
        return inputStream;
    }

}
