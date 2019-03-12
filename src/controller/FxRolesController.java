package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.MenuADO;
import model.RolADO;
import model.RolTB;

public class FxRolesController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private ListView<RolTB> lvRol;
    @FXML
    private ListView<CheckBox> lvMenus;
    @FXML
    private ListView<CheckBox> lbSubmenus;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RolADO.RolList().forEach(e -> {
            lvRol.getItems().add(new RolTB(e.getIdRol(), e.getNombre()));
        });

    }

    private void InitializationTransparentBackground() {
        Session.PANE.setStyle("-fx-background-color: black");
        Session.PANE.setTranslateX(0);
        Session.PANE.setTranslateY(0);
        Session.PANE.setPrefWidth(Session.WIDTH_WINDOW);
        Session.PANE.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.PANE.setOpacity(0.7f);
        content.getChildren().add(Session.PANE);
    }

    private void onViewRolesAgregar() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_ROLESPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxRolesProcesoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Rol", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.PANE);
        });
        stage.show();
      
    }

    @FXML
    private void onActionReload(ActionEvent event) {

    }

    @FXML
    private void onMouseClickedMenus(MouseEvent event) {
        if (lvMenus.getSelectionModel().getSelectedIndex() >= 0 && lvMenus.isFocused()) {
            lbSubmenus.getItems().clear();
            MenuADO.GetSubMenus(
                    lvRol.getSelectionModel().getSelectedItem().getIdRol(),
                    Integer.parseInt(lvMenus.getSelectionModel().getSelectedItem().getId())
            ).forEach(e -> {
                CheckBox checkBox = new CheckBox();
                checkBox.setText(e.getNombre());
                checkBox.setSelected(e.isEstado());
                lbSubmenus.getItems().add(checkBox);
            });
        }
    }

    @FXML
    private void onMouseClickedRoles(MouseEvent event) {
        if (lvRol.getSelectionModel().getSelectedIndex() >= 0 && lvRol.isFocused()) {
            lvMenus.getItems().clear();
            MenuADO.GetMenus(lvRol.getSelectionModel().getSelectedItem().getIdRol()).forEach(e -> {
                CheckBox checkBox = new CheckBox();
                checkBox.setId("" + e.getIdMenu());
                checkBox.setText(e.getNombre());
                checkBox.setSelected(e.isEstado());
                lvMenus.getItems().add(checkBox);
                lbSubmenus.getItems().clear();
            });
        }
    }

    @FXML
    private void onKeyPressedGuardar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionGuardar(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedAgregar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewRolesAgregar();
        }
    }

    @FXML
    private void onActionAgregar(ActionEvent event) throws IOException {
        onViewRolesAgregar();
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
