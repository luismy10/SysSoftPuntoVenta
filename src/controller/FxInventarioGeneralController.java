package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.ArticuloADO;
import model.ArticuloTB;

public class FxInventarioGeneralController implements Initializable {

    @FXML
    private Label lblLoad;
    @FXML
    private GridPane gpList;
    @FXML
    private Label lblValoTotal;

    private AnchorPane content;

    private ArrayList<ArticuloTB> listInventario = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    

    public void fillInventarioTable() {
        ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<ArrayList<ArticuloTB>> task = new Task<ArrayList<ArticuloTB>>() {
            @Override
            public ArrayList<ArticuloTB> call() {
                return ArticuloADO.ListInventario();
            }
        };

        task.setOnSucceeded((WorkerStateEvent e) -> {
            listInventario = task.getValue();
            double total = 0;
            if (listInventario != null) {
                for (int i = 0; i < listInventario.size(); i++) {
                    gpList.add(addElementGridPane("l1" + (i + 1), listInventario.get(i).getId().get() + "", Pos.CENTER), 0, (i + 1));
                    gpList.add(addElementGridPane("l2" + (i + 1), listInventario.get(i).getClave() + "\n" + listInventario.get(i).getNombreMarca(), Pos.CENTER_LEFT), 1, (i + 1));
                    gpList.add(addElementGridPane("l3" + (i + 1), Tools.roundingValue(listInventario.get(i).getCantidad(), 2), Pos.CENTER_LEFT), 2, (i + 1));
                    gpList.add(addElementGridPane("l4" + (i + 1), listInventario.get(i).getUnidadCompraName(), Pos.CENTER_LEFT), 3, (i + 1));
                    gpList.add(addElementGridPane("l5" + (i + 1), listInventario.get(i).getEstadoName().get(), Pos.CENTER_RIGHT), 4, (i + 1));
                    gpList.add(addElementGridPane("l6" + (i + 1), Session.MONEDA + Tools.roundingValue(listInventario.get(i).getPrecioCompra(), 2), Pos.CENTER_RIGHT), 5, (i + 1));
                    gpList.add(addElementGridPane("l7" + (i + 1), Session.MONEDA + Tools.roundingValue(listInventario.get(i).getTotalImporte(), 2), Pos.CENTER_RIGHT), 6, (i + 1));
                    total += listInventario.get(i).getTotalImporte();
                }
                lblValoTotal.setText(Session.MONEDA + Tools.roundingValue(total, 2));
            }
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

    private Label addElementGridPane(String id, String nombre, Pos pos) {
        Label label = new Label(nombre);
        label.setId(id);
        label.setStyle("-fx-text-fill:#020203;-fx-background-color: #dddddd;-fx-padding: 0.4166666666666667em 0.8333333333333334em 0.4166666666666667em 0.8333333333333334em;");
        label.getStyleClass().add("labelRoboto14");
        label.setAlignment(pos);
        label.setWrapText(true);
        label.setPrefWidth(Control.USE_COMPUTED_SIZE);
        label.setPrefHeight(Control.USE_COMPUTED_SIZE);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        return label;
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }
}
