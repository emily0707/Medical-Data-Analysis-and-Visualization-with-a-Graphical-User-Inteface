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
import Model.ModelForExperiments;
import Model.PlateStatus;
import Util.ErrorMsg;
import com.jfoenix.controls.JFXButton;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
    //upload xml files area
    @FXML
    private JFXButton uploadFiles;
    @FXML
    private JFXButton resetData;
    @FXML
    private JFXButton doneUploadFiles;
    @FXML
    private ListView filesList;
    private List<String> fileNames = new ArrayList<String>(); // store xml file names as string 
    private Map<Integer, List<String>> mapOfExperiments = new HashMap<>(); 
    
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
    ObservableList<probeTableData> probesToLoad = FXCollections.observableArrayList();
    
    //right-top area, display experiement info
    @FXML
    private ChoiceBox<Integer> DropdownExperimentsChoiceBox;
    ObservableList<Integer> experiments = FXCollections.observableArrayList(); // list for choice box choices 
    @FXML
    private Text totalNumberOfExperiments;
    @FXML
    private Text currentExperiementNumber;
    @FXML
    private Text XMLfilesNames;
    private int curExperiment = -1; initiliaze to -1. 
    @FXML
    private Button setUpExperiments; // click to open a new page to edit xml files for each experiment.
 
    // user input area for plate1  and plate 2. 
    @FXML
    private TextField numSampleInput1;
    @FXML
    private TextField numReplicaInput1;
    @FXML
    private TextField sampleNamesInput1;
    @FXML
    private TextField numProbeInput1;
    @FXML
    private TextField numSampleInput2;
    @FXML
    private TextField numReplicaInput2;
    @FXML
    private TextField numProbeInput2;
    @FXML
    private TextField sampleNamesInput2;
    @FXML
    private TextField numSampleInput3;
    @FXML
    private TextField numReplicaInput3;
    @FXML
    private TextField numProbeInput3;
    @FXML
    private TextField sampleNamesInput3;
    @FXML
    private JFXButton checkLayout;
    @FXML
    private JFXButton confirmInputBtn;    
    //List<UserInputForBeadPlate> userInputsForBeadPlate = new ArrayList<>(); // list for user input data
    //HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap= new HashMap<>();

   
// bead plates tab area, use for display proble layout 
    @FXML
    private TabPane beadPlatesTab;
    @FXML
    private Tab beadPlate1Tab;
    @FXML
    private Tab beadPlate2Tab;
    @FXML
    private Tab beadPlate3Tab;
    @FXML
    private GridPane beadPlate1Layout;
    @FXML
    private GridPane beadPlate2Layout;
    @FXML
    private GridPane beadPlate3Layout;
    List<GridPane> beadPlateLayouts = new ArrayList<>(); // list for layout gridPane     
    List<Tab> beadPlates = new ArrayList<>(); // list for bead plate tabs 
    List<TextField> layoutCellsList = new ArrayList<>(); // List to hold textfield in each gridpane cells. 
    //colors list for set diffrent colors to each probe
    List<String> colors = new ArrayList<>(Arrays.asList("-fx-background-color:blue;", "-fx-background-color:yellow;", "-fx-background-color:red;", "-fx-background-color:pink;", 
            "-fx-background-color:green;", "-fx-background-color:orange;", "-fx-background-color:golden;", "-fx-background-color:purple;", "-fx-background-color:AQUA;",
            "-fx-background-color:BLUEVIOLET;", "-fx-background-color:#F5F5DC;", "-fx-background-color:BISQUE;", "-fx-background-color:brown;", "-fx-background-color:CORAL;"));
    private ObservableList<probeTableData> probes1 = FXCollections.observableArrayList();
    private ObservableList<probeTableData> probes2 = FXCollections.observableArrayList();
    private ObservableList<probeTableData> probes3 = FXCollections.observableArrayList();
//probe table area    
    @FXML
    private TableView<probeTableData> probeTable1;
    @FXML
    private TableColumn<probeTableData,Integer> ProbeCol1; 
    @FXML
    private TableColumn<probeTableData,String> BeadPlate1ProbeCol;        
    @FXML
    private JFXButton loadProbe1;
    @FXML
    private JFXButton editProbesInBeadPlate1;
     
    @FXML
    private TableView<probeTableData> probeTable2;
    @FXML
    private TableColumn<probeTableData,Integer> ProbeCol2;
    @FXML
    private TableColumn<probeTableData,String> BeadPlate2ProbeCol;  
    @FXML
    private JFXButton editProbesInBeadPlate2;      
    @FXML
    private JFXButton loadProbe2;

    @FXML
    private TableView<?> probeTable3;
    @FXML
    private TableColumn<?, ?> probeCol3;
    @FXML
    private TableColumn<?, ?> BeadPlate3ProbeCol;
    @FXML
    private JFXButton editProbesInBeadPlate3;
    @FXML
    private JFXButton loadProbe3;    
    
    
    //bead plate set up status table
    @FXML
    private TableView<PlateStatus> beadPlateStatusTable;
    @FXML
    private TableColumn<PlateStatus, String> beadPlateCol;
    @FXML
    private TableColumn<PlateStatus, String> confirmedCol;
    private  ObservableList<PlateStatus> status = FXCollections.observableArrayList();
    

// analyze   
    @FXML
    private JFXButton analyze;
    @FXML
    private AnchorPane exp2AP;
    @FXML
    private Text plate2Text;
    @FXML
    private Text plate3Text;







        
        
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
        probeTable1.setItems(probes1);
         
         //set beads data into bead plate 2 probe List table. 
        ProbeCol2.setCellValueFactory(new PropertyValueFactory<probeTableData,Integer>("probeCount"));
        BeadPlate2ProbeCol.setCellValueFactory(new PropertyValueFactory<probeTableData,String>("probeForPlate"));   
        //probeTable2.setItems(testdata);
        
        DropdownExperimentsChoiceBox.getSelectionModel().selectedIndexProperty().addListener(this::itemChanged);
    } 
    
   
    /*upload xml files area */
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
            ModelForExperiments.getInstance().setXMLFiles(fileNames);
            int count = fileNames.size() /2;
            ModelForExperiments.getInstance().setNumberOfExperiments(count);
            
            //set value to experiements dropbox base on # of experiments.
            List<Integer> counts = new ArrayList<Integer>();
            for(int i = 1; i <=count; i++)
            {
                counts.add(i);
                mapOfExperiments.put(i, new ArrayList<String>());
                mapOfExperiments.get(i).add(fileNames.get(2*i -1));
                if(2*i < fileNames.size()) // for # of xml flles is odd. 
                    mapOfExperiments.get(i).add(fileNames.get(2*i));
            }
            ModelForExperiments.getInstance().setExperimentsMap(mapOfExperiments);// put it in model 
            //Map<Integer, List<String>> XMLFileMap = ModelForExperiments.getInstance().getExperimentsXMLFileMap();
            experiments = FXCollections.observableList(counts); // for choice box drop down menue
            ModelForExperiments.getInstance().setExperiments(experiments);
            initializePlateStatus();
        }
        else
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("Files valid!");
        }           
        updateExperimentsInfo();
    }
    
    public void initializePlateStatus()
    {
        int experiments = ModelForExperiments.getInstance().getNumberOfExperiments();
        for(int i = 1; i <=experiments; i++)
        {
            String experiment = "Experiment " + i; 
            List<String> lists = ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(i);
            for(int j = 1 ; j <=lists.size(); j++)
            {
                String plate = experiment + " plate " + j;
                status.add(new PlateStatus(plate, "false"));
            }
        }
        beadPlateCol.setCellValueFactory(new PropertyValueFactory<PlateStatus,String>("plate"));
        confirmedCol.setCellValueFactory(new PropertyValueFactory<PlateStatus,String>("status"));
        beadPlateStatusTable.setItems(status);       
    }
    
   // change plate status to Yes after user click "confirm input " button 
    @FXML
    private void confirmInputEvent(ActionEvent event) {
        Map<Integer, List<String>> map = ModelForExperiments.getInstance().getExperimentsXMLFileMap();
        for( int i = (curExperiment -1)*2; i < map.get(curExperiment).size() + (curExperiment -1)*2; i++)
        {
            status.get(i).setStatus("True");
        }
        beadPlateStatusTable.refresh();
    }
    
    @FXML
    private void doneUploadFilesEvent(ActionEvent event) {
        uploadFiles.setDisable(true);
    }

    @FXML
    private void resetDataEvent(ActionEvent event) {
        filesList.getItems().clear();
        uploadFiles.setDisable(false);
        
        // to do : xml files map clear previous data associated with xml files, do it in the set up experiment page
        // to do : clear choice box and other text
        // to do:  clear user input??
      
    }
    
    // choice box listener 
    public void itemChanged(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
    {
        int val = (int)newValue; // curExperiment -1  e.g. 0
        curExperiment = experiments.get(val); // get current Experiment number e.g 1
        // change information on the top experiment area 
        currentExperiementNumber.setText(String.valueOf(curExperiment));
        ModelForExperiments.getInstance().setCurrentExperiment(curExperiment);
        HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap();
        List<String> files = ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(curExperiment);
        XMLfilesNames.setText(generateXMLFilesString(files));      
        // clear previous input
        clearUserInput(3);       
        //clearBeadsPlateLayout();
        clearLayout();
        
        //nable & disable plate tab, bead , user input base on the # of xml files of current experiment
        int fileSize = files.size();
        setUpExeprimentBaseOnSizeOfXMLFiles(fileSize);
        
        // display user set up information on the bead plate. 
        if(userInputsForBeadPlateMap.size()==0) return;  // if no user input for experiment set up, just return. 
        // change layout and user input infor for the new expriment if there are user inputs for current experiments
        if(userInputsForBeadPlateMap.size()!=0 && userInputsForBeadPlateMap.get(curExperiment).size()!=0 )
        {
           int size = userInputsForBeadPlateMap.get(curExperiment).size();
           displayUserInput(userInputsForBeadPlateMap.get(curExperiment),size);
        }
     }
    
    // helper function for set up experiment after user manually set up experiment 
    // enable & disable plate tab, bead , user input base on the # of xml files of current experiment. 
    private void setUpExeprimentBaseOnSizeOfXMLFiles(int fileSize)
    {
        if(fileSize == 0) // no xml files choosen for the experiment , error
        {
                ErrorMsg error = new ErrorMsg();
                error.showError("No XML Files choosen for experiment " + curExperiment );            
        }
        if(fileSize == 1)
        {
            disablePlate2();
            disablePlate3();
        }
        if(fileSize == 2)
        {
            enablePlate2();
            disablePlate3();
        }
        if(fileSize == 3)
        {
            enablePlate2();
            enablePlate3();
        }
    }
    
    // disable plate2, user input for plate 2, and bead table plate2
    private void disablePlate2()
    {
            beadPlate2Tab.setDisable(true);
            plate2Text.setVisible(false);
            numSampleInput2.setDisable(true);
            numReplicaInput2.setDisable(true);
            numProbeInput2.setDisable(true);
            sampleNamesInput2.setDisable(true);
            probeTable2.setDisable(true);
            loadProbe2.setDisable(true);
            editProbesInBeadPlate2.setDisable(true);
    }
    private void enablePlate2()
    {
            beadPlate2Tab.setDisable(false);
            plate2Text.setVisible(true);
            numSampleInput2.setDisable(false);
            numReplicaInput2.setDisable(false);
            numProbeInput2.setDisable(false);
            sampleNamesInput2.setDisable(false); 
            probeTable2.setDisable(false);
            loadProbe2.setDisable(false);
            editProbesInBeadPlate2.setDisable(false);
    }
    private void disablePlate3()
    {
            beadPlate3Tab.setDisable(true);
            plate3Text.setVisible(false);
            numSampleInput3.setDisable(true);
            numReplicaInput3.setDisable(true);
            numProbeInput3.setDisable(true);
            sampleNamesInput3.setDisable(true); 
            probeTable3.setDisable(true);
            loadProbe3.setDisable(true);
            editProbesInBeadPlate3.setDisable(true);        
    }
    private void enablePlate3()
    {
            beadPlate3Tab.setDisable(false);
            plate3Text.setVisible(true);
            numSampleInput3.setDisable(false);
            numReplicaInput3.setDisable(false);
            numProbeInput3.setDisable(false);
            sampleNamesInput3.setDisable(false); 
            probeTable3.setDisable(false);
            loadProbe3.setDisable(false);
            editProbesInBeadPlate3.setDisable(false);
    }
            
            

    //update left upper experiments info after user upload xml files
    private void updateExperimentsInfo() {
        //display # of experiements
        int count = ModelForExperiments.getInstance().getNumberOfExperiments();
        totalNumberOfExperiments.setText(Integer.toString(count));
        
        // update # of experiment in drop down choice box 
        ObservableList<Integer> experiments = ModelForExperiments.getInstance().getExperiments();
        DropdownExperimentsChoiceBox.setItems(experiments);
        
        // update bead plate status table 
        // function to be developed 
        
        //clear previous user input and layout if any, user needs to submit information again. 
        ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().clear(); 
        currentExperiementNumber.setText("0");
        XMLfilesNames.setText(null);
        clearUserInput(1);   
        clearUserInput(2);   
        clearUserInput(3);   
        clearLayout();
        
        //add experiments to the context and create beadlist for each bead plate, data structure: map
        for(int i =0; i<experiments.size();i++)
        {
            ModelForExperiments.getInstance().addExperiement(Integer.valueOf(count));
        }
       
        //after user click expriements from the choice box, Change experiment info
        /*
        DropdownExperimentsChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //check whether user has done with uploading files 
               if(!uploadFiles.isDisable()) 
                   System.out.println("Please confirm your xml files first!");      
               
                Integer currentExp = DropdownExperimentsChoiceBox.getSelectionModel().getSelectedItem();
                curExperiment = currentExp;
                ModelForExperiments.getInstance().setCurrentExperiment(curExperiment);
                currentExperiementNumber.setText(Integer.toString(currentExp));  
                List<String> files = ModelForExperiments.getInstance().getExperimentsMap().get(currentExp);
                XMLfilesNames.setText(generateXMLFilesString(files));
                //ModelForExperiments.getInstance().setCurrentExperiment( currentExp -1 ); // set current experiment in model for proble table
            }
        });*/
    }
    
    // helper function :
    //when user change experiment, generate xml files string for the new experiment base on the list of files name. 
    private String generateXMLFilesString( List<String> files)
    {
        String s = "";
        for(int i = 0; i <files.size(); i++)
        {
            if(i==files.size() -1) 
                s += files.get(i);
            else 
                s += files.get(i) + ",";
        }
        return s;
    }
    // open a new page for user to manually to edit xml files for each experiment. 
    @FXML
    private void setUpExperimentsAction(ActionEvent event) throws MalformedURLException {
         //SetUpExperimentsController beadsController = null;
         URL url = Paths.get("./src/View/SetUpExperiments.fxml").toUri().toURL();
         Parent root ;
               try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                root = fxmlLoader.load(url);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));  
                stage.showAndWait();

        stage.show();
        } catch(Exception e) {
           e.printStackTrace();
          }
        updateExperimentsInfo();        
    }
    
    // event for user select different experiement,
    //display user input data and bead layout for that experiement base on the data stored for the experiment. 
    // if no data, leave blank user input and empty bead layout. 
    @FXML
    private void changeExperimentEvent(Event event) {
        // set text for that current experiment
       if(ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(curExperiment) == null) return;
       int size = ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(curExperiment).size();
        for(int i = 0; i < size; i++ )
       {
           if(i == size-1)
           {
                XMLfilesNames.setText( ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(curExperiment).get(i) + ",");
           }
           else 
               XMLfilesNames.setText( ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(curExperiment).get(i));
       }
        
        
        //if the experiement already has data, show data 
        HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap();
        if(userInputsForBeadPlateMap.get(curExperiment)!=null)
        {
            int plateSize = userInputsForBeadPlateMap.get(curExperiment).size();
            displayUserInput(userInputsForBeadPlateMap.get(curExperiment),plateSize);
            displayBeadsPlateLayout(curExperiment);
        }        
        //has no data submited, display empty user input area and empty beads plate table. 
        else 
        {
            //clearUserInput();
            //clearLayout();
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
    

    

// open a new page for user to manually to edit xml files for each experiment. 
    private void loadSetUpExperimentsPage() {
        
        AddBeadsPageController beadsController = null;
        try {
           
            FXMLLoader fxmlLoader = new FXMLLoader();
            //C:\Users\feiping\Documents\NetBeansProjects\JavaFXApplication1\src\View
            Parent root = fxmlLoader.load(getClass().getResource("/JavaFXApplication1/src/View/SetUpExperiments.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));  
            stage.showAndWait();
        
            //pass beads from homepage the editbeads page
            beadsController = fxmlLoader.getController();
            //beadsController.initData(beads);
            stage.showAndWait();
            
        } catch(Exception e) {
           e.printStackTrace();
        }

    }
    //helper fucntionf for chaning experiment
    // when no data at cur experiement, clear userinput area.
    private void clearUserInput(int size)
    {
        if(size == 1){
            clearUserInputForBeadPlate1();
        }
        if(size  == 2)
        {
                clearUserInputForBeadPlate1();
                clearUserInputForBeadPlate2();
        }
        if(size == 3)
        {
                clearUserInputForBeadPlate1();
                clearUserInputForBeadPlate2();     
                clearUserInputForBeadPlate3();   
        }
    }
    
    public void clearUserInputForBeadPlate1()
    {
        numSampleInput1.clear();
        numReplicaInput1.clear();
        sampleNamesInput1.clear();
        numProbeInput1.clear();
        sampleNamesInput1.clear();
    }
    private void clearUserInputForBeadPlate2()
    {
        numSampleInput2.clear();
        numReplicaInput2.clear();
        numProbeInput2.clear();
        sampleNamesInput2.clear();        

    }
    
        private void clearUserInputForBeadPlate3()
    {
        numSampleInput3.clear();
        numReplicaInput3.clear();
        sampleNamesInput3.clear();
        numProbeInput3.clear();
        sampleNamesInput3.clear();
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
    
    
    //heleper function clear the bead plate layout. However, it also clears grid lines. 
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
    private void displayUserInput(List<UserInputForBeadPlate> data, int size) {
        if(size == 1){
            
        }
        if(size  == 2)
        {
                displayUserInputForBeadPlate1(data);
                displayUserInputForBeadPlate2(data);
        }
        if(size == 3)
        {
                displayUserInputForBeadPlate1(data);
                displayUserInputForBeadPlate2(data);
                displayUserInputForBeadPlate3(data);
        }

        
    }

    private void displayUserInputForBeadPlate1(List<UserInputForBeadPlate> data)
    {
        //update user input for bead plate 1
        numSampleInput1.setText(Integer.toString(data.get(0).getNumOfSamples()));
        numReplicaInput1.setText(Integer.toString(data.get(0).getNumOfSamples()));
        numProbeInput1.setText(Integer.toString(data.get(0).getNumOfProbes()));
        sampleNamesInput1.setText(data.get(0).getNameInput());        
    }
 
    private void displayUserInputForBeadPlate2(List<UserInputForBeadPlate> data)
    {
        numSampleInput2.setText(Integer.toString(data.get(1).getNumOfSamples()));
        numReplicaInput2.setText(Integer.toString(data.get(1).getNumOfSamples()));
        numProbeInput2.setText(Integer.toString(data.get(1).getNumOfProbes()));
        sampleNamesInput2.setText(data.get(1).getNameInput());
    }
    
    private void displayUserInputForBeadPlate3(List<UserInputForBeadPlate> data)
    {
        numSampleInput3.setText(Integer.toString(data.get(2).getNumOfSamples()));
        numReplicaInput3.setText(Integer.toString(data.get(2).getNumOfSamples()));
        numProbeInput3.setText(Integer.toString(data.get(2).getNumOfProbes()));
        sampleNamesInput3.setText(data.get(2).getNameInput());
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
        beadPlateLayouts.add(beadPlate3Layout);
        beadPlates.add(beadPlate1Tab);
        beadPlates.add(beadPlate2Tab);       
        beadPlates.add(beadPlate3Tab);   
        }
       
       // erro msg for user has not slected an experiment
       if(curExperiment == -1)
       {
                ErrorMsg error = new ErrorMsg();
                error.showError("choose an experiment first!");           
       }
       //clearLayout();
        List<UserInputForBeadPlate> userInputsForBeadPlate = new ArrayList<>();
       //gather user inputs for bead plate1 form each text fileds 
        int numberOfSamples = Integer.parseInt(numSampleInput1.getText());
        int numberOfReps = Integer.parseInt(numReplicaInput1.getText());
        int numberOfProbes =  Integer.parseInt(numProbeInput1.getText());  
        
        //check whether input is valid 
        if(numberOfSamples * numberOfReps * numberOfProbes > 96)
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("user input for Experiment " + curExperiment + " bead plate one exceeds 96!");
        }

        String names = sampleNamesInput1.getText();
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
        curExperiment = ModelForExperiments.getInstance().getCurrentExperiment();
        ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().put(curExperiment , userInputsForBeadPlate);
        displayBeadsPlateLayout(curExperiment);

    }
    
    //get data of current experiement and display the bead plate layout. 
    private void displayBeadsPlateLayout(int curExperiment)
    { 
        HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap();
        if(userInputsForBeadPlateMap.get(curExperiment) == null) return;
        for(int i =0; i <userInputsForBeadPlateMap.get(curExperiment).size();i++ )
        {
            displayLayout(i, userInputsForBeadPlateMap.get(curExperiment).get(i));
        }        
    }

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
                String color = colors.get(i % colors.size());  // choose different color to each probe
                for(int j = 0; j < numberOfSamples * numberOfReps; j++)
                {
                    layoutCellsList.get(cellsCount).setStyle(color);  // set color to those cells.
                    layoutCellsList.get(cellsCount).setText(nameList[cellsCount %(nameList.length)] + "." + ((j)/numberOfSamples+1)); //set probe text to those cells
                    cellsCount++;
                }
            }
        }       
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
        ModelForExperiments.getInstance().setCurrentPlate(0);
        loadAddBeadsPage();
        
        int experiement=ModelForExperiments.getInstance().getCurrentExperiment();
        int plate = ModelForExperiments.getInstance().getCurrentPate();
        ObservableList<bead> probes = ModelForExperiments.getInstance().getProbes(experiement, plate); //selected data for the bead plate
       
        ObservableList<probeTableData> popludateDataForPlate1 = createProbesTabeData(probes, experiement, plate);
        ModelForExperiments.getInstance().addOneProbeListForPopulate(experiement, plate, popludateDataForPlate1);
        probeTable1.setItems(popludateDataForPlate1);
        this.probeTable1.refresh();
        probeTable1.setVisible(true);
    }

    
    @FXML
    private void editBeadForPlate2Event(ActionEvent event) {
        ModelForExperiments.getInstance().setCurrentPlate(1);
        loadAddBeadsPage();
        
        int experiement=ModelForExperiments.getInstance().getCurrentExperiment();
        int plate = ModelForExperiments.getInstance().getCurrentPate();
        ObservableList<bead> probes = ModelForExperiments.getInstance().getProbes(experiement, plate); //selected data for the bead plate
       
        ObservableList<probeTableData> popludateDataForPlate2 = createProbesTabeData(probes, experiement, plate);
        //ModelForExperiments.getInstance().addProbeListForPopulate(experiement, plate, popludateDataForPlate2);
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

private void generateBeadsToLoad()
{
    probesToLoad.add(new probeTableData(1, "TCR"));
     probesToLoad.add(new probeTableData(2, "CD"));
      probesToLoad.add(new probeTableData(3, "LAT"));
       probesToLoad.add(new probeTableData(4, "ZAP"));
       probesToLoad.add(new probeTableData(5, "SLP"));


}

    @FXML
    private void loadProbe1Event(ActionEvent event) {
        if(probesToLoad.size()==0) 
            generateBeadsToLoad();
        else
        {
            // if no map generate for current experiment probles, initiliaze it. 
            if(ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment) == null)
                ModelForExperiments.getInstance().initializeProbeListMap(curExperiment);
        HashMap<Integer, ObservableList<probeTableData>> map = ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment);
            // when no input or input is 0, error
           if(numProbeInput1.getText() == null || Integer.parseInt(numProbeInput1.getText()) == 0 )
            {
                ErrorMsg error = new ErrorMsg();
                error.showError("Please set up probe numbers before load probe!");
            }
            else // load # of probles base on user input 
            {
                ObservableList<probeTableData> probes = FXCollections.observableArrayList();
                for(int i = 0; i < Integer.parseInt(numProbeInput1.getText());i++ )
                {
                    probeTableData probe = probesToLoad.get(i);
                    probes.add(probe);
                }
                ModelForExperiments.getInstance().addOneProbeListForPopulate(curExperiment,1,probes);
                probes1 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 1);
                //refresh probe table.
                probeTable1.getItems().clear();
                probeTable1.getItems().addAll(probes1);
                // probeTable1.getColumns().get(0).setVisible(false);
               // probeTable1.getColumns().get(0).setVisible(true);
            }

        }
    }

    @FXML
    private void loadProbe2Event(ActionEvent event) {
    }

    @FXML
    private void loadProbe3Event(ActionEvent event) {
    }

    






}

