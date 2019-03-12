package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.TicketTB;

public class FxTicketVariableController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtSearch;
    @FXML
    private ListView<TicketTB> lvLista;

    private FxTicketController ticketController;

    private ArrayList<TicketTB> listCabecera;

    private HBox hBox;

    private int sheetWidth;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        listCabecera = new ArrayList<>();
        listCabecera.add(new TicketTB("Representante de la empresa", Session.NOMBRE_REPRESENTANTE));
        listCabecera.add(new TicketTB("Telefono de la empresa", Session.TELEFONO_EMPRESA));
        listCabecera.add(new TicketTB("Celular de la empresa", Session.CELULAR_EMPRESA));
        listCabecera.add(new TicketTB("Pagina web de la empresa", Session.PAGINAWEB_EMPRESA));
        listCabecera.add(new TicketTB("Email de la empresa", Session.EMAIL_EMPRESA));
        listCabecera.add(new TicketTB("Dirección de la empresa", Session.DIRECCION_EMPRESA));
        listCabecera.add(new TicketTB("Ruc de la empresa", Session.RUC_EMPRESA));
        listCabecera.add(new TicketTB("Nombre de la empresa", Session.NOMBRE_EMPRESA));
        listCabecera.add(new TicketTB("Fecha actual", Tools.getDate("dd/MM/yyyy")));
        listCabecera.add(new TicketTB("Hora actual", Tools.getHour("hh:mm:ss aa")));
        lvLista.getItems().addAll(listCabecera);
    }

    public void setLoadComponent(HBox hBox, int sheetWidth) {
        this.hBox = hBox;
        this.sheetWidth = sheetWidth;
    }

    private void addTextVariable() {
        if (lvLista.getSelectionModel().getSelectedIndex() >= 0) {
            int widthContent = 0;
            for (int i = 0; i < hBox.getChildren().size(); i++) {
                TextFieldTicket fieldTicket = ((TextFieldTicket) hBox.getChildren().get(i));
                widthContent += fieldTicket.getColumnWidth();
            }
            if (widthContent <= sheetWidth) {
                int widthNew = sheetWidth - widthContent;
                TextFieldTicket field = ticketController.addElementTextField("iu", lvLista.getSelectionModel().getSelectedItem().getVariable().toString(), false, 1, widthNew, Pos.CENTER_LEFT, false);
                hBox.getChildren().add(field);
                Tools.Dispose(window);
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Ticket", "Seleccione un item de la lista.", false);
        }
    }

    @FXML
    private void onKeyPressedAdd(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            addTextVariable();
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) {
        addTextVariable();
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

    public void setInitTicketController(FxTicketController ticketController) {
        this.ticketController = ticketController;
    }

}
