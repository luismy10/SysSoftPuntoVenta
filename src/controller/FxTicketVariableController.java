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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.TicketTB;

public class FxTicketVariableController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ListView<TicketTB> lvLista;
    @FXML
    private TextField txtContenido;

    private FxTicketController ticketController;

    private ArrayList<TicketTB> listCabecera;

    private ArrayList<TicketTB> listDetalleCuerpo;

    private ArrayList<TicketTB> listPie;

    private HBox hBox;

    private int sheetWidth;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        listCabecera = new ArrayList<>();
        listDetalleCuerpo = new ArrayList<>();
        listPie = new ArrayList<>();
    }

    public void setLoadComponent(HBox hBox, int sheetWidth) {
        this.hBox = hBox;
        this.sheetWidth = sheetWidth;
        if (hBox.getId().substring(0, 2).equalsIgnoreCase("cb")) {
            listCabecera.add(new TicketTB("Representante de la empresa", Session.REPRESENTANTE_EMPRESA, "repeempresa"));
            listCabecera.add(new TicketTB("Telefono de la empresa", Session.TELEFONO_EMPRESA, "telempresa"));
            listCabecera.add(new TicketTB("Celular de la empresa", Session.CELULAR_EMPRESA, "celempresa"));
            listCabecera.add(new TicketTB("Pagina web de la empresa", Session.PAGINAWEB_EMPRESA, "pagwempresa"));
            listCabecera.add(new TicketTB("Email de la empresa", Session.EMAIL_EMPRESA, "emailempresa"));
            listCabecera.add(new TicketTB("Dirección de la empresa", Session.DIRECCION_EMPRESA, "direcempresa"));
            listCabecera.add(new TicketTB("Ruc de la empresa", Session.RUC_EMPRESA, "rucempresa"));
            listCabecera.add(new TicketTB("Razón social de la empresa", Session.RAZONSOCIAL_EMPRESA, "razoempresa"));
            listCabecera.add(new TicketTB("Nombre comercial de la empresa", Session.NOMBRECOMERCIAL_EMPRESA, "nomcomempresa"));
            listCabecera.add(new TicketTB("Fecha actual", Tools.getDate("dd/MM/yyyy"), "fchactual"));
            listCabecera.add(new TicketTB("Hora actual", Tools.getHour("hh:mm:ss aa"), "horactual"));
            listCabecera.add(new TicketTB("Nombre del documento de venta", "Documento de venta", "docventa"));
            listCabecera.add(new TicketTB("Numeración del documento de venta", "V000-00000000", "numventa"));
            lvLista.getItems().addAll(listCabecera);
        } else if (hBox.getId().substring(0, 2).equalsIgnoreCase("dr")) {
            listDetalleCuerpo.add(new TicketTB("Código de barras", "789456123789", "codbarrasarticulo"));
            listDetalleCuerpo.add(new TicketTB("Descripción del árticulo", "Descripcion del articulo para la venta", "nombretarticulo"));
            listDetalleCuerpo.add(new TicketTB("Cantidad por árticulo", "1000", "cantarticulo"));
            listDetalleCuerpo.add(new TicketTB("Precio unitario por árticulo", "0000.00", "precarticulo"));
            listDetalleCuerpo.add(new TicketTB("Descuento por árticulo", "0000.00", "descarticulo"));
            listDetalleCuerpo.add(new TicketTB("Importe por árticulo", "0000.00", "impoarticulo"));
            lvLista.getItems().addAll(listDetalleCuerpo);
        } else if (hBox.getId().substring(0, 2).equalsIgnoreCase("cp")) {
            listPie.add(new TicketTB("Importe total", "M 00.00", "imptotal"));
            listPie.add(new TicketTB("Sub total", "M 00.00", "subtotal"));
            listPie.add(new TicketTB("Descuento total", "M 00.00", "dscttotal"));
            listPie.add(new TicketTB("Total a pagar", "M 00.00", "totalpagar"));
            listPie.add(new TicketTB("Efectivo", "M 00.00", "efectivo"));
            listPie.add(new TicketTB("Vuelto", "M 00.00", "vuelto"));
            lvLista.getItems().addAll(listPie);
        }
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
                TextFieldTicket field = ticketController.addElementTextField("iu", lvLista.getSelectionModel().getSelectedItem().getVariable().toString(), false, 0, widthNew, Pos.CENTER_LEFT, false, lvLista.getSelectionModel().getSelectedItem().getIdVariable());
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

    @FXML
    private void onMouseClickedList(MouseEvent event) {
        if (event.getClickCount() == 2) {
            addTextVariable();
        } else if (event.getClickCount() == 1) {
            if (!lvLista.getItems().isEmpty()) {
                txtContenido.setText(lvLista.getSelectionModel().getSelectedItem().getVariable().toString());
            }
        }
    }

    public void setInitTicketController(FxTicketController ticketController) {
        this.ticketController = ticketController;
    }

}
