package controller;

import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.DBUtil;
import model.DetalleADO;
import model.DetalleTB;

public class FxDetalleController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtCode;
    @FXML
    private TextField txtName;
    @FXML
    private Label lblTitle;
    @FXML
    public TextArea txtDescripcion;
    @FXML
    public ComboBox<Estado> cbEstado;

    private boolean stateconnect;

    private int idDetalle;
    @FXML
    private Button btnToAction;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        cbEstado.getItems().add(new Estado("1", "Habilitado"));
        cbEstado.getItems().add(new Estado("0", "Inhabilitado"));
        cbEstado.setConverter(new javafx.util.StringConverter<Estado>() {
            @Override
            public String toString(Estado object) {
                return object.getNombre();
            }

            @Override
            public Estado fromString(String string) {
                return cbEstado.getItems().stream().filter(ap
                        -> ap.getNombre().equals(string)).findFirst().orElse(null);
            }
        });
        cbEstado.setValue(new Estado("1", "Habilitado"));
        System.out.println(cbEstado.getItems().get(1).getNombre());

    }

    public void setValueAdd(String... values) {
        lblTitle.setText("Detalle del mantenimiento del " + values[0].toLowerCase());
        txtCode.setText(values[1]);
        idDetalle = Integer.parseInt(values[2]);

    }

    public void setValueUpdate(String... values) {
        lblTitle.setText("Detalle del mantenimiento del " + values[0].toLowerCase());
        txtCode.setText(values[1]);
        idDetalle = Integer.parseInt(values[2]);
        btnToAction.setText("Actualizar");
        btnToAction.getStyleClass().add("buttonFourth");

    }

    private void aValidityProcess() {
        if (txtCode.getText().equalsIgnoreCase("")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle", "Ingrese el código, por favor.", false);
            txtCode.requestFocus();
        } else if (txtName.getText().equalsIgnoreCase("")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle", "Ingrese el nombre, por favor.", false);
            txtName.requestFocus();
        } else {
            stateconnect = DBUtil.StateConnection();
            if (stateconnect) {
                short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Mantenimiento", "¿Esta seguro de continuar?", true);
                if (confirmation == 1) {
                    DetalleTB detalleTB = new DetalleTB();
                    detalleTB.setIdDetalle(idDetalle);
                    detalleTB.setIdMantenimiento(txtCode.getText());
                    detalleTB.setNombre(txtName.getText());
                    detalleTB.setDescripcion(txtDescripcion.getText());
                    detalleTB.setEstado(cbEstado.getValue().getId());
                    detalleTB.setUsuarioRegistro("76423388");
                    String result = DetalleADO.CrudEntity(detalleTB);
                    if (result.equalsIgnoreCase("registered")) {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle", "Registrado correctamente.", false);

                    } else if (result.equalsIgnoreCase("duplicate")) {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle", "No se puede haber 2 detalles con el mismo nombre.", false);

                    } else if (result.equalsIgnoreCase("error")) {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle", "No se puedo completar la ejecución.", false);
                    } else {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Detalle", result, false);
                    }
                }
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Detalle", "No hay conexión al servidor.", false);
            }
        }
    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) {
        System.out.println(cbEstado.getValue().getId());
    }

    @FXML
    private void onPressedToCancel(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionToCancel(ActionEvent event) {
        Tools.Dispose(window);
    }

    @FXML
    public void OnKeyPressedDescripcion(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.TAB) {
            Node node = (Node) keyEvent.getSource();
            if (node instanceof TextArea) {
                TextAreaSkin skin = (TextAreaSkin) ((TextArea) node).getSkin();
                if (keyEvent.isShiftDown()) {
                    skin.getBehavior().traversePrevious();
                } else {
                    skin.getBehavior().traverseNext();
                }
            }
            keyEvent.consume();
        }
    }

    public class Estado {

        private String Id;
        private String Nombre;

        public Estado() {

        }

        public Estado(String Id, String Nombre) {
            this.Id = Id;
            this.Nombre = Nombre;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getNombre() {
            return Nombre;
        }

        public void setNombre(String Nombre) {
            this.Nombre = Nombre;
        }

    }
}
