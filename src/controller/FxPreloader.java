package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.ClienteADO;
import model.ClienteTB;
import model.ConsultasADO;
import model.DBUtil;
import model.DetalleADO;
import model.EmpresaADO;
import model.EmpresaTB;

public class FxPreloader extends Preloader {

    private static final double WIDTH = 600;
    private static final double HEIGHT = 400;

    private Stage preloaderStage;
    private Scene scene;

    private Label progress;

    public FxPreloader() {
        Session.CONNECTION_SESSION = false;
    }

    @Override
    public void init() throws Exception {
        Platform.runLater(() -> {
            Image image = new Image("/view/logo.png");
            Label titlelogo = new Label();
            titlelogo.setGraphic(new ImageView(image));
            Label title = new Label("Iniciando la aplicación!\nCargando, por favor espere...");
            title.setTextAlignment(TextAlignment.CENTER);
            title.getStyleClass().add("labelRoboto16");
            title.getStyleClass().add("splashtext");
            progress = new Label("0%");
            progress.getStyleClass().add("labelRoboto18");
            progress.getStyleClass().add("splashtext");
            VBox root = new VBox(titlelogo, title, progress);
            root.getStyleClass().add("splashbackground");
            root.setAlignment(Pos.CENTER);

            scene = new Scene(root, WIDTH, HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/view/styles.css").toExternalForm());

        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;
        preloaderStage.getIcons().add(new Image(Tools.FX_LOGO));
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        preloaderStage.setScene(scene);
        preloaderStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof ProgressNotification) {
            progress.setText(((ProgressNotification) info).getProgress() + "%");
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_LOAD:
                System.out.println("BEFORE_LOAD");
                break;
            case BEFORE_INIT:
                System.out.println("BEFORE_INIT");
                TimerService service = new TimerService();
                service.setPeriod(Duration.seconds(59));
                service.setOnSucceeded((WorkerStateEvent t) -> {
                    try {
                        DBUtil.dbConnect();
                        Session.CONNECTION_SESSION = true;
                        DBUtil.dbDisconnect();
                        ExecutorService exec = Executors.newFixedThreadPool(1);
                        ExecutorCompletionService completionService = new ExecutorCompletionService<>(exec);
                        completionService.submit(ConsultasADO.TotalObjectInit());
                        Long[] tipos = (Long[]) completionService.take().get();
                        Session.ARTICULOS_TOTAL = "" + tipos[0];
                        Session.CLIENTES_TOTAL = "" + tipos[1];
                        Session.PROVEEDORES_TOTAL = "" + tipos[2];
                        Session.TRABAJADORES_TOTAL = "" + tipos[3];
                        System.out.println("dentro-----------------");
                        System.out.println(Session.TRABAJADORES_TOTAL);
                        if (!exec.isShutdown()) {
                            exec.shutdown();
                        }
                    } catch (SQLException | InterruptedException | ExecutionException ex) {
                        System.out.println(getClass().getName() + ":" + ex);
                    }

                });
                service.start();

                ArrayList<EmpresaTB> list = EmpresaADO.GetEmpresa();
                if (!list.isEmpty()) {
                    Session.EMPRESA = list.get(0).getRazonSocial().equalsIgnoreCase(list.get(0).getNombre()) ? list.get(0).getNombre() : list.get(0).getRazonSocial();
                    Session.TELEFONO = list.get(0).getTelefono();
                    Session.CELULAR = list.get(0).getCelular();
                    Session.PAGINAWEB = list.get(0).getPaginaWeb();
                    Session.EMAIL = list.get(0).getEmail();
                    Session.DIRECCION = list.get(0).getDomicilio();
                }
                DetalleADO.GetDetailIdName("3", "0010", "").forEach(e -> {
                    Session.IMPUESTO = Double.parseDouble(e.getDescripcion().get());
                });

                ClienteTB clienteTB = ClienteADO.GetByIdClienteVenta("00000000");
                if (clienteTB != null) {
                    Session.IDCLIENTE = clienteTB.getIdCliente();
                    Session.DATOSCLIENTE = clienteTB.getApellidos() + " " + clienteTB.getNombres();
                }
                System.out.println("BEFORE_INIT");
                break;
            case BEFORE_START:
                preloaderStage.hide();
                System.out.println("BEFORE_START");
                break;
        }
    }
}
