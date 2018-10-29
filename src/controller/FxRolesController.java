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
import model.RolADO;
import model.RolTB;

public class FxRolesController implements Initializable {

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

    @FXML
    private void onActionReload(ActionEvent event) {

    }

    @FXML
    private void onMouseClickedMenus(MouseEvent event) {
        if (lvMenus.getSelectionModel().getSelectedIndex() >= 0 && lvMenus.isFocused()) {
            lbSubmenus.getItems().clear();
//            MenuADO.GetSubMenus(Integer.parseInt(lvMenus.getSelectionModel().getSelectedItem().getId())).forEach(e -> {
//                CheckBox checkBox = new CheckBox();
//                checkBox.setText(e.getNombre());
//                checkBox.setSelected(e.isEstado());
//                lbSubmenus.getItems().add(checkBox);
//            });
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
            });
        }
    }

    void setContent(AnchorPane content) {
        this.content = content;
    }

}
