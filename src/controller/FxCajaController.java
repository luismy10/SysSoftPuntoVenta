/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CajaADO;
import model.CajaTB;
import model.MonedaADO;
import model.MonedaTB;
import model.VentaTB;

/**
 * FXML Controller class
 *
 * @author Ruberfc
 */
public class FxCajaController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Button btnCorte;
    @FXML
    private Button btnImprimir;
    @FXML
    private Label lblCargo;
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblVentasTotales;
    @FXML
    private Label lblFondoCaja;
    @FXML
    private Label lblIngresos;
    @FXML
    private Label lblEgresos;
    @FXML
    private Label lblDevoluciones;
    @FXML
    private Label lblEntradas;
    @FXML
    private Label lblSalidas;
    @FXML
    private Label lblTotalDineroCaja;

    private AnchorPane content;

    private String simbolo_Moneda;
    
    private double totalDineroCaja;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.lblCargo.setText(Session.USER_NAME);
        this.lblFecha.setText(Tools.getDate().toString());
        this.listarMontos();
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

    public void listarMontos() {

        List<MonedaTB> list_Moneda = MonedaADO.GetMonedasCombBox();

        for (int i = 0; i < list_Moneda.size(); i++) {
            if (list_Moneda.get(i).getPredeterminado() == true) {
                simbolo_Moneda = list_Moneda.get(i).getSimbolo();
                break;
            }
        }

        CajaTB cajaTB = CajaADO.consultarMontosCaja(Session.USER_ID, "activo".toUpperCase());
        VentaTB ventaTB = CajaADO.consultarMontosVenta(Session.USER_ID, "activo".toUpperCase());
        
        this.lblVentasTotales.setText(String.valueOf(simbolo_Moneda + " " + Tools.roundingValue(ventaTB.getTotal(), 2)));

        this.lblFondoCaja.setText(String.valueOf(simbolo_Moneda + " " + Tools.roundingValue(cajaTB.getMontoInicial(), 2)));
        this.lblIngresos.setText(String.valueOf(simbolo_Moneda + " " + Tools.roundingValue(cajaTB.getIngresos(), 2)));
        this.lblEgresos.setText(String.valueOf(simbolo_Moneda + " " + Tools.roundingValue(cajaTB.getEgresos(), 2)));
        this.lblDevoluciones.setText(String.valueOf(simbolo_Moneda + " " + Tools.roundingValue(cajaTB.getDevoluciones(), 2)));
        this.lblEntradas.setText(String.valueOf(simbolo_Moneda + " " + Tools.roundingValue(cajaTB.getEntradas(), 2)));
        this.lblSalidas.setText(String.valueOf(simbolo_Moneda + " " + Tools.roundingValue(cajaTB.getSalidas(), 2)));
        
        totalDineroCaja = (cajaTB.getMontoInicial()+cajaTB.getIngresos()+cajaTB.getEntradas())-(cajaTB.getEgresos()+cajaTB.getDevoluciones()+cajaTB.getSalidas());
        
        this.lblTotalDineroCaja.setText(String.valueOf(simbolo_Moneda + " " + Tools.roundingValue(totalDineroCaja, 2)));
    }

    @FXML
    private void onActionCorte(ActionEvent event) throws IOException {

        short value = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Caja", "Esta seguro(a) de hacer el corte de caja. ", "Aceptar", "Cancelar");
        if (value == 0) {

            CajaTB cajaTB = new CajaTB();

            cajaTB.setMontoFinal(totalDineroCaja);
            cajaTB.setFechaCierre(Timestamp.valueOf(LocalDateTime.now()));
            cajaTB.setEstado("ACTIVO".toUpperCase());
            cajaTB.setIdEmpleado(Session.USER_ID);

            String result = CajaADO.crudCorteCaja(cajaTB);

            if (result.equalsIgnoreCase("update")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Caja", "Se realizó correctamente el corte de caja.", false);
                Tools.Dispose(content);
                URL urllogin = getClass().getResource(Tools.FX_FILE_LOGIN);
                FXMLLoader fXMLLoaderLogin = FxWindow.LoaderWindow(urllogin);
                Parent parent = fXMLLoaderLogin.load(urllogin.openStream());
                Scene scene = new Scene(parent);
                Stage primaryStage = new Stage();
                primaryStage.getIcons().add(new Image(Tools.FX_LOGO));
                primaryStage.setScene(scene);
                primaryStage.initStyle(StageStyle.DECORATED);
                primaryStage.setTitle("Sys Soft");
                primaryStage.centerOnScreen();
                primaryStage.setMaximized(true);
                primaryStage.show();
                primaryStage.requestFocus();
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Caja", result, false);
            }
        }

    }

    @FXML
    private void onActionImprimir(ActionEvent event) {
    }

}
