package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.layout.VBox;
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
    static final String FX_FILE_CLIENTE_PROCESO = "/view/persona/FxClienteProceso.fxml";
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
    static final String FX_FILE_MIEMPRESA = "/view/miempresa/FxMiEmpresa.fxml";
    static final String FX_FILE_ARTICULO = "/view/articulo/FxArticulos.fxml";
    static final String FX_FILE_ARTICULOPROCESO = "/view/articulo/FxArticuloProceso.fxml";
    static final String FX_FILE_COMPRAS = "/view/compra/FxCompras.fxml";
    static final String FX_FILE_COMPRASREALIZADAS = "/view/compra/FxComprasRealizadas.fxml";
    static final String FX_FILE_CONSULTAS = "/view/inicio/FxConsultas.fxml";
    static final String FX_FILE_ARTICULOLISTA = "/view/articulo/FxArticuloLista.fxml";
    static final String FX_FILE_ARTICULOCOMPRA = "/view/articulo/FxArticuloCompra.fxml";
    static final String FX_FILE_COMPRASDETALLE = "/view/compra/FxCompraDetalle.fxml";
    static final String FX_FILE_LOTE = "/view/lote/FxLote.fxml";
    static final String FX_FILE_LOTECAMBIAR = "/view/lote/FxLoteCambiar.fxml";
    static final String FX_FILE_LOTEPROCESO = "/view/lote/FxLoteProceso.fxml";
    static final String FX_FILE_REPRESENTANTE = "/view/persona/FxRepresentante.fxml";
    static final String FX_FILE_VENTA = "/view/venta/FxVenta.fxml";
    static final String FX_FILE_VENTAPROCESO = "/view/venta/FxVentaProceso.fxml";
    static final String FX_FILE_VENTAREALIZADAS = "/view/venta/FxVentaRealizadas.fxml";
    static final String FX_FILE_VENTADETALLE = "/view/venta/FxVentaDetalle.fxml";
    static final String FX_FILE_CLIENTELISTA = "/view/persona/FxClienteLista.fxml";
    static final String FX_FILE_ROLES = "/view/login/FxRoles.fxml";
    static final String FX_FILE_ROLESPROCESO = "/view/login/FxRolesProceso.fxml";
    static final String FX_FILE_EMPLEADOS = "/view/login/FxEmpleados.fxml";
    static final String FX_FILE_EMPLEADOSPROCESO = "/view/login/FxEmpleadosProceso.fxml";
    static final String FX_FILE_DETALLELISTA = "/view/mantenimiento/FxDetalleLista.fxml";
    static final String FX_FILE_DETALLEPROCESO = "/view/mantenimiento/FxDetalleProceso.fxml";
    static final String FX_FILE_INVENTARIOINICIAL = "/view/lote/FxInventarioInicial.fxml";
    static final String FX_FILE_IMPORTARINVENTARIO = "/view/lote/FxImportarInventario.fxml";
    static final String FX_FILE_CODIGOBARRAS = "/view/articulo/FxCodigoBarras.fxml";
    static final String FX_FILE_PROVEEDORESREPRENTANTE = "/view/proveedores/FxProveedorRepresentante.fxml";
    static final String FX_FILE_ACTUALIZAR_STOCK = "/view/articulo/FxArticuloActualizarStock.fxml";
    static final String FX_FILE_ARTICULOHISTORIAL = "/view/articulo/FxArticuloHistorial.fxml";
    static final String FX_FILE_VENTADESCUENTO = "/view/venta/FxVentaDescuento.fxml";
    static final String FX_FILE_VENTAGRANEL = "/view/venta/FxVentaGranel.fxml";
    static final String FX_FILE_INVENTARIOGENERAL = "/view/lote/FxInventarioGeneral.fxml";
    static final String FX_FILE_REPORTES = "/view/inicio/FxReportes.fxml";
    static final String FX_FILE_ARTICULOREPORTES = "/view/articulo/FxArticuloReportes.fxml";
    static final String FX_FILE_MONEDA = "/view/mantenimiento/FxMoneda.fxml";
    static final String FX_FILE_MONEDAPROCESO = "/view/mantenimiento/FxMonedaProceso.fxml";
    static final String FX_FILE_IMPRESORATICKET= "/view/venta/FxImpresoraTicket.fxml";
    static final String FX_FILE_TIPODOCUMENTO = "/view/mantenimiento/FxTipoDocumento.fxml";
    
    public static short AlertMessage(Window window, AlertType type, String title, String value, boolean validation) {
        final URL url = Tools.class.getClass().getResource("/view/alert.css");
        Alert alert = new Alert(type);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/view/icon.png"));
        alert.setTitle(title);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(window);
        alert.setHeaderText(null);
        alert.setContentText(value);
        alert.getDialogPane().getStylesheets().add(url.toExternalForm());

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

    public static short AlertMessage(Window window, AlertType type, String title, String value) {
        final URL url = Tools.class.getClass().getResource("/view/alert.css");
        Alert alert = new Alert(type);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/view/icon.png"));
        alert.setTitle(title);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(window);
        alert.setHeaderText(null);
        alert.setContentText(value);
        alert.getDialogPane().getStylesheets().add(url.toExternalForm());

        ButtonType buttonTypeTwo = new ButtonType("Aceptar y no Imprimir", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeOne = new ButtonType("Aceptar e Imprimir", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeTwo, buttonTypeOne);
        Button buttonTwo = (Button) alert.getDialogPane().lookupButton(buttonTypeTwo);
        buttonTwo.setDefaultButton(false);
        Button buttonOne = (Button) alert.getDialogPane().lookupButton(buttonTypeOne);
        buttonTwo.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                alert.setResult(buttonTypeTwo);
            }
        });
        buttonOne.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                alert.setResult(buttonTypeOne);
            }
        });

        Optional<ButtonType> optional = alert.showAndWait();
        return (short) (optional.get() == buttonTypeTwo ? 0 : 1);

    }

    public static void DisposeWindow(AnchorPane window, EventType<KeyEvent> eventType) {
        window.addEventHandler(eventType, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Dispose(window);
                event.consume();
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

    public static Object getValueAt(TableView table, int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < table.getItems().size() && columnIndex >= 0 && columnIndex < table.getColumns().size()) {
            return ((TableColumn) table.getColumns().get(columnIndex)).getCellObservableValue(rowIndex).getValue();
        } else {
            return null;
        }
    }

    public static String[] getDataPeople(String data) {
        if (data != null) {
            return data.trim().split(" ");
        } else {
            return null;
        }
    }

    public static String roundingValue(double valor, int decimals) {
        BigDecimal decimal = BigDecimal.valueOf(valor);
        decimal = decimal.setScale(decimals, RoundingMode.HALF_UP);
        return decimal.toPlainString();
    }

    public static double calculateTax(double porcentaje, double valor) {
        double igv = (porcentaje / 100.00);
        double impu = valor * igv;
        return impu;
    }

    public static String calculateAumento(double porcentaje, double costo) {
        double totalimporte = costo + (costo * (porcentaje / 100));
        double redondeandoimporte = Double.parseDouble(Tools.roundingValue(totalimporte, 1));
        return Tools.roundingValue(redondeandoimporte, 2);
    }

    public static double calculateValueNeto(double porcentaje, double valuecalculate) {
        double subprimer = (porcentaje + 100);
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

    public static boolean isNumeric(String cadena) {
        if (cadena == null || cadena.isEmpty()) {
            return false;
        }
        boolean resultado;
        try {
            Double.parseDouble(cadena);
            resultado = true;
        } catch (NumberFormatException ex) {
            resultado = false;
        }
        return resultado;
    }

    public static boolean isText(String cadena) {
        return (cadena == null || cadena.isEmpty());
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

    public static String getDate(String format) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Timestamp getDateHour() {
        return new Timestamp(new Date().getTime());
    }

    public static void openFile(String ruta) {
        try {
            File path = new File(ruta);
            Desktop.getDesktop().open(path);
        } catch (IOException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.INFO, ex.getLocalizedMessage());
        }
    }

    static void DisposeWindow(VBox window, EventType<KeyEvent> KEY_RELEASED) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
