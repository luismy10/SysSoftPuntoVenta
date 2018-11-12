/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.HistorialADO;
import model.HistorialTB;
import model.DBUtil;

/**
 * FXML Controller class
 *
 * @author Ruberfc
 */
public class FxHistorialController implements Initializable {

    @FXML
    private DatePicker dtFechaInicial;
    @FXML
    private DatePicker dtFechaFinal;
    @FXML
    private Label lblLoad;
    @FXML
    private TextField txtBuscar;
    @FXML
    private TableView<HistorialTB> tvListaHistorial;
    @FXML
    private TableColumn<HistorialTB, String> tcId;
    @FXML
    private TableColumn<HistorialTB, String> tcOperacion;
    @FXML
    private TableColumn<HistorialTB, String> tcCodArticulo;
    @FXML
    private TableColumn<HistorialTB, String> tcFecha;
    @FXML
    private TableColumn<HistorialTB, String> tcEntrada;
    @FXML
    private TableColumn<HistorialTB, String> tcSalida;
    @FXML
    private TableColumn<HistorialTB, String> tcUsuario;
    
    private AnchorPane windowinit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tcId.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getIdHistorial().get()));
        tcOperacion.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getTipoOperacion()));
        tcCodArticulo.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getIdArticulo()));
        tcFecha.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaRegistro()));
        tcEntrada.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getEntrada()));
        tcSalida.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getSalida()));
        tcUsuario.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getUsuarioRegistro()));

        //changeViewArticuloSeleccionado();
    }

    public void fillHistorialTable(String value) {
        if (DBUtil.StateConnection()) {

            ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });

            Task<ObservableList<HistorialTB>> task = new Task<ObservableList<HistorialTB>>() {
                @Override
                public ObservableList<HistorialTB> call() {
                    return HistorialADO.ListArticulos(value);
                }
            };

            task.setOnSucceeded((WorkerStateEvent e) -> {
                this.tvListaHistorial.setItems((ObservableList<HistorialTB>) task.getValue());
                this.lblLoad.setVisible(false);
            });
            task.setOnFailed((WorkerStateEvent event) -> {
                this.lblLoad.setVisible(false);
            });

            task.setOnScheduled((WorkerStateEvent event) -> {
                this.lblLoad.setVisible(true);
                //this.tvListaHistorial.itemsProperty().bind(task.valueProperty());
            });
            exec.execute(task);

            if (!exec.isShutdown()) {
                exec.shutdown();
            }
        }

    }

    @FXML
    private void onActionRecargar(ActionEvent event) {
    }

    @FXML
    private void onActionReporte(ActionEvent event) {
    }

    void setContent(AnchorPane windowinit) {
        this.windowinit=windowinit;
    }


}
