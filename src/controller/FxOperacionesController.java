package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FxOperacionesController implements Initializable {

    @FXML
    private HBox window;
    @FXML
    private TextField txtSearch;

    private AnchorPane windowinit;

    private AnchorPane content;

    private FXMLLoader fXMLVenta;

    private VBox nodeVenta;

    private FxVentaController controllerVenta;

    private FXMLLoader fXMLArticulo;

    private VBox nodeArticulo;

    private FxArticulosController controllerArticulo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            fXMLVenta = new FXMLLoader(getClass().getResource(Tools.FX_FILE_VENTA));
            nodeVenta = fXMLVenta.load();
            controllerVenta = fXMLVenta.getController();

            fXMLArticulo = new FXMLLoader(getClass().getResource(Tools.FX_FILE_ARTICULO));
            nodeArticulo = fXMLArticulo.load();
            controllerArticulo = fXMLArticulo.getController();
        } catch (IOException ex) {
            Logger.getLogger(FxOperacionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openWindowCustomers() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_CLIENTE));
        VBox node = fXMLPrincipal.load();
        FxClienteController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
        controller.fillCustomersTable("");
    }

    private void openWindowProviders() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_PROVEEDORES));
        VBox node = fXMLPrincipal.load();
        FxProveedorController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
        controller.fillCustomersTable("");

    }

    private void openWindowArticulos() throws IOException {
        controllerArticulo.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(nodeArticulo, 0d);
        AnchorPane.setTopAnchor(nodeArticulo, 0d);
        AnchorPane.setRightAnchor(nodeArticulo, 0d);
        AnchorPane.setBottomAnchor(nodeArticulo, 0d);
        content.getChildren().add(nodeArticulo);
        controllerArticulo.fillArticlesTable((short)1,"",0);

    }

    private void openWindowCompras() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_COMPRAS));
        ScrollPane node = fXMLPrincipal.load();
        FxCompraController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
    }

    private void openWindowLote() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_LOTE));
        VBox node = fXMLPrincipal.load();
        FxLoteController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
        controller.loadDataInitial();
    }

    private void openWindowVenta() throws IOException {
        controllerVenta.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(nodeVenta, 0d);
        AnchorPane.setTopAnchor(nodeVenta, 0d);
        AnchorPane.setRightAnchor(nodeVenta, 0d);
        AnchorPane.setBottomAnchor(nodeVenta, 0d);
        content.getChildren().add(nodeVenta);
        controllerVenta.loadValidarCaja();
        controllerVenta.getTxtSearch().requestFocus();
    }

    private void openWindowInventario() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_INVENTARIOINICIAL));
        VBox node = fXMLPrincipal.load();
        FxInventarioInicialController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);

    }

    private void openWindowBoxCut() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_CAJA));
        VBox node = fXMLPrincipal.load();
        FxCajaController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);

    }

    @FXML
    private void onKeyPressedCustomers(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowCustomers();
        }
    }

    @FXML
    private void onActionCustomers(ActionEvent event) throws IOException {
        openWindowCustomers();
    }

    @FXML
    private void onKeyPressedProviders(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowProviders();
        }
    }

    @FXML
    private void onActionProviders(ActionEvent event) throws IOException {
        openWindowProviders();
    }

    @FXML
    private void onKeyPressedArticulos(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowArticulos();
        }
    }

    @FXML
    private void onActionArticulos(ActionEvent event) throws IOException {
        openWindowArticulos();
    }

    @FXML
    private void onKeyPressedCompras(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowCompras();
        }
    }

    @FXML
    private void onActionCompras(ActionEvent event) throws IOException {
        openWindowCompras();
    }

    @FXML
    private void onActionLotes(ActionEvent event) throws IOException {
        openWindowLote();

    }

    @FXML
    private void onKeyPressedLotes(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowLote();
        }
    }

    @FXML
    private void onKeyPressedVentas(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowVenta();
        }
    }

    @FXML
    private void onActionVentas(ActionEvent event) throws IOException {
        openWindowVenta();
    }

    @FXML
    private void onKeyPressedInventario(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowInventario();
        }
    }

    @FXML
    private void onActionInventario(ActionEvent event) throws IOException {
        openWindowInventario();
    }

    @FXML
    private void onActionCorteCaja(ActionEvent event) throws IOException {
        openWindowBoxCut();
//        if (Session.USER_NAME_PUESTO.equalsIgnoreCase("administrador")) {
//            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Caja", "El administrador no esta facultado para hacer el corte de caja.", false);
//        } else {
//            openWindowBoxCut();
//        }
    }

    @FXML
    private void onKeyPressedCorteCaja(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowBoxCut();
        }
//        if (Session.USER_NAME_PUESTO.equalsIgnoreCase("administrador")) {
//            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Caja", "El administrador no esta facultado para hacer el corte de caja.", false);
//        } else {
//            openWindowBoxCut();
//        }
    }

    public void setContent(AnchorPane windowinit, AnchorPane content) {
        this.windowinit = windowinit;
        this.content = content;
    }

}
