/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import com.jfoenix.controls.JFXButton;
import java.awt.Color;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;



/**
 *
 * @author feiping
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private JFXButton selectFiles;
    @FXML
    private JFXButton uploadFiles;
    @FXML
    private ListView filesList;
    @FXML
    private JFXButton cancelBead;
    @FXML
    private TableColumn<beadTableData, Integer> beadCol;
    @FXML
    private TableColumn<beadTableData, String> analyteCol;
    @FXML
    private TextField beadInput;
    @FXML
    private TextField analyteInput;
    @FXML
    private JFXButton addBead;
    @FXML
    private TableView<beadTableData> beadTable;
            ObservableList<beadTableData> beads = FXCollections.observableArrayList(
			new beadTableData(1, "TCR") ,
			new beadTableData(5, "CD3") ,
			new beadTableData(13, "LAT"),
			new beadTableData(23, "PLC")
			);
    @FXML
    private TextField cell2;
    @FXML
    private TextField cell3;
    @FXML
    private TextField cell4;
    @FXML
    private TextField cell5;
    @FXML
    private TextField cell6;
    @FXML
    private TextField seven;
    @FXML
    private TextField eight;
    @FXML
    private TextField nine;
    @FXML
    private TextField ten;
    @FXML
    private TextField cell1;
    @FXML
    private TextField numSampleInput;
    @FXML
    private TextField numReplicaInput;
    @FXML
    private TextField numProbeInput;
    @FXML
    private JFXButton checkLayout;
    @FXML
    private JFXButton analyze;
    @FXML
    private Tab tab1;
    @FXML
    private GridPane layoutGrid;
    
    int numberOfSamples;
    int numberOfReps;
    int numberOfProbes = 0;   
    List<TextField> cellsList = new ArrayList<>();
    //@FXML
    //List<Color> colorList = new ArrayList<>();
    List<String> colorList = new ArrayList<>();
    @FXML
    private GridPane layoutGrid1;
    @FXML
    private TextField cell11;
    @FXML
    private TextField cell21;
    @FXML
    private TextField cell31;
    @FXML
    private TextField cell41;
    @FXML
    private TextField cell51;
    @FXML
    private TextField cell61;
    @FXML
    private TextField seven1;
    @FXML
    private TextField eight1;
    @FXML
    private TextField nine1;
    @FXML
    private TextField ten1;
    @FXML
    private TextField sampleNamesInput;
    
    List<String> colors = new ArrayList<>(Arrays.asList("-fx-background-color:blue;", "-fx-background-color:yellow;", "-fx-background-color:red;", "-fx-background-color:pink;", 
            "-fx-background-color:green;", "-fx-background-color:orange;", "-fx-background-color:golden;", "-fx-background-color:purple;", "-fx-background-color:AQUA;",
            "-fx-background-color:BLUEVIOLET;", "-fx-background-color:#F5F5DC;", "-fx-background-color:BISQUE;", "-fx-background-color:brown;", "-fx-background-color:CORAL;"));
    @FXML
    private TextField probeAnalyteInput;
    @FXML
    private TextField probeInput;
    @FXML
    private JFXButton addProbe;
    @FXML
    private JFXButton cancelProbe;
    
    @FXML
    private TableView<probeTableData> probeTable;
    public ObservableList<probeTableData> probeList;
    @FXML
    private TableColumn<probeTableData, Integer> probeCol;
    @FXML
    private TableColumn<probeTableData, String> probeAnalyteCol;
    @FXML
    private Tab tab2;
    @FXML
    private TabPane experimentsTabs;
    @FXML
    private Menu menuInput;
    @FXML
    private Menu meunMedianValues;
    @FXML
    private AnchorPane exp2AP;

        
        @Override
    public void initialize(URL url, ResourceBundle rb) {
        // bead table on input tab
        beadCol.setCellValueFactory(new PropertyValueFactory<beadTableData, Integer>("bead"));
	analyteCol.setCellValueFactory(new PropertyValueFactory<beadTableData,String>("analyte"));
	beadTable.setItems(beads);
      
        //probe table on input tab. 
        probeCol.setCellValueFactory(new PropertyValueFactory<probeTableData, Integer>("probe"));
	probeAnalyteCol.setCellValueFactory(new PropertyValueFactory<probeTableData,String>("analyte"));
        probeTable.setItems(probeList);
    }    

    @FXML
    private void selectFilesEvent(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("XML Files", "*.xml"));
        
        List<File> selectedFiles = fc.showOpenMultipleDialog(null);
        
        
        if(selectedFiles!= null)
        {
            for(int i =0; i<selectedFiles.size(); i++)
            {
                filesList.getItems().add(selectedFiles.get(i).getName());
            }           
        }
        else
        {
            System.out.println("File is not valid");
        }
        
    }

    @FXML
    private void uploadFilesEvent(ActionEvent event) {
    }

    @FXML
    private void addBeadEvent(ActionEvent event) {
        if(beadInput.getText()==null || analyteInput.getText()==null)
        {
            System.out.println("invalid Input!");
        }
        beads.add(new beadTableData(
            Integer.parseInt(beadInput.getText()),analyteInput.getText()));
            beadInput.clear();
            analyteInput.clear();
    }

    @FXML
    private void cancelBeadEvent(ActionEvent event) {
                    beadInput.clear();
            analyteInput.clear();
    }

    @FXML
    private void checkLayoutEvent(ActionEvent event) {
     
        numberOfSamples = Integer.parseInt(numSampleInput.getText());
        numberOfReps = Integer.parseInt(numReplicaInput.getText());
        numberOfProbes =  Integer.parseInt(numProbeInput.getText());  
        String names = sampleNamesInput.getText();
        String[] nameList = names.split(",");
        int cellsToFill = numberOfSamples * numberOfReps * numberOfProbes;
        cellsList = getCells(layoutGrid);
        //cellsList = getCells(experimentsTabs.getSelectionModel().getSelectedItem().getProperties().equals(AnchorPane));
        int cellsCount =0;

        while(cellsCount<cellsToFill)
        {
            for(int i = 0; i<numberOfProbes; i++)
            {
                String color1 = colors.get(i%colors.size());  
                for(int j = 0; j<numberOfSamples * numberOfReps; j++)
                {
                    cellsList.get(cellsCount).setStyle(color1);  // set color
                    cellsList.get(cellsCount).setText(nameList[cellsCount%(nameList.length)] + "." + ((i)%numberOfReps+1)); //set text
                    cellsCount++;
                }
            }
        }
        
        //set probes values to probe table
        probeList = generateProbes(numberOfProbes);
        probeTable.setItems(probeList);
        probeTable.setRowFactory(tv-> new TableRow<probeTableData>()
        {
            @Override
            protected void updateItem(probeTableData item, boolean empty)
            {
                if (item == null)return;
                Integer n = item.getProbe();
                int index = Integer.parseInt(n.toString());
                setStyle(colors.get(index-1));
            }
        }
        );

    }


    
    public ObservableList<probeTableData> generateProbes(int n) {
        ObservableList<probeTableData> allData = FXCollections.observableArrayList();
        for (int i = 0; i < n; i++) {
            allData.add(new probeTableData(i+1, "TCR"));
        }
        return allData;
    }
    
    
    
    
    public List<TextField> getCells(GridPane gridPane)
    {
        List<TextField> cells = new ArrayList<>();
            for(Node currentNode : gridPane.getChildren())
            {
                if (currentNode instanceof TextField)
                {
                    cells.add((TextField)currentNode);
                }
            }
        return cells;
    }
            
    public Node getNodeByRowColumnIndex (final int column, final int row, GridPane gridPane) {
     System.out.println("enter getNode function" );    
    Node result = null;
    ObservableList<Node> childrens = gridPane.getChildren();

    for (Node node : childrens) {
        if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
            result = node;
            break;
        }
    }
     System.out.println("leave getNode function" );    
    return result;
}
    

    @FXML
    private void analyzeEvent(ActionEvent event) {
    }

    @FXML
    private void addProbeEvent(ActionEvent event) {
    if(probeInput.getText()==null || probeAnalyteInput.getText()==null)
        {
            System.out.println("invalid Input!");
        }
        probeList.add(new probeTableData(
            Integer.parseInt(probeInput.getText()),probeAnalyteInput.getText()));
            probeInput.clear();
            probeAnalyteInput.clear();
    }



    @FXML
    private void cancelProbeEvent(ActionEvent event) {
        probeInput.clear();
        probeAnalyteInput.clear();
    }

    @FXML
    private void changeExperimentEvent(Event event) {
        numSampleInput.clear();
        numReplicaInput.clear();
        sampleNamesInput.clear();
        numProbeInput.clear();
        probeTable.getItems().clear();
        //probeTable.setVisible(false);
    
    }



}

