package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class FxPersonaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ComboBox<?> cbDocumentType;
    @FXML
    private TextField txtDocumentNumber;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtMotherLastName;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtSecondName;
    @FXML
    private ComboBox<?> cbSex;
    @FXML
    private DatePicker dpBirthdate;
    @FXML
    private Button btnDirectory;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void onKeyPressedToDirectory(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionToDirectory(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedToCancel(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionToCancel(ActionEvent event) {

    }

}
