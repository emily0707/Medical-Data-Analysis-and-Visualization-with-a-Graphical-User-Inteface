/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import com.jfoenix.controls.JFXButton;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.T;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import static javax.swing.text.html.HTML.Tag.S;
import static jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants.S;
import static jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType.S;



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
    private TableColumn<beadTableData, String> beadCol;
    @FXML
    private TableColumn<beadTableData, String> analyteCol;
    private TextField beadInput;
    private TextField analyteInput;
    @FXML
    private JFXButton addBead;
    @FXML
    public TableView<beadTableData> beadTable;
            ObservableList<beadTableData> beads = FXCollections.observableArrayList(
			new beadTableData("1", "TCR") ,
			new beadTableData("1", "CD3") ,
			new beadTableData("1", "LAT"),
			new beadTableData("1", "PLC")
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
    
    List<GridPane> expLayouts = new ArrayList<>();
    List<experimentData> expDatas = new ArrayList<>();
    List<Tab> expriments = new ArrayList<>();
    @FXML
    private TextField analyteInput1;
    @FXML
    private JFXButton addBead1;
    
    
    
    
        @Override
    public void initialize(URL url, ResourceBundle rb) {
        // bead table on input tab
        beadCol.setCellValueFactory(new PropertyValueFactory<beadTableData, String>("bead"));
	analyteCol.setCellValueFactory(new PropertyValueFactory<beadTableData,String>("analyte"));
	beadTable.setItems(beads);
        //set text filed to columns so that they can be edited. 
        beadCol.setCellFactory(TextFieldTableCell.forTableColumn());
        analyteCol.setCellFactory(TextFieldTableCell.forTableColumn());
        

      
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
    private void addBeadEvent(ActionEvent event) {
        try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addBeads.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));  
                stage.show();
        } catch(Exception e) {
           e.printStackTrace();
          }

    }

    @FXML
    private void cancelBeadEvent(ActionEvent event) {
                    beadInput.clear();
            analyteInput.clear();
    }
    
    private void addGridLayouts()
    {
        expLayouts.add(layoutGrid);
        expLayouts.add(layoutGrid1);

    }

    @FXML
    private void checkLayoutEvent(ActionEvent event) {
       if(expriments.size()==0)
        {
        expLayouts.add(layoutGrid);
        expLayouts.add(layoutGrid1);
        expriments.add(tab1);
        expriments.add(tab2);              
        }

        
        numberOfSamples = Integer.parseInt(numSampleInput.getText());
        numberOfReps = Integer.parseInt(numReplicaInput.getText());
        numberOfProbes =  Integer.parseInt(numProbeInput.getText());  
        String names = sampleNamesInput.getText();
        String[] nameList = names.split(",");
        ObservableList<probeTableData> probeList= generateProbes(numberOfProbes);
        
        //int index = getSeletedTabIndex();
             int index=experimentsTabs.getSelectionModel().getSelectedIndex();
        
        
        expDatas.add(new experimentData(numberOfSamples, numberOfReps,nameList,numberOfProbes,probeList ));
        
        displayLayout(index, expDatas.get(index));
        

    }
    
    
  private void displayLayout(int index, experimentData data) {

      


//fill cells with colors and values base on users' inputs
        cellsList = getCells(expLayouts.get(index));
        int cellsCount =0;
        String[] nameList = data.getNames();

        int cellsToFill = data.getNumOfSamples()*data.getNumOfReplicas()*data.getNumOfProbes();
        while(cellsCount<cellsToFill)
        {
            for(int i = 0; i<numberOfProbes; i++)
            {
                String color1 = colors.get(i%colors.size());  
                for(int j = 0; j<numberOfSamples * numberOfReps; j++)
                {
                    cellsList.get(cellsCount).setStyle(color1);  // set color
                    cellsList.get(cellsCount).setText(nameList[cellsCount%(nameList.length)] + "." + ((j)/numberOfSamples+1)); //set text
                    cellsCount++;
                }
            }
        }
        
        
        //set probes values to probe table
        probeList=data.getProbeList();
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

    
    private int getSeletedTabIndex() {
        for(int i = 0; i<expriments.size();i++)
            if(expriments.get(i).isSelected())
            {
                return i;
            }
        return -1;
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
             int index=experimentsTabs.getSelectionModel().getSelectedIndex();
        expDatas.get(index).updateProbe(new probeTableData(
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
        int index=experimentsTabs.getSelectionModel().getSelectedIndex();
        if(expDatas.size()<(index+1))
           {
           numSampleInput.clear();
           numReplicaInput.clear();
           sampleNamesInput.clear();
           numProbeInput.clear();
           probeTable.getItems().clear();
           }
        else
        {
             //fill textfile
            numSampleInput.setText(String.valueOf(expDatas.get(index).getNumOfSamples()));
            displayLayout(index, expDatas.get(index));
        }   
    }



    @FXML
    private void uploadFilesEvent(ActionEvent event) {
    }



    @FXML
    private void editBeadFileEvent(ActionEvent event) throws IOException {
       // Runtime rt = Runtime.getRuntime();
        //String path = javafxapplication1.class.getProtectionDomain().getCodeSource().getLocation().getPath();
       // String file = "C:\\Users\\feiping\\Documents\\NetBeansProjects\\JavaFXApplication1\\src\\javafxapplication1\\beads.txt";
       // Process p = rt.exec("notepad" + file);
        
        ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "C:\\Users\\feiping\\Documents\\NetBeansProjects\\JavaFXApplication1\\src\\javafxapplication1\\beads.txt");
        pb.start();
        
    }

    @FXML
    // set bead class cloumn editable 
    private void changeBeadEvent(CellEditEvent<beadTableData, String> edittedCell) {
        beadTableData beadSeleted= beadTable.getSelectionModel().getSelectedItem();
        beadSeleted.setBead(edittedCell.getNewValue());
    }

    @FXML
    private void changeBeadAnalyte(CellEditEvent<beadTableData, String> edittedCell) {
        beadTableData beadSeleted= beadTable.getSelectionModel().getSelectedItem();
        beadSeleted.setAnalyte(edittedCell.getNewValue());
    }




}

