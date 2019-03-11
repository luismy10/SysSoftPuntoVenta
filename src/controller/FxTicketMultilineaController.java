package controller;

import com.sun.javafx.scene.control.skin.TextAreaSkin;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class FxTicketMultilineaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextArea textArea;

    private FxTicketController ticketController;

    private HBox hBox;
    
    private int sheetWidth;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
    }

    public void setLoadComponent(HBox hBox,int sheetWidth) {
        this.hBox = hBox;
        this.sheetWidth = sheetWidth;
    }

    private void addTextMultilinea() {
        if (!textArea.getText().trim().isEmpty()) {
            int widthContent = 0;
            for (int i = 0; i < hBox.getChildren().size(); i++) {
                TextFieldTicket fieldTicket = ((TextFieldTicket) hBox.getChildren().get(i));
                widthContent += fieldTicket.getColumnWidth();
            }
            if (widthContent <= sheetWidth) {
                int widthNew = sheetWidth - widthContent;
                TextFieldTicket field = ticketController.addElementTextField("iu", textArea.getText(), true, 1, widthNew, Pos.CENTER_LEFT, true);
                hBox.getChildren().add(field);
                Tools.Dispose(window);
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Ticket", "El área de texto no puede estar vacío.", false);
        }
    }

    @FXML
    private void onKeyPressedAdd(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            addTextMultilinea();
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) {
        addTextMultilinea();
    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Tools.Dispose(window);
    }

    @FXML
    private void onKeyPresseTextArea(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            Node node = (Node) event.getSource();
            if (node instanceof TextArea) {
                TextAreaSkin skin = (TextAreaSkin) ((TextArea) node).getSkin();
                if (!event.isControlDown()) {
                    if (event.isShiftDown()) {
                        skin.getBehavior().traversePrevious();
                    } else {
                        skin.getBehavior().traverseNext();
                    }
                } else {
                    TextArea textA = (TextArea) node;
                    textA.replaceSelection("\t");
                }
                event.consume();
            }
        }
    }

    public void setInitTicketController(FxTicketController ticketController) {
        this.ticketController = ticketController;
    }

}
