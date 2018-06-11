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
import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
//import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author feiping
 */
public class FoldChangeController implements Initializable {

    @FXML
    private ScrollPane sp1;
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane platesGridPane;
    @FXML
    private ScrollPane spForFoldChange;
    int largestSamples = 0;
    int experiments = 0;
    private  HashMap<Integer, List<Integer>> mapOfSamplesNumbers = new HashMap<>();
    ObservableList<bead> analytes = FXCollections.observableArrayList();
    //List<List<Integer>> choosenSamples = new ArrayList<>(); 
    
    //ToggleGroup group = new ToggleGroup(); // For manage radio buttons for users to select. 
    List<RadioButton> radioButtons = new ArrayList<>();
    List<RadioButton> slectedRBs = new ArrayList<>();
    int plates = 0; // samlleset plate count for the two slected samples 
    int probes = 0; // samlleset probes for the two slected samples. 
    String sample1 ="";
    String sample2 = "";
    @FXML
    private Text sample1Name;
    @FXML
    private Text sample2Name;
    private double defaultCutOffValue = 0;
    @FXML
    private TextField cutOffValueBox;
    List<List<HashMap<Integer,Double>>> foldChangeMatrix ;
    String[] red = {" -fx-background-color: rgb(255, 235, 230);"," -fx-background-color: rgb(255, 214, 204);",
        " -fx-background-color: rgb(255, 133, 102);"," -fx-background-color: rgb(255, 92, 51);"};
    
    String[] blue = {" -fx-background-color: rgb(235, 235, 250);"," -fx-background-color: rgb(214,214,245);",
     " -fx-background-color: rgb(173,173,245);"," -fx-background-color: rgb(112,112,219);"};
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        experiments = ModelForExperiments.getInstance().getNumberOfExperiments();
        largestSamples =  ModelForExperiments.getInstance().getLargestSampleCount();
        mapOfSamplesNumbers = ModelForExperiments.getInstance().getMapOfSamplesNumbers();
        analytes = ModelForExperiments.getInstance().getAnalytes();

        
        tableRow(experiments);    
        tableCol(largestSamples);
        fillRadioButton();
        sp1.setContent(gridPane); // add scroll bars to the grid pane. 
        spForFoldChange.setContent(platesGridPane);
        

        
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
    private void fillRadioButton() {
        for(int i = 1; i <= experiments; i++)
        {
            for(int j = 1; j <=getLargestSampleCountForOneExperiment(i); j++)
            {
                //curSample = j;
                RadioButton btn = new RadioButton();
                btn.setText("");
                btn.setAlignment(Pos.CENTER);
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(btn.isSelected()){
                            if(slectedRBs.size()<2)
                            {
                                    slectedRBs.add(btn);
                                    //int row = gridPane.getRowIndex(btn);
                                    //int col = gridPane.getColumnIndex(btn);
                                    //choosenSamples.add(new ArrayList<Integer>(Arrays.asList(row,col)));
                                }
                            else
                            {
                                slectedRBs.get(0).setSelected(false); // set the 1st element unselected
                                slectedRBs.remove(0); // remove 1st element from the selected list
                                //choosenSamples.remove(0);
                                slectedRBs.add(btn);
                               // System.out.println("No more than two buttons! ");
                            }
                            
                            if(slectedRBs.size() ==2)
                                {
                                    defaultCutOffValue = Double.parseDouble(cutOffValueBox.getText());
                                    showFoldChange(defaultCutOffValue);
                                }
                        }
                    }


                });
                radioButtons.add(btn);
                gridPane.add(btn, j, i);
                GridPane.setMargin(btn, new Insets(10));
            }
        }
    }


    private int getLargestSampleCountForOneExperiment(int i ) {
        int res = 0;
        //initilizeMapOfSample(); 
        for(Integer n : mapOfSamplesNumbers.get(i))
        {
            res = Math.max(res, n);
        }
         return res;       
    }
    
    
    private void showFoldChange(double cutOff) {
        int  experimementPos1 = gridPane.getRowIndex(slectedRBs.get(0));
        int sampleIndex1 = gridPane.getColumnIndex(slectedRBs.get(0)) -1;
        int experimementPos2 = gridPane.getRowIndex(slectedRBs.get(1));
        int sampleIndex2 = gridPane.getColumnIndex(slectedRBs.get(1)) -1;
        sample1 = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().get(experimementPos1).get(0).getNames()[sampleIndex1];
        sample2 = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().get(experimementPos2).get(0).getNames()[sampleIndex2];
       // display sample name on the top of the table
       sample1Name.setText("Experiment " + Integer.toString(gridPane.getRowIndex(slectedRBs.get(0))) + " " + sample1 + ", ");
       sample2Name.setText("Experiment " + Integer.toString(gridPane.getRowIndex(slectedRBs.get(1))) + " " + sample2);
       
      foldChangeMatrix = calculateFoldChange(experimementPos1,sampleIndex1,experimementPos2,sampleIndex2);
      
      platesGridPane.getChildren().clear();
        
       

       
        //diaply analyte information on the 1st colomn
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
      //  int curExperiment = slectedRBs.get(0).
      //  HashMap<Integer, ObservableList<probeTableData>> probesListForCurExperiment = ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment); 
      //  int countsOfPlates = probesListForCurExperiment.size() -1; // initilize probelistForCurexperiment contains key=0 value, which is never used and is empty
        // display probes 
        int pos = 1; // for put plate 1/2 at the right position. 
        for(int i = 1; i <= plates; i++)
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
         
            ObservableList<probeTableData> probes = ModelForExperiments.getInstance().getProbeListForPopulate(experimementPos1, i);
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
                 displayFoldChangeforOneProbe(i, j, foldChangeMatrix.get(i-1).get(j),cutOff);
            }            
        }       
    }   
 

    //helper function to get fold change matrix
    private List<List<HashMap<Integer, Double>>> calculateFoldChange(int experimementPos1, int sampleIndex1, int experimementPos2, int sampleIndex2) {

       List<HashMap<Integer,Double>> mv1ForOnePlate = new ArrayList<>();
       List<HashMap<Integer,Double>>  mv2ForOnePlate  = new ArrayList<>();
       

       
      List<List<HashMap<Integer,Double>>> foldChange = new ArrayList<>();
      plates = Math.min(ModelForExperiments.getInstance().getMedianValueMatrix().get(experimementPos1).size(), 
              ModelForExperiments.getInstance().getMedianValueMatrix().get(experimementPos2).size()); //get the samller size of plates 
      probes = getSamllestProbes(experimementPos1,experimementPos2,plates);
       for(int i = 0; i < plates; i++)
       {
           List<HashMap<Integer,Double>> plate = new ArrayList<>();
           mv1ForOnePlate = ModelForExperiments.getInstance().getMedianValueMatrix().get(experimementPos1).get(i).get(sampleIndex1);
           mv2ForOnePlate = ModelForExperiments.getInstance().getMedianValueMatrix().get(experimementPos2).get(i).get(sampleIndex2);
           for(int k = 0; k< probes; k++)
           {
                HashMap<Integer,Double>  probe = new HashMap<>();
                for(int j = 0; j <analytes.size();j++)
                {
                    int regionNumber = analytes.get(j).getRegionNumber();
                    double mv1 = mv1ForOnePlate.get(k).get(regionNumber);
                    double mv2 = mv2ForOnePlate.get(k).get(regionNumber);                   
                    double fc = Math.log(mv1ForOnePlate.get(k).get(regionNumber)) / Math.log(2)  - Math.log(mv2ForOnePlate.get(k).get(regionNumber)) / Math.log(2);
                    probe.put(regionNumber, Math.round(fc*1000.0)/1000.0);
                }      
                plate.add(probe);
           }
           foldChange.add(plate);
       }
       return foldChange;
    }
   
    private int getSamllestProbes(int experimementPos1, int experimementPos2, int plates)
    {
         List<UserInputForBeadPlate> userInputs1 = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().get(experimementPos1);
         List<UserInputForBeadPlate> userInputs2 = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap().get(experimementPos2);
         int res = userInputs1.get(0).getNumOfProbes();
         for(int i  = 0 ; i <plates;i++)
         {
             res = Math.min(res, userInputs1.get(i).getNumOfProbes());
             res = Math.min(res, userInputs2.get(i).getNumOfProbes());
         }
        
        return res;
    }

    private void displayFoldChangeforOneProbe(int platePos, int probeIndex, HashMap<Integer, Double> data, double cutOff) {
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
            double foldChange = data.get(analytes.get(i-1).getRegionNumber());
            label.setText(Double.toString(foldChange));   
            colorCode(label,foldChange,cutOff);
            label.autosize();            
            platesGridPane.add(label,pos,i+1);
           // GridPane.setMargin(label, new Insets(0));
        }
    }

    private int getPreProbes(int platePos) {
        int preProbes = 0; 
        HashMap<Integer, ObservableList<probeTableData>> probesListForCurExperiment = ModelForExperiments.getInstance().getProbeMapForPopulate().get(gridPane.getRowIndex(slectedRBs.get(0)));             
        for(int i = 1; i< platePos;i++ ) // don't count the current plate
         {
             preProbes += probesListForCurExperiment.get(i).size();
         }            
        return preProbes;
    }

    // 0.25+ for different shades of red, -0.25- for different shades of blue. 
    private void colorCode(Label label, double foldChange, double cutOff) {
        // red color
        if(foldChange >= cutOff)
        {
            int times = (int) (foldChange/cutOff);
            if(foldChange%cutOff !=0)
                times++;
            if(times <= red.length)
                label.setStyle(red[times-1]);
            else
                label.setStyle(red[red.length-1]);
        }
        // blue color 
        else if (foldChange <= -cutOff)
        {
            int times = (int) (-foldChange/cutOff);
            if(-foldChange%cutOff !=0)
                times++;
            if(times <= blue.length)
                label.setStyle(blue[times-1]);
            else
                label.setStyle(blue[blue.length-1]);
            
        }
        else
            return;
            
        
    }

    @FXML
    private void changeCutOffValueEvent(ActionEvent event) {
        
        double cutOff = Double.parseDouble(cutOffValueBox.getText());
        showFoldChange(cutOff);
    }

    private void resetColorCodeForOneProbe(int platePos, int probeIndex, HashMap<Integer, Double> data) {
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
            double foldChange = data.get(analytes.get(i-1).getRegionNumber());
            Label label = (Label) platesGridPane.getChildren().get(i+1);
            colorCode(label,foldChange,defaultCutOffValue);
            label.autosize();            
            platesGridPane.add(label,pos,i+1);
           // GridPane.setMargin(label, new Insets(0));
        }
    }
    
}
