
package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.PagoProveedoresADO;
import model.PagoProveedoresTB;


public class FxHistorialPagosController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblFechaInicial;
    @FXML
    private Label lblFechaFinal;
    @FXML
    private Label lblCuotaActual;
    @FXML
    private Label lblPlazo;
    @FXML
    private Label lblPagado;
    @FXML
    private Label lblPendiente;
    @FXML
    private Label lblTotal;
    @FXML
    private TableView<PagoProveedoresTB> tvList;
    @FXML
    private TableColumn<PagoProveedoresTB, String> tcNumeroRegistro;
    @FXML
    private TableColumn<PagoProveedoresTB, String> tcFecha;
    @FXML
    private TableColumn<PagoProveedoresTB, String> tcCuota;
    @FXML
    private TableColumn<PagoProveedoresTB, String> tcObservacion;
    @FXML
    private TableColumn<PagoProveedoresTB, String> tcMonto;
    @FXML
    private Button btnAmortizar;
    
    private FxCompraDetalleController compraDetalleController;
    
    private String idCompra;
    
    private LocalDate fecha_Inicial;
    
    private int dias;
    
    private double pagado;
    
    private double total;
    
    private String simboloMoneda;

    private List<PagoProveedoresTB> listPagoProveederes;
    
    private PagoProveedoresTB pagoProveedoresTB;   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        
        tcNumeroRegistro.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getIdPagoProveedores()));
        tcFecha.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaActual().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE)));
        tcCuota.setCellValueFactory(cellData -> Bindings.concat(String.valueOf(cellData.getValue().getCuotaActual())));
        tcObservacion.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getObservacion()));
        tcMonto.setCellValueFactory(cellData -> Bindings.concat(simboloMoneda+" "+Tools.roundingValue(cellData.getValue().getMontoActual(), 2)));
        
        pagoProveedoresTB = new PagoProveedoresTB();
    }

    public void initListHistorialPagos() {
        
        pagado = 0;

        tvList.setItems(PagoProveedoresADO.ListHistorialPagoCompras(idCompra));

        listPagoProveederes = new ArrayList<>();

        for (int i = 0; i < tvList.getItems().size(); i++) {

            listPagoProveederes.add(tvList.getItems().get(i));

            if (i == 0) {
                               
                pagoProveedoresTB.setMontoTotal(total);
                pagoProveedoresTB.setPlazos(tvList.getItems().get(i).getPlazos());
                pagoProveedoresTB.setDias(tvList.getItems().get(i).getDias());
                pagoProveedoresTB.setFechaActual(tvList.getItems().get(i).getFechaActual());
                // Falta Observacion
                pagoProveedoresTB.setEstado(tvList.getItems().get(i).getEstado());
                pagoProveedoresTB.setIdProveedor(tvList.getItems().get(i).getIdProveedor());
                pagoProveedoresTB.setIdCompra(idCompra);
                pagoProveedoresTB.setIdEmpleado(tvList.getItems().get(i).getIdEmpleado());

                fecha_Inicial = LocalDate.parse( pagoProveedoresTB.getFechaActual().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
                dias = pagoProveedoresTB.getDias();
                this.lblFechaInicial.setText(""+fecha_Inicial);
                this.lblFechaFinal.setText(""+fecha_Inicial.plusDays(dias));
                this.lblPlazo.setText(pagoProveedoresTB.getPlazos());
                
                System.out.println("Entro a: #"+i);
            }         
        
            if (tvList.getItems().size() == i + 1) {
                
                pagoProveedoresTB.setCuotaActual(tvList.getItems().get(i).getCuotaActual());            
                this.lblCuotaActual.setText(pagoProveedoresTB.getCuotaActual() + "");
                
                System.out.println("Entro a: #"+i);
            }
            
            pagado =  pagado + tvList.getItems().get(i).getMontoActual();

        }

        this.lblPendiente.setText(simboloMoneda+" "+Tools.roundingValue(total - pagado, 2));
        this.lblPagado.setText(simboloMoneda+" "+Tools.roundingValue(pagado, 2));
        this.lblTotal.setText(simboloMoneda+" "+Tools.roundingValue(total, 2));       

    }
    
    public void initBotonAbonar(){ 
        this.btnAmortizar.requestFocus();
    }

    private void openWindowAmortizarPagos() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_AMORTIZAR_PAGOS);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());

        FxAmortizarPagosController controller = fXMLLoader.getController();
        controller.setInitAmortizarPagosController(this, pagoProveedoresTB, pagado, simboloMoneda, compraDetalleController);

        Stage stage = FxWindow.StageLoaderModal(parent, "Amortizar deuda", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            initListHistorialPagos();
        });
        stage.show();

        controller.setInitValues();

    }

    @FXML
    private void onActionAmortizar(ActionEvent event) throws IOException {
        openWindowAmortizarPagos();
    }

    @FXML
    private void onKeyPressedAmortizar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowAmortizarPagos();
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Tools.Dispose(window);
    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    public void setInitHistorialPagosController(FxCompraDetalleController compraDetalleController, String idCompra, double total, String simboloMoneda) {
        this.compraDetalleController = compraDetalleController;
        this.idCompra = idCompra;
        this.total = total;
        this.simboloMoneda = simboloMoneda;
    }

    @FXML
    private void onKeyPressedEliminar(KeyEvent event) {
        
        if (event.getCode() == KeyCode.ENTER) {
            eliminarPagoCuota();
        }
    }

    @FXML
    private void onActionEliminar(ActionEvent event) {
        eliminarPagoCuota();
    }
    
    private void eliminarPagoCuota(){
        if(tvList.getSelectionModel().getSelectedIndex() >= 0){
            short value = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Historial de pagos", "Está seguro de eliminar el registro de pago", true);
            if(value==1){
                
                String result = PagoProveedoresADO.EliminarPagoCuota(String.valueOf(tvList.getSelectionModel().getSelectedItem().getIdPagoProveedores()));
                if(result.equalsIgnoreCase("removed")){
                    initListHistorialPagos();
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Historial de pagos", "Se eliminó el registro de pago correctamente", true);
                }
                else{
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Historial de pagos", result, true);
                }
                
            }
        }
        else{
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Historial de pagos", "Seleccione un item para eliminarlo", false);
        }
    }

}
