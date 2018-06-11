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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author feiping
 */
public class ANCController implements Initializable {

    @FXML
    private ScrollPane spForSamples;
    @FXML
    private GridPane gpForSamples;
    @FXML
    private ScrollPane spForANC;
    @FXML
    private GridPane gpForANC;
    int largestSamples = 0;
    int experiments = 0;
    private  HashMap<Integer, List<Integer>> mapOfSamplesNumbers = new HashMap<>();
    ObservableList<bead> analytes = FXCollections.observableArrayList();
    int curSample =0;
    int curExperiment = 0;
    ToggleGroup group = new ToggleGroup(); // For manage radio buttons for users to select. 
    @FXML
    private Text sampleName;
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
        calcaluateANCMatrix();
        fillRadioButton();
        spForSamples.setContent(gpForSamples); // add scroll bars to the grid pane. 
        spForANC.setContent(gpForANC);
        
        // display right content and remove previous slection of radio button. 
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (group.getSelectedToggle() != null) 
                {
                    curExperiment = gpForSamples.getRowIndex((Node) newValue);
                    curSample = gpForSamples.getColumnIndex((Node) newValue);
                    ModelForExperiments.getInstance().setCurSample(curSample);
            
                    oldValue.setSelected(false);
                    showANC();
                }
            }
        });   
        
    }    
       //dynamically add rows for gridpane base on previous user input. 
    public  GridPane tableRow(int rows){
    for(int i=1; i<=rows; i++){
        //set width for cells of the cols
        ColumnConstraints column = new ColumnConstraints(100);
        gpForSamples.getColumnConstraints().add(column);
        Label label = new Label();
        String s = "  Experiment " + i;
        label.setText(s);    
        //textField.setAlignment(Pos.CENTER);
        label.autosize();
        gpForSamples.add(label,0,i);
     }
    return gpForSamples;
    }

     //dynamically add cols for gridpane base on previous user input. 
    public  void tableCol(int col){
        
    for(int i=1; i<=col; i++){
        //set width for cells of the cols
        ColumnConstraints column = new ColumnConstraints(100);
        gpForSamples.getColumnConstraints().add(column);        
        Label label = new Label();
        String s = "Sample  " + i;
        label.setText(s);    
        label.autosize();
        //add them to the GridPane
        gpForSamples.add(label,i,0);
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
                    showANC();
                }
                btn.setToggleGroup(group);
                gpForSamples.add(btn, j, i);
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

    private void showANC() {
        // clear previous data if any 
        gpForANC.getChildren().clear();
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
            gpForANC.getColumnConstraints().add(column);            
            Label label = new Label();
            String s = analytes.get(i-1).getAnalyte() + "(" + analytes.get(i-1).getRegionNumber() + ")";
            label.setText(s);    
            label.autosize();
            gpForANC.add(label,0,i+1);
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
            gpForANC.getColumnConstraints().add(column);                
            Label label = new Label();
            String s = "Plate " + i;
            label.setText(s);   
            label.autosize();         
            //add them to the GridPane
            gpForANC.add(label,pos,0);
         
            ObservableList<probeTableData> probes = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, i);
            for(int j = 0 ; j < probes.size();j++)
            {
                gpForANC.getColumnConstraints().add(column);    
                Label label1  = new Label();
                s = probes.get(j).getProbeForPlate(); // get probe's name (analyte)
                label1.setText(s);    
                label1.autosize();                          
                gpForANC.add(label1,pos,1); //add them to the GridPane
                pos++;                
                //showMedianValueDataInPlace(i,j,probes,medianValueOriginalData); 
                //getOneProbeDataForMedianValue(int experimentPos, int plateIndex,  int sampleIndex, int probeIndex)
                HashMap<Integer, Double> ANCForOneProbe  =  ModelForExperiments.getInstance().getOneProbeDataForANC(curExperiment , i-1, curSample-1 , j);
                displayANCforOneProbe(i, j, ANCForOneProbe);
            }            
        }    
    }

    private void calcaluateANCMatrix() {
        
       if(!ModelForExperiments.getInstance().getANCMatrix().isEmpty()) return;
       
       HashMap<Integer, List< HashMap<Integer, HashMap<Integer,   List<Integer>>>>>   orginalXMLData = ModelForExperiments.getInstance().getOriginalXMLData();
        for(int i = 1; i <=experiments;i++)
        {
            List< HashMap<Integer, HashMap<Integer,   List<Integer>>>> orignalDataForOneExperiment = orginalXMLData.get(i); 
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
                        HashMap<Integer, Double> finalANCForOneProbe  = getANCForOneProbe(i,j,k,x, ProbesForOnePlate, orignalDataForOneExperiment, input);
                        ModelForExperiments.getInstance().setOneProbeDataForANC(i, j, k, finalANCForOneProbe);
  
                     }
                }

            }

        }
        
        
    }
 
    private HashMap<Integer, Double> getANCForOneProbe(int experimentPos, int plateIndex, int sampleIndex, int probeIndex, 
        ObservableList<probeTableData> ProbesForOnePlate,  List< HashMap<Integer, HashMap<Integer,   List<Integer>>>> orignalDataForOneExperiment, 
        UserInputForBeadPlate userInput) {
         HashMap<Integer, HashMap<Integer,   List<Integer>>> dataForOnePlate = orignalDataForOneExperiment.get(plateIndex);
        int numberOfSamples = userInput.getNumOfSamples();
        int numberOfReplicas = userInput.getNumOfReplicas();
        
       // System.out.println("experiment pos is " + experimentPos + ". plateIndex is  " +plateIndex + ". sampleIndex is " +sampleIndex +". probeIndex is " +  probeIndex);
        int probePos = probeIndex;
        int samplePos = sampleIndex+1;
        int prevWells = probePos *(numberOfSamples * numberOfReplicas); // previous wells to get right number of wells to start
        samplePos = prevWells + samplePos; //  well of sample
        int wellNo = getWellNoInXmlFiles(samplePos);
       // System.out.println("sample wellno is " +wellNo );
        HashMap<Integer,List<Integer>> sampleData = dataForOnePlate.get(wellNo);
        

        for(int j = 1; j < numberOfReplicas; j++)
        {
            int replicaPos = samplePos + j*numberOfSamples;
            wellNo = getWellNoInXmlFiles(replicaPos);
           // wellsForCalculate.add(dataMap.get(wellNo)); // well of relicas 
        }
        //System.out.println("replica wellno is " +wellNo );
        HashMap<Integer,List<Integer>> replicaData = dataForOnePlate.get(wellNo);
        
        HashMap<Integer, Double> ANCForOneProbe  = PValue(sampleData,replicaData);
        
       // ModelForExperiments.getInstance().setOneProbeDataForMedianValueOriginalData(experimentPos, plateIndex, sampleIndex, wellsForCalculate);
        //finalMedianValueForOneProbe = calculateMedianValue(wellsForCalculate); // get final median value data 
        //System.out.println("curExperiment is " + curExperiment + ". curSample is " + curSample + ". cur plate is " + platePos + ". cur Probe is " + probePos);
       // return finalMedianValueForOneProbe;
       return ANCForOneProbe;
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

    private HashMap<Integer, Double>  PValue(HashMap<Integer, List<Integer>> sampleData, HashMap<Integer, List<Integer>> replicaData) {
         HashMap<Integer, Double> res = new HashMap<>();
         for(int i = 0; i <analytes.size();i++)
         {
             int key = analytes.get(i).getRegionNumber();
             List<Double> sample = convertToDouble(sampleData.get(key));
             /*System.out.println("sample data for analyte  " + key);
             for(int j = 0; j < sample.size();j++) //debug
             {
                 System.out.println(sample.get(j));
             } */
             List<Double> replica = convertToDouble(replicaData.get(key));
             /*
             System.out.println("replica data for analyte  " + key);//debug
             for(int j = 0; j < replica.size();j++) //debug
             {
                 System.out.println(replica.get(j));
             }*/
              
             double pValue = getPValue( sample,  replica);
             res.put(key, pValue);
         }
         return res;
    }

    private List<Double> convertToDouble(List<Integer> get) {
        List<Double> res = new ArrayList<>();
        for(int n : get)
            res.add((double)n);
        return res;
    }

    //x1: sample, x2: replica
    private double getPValue(List<Double> x1, List<Double> x2) {
        List<Double> binEdges = getBinEdges(x1,x2);
        List<Double> binCounts1 = Histc(x1,binEdges,1);
        List<Double> binCounts2 = Histc(x2,binEdges,1);
        List<Double> sumCounts1 = getSumCounts(binCounts1);
        List<Double> sumCounts2 = getSumCounts(binCounts2);
        List<Double> deltaCDF = getDeltaCDF(sumCounts1 ,sumCounts2);
        double KSstatistic = Collections.max(deltaCDF);
        
        double lambda = getLambda(x1,x2,KSstatistic);
        double pValue = calPValue(lambda);
        pValue = Math.round(pValue*10000)/10000.0d;
        //System.out.println("PValue is : " + pValue);
        return pValue;
    }

    private List<Double> getBinEdges(List<Double> x1, List<Double> x2) {
        List<Double> res = new ArrayList<>();
        List<Double> merge = new ArrayList<>();
        merge.addAll(x1);
        merge.addAll(x2);
        Collections.sort(merge);
       res.add(Double.NEGATIVE_INFINITY);
       res.addAll(merge);
       res.add(Double.POSITIVE_INFINITY);
       return res;
    }

    private List<Double> Histc(List<Double> x, List<Double> binEdges, int n) {
        List<Double> res = new ArrayList<>();
        Collections.sort(x);
        
        double count = 0; // how many cur in x
        double cur = -1; // set cur to cur val if x contains multiple cur val. 
        for(int i = 0; i < binEdges.size(); i++)
        {
            if(cur != -1 )
            {
                if(i+1 < binEdges.size()  && binEdges.get(i+1)==cur)
                    res.add(0.0);
                else
                {
                    res.add(count);
                    count = 0;
                    cur = -1;
                }
            }
            else
            {
                double curVal= binEdges.get(i);
                if(x.contains(binEdges.get(i)))
                {
                    count = countVal(x,curVal);
                    if(countVal(binEdges,curVal) == 1)
                    {
                        res.add(count);
                    }
                    else
                    {
                        res.add(0.0);
                        cur = curVal;
                    }
                }
                else
                    res.add(0.0);

            }
        }
        return res;
    }

    private List<Double> getSumCounts(List<Double> x) {
           List<Double> res = new ArrayList<>();
           List<Double> cumsum = new ArrayList<>();
           
           //get cumsum
           double sum = 0;
           for(int i = 0; i <x.size();i++)
           {
               sum += x.get(i);
               cumsum.add(sum);
           }
           
           ////cumsum(binCounts1)./sum(binCounts1);
           for(int i = 0; i< x.size();i++)
           {
               double val = cumsum.get(i) / sum;
               res.add(val);
           }
           return res;
    }

    private List<Double> getDeltaCDF(List<Double> sumCounts1, List<Double> sumCounts2) {
          List<Double> res = new ArrayList<>();
          //calculate base on sampleCDFs
          for(int i = 0; i <sumCounts1.size()-1; i++)
          {
              res.add(Math.abs(sumCounts1.get(i)-sumCounts2.get(i)));
          }
          return res;
    }

    private double getLambda(List<Double> x1, List<Double> x2, double KSstatistic) {
         double n1 = x1.size();
         double n2 = x2.size();
         double n = (n1 * n2) / (n1 + n2);
         double sqrt = Math.sqrt(n) ;
         double ll =  (Math.sqrt(n) + 0.12 + 0.11/Math.sqrt(n))*KSstatistic;
         double lambda = Math.max((Math.sqrt(n) + 0.12 + 0.11/Math.sqrt(n))*KSstatistic, 0.0);
         return lambda;
    }

    private double calPValue(double lambda) {
        //j.^2
         List<Double> j = new ArrayList<>();
         List<Double> toBeExp = new ArrayList<>();
         List<Double> exp = new ArrayList<>(); //exp(-2*lambda*lambda*j.^2));
         List<Double> negJmenusOne = new ArrayList<>();
         List<Double> toBeSum = new ArrayList<>();
         
        double sum =0.0;
        for(double i = 1; i<=101; i++ )
        {
            j.add(i);
            toBeExp.add((-2)*lambda*lambda* (i*i));
            exp.add(Math.exp(toBeExp.get((int) (i-1))));
            
            //(-1).^(j-1)
            if((i-1)%2 == 0)
                negJmenusOne.add(1.0);
            else
                negJmenusOne.add(-1.0);
            
            //(-1).^(j-1).*exp(-2*lambda*lambda*j.^2)
            toBeSum.add(negJmenusOne.get((int) (i-1)) * exp.get((int) (i-1)));
            sum += toBeSum.get((int) (i-1));
        }
        double p = Math.min(Math.max(2*sum, 0), 1);
        return p;
    }

    private double countVal(List<Double> x, double n) {
        double count =1;
        int lastIndex = x.lastIndexOf(n);
        while( lastIndex!=0 && x.get(lastIndex-1) == n )
        {
            lastIndex--;
            count++;
        }
        return count;
    }

    private void displayANCforOneProbe(int platePos, int probeIndex, HashMap<Integer, Double> ANCForOneProbe) {
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
            gpForANC.getColumnConstraints().add(column);            
            Label label = new Label();
            gpForANC.setRowIndex(label, i);
            gpForANC.setColumnIndex(label,probeIndex);
            double ANC = ANCForOneProbe.get(analytes.get(i-1).getRegionNumber());
            label.setText(Double.toString(ANC));   
            colorCode(label,ANC);

            label.autosize();            
            gpForANC.add(label,pos,i+1);
           // GridPane.setMargin(label, new Insets(0));
        }
    }

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

    private void colorCode(Label label, double pVal) {
        // red color for pval<0.05
        double cutOff = 0.05;
        if(pVal < cutOff && pVal!=0)
        {
            int times = (int) (cutOff/pVal);
            if(times <= red.length)
                label.setStyle(red[times-1]);
            else
                label.setStyle(red[red.length-1]);
        }
        
         // blue color for pval>0.2
        double cutOff2 = 0.2;
        if(pVal > cutOff2)
        {
            int times = (int) ( pVal/ cutOff2);
            if(times <= blue.length)
                label.setStyle(blue[times-1]);
            else
                label.setStyle(blue[blue.length-1]);
        }
    }
    
}
