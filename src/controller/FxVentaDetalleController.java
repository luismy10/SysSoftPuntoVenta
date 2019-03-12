package controller;

import br.com.adilson.util.PrinterMatrix;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javax.print.PrintService;
import model.DBUtil;
import model.DetalleVentaTB;
import model.EmpleadoTB;
import model.VentaADO;
import model.VentaTB;

public class FxVentaDetalleController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblLoad;
    @FXML
    private Text lblFechaVenta;
    @FXML
    private Text lblComprobante;
    @FXML
    private TableView<DetalleVentaTB> tvList;
    @FXML
    private TableColumn<DetalleVentaTB, Integer> tcId;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcDescripcion;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcCantidad;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcImporte;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcMedida;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcPrecio;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcDescuento;
    @FXML
    private Text lblTotal;
    @FXML
    private Text lblCliente;
    @FXML
    private Text lblEstado;
    @FXML
    private Text lblObservaciones;
    @FXML
    private Text lblVendedor;
    @FXML
    private Text lblSerie;

    private String idVenta;

    private FxVentaRealizadasController ventaRealizadasController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        tcId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        tcDescripcion.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getArticuloTB().getNombreMarca()));
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getCantidad(), 2)));
        tcMedida.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getArticuloTB().getUnidadVenta() == 1 ? "Por Unidad/Pza" : "A Granel"));
        tcPrecio.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getPrecioVenta(), 2)));
        tcDescuento.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getDescuento(), 2)));
        tcImporte.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getImporte(), 2)));
    }

    private void fillVentasDetalleTable(String value) throws SQLException {
        tvList.setItems(VentaADO.ListVentasDetalle(value));
        lblLoad.setVisible(false);
    }

    public void setInitComponents(LocalDateTime fechaRegistro, String cliente, String comprobanteName, String serie, String numeracion, String observaciones, String idVenta) {
        lblFechaVenta.setText(fechaRegistro.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
        lblCliente.setText(cliente);
        lblComprobante.setText(comprobanteName);
        lblSerie.setText(serie + "-" + numeracion);
        lblObservaciones.setText(observaciones);
        this.idVenta = idVenta;
        DBUtil.dbConnect();
        if (DBUtil.getConnection() != null) {
            try {
                VentaTB ventaTB = VentaADO.GetVenta(idVenta);
                if (ventaTB != null) {
                    lblEstado.setText(ventaTB.getEstadoName());
                    lblTotal.setText(ventaTB.getMonedaName() + " " + Tools.roundingValue(ventaTB.getTotal(), 2));
                }
                EmpleadoTB empleadoTB = VentaADO.GetEmpleadoVenta(idVenta);
                if (empleadoTB != null) {
                    lblVendedor.setText(empleadoTB.getApellidos() + " " + empleadoTB.getNombres());
                }
                fillVentasDetalleTable(idVenta);
            } catch (SQLException ex) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Detalle de venta", "No se pudo completar la petición, intente nuevamente.\n Error en: " + ex.getLocalizedMessage(), false);
            } finally {
                DBUtil.dbDisconnect();
            }
        }

    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        short validate = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Detalle de ventas", "¿Está seguro de cancelar la venta?", true);
        if (validate == 1) {
            String result = VentaADO.CancelTheSale(idVenta, tvList);
            if (result.equalsIgnoreCase("update")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle de venta", "Se ha cancelado con éxito", false);
//                ventaRealizadasController.fillVentasTable("");
                Tools.Dispose(window);
            } else if (result.equalsIgnoreCase("scrambled")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle de venta", "Ya está cancelada la venta!", false);
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle de venta", result, false);

            }
        }
    }

    

    public void imprimirVenta(String ticket) {
        if (Session.STATE_IMPRESORA && Session.NAME_IMPRESORA != null && Session.CORTA_PAPEL != null) {

            Date date = new Date();
            SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat hora = new SimpleDateFormat("hh:mm:ss aa");

            try {
                String ruc = "RUC " + Session.RUC_EMPRESA;
                String telcel = "TEL: " + Session.TELEFONO_EMPRESA + " CEL:" + Session.CELULAR_EMPRESA;
                String documento = "";
                if (ticket.substring(0, 1).equalsIgnoreCase("b")) {
                    documento = "BOLETA DE VENTA ELECTRONICA";
                } else if (ticket.substring(0, 1).equalsIgnoreCase("f")) {
                    documento = "FACTURA DE VENTA ELECTRONICA";
                } else if (ticket.substring(0, 1).equalsIgnoreCase("t")) {
                    documento = "NOTA DE VENTA";
                }
                String efectivo = "EFECTIVO SOLES  --";
                String vuelto = "Cambio  --";
                String total = lblTotal.getText();
                PrinterMatrix p = new PrinterMatrix();

                int filas = tvList.getItems().size();

                int count = 13;

                p.setOutSize(getSizePaper(filas, count), 40);

                p.printTextWrap(1, 0, (int) (40 - Session.NOMBRE_EMPRESA.length()) / 2, 40, Session.NOMBRE_EMPRESA);
                p.printTextWrap(2, 0, (int) (40 - ruc.length()) / 2, 40, ruc);
                p.printTextWrap(3, 1, 0, 40, Session.DIRECCION_EMPRESA);
                p.printTextWrap(5, 0, (int) (40 - telcel.length()) / 2, 40, telcel);

                p.printTextWrap(6, 0, (int) (40 - documento.length()) / 2, 40, documento);
                p.printTextWrap(7, 0, (int) (40 - ticket.length()) / 2, 40, ticket);
                p.printTextWrap(8, 0, 0, 40, "FECHA DE EMISION:" + fecha.format(date) + " " + hora.format(date));

                p.printCharAtCol(10, 0, 40, "=");

                p.printTextWrap(10, 1, 0, 40, "Descripcion");
                p.printTextWrap(11, 0, 0, "Cantidad x P.Unitario".length(), "Cantidad x P.Unitario");
                p.printTextWrap(11, 0, (40 - "Importe".length()), 40, "Importe");

                p.printCharAtCol(13, 0, 40, "=");

                for (int i = 0; i < filas; i++) {
                    p.printTextWrap(count, 1, 0, 40, Tools.getValueTable(tvList, 1));
                    count += 2;
                    p.printTextWrap(count, 0, 0, 40, Tools.getValueTable(tvList, 2) + " x " + Tools.getValueTable(tvList, 4));
                    p.printTextWrap(count, 0, 40 - Tools.getValueTable(tvList, 6).length(), 40, Tools.getValueTable(tvList, 6));
                    count++;
                }
                count++;

                p.printCharAtCol(count, 0, 40, "=");

                p.printTextWrap(count, 1, 0, 40, "IMPORTE TOTAL");
                p.printTextWrap(count, 1, 40 - total.length(), 40, total);

                count += 2;
                p.printCharAtCol(count, 0, 40, "=");

                p.printTextWrap(count, 1, 40 - efectivo.length(), 40, efectivo);

                count++;
                p.printTextWrap(count, 0, 40 - vuelto.length(), 40, vuelto);

                count += 2;
                p.printCharAtCol(count, 0, 40, "=");

                p.printTextWrap(count, 1, (int) (40 - "Representacion Impresa del Documento".length()) / 2, 40, "Representacion Impresa del Documento");

                count++;
                p.printTextWrap(count, 0, (int) (40 - "de Venta Electronica".length()) / 2, 40, "de Venta Electronica");

                count++;
                p.printTextWrap(count, 0, 0, 40, "GRACIAS POR SU COMPRA...");

                count++;
                p.printTextWrap(count, 0, 0, 40, "\n");

                count++;
                p.printTextWrap(count, 0, 0, 40, "\n");

                count++;
                p.printTextWrap(count, 0, 0, 40, "\n");

                count++;
                p.printTextWrap(count, 0, 0, 40, "\n");

                count++;
                p.printTextWrap(count, 0, 0, 40, "\n");

                p.toFile("c:\\temp\\impresion.txt");
//                File file = new File("c:\\temp\\impresion.txt");
//                FileInputStream inputStream = null;
//                try {
//                    try {
//                        inputStream = new FileInputStream(file);
//                    } catch (FileNotFoundException ex) {
//
//                    }
//                    if (inputStream == null) {
//                        return;
//                    }
//                    DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
//                    PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
//                    PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
//                    PrintService service = findPrintService(Session.NAME_IMPRESORA, printService);
//                    DocPrintJob job = service.createPrintJob();
//
//                    byte[] bytes = readFileToByteArray(file);
//                    byte[] cutP = new byte[]{0x1d, 'V', 1};
//                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                    outputStream.write(bytes);
//                    outputStream.write(cutP);
//                    byte c[] = outputStream.toByteArray();
//
//                    Doc doc = new SimpleDoc(c, flavor, null);
//
//                    job.print(doc, null);
//                } catch (IOException | PrintException e) {
//                    // TODO Auto-generated catch block
//
//                } finally {
//                    if (inputStream != null) {
//                        try {
//                            inputStream.close();
//                        } catch (IOException ex) {
//
//                        }
//                    }
//                }
            } catch (Exception e) {
                System.out.println("Error de impresion de venta: "+e);
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Venta", "Error al imprimir, configure correctamente su impresora.", false);
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Venta", "No esta configurado la impresora :D", false);

        }

    }

    private int getSizePaper(int filas, int inicial) {
        int recorrido = inicial;
        for (int i = 0; i < filas; i++) {
            recorrido += 2;
            recorrido++;
        }
        recorrido++;
        recorrido += 2;
        recorrido++;
        recorrido += 2;
        recorrido++;
        recorrido++;
        recorrido++;
        recorrido++;
        recorrido++;
        recorrido++;
        recorrido++;
        recorrido++;
        return recorrido;
    }

    private PrintService findPrintService(String printerName, PrintService[] services) {
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }

    private static byte[] readFileToByteArray(File file) {
        byte[] bArray = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bArray);
            fis.close();

        } catch (IOException ioExp) {
        }
        return bArray;
    }
    
    @FXML
    private void onKeyPressedImprimir(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            imprimirVenta(lblSerie.getText());
        }
    }

    @FXML
    private void onActionImprimir(ActionEvent event) {
         imprimirVenta(lblSerie.getText());
    }

    public void setInitVentasController(FxVentaRealizadasController ventaRealizadasController) {
        this.ventaRealizadasController = ventaRealizadasController;
    }

}
