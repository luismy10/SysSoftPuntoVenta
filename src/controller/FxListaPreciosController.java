package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.ArticuloADO;
import model.ArticuloTB;

public class FxListaPreciosController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblNombreArticulo;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, Integer> tcNumero;
    @FXML
    private TableColumn<ArticuloTB, String> tcNombre;
    @FXML
    private TableColumn<ArticuloTB, String> tcValor;

    private FxVentaController ventaController;
    
     private ArticuloTB articuloTB;
     
     private int index;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        tcNumero.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcNombre.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNombreGenerico()));
        tcValor.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getCostoCompra(), 2)));
    }

    public void loadDataView(ArticuloTB item,int idx) {
        articuloTB = item;
        index = idx;
        ArticuloTB artPrices = ArticuloADO.GetItemPriceList(articuloTB.getIdArticulo());
        if (artPrices != null) {
            ObservableList<ArticuloTB> arrayList = FXCollections.observableArrayList();
//            if (artPrices.getPrecioVenta() >= 0) {
//                ArticuloTB a1 = new ArticuloTB();
//                a1.setId(1);
//                a1.setNombreGenerico("Precio 1");
//                a1.setPrecioCompra(artPrices.getPrecioVenta());
//                arrayList.add(a1);
//            }
//            if (artPrices.getPrecioVenta2() >= 0) {
//                ArticuloTB a1 = new ArticuloTB();
//                a1.setId(2);
//                a1.setNombreGenerico("Precio 2");
//                a1.setPrecioCompra(artPrices.getPrecioVenta2());
//                arrayList.add(a1);
//            }
//            if (artPrices.getPrecioVenta3() >= 0) {
//                ArticuloTB a1 = new ArticuloTB();
//                a1.setId(3);
//                a1.setNombreGenerico("Precio 3");
//                a1.setPrecioCompra(artPrices.getPrecioVenta3());
//                arrayList.add(a1);
//            }
            tvList.setItems(arrayList);
            if(!tvList.getItems().isEmpty()){
                tvList.requestFocus();
                tvList.getSelectionModel().select(0);
            }
        }

        lblNombreArticulo.setText(articuloTB.getNombreMarca());
    }

    private void onSelectPrice() {
//        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
//            articuloTB.setPrecioVenta(tvList.getSelectionModel().getSelectedItem().getPrecioCompra());
//            articuloTB.setTotalImporte(
//                    (articuloTB.getCantidad() * articuloTB.getPrecioVenta())
//                    - articuloTB.getDescuento()
//            );
//            ventaController.getTvList().getItems().set(index, articuloTB);
//            ventaController.calculateTotales();
//            Tools.Dispose(window);
//        }
    }

    @FXML
    private void onKeyPressedList(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onSelectPrice();
        }
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
        if (event.getClickCount() == 2) {
            onSelectPrice();
        }
    }

    public void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

}
