package controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.util.Optional;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
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
    static final String FX_FILE_CLIENTE = "/view/persona/FxCliente.fxml";
    static final String FX_FILE_CONFIGURACION = "/view/inicio/FxConfiguracion.fxml";
    static final String FX_FILE_ASIGNACION = "/view/persona/FxAsignacion.fxml";
    static final String FX_FILE_PROVEEDORES = "/view/proveedores/FxProveedores.fxml";
    static final String FX_FILE_PROVEEDOREPROCESO = "/view/proveedores/FxProveedorProceso.fxml";
    static final String FX_FILE_PROVEEDORLISTA = "/view/proveedores/FxProveedorLista.fxml";
    static final String FX_FILE_REPRESENTANTELISTA = "/view/persona/FxRepresentanteLista.fxml";
    static final String FX_FILE_MIEMPRESA = "/view/miempresa/FxMiEmpresa.fxml";
    static final String FX_FILE_ARTICULO = "/view/articulo/FxArticulos.fxml";
    static final String FX_FILE_ARTICULOPROCESO = "/view/articulo/FxArticuloProceso.fxml";
    static final String FX_FILE_COMPRAS = "/view/compra/FxCompras.fxml";
    static final String FX_FILE_COMPRASREALIZADAS = "/view/compra/FxComprasRealizadas.fxml";
    static final String FX_FILE_CONSULTAS = "/view/inicio/FxConsultas.fxml";
    static final String FX_FILE_ARTICULOLISTA = "/view/articulo/FxArticuloLista.fxml";
    static final String FX_FILE_ARTICULOCOMPRA = "/view/articulo/FxArticuloCompra.fxml";
    
    public static short AlertMessage(Window window, AlertType type, String title, String value, boolean validation) {
        Alert alert = new Alert(type);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/view/icon.png"));
        alert.setTitle(title);
        alert.getDialogPane().setStyle("");
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

    public static String[] getDataPeople(String data) {
        if (!data.equalsIgnoreCase("") || !data.equals(null)) {
            return data.trim().split(" ");
        }
        return null;
    }

    public static String roundingValue(double valor, int decimals) {
        BigDecimal decimal = BigDecimal.valueOf(valor);
        decimal = decimal.setScale(decimals, RoundingMode.HALF_UP);
        return decimal.toPlainString();
    }
    
    public static double calculateTax(int porcentaje,double valor) {
        double igv = ((double)porcentaje / 100.00);
        double impu = valor * igv;
        return impu;
    }
    
    public static double calculateValueNeto(int porcentaje,double valuecalculate) {
        double subprimer = ((double) porcentaje + 100);
        double valor = valuecalculate;
        double totalvalor = valor / (subprimer * 0.01);
        return totalvalor;
    }

    public static void actualDate(String date, DatePicker datePicker) {
        if (date.contains("-")) {
            datePicker.setValue(LocalDate.of(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1]), Integer.parseInt(date.split("-")[2])));
        } else if (date.contains("/")) {
            datePicker.setValue(LocalDate.of(Integer.parseInt(date.split("/")[0]), Integer.parseInt(date.split("/")[1]), Integer.parseInt(date.split("/")[2])));
        }
    }

    public static String getDatePicker(DatePicker datePicker) {
        LocalDate localDate = datePicker.getValue();
        return localDate.toString();
    }

    public static String getDate() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static Timestamp getDateHour() {
        return new Timestamp(new Date().getTime());
    }

}
