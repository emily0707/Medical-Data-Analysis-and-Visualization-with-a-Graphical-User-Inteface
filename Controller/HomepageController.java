/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Util.StAXParser;
import Model.UserInputForBeadPlate;
import Model.bead;
import Model.probeTableData;
import Model.ModelForProbeTabe;
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
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ChoiceBox;
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
import javafx.scene.text.Text;
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
public class HomepageController implements Initializable {
    @FXML
    private JFXButton selectFiles;
    @FXML
    private JFXButton uploadFiles;
    @FXML
    private ListView filesList;
    private List<String> fileNames = new ArrayList<String>(); // store xml file names as string 
    
   //bead class table area 
    @FXML
    public TableView<bead> beadTable;  
    @FXML
    private TableColumn<bead, String> beadCol; 
    @FXML
    private TableColumn<bead, String> analyteCol;
    private TextField beadInput; // not needed
    private TextField analyteInput; // not needed
    private  ObservableList<bead> beads;   //beads read from xml files
    
    //right-top area, display experiement info
    @FXML
    private ChoiceBox<Integer> DropdownExperimentsChoiceBox;
    @FXML
    private Text totalNumberOfExperiments;
    @FXML
    private Text currentExperiementNumber;
    @FXML
    private Text XMLfilesNames;
    private int curExperiment;
 
    // user input area for plate1  and plate 2. 
    @FXML
    private TextField numSampleInput;
    @FXML
    private TextField numReplicaInput;
    @FXML
    private TextField numProbeInput;
    @FXML
    private TextField sampleNamesInput;
    @FXML
    private TextField numSampleInput2;
    @FXML
    private TextField numReplicaInput2;
    @FXML
    private TextField numProbeInput2;
    @FXML
    private TextField sampleNamesInput2;
    @FXML
    private JFXButton checkLayout;
    
    //List<UserInputForBeadPlate> userInputsForBeadPlate = new ArrayList<>(); // list for user input data
    HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap= new HashMap<>();

   
// bead plates tab area, use for display proble layout 
    @FXML
    private TabPane beadPlatesTab;
    @FXML
    private Tab beadPlate1Tab;
    @FXML
    private Tab beadPlate2Tab;
    @FXML
    private GridPane beadPlate1Layout;
    @FXML
    private GridPane beadPlate2Layout;
    List<GridPane> beadPlateLayouts = new ArrayList<>(); // list for layout gridPane     
    List<Tab> beadPlates = new ArrayList<>(); // list for bead plate tabs 
    List<TextField> layoutCellsList = new ArrayList<>(); // List to hold textfield in each gridpane cells. 
    //colors list for set diffrent colors to each probe
    List<String> colors = new ArrayList<>(Arrays.asList("-fx-background-color:blue;", "-fx-background-color:yellow;", "-fx-background-color:red;", "-fx-background-color:pink;", 
            "-fx-background-color:green;", "-fx-background-color:orange;", "-fx-background-color:golden;", "-fx-background-color:purple;", "-fx-background-color:AQUA;",
            "-fx-background-color:BLUEVIOLET;", "-fx-background-color:#F5F5DC;", "-fx-background-color:BISQUE;", "-fx-background-color:brown;", "-fx-background-color:CORAL;"));
    
//probe table area    
    @FXML
    private TableView<probeTableData> probeTable1;
    @FXML
    private TableColumn<probeTableData,Integer> ProbeCol1; 
    @FXML
    private TableColumn<probeTableData,String> BeadPlate1ProbeCol;
    @FXML
    private TableView<probeTableData> probeTable2;
    @FXML
    private TableColumn<probeTableData,Integer> ProbeCol2;
    @FXML
    private TableColumn<probeTableData,String> BeadPlate2ProbeCol;  
    @FXML
    private JFXButton editProbesInBeadPlate1;
    @FXML
    private JFXButton editProbesInBeadPlate2;        

// analyze   
    @FXML
    private JFXButton analyze;

//Below are elements used for displaying probe layout in bead plates.     

    @FXML
    private Menu menuInput;
    @FXML
    private Menu meunMedianValues;

        
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {      
        String filePath = "C:\\Appling\\CS Master\\Capstone\\AnalysiswithM\\110614_SEE3.xml";
        StAXParser parser = new StAXParser();
        beads =  parser.getBeads(filePath);

        // set beads data into bead class List table. 
        beadCol.setCellValueFactory(new PropertyValueFactory<bead,String>("RegionNumber"));
        analyteCol.setCellValueFactory(new PropertyValueFactory<bead,String>("Analyte"));
        beadTable.setItems(beads);
        
        //set beads data into bead plate 1 probe List table. 
        ProbeCol1.setCellValueFactory(new PropertyValueFactory<probeTableData,Integer>("probeCount"));
        BeadPlate1ProbeCol.setCellValueFactory(new PropertyValueFactory<probeTableData,String>("probeForPlate"));
       // probeTable1.setItems(testdata);
         
         //set beads data into bead plate 2 probe List table. 
        ProbeCol2.setCellValueFactory(new PropertyValueFactory<probeTableData,Integer>("probeCount"));
        BeadPlate2ProbeCol.setCellValueFactory(new PropertyValueFactory<probeTableData,String>("probeForPlate"));   
        //probeTable2.setItems(testdata);

    }   

    // for upload file button. click the button to display files lists in a listview 
    @FXML
    private void selectFilesEvent(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
        
        List<File> selectedFiles = fc.showOpenMultipleDialog(null);

        if(selectedFiles!= null)
        {
            for(int i =0; i<selectedFiles.size(); i++)
            {
                String filename = selectedFiles.get(i).getName();
                filesList.getItems().add(filename); // display on the listview fileList. 
                fileNames.add(filename);
            }           
        }
        else
        {
            System.out.println("File is not valid");
        }
        
        updateExperimentsInfo();
    }

    //update left upper experiments info after user upload xml files
    private void updateExperimentsInfo() {
        //display # of experiements
        String count = Integer.toString(fileNames.size() /2);
        totalNumberOfExperiments.setText(count);
        
        //set value to experiements dropbox base on # of experiments.
        List<Integer> counts = new ArrayList<Integer>();
        for(int i = 0; i <fileNames.size()/2; i++)
        {
            counts.add(i+1);
        }
        ObservableList<Integer> experiments = FXCollections.observableList(counts);
        DropdownExperimentsChoiceBox.setItems(experiments);
        
        //add experiments to the context and create beadlist for each bead plate
        for(int i =0; i<experiments.size();i++)
        {
            ModelForProbeTabe.getInstance().addExperiement(Integer.valueOf(count));
        }
       
        //after user clieck expriements from the choice box, Change experiment info
        DropdownExperimentsChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Integer newValue = DropdownExperimentsChoiceBox.getSelectionModel().getSelectedItem();
                currentExperiementNumber.setText(Integer.toString(newValue));  
                XMLfilesNames.setText(fileNames.get(newValue*2 -2) + "," +  fileNames.get(newValue*2 -1));
                ModelForProbeTabe.getInstance().setCurrentExperiment( newValue -1 ); // set current experiment in model for proble table
            }
        });
    }
    
        // event for user select different experiement,
    //display user input data and bead layout for that experiement base on the data stored for the experiment. 
    // if no data, leave blank user input and empty bead layout. 
    @FXML
    private void changeExperimentEvent(Event event) {
        //after user clieck expriements from the choice box, Change experiment info       
        DropdownExperimentsChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Integer newValue = DropdownExperimentsChoiceBox.getSelectionModel().getSelectedItem();
                curExperiment = (int)newValue -1;
                currentExperiementNumber.setText(Integer.toString(newValue));  
                XMLfilesNames.setText(fileNames.get(newValue*2 -2) + "," +  fileNames.get(newValue*2 -1));
                ModelForProbeTabe.getInstance().setCurrentExperiment( newValue -1 ); // set current experiment in model for proble table
            }
        });
        
        //if the experiement already has data, show data 
        if(userInputsForBeadPlateMap.get(curExperiment)!=null)
        {
            displayUserInput(userInputsForBeadPlateMap.get(curExperiment));
            displayBeadsPlateLayout(curExperiment);
        }        
        //has no data submited, display empty user input area and empty beads plate table. 
        else 
        {
            clearUserInput();
            clearLayout();
            //clearBeadsPlateLayout();

        }
        
        /*
        int index = beadPlatesTab.getSelectionModel().getSelectedIndex();
        if(userInputsForBeadPlate .size()<(index+1))
           {
           numSampleInput.clear();
           numReplicaInput.clear();
           sampleNamesInput.clear();
           numProbeInput.clear();
           probeTable1.getItems().clear();
           probeTable2.getItems().clear();
           }
        else
        {
             //fill textfile
            numSampleInput.setText(String.valueOf(userInputsForBeadPlate .get(index).getNumOfSamples()));
            displayLayout(index, userInputsForBeadPlate .get(index));
        }   
*/
    }
    
    //helper fucntionf for chaning experiment
    // when no data at cur experiement, clear userinput area.
    private void clearUserInput()
    {
        //clear bead plate1 
        // consider use getChildren.clear() 
        numSampleInput.clear();
        numReplicaInput.clear();
        sampleNamesInput.clear();
        numProbeInput.clear();
        sampleNamesInput.clear();
        
        //clear bead plate2
        numSampleInput2.clear();
        numReplicaInput2.clear();
        sampleNamesInput2.clear();
        numProbeInput2.clear();
        sampleNamesInput2.clear();
    }
    
    //helper function for changing experiment
    //when no user inputs for this experiment, clear bead plate layout.
    private void clearBeadsPlateLayout()
    {
        String[] emptyNameList = {};
        List<String> emptyProbeList = new ArrayList<>();
        UserInputForBeadPlate empty = new UserInputForBeadPlate(0,0,"", emptyNameList,0,emptyProbeList);
        for(int i = 0; i <beadPlateLayouts.size(); i++)
        {
            displayLayout(i,empty);
        }
    }
    
       private void clearLayout()
   {
       for(int i = 0; i < beadPlateLayouts.size();i++)
       {
           layoutCellsList = getCells(beadPlateLayouts .get(i));
           for(int j =0; j <layoutCellsList.size(); j++)
           {
               layoutCellsList.get(j).clear();
               layoutCellsList.get(j).setStyle("-fx-background-color:white;");
           }
       }
   }
    
    // helper function for change experiment
    // show data in the user input area base on previous user input stored in the map.
    private void displayUserInput(List<UserInputForBeadPlate> data) {
        //update user input for bead plate 1
        numSampleInput.setText(Integer.toString(data.get(0).getNumOfSamples()));
        numReplicaInput.setText(Integer.toString(data.get(0).getNumOfSamples()));
        numProbeInput.setText(Integer.toString(data.get(0).getNumOfProbes()));
        sampleNamesInput.setText(data.get(0).getNameInput());
        
        //update user Input for bead Plate2
        numSampleInput2.setText(Integer.toString(data.get(1).getNumOfSamples()));
        numReplicaInput2.setText(Integer.toString(data.get(1).getNumOfSamples()));
        numProbeInput2.setText(Integer.toString(data.get(1).getNumOfProbes()));
        sampleNamesInput2.setText(data.get(1).getNameInput());
        
    }

   
    //open a pop-up page to edit beads
    private void addBeadEvent(ActionEvent event) {
        try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = fxmlLoader.load(getClass().getResource("addBeads.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));  
                stage.showAndWait();

        stage.show();
        } catch(Exception e) {
           e.printStackTrace();
          }
    }

    private void cancelBeadEvent(ActionEvent event) {
                    beadInput.clear();
            analyteInput.clear();
    }
    
    


    //for display layout button. click to diaplay bead plate layout on each bead plate tab. 
    @FXML
    private void checkLayoutEvent(ActionEvent event) {
        
       if(beadPlates.size()==0)
        {
        beadPlateLayouts.add(beadPlate1Layout);
        beadPlateLayouts.add(beadPlate2Layout);
        beadPlates.add(beadPlate1Tab);
        beadPlates.add(beadPlate2Tab);              
        }
       //clearLayout();
        List<UserInputForBeadPlate> userInputsForBeadPlate = new ArrayList<>();
       //gather user inputs for bead plate1 form each text fileds 
        int numberOfSamples = Integer.parseInt(numSampleInput.getText());
        int numberOfReps = Integer.parseInt(numReplicaInput.getText());
        int numberOfProbes =  Integer.parseInt(numProbeInput.getText());  
        String names = sampleNamesInput.getText();
        String[] nameList = names.split(",");
       // ObservableList<probeTableData> probeList= generateProbes(numberOfProbes); 
        List<String> probeList = new ArrayList<>();
        //combine user inputs and probeList into a UserInputForBeadPlate object
        userInputsForBeadPlate.add(new UserInputForBeadPlate(numberOfSamples, numberOfReps,names, nameList,
                numberOfProbes,probeList ));

        //gather user inputs for bead plate2 for each text fileds 
        int numberOfSamples2 = Integer.parseInt(numSampleInput2.getText());
        int numberOfReps2 = Integer.parseInt(numReplicaInput2.getText());
        int numberOfProbes2 =  Integer.parseInt(numProbeInput2.getText());  
        String names2 = sampleNamesInput2.getText();
        String[] nameList2 = names.split(",");
        List<String> probeList2 = new ArrayList<>();
               
        //combine user inputs and probeList into a UserInputForBeadPlate object
        userInputsForBeadPlate.add(new UserInputForBeadPlate(numberOfSamples2, numberOfReps2,names2, nameList2,
                numberOfProbes2,probeList2 ));
        //find current experiment, put the experiement into the map associated with curexperiment number. 
        userInputsForBeadPlateMap.put(curExperiment, userInputsForBeadPlate);
       // userInputsForBeadPlate.clear();
                
        curExperiment = Integer.parseInt(currentExperiementNumber.getText()) - 1;
        displayBeadsPlateLayout(curExperiment);
        //create probe table datas for popolating data in probe table. 
        //displayProbeTable();
    }
    
    //get data of current experiement and display the bead plate layout. 
    private void displayBeadsPlateLayout(int curExperiment)
    {
        if(userInputsForBeadPlateMap.get(curExperiment) == null) return;
        for(int i =0; i <userInputsForBeadPlateMap.get(curExperiment).size();i++ )
        {
            displayLayout(i, userInputsForBeadPlateMap.get(curExperiment).get(i));
        }
        
    }
/*    
private void displayProbeTable() {
    List<Integer> counts = Arrays.asList(1, 2, 3, 4, 5);
    List<String> probeList1 = Arrays.asList("One", "Two", "Three", "Four", "Five");
    List<String> probeList2 = Arrays.asList("One", "Two", "Three", "Four", "Five");
    for (int i = 0; i < Math.max(probeList1.size(), probeList2.size()); i++) {
    probeTable.getItems().add(new probeTableData(counts.get(i), probeList1.get(i),probeList2.get(i)));
    } 
    
}*/



    //display layout in the gridpane
    // index: index of the bead plate 
    //data: user input data for that bead plate   
  private void displayLayout(int index, UserInputForBeadPlate data) {
        //fill cells with colors and values base on users' inputs
        layoutCellsList = getCells(beadPlateLayouts .get(index));
        int cellsCount =0;
        String[] nameList = data.getNames(); 
        int cellsToFill = data.getNumOfSamples()*data.getNumOfReplicas()*data.getNumOfProbes(); // totoal cells need to fill in in the grid pane
        int numberOfProbes=data.getNumOfProbes();
        int numberOfSamples = data.getNumOfSamples();
        int numberOfReps = data.getNumOfReplicas();
        while(cellsCount<cellsToFill)
        {
            for(int i = 0; i<numberOfProbes; i++)
            {
                String color = colors.get(i%colors.size());  // choose different color to each probe
                for(int j = 0; j<numberOfSamples * numberOfReps; j++)
                {
                    layoutCellsList.get(cellsCount).setStyle(color);  // set color to those cells.
                    layoutCellsList.get(cellsCount).setText(nameList[cellsCount%(nameList.length)] + "." + ((j)/numberOfSamples+1)); //set probe text to those cells
                    cellsCount++;
                }
            }
        }       
        
        ///populate initial data into probe table
        //List<String> probeList=data.getProbeList();
        //probeTable.setItems(probeList);
        /*
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
*/
  }

    
    private int getSeletedTabIndex() {
        for(int i = 0; i<beadPlates .size();i++)
            if(beadPlates .get(i).isSelected())
            {
                return i;
            }
        return -1;
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
    private void uploadFilesEvent(ActionEvent event) {
    }




    // open a open up page to edit beads for each bead plate,
    private void loadAddBeadsPage() {
        
        AddBeadsPageController beadsController = null;
        try {
           
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("/View/addBeads.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));  
            stage.showAndWait();
        
            //pass beads from homepage the editbeads page
            beadsController = fxmlLoader.getController();
            beadsController.initData(beads);
            stage.showAndWait();
            
        } catch(Exception e) {
           e.printStackTrace();
        }

    }

    @FXML
    private void editBeadForPlate1Event(ActionEvent event) {
        ModelForProbeTabe.getInstance().setCurrentPlate(0);
        loadAddBeadsPage();
        
        int experiement=ModelForProbeTabe.getInstance().getCurrentExperiment();
        int plate = ModelForProbeTabe.getInstance().getCurrentPate();
        ObservableList<bead> probes = ModelForProbeTabe.getInstance().getProbes(experiement, plate); //selected data for the bead plate
       
        ObservableList<probeTableData> popludateDataForPlate1 = createProbesTabeData(probes, experiement, plate);
        ModelForProbeTabe.getInstance().addProbeListForPopulate(experiement, plate, popludateDataForPlate1);
        probeTable1.setItems(popludateDataForPlate1);
        this.probeTable1.refresh();
        probeTable1.setVisible(true);
    }

    
    @FXML
    private void editBeadForPlate2Event(ActionEvent event) {
        ModelForProbeTabe.getInstance().setCurrentPlate(1);
        loadAddBeadsPage();
        
        int experiement=ModelForProbeTabe.getInstance().getCurrentExperiment();
        int plate = ModelForProbeTabe.getInstance().getCurrentPate();
        ObservableList<bead> probes = ModelForProbeTabe.getInstance().getProbes(experiement, plate); //selected data for the bead plate
       
        ObservableList<probeTableData> popludateDataForPlate2 = createProbesTabeData(probes, experiement, plate);
        ModelForProbeTabe.getInstance().addProbeListForPopulate(experiement, plate, popludateDataForPlate2);
        probeTable2.setItems(popludateDataForPlate2);
        this.probeTable2.refresh();
        probeTable2.setVisible(true);
    }


    //create list of selected probes for each plate. aims for populate into the table 
    // parameter probes: user selected data for the bead plate
    private ObservableList<probeTableData> createProbesTabeData(ObservableList<bead> probes,int experiement, int plate ) {
         ObservableList<probeTableData> res = FXCollections.observableArrayList();
        for(int i =0; i < probes.size();i++){
            res.add(new probeTableData(i, probes.get(i).getAnalyte()));
        }
        return res;
    }



}

