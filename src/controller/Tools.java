package controller;

import java.util.Optional;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Tools {

    static final String FX_FILE_DIRECTORIO = "/view/directorio/FxDirectorio.fxml";
    static final String FX_FILE_DETALLE_MATENIMIENTO = "/view/mantenimiento/FxDetalleMantenimiento.fxml";
    static final String FX_FILE_DETALLE = "/view/mantenimiento/FxDetalle.fxml";
    static final String FX_FILE_MANTENIMIENTO = "/view/mantenimiento/FxMantenimiento.fxml";
    static final String FX_FILE_LOGIN = "/view/login/FxLogin.fxml";
    static final String FX_LOGO = "/view/icon.png";
    static final String FX_FILE_PERSONA = "/view/persona/FxPersona.fxml";
    static final String FX_FILE_PERFIL = "/view/persona/FxPerfil.fxml";
    static final String FX_FILE_INICIO = "/view/inicio/FxInicio.fxml";
    static final String FX_FILE_OPERACIONES = "/view/inicio/FxOperaciones.fxml";
    static final String FX_FILE_PRINCIPAL = "/view/inicio/FxPrincipal.fxml";

    public static short AlertMessage(Window window, AlertType type, String title, String value, boolean validation) {
        Alert alert = new Alert(type);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/view/icon.png"));
        alert.setTitle(title);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(window);
        alert.setHeaderText(null);
        alert.setContentText(value);
        ButtonType buttonTypeOne = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeTwo = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonTypeClose = new ButtonType("Aceptar", ButtonBar.ButtonData.CANCEL_CLOSE);
        if (validation) {
            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            Button buttonOne = (Button) alert.getDialogPane().lookupButton(buttonTypeOne);
            buttonOne.setDefaultButton(false);
            Button buttonTwo = (Button) alert.getDialogPane().lookupButton(buttonTypeTwo);
            buttonOne.setOnKeyPressed((KeyEvent event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    alert.setResult(buttonTypeOne);
                }
            });
            buttonTwo.setOnKeyPressed((KeyEvent event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    alert.setResult(buttonTypeTwo);
                }
            });
            Optional<ButtonType> optional = alert.showAndWait();
            return (short) (optional.get() == buttonTypeOne ? 1 : 0);
        } else {
            alert.getButtonTypes().setAll(buttonTypeClose);
            Button buttonOne = (Button) alert.getDialogPane().lookupButton(buttonTypeClose);
            buttonOne.setOnKeyPressed((KeyEvent event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    alert.setResult(buttonTypeClose);
                }
            });
            Optional<ButtonType> optional = alert.showAndWait();
            return (short) (optional.get() == buttonTypeClose ? 1 : 0);
        }
    }

    public static void DisposeWindow(AnchorPane window, EventType<KeyEvent> eventType) {
        window.addEventHandler(eventType, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Dispose(window);
            }
        });
    }

    public static void Dispose(AnchorPane window) {
        Stage stage = (Stage) window.getScene().getWindow();
        stage.close();
    }

    public static String getValueTable(TableView table, int position) {
        return Tools.getValueAt(table, table.getSelectionModel().getSelectedIndex(), position).toString();
    }

    private static Object getValueAt(TableView table, int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < table.getItems().size() && columnIndex >= 0 && columnIndex < table.getColumns().size()) {
            return ((TableColumn) table.getColumns()).getCellObservableValue(rowIndex).getValue();
        } else {
            return null;
        }
    }
    

}
