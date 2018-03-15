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
import javafx.scene.input.MouseEvent;
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
    private  ObservableList<bead> analytes;   //beads read from xml files
    
    
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
    private int curExperiment = -1; //initiliaze to -1. 
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
    private ObservableList<probeTableData> probes1 = FXCollections.observableArrayList();
    ObservableList<probeTableData> probesToLoad1 = FXCollections.observableArrayList();
     
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
    private ObservableList<probeTableData> probes2 = FXCollections.observableArrayList();
    ObservableList<probeTableData> probesToLoad2 = FXCollections.observableArrayList();

    @FXML
    private TableView<probeTableData> probeTable3;
    @FXML
    private TableColumn<probeTableData,Integer> ProbeCol3;
    @FXML
    private TableColumn<probeTableData,String> BeadPlate3ProbeCol;
    @FXML
    private JFXButton editProbesInBeadPlate3;
    @FXML
    private JFXButton loadProbe3;    
    private ObservableList<probeTableData> probes3 = FXCollections.observableArrayList();
    
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

    //default data to user Input area 
    String namesInput = "WK,WC,HK,HC";
    String[] names = namesInput.split(",");
    UserInputForBeadPlate defaultUserInput = new UserInputForBeadPlate(4, 2, namesInput, names,10, new ArrayList<String>());


        
    @Override
    public void initialize(URL url, ResourceBundle rb) {      

        //HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap= new HashMap<>(); 
        //initilize probe to load for probe table 1
        String[] probesList1 = {"Fyn","PSD95","NMDAR1","NMDAR2A","NMDAR2B","mGluR5","Shank3","Homer1","Homer1a","PI3K"};
        for(int i = 1; i <= probesList1.length; i++)
        {
            probesToLoad1.add(new probeTableData(i, probesList1[i-1]));
        }
        String[] probesList2 = {"GluR1","GluR2","SynGAP","SAP97","NLGN3","Ube3a","CamKII","SAPAP","panSHANK","Shank1"};
        for(int i = 1; i <= probesList2.length; i++)
        {
            probesToLoad2.add(new probeTableData(i, probesList2[i-1]));
        }
        
        //poplulate files list 
        if(ModelForExperiments.getInstance().getXMLFiles()!=null && !ModelForExperiments.getInstance().getXMLFiles().isEmpty() )
        {
            List<String> fileNames = ModelForExperiments.getInstance().getXMLFiles();
            ObservableList<String> files = FXCollections.observableArrayList(fileNames);
            filesList.setItems(files);           
        }

        //populate choice box 
        if(ModelForExperiments.getInstance().getExperiments()!=null && !ModelForExperiments.getInstance().getExperiments().isEmpty())
        {
            experiments = ModelForExperiments.getInstance().getExperiments();
            DropdownExperimentsChoiceBox.setItems(experiments);                  
        }
        DropdownExperimentsChoiceBox.getSelectionModel().selectedIndexProperty().addListener(this::itemChanged);     
        
        // popoluate infomation for cur experiment
        if(ModelForExperiments.getInstance().getCurrentExperiment() > 0)
        {
            int count = ModelForExperiments.getInstance().getNumberOfExperiments();
            totalNumberOfExperiments.setText(Integer.toString(count));         
            curExperiment =ModelForExperiments.getInstance().getCurrentExperiment();
            DropdownExperimentsChoiceBox.setValue(curExperiment);
            currentExperiementNumber.setText(String.valueOf(curExperiment));
            ModelForExperiments.getInstance().setCurrentExperiment(curExperiment);
            List<String> files = ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(curExperiment);
            XMLfilesNames.setText(generateXMLFilesString(files));                
        }            

        // populate analytes data. 
        beadCol.setCellValueFactory(new PropertyValueFactory<bead,String>("RegionNumber"));
        analyteCol.setCellValueFactory(new PropertyValueFactory<bead,String>("Analyte"));
        if(ModelForExperiments.getInstance().getAnalytes()!=null) 
        {
            analytes = ModelForExperiments.getInstance().getAnalytes();
        }
        beadTable.setItems(analytes);
        
         if(beadPlates.size()==0)
        {
            beadPlateLayouts.add(beadPlate1Layout);
            beadPlateLayouts.add(beadPlate2Layout);
            beadPlateLayouts.add(beadPlate3Layout);
            beadPlates.add(beadPlate1Tab);
            beadPlates.add(beadPlate2Tab);       
            beadPlates.add(beadPlate3Tab);   
        }
        
        //populate user input and bead plates layout.     
        if(ModelForExperiments.getInstance().getProbeListForPopulate()!=null && !ModelForExperiments.getInstance().getProbeListForPopulate().isEmpty())
        {
            
            checkLayoutEventHelper(); // diaplay bead plate layout
            HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap();
            int size = userInputsForBeadPlateMap.get(curExperiment).size();
            displayUserInput(userInputsForBeadPlateMap.get(curExperiment),size);
        }
        
        
        

        
        //set beads data into bead plate 1 probe List table. 
        
        ProbeCol1.setCellValueFactory(new PropertyValueFactory<probeTableData,Integer>("probeCount"));
        BeadPlate1ProbeCol.setCellValueFactory(new PropertyValueFactory<probeTableData,String>("probeForPlate"));
        if(curExperiment >0 && ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 1)!=null) // populate probe data into probe tables
        {
            probes1 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 1);                    
        }
        probeTable1.setItems(probes1);
        probeTable1.setRowFactory(row -> new TableRow<probeTableData>(){ // set color code to probe table 1
                @Override
                public void updateItem(probeTableData item, boolean empty){
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("");
                    } else {                        
                        //need get size of probes of that plate and then update the color correctly 
                        String s = numProbeInput1.getText();
                        if(s!= null && !s.equals(""))
                            {
                                int size = Integer.parseInt(s);
                                int index = (item.getProbeCount()-1) % size;
                                setStyle(colors.get(index));                    
                            }

                    }
                }
        }
        );



         //set beads data into bead plate 2 probe List table. 
        ProbeCol2.setCellValueFactory(new PropertyValueFactory<probeTableData,Integer>("probeCount"));
        BeadPlate2ProbeCol.setCellValueFactory(new PropertyValueFactory<probeTableData,String>("probeForPlate"));   
        if(curExperiment >0 && ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 2)!=null)
        {
            probes2 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 2);                    
        }        
        probeTable2.setItems(probes2);
        probeTable2.setRowFactory(row -> new TableRow<probeTableData>(){ // set color code to probe table 
                @Override
                public void updateItem(probeTableData item, boolean empty){
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("");
                    } else {                        
                        //need get size of probes of that plate and then update the color correctly 
                        String s = numProbeInput2.getText();
                        if(s!=null && !s.equals("") )
                            {
                                int size = Integer.parseInt(numProbeInput2.getText());
                                int index = (item.getProbeCount()-1) % size;
                                setStyle(colors.get(index));                    
                            }
                    }
                }
        }
        );        
        
        //set beads data into bead plate 3 probe List table. 
        ProbeCol3.setCellValueFactory(new PropertyValueFactory<probeTableData,Integer>("probeCount"));
        BeadPlate3ProbeCol.setCellValueFactory(new PropertyValueFactory<probeTableData,String>("probeForPlate"));   
        if(curExperiment >0 &&  ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 3)!=null)
        {
            probes3 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 3);                    
        }        
        probeTable3.setItems(probes3);
        probeTable3.setRowFactory(row -> new TableRow<probeTableData>(){ // set color code to probe table 
                @Override
                public void updateItem(probeTableData item, boolean empty){
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("");
                    } else {                        
                        //need get size of probes of that plate and then update the color correctly 
                        String s = numProbeInput3.getText();
                        if(s!=null && !s.equals("")  )
                            {
                                int size = Integer.parseInt(numProbeInput3.getText());
                                int index = (item.getProbeCount()-1) % size;
                                setStyle(colors.get(index));                    
                            }
                    }
                }
        }
        );   
            

    } 
    
   
    /*upload xml files area */
    // for upload file button. click the button to display files lists in a listview 
    @FXML
    private void selectFilesEvent(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
        
        List<File> selectedFiles = fc.showOpenMultipleDialog(null);

        HashMap<Integer, List<String>> mapOfExperimentsWithAbsolutePath = new HashMap<>();
        if(selectedFiles!= null)
        {
            for(int i =0; i<selectedFiles.size(); i++)
            {
                //get absolute path of the file on users' computer
                // ?? save the absolute path
                File file = selectedFiles.get(i);
                String path = "";
                path += file.getAbsolutePath();
                String directory = file.getParentFile().getAbsolutePath();
                ModelForExperiments.getInstance().setDirectory(directory); // save directory to model
                
                String filename = selectedFiles.get(i).getName();
                if(analytes==null || analytes.isEmpty())
                {
                    getAnalytes(path);
                    beadTable.setItems(analytes);
                }            
                filesList.getItems().add(filename); // display on the listview fileList. 
                fileNames.add(filename);
            } 
            ModelForExperiments.getInstance().setXMLFiles(fileNames);
            int count = fileNames.size() %2 == 1 ? fileNames.size() /2+1 : fileNames.size() /2;
            ModelForExperiments.getInstance().setNumberOfExperiments(count);
            
            //set value to experiements dropbox base on # of experiments.
            List<Integer> counts = new ArrayList<Integer>();    
            int index = 0;
            for(int i = 1; i <=count; i++)
            {
                counts.add(i);                
                mapOfExperiments.put(i, new ArrayList<String>());                
                mapOfExperiments.get(i).add(fileNames.get(index++));                           
                if(index == fileNames.size()) break; // exceeds range 
                mapOfExperiments.get(i).add(fileNames.get(index++));

            }
            ModelForExperiments.getInstance().setExperimentsMap(mapOfExperiments);// put it in model 
            experiments = FXCollections.observableList(counts); // for choice box drop down menue
            ModelForExperiments.getInstance().setExperiments(experiments);
            //create proble list map for each experiment
            ModelForExperiments.getInstance().initilizeProbeListForPopulate();  
            
        }
        else
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("Files valid!");
        }           
        updateExperimentsInfo();
    }
    
    //read analystes from the first xml file and save it to data model. 
    private void getAnalytes(String filePath )
    {
        StAXParser parser = new StAXParser();
        analytes =  parser.getAnalytes(filePath);
        ModelForExperiments.getInstance().setAnalytes(analytes);        
    }
    
    
    // initialize plate stauts after user upload xml files or manually set up experiment
    public void initializePlateStatus()
    {
        //clear status list if it is not empty
        if(!status.isEmpty())
            status.clear();
        
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
        //numSampleInput1.setText("2");
    }
    
   // change plate status to Yes after user click "confirm input " button 
    @FXML
    private void confirmInputEvent(ActionEvent event) {
        Map<Integer, List<String>> map = ModelForExperiments.getInstance().getExperimentsXMLFileMap();
        int index = 0; 
        // calculate start index in the status list
        if(curExperiment!=0)
        {
            for(int i = 1; i <curExperiment;i++ )
            {
                index+=map.get(i).size();
            }
        }
        for( int i = index; i < index + map.get(curExperiment).size(); i++)
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
        
        // xml files map clear previous data associated with xml files, do it in the set up experiment page
        ModelForExperiments.getInstance().getXMLFiles().clear();
        ModelForExperiments.getInstance().getExperimentsXMLFileMap().clear();
        ModelForExperiments.getInstance().getExperiments().clear();
        
        //clear analytes table 
        analytes.clear();
        ModelForExperiments.getInstance().getAnalytes().clear();
        beadTable.refresh();
        
        //clear choice box and other text
        experiments.clear();
        totalNumberOfExperiments.setText("0");
        currentExperiementNumber.setText("");
        XMLfilesNames.setText("");
        curExperiment = -1;
        ModelForExperiments.getInstance().setCurrentExperiment(curExperiment);
        
        // clear status table
        status.clear();
        beadPlateStatusTable.refresh();
        
        //clear probe tables
        ModelForExperiments.getInstance().getProbeListForPopulate().clear();
        probes1.clear();
        probeTable1.refresh();
        probes2.clear();
        probeTable2.refresh();
        probes3.clear();
        probeTable3.refresh();
       
        // clear user input
       ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().clear();
       clearUserInputForBeadPlate1();
       clearUserInputForBeadPlate2();
       clearUserInputForBeadPlate3();
       
       //clear bead plate layout
       clearLayout();     
       
       //clear median value data matrix 
       ModelForExperiments.getInstance().getMedianValueMatrix().clear();
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
        //refresh probe table
        int fileSize = files.size();
        setUpExeprimentBaseOnSizeOfXMLFiles(fileSize);
        
        // display user set up information on the bead plate. 
        // change layout and user input infor for the new expriment if there are user inputs for current experiments
        // if no user input or less then set up default inputs 
        if(userInputsForBeadPlateMap.get(curExperiment)==null )
        {
               List<UserInputForBeadPlate> defaultInputs = new ArrayList<>(); //??
               ModelForExperiments.getInstance().setUserInputsForOneExperiment(curExperiment,defaultInputs); 
        }
        int size = userInputsForBeadPlateMap.get(curExperiment).size();
        if(size != fileSize)
        {
            List<UserInputForBeadPlate> inputs = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().get(curExperiment);
               while(size!=fileSize)
               {
                   inputs.add(defaultUserInput);
                   size++;
               }
               ModelForExperiments.getInstance().setUserInputsForOneExperiment(curExperiment,inputs);            
        }

       userInputsForBeadPlateMap= ModelForExperiments.getInstance().getUserInputsForBeadPlateMap();
       displayUserInput(userInputsForBeadPlateMap.get(curExperiment),size);
       
       // display bead plate layout and probe tables. 
        checkLayoutEventHelper();
     }
    
    private void clearProbeTables()
    {
         if(ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment) == null)
         {
             probes1.clear();
             probeTable1.refresh();  
             probes2.clear();
             probeTable2.refresh();  
             probes3.clear();
             probeTable3.refresh();  

         }
         else 
         {
             //update probe 1 table
             ObservableList<probeTableData> probesForPlate1 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 1);
                     //.getProbeListForPopulate(curExperiment, 1);
             if(probesForPlate1== null)
             {
                 ObservableList<probeTableData> list = FXCollections.observableArrayList();
                 ModelForExperiments.getInstance().addOneProbeListForPopulate(curExperiment, 1, list);
             }
            probes1.clear();
            probes1.addAll(probesForPlate1);
            probeTable1.refresh();                 

            // update probe2 table
            if(!probeTable2.isDisabled()){
                ObservableList<probeTableData> probesForPlate2 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 2);
                if(probesForPlate2== null)
                 {
                     probesForPlate2 = FXCollections.observableArrayList();
                     ModelForExperiments.getInstance().addOneProbeListForPopulate(curExperiment, 2, probesForPlate2);
                 }
                probes2.clear();
                probes2.addAll(probesForPlate2);
                probeTable2.refresh();                                 
            }


            //update probe 3 table
             if(!probeTable3.isDisabled())
             {
                ObservableList<probeTableData> probesForPlate3 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 3);
               if(probesForPlate3== null)
                {
                    probesForPlate3 = FXCollections.observableArrayList();
                    ModelForExperiments.getInstance().addOneProbeListForPopulate(curExperiment, 3, probesForPlate3);
                }
                probes3.clear();
                probes3.addAll(probesForPlate1);
               probeTable3.refresh();                    
             }
              
          
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
        probes1.clear();
        refreshProbeTable1();
        if(fileSize == 1)
        {            
            //refreshProbeTable1();
            if(!beadPlate2Tab.isDisabled()) 
            {
               probes2.clear();
               probeTable2.refresh();  
               disablePlate2();
            }
            if(!beadPlate3Tab.isDisabled())
            {
                probes3.clear();
                probeTable3.refresh();  
                disablePlate3();
            }
        }
        if(fileSize == 2)
        {
            if(beadPlate2Tab.isDisabled()) 
            {                
                enablePlate2();
            }
            refreshProbeTable2();
            if(!beadPlate2Tab.isDisabled()) 
            {
                probes3.clear();
                probeTable3.refresh();  
                disablePlate3();
            }
        }
        if(fileSize == 3)
        {
            if(beadPlate2Tab.isDisabled()) 
            {
                enablePlate2();
            }
            refreshProbeTable2();
            if(beadPlate3Tab.isDisabled())
            {
                enablePlate3();               
            }
            refreshProbeTable3();
        }
    }
 
        public void refreshProbeTable1()
    {
        curExperiment = ModelForExperiments.getInstance().getCurrentExperiment();
        if(!probeTable1.isDisabled()){
    
                ObservableList<probeTableData> probesForPlate1 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 1);
                if(probesForPlate1== null)
                 {
                     probesForPlate1 = FXCollections.observableArrayList();
                     ModelForExperiments.getInstance().addOneProbeListForPopulate(curExperiment, 1, probesForPlate1);
                 }
                probes1.clear();
                probes1.addAll(probesForPlate1);
                probeTable1.refresh();                                 
    }
    }
        
    public void refreshProbeTable2()
    {
                    if(!probeTable2.isDisabled()){
                ObservableList<probeTableData> probesForPlate2 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 2);
                if(probesForPlate2== null)
                 {
                     probesForPlate2 = FXCollections.observableArrayList();
                     ModelForExperiments.getInstance().addOneProbeListForPopulate(curExperiment, 2, probesForPlate2);
                 }
                probes2.clear();
                probes2.addAll(probesForPlate2);
                probeTable2.refresh();                                 
            }
    }
    
        public void refreshProbeTable3()
    {
                    if(!probeTable3.isDisabled()){
                ObservableList<probeTableData> probesForPlate3 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 3);
                if(probesForPlate3== null)
                 {
                     probesForPlate3 = FXCollections.observableArrayList();
                     ModelForExperiments.getInstance().addOneProbeListForPopulate(curExperiment, 3, probesForPlate3);
                 }
                probes3.clear();
                probes3.addAll(probesForPlate3);
                probeTable3.refresh();                                 
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
            
            

    //update left upper experiments info after user upload xml files or manually set up xml files for experiments
    private void updateExperimentsInfo() {
        //display # of experiements
        int count = ModelForExperiments.getInstance().getNumberOfExperiments();
        totalNumberOfExperiments.setText(Integer.toString(count));
      
        // update # of experiment in drop down choice box        
        ObservableList<Integer> experimentsNew = ModelForExperiments.getInstance().getExperiments();
        //experiments.clear();
        //experiments.addAll(experimentsNew);
        DropdownExperimentsChoiceBox.setItems(experimentsNew);
        
        int defaultExperiment = 1;
        DropdownExperimentsChoiceBox.setValue(defaultExperiment);
        currentExperiementNumber.setText(String.valueOf(defaultExperiment));
        ModelForExperiments.getInstance().setCurrentExperiment(defaultExperiment);
        List<String> files = ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(defaultExperiment);
        XMLfilesNames.setText(generateXMLFilesString(files));    
        //nable & disable plate tab, bead , user input base on the # of xml files of current experiment
        int fileSize = files.size();
        //setUpExeprimentBaseOnSizeOfXMLFiles(fileSize);

        // update bead plate status table 
        initializePlateStatus();
        
        // update probe list map
       // ModelForExperiments.getInstance().initilizeProbeListForPopulate();
        
        //clear previous user input and layout if any, user needs to submit information again. 
       // ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().clear(); 
        
        //clearUserInput(fileSize);   
        //clearLayout();
        
       // setUserInputByDefaultBaseOnSizeOfXMLFiles(fileSize); 
        
        
        //add experiments to the context and create beadlist for each bead plate, data structure: map
        //for(int i =0; i<experiments.size();i++)
       // {
           // ModelForExperiments.getInstance().addExperiement(Integer.valueOf(count));
       // }
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
         //clear 
         URL url = Paths.get("./src/View/SetUpExperiments.fxml").toUri().toURL();
         Parent root ;
               try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                root = fxmlLoader.load(url);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));  
                stage.showAndWait();

        //stage.show();
        } catch(Exception e) {
           e.printStackTrace();
          }
        updateExperimentsInfo();        
    }
    
    // event for user select different experiement,
    //display user input data and bead layout for that experiement base on the data stored for the experiment. 
    // if no data, leave blank user input and empty bead layout. 
    @FXML
    private void changeBeadPlateTabEvent(Event event) {
        // set text for that current experiment
       if(ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(curExperiment) == null) return;
       int size = ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(curExperiment).size();
        //if the experiement already has data, show data 
        HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap();
        if(userInputsForBeadPlateMap.get(curExperiment)!=null)
        {
            int plateSize = userInputsForBeadPlateMap.get(curExperiment).size();
            displayUserInput(userInputsForBeadPlateMap.get(curExperiment),plateSize);
            displayBeadsPlateLayout(curExperiment);
        }        

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
                if(!beadPlate2Tab.isDisabled()) clearUserInputForBeadPlate2();
        }
        if(size == 3)
        {
                clearUserInputForBeadPlate1();
               if(!beadPlate2Tab.isDisabled())  clearUserInputForBeadPlate2();     
                if(!beadPlate3Tab.isDisabled()) clearUserInputForBeadPlate3();   
        }
    }
    
    public void clearUserInputForBeadPlate1()
    {
        numSampleInput1.clear();
        numReplicaInput1.clear();
        sampleNamesInput1.clear();
        numProbeInput1.clear();

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
            displayUserInputForBeadPlate1(data);
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
        numReplicaInput1.setText(Integer.toString(data.get(0).getNumOfReplicas()));
        numProbeInput1.setText(Integer.toString(data.get(0).getNumOfProbes()));
        sampleNamesInput1.setText(data.get(0).getNameInput());        
    }
 
    private void displayUserInputForBeadPlate2(List<UserInputForBeadPlate> data)
    {
        numSampleInput2.setText(Integer.toString(data.get(1).getNumOfSamples()));
        numReplicaInput2.setText(Integer.toString(data.get(1).getNumOfReplicas()));
        numProbeInput2.setText(Integer.toString(data.get(1).getNumOfProbes()));
        sampleNamesInput2.setText(data.get(1).getNameInput());
    }
    
    private void displayUserInputForBeadPlate3(List<UserInputForBeadPlate> data)
    {
        numSampleInput3.setText(Integer.toString(data.get(2).getNumOfSamples()));
        numReplicaInput3.setText(Integer.toString(data.get(2).getNumOfReplicas()));
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

        // erro msg for user has not slected an experiment
       if(curExperiment == -1)
       {
                ErrorMsg error = new ErrorMsg();
                error.showError("choose an experiment first!"); 
                return;
       }
       
       checkLayoutEventHelper();
    }
    
   
    private void checkLayoutEventHelper()
    {
     
       // gather users' input and put it in to map base on size of XML files. 
       List<UserInputForBeadPlate> userInputsForBeadPlate = new ArrayList<>();
       curExperiment = ModelForExperiments.getInstance().getCurrentExperiment();
       int size = ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(curExperiment).size();
       if(size == 1)
       {
           if(!getUserInputforPlate1(userInputsForBeadPlate)) return;
       }
       else if (size == 2)
       {
           if(!getUserInputforPlate1(userInputsForBeadPlate)) return;
           if(!getUserInputforPlate2(userInputsForBeadPlate)) return;      
       }
       else
       {
           if(!getUserInputforPlate1(userInputsForBeadPlate)) return;
           if(!getUserInputforPlate2(userInputsForBeadPlate)) return; 
           if(!getUserInputforPlate3(userInputsForBeadPlate)) return;          
       }
        //find current experiment, put the experiement into the map associated with curexperiment number. 
        ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().put(curExperiment , userInputsForBeadPlate);
        displayBeadsPlateLayout(curExperiment);        
        displayProbeTable(size);
        HashMap<Integer, HashMap<Integer, ObservableList<probeTableData>>> probesListForPopulate  = ModelForExperiments.getInstance().getProbeMapForPopulate(); // debug
   
    }
    
    // load probe and diaply probes on to probe tables base on xml files size. 
    private void displayProbeTable(int size)
    {
       if(size == 1)
       {
           loadProbe1Helper() ;
       }
       else if (size == 2)
       {
           loadProbe1Helper() ;
           loadProbe2Helper() ;
       }
       else
       {
           loadProbe1Helper() ;
           loadProbe2Helper() ;
           loadProbe3Helper() ;
       }        
    }
    //get data of current experiement and display the bead plate layout. 
    private void displayBeadsPlateLayout(int curExperiment)
    { 
        clearLayout(); // in case the new input of totoal number beads is less than previous input 
        HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap();
        if(userInputsForBeadPlateMap.get(curExperiment) == null) return;
        //List<UserInputForBeadPlate> inputs = userInputsForBeadPlateMap.get(curExperiment);
        for(int i =0; i <userInputsForBeadPlateMap.get(curExperiment).size();i++ )
        {
            displayLayout(i, userInputsForBeadPlateMap.get(curExperiment).get(i));
        }        
    }

    //display layout in the gridpane
    // index: index of the bead plate 
    //data: user input data for that bead plate   
  private void displayLayout(int index, UserInputForBeadPlate data) {
        
                
         if(beadPlates.size()==0)
        {
            beadPlateLayouts.add(beadPlate1Layout);
            beadPlateLayouts.add(beadPlate2Layout);
            beadPlateLayouts.add(beadPlate3Layout);
            beadPlates.add(beadPlate1Tab);
            beadPlates.add(beadPlate2Tab);       
            beadPlates.add(beadPlate3Tab);   
        }
         //fill cells with colors and values base on users' inputs
        layoutCellsList = getCells(beadPlateLayouts.get(index));
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

            
        } catch(Exception e) {
           e.printStackTrace();
        }

    }

    @FXML
    private void editBeadForPlate1Event(ActionEvent event) {
        if(curExperiment == -1)
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("choose an Experiment first!");                   
        }
        if(numProbeInput1.getText() == null || Integer.parseInt(numProbeInput1.getText()) <=0 ) 
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("invalid input for number of probe!");           
        }
        if(probes1 ==null || probes1.size() == 0)
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("please load probe first!");                
        }
        ModelForExperiments.getInstance().setCurPlate(1);
        loadAddBeadsPage();        
        curExperiment=ModelForExperiments.getInstance().getCurrentExperiment();    
        ObservableList<probeTableData> probesForPlate1 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 1); //selected data for the bead plate
        probeTable1.getItems().clear();
        probeTable1.getItems().addAll(probesForPlate1);
    }

    
    @FXML
    private void editBeadForPlate2Event(ActionEvent event) {
        if(curExperiment == -1)
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("choose an Experiment first!");                   
        }
        if(numProbeInput2.getText() == null || Integer.parseInt(numProbeInput2.getText()) <=0 ) 
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("invalid input for number of probe!");           
        }
        if(probes2 ==null || probes2.size() == 0)
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("please load probe first!");                
        }
        ModelForExperiments.getInstance().setCurPlate(2);
        loadAddBeadsPage();        
        curExperiment=ModelForExperiments.getInstance().getCurrentExperiment();    
        ObservableList<probeTableData> probesForPlate2 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 2); //selected data for the bead plate
        probeTable2.getItems().clear();
        probeTable2.getItems().addAll(probesForPlate2);
    }
    
    @FXML
    private void editBeadForPlate3Event(ActionEvent event) {
        if(curExperiment == -1)
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("choose an Experiment first!");                   
        }
        if(numProbeInput3.getText() == null || Integer.parseInt(numProbeInput3.getText()) <=0 ) 
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("invalid input for number of probe!");           
        }
        if(probes3 ==null || probes3.size() == 0)
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("please load probe first!");                
        }
        ModelForExperiments.getInstance().setCurPlate(3);
        loadAddBeadsPage();        
        curExperiment=ModelForExperiments.getInstance().getCurrentExperiment();    
        ObservableList<probeTableData> probesForPlate3 = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 3); //selected data for the bead plate
        probeTable3.getItems().clear();
        probeTable3.getItems().addAll(probesForPlate3);  
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



    @FXML
    private void loadProbe1Event(ActionEvent event) {       
        loadProbe1Helper();
    }
    
    private void loadProbe1Helper() {
        
        if(probes1.size()!=0)  // if not empty, clear previous data first. 
            probes1.clear();

        // if no map generate for current experiment probles, initiliaze it. 
            if(ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment) == null)
                ModelForExperiments.getInstance().initializeProbeListMap(curExperiment);
        //HashMap<Integer, ObservableList<probeTableData>> map = ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment);
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
                    probeTableData probe = probesToLoad1.get(i);
                    probes.add(probe);
                }
                ModelForExperiments.getInstance().addOneProbeListForPopulate(curExperiment,1,probes);
                HashMap<Integer, ObservableList<probeTableData>> probesListForCurExperiment = ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment); 
        
                probes1.addAll(ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 1));
                probeTable1.refresh();

            }
    }    

    @FXML
    private void loadProbe2Event(ActionEvent event) {
      loadProbe2Helper();
    }

    private void loadProbe2Helper() {
        if(probes2.size()!=0)  // if not empty, clear previous data first. 
            probes2.clear();

            // if no map generate for current experiment probles, initiliaze it. 
            if(ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment) == null)
                ModelForExperiments.getInstance().initializeProbeListMap(curExperiment);
        //HashMap<Integer, ObservableList<probeTableData>> map = ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment);
            // when no input or input is 0, error
            if(numProbeInput2.getText() == null || Integer.parseInt(numProbeInput2.getText()) == 0 )
            {
                ErrorMsg error = new ErrorMsg();
                error.showError("Please set up probe numbers before load probe!");
            }
            else // load # of probles base on user input 
            {
                ObservableList<probeTableData> probes = FXCollections.observableArrayList();
                for(int i = 0; i < Integer.parseInt(numProbeInput2.getText());i++ )
                {
                    probeTableData probe = probesToLoad2.get(i);
                    probes.add(probe);
                }
                ModelForExperiments.getInstance().addOneProbeListForPopulate(curExperiment,2,probes);
                probes2.addAll(ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 2));
                probeTable2.refresh();

            }
    }
    
    @FXML
    private void loadProbe3Event(ActionEvent event) {
        loadProbe3Helper();
    }

   private void loadProbe3Helper() {
        if(probes3.size()!=0)  // if not empty, clear previous data first. 
            probes3.clear();
            // if no map generate for current experiment probles, initiliaze it. 
            if(ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment) == null)
                ModelForExperiments.getInstance().initializeProbeListMap(curExperiment);
        //HashMap<Integer, ObservableList<probeTableData>> map = ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment);
            // when no input or input is 0, error
            if(numProbeInput3.getText() == null || Integer.parseInt(numProbeInput3.getText()) == 0 )
            {
                ErrorMsg error = new ErrorMsg();
                error.showError("Please set up probe numbers before load probe!");
            }
            else // load # of probles base on user input 
            {
                ObservableList<probeTableData> probes = FXCollections.observableArrayList();
                for(int i = 0; i < Integer.parseInt(numProbeInput3.getText());i++ )
                {
                    probeTableData probe = probesToLoad1.get(i);
                    probes.add(probe);
                }
                ModelForExperiments.getInstance().addOneProbeListForPopulate(curExperiment,3,probes);
                probes3.addAll(ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, 3));
                probeTable3.refresh();

            }


    }    
    //gather user inputs for bead plate1 form each text fileds 
   //save the userInputsForBeadPlate in to the list passed in
    private boolean getUserInputforPlate1(List<UserInputForBeadPlate> userInputsForBeadPlate) {
        String samples = numSampleInput1.getText();
        String reps = numReplicaInput1.getText();
        String probes = numProbeInput1.getText();

        if(hasErrorsForUserInput(samples, reps, probes)) return false; // if input valid return; 
        
        int numberOfSamples = Integer.parseInt(samples);
        int numberOfReps = Integer.parseInt(reps);
        int numberOfProbes =  Integer.parseInt(probes);  

        String names = sampleNamesInput1.getText();
        String[] nameList = names.split(",");
        List<String> probeList = new ArrayList<>();
        //combine user inputs and probeList into a UserInputForBeadPlate object
        userInputsForBeadPlate.add(new UserInputForBeadPlate(numberOfSamples, numberOfReps,names, nameList,
                numberOfProbes,probeList ));
        return true;
    }

    // check errors for user inputs and remind users
    private boolean hasErrorsForUserInput(String samples , String reps, String probes)
    {
        if(samples==null || samples.equals("") || reps==null || reps.equals("") || probes==null || probes.equals("") )
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("user input for Experiment incomplete! ");  
            return true;        
        }
        if(!samples.chars().allMatch( Character::isDigit ))
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("user input for Experiment is not correct format" + " number of samples is numbers only ");  
            return true;
        }
        if ( !reps.chars().allMatch( Character::isDigit ) )
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("user input for Experiment is not correct format" + " number of replicas is numbers only ");   
            return true;
        }
        if ( !probes.chars().allMatch( Character::isDigit ) )
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("user input for Experiment is not correct format" + " number of probes is numbers only ");    
            return true;
        }    
        int numberOfSamples = Integer.parseInt(samples);
        int numberOfReps = Integer.parseInt(reps);
        int numberOfProbes =  Integer.parseInt(probes);  
        //check whether input is valid 
        if(numberOfSamples * numberOfReps * numberOfProbes > 96)
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("user input for Experiment " + curExperiment + " bead plate one exceeds 96!");
            return true;
        }
        return false;
    }
        //gather user inputs for bead plate1 form each text fileds 
    private boolean getUserInputforPlate2(List<UserInputForBeadPlate> userInputsForBeadPlate) {
        String samples = numSampleInput2.getText();
        String reps = numReplicaInput2.getText();
        String probes = numProbeInput2.getText();

        if(hasErrorsForUserInput(samples, reps, probes)) return false; // if input valid return; 
        
        int numberOfSamples = Integer.parseInt(samples);
        int numberOfReps = Integer.parseInt(reps);
        int numberOfProbes =  Integer.parseInt(probes);  
        
        String names = sampleNamesInput2.getText();
        String[] nameList = names.split(",");
        List<String> probeList = new ArrayList<>();
        //combine user inputs and probeList into a UserInputForBeadPlate object
        userInputsForBeadPlate.add(new UserInputForBeadPlate(numberOfSamples, numberOfReps,names, nameList,
                numberOfProbes,probeList ));
        return true;
    }

    
    //gather user inputs for bead plate3 form each text fileds 
    private boolean getUserInputforPlate3(List<UserInputForBeadPlate> userInputsForBeadPlate) {
        String samples = numSampleInput3.getText();
        String reps = numReplicaInput3.getText();
        String probes = numProbeInput3.getText();

        if(hasErrorsForUserInput(samples, reps, probes)) return false; // if input valid return; 
        
        int numberOfSamples = Integer.parseInt(samples);
        int numberOfReps = Integer.parseInt(reps);
        int numberOfProbes =  Integer.parseInt(probes);  
        
        String names = sampleNamesInput3.getText();
        String[] nameList = names.split(",");
        List<String> probeList = new ArrayList<>();
        //combine user inputs and probeList into a UserInputForBeadPlate object
        userInputsForBeadPlate.add(new UserInputForBeadPlate(numberOfSamples, numberOfReps,names, nameList,
                numberOfProbes,probeList ));
        return true;
    }

    @FXML
    private void changeExperimentEvent(MouseEvent event) {
    }

    private void clearLayout1Event(ActionEvent event) {
        clearLayout();
    }

  

 
}










