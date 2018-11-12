/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import model.VentaADO;
import model.VentaTB;

/**
 * FXML Controller class
 *
 * @author Ruberfc
 */
public class FxVentaProcesoController implements Initializable {

    private FxVentaController ventaController;
    @FXML
    private AnchorPane window;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void onActionAceptar(ActionEvent event) {

        VentaTB ventaTB = new VentaTB();
        ventaTB.setCliente("234234");
        ventaTB.setVendedor("123123123");
        ventaTB.setComprobante(1);
        ventaTB.setFechaVenta(Timestamp.valueOf(Tools.getDate() + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
        ventaTB.setSubTotal(100);
        ventaTB.setGravada(100);
        ventaTB.setDescuento(100);
        ventaTB.setIgv(100);
        ventaTB.setTotal(100);
        short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Venta", "¿Esta seguro de continuar?", true);
        if (confirmation == 1) {
            String result = VentaADO.CrudVenta(ventaTB);
            switch (result) {
                case "register":
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Venta", "Se realiazo la venta con éxito", false);
                    ventaController.resetVenta();
                    break;
                default:
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Venta", result, false);
                    break;
            }
        }

    }

    void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

}
