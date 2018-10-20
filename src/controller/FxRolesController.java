package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.DetalleADO;
import model.DetalleTB;
import model.MenuADO;

public class FxRolesController implements Initializable {

    @FXML
    private ListView<DetalleTB> lvUsuarios;
    @FXML
    private ListView<CheckBox> lvMenus;
    @FXML
    private ListView<CheckBox> lbSubmenus;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DetalleADO.GetDetailIdName("2", "0012", "").forEach(e -> {
            lvUsuarios.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });

        MenuADO.GetMenus().forEach(e -> {
            CheckBox checkBox = new CheckBox();
            checkBox.setId(""+e.getIdMenu()); 
            checkBox.setText(e.getNombre());
            checkBox.setSelected(e.isEstado());
            lvMenus.getItems().add(checkBox);
        });

    }

    @FXML
    private void onActionRegister(ActionEvent event) {

    }

    @FXML
    private void onActionReload(ActionEvent event) {

    }

    @FXML
    private void onMouseClickedMenus(MouseEvent event) {
        if (lvMenus.getSelectionModel().getSelectedIndex() >= 0 && lvMenus.isFocused()) {
            lbSubmenus.getItems().clear();
            MenuADO.GetSubMenus(Integer.parseInt(lvMenus.getSelectionModel().getSelectedItem().getId())).forEach(e -> {
                CheckBox checkBox = new CheckBox();
                checkBox.setText(e.getNombre());
                checkBox.setSelected(e.isEstado());
                lbSubmenus.getItems().add(checkBox);
            });
        }
    }

    void setContent(AnchorPane content) {
        this.content = content;
    }

}
