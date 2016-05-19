/*
 * Copyright (C) 2016 Amadeus
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package celestial;

import celestial_computator.EarthOrbitalComputator;
import celestial_exception.NoradAlreadyExistsException;
import celestial_object.EarthOrbitalObject;
import celestial_renderer.EarthOrbitalRenderer;
import celestial_database.DatabaseAccessor;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import celestial_parser.CelestialParser;
import java.util.List;
import sgp4v.SatElsetException;

public class FXMLLocalController implements Initializable {
    @FXML
    private Label labelDate;
    @FXML
    private Slider sliderDate;
    @FXML
    private Canvas canvas;
    @FXML
    private GraphicsContext gc;
    @FXML
    private AnchorPane ap;
    @FXML
    private TextField noradIDText;
    @FXML
    private TextField nameText;
    @FXML
    private Label xLabel;
    @FXML
    private Label yLabel;
    @FXML
    private Label zLabel;
    @FXML
    private Label vLabel;
    @FXML
    private Label altLabel;
    
    /* ObjectsTableData and it's columns */
    @FXML
    private TableView<ObjectsTableData> dbTable;
    @FXML
    private TableColumn<ObjectsTableData, Integer> dbNoradIDCol;
    @FXML
    private TableColumn<ObjectsTableData, String> dbNameCol;
    @FXML
    private TableColumn<ObjectsTableData, String> dbObjTypeCol;
    @FXML
    private TableColumn<ObjectsTableData, String> dbOrbTypeCol;
    @FXML
    private TableColumn<ObjectsTableData, String> dbCountryCol;
    /* End of ObjectsTableData and it's columns */
    
    /* ObjectsToRenderTableData and it's columns */
    @FXML
    private TableView<ObjectsToRenderTableData> otrTable;
    @FXML
    private TableColumn<ObjectsToRenderTableData, Integer> otrNoradIDCol;
    @FXML
    private TableColumn<ObjectsToRenderTableData, Integer> otrEpochYearCol;
    @FXML
    private TableColumn<ObjectsToRenderTableData, String> otrEpochDayCol;
    /* End of ObjectsToRenderTableData and it's columns */
    
    /* Wrapped into TableView, see JavaFX documentation */
    private ObservableList<ObjectsTableData> tbl_data;
    private ObservableList<ObjectsToRenderTableData> otr_tbl_data;
    
    private Image img;
    /* Only one, holds render methods and EarthOrbitalComputator list */
    private EarthOrbitalRenderer eor;
    /* Only one, to parse text files */
    private CelestialParser cp;
    /* List of EarthOrbitalObjects (temporal data holder) */
    private static List<EarthOrbitalObject> eoos;
    /* Toggle-adjusted value in days */
    private double dayFix;
    /* Adjustment in seconds to system time (acuumulate it using slider) */
    private int secAdjust;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open TLE File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        cp = new CelestialParser(selectedFile);
        cp.readTLEFile();

        DatabaseAccessor.loadAll();
    }
    @FXML
    private void handleButtonAction2(ActionEvent event) {
        String noradIDStr = noradIDText.getText();
        /* ^\d*$ - regular expression for zero or more digits */
        if (!noradIDStr.matches("^\\d*$")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Something went wrong...");
            alert.setContentText("Use numbers for NoradID.");
            alert.showAndWait();
        } else {
            dbTable.getItems().clear();
            DatabaseAccessor.getAll(dbTable, tbl_data, noradIDText.getText(), nameText.getText());
        }
    }
    @FXML
    private void handleButtonAction3(ActionEvent event) {
        try {
            ObjectsTableData dbTableRow = dbTable.getSelectionModel().getSelectedItem();
            eor.addObject(dbTableRow.getRNoradID(), dayFix);
            ArrayList<EarthOrbitalComputator> eocl = (ArrayList) eor.getEocl();
            EarthOrbitalObject eoo = eocl.get(eocl.size()-1).getEOO();
            otr_tbl_data.add(new ObjectsToRenderTableData(
                    eoo.getNoradID(),
                    eoo.getEpochYear(),
                    Double.toString(eoo.getEpochDay())
            ));
        } catch (NullPointerException ex) {
            System.out.println("Choose field first!");
        } catch (NoradAlreadyExistsException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Something went wrong...");
            alert.setContentText("NoradID is already provided!");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleButtonAction4(ActionEvent event) {
        int selectedRowIndex = otrTable.getSelectionModel().getSelectedIndex();
        EarthOrbitalComputator eoc = eor.render(selectedRowIndex, dayFix);
        if (eoc != null) {
            xLabel.setText(String.format("%.0f km", eoc.getX()));
            yLabel.setText(String.format("%.0f km", eoc.getY()));
            zLabel.setText(String.format("%.0f km", eoc.getZ()));
            vLabel.setText(String.format("%.0f km/sec", eoc.getV()));
            altLabel.setText(String.format("%.0f km", eoc.getAlt()));
        }
    }
    @FXML
    private void handleButtonAction5(ActionEvent event)
            throws SatElsetException, MalformedURLException, IOException, ClassNotFoundException {
        System.out.println("Trying to connect to servlet...");
        ObjectsToRenderTableData otrTableRow = otrTable.getSelectionModel().getSelectedItem();
        eor.delObject(otrTableRow.getRNoradID());
        otr_tbl_data.remove(otrTableRow);
        otrTable.getSelectionModel().clearSelection();
    }
    @FXML
    private void handleSliderAction() {
        dayFix += sliderDate.getValue();
        sliderDate.setValue(0);
        secAdjust = (int) ((24*60*60)*dayFix);
    }
    
    public static void addEOO(EarthOrbitalObject eoo) {
        eoos.add(eoo);
    }
    public static void clearEOOS() {
        eoos.clear();
    }
    public static List<EarthOrbitalObject> getEOOS() {
        return eoos;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /* Objects TableView init */
        tbl_data = FXCollections.observableArrayList();
        dbNoradIDCol.setCellValueFactory(new PropertyValueFactory<>("rNoradID"));
        dbNameCol.setCellValueFactory(new PropertyValueFactory<>("rName"));
        dbObjTypeCol.setCellValueFactory(new PropertyValueFactory<>("rObjType"));
        dbOrbTypeCol.setCellValueFactory(new PropertyValueFactory<>("rOrbType"));
        dbCountryCol.setCellValueFactory(new PropertyValueFactory<>("rCountry"));
        dbTable.setItems(tbl_data);
        
        /* ObjectsToRender TableView init */
        otr_tbl_data = FXCollections.observableArrayList();
        otrNoradIDCol.setCellValueFactory(new PropertyValueFactory<>("rNoradID"));
        otrEpochYearCol.setCellValueFactory(new PropertyValueFactory<>("rEpochYear"));
        otrEpochDayCol.setCellValueFactory(new PropertyValueFactory<>("rEpochDay"));
        otrTable.setItems(otr_tbl_data);
        
        /* Timer init to refresh every 1 second */
        Timeline celestialTimer = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
            DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
            Date dateobj = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateobj);
            // Here time is adjusted using secAdjust
            calendar.add(Calendar.SECOND, secAdjust);
            labelDate.setText(df.format(calendar.getTime()));
        }));
        celestialTimer.setCycleCount(Timeline.INDEFINITE);
        celestialTimer.play();
        
        /* Canvas init */
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        img = new Image("file:earth.jpg", 600, 300, true, true);
        gc.drawImage(img, 0, 0);
        
        /* EarthOrbitalRenderer init */
        eor = new EarthOrbitalRenderer(canvas, img);
        
        /* dayFix init*/
        dayFix = 0;
        /* secAdjust init */
        secAdjust = 0;
        
        /* EarthOrbitalObjects init */
        eoos = new ArrayList<>();
        
        /* DatabaseAccessor constructor init (this one is static) */
        DatabaseAccessor dba_construct = new DatabaseAccessor();
    }
    
}
