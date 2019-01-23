package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class FxImpresoraTicketController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ComboBox<String> cbImpresoras;
    @FXML
    private CheckBox cbCortarPapel;

    private FxVentaController ventaController;

    private PrinterService printerService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        printerService = new PrinterService();
        printerService.getPrinters().forEach(e -> {
            cbImpresoras.getItems().add(e);
        });
        for (int i = 0; i < cbImpresoras.getItems().size(); i++) {
            if (cbImpresoras.getItems().get(i).equalsIgnoreCase(Session.NAME_IMPRESORA)) {
                cbImpresoras.getSelectionModel().select(i);
                break;
            }
        }
        if (Session.CORTA_PAPEL != null) {
            cbCortarPapel.setSelected(Session.CORTA_PAPEL.equalsIgnoreCase("1"));
        }
    }

    private void eventGuardarImpresora() {
        if (cbImpresoras.getSelectionModel().getSelectedIndex() >= 0) {
            String ruta = "./archivos/impresoraticket.txt";
            File archivo;
            BufferedWriter bw = null;
            try {
                archivo = new File(ruta);
                if (archivo.exists()) {
                    bw = new BufferedWriter(new FileWriter(archivo));
                    bw.write(cbImpresoras.getSelectionModel().getSelectedItem());
                    bw.newLine();
                    bw.write(cbCortarPapel.isSelected() ? "1" : "0");
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Impresora de ticket", "Se guardo la configuración", false);
                } else {
                    bw = new BufferedWriter(new FileWriter(archivo));
                    bw.write(cbImpresoras.getSelectionModel().getSelectedItem());
                    bw.newLine();
                    bw.write(cbCortarPapel.isSelected() ? "1" : "0");
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Impresora de ticket", "Se guardo la configuración", false);
                }
            } catch (IOException e) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Impresora de ticket", "Error al crear el archivo:" + e.getLocalizedMessage(), false);
            } finally {
                try {
                    if (null != bw) {
                        bw.close();
                    }
                    iniciarRutasImpresion();
                } catch (IOException e2) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Impresora de ticket", "Error al finalizar el creado del archivo:" + e2.getLocalizedMessage(), false);

                }
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Impresora de ticket", "Seleccione una impresora", false);

        }
    }

    private void iniciarRutasImpresion() {
        File archivo;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            archivo = new File("./archivos/impresoraticket.txt");
            if (archivo.exists()) {
                Session.STATE_IMPRESORA = true;
                // Apertura del fichero y creacion de BufferedReader para poder
                // hacer una lectura comoda (disponer del metodo readLine()).
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                // Lectura del fichero                           
                Session.NAME_IMPRESORA = br.readLine();
                Session.CORTA_PAPEL = br.readLine();
                System.out.println(Session.NAME_IMPRESORA);
                System.out.println(Session.CORTA_PAPEL);
                Tools.Dispose(window);
            } else {
                Session.STATE_IMPRESORA = false;
                Tools.Dispose(window);
            }

        } catch (IOException e) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Impresora de ticket", "Error al leer el archivo:" + e.getLocalizedMessage(), false);
            Session.STATE_IMPRESORA = false;
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
                if (null != br) {
                    br.close();
                }
            } catch (IOException e2) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Impresora de ticket", "Error al finalizar la lectura del archivo:" + e2.getLocalizedMessage(), false);

            }
        }
    }

    @FXML
    private void onKeyPressedGuardar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            eventGuardarImpresora();
        }
    }

    @FXML
    private void onActionGuardar(ActionEvent event) {
        eventGuardarImpresora();
    }

    private void eventImprimirPrueba() {
        if (cbImpresoras.getSelectionModel().getSelectedIndex() >= 0) {

            if (cbCortarPapel.isSelected()) {
                String text = "Impresora " + cbImpresoras.getSelectionModel().getSelectedItem()
                        + "\nPara uso de todo tipo de tickets"
                        + "\nCorta papel"
                        + "\n\n\n\n\n\n\n\n\n\n";
                printerService.printString(cbImpresoras.getSelectionModel().getSelectedItem(), text);
                byte[] cutP = new byte[]{0x1d, 'V', 1};
                printerService.printBytes(cbImpresoras.getSelectionModel().getSelectedItem(), cutP);
            } else {
                String text = "Impresora " + cbImpresoras.getSelectionModel().getSelectedItem()
                        + "\nPara uso de todo tipo de tickets"
                        + "\nNo corta papel"
                        + "\n\n\n\n\n\n\n\n\n\n";
                printerService.printString(cbImpresoras.getSelectionModel().getSelectedItem(), text);
            }
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Impresora de ticket", "Seleccione una impresora", false);
        }
    }

    @FXML
    private void onKeyPressedProbar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            eventImprimirPrueba();
        }
    }

    @FXML
    private void onActionProbar(ActionEvent event) {
        eventImprimirPrueba();
    }

    public void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

}
