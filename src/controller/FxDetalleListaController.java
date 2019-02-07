package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.DetalleADO;
import model.DetalleTB;

public class FxDetalleListaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtSearch;
    @FXML
    private ListView<DetalleTB> lvList;

    private String idMantenimiento;
    
    private boolean esVerdadero;

    private FxArticuloProcesoController procesoController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        idMantenimiento = "";
    }

    public void initListDetalle(String idDetalle, String nombre) {
        this.idMantenimiento = idDetalle;
        lvList.getItems().clear();
        DetalleADO.GetDetailIdName("4", idMantenimiento, nombre).forEach(e -> {
            lvList.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
    }
    
    public void initListNameImpuesto(String idDetalle){
        this.idMantenimiento = idDetalle;
        this.lvList.getItems().clear();
        DetalleADO.GetDetailNameImpuesto().forEach(e ->{
            this.lvList.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
    }

    private void openWindowProceso() throws IOException {

        URL url = getClass().getResource(Tools.FX_FILE_DETALLEPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxDetalleProcesoController controller = fXMLLoader.getController();
        controller.setControllerDetalleLista(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Detalle Agregar", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        controller.setInitComponents(0, idMantenimiento);
    }

    private void openWindowEditar() throws IOException {
        if (lvList.getSelectionModel().getSelectedIndex() >= 0) {
            URL url = getClass().getResource(Tools.FX_FILE_DETALLEPROCESO);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            FxDetalleProcesoController controller = fXMLLoader.getController();
            controller.setControllerDetalleLista(this);
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Detalle Editar", window.getScene().getWindow());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
            controller.setInitComponents(lvList.getSelectionModel().getSelectedItem().getIdDetalle().get(),
                    idMantenimiento);
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle lista", "Seleccione un elemento de la lista para editarlo", false);
            lvList.requestFocus();
        }

    }

    @FXML
    private void onKeyPressedProceso(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowProceso();
        }
    }

    @FXML
    private void onActionProceso(ActionEvent event) throws IOException {
        openWindowProceso();
    }

    @FXML
    private void onKeyPressedEditar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowEditar();
        }
    }

    @FXML
    private void onActionEditar(ActionEvent event) throws IOException {
        openWindowEditar();
    }

    @FXML
    private void onKeyPressedToSearh(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            lvList.requestFocus();
            lvList.getSelectionModel().select(0);
        }
    }

    @FXML
    private void onKeyReleasedToSearch(KeyEvent event) {
        initListDetalle(idMantenimiento, txtSearch.getText().trim());
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
        if (lvList.getSelectionModel().getSelectedIndex() >= 0 && lvList.isFocused()) {
            if (event.getClickCount() == 2) {
                if (idMantenimiento.equalsIgnoreCase("0008")) {
                    procesoController.setIdPresentacion(lvList.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    procesoController.getTxtPresentacion().setText(lvList.getSelectionModel().getSelectedItem().getNombre().get());
                    Tools.Dispose(window);
                } else if (idMantenimiento.equalsIgnoreCase("0007")) {
                    procesoController.setIdMarca(lvList.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    procesoController.getTxtMarca().setText(lvList.getSelectionModel().getSelectedItem().getNombre().get());
                    Tools.Dispose(window);
                } else if (idMantenimiento.equalsIgnoreCase("0006")) {
                    procesoController.setIdCategoria(lvList.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    procesoController.getTxtCategoria().setText(lvList.getSelectionModel().getSelectedItem().getNombre().get());
                    Tools.Dispose(window);
                } else if (idMantenimiento.equalsIgnoreCase("0013")) {
                    procesoController.setIdMedida(lvList.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    procesoController.getTxtMedida().setText(lvList.getSelectionModel().getSelectedItem().getNombre().get());
                    Tools.Dispose(window);
                } 
//                else if (idMantenimiento.equalsIgnoreCase("0010")) {
//                    procesoController.setIdImpuesto(lvList.getSelectionModel().getSelectedItem().getIdDetalle().get());
//                    procesoController.getTxtImpuesto().setText(lvList.getSelectionModel().getSelectedItem().getNombre().get());
//                    Tools.Dispose(window);
//                }
                else if (idMantenimiento.equalsIgnoreCase("0")) {
                    procesoController.setIdImpuesto(lvList.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    procesoController.getTxtImpuesto().setText(lvList.getSelectionModel().getSelectedItem().getNombre().get());
                    Tools.Dispose(window);
                }

            }
        }
    }

    @FXML
    private void onKeyPressedList(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (lvList.getSelectionModel().getSelectedIndex() >= 0 && lvList.isFocused()) {
                if (idMantenimiento.equalsIgnoreCase("0008")) {
                    procesoController.setIdPresentacion(lvList.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    procesoController.getTxtPresentacion().setText(lvList.getSelectionModel().getSelectedItem().getNombre().get());
                    Tools.Dispose(window);
                } else if (idMantenimiento.equalsIgnoreCase("0007")) {
                    procesoController.setIdMarca(lvList.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    procesoController.getTxtMarca().setText(lvList.getSelectionModel().getSelectedItem().getNombre().get());
                    Tools.Dispose(window);
                } else if (idMantenimiento.equalsIgnoreCase("0006")) {
                    procesoController.setIdCategoria(lvList.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    procesoController.getTxtCategoria().setText(lvList.getSelectionModel().getSelectedItem().getNombre().get());
                    Tools.Dispose(window);
                } else if (idMantenimiento.equalsIgnoreCase("0010")) {
                    procesoController.setIdImpuesto(lvList.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    procesoController.getTxtImpuesto().setText(lvList.getSelectionModel().getSelectedItem().getNombre().get());
                    Tools.Dispose(window);
                }
            }
        }
    }

    public void setControllerArticulo(FxArticuloProcesoController procesoController) {
        this.procesoController = procesoController;
    }

}
