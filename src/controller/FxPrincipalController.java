package controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
        //lblFechaActual.setText(Tools.getDate("EEEE d 'de' MMMM 'de' yyyy"));
        initClock();
        lblArticulo.setText(Session.ARTICULOS_TOTAL);
        lblCliente.setText(Session.CLIENTES_TOTAL);
        lblProveedor.setText(Session.PROVEEDORES_TOTAL);
        lblTrabajador.setText(Session.TRABAJADORES_TOTAL);
    }

    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d ', de' MMMM 'de' yyyy HH:mm:ss");
            lblFechaActual.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

}
