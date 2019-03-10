/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.PagoProveedoresADO;
import model.PagoProveedoresTB;

/**
 * FXML Controller class
 *
 * @author Ruberfc
 */
public class FxHistorialPagosController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblPagado;
    @FXML
    private Label lblPendiente;
    @FXML
    private Label lblCuotaTotal;
    @FXML
    private Label lblCuotaActual;
    @FXML
    private Label lblFechaInicial;
    @FXML
    private Label lblFechaFinal;
    @FXML
    private TableView<PagoProveedoresTB> tvList;
    @FXML
    private TableColumn<PagoProveedoresTB, String> tcFecha;
    @FXML
    private TableColumn<PagoProveedoresTB, String> tcCuotas;
    @FXML
    private TableColumn<PagoProveedoresTB, String> tcValor;
    @FXML
    private TableColumn<PagoProveedoresTB, String> tcMonto;

    private FxCompraDetalleController compraDetalleController;

    private String idProveedor;

    private String idCompra;

    private double total;

    private List<PagoProveedoresTB> listPagoProveederes;
    
    private PagoProveedoresTB pagoProveedoresTB;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcFecha.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaActual().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE)));
        tcCuotas.setCellValueFactory(cellData -> Bindings.concat(String.valueOf(cellData.getValue().getCuotaActual())));
        tcValor.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getValorCuota()));
        tcMonto.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getMontoActual()));
        
        pagoProveedoresTB = new PagoProveedoresTB();
    }

    public void initListHistorialPagos() {

        tvList.setItems(PagoProveedoresADO.ListHistorialPagoCompras(idCompra));

        listPagoProveederes = new ArrayList<>();

        for (int i = 0; i < tvList.getItems().size(); i++) {

            listPagoProveederes.add(tvList.getItems().get(i));

            if (i == 0) {
                
                this.lblCuotaTotal.setText(tvList.getItems().get(i).getCuotaTotal() + "");
                this.lblFechaInicial.setText(tvList.getItems().get(i).getFechaInicial().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
                this.lblFechaFinal.setText(tvList.getItems().get(i).getFechaFinal().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
                
                pagoProveedoresTB.setCuotaTotal(tvList.getItems().get(i).getCuotaTotal());
                pagoProveedoresTB.setFechaInicial(tvList.getItems().get(i).getFechaInicial());
                pagoProveedoresTB.setFechaFinal(tvList.getItems().get(i).getFechaFinal());

            }

            if (tvList.getItems().size() == i + 1) {
                
                this.lblPagado.setText(tvList.getItems().get(i).getMontoActual() + "");
                this.lblCuotaActual.setText(tvList.getItems().get(i).getCuotaActual() + "");   
                
                pagoProveedoresTB.setMontoActual(tvList.getItems().get(i).getMontoActual());
                pagoProveedoresTB.setCuotaActual(tvList.getItems().get(i).getCuotaActual());
                pagoProveedoresTB.setValorCuota(tvList.getItems().get(i).getValorCuota());
                pagoProveedoresTB.setPlazos(tvList.getItems().get(i).getPlazos());
            }

        }

        lblPendiente.setText(String.valueOf(total - Double.parseDouble(lblPagado.getText())));
        lblTotal.setText(String.valueOf(total));
        
        pagoProveedoresTB.setMontoTotal(total);


    }

    private void initObjetPagoProveedores() {

        pagoProveedoresTB.setIdProveedor(idProveedor);
        pagoProveedoresTB.setIdCompra(idCompra);
        
    }

    private void openWindowAmortizarPagos() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_AMORTIZAR_PAGOS);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());

        FxAmortizarPagosController controller = fXMLLoader.getController();
        controller.setInitAmortizarPagosController(this, pagoProveedoresTB);

        Stage stage = FxWindow.StageLoaderModal(parent, "Amortizar deuda", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        controller.setInitTexField();

    }

    @FXML
    private void onActionAmortizar(ActionEvent event) throws IOException {
        initObjetPagoProveedores();
        openWindowAmortizarPagos();
    }

    @FXML
    private void onKeyPressedAmortizar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            initObjetPagoProveedores();
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

    public void setInitHistorialPagosController(FxCompraDetalleController compraDetalleController, String idProveedor, String idCompra, double total) {
        this.compraDetalleController = compraDetalleController;
        this.idProveedor = idProveedor;
        this.idCompra = idCompra;
        this.total = total;
    }

}
