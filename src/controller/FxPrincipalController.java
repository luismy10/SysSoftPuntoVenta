package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

public class FxPrincipalController implements Initializable {

    @FXML
    private ScrollPane window;
    @FXML
    private Text lblFechaActual;
    @FXML
    private Text lblArticulo;
    @FXML
    private Text lblCliente;
    @FXML
    private Text lblProveedor;
    @FXML
    private Text lblTrabajador;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize");
        lblFechaActual.setText(Tools.getDate("EEEE d 'de' MMMM 'de' yyyy"));        
        lblArticulo.setText(Session.ARTICULOS_TOTAL);
        lblCliente.setText(Session.CLIENTES_TOTAL);
        lblProveedor.setText(Session.PROVEEDORES_TOTAL);
        lblTrabajador.setText(Session.TRABAJADORES_TOTAL);
        System.out.println(Session.TRABAJADORES_TOTAL);
    }

}
