package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ArticuloADO;
import model.ArticuloTB;
import model.DBUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FxInventarioInicialController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblLoad;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, String> tcId;
    @FXML
    private TableColumn<ArticuloTB, String> tcClave;
    @FXML
    private TableColumn<ArticuloTB, String> tcArticulo;
    @FXML
    private TableColumn<ArticuloTB, String> tcLote;
    @FXML
    private TableColumn<ArticuloTB, String> tcCaducidad;
    @FXML
    private TableColumn<ArticuloTB, Double> tcCompra;
    @FXML
    private TableColumn<ArticuloTB, Double> tcPrecio;
    @FXML
    private TableColumn<ArticuloTB, Double> tcExistencias;

    private AnchorPane content;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getIdArticulo()));
        tcClave.setCellValueFactory(cellData -> cellData.getValue().getClave());
        tcArticulo.setCellValueFactory(cellData -> cellData.getValue().getNombreMarca());
        tcLote.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().isLote() ? "SI" : "NO"
        ));
        tcCaducidad.setCellValueFactory(cellData -> Bindings.concat(""));
        tcCompra.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecioCompra()).asObject());
        tcPrecio.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecioVenta()).asObject());
        tcExistencias.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getCantidad()).asObject());
    }
    
    private void InitializationTransparentBackground() {
        Session.pane.setStyle("-fx-background-color: black");
        Session.pane.setTranslateX(0);
        Session.pane.setTranslateY(0);
        Session.pane.setPrefWidth(Session.WIDTH_WINDOW);
        Session.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.pane.setOpacity(0.7f);
        content.getChildren().add(Session.pane);
    }

    public void fillArticlesTable() {
        if (DBUtil.StateConnection()) {

            ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });

            Task<List<ArticuloTB>> task = new Task<List<ArticuloTB>>() {
                @Override
                public ObservableList<ArticuloTB> call() {
                    return ArticuloADO.ListIniciarInventario();
                }
            };

            task.setOnSucceeded((WorkerStateEvent e) -> {
                tvList.setItems((ObservableList<ArticuloTB>) task.getValue());
                lblLoad.setVisible(false);
            });
            task.setOnFailed((WorkerStateEvent event) -> {
                lblLoad.setVisible(false);
            });

            task.setOnScheduled((WorkerStateEvent event) -> {
                lblLoad.setVisible(true);
            });
            exec.execute(task);

            if (!exec.isShutdown()) {
                exec.shutdown();
            }
        }

    }

    @FXML
    private void onActionBuscar(ActionEvent event) {

    }

    @FXML
    private void onActionIniciar(ActionEvent event) {
        fillArticlesTable();
    }

    @FXML
    private void onActionGenerar(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Inventario inicial");
        fileChooser.setInitialFileName("Inventario Inicial");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Libro de Excel (*.xlsx)", "*.xlsx"),
                new FileChooser.ExtensionFilter("Libro de Excel(1997-2003) (*.xls)", "*.xls")
        );
        File file = fileChooser.showSaveDialog(window.getScene().getWindow());
        if (file != null) {
            file = new File(file.getAbsolutePath());
            if (file.getName().endsWith("xls") || file.getName().endsWith("xlsx")) {
                try {
                    Workbook workbook;
                    if (file.getName().endsWith("xls")) {
                        workbook = new HSSFWorkbook();
                    } else {
                        workbook = new XSSFWorkbook();
                    }
                    
                    Sheet sheet = workbook.createSheet("Inventario inicial");
                   
                    Font font = workbook.createFont();
                    font.setFontHeightInPoints((short) 12);
                    font.setBold(true);
                    font.setColor(HSSFColor.BLACK.index);

                    CellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.setFont(font);
                    cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    String header[] = {"Id","Clave", "Artículo", "Lote", "Fecha de Caducidad", "Precio Compra", "Precio Venta", "Existencias"};

                    Row headerRow = sheet.createRow(0);
                    for (int i = 0; i < header.length; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(header[i].toUpperCase());

                    }

                    for (int i = 0; i < tvList.getItems().size(); i++) {
                        Row row = sheet.createRow(i + 1);
                        
                        Cell cell1 = row.createCell(0);
                        cell1.setCellValue(String.valueOf(Tools.getValueAt(tvList, i, 0)));
                        cell1.setCellType(Cell.CELL_TYPE_STRING);
                        sheet.autoSizeColumn(cell1.getColumnIndex());
                        
                        Cell cell2 = row.createCell(1);
                        cell2.setCellValue(String.valueOf(Tools.getValueAt(tvList, i, 1)));
                        cell2.setCellType(Cell.CELL_TYPE_STRING);
                        sheet.autoSizeColumn(cell2.getColumnIndex());

                        Cell cell3 = row.createCell(2);
                        cell3.setCellValue(Tools.getValueAt(tvList, i, 2).toString());
                        cell3.setCellType(Cell.CELL_TYPE_STRING);
                        sheet.autoSizeColumn(cell3.getColumnIndex());

                        Cell cell4 = row.createCell(3);
                        cell4.setCellValue(Tools.getValueAt(tvList, i, 3).toString());
                        cell4.setCellType(Cell.CELL_TYPE_STRING);
                        sheet.autoSizeColumn(cell4.getColumnIndex());

                        Cell cell5 = row.createCell(4);
                        cell5.setCellValue(Tools.getValueAt(tvList, i, 4).toString());
                        cell5.setCellType(Cell.CELL_TYPE_NUMERIC);
                        sheet.autoSizeColumn(cell5.getColumnIndex());

                        CellStyle cellStyleCell = workbook.createCellStyle();
                        cellStyleCell.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

                        Cell cell6 = row.createCell(5);
                        cell6.setCellValue(Double.parseDouble(Tools.getValueAt(tvList, i, 5).toString()));
                        cell6.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell6.setCellStyle(cellStyleCell);
                        sheet.autoSizeColumn(cell6.getColumnIndex());
                        
                        Cell cell7 = row.createCell(6);
                        cell7.setCellValue(Double.parseDouble(Tools.getValueAt(tvList, i, 6).toString()));
                        cell7.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell7.setCellStyle(cellStyleCell);
                        sheet.autoSizeColumn(cell7.getColumnIndex());

                        Cell cell8 = row.createCell(7);
                        cell8.setCellValue(Double.parseDouble(Tools.getValueAt(tvList, i, 7).toString()));
                        cell8.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell8.setCellStyle(cellStyleCell);
                        sheet.autoSizeColumn(cell8.getColumnIndex());
                        
                    }

                    try (FileOutputStream out = new FileOutputStream(file)) {
                        workbook.write(out);
                    }
                    workbook.close();
                    Tools.openFile(file.getAbsolutePath());
                    
                } catch (IOException ex) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Exportar", "Error al exportar el archivo, intente de nuevo.", false);
                }

            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Exportar", "Elija un formato valido", false);
            }
        }
    }

    @FXML
    private void onActionSubir(ActionEvent event) throws IOException {
    
        URL url = getClass().getResource(Tools.FX_FILE_IMPORTARINVENTARIO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxImportarInventarioController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoader(parent, "Importar inventario", window.getScene().getWindow());
        stage.setResizable(true);        
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
        
    }

    @FXML
    private void onActionSearch(ActionEvent event) {

    }

    void setContent(AnchorPane content) {
        this.content = content;
    }

}
