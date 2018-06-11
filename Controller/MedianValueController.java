/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ModelForExperiments;
import Model.UserInputForBeadPlate;
import Model.bead;
import Model.probeTableData;
import Util.ErrorMsg;
import Util.StAXParser;
import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author feiping
 */
public class MedianValueController implements Initializable {

    @FXML
    private GridPane gridPane;

    HashMap<Integer, List<Integer>> mapOfSamples = new HashMap<>();// store the # of samples for each experiment and bead plate. 
    int samples = 0;
    int experiments = 0;
    int curSample =0;
    int curExperiment = 0;
    @FXML
    private ScrollPane sp1;

    @FXML
    private ScrollPane spForMedianValue;
    
    ToggleGroup group = new ToggleGroup(); // For manage radio buttons for users to select. 
    ObservableList<bead> analytes = FXCollections.observableArrayList();
    List<Label> medianValueCellsList = new ArrayList<>(); // List to hold textfield in each gridpane cells. 
    private GridPane medianValueGridPane;
    @FXML
    private GridPane platesGridPane;
    @FXML
    private Text sampleName;
    
    //info for pop up page
    private int analyteIndexForPopUpPage =0;
    private int plateIndexForPopUpPage =0;
    private int probeIndexForPopUpPage =0;
    String[] samplesNames ;
    
    /**
     * Initializes the controlayoutCellsListller class.
     */
   @Override
    public void initialize(URL url, ResourceBundle rb) {
         
        experiments = ModelForExperiments.getInstance().getNumberOfExperiments();
        samples = getLargestSampleCountForAllExperiment();  
       
        analytes = ModelForExperiments.getInstance().getAnalytes();
        calcaluateMedianValueMatrix();
        tableRow(experiments);    
        tableCol(samples);
        fillRadioButton();
        sp1.setContent(gridPane); // add scroll bars to the grid pane. 
        spForMedianValue.setContent(platesGridPane);
        //sp1.setFitToWidth(true);
        //gridPane.setHgrow(sp1, Priority.ALWAYS);


        // display right content and remove previous slection of radio button. 
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (group.getSelectedToggle() != null) 
                {
                    curExperiment = gridPane.getRowIndex((Node) newValue);
                    curSample = gridPane.getColumnIndex((Node) newValue);
                    ModelForExperiments.getInstance().setCurSample(curSample);
            
                    oldValue.setSelected(false);
                    showMedianValue();
                }
            }
        });   
        
    }    

    //dynamically add rows for gridpane base on previous user input. 
    public  GridPane tableRow(int rows){
    for(int i=1; i<=rows; i++){
        //set width for cells of the cols
        ColumnConstraints column = new ColumnConstraints(100);
        gridPane.getColumnConstraints().add(column);
        Label label = new Label();
        String s = "  Experiment " + i;
        label.setText(s);    
        //textField.setAlignment(Pos.CENTER);
        label.autosize();
        gridPane.add(label,0,i);
     }
    return gridPane;
    }

     //dynamically add cols for gridpane base on previous user input. 
    public  void tableCol(int col){
        
    for(int i=1; i<=col; i++){
        //set width for cells of the cols
        ColumnConstraints column = new ColumnConstraints(100);
        gridPane.getColumnConstraints().add(column);        
        Label label = new Label();
        String s = "Sample  " + i;
        label.setText(s);    
        label.autosize();
        //add them to the GridPane
        gridPane.add(label,i,0);
        // margins are up to your preference
        GridPane.setMargin(label, new Insets(5));
     }
    }    

    //create radio button for user to choose from. 
    private void fillRadioButton() {
        for(int i = 1; i <= experiments; i++)
        {
            for(int j = 1; j <=getLargestSampleCountForOneExperiment(i); j++)
            {
                curSample = j;
                RadioButton btn = new RadioButton();
                btn.setText("");
                btn.setAlignment(Pos.CENTER);
                if(group.getToggles().isEmpty())  // set the 1st button defalt to be selected 
                {
                    curExperiment = i; 
                    btn.setSelected(true);
                    showMedianValue();
                }
                btn.setToggleGroup(group);
                gridPane.add(btn, j, i);
                GridPane.setMargin(btn, new Insets(10));
            }
        }
   
    }    
    //after user slecting a sample(radio button), display median value data in bottom table
    //structure: one grid pane for plate. each plate needs a grid pane. 
    private void showMedianValue() {
        // clear previous data if any 
        platesGridPane.getChildren().clear();
       // platesGridPane.gridLinesVisibleProperty().set(true);
        
       // display sample name on the top of the table
      UserInputForBeadPlate input = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().get(curExperiment).get(0); // get the user input for 1st plate
      String[] names = input.getNames();
      sampleName.setText(names[curSample -1]); // show sample name for the median value table        
        //diaply analyte information on the 1st colomn
        analytes = ModelForExperiments.getInstance().getAnalytes();
        for(int i = 1; i <= analytes.size();i++)
        {
            //set width for cells of the cols
            ColumnConstraints column = new ColumnConstraints(70);
            platesGridPane.getColumnConstraints().add(column);            
            Label label = new Label();
            String s = analytes.get(i-1).getAnalyte() + "(" + analytes.get(i-1).getRegionNumber() + ")";
            label.setText(s);    
            label.autosize();
            platesGridPane.add(label,0,i+1);
        }
        

    //display plate information on the 1st row & probes infor on the 2nd row
        HashMap<Integer, ObservableList<probeTableData>> probesListForCurExperiment = ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment); 
        int countsOfPlates = probesListForCurExperiment.size() -1; // initilize probelistForCurexperiment contains key=0 value, which is never used and is empty
        // display probes 
        int pos = 1; // for put plate 1/2 at the right position. 
        for(int i = 1; i <= countsOfPlates; i++)
        {
            //set width for cells of the cols
            ColumnConstraints column = new ColumnConstraints(70);
            platesGridPane.getColumnConstraints().add(column);                
            Label label = new Label();
            String s = "Plate " + i;
            label.setText(s);   
            label.autosize();         
            //add them to the GridPane
            platesGridPane.add(label,pos,0);
         
            ObservableList<probeTableData> probes = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, i);
            for(int j = 0 ; j < probes.size();j++)
            {
                platesGridPane.getColumnConstraints().add(column);    
                Label label1  = new Label();
                s = probes.get(j).getProbeForPlate(); // get probe's name (analyte)
                label1.setText(s);    
                label1.autosize();                          
                platesGridPane.add(label1,pos,1); //add them to the GridPane
                pos++;                
                //showMedianValueDataInPlace(i,j,probes,medianValueOriginalData); 
                //getOneProbeDataForMedianValue(int experimentPos, int plateIndex,  int sampleIndex, int probeIndex)
                HashMap<Integer, Double> finalMedianValueForOneProbe  =  ModelForExperiments.getInstance().getOneProbeDataForMedianValue(curExperiment , i-1, curSample-1 , j);
                displayMedianValueforOneProbe(i, j, finalMedianValueForOneProbe);
            }            
        }       
    }   
 
    //helper function: calculate right well no in xml files. 
    // wells count in this project from col to col, wells count in xml files from row to row. 
    private int getWellNoInXmlFiles(int n)
    {
        int row  = n%8;
        int col = n/8 +1;
        if(row == 0) // the 8th row elements
        {
            row = 8;
            col = n/8;
        }        
        int res = (row -1)*12 + col;
        return res;
    }
    
    // if not platePos is not 1, it means it has previous probes of previous plates. 
    private int getPreProbes(int platePos)
    {
        int preProbes = 0; 
        HashMap<Integer, ObservableList<probeTableData>> probesListForCurExperiment = ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment);             
        for(int i = 1; i< platePos;i++ ) // don't count the current plate
         {
             preProbes += probesListForCurExperiment.get(i).size();
         }            
        return preProbes;
    }
    
    //Helper function : diaply meidan value in page for one probe
     //platePos starts from 1, probeIndex starts from 0. 
    private void displayMedianValueforOneProbe(int platePos, int probeIndex, HashMap<Integer, Double> finalMedianValueForOneProbe) {
        //calclulate the col number to populate median value of this probe  = previous plate + cur Platepos
        int preProbes = 0; // if not platePos is not 1, it means it has previous probes of previous plates. 
        if(platePos>1)
        {
           preProbes = getPreProbes(platePos);      
        }
        int pos = preProbes + (probeIndex +1); 
        
       // final TextField[] textfields = new TextField[analytes.size()]; 
        for(int i = 1; i <= analytes.size();i++)
        {
            //set width for cells of the cols
            ColumnConstraints column = new ColumnConstraints(70);
            platesGridPane.getColumnConstraints().add(column);            
            Label label = new Label();
            platesGridPane.setRowIndex(label, i);
            platesGridPane.setColumnIndex(label,probeIndex);
            double medianValue = finalMedianValueForOneProbe.get(analytes.get(i-1).getRegionNumber());
            label.setText(Double.toString(medianValue));   
            colorCode(label,medianValue);
            //set the following three index for pop up page
            //analyteIndex = i-1;
            //when users click the median value, pops up a new page to diaplay other information 
            label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    try {
                       
                        analyteIndexForPopUpPage = GridPane.getRowIndex( label) -2 ;
                        plateIndexForPopUpPage = platePos -1 ;
                        //probeIndexForPopUpPage = GridPane.getColumnIndex( label) -1 ;
                        int preProbes = getPreProbes(platePos);// for debug
                        probeIndexForPopUpPage = (GridPane.getColumnIndex( label) -1) - getPreProbes(platePos);

                        
                        displayAnalyteInOtherSamples();
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(MedianValueController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            label.autosize();            
            platesGridPane.add(label,pos,i+1);
           // GridPane.setMargin(label, new Insets(0));
        }
    }
        
    //helper function : calculate final median value for one Probe.
    private  HashMap<Integer, Double>  calculateMedianValue(List<HashMap<Integer, Double>> wellsForCalculate) {
      
        HashMap<Integer, Double> finalMedianValueForOneProbe = new HashMap<>();
        for(int i = 0; i < analytes.size();i++)
        {
            int regionNumber = analytes.get(i).getRegionNumber();       
            double sum = 0;
            //System.out.println("curEepriment is " + curExperiment + ". cur curSample is "  + curSample + ". cur RegionNumber is " + regionNumber);
            for(HashMap<Integer, Double> map : wellsForCalculate)
            {

                //double data = map.get(regionNumber);
                sum += map.get(regionNumber);
            }
            double finalMeidanValue = sum / wellsForCalculate.size();
            finalMedianValueForOneProbe.put(regionNumber, finalMeidanValue);
        }
        return finalMedianValueForOneProbe;
    }

    //helper function to use stax parser to get median value maps 
    // size = # of files/plates
    private  List<HashMap<Integer, HashMap<Integer,  Double>>> getMeidanValueOriginalData(int experimentPos) {
    String directory = ModelForExperiments.getInstance().getDirectory();
    List<String> files = ModelForExperiments.getInstance().getExperimentsXMLFileMap().get(experimentPos);
    List<String> absolutePath = new ArrayList<>();
    List<HashMap<Integer, HashMap<Integer,  Double>>> meidanValueData = new ArrayList<>();
    StAXParser parser = new StAXParser();
   // for(int i = files.size()-1 ; i >=0; i--) // the sequence should be backwards
    for(int i = 0 ; i < files.size(); i++) 
    {
       absolutePath.add(directory + "\\" + files.get(i) );   
     }
    
    for(int i = 0 ; i < absolutePath.size();i++) // 
    {
        meidanValueData.add(parser.getMedianValueData(absolutePath.get(i), experimentPos, i));
    }
     return meidanValueData;    
}    
    
    //helper function: get cells in a grid pane so tha
    public void  getCells(GridPane gridPane)
    {
            for(Node currentNode : gridPane.getChildren())
            {
                if (currentNode instanceof Label)
                {
                    medianValueCellsList.add((Label)currentNode);
                    
                }
            }
       
    }
    
        
    //heleper function: clear the bead plate layou 
    private void clearMedianValueGridPaneLayout()
   {

           for(int j =0; j <medianValueCellsList.size(); j++)
           {
               medianValueCellsList.get(j).setText("");
               medianValueCellsList.get(j).setStyle("-fx-background-color:white;");
           }
           
  
   }
    
    //find the col size for the table
    private int getLargestSampleCountForAllExperiment() {
        HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap();
        int res = 0;
        int iIndex = 0;
        int jIndex =0;
        for(int i = 1; i <= userInputsForBeadPlateMap.size();i++)
        {
            List<Integer> samplesCountForOneExperiment = new ArrayList<>();
             List<UserInputForBeadPlate> inputs =  userInputsForBeadPlateMap.get(i);
            for(int j = 0; j < inputs.size();j++)
            {
                UserInputForBeadPlate input = inputs.get(j);
                int size = input.getNumOfSamples();
                samplesCountForOneExperiment.add(input.getNumOfSamples());
                if(res < size)
                {
                    iIndex =  i;
                    jIndex =  j;
                    res = size;
                }
            }
            mapOfSamples.put(i, samplesCountForOneExperiment);
        }
        samplesNames = userInputsForBeadPlateMap.get(iIndex).get(jIndex).getNames(); 
        ModelForExperiments.getInstance().setLargestSampleCount(res);
        ModelForExperiments.getInstance().setMapOfSamplesNumbers(mapOfSamples);
        return res;
    }

    private int getLargestSampleCountForOneExperiment(int i ) {
        int res = 0;
        //initilizeMapOfSample(); 
        for(Integer n : mapOfSamples.get(i))
        {
            res = Math.max(res, n);
        }
         return res;       
    }
    


    // open pop up page to dispaly other values 
    // analytePos starts from 1, platePos starts from 1, probePos starts from 0. 
    private void displayAnalyteInOtherSamples() throws MalformedURLException {
        //bead analyteIndex = analytes.get(analytePos -1);
        ModelForExperiments.getInstance().setCurAnalyte(analytes.get(analyteIndexForPopUpPage));
        bead curan = ModelForExperiments.getInstance().getCurAnalyte(); // for debug 
        ModelForExperiments.getInstance().setCurPlate(plateIndexForPopUpPage); 
        ModelForExperiments.getInstance().setCurProbe(probeIndexForPopUpPage);
        ModelForExperiments.getInstance().setNumberOfSamples(samples);
        ModelForExperiments.getInstance().setSampleNames(samplesNames);
        // open up the pop up page 
        URL url = Paths.get("./src/View/PopUpMedianValue.fxml").toUri().toURL();
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
   
    }

    private void calcaluateMedianValueMatrix() {
         if(!ModelForExperiments.getInstance().getMedianValueMatrix().isEmpty()) return;
          
        for(int i = 1; i <=experiments;i++)
        {
            List<HashMap<Integer, HashMap<Integer,  Double>>> medianValueOriginalData = getMeidanValueOriginalData(i);
            
            HashMap<Integer, ObservableList<probeTableData>> probesListForCurExperiment = ModelForExperiments.getInstance().getProbeMapForPopulate().get(i); 
            List<UserInputForBeadPlate> inputs =  ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().get(i);
            int numberOfPlates = inputs.size();
            //System.out.println("experiment is " + i ); // for debug
            for(int j = 0; j < numberOfPlates; j++)
            {
                UserInputForBeadPlate input = inputs.get(j);
                int numberOfSamples  = input.getNumOfSamples();
                ObservableList<probeTableData> ProbesForOnePlate = probesListForCurExperiment.get(j+1);
                int numberOfProbes = input.getNumOfProbes();
                //System.out.println("plate Index is " + j ); // for debug
                for(int k = 0; k<numberOfSamples; k++ )
                {
                    //System.out.println("sample Index is " + k); 
                     for(int x = 0; x<numberOfProbes; x++)
                     {
                        //System.out.println("probe Index is " + x ); 
                        HashMap<Integer, Double> finalMedianValueForOneProbe  = getFinalMeidianValueForOneProbe(i,j,k,x, ProbesForOnePlate, medianValueOriginalData, input);
                        ModelForExperiments.getInstance().setOneProbeDataForMedianValue(i, j, k, finalMedianValueForOneProbe);
                        //for debug
                        HashMap<Integer, List<List<List<HashMap<Integer,Double>>>>> meidanValueMatrix =  ModelForExperiments.getInstance().getMedianValueMatrix();
                        List<List<List<HashMap<Integer,Double>>>> exper = meidanValueMatrix.get(i);
                        List<List<HashMap<Integer,Double>>> plat =exper.get(j);
                        List<HashMap<Integer,Double>> samp = plat.get(k);
                                
                     }
                }

            }

        }
    }

    private HashMap<Integer, Double> getFinalMeidianValueForOneProbe(int experimentPos, int plateIndex, int sampleIndex, int probeIndex, 
            ObservableList<probeTableData> ProbesForOnePlate, List<HashMap<Integer, HashMap<Integer, Double>>> medianValueOriginalData, 
            UserInputForBeadPlate userInput) {
        HashMap<Integer, HashMap<Integer,  Double>> dataMap = medianValueOriginalData.get(plateIndex);
        int numberOfSamples = userInput.getNumOfSamples();
        ModelForExperiments.getInstance().setNumberOfSamples(numberOfSamples);
        int numberOfReplicas = userInput.getNumOfReplicas();
        HashMap<Integer, Double> finalMedianValueForOneProbe  = new HashMap<>();// for  final median value data
        
        int probePos = probeIndex;
        int samplePos = sampleIndex+1;
        int prevWells = probePos *(numberOfSamples * numberOfReplicas); // previous wells to get right number of wells to start
        List< HashMap<Integer,  Double>>  wellsForCalculate = new ArrayList<>(); // wells that used for calcalute final meidan value 
        samplePos = prevWells + samplePos; //  well of sample
        int wellNo = getWellNoInXmlFiles(samplePos);
        wellsForCalculate.add(dataMap.get(wellNo));
        for(int j = 1; j < numberOfReplicas; j++)
        {
            int replicaPos = samplePos + j*numberOfSamples;
            wellNo = getWellNoInXmlFiles(replicaPos);
            wellsForCalculate.add(dataMap.get(wellNo)); // well of relicas 
        }
        
        ModelForExperiments.getInstance().setOneProbeDataForMedianValueOriginalData(experimentPos, plateIndex, sampleIndex, wellsForCalculate);
        finalMedianValueForOneProbe = calculateMedianValue(wellsForCalculate); // get final median value data 
        //System.out.println("curExperiment is " + curExperiment + ". curSample is " + curSample + ". cur plate is " + platePos + ". cur Probe is " + probePos);
        return finalMedianValueForOneProbe;
    }
    
    /*
    ii.	Gradually increase the shades of color 
1.	100-200
2.	200-1000
3.	1000-5000
4.	5000+

    */

    private void colorCode(Label label, double medianValue) {
        if(medianValue >100 && medianValue<200)
        {
            label.setStyle(" -fx-background-color: rgb(255,207,171);");
        }
        else if(medianValue >=200 && medianValue<1000)
        {
            label.setStyle(" -fx-background-color: rgb(252,137,172);");
        }
        else if(medianValue >=1000 && medianValue<5000)
        {
            label.setStyle(" -fx-background-color: rgb(238,32,77);");
        }
        else if(medianValue >=5000)
        {
            label.setStyle(" -fx-background-color: rgb(202,55,103);");
        }
    }
}



            
     
