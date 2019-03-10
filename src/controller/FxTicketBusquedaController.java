
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class FxTicketBusquedaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ListView<?> lvLista;
    
    private FxTicketController ticketController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
    }    


    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Tools.Dispose(window);
    }

    @FXML
    private void onMouseClickedLista(MouseEvent event) {
        if(lvLista.getSelectionModel().getSelectedIndex()>=0){
            
        }
    }

    public void setInitTicketController(FxTicketController ticketController) {
        this.ticketController=ticketController;
    }
    
}
