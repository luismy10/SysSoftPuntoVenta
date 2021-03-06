package controller;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
    static final String FX_FILE_PROVEEDORES = "/view/proveedores/FxProveedor.fxml";
    static final String FX_FILE_PROVEEDOREPROCESO = "/view/proveedores/FxProveedorProceso.fxml";
    static final String FX_FILE_PROVEEDORLISTA = "/view/proveedores/FxProveedorLista.fxml";
    static final String FX_FILE_MIEMPRESA = "/view/miempresa/FxMiEmpresa.fxml";
    static final String FX_FILE_ARTICULO = "/view/articulo/FxArticulos.fxml";
    static final String FX_FILE_ARTICULOPROCESO = "/view/articulo/FxArticuloProceso.fxml";
    static final String FX_FILE_COMPRAS = "/view/compra/FxCompra.fxml";
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
    static final String FX_FILE_ACTUALIZAR_STOCK = "/view/articulo/FxArticuloActualizarStock.fxml";
    static final String FX_FILE_ARTICULOHISTORIAL = "/view/articulo/FxArticuloHistorial.fxml";
    static final String FX_FILE_VENTADESCUENTO = "/view/venta/FxVentaDescuento.fxml";
    static final String FX_FILE_VENTAGRANEL = "/view/venta/FxVentaGranel.fxml";
    static final String FX_FILE_INVENTARIOGENERAL = "/view/lote/FxInventarioGeneral.fxml";
    static final String FX_FILE_REPORTES = "/view/inicio/FxReportes.fxml";
    static final String FX_FILE_ARTICULOREPORTES = "/view/articulo/FxArticuloReportes.fxml";
    static final String FX_FILE_MONEDA = "/view/moneda/FxMoneda.fxml";
    static final String FX_FILE_MONEDAPROCESO = "/view/moneda/FxMonedaProceso.fxml";
    static final String FX_FILE_IMPRESORATICKET = "/view/venta/FxImpresoraTicket.fxml";
    static final String FX_FILE_TIPODOCUMENTO = "/view/tipodocumento/FxTipoDocumento.fxml";
    static final String FX_FILE_IMPUESTO = "/view/impuesto/FxImpuesto.fxml";
    static final String FX_FILE_IMPUESTOPROCESO = "/view/impuesto/FxImpuestoProceso.fxml";
    static final String FX_FILE_VENTAREPORTE = "/view/venta/FxVentaReporte.fxml";
    static final String FX_FILE_LISTAPRECIOS = "/view/venta/FxListaPrecios.fxml";
    static final String FX_FILE_COMPRASPROCESO = "/view/compra/FxCompraProceso.fxml";
    static final String FX_FILE_PLAZOS = "/view/compra/FxPlazos.fxml";
    static final String FX_FILE_TICKET = "/view/tipodocumento/FxTicket.fxml";
    static final String FX_FILE_HISTORIAL_PAGOS = "/view/compra/FxHistorialPagos.fxml";
    static final String FX_FILE_AMORTIZAR_PAGOS = "/view/compra/FxAmortizarPagos.fxml";

    static final String FX_FILE_CAJA = "/view/caja/FxCaja.fxml";
    static final String FX_FILE_CAJACONSULTAS = "/view/caja/FxCajaConsultas.fxml";
    static final String FX_FILE_CAJAAPERTURA = "/view/caja/FxAperturaCaja.fxml";
    static final String FX_FILE_CAJABUSQUEDA = "/view/caja/FxCajaBusqueda.fxml";
    static final String FX_FILE_CAJAPROCESADOS = "/view/caja/FxCajaProcesasdos.fxml";
    static final String FX_FILE_CAJADETALLEMOVIMIENTO = "/view/caja/FxCajaDetalleMovimiento.fxml";

    static final String FX_FILE_TICKETBUSQUEDA = "/view/tipodocumento/FxTicketBusqueda.fxml";
    static final String FX_FILE_TICKETMULTILINEA = "/view/tipodocumento/FxTicketMultilinea.fxml";

    static final String FX_FILE_TICKETVARIABLE = "/view/tipodocumento/FxTicketVariable.fxml";
    static final String FX_FILE_TICKETPROCESO = "/view/tipodocumento/FxTicketProceso.fxml";
    static final String FX_FILE_TIPODOCUMENTOPROCESO = "/view/tipodocumento/FxTipoDocumentoProceso.fxml";

    static final String FX_FILE_VENTAABONO = "/view/venta/FxVentaAbono.fxml";
    static final String FX_FILE_VENTAABONOPROCESO = "/view/venta/FxVentaAbonoProceso.fxml";
    static final String FX_FILE_VENTAFONDOINICIAL = "/view/venta/FxVentaFondoInicial.fxml";
    static final String FX_FILE_VENTAMOVIMIENTO = "/view/venta/FxVentaMovimiento.fxml";

    static final String FX_FILE_REPORTEOPCION = "/view/articulo/FxReporteOpcion.fxml";

    static final String FX_FILE_VENTAUTILIDAD = "/view/venta/FxVentasUtilidades.fxml";

    static final String FX_FILE_VENTADEVOLUCION = "/view/venta/FxVentaDevolucion.fxml";

    static final String FX_FILE_CAJACERRARCAJA = "/view/caja/FxCajaCerrarCaja.fxml";

    static final String FX_FILE_ETIQUETAS = "/view/etiquetas/FxEtiquetas.fxml";
    static final String FX_FILE_ETIQUETASBUSQUEDA = "/view/etiquetas/FxEtiquetasBusqueda.fxml";
    static final String FX_FILE_ETIQUETASEDITAR = "/view/etiquetas/FxEtiquetasEditar.fxml";
    static final String FX_FILE_ETIQUETASNUEVO = "/view/etiquetas/FxEtiquetasNuevo.fxml";
    static final String FX_FILE_ETIQUETASPREVISUALIZADOR = "/view/etiquetas/FxEtiquetasPrevisualizador.fxml";
    static final String FX_FILE_ETIQUETASPROCESO = "/view/etiquetas/FxEtiquetasProceso.fxml";
    static final String FX_FILE_ETIQUETASPROCESOBUSQUEDA = "/view/etiquetas/FxEtiquetasProcesoBusquedaFxEtiquetasEditar.fxml";
    static final String FX_FILE_IMPRESORAETIQUETA = "/view/etiquetas/FxImpresoraEtiqueta.fxml";
    
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
        alert.getButtonTypes().clear();

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
        alert.getButtonTypes().clear();

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

    public static short AlertMessage(Window window, AlertType type, String title, String value, String buton1, String buton2) {
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

        ButtonType buttonTypeTwo = new ButtonType(buton1, ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeOne = new ButtonType(buton2, ButtonBar.ButtonData.CANCEL_CLOSE);

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
        double igv = (double) (porcentaje / 100.00);
        return (double) (valor * igv);
    }

    public static double calculateAumento(double porcentaje, double costo) {
        double totalimporte = costo + (costo * (porcentaje / 100.00));
        return Double.parseDouble(Tools.roundingValue(totalimporte, 1));
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

    public static boolean isNumericInteger(String cadena) {
        if (cadena == null || cadena.isEmpty()) {
            return false;
        }
        boolean resultado;
        try {
            Integer.parseInt(cadena);
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

    public static String getHour(String format) {
        Date date = new Date();
        SimpleDateFormat hour = new SimpleDateFormat(format);
        return hour.format(date);
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

    public static String leerArchivoTexto(String ruta) {
        String contenido = "";
        InputStream inStream = null;
        BufferedInputStream bis = null;
        try {
            inStream = new FileInputStream(ruta);
            bis = new BufferedInputStream(inStream);
            while (bis.available() > 0) {
                char c = (char) bis.read();
                contenido += c;
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
        return contenido;
    }

    public static JSONObject obtenerObjetoJSON(final String codigoJSON) {
        JSONParser lector = new JSONParser();
        JSONObject objectJSON = null;
        try {
            Object recuperado = lector.parse(codigoJSON);
            objectJSON = (JSONObject) recuperado;
        } catch (ParseException ex) {
            System.out.println("Posicion:" + ex.getPosition());
            System.out.println(ex);
        }
        return objectJSON;
    }

    public static JSONArray obtenerArrayJSON(final String codigoJSON) {
        JSONParser lector = new JSONParser();
        JSONArray arrayJSON = null;

        try {
            Object recuperado = lector.parse(codigoJSON);
            arrayJSON = (JSONArray) recuperado;
        } catch (ParseException e) {
            System.out.println("Posicion: " + e.getPosition());
            System.out.println(e);
        }

        return arrayJSON;
    }

}
