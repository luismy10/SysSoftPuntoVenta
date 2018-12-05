package controller;

import java.awt.HeadlessException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.ArticuloTB;
import model.CompraADO;
import model.CompraTB;
import model.DBUtil;
import model.ProveedorTB;
import model.RepresentanteTB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FxCompraDetalleController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Text lblFechaCompra;
    @FXML
    private Text lblDocumento;
    @FXML
    private Text lblProveedor;
    @FXML
    private Text lblDomicilio;
    @FXML
    private Text lblContacto;
    @FXML
    private Text lblRepresentante;
    @FXML
    private Text lblComprobante;
    @FXML
    private Text lblNumeracion;
    @FXML
    private Text lblTotal;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, Integer> tcId;
    @FXML
    private TableColumn<ArticuloTB, String> tcDescripcion;
    @FXML
    private TableColumn<ArticuloTB, Double> tcCantidad;
    @FXML
    private TableColumn<ArticuloTB, String> tcMedidad;
    @FXML
    private TableColumn<ArticuloTB, String> tcPrecioCompra;
    @FXML
    private TableColumn<ArticuloTB, String> tcDescuento;
    @FXML
    private TableColumn<ArticuloTB, String> tcImporte;
    @FXML
    private Label lblLoad;

    private FxComprasRealizadasController comprascontroller;

    private String idCompra;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        tcId.setCellValueFactory(callData -> callData.getValue().getId().asObject());

        tcDescripcion.setCellValueFactory(callData -> Bindings.concat(
                callData.getValue().getClave().get() + "\n" + callData.getValue().getNombreMarca().get()
        ));

        tcMedidad.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getUnidadVenta() == 1 ? "Por Unidad/Pza" : "A Granel"
        ));

        tcCantidad.setCellValueFactory(callData -> new SimpleDoubleProperty(callData.getValue().getCantidad()).asObject());

        tcPrecioCompra.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getPrecioCompra(), 2)));

        tcDescuento.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getDescuento().get(), 2)));

        tcImporte.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getImporte().get(), 2)));

    }

    private void fillArticlesTable(String value) {
        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<List<ArticuloTB>> task = new Task<List<ArticuloTB>>() {
            @Override
            public ObservableList<ArticuloTB> call() {
                return CompraADO.ListDetalleCompra(value);
            }
        };
        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems((ObservableList<ArticuloTB>) task.getValue());
            lblLoad.setVisible(false);
        });
        task.setOnFailed((WorkerStateEvent event) -> {
            lblLoad.setVisible(false);
        });

        task.setOnScheduled((WorkerStateEvent event) -> {
            lblLoad.setVisible(true);
        });
        exec.execute(task);
        if (!exec.isShutdown()) {
            exec.shutdown();
        }
    }

    public void setLoadDetalle(String idCompra) {
        this.idCompra = idCompra;
        ArrayList<Object> objects = CompraADO.ListCompletaDetalleCompra(idCompra);
        CompraTB compraTB = (CompraTB) objects.get(0);
        ProveedorTB proveedorTB = (ProveedorTB) objects.get(1);
        RepresentanteTB representanteTB = (RepresentanteTB) objects.get(2);

        if (compraTB != null) {            
            lblFechaCompra.setText(compraTB.getFechaCompra().get().format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy")));
            lblComprobante.setText(compraTB.getComprobanteName());
            lblNumeracion.setText(compraTB.getNumeracion());
            lblTotal.setText("S/. " + Tools.roundingValue(compraTB.getTotal().get(), 2));

        }
        
        if (proveedorTB != null) {
            lblDocumento.setText(proveedorTB.getNumeroDocumento().get());
            lblProveedor.setText(proveedorTB.getRazonSocial().get());
            lblDomicilio.setText(proveedorTB.getDireccion().equalsIgnoreCase("")
                    ? "No tiene un domicilio registrado"
                    : proveedorTB.getDireccion());
            lblContacto.setText("Tel: " + proveedorTB.getTelefono() + " Cel: " + proveedorTB.getCelular());
        }

       
        if (representanteTB != null) {
            lblRepresentante.setText(
                    representanteTB.getApellidos() + " " + representanteTB.getNombres() + " - "
                    + representanteTB.getTelefono() + " " + representanteTB.getCelular()
            );
        }else{
            lblRepresentante.setText("No tiene un representante registrado");
        }

        fillArticlesTable(idCompra);
    }

    @FXML
    private void onActionReporte(ActionEvent event) throws UnsupportedLookAndFeelException {
        try {
            DBUtil.dbConnect();
            InputStream dir = getClass().getResourceAsStream("/report/DetalleCompra.jasper");
            InputStream imgInputStream
                    = getClass().getResourceAsStream("/view/logo.png");

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(dir);
            Map map = new HashMap();
            map.put("IDCOMPRA", idCompra);
            map.put("EMPRESA", Session.EMPRESA);
            map.put("LOGO", imgInputStream);
            map.put("EMAIL", "EMAIL" + Session.EMAIL);
            map.put("TELEFONOCELULAR", "TEL:" + Session.TELEFONO + " CEL:" + Session.CELULAR);
            map.put("DIRECCION", Session.DIRECCION);

            map.put("FECHACOMPRA", lblFechaCompra.getText());
            map.put("PROVEEDOR", lblProveedor.getText());
            map.put("PRODIRECCION", lblDomicilio.getText());
            map.put("PROTELEFONOCELULAR", lblContacto.getText());
            map.put("PROEMAIL", "");

//            map.put("TOTAL", lblTotal.getText());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, DBUtil.getConnection());

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setIconImage(new ImageIcon(getClass().getResource(Tools.FX_LOGO)).getImage());
            jasperViewer.setTitle("Detalle de compra");
            jasperViewer.setSize(840, 650);
            jasperViewer.setLocationRelativeTo(null);
            jasperViewer.setVisible(true);

        } catch (HeadlessException | JRException ex) {
            System.out.println("Error al generar el reporte : " + ex);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(FxCompraDetalleController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {
                Logger.getLogger(FxCompraDetalleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void setInitComptrasController(FxComprasRealizadasController comprascontroller) {
        this.comprascontroller = comprascontroller;
    }

    @FXML
    private void onActionPrueba(ActionEvent event) {

    }

}
