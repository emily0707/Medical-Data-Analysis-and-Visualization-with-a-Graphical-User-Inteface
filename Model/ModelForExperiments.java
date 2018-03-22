package Model;


import Util.ErrorMsg;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.UserInputForBeadPlate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// to hold probe data on the homepage for each bead plates 
// pass and change data between homepage and add bead pages
/**
 *
 * @author feiping
 */
public class ModelForExperiments {
    
    //defalut probes to two types of experiment. put them in the model so that user can save the change for future experiments. 
    List<List<String>> ProbesForExperimentType1 = new ArrayList<>();
    //List<List<String>> defaultProbesForExperimentType2 = new ArrayList<>();
    
    
    private ObservableList<bead> analytes = FXCollections.observableArrayList();
    //initial probes list
    private ObservableList<probeTableData> probesToLoad = FXCollections.observableArrayList();
    // private final static Context instance = new Context();
    private final static ModelForExperiments instance = new ModelForExperiments();
    //probe data contains both bead class number(region number ) and analyte name.
    private HashMap<Integer, HashMap<Integer,  ObservableList<bead>>> data = new HashMap<>(); 
    
    //proble data contains only analyte name and counts. created to pupoluate into the homepage proble tables
    private HashMap<Integer, HashMap<Integer, ObservableList<probeTableData>>> probesListForPopulate = new HashMap<>();
    
    //user input data for each experiment 
     HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap= new HashMap<>();
    
    private int currentExperiment;
    private int currentPlate;
    private int numberOfExpriments; // total number of experiments
    private List<String> fileNames = new ArrayList<>(); // xml files upload by users
    private Map<Integer, List<String>> XMLFileMap = new HashMap<>(); // xml files for each experiemnt, experiment nubmer is key.
    private String  directory; //absolute  directory of xml files. 
    private ObservableList<Integer> experiments; // for choice box display # of experiments 
    
    /* Median value Matrix data strucuture explanation.
    for each experiment and sample, HashTable<Integer, List<SampleData>> (key: number of experiment; value: list of sample data)
                   sample1  sample2
    experiment 1
    experiment 2
    
    Sampe data : List<List<List<HashMap<Integer,Double>>>>
    for each sample data : List<PlateData>
    for each Platedata List<probeData>
    for probe data: HashTable<Integer, double> (key: analyte region number, value: median value for that analyte)
               plate 1                        plate2         
               probe1    probe2   probe3      probe1     probe2    probe3
    Analyte1 
    Analyte2
    */
    HashMap<Integer, List<List<List<HashMap<Integer,Double>>>>> meidanValueMatrix = new HashMap<>();
    
    // has orginal median value data for each wells. 
    //List<HashMap<Integer,Double>> contains orignal meidan value data of each wells 
    HashMap<Integer, List<List<List<List<HashMap<Integer,Double>>>>>> medianValueOriginalData = new HashMap<>(); 
    //HashMap<Integer,  List<HashMap<Integer, HashMap<Integer,  Double>>>> medianValueOriginalData = new HashMap<>(); 
  
     // pass analyte, curPlate, curProbe when user click one meidan value to open a pop up page.
    private bead curAnalyte;
    private int curPlate =0;
    private int curProbe =0;
    private int curSample = 0;
    private int numberOfSamples = 0;
    private String[] sampleNames;
    
    
    public static ModelForExperiments getInstance() {
        return instance;
    }

    // read data from probes1.txt and save it into the ProbesForExperimentType1 list. 
    public void initializeProbesForExperimentType1() throws FileNotFoundException
    {
            // pass the path to the file as a parameter
        File file =new File("C:\\Users\\feiping\\Documents\\NetBeansProjects\\JavaFXApplication1\\src\\Controller\\probes1.txt");
        Scanner sc = new Scanner(file); 
        while (sc.hasNextLine())
        {
            String str = sc.nextLine();
            List<String> probeList = Arrays.asList(str.split(","));
            ProbesForExperimentType1.add(probeList);
        }
        sc.close();
    }
    
    public List<List<String>>  getProbesForExperimentType1() throws FileNotFoundException
    {
        if(ProbesForExperimentType1.isEmpty())
            initializeProbesForExperimentType1();
        return ProbesForExperimentType1;
    }
            
    public void setDirectory(String  directory)
    {
        this.directory = directory;
    }
    
    public String  getDirectory()
    {
        return directory;
    }
    
    public void setAnalytes(ObservableList<bead> analytes)
    {
        this.analytes = analytes;
    }
    
    public ObservableList<bead>  getAnalytes()
    {
        return analytes;
    }
    
    public int getCurPlate()
    {
        return currentPlate;
    }
    
    public void setCurPlate(int plate)
    {
        currentPlate = plate;
    }
   public void setProbesForLoad(ObservableList<probeTableData> probesToLoad )
   {
       this.probesToLoad = probesToLoad;
   }
    public ObservableList<probeTableData>  getProbesForLoad( )
   {
       generateBeadsToLoad();
       return probesToLoad;
   }
   
    private void generateBeadsToLoad()
{
    probesToLoad.add(new probeTableData(1, "TCR"));
     probesToLoad.add(new probeTableData(2, "CD"));
      probesToLoad.add(new probeTableData(3, "LAT"));
       probesToLoad.add(new probeTableData(4, "ZAP"));
       probesToLoad.add(new probeTableData(5, "SLP"));


}
   public void swap(int experiment, int index1, int index2) {
        String s1 = XMLFileMap.get(experiment).get(index1);
        String s2 = XMLFileMap.get(experiment).get(index2);
        XMLFileMap.get(experiment).set(index2, s1);
        XMLFileMap.get(experiment).set(index1, s2);  
    }
   
   public void setUserInputsForBeadPlateMap( HashMap<Integer, List<UserInputForBeadPlate>> userInputsForBeadPlateMap)
   {
       this.userInputsForBeadPlateMap = userInputsForBeadPlateMap;
   }
      
   public void setUserInputsForOneExperiment( int experiment, List<UserInputForBeadPlate> input)
   {
       userInputsForBeadPlateMap.put(experiment, input);
   }
   
    public HashMap<Integer, List<UserInputForBeadPlate>>  getUserInputsForBeadPlateMap( )
   {
       return userInputsForBeadPlateMap;
   }
    

   
    public HashMap<Integer,  HashMap<Integer,ObservableList<bead>>> getData()
    {
        return data;
    }
    
    //initilize probe list map after user upload xml files or manually set up experiments. 
   public void initilizeProbeListForPopulate()
   {
       //if not empty clear it first. (for manually set up experiments)
       if(!probesListForPopulate.isEmpty()) 
           probesListForPopulate.clear();
       
       for(int i = 1; i <=numberOfExpriments; i++ )
       {
            HashMap<Integer, ObservableList<probeTableData>> probesList = new HashMap<>();
            for( int j =0; j < XMLFileMap.get(i).size();j++)
            {
                ObservableList<probeTableData> list =  FXCollections.observableArrayList();
                probesList.put(j, list);
            }
            probesListForPopulate.put(i,  probesList);           
       }
   }
    //set probles in to the hashtable that holds probes data for each experiment and each bead plate. 
    public void setProbes(int experiement, int beadPlate, ObservableList<bead> probes)
    {
        if(!data.containsKey(experiement))
        {
            data.put(experiement, new HashMap<Integer,ObservableList<bead>>());
        }
           ObservableList<bead> tobe =  FXCollections.observableArrayList(probes);
           //ObservableList<bead> experimentBeads = FXCollections.observableArrayList();
            data.get(experiement).put(beadPlate, tobe);           
    }
    public void setProbeListForPopulate(HashMap<Integer, HashMap<Integer, ObservableList<probeTableData>>> probeLists)
    {
        this.probesListForPopulate = probeLists;
    }
    
    public void setProbeListForOnePlate(int experiement, int beadPlate, ObservableList<probeTableData> probes)
    {
        probesListForPopulate.get(experiement).put(beadPlate,probes);
    }
    
    public HashMap<Integer, HashMap<Integer, ObservableList<probeTableData>>> getProbeListForPopulate()
    {
        return probesListForPopulate;
    }
    
    // this function is for test 
    /*
    public ObservableList<probeTableData> getProbesForOnePlate(int experiement, int beadPlate)
    {
        if(!probesListForPopulate.containsKey(experiement)) 
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("not contains the experiment " +  experiement + " information!");
            return null;
        }
        
        if(!probesListForPopulate.get(experiement).containsKey(beadPlate))
        {
            ErrorMsg error = new ErrorMsg();
            error.showError("bead Plate " +  beadPlate + " dose not exisit!");
            return null;
        }    
            return probesListForPopulate.get(experiement).get(beadPlate);

    }
    */
    
    //create probe container for each experiemnt
    public void addExperiement(int experiements)
    {
        for(int i = 0; i < experiements; i++)
        {
            data.put(i, new HashMap<Integer, ObservableList<bead>>() );
        }
    }

    public void setCurrentExperiment(Integer experiement) {
        currentExperiment = experiement;
    }
    
    public int getCurrentExperiment() {
         return currentExperiment;
    }
  
    
    //initialize the probe list for the experiment
    public void initializeProbeListMap(int experiment )
    {
        if(!probesListForPopulate.containsKey(experiment))
        {
             probesListForPopulate.put(experiment, new HashMap<Integer, ObservableList<probeTableData>>());
             List<String> s = XMLFileMap.get(experiment);
             int size = s.size();
             for(int i = 1; i <= XMLFileMap.get(experiment).size();i++)
             {
                 ObservableList<probeTableData> probes = FXCollections.observableArrayList();
                 probesListForPopulate.get(experiment).put(i, probes);
             }
        }
    }
    
    // add probe list to certain plate. 
    public void addOneProbeListForPopulate(int experiment, int plate, ObservableList<probeTableData> probes)
    {
        probesListForPopulate.get(experiment).put(plate, probes);
    }
    
    public  HashMap<Integer, HashMap<Integer, ObservableList<probeTableData>>> getProbeMapForPopulate()
    {
        return probesListForPopulate;
    }
    
    // get probe list for cetain plate. 
    public  ObservableList<probeTableData> getProbeListForPopulate(int experiment, int plate)
    {
        if(!probesListForPopulate.containsKey(experiment)  || !probesListForPopulate.get(experiment).containsKey(plate)) return null;
        return probesListForPopulate.get(experiment).get(plate);
    }
    

    // for all xmlfiles user uploaded
    public void setXMLFiles(List<String> list)
    {
        fileNames = list;
    }
    //get all xml files user uploaded
    public List<String> getXMLFiles()
    {
        return fileNames;
    }
    
    public void setNumberOfExperiments(int n)
    {
        numberOfExpriments = n;
    }
    public int getNumberOfExperiments()
    {
        return numberOfExpriments;
    }
    
    public void setExperiments( ObservableList<Integer> experiments)
    {
        this.experiments = experiments;
    }
    
    public  ObservableList<Integer>  getExperiments( )
    {
        return experiments;
    }
    //put xml files map in to model 
    public void setExperimentsMap(Map<Integer, List<String>> map)
    {
        this.XMLFileMap = map;
    }
    
    // replace xml file list for one experiment
    public void seXMLfileListForOneExperiment(int experiment, List<String> list)
    {
        XMLFileMap.put(experiment, list);

    }
    public Map<Integer, List<String>>  getExperimentsXMLFileMap( )
    {
        return XMLFileMap;
    }
 


    
    public HashMap<Integer, List<List<List<HashMap<Integer,Double>>>>>    getMedianValueMatrix()
    {
        return meidanValueMatrix;
    }
    
    public void setMedianValueMatrix(HashMap<Integer, List<List<List<HashMap<Integer,Double>>>>> meidanValueMatrix)
    {
        this.meidanValueMatrix = meidanValueMatrix;
    }   

    //experiment starts from 1, sampleIndex, PlateIndex, probeIndex start from 0
    public  void setOneProbeDataForMedianValue(int experimentPos, int plateIndex,  int sampleIndex,  HashMap<Integer,Double> meidanValueDataForOneProbe)
    {
        // when median value matrix is empty, initialize it. 
        if(meidanValueMatrix.isEmpty() || !meidanValueMatrix.containsKey(experimentPos) ) 
        {
            List<List<List<HashMap<Integer,Double>>>> experiment= new ArrayList<>();
            meidanValueMatrix.put(experimentPos, experiment);
        }
        if(meidanValueMatrix.get(experimentPos).isEmpty() || meidanValueMatrix.get(experimentPos).size() < (plateIndex+1))
        {
            List<List<HashMap<Integer,Double>>> plate = new ArrayList<>();
            meidanValueMatrix.get(experimentPos).add(plate);
        }
        if(meidanValueMatrix.get(experimentPos).get(plateIndex).isEmpty() || meidanValueMatrix.get(experimentPos).get(plateIndex).size() < (sampleIndex+1))
        {
            List<HashMap<Integer,Double>> sample = new ArrayList<>();
            meidanValueMatrix.get(experimentPos).get(plateIndex).add(sample);
        }
        //sample starts from 1. sampleIndex = sample -1;  //plate starts from 1. plateIndex = plate -1;
        meidanValueMatrix.get(experimentPos).get(plateIndex).get(sampleIndex).add(meidanValueDataForOneProbe);
    }   
    
    public HashMap<Integer,Double> getOneProbeDataForMedianValue(int experimentPos, int plateIndex,  int sampleIndex, int probeIndex)
    {
        return meidanValueMatrix.get(experimentPos).get(plateIndex).get(sampleIndex).get(probeIndex);
    }

    
    public HashMap<Integer, List<List<List<List<HashMap<Integer,Double>>>>>> getMedianValueOriginalData ()
    {
        return medianValueOriginalData;
    }
    
    public void setMedianValueOriginalData(HashMap<Integer, List<List<List<List<HashMap<Integer,Double>>>>>> medianValueOriginalData)
    {
        this.medianValueOriginalData = medianValueOriginalData;
    }   

    //experiment starts from 1, sampleIndex, PlateIndex, probeIndex start from 0
    public  void setOneProbeDataForMedianValueOriginalData(int experimentPos, int plateIndex,  int sampleIndex, 
                         List< HashMap<Integer,  Double>>  OriginalDataForOneProbe)
    {
        //    HashMap<Integer, List<List<List<List<HashMap<Integer,Double>>>>>> medianValueOriginalData = new HashMap<>(); 
        // when median value matrix is empty, initialize it. 
        if(medianValueOriginalData.isEmpty() || !medianValueOriginalData.containsKey(experimentPos))
        {
            List<List<List<List<HashMap<Integer,Double>>>>> experiment= new ArrayList<>();
            medianValueOriginalData.put(experimentPos, experiment);
        }
        if(medianValueOriginalData.get(experimentPos).isEmpty() || medianValueOriginalData.get(experimentPos).size() < (plateIndex+1))
        {
            List<List<List<HashMap<Integer,Double>>>> plate = new ArrayList<>();
            medianValueOriginalData.get(experimentPos).add(plate);
        }
        if(medianValueOriginalData.get(experimentPos).get(plateIndex).isEmpty() || 
                medianValueOriginalData.get(experimentPos).get(plateIndex).size() < (sampleIndex+1))
        {
            List<List<HashMap<Integer,Double>>> sample = new ArrayList<>();
            medianValueOriginalData.get(experimentPos).get(plateIndex).add(sample);
        }
        /*
        if(medianValueOriginalData.get(experimentPos).get(plateIndex).get(sampleIndex).isEmpty() ||
           medianValueOriginalData.get(experimentPos).get(plateIndex).get(sampleIndex).size() < (probeIndex +1 ) )
        {
            List<HashMap<Integer,Double>> probe = new ArrayList<>();
            medianValueOriginalData.get(experimentPos).get(plateIndex).get(sampleIndex).add(probe);
        }*/
        //sample starts from 1. sampleIndex = sample -1;  //plate starts from 1. plateIndex = plate -1;
        medianValueOriginalData.get(experimentPos).get(plateIndex).get(sampleIndex).add(OriginalDataForOneProbe);
    }   
    
    public List< HashMap<Integer,  Double>> getOneProbeDataForOrignalMedianValue(int experimentPos, int plateIndex,  int sampleIndex, int probeIndex)
    {
        return medianValueOriginalData.get(experimentPos).get(plateIndex).get(sampleIndex).get(probeIndex);
    }    
    
    public bead getCurAnalyte()
    {
        return curAnalyte;
    }
    
    public void setCurAnalyte(bead curAnalyte)
    {
        this.curAnalyte = curAnalyte;
    }

        
    public int getCurProbe()
    {
        return curProbe;
    }
    
    public void setCurProbe(int curProbe)
    {
        this.curProbe = curProbe;
    }

    public int getNumberOfSamples()
    {
        return numberOfSamples;
    }
    
    public void setNumberOfSamples(int numberOfSamples)
    {
        this.numberOfSamples = numberOfSamples;
    }
    
    public int getCurSample()
    {
        return curSample;
    }
    
    public void setCurSample(int curSample)
    {
        this.curSample = curSample;
    }
    public String[]  getSampleNames()
    {
        return sampleNames;
    }
    
    public void setSampleNames(String[] sampleNames)
    {
        this.sampleNames = sampleNames;
    }
        
}
