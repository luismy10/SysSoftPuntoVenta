
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.LoginADO;
import model.LoginTB;


public class FxLoginController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtClave;

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onActionContactenos(ActionEvent event) {
    }

    @FXML
    private void onActionEntrar(ActionEvent event) {
        LoginTB loginTB = new LoginTB();
        loginTB.setUsuario(txtUsuario.getText());
        loginTB.setClave(txtClave.getText());
        if (LoginADO.ValidateLogin(loginTB))System.out.println("bien");
        else System.out.println("mal");
    }
    
}
