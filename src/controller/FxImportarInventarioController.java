package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import model.ArticuloTB;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class FxImportarInventarioController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblLoad;
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
    private TableColumn<ArticuloTB, Double> tcPrecio;
    @FXML
    private TableColumn<ArticuloTB, Double> tcExistencias;

    private ObservableList<ArticuloTB> listImportada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getIdArticulo()));
        tcClave.setCellValueFactory(cellData -> cellData.getValue().getClave());
        tcArticulo.setCellValueFactory(cellData -> cellData.getValue().getNombre());
        tcLote.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().isLote() ? "SI" : "NO"
        ));
        tcCaducidad.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getFechaRegistro().get().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))));
        tcPrecio.setCellValueFactory(cellData -> cellData.getValue().getPrecioVenta().asObject());
        tcExistencias.setCellValueFactory(cellData -> cellData.getValue().getCantidad().asObject());
        listImportada = FXCollections.observableArrayList();
        tvList.setItems(listImportada);
    }

    @FXML
    private void onActionIniciar(ActionEvent event) {

    }

    @FXML
    private void onActionImportar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Inventario inicial");
        fileChooser.setInitialFileName("Inventario Inicial");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Libro de Excel (*.xlsx)", "*.xlsx"),
                new FileChooser.ExtensionFilter("Libro de Excel(1997-2003) (*.xls)", "*.xls")
        );
        File file = fileChooser.showOpenDialog(window.getScene().getWindow());
        if (file != null) {

            file = new File(file.getAbsolutePath());
            if (file.getName().endsWith("xls") || file.getName().endsWith("xlsx")) {

                try {
                    Workbook workbook = WorkbookFactory.create(file);
                    Sheet sheet = workbook.getSheetAt(0);
                    Iterator<Row> iterator = sheet.rowIterator();
                    int indexRow = -1;
                    while (iterator.hasNext()) {
                        indexRow++;
                        Row row = iterator.next();
                        Cell cell1 = row.getCell(0);
                        Cell cell2 = row.getCell(1);
                        Cell cell3 = row.getCell(2);
                        Cell cell4 = row.getCell(3);
                        Cell cell5 = row.getCell(4);
                        Cell cell6 = row.getCell(5);
                        Cell cell7 = row.getCell(6);
                        if (indexRow > 0) {
                            try {
                                ArticuloTB articuloTB = new ArticuloTB();
                                articuloTB.setIdArticulo(cell1.getStringCellValue());
                                articuloTB.setClave(cell2.getStringCellValue());
                                articuloTB.setNombre(cell3.getStringCellValue());
                                articuloTB.setLote(cell4.getStringCellValue().equalsIgnoreCase("SI"));
                                articuloTB.setFechaRegistro(new java.sql.Date(cell5.getDateCellValue().getTime()).toLocalDate());
                                articuloTB.setPrecioVenta(cell6.getNumericCellValue());
                                articuloTB.setCantidad(cell7.getNumericCellValue());
                                listImportada.add(articuloTB);
                            } catch (Exception ex) {
                                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Importar", "El formato de las celdas no son correctas.", false);
                                break;
                            }
                        }
                    }
                } catch (IOException | EncryptedDocumentException | InvalidFormatException ex) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Importar", "Error al importar el archivo, intente de nuevo.", false);
                }
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Importar", "Elija un formato valido.", false);
            }
        }
    }

}
