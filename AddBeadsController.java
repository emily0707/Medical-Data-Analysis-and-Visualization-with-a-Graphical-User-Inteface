/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author feiping
 */
//controller for beads editing pop-up page.
public class AddBeadsController implements Initializable {
    @FXML
    private Button addBeadsBtn;
    @FXML
    private Button deleteAnalyteFromExp;
    @FXML
    private AnchorPane addBeadsPage;
    @FXML
    TableView<bead> beadsListTable;

    @FXML
    private TableColumn<bead, String> beadClassCol;
    @FXML
    private TableColumn<bead, String> analyteCol;
    @FXML
    private TableView<bead> experimentBeadsTable;
                    ObservableList<bead> experimentBeads = FXCollections.observableArrayList(
			);
    @FXML
    private TableColumn<bead, String> experimentBeadClassCol;
    @FXML
    private TableColumn<bead, String> experimentAnalyteCol;
    

     
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<bead> beadsfromXML = FXCollections.observableArrayList();
        String filePath = "C:\\Appling\\CS Master\\Capstone\\AnalysiswithM\\110614_SEE3.xml";
        StAXParser parser = new StAXParser();
        beadsfromXML =  parser.getBeads(filePath);
   
 
        // set beads data into bead class List table. 
        beadClassCol.setCellValueFactory(new PropertyValueFactory<bead,String>("RegionNumber"));
        analyteCol.setCellValueFactory(new PropertyValueFactory<bead,String>("Analyte"));
        beadsListTable.setItems(beadsfromXML);
    
        // set experiement bead data into experiement bead table
        experimentBeadClassCol.setCellValueFactory(new PropertyValueFactory<bead,String>("RegionNumber"));
        experimentAnalyteCol.setCellValueFactory(new PropertyValueFactory<bead,String>("Analyte"));
        experimentBeadsTable.setItems(experimentBeads);
    

    }    

    @FXML
    private void addBeadAction(ActionEvent event) {
    //get user slected data and add it into the experiement beads so that it shows on the add bead page 
    bead beadSelected = beadsListTable.getSelectionModel().getSelectedItem();
    experimentBeads.add(beadSelected);
    }

    @FXML
    private void deleteBeadAction(ActionEvent event) {
    }
    
}
