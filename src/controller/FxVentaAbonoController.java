
package controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.CuentasClienteADO;
import model.CuentasClienteTB;


public class FxVentaAbonoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblPagado;
    @FXML
    private Label lblPendiente;
    @FXML
    private Label lblMontoInicial;
    @FXML
    private Label lblPlazos;
    @FXML
    private Label lblFechaVencimiento;
    @FXML
    private TableView<?> tvList;
    @FXML
    private TableColumn<?, ?> tcFecha;
    @FXML
    private TableColumn<?, ?> tcCuotas;
    @FXML
    private TableColumn<?, ?> tcMonto;
    @FXML
    private TableColumn<?, ?> tcReferencia;
    
    private FxVentaDetalleController ventaDetalleController;
    
    private String simboloMoneda;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        simboloMoneda = "M";
    }    
    
    public void loadInitData(String idVenta,String simboloMoneda){
        this.simboloMoneda = simboloMoneda;
        CuentasClienteTB cuentasClienteTB = CuentasClienteADO.Get_CuentasCliente_ById(idVenta);
        if(cuentasClienteTB != null){
            lblPlazos.setText(cuentasClienteTB.getPlazosName());
            lblFechaVencimiento.setText(cuentasClienteTB.getFechaVencimiento().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
            lblMontoInicial.setText(simboloMoneda+" "+Tools.roundingValue(cuentasClienteTB.getMontoInicial(), 2));
            lblPagado.setText(simboloMoneda+" 00.00");
            lblPendiente.setText(simboloMoneda+" 00.00");
            lblTotal.setText(simboloMoneda+" 00.00");
        }
    }

    @FXML
    private void onKeyPressedAmortizar(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            
        }
    }

    @FXML
    private void onActionAmortizar(ActionEvent event) {
        
    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
         Tools.Dispose(window);
    }

    public void setInitVentaAbonoController(FxVentaDetalleController ventaDetalleController) {
        this.ventaDetalleController=ventaDetalleController;
    }
    
}
