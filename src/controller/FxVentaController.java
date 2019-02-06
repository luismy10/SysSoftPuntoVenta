package controller;

import br.com.adilson.util.PrinterMatrix;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import model.ArticuloADO;
import model.ArticuloTB;
import model.ComprobanteADO;
import model.MonedaADO;
import model.MonedaTB;
import model.TipoDocumentoADO;
import model.TipoDocumentoTB;
import model.VentaTB;

public class FxVentaController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private ComboBox<TipoDocumentoTB> cbComprobante;
    @FXML
    private ComboBox<MonedaTB> cbMoneda;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, String> tcArticulo;
    @FXML
    private TableColumn<ArticuloTB, String> tcCantidad;
    @FXML
    private TableColumn<ArticuloTB, String> tcPrecio;
    @FXML
    private TableColumn<ArticuloTB, String> tcDescuento;
    @FXML
    private TableColumn<ArticuloTB, String> tcImporte;
    @FXML
    private Text lblSubTotal;
    @FXML
    private Text lblDescuento;
    @FXML
    private Text lblGravada;
    @FXML
    private Text lblIgv;
    @FXML
    private Text lblTotal;
    @FXML
    private Text lblSerie;
    @FXML
    private Text lblNumeracion;
    @FXML
    private TextField txtCliente;
    @FXML
    private Text lblTotalPagar;
    @FXML
    private TextField txtSearch;
    @FXML
    private Text lblMoneda;
    @FXML
    private Text lblSubTotalMoneda;
    @FXML
    private Text lblDescuentoMoneda;
    @FXML
    private Text lblExonerado;
    @FXML
    private Text lblExoneradoMoneda;
    @FXML
    private Text lblGravadaMoneda;
    @FXML
    private Text lblIgvMoneda;
    @FXML
    private Text lblTotalPagarMoneda;

    private AnchorPane content;

    private double subTotal;

    private double descuento;

    private String idCliente;

    private String datodCliente;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        window.setOnKeyReleased((KeyEvent event) -> {
            try {
                if (null != event.getCode()) {
                    switch (event.getCode()) {
                        case F5:
                            openWindowGranel("Cambiar precio al Artículo", false);
                            event.consume();
                            break;
                        case F7:
                            openWindowGranel("Sumar precio al Artículo", true);
                            event.consume();
                            break;
                        case F1:
                            openWindowVentaProceso();
                            event.consume();
                            break;
                        case F2:
                            openWindowArticulos();
                            event.consume();
                            break;
                        case F8:

                            break;
                        case F9:
                            short value = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Venta", "¿Está seguro de borrar la venta?", true);
                            if (value == 1) {
                                resetVenta();
                                event.consume();
                            }
                            event.consume();
                            break;
                        case DELETE:
                            removeArticulo();
                            event.consume();
                            break;
                        default:

                            break;
                    }
                }
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        });
        setClienteVenta(Session.IDCLIENTE, Session.DATOSCLIENTE);
        lblMoneda.setText(Session.MONEDA);
        lblSubTotalMoneda.setText(Session.MONEDA);
        lblDescuentoMoneda.setText(Session.MONEDA);
        lblExoneradoMoneda.setText(Session.MONEDA);
        lblGravadaMoneda.setText(Session.MONEDA);
        lblIgvMoneda.setText(Session.MONEDA);
        lblTotalPagarMoneda.setText(Session.MONEDA);
        initTable();
        loadWindow();
    }

    private void loadWindow() {
        cbComprobante.getItems().clear();
        TipoDocumentoADO.GetDocumentoCombBox().forEach(e -> {
            cbComprobante.getItems().add(new TipoDocumentoTB(e.getIdTipoDocumento(), e.getNombre(), e.isPredeterminado()));
        });
        if (!cbComprobante.getItems().isEmpty()) {
            for (int i = 0; i < cbComprobante.getItems().size(); i++) {
                if (cbComprobante.getItems().get(i).isPredeterminado() == true) {
                    cbComprobante.getSelectionModel().select(i);
                    Session.DEFAULT_COMPROBANTE = i;
                    break;
                }
            }
        }

        txtCliente.setText(Session.DATOSCLIENTE);

        cbMoneda.getItems().clear();
        MonedaADO.GetMonedasCombBox().forEach(e -> {
            cbMoneda.getItems().add(new MonedaTB(e.getIdMoneda(), e.getNombre(), e.getPredeterminado()));
        });

        if (!cbMoneda.getItems().isEmpty()) {
            for (int i = 0; i < cbMoneda.getItems().size(); i++) {
                if (cbMoneda.getItems().get(i).getPredeterminado() == true) {
                    cbMoneda.getSelectionModel().select(i);
                    Session.DEFAULT_MONEDA = i;
                    break;
                }
            }
        }

        String[] array = ComprobanteADO.GetSerieNumeracion().split("-");
        lblSerie.setText(array[0]);
        lblNumeracion.setText(array[1]);
    }

    private void initTable() {
        tcArticulo.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClave() + "\n" + cellData.getValue().getNombreMarca()
        ));
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getCantidad(), 2)));
        tcPrecio.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getPrecioVenta(), 2)));
        tcDescuento.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getDescuento().get(), 2)));
        tcImporte.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getImporte().get(), 2)));
    }

    private void InitializationTransparentBackground() {
        Session.pane.setStyle("-fx-background-color: black");
        Session.pane.setTranslateX(0);
        Session.pane.setTranslateY(0);
        Session.pane.setPrefWidth(Session.WIDTH_WINDOW);
        Session.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.pane.setOpacity(0.7f);
        content.getChildren().add(Session.pane);
    }

    private void openWindowArticulos() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_ARTICULOLISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxArticuloListaController controller = fXMLLoader.getController();
        controller.setInitVentasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Seleccione un Artículo", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
        controller.fillProvidersTable("");
    }

    private void openWindowVentaProceso() throws IOException {
        if (!tvList.getItems().isEmpty()) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_VENTAPROCESO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxVentaProcesoController controller = fXMLLoader.getController();
            controller.setInitVentasController(this);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Completar la venta", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();
            VentaTB ventaTB = new VentaTB();
            ventaTB.setCliente(idCliente);
            ventaTB.setVendedor(Session.USER_ID);
            ventaTB.setComprobante(cbComprobante.getSelectionModel().getSelectedIndex() >= 0
                    ? cbComprobante.getSelectionModel().getSelectedItem().getIdTipoDocumento()
                    : 0
            );
            ventaTB.setComprobanteName(cbComprobante.getSelectionModel().getSelectedIndex() >= 0
                    ? cbComprobante.getSelectionModel().getSelectedItem().getNombre()
                    : "");
            ventaTB.setMoneda(cbMoneda.getSelectionModel().getSelectedIndex() >= 0 ? cbMoneda.getSelectionModel().getSelectedItem().getIdMoneda() : 0);
            ventaTB.setSerie(lblSerie.getText());
            ventaTB.setNumeracion(lblNumeracion.getText());
            ventaTB.setFechaVenta(Timestamp.valueOf(Tools.getDate() + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
            ventaTB.setSubTotal(Double.parseDouble(lblSubTotal.getText()));
            ventaTB.setGravada(Double.parseDouble(lblGravada.getText()));
            ventaTB.setDescuento(Double.parseDouble(lblDescuento.getText()));
            ventaTB.setIgv(Double.parseDouble(lblIgv.getText()));
            ventaTB.setTotal(Double.parseDouble(lblTotalPagar.getText()));            

            controller.setInitComponents(ventaTB, txtCliente.getText(), tvList);
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Ventas", "Debes agregar artículos a la venta", false);
        }

    }

    private void openWindowImpresora() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_IMPRESORATICKET);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxImpresoraTicketController controller = fXMLLoader.getController();
        controller.setInitVentasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Configurar impresora", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();

    }

    private void openWindowCliente() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_CLIENTELISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxClienteListaController controller = fXMLLoader.getController();
        controller.setInitVentasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Elija un cliente", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
        controller.fillCustomersTable("");
    }

    private void openWindowDescuento() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_VENTADESCUENTO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxVentaDescuentoController controller = fXMLLoader.getController();
            controller.setInitVentasController(this);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Descuento del Artículo", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();
            controller.initComponents(tvList.getSelectionModel().getSelectedItem(), tvList.getSelectionModel().getSelectedIndex());
        } else {
            tvList.requestFocus();
        }

    }

    private void openWindowGranel(String title, boolean opcion) throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            InitializationTransparentBackground();
            URL url = getClass().getResource(Tools.FX_FILE_VENTAGRANEL);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxVentaGranelController controller = fXMLLoader.getController();
            controller.setInitVentasController(this);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, title, window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnHiding((WindowEvent WindowEvent) -> {
                content.getChildren().remove(Session.pane);
            });
            stage.show();
            controller.initComponents(
                    title,
                    tvList.getSelectionModel().getSelectedItem(),
                    tvList.getSelectionModel().getSelectedIndex(),
                    opcion
            );
        } else {
            tvList.requestFocus();
        }

    }

    public void getAddArticulo(ArticuloTB articulo) {
        if (validateDuplicateArticulo(tvList, articulo)) {
            for (int i = 0; i < tvList.getItems().size(); i++) {
                if (tvList.getItems().get(i).getIdArticulo().equalsIgnoreCase(articulo.getIdArticulo())) {
                    if (articulo.getUnidadVenta() == 2) {
                        tvList.getSelectionModel().select(i);
                        txtSearch.requestFocus();
                    } else {
                        ArticuloTB articuloTB = tvList.getItems().get(i);
                        articuloTB.setCantidad(tvList.getItems().get(i).getCantidad() + 1);
                        articuloTB.setSubTotal(articuloTB.getCantidad() * articuloTB.getPrecioVenta());
                        articuloTB.setImporte(
                                articuloTB.getSubTotal().get()
                                - articuloTB.getDescuento().get()
                        );
                        tvList.getItems().set(i, articuloTB);
                        calculateTotales();
                        txtSearch.requestFocus();
                    }
                }
            }

        } else {
            if (articulo.getUnidadVenta() == 2) {
                try {
                    tvList.getItems().add(articulo);
                    int index = tvList.getItems().size() - 1;
                    tvList.requestFocus();
                    tvList.getSelectionModel().select(index);
                    openWindowGranel("Cambiar precio al Artículo", false);
                } catch (IOException ex) {
                }
            } else {
                tvList.getItems().add(articulo);
                calculateTotales();
                txtSearch.requestFocus();
            }
        }

    }

    private boolean validateDuplicateArticulo(TableView<ArticuloTB> view, ArticuloTB articuloTB) {
        boolean ret = false;
        for (int i = 0; i < view.getItems().size(); i++) {
            if (view.getItems().get(i).getClave().equals(articuloTB.getClave())) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public void resetVenta() {
        String[] array = ComprobanteADO.GetSerieNumeracionEspecifico(this.cbComprobante.getSelectionModel().getSelectedItem().getNombre()).split("-");
        lblSerie.setText(array[0]);
        lblNumeracion.setText(array[1]);
        this.tvList.getItems().clear();
        lblMoneda.setText(Session.MONEDA);
        lblSubTotalMoneda.setText(Session.MONEDA);
        lblDescuentoMoneda.setText(Session.MONEDA);
        lblExoneradoMoneda.setText(Session.MONEDA);
        lblGravadaMoneda.setText(Session.MONEDA);
        lblIgvMoneda.setText(Session.MONEDA);
        
        lblTotal.setText("0.00");
        lblSubTotal.setText("0.00");
        lblDescuento.setText("0.00");
        lblGravada.setText("0.00");
        lblIgv.setText("0.00");
        lblTotalPagar.setText("0.00");
        setClienteVenta(Session.IDCLIENTE, Session.DATOSCLIENTE);

        cbMoneda.getSelectionModel().select(Session.DEFAULT_MONEDA);
        cbComprobante.getSelectionModel().select(Session.DEFAULT_COMPROBANTE);
        txtSearch.requestFocus();
    }

    public void imprimirVenta(VentaTB ventaTB, String efec, String vuel, String ticket) {
        if (Session.STATE_IMPRESORA && Session.NAME_IMPRESORA != null && Session.CORTA_PAPEL != null) {
//            PrinterJob pj = PrinterJob.getPrinterJob();
//            
//            Book book = new Book();
//            book.append(new BillPrintable(ventaTB.getSubTotal(), ventaTB.getDescuento(),
//                    ventaTB.getGravada(), ventaTB.getIgv(), ventaTB.getTotal(), Double.parseDouble(efec), Double.parseDouble(vuel), ticket, tvList),
//                    getPageFormat(pj));
//            pj.setPageable(book);
//            try {
//                pj.print();
//            } catch (PrinterException ex) {
//            }

            Date date = new Date();
            SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat hora = new SimpleDateFormat("hh:mm:ss aa");

            try {
                String ruc = "RUC " + Session.RUC;
                String telcel = "TEL: " + Session.TELEFONO + " CEL:" + Session.CELULAR;
                String documento = "";
                if (ticket.substring(0, 1).equalsIgnoreCase("b")) {
                    documento = "BOLETA DE VENTA ELECTRONICA";
                } else if (ticket.substring(0, 1).equalsIgnoreCase("f")) {
                    documento = "FACTURA DE VENTA ELECTRONICA";
                } else if (ticket.substring(0, 1).equalsIgnoreCase("t")) {
                    documento = "TICKET DE VENTA";
                }
                String efectivo = "EFECTIVO SOLES  " + Session.MONEDA + " " + efec;
                String vuelto = "Cambio  " + Session.MONEDA + " " + vuel;
                String total = Session.MONEDA + " " + Tools.roundingValue(ventaTB.getTotal(), 2);
                PrinterMatrix p = new PrinterMatrix();

                int filas = tvList.getItems().size();

                int count = 13;

                p.setOutSize(getSizePaper(filas, count), 40);

                p.printTextWrap(1, 0, (int) (40 - Session.NOMBREEMPRESA.length()) / 2, 40, Session.NOMBREEMPRESA);
                p.printTextWrap(2, 0, (int) (40 - ruc.length()) / 2, 40, ruc);
                p.printTextWrap(3, 1, 0, 40, Session.DIRECCION);
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
                    p.printTextWrap(count, 1, 0, 40, tvList.getItems().get(i).getNombreMarca());
                    count += 2;
                    p.printTextWrap(count, 0, 0, 40, tvList.getItems().get(i).getCantidad() + " x " + Tools.roundingValue(tvList.getItems().get(i).getPrecioVenta(), 2));
                    p.printTextWrap(count, 0, 40 - Tools.roundingValue(tvList.getItems().get(i).getImporte().get(), 2).length(), 40, Tools.roundingValue(tvList.getItems().get(i).getImporte().get(), 2));
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
                File file = new File("c:\\temp\\impresion.txt");
                FileInputStream inputStream = null;
                try {
                    try {
                        inputStream = new FileInputStream(file);
                    } catch (FileNotFoundException ex) {

                    }
                    if (inputStream == null) {
                        return;
                    }
                    DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                    PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                    PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
                    PrintService service = findPrintService(Session.NAME_IMPRESORA, printService);
                    DocPrintJob job = service.createPrintJob();

                    byte[] bytes = readFileToByteArray(file);
                    byte[] cutP = new byte[]{0x1d, 'V', 1};
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    outputStream.write(bytes);
                    outputStream.write(cutP);
                    byte c[] = outputStream.toByteArray();

                    Doc doc = new SimpleDoc(c, flavor, null);

                    job.print(doc, null);
                } catch (IOException | PrintException e) {
                    // TODO Auto-generated catch block

                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException ex) {

                        }
                    }
                }
            } catch (Exception e) {
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

    private void removeArticulo() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Venta", "¿Esta seguro de quitar el artículo?", true);
            if (confirmation == 1) {
                ObservableList<ArticuloTB> articuloSelect, allArticulos;
                allArticulos = tvList.getItems();
                articuloSelect = tvList.getSelectionModel().getSelectedItems();
                articuloSelect.forEach(allArticulos::remove);
                calculateTotales();
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Venta", "Seleccione un artículo para quitarlo", false);
        }
    }

    @FXML
    private void onKeyPressedRegister(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowVentaProceso();
        }
    }

    @FXML
    private void onActionDescuento(ActionEvent event) throws IOException {
        openWindowDescuento();
    }

    @FXML
    private void onActionRegister(ActionEvent event) throws IOException {
        openWindowVentaProceso();
    }

    @FXML
    private void onKeyPressedArticulo(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowArticulos();
        }
    }

    @FXML
    private void onActionArticulo(ActionEvent event) throws IOException {
        openWindowArticulos();
    }

    @FXML
    private void onKeyPressedRemover(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            removeArticulo();
        }
    }

    @FXML
    private void onActionRemover(ActionEvent event) {
        removeArticulo();
    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!tvList.getItems().isEmpty()) {
                short value = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Venta", "¿Está seguro de borrar la venta?", true);
                if (value == 1) {
                    resetVenta();
                }
            }

        }
    }

    @FXML
    private void onActionCancel(ActionEvent event) {
        if (!tvList.getItems().isEmpty()) {
            short value = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Venta", "¿Está seguro de borrar la venta?", true);
            if (value == 1) {
                resetVenta();
            }
        }
    }

    @FXML
    private void onActionCliente(ActionEvent event) throws IOException {
        openWindowCliente();
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {

    }

    @FXML
    private void onKeyReleasedList(KeyEvent event) {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            if (event.getCode() == KeyCode.PLUS || event.getCode() == KeyCode.ADD) {
                ObservableList<ArticuloTB> articuloTBs;
                articuloTBs = tvList.getSelectionModel().getSelectedItems();
                int index = tvList.getSelectionModel().getSelectedIndex();
                articuloTBs.forEach(e -> {
                    ArticuloTB articuloTB = new ArticuloTB();
                    articuloTB.setIdArticulo(e.getIdArticulo());
                    articuloTB.setClave(e.getClave());
                    articuloTB.setNombreMarca(e.getNombreMarca());
                    articuloTB.setCantidad(e.getCantidad() + 1);
                    articuloTB.setPrecioVenta(e.getPrecioVenta());
                    articuloTB.setDescuento(e.getDescuento().get());
                    articuloTB.setSubTotal(articuloTB.getCantidad() * e.getPrecioVenta());
                    articuloTB.setImporte(
                            articuloTB.getSubTotal().get()
                            - articuloTB.getDescuento().get()
                    );
                    articuloTB.setInventario(e.isInventario());
                    articuloTB.setUnidadVenta(e.getUnidadVenta());
                    tvList.getItems().set(index, articuloTB);
                    tvList.getSelectionModel().select(index);
                    calculateTotales();
                });

            } else if (event.getCode() == KeyCode.MINUS || event.getCode() == KeyCode.SUBTRACT) {
                ObservableList<ArticuloTB> articuloTBs;
                articuloTBs = tvList.getSelectionModel().getSelectedItems();
                int index = tvList.getSelectionModel().getSelectedIndex();

                articuloTBs.forEach(e -> {
                    ArticuloTB articuloTB = new ArticuloTB();
                    articuloTB.setIdArticulo(e.getIdArticulo());
                    articuloTB.setClave(e.getClave());
                    articuloTB.setNombreMarca(e.getNombreMarca());
                    articuloTB.setCantidad(e.getCantidad() - 1);
                    articuloTB.setPrecioVenta(e.getPrecioVenta());
                    if (articuloTB.getCantidad() < 1) {
                        return;
                    }
                    articuloTB.setDescuento(e.getDescuento().get());
                    articuloTB.setSubTotal(articuloTB.getCantidad() * e.getPrecioVenta());
                    articuloTB.setImporte(
                            articuloTB.getSubTotal().get()
                            - articuloTB.getDescuento().get()
                    );
                    articuloTB.setInventario(e.isInventario());
                    articuloTB.setUnidadVenta(e.getUnidadVenta());
                    tvList.getItems().set(index, articuloTB);
                    tvList.getSelectionModel().select(index);
                    calculateTotales();
                });
            }
        }
    }

    public void calculateTotales() {
        double subTotalInterno, descuentoInterno;

        tvList.getItems().forEach(e -> subTotal += e.getSubTotal().get());
        lblSubTotal.setText(Tools.roundingValue(subTotal, 2));
        subTotalInterno = subTotal;
        subTotal = 0;

        tvList.getItems().forEach(e -> descuento += e.getDescuento().get());
        lblDescuento.setText(Tools.roundingValue(descuento, 2));
        descuentoInterno = descuento;
        descuento = 0;

        double total = subTotalInterno - descuentoInterno;
        lblTotal.setText(Tools.roundingValue(total, 2));
        lblTotalPagar.setText(Tools.roundingValue(total, 2));

        double gravada = Tools.calculateValueNeto(Session.IMPUESTO, total);
        lblGravada.setText(Tools.roundingValue(gravada, 2));

        double impuesto = Tools.calculateTax(Session.IMPUESTO, gravada);
        lblIgv.setText(Tools.roundingValue(impuesto, 2));

    }

    // Metodo para obtener Tipo Comprobante
    public String obtenerTipoComprobante() {
        return this.cbComprobante.getSelectionModel().getSelectedItem().getNombre();
    }

    @FXML
    private void onKeyPressedPrecio(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowGranel("Cambiar precio al Artículo", false);
        }
    }

    @FXML
    private void onActionPrecio(ActionEvent event) throws IOException {
        openWindowGranel("Cambiar precio al Artículo", false);
    }

    @FXML
    private void onKeyPressedPrecioSumar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowGranel("Sumar precio al Artículo", true);
        }
    }

    @FXML
    private void onActionPrecioSumar(ActionEvent event) throws IOException {
        openWindowGranel("Sumar precio al Artículo", true);
    }

    @FXML
    private void onKeyReleasedSearch(KeyEvent event) {
        if (event.getCode() != KeyCode.ESCAPE
                && event.getCode() != KeyCode.F1
                && event.getCode() != KeyCode.F2
                && event.getCode() != KeyCode.F3
                && event.getCode() != KeyCode.F4
                && event.getCode() != KeyCode.F5
                && event.getCode() != KeyCode.F6
                && event.getCode() != KeyCode.F7
                && event.getCode() != KeyCode.F8
                && event.getCode() != KeyCode.F9
                && event.getCode() != KeyCode.F10
                && event.getCode() != KeyCode.F11
                && event.getCode() != KeyCode.F12
                && event.getCode() != KeyCode.ALT
                && event.getCode() != KeyCode.CONTROL
                && event.getCode() != KeyCode.UP
                && event.getCode() != KeyCode.DOWN
                && event.getCode() != KeyCode.RIGHT
                && event.getCode() != KeyCode.LEFT
                && event.getCode() != KeyCode.TAB
                && event.getCode() != KeyCode.CAPS
                && event.getCode() != KeyCode.SHIFT
                && event.getCode() != KeyCode.HOME
                && event.getCode() != KeyCode.WINDOWS
                && event.getCode() != KeyCode.ALT_GRAPH
                && event.getCode() != KeyCode.CONTEXT_MENU
                && event.getCode() != KeyCode.END
                && event.getCode() != KeyCode.INSERT
                && event.getCode() != KeyCode.PAGE_UP
                && event.getCode() != KeyCode.PAGE_DOWN
                && event.getCode() != KeyCode.NUM_LOCK
                && event.getCode() != KeyCode.PRINTSCREEN
                && event.getCode() != KeyCode.SCROLL_LOCK
                && event.getCode() != KeyCode.PAUSE) {
            ArticuloTB a = ArticuloADO.Get_Articulo_By_Search(txtSearch.getText().trim());
            if (a != null) {
                ArticuloTB articuloTB = new ArticuloTB();
                articuloTB.setIdArticulo(a.getIdArticulo());
                articuloTB.setClave(a.getClave());
                articuloTB.setNombreMarca(a.getNombreMarca());
                articuloTB.setCantidad(1);
                articuloTB.setPrecioVenta(a.getPrecioVenta());
                articuloTB.setDescuento(0);
                articuloTB.setSubTotal(1 * a.getPrecioVenta());
                articuloTB.setImporte(
                        articuloTB.getSubTotal().get()
                        - articuloTB.getDescuento().get()
                );
                articuloTB.setInventario(a.isInventario());
                articuloTB.setUnidadVenta(a.getUnidadVenta());
                getAddArticulo(articuloTB);
                txtSearch.clear();
                txtSearch.requestFocus();
            } else {                
                
            }
        }

    }

    @FXML
    private void onKeyPressedImprimir(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowImpresora();
        }
    }

    @FXML
    private void onActionImprimir(ActionEvent event) throws IOException {
        openWindowImpresora();
    }

    public void setClienteVenta(String id, String datos) {
        idCliente = !id.equalsIgnoreCase("") ? id : Session.IDCLIENTE;
        datodCliente = datos;
        txtCliente.setText(datodCliente.equalsIgnoreCase("")
                ? Session.DATOSCLIENTE
                : datodCliente);
    }

//    public PageFormat getPageFormat(PrinterJob pj) {
//        PageFormat pf = pj.defaultPage();
//        Paper paper = pf.getPaper();
//        paper.setImageableArea(0, 0, pf.getWidth(), pf.getImageableHeight());   //define boarder size    after that print area width is about 180 points
//        pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
//        pf.setPaper(paper);
//        return pf;
//    }

    @FXML
    private void onActionComprobante(ActionEvent event) {

        String[] array;

        switch (this.cbComprobante.getSelectionModel().getSelectedIndex()) {
            case 0:
                array = ComprobanteADO.GetSerieNumeracionEspecifico(this.cbComprobante.getSelectionModel().getSelectedItem().getNombre()).split("-");
                lblSerie.setText(array[0]);
                lblNumeracion.setText(array[1]);
                //System.out.println(this.cbComprobante.getSelectionModel().getSelectedItem().getNombre().get());
                break;
            case 1:
                array = ComprobanteADO.GetSerieNumeracionEspecifico(this.cbComprobante.getSelectionModel().getSelectedItem().getNombre()).split("-");
                lblSerie.setText(array[0]);
                lblNumeracion.setText(array[1]);
                break;
            case 2:
                array = ComprobanteADO.GetSerieNumeracionEspecifico(this.cbComprobante.getSelectionModel().getSelectedItem().getNombre()).split("-");
                lblSerie.setText(array[0]);
                lblNumeracion.setText(array[1]);
                break;
            default:
                break;
        }
    }

    public TextField getTxtSearch() {
        return txtSearch;
    }

    public TableView<ArticuloTB> getTvList() {
        return tvList;
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
