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
import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

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
    List<TextField> medianValueCellsList = new ArrayList<>(); // List to hold textfield in each gridpane cells. 
    private GridPane medianValueGridPane;
    @FXML
    private GridPane platesGridPane;
    @FXML
    private Button testClear;
    /**
     * Initializes the controlayoutCellsListller class.
     */
   @Override
    public void initialize(URL url, ResourceBundle rb) {
         
        experiments = ModelForExperiments.getInstance().getNumberOfExperiments();
        samples = getLargestSampleCountForAllExperiment();       
        //initilizeFakeData();
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
        TextField textField = new TextField();
        String s = "Experiment " + i;
        textField.setText(s);    
        //textField.setAlignment(Pos.CENTER);
        textField.autosize();
        gridPane.add(textField,0,i);
     }
    return gridPane;
    }

     //dynamically add cols for gridpane base on previous user input. 
    public  void tableCol(int col){
    for(int i=1; i<=col; i++){
        //set width for cells of the cols
        ColumnConstraints column = new ColumnConstraints(100);
        gridPane.getColumnConstraints().add(column);        
        TextField textField = new TextField();
        String s = "Sample " + i;
        textField.setText(s);    
        textField.autosize();
        //add them to the GridPane
        gridPane.add(textField,i,0);
        // margins are up to your preference
        GridPane.setMargin(textField, new Insets(5));
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
                GridPane.setMargin(btn, new Insets(25));
            }
        }
   
    }    
    
    //after user slecting a sample(radio button), display median value data in bottom table
    //structure: one grid pane for plate. each plate needs a grid pane. 
    private void showMedianValue() {
        // clear previous data if any 
        platesGridPane.getChildren().clear();
       // platesGridPane.gridLinesVisibleProperty().set(true);
        
        //diaply analyte information on the 1st colomn
        analytes = ModelForExperiments.getInstance().getAnalytes();
        for(int i = 1; i <= analytes.size();i++)
        {
            //set width for cells of the cols
            ColumnConstraints column = new ColumnConstraints(100);
            platesGridPane.getColumnConstraints().add(column);            
            TextField textField = new TextField();
            String s = analytes.get(i-1).getAnalyte();
            textField.setText(s);    
            textField.autosize();
            platesGridPane.add(textField,0,i+1);
           // GridPane.setMargin(textField, new Insets(0));
        }
 
        //display plate information on the 1st row & probes infor on the 2nd row
        HashMap<Integer, ObservableList<probeTableData>> probesListForCurExperiment = ModelForExperiments.getInstance().getProbeMapForPopulate().get(curExperiment); 
        int countsOfPlates = probesListForCurExperiment.size() -1; // initilize probelistForCurexperiment contains key=0 value, which is never used and is empty
        // display probes 
        int pos = 1;
        for(int i = 1; i <= countsOfPlates; i++)
        {
            //set width for cells of the cols
            ColumnConstraints column = new ColumnConstraints(100);
            platesGridPane.getColumnConstraints().add(column);                
            TextField textField = new TextField();
            String s = "Plate " + i;
            textField.setText(s);   
            textField.autosize();         
            //add them to the GridPane
            platesGridPane.add(textField,pos,0);
         
            ObservableList<probeTableData> probes = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, i);
            for(int j = 0 ; j < probes.size();j++)
            {
                platesGridPane.getColumnConstraints().add(column);    
                textField = new TextField();
                s = probes.get(j).getProbeForPlate();
                textField.setText(s);    
                textField.autosize();                          
                platesGridPane.add(textField,pos,1); //add them to the GridPane
                pos++;
            }            
        }       
    }
    
    
    //helper function: get cells in a grid pane so tha
    public void  getCells(GridPane gridPane)
    {
            for(Node currentNode : gridPane.getChildren())
            {
                if (currentNode instanceof TextField)
                {
                    medianValueCellsList.add((TextField)currentNode);
                    
                }
            }
       
    }
    
        
    //heleper function: clear the bead plate layout. However, it also clears grid lines. 
    private void clearMedianValueGridPaneLayout()
   {

           for(int j =0; j <medianValueCellsList.size(); j++)
           {
               medianValueCellsList.get(j).clear();
               medianValueCellsList.get(j).setStyle("-fx-background-color:white;");
           }
           
  
   }
    
    //find the col size for the table
    private int getLargestSampleCountForAllExperiment() {
        HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap = ModelForExperiments.getInstance().getUserInputsForBeadPlateMap();
        int res = 0;
        for(int i = 1; i <= userInputsForBeadPlateMap.size();i++)
        {
            List<Integer> samplesCount = new ArrayList<>();
            for(UserInputForBeadPlate input :userInputsForBeadPlateMap.get(i) )
            {
                int size = input.getNumOfSamples();
                samplesCount.add(input.getNumOfSamples());
                res = Math.max(res, input.getNumOfSamples());
            }
            mapOfSamples.put(i, samplesCount);
        }
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
    
    public void initilizeFakeData()
    {
        samples = 20;
        experiments = 5;
                List<Integer> samplesCount = new ArrayList<>();
        samplesCount.add(1);samplesCount.add(2);
        mapOfSamples.put(1, samplesCount);
         
          mapOfSamples.put(3, samplesCount);
           mapOfSamples.put(4, samplesCount);
            mapOfSamples.put(5, samplesCount);
             List<Integer> samplesCount1 = new ArrayList<>();
            samplesCount1.add(3);
            mapOfSamples.put(2, samplesCount1);
                    mapOfSamples.put(1, samplesCount);
         
          mapOfSamples.put(6, samplesCount);
           mapOfSamples.put(7, samplesCount);
            mapOfSamples.put(8, samplesCount);
            
    }

    @FXML
    private void testClearAction(ActionEvent event) {
        clearMedianValueGridPaneLayout();
    }

            
     
}


