package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class FxConsultasController implements Initializable {

    @FXML
    private TextField txtSearch;

    private AnchorPane vbContent;

    private AnchorPane windowinit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void openWindowPurchasesMade() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_COMPRASREALIZADAS));
        VBox node = fXMLPrincipal.load();
        FxComprasRealizadasController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit,vbContent);
        vbContent.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        vbContent.getChildren().add(node);
        
        controller.fillPurchasesTable((short)1,"",Tools.getDate(),Tools.getDate(),"");
    }

    private void openWindowDirectory() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_DIRECTORIO));
        VBox node = fXMLPrincipal.load();
        FxDirectorioController controller = fXMLPrincipal.getController();
        vbContent.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        vbContent.getChildren().add(node);
        controller.fillEmployeeTable("");

    }

    private void openWindowArticulo() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_ARTICULOHISTORIAL));
        VBox node = fXMLPrincipal.load();
        FxArticuloHistorialController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        vbContent.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        vbContent.getChildren().add(node);
    }

    private void openWindowVentas() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_VENTAREALIZADAS));
        VBox node = fXMLPrincipal.load();
        FxVentaRealizadasController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        vbContent.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        vbContent.getChildren().add(node);
    }

    private void openWindowInvetario() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_INVENTARIOGENERAL));
        VBox node = fXMLPrincipal.load();
        FxInventarioGeneralController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        vbContent.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        vbContent.getChildren().add(node);
        controller.fillInventarioTable();
    }

    @FXML
    private void onKeyPressedCompras(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowPurchasesMade();
        }
    }

    @FXML
    private void onActionCompras(ActionEvent event) throws IOException {
        openWindowPurchasesMade();
    }

    @FXML
    private void onKeyPressedDirectory(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowDirectory();
        }
    }

    @FXML
    private void onActionDirectory(ActionEvent event) throws IOException {
        openWindowDirectory();

    }

    @FXML
    private void onKeyPressedVentas(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowVentas();
        }
    }

    @FXML
    private void onActionVentas(ActionEvent event) throws IOException {
        openWindowVentas();
    }

    @FXML
    private void onKeyPressedHistorial(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowArticulo();
        }
    }

    @FXML
    private void onActionHistorial(ActionEvent event) throws IOException {
        openWindowArticulo();
    }

    @FXML
    private void onKeyPressedInventario(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowInvetario();
        }
    }

    @FXML
    private void onActionInventario(ActionEvent event) throws IOException {
        openWindowInvetario();
    }

    public void setContent(AnchorPane windowinit, AnchorPane vbContent) {
        this.windowinit = windowinit;
        this.vbContent = vbContent;
    }

}
