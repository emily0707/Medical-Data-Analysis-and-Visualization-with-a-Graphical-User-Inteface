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
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Menu inputData;
    @FXML
    private Menu tab2;
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
    int numberOfProbes;   
    List<TextField> cellsList = new ArrayList<>();
    //@FXML
    //List<Color> colorList = new ArrayList<>();
    List<String> colorList = new ArrayList<>();
    @FXML
    private Tab tab11;
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
    
        @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        beadCol.setCellValueFactory(new PropertyValueFactory<beadTableData, Integer>("bead"));
	analyteCol.setCellValueFactory(new PropertyValueFactory<beadTableData,String>("analyte"));
	beadTable.setItems(beads);
       

        
        //cell1. setStyle("-fx-background-color:#38ee00;");
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
        int cellsToFill = numberOfSamples * numberOfReps * numberOfProbes;
        cellsList = getCells(layoutGrid);
        int cellsCount =0;

        
        // List<String> x = new ArrayList<>(Arrays.asList("fx-background-color: white;", "fx-background-color: red;", "fx-background-color: blue;"));

        while(cellsCount<cellsToFill)
        {
            for(int i = 0; i<numberOfProbes; i++)
            {
                for(int j = 0; j<numberOfSamples * numberOfReps; j++)
                {
                   // String color1 = colorList.get(cellsCount);
                   // String color1 = x.get(cellsCount);
                    //String color1 = y[cellsCount];
                    cellsList.get(cellsCount).setStyle("-fx-background-color:red;");   
                    cellsCount++;
                }
            }
        }
    }
    
  /*  public String setColor(int colorCode)
    {
            String a = "-fx-background-color: rgb(";
            String b = Integer.toString(colorCode);
            String c = ",0,0)";
            String color = new StringBuilder().
                    append(a).
                    append(b).
                    append(c).
                    toString();
            return color;
    }*/
    
    public List<String> setColorList()
    {
        String a ="fx-background-color: white;" ;
         String b ="fx-background-color: red;" ;
        String c ="fx-background-color: blue;" ;
        List<String> colors = new ArrayList<>();
        colors.add(a);
        colors.add(b);
        colors.add(c);
        return colors;

    }
    
    
   /* public List<Color> setColorList()
    {
       List<Color> colors = new ArrayList<>();
        Color a = Color.BLUE;
        Color b = Color.RED;
        Color c = Color.GREEN;
        Color d = Color.PINK;
        Color e = Color.BLACK;
        Color f = Color.WHITE;
        Color g = Color.GRAY; 
        Color h = Color.YELLOW;
        //ArrayList<Color> colors = new ArrayList(Arrays.asList(a, b, c,d, e ,f , g, h));
        colors.add(a);
        colors.add(b);
        colors.add(c);
        colors.add(d); 
        colors.add(e);
        colors.add(f);
        colors.add(g);
        colors.add(h); 
           
        return colors;
    }
    */
    
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


}
