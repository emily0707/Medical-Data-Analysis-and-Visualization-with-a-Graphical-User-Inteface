package Model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// to hold probe data on the homepage for each bead plates 
/**
 *
 * @author feiping
 */
public class ModelForProbeTabe {
    
    //private HashMap<Integer, List<bead> probes> dat;a;
    
    // private final static Context instance = new Context();
    private final static ModelForProbeTabe instance = new ModelForProbeTabe();
    //probe data contains both bead class number(region number ) and analyte name.
    private HashMap<Integer, HashMap<Integer,  ObservableList<bead>>> data = new HashMap<>(); 
    //proble data contains only analyte name and counts. created to pupoluate into the homepage proble tables
    private HashMap<Integer, HashMap<Integer, ObservableList<probeTableData>>> probesListForPopulate = new HashMap<>();
    private int currentExperiment;
    private int currentPlate;
    
    public static ModelForProbeTabe getInstance() {
        return instance;
    }
    
    public HashMap<Integer,  HashMap<Integer,ObservableList<bead>>> getData()
    {
        return data;
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
    
    public ObservableList<bead> getProbes(int experiement, int beadPlate)
    {
        if(!data.containsKey(experiement)) 
        {
            System.out.println("not contains the experiment!");
             return null;
        }
        
        if(!data.get(experiement).containsKey(beadPlate))
        {
           
            System.out.println("bead pate dose not exisit!");
             return null;
        }    
            return data.get(experiement).get(beadPlate);

    }
    
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
    
    public void setCurrentPlate(int plate){
        currentPlate=plate;
    }

    public int getCurrentPate() {
         return currentPlate;
    }
    
    //put data into the display list
    public void addProbeListForPopulate(int experiment, int plate, ObservableList<probeTableData> probes)
    {
        if(!probesListForPopulate.containsKey(experiment))
        {
             probesListForPopulate.put(experiment, new HashMap<Integer, ObservableList<probeTableData>>());
        }
        probesListForPopulate.get(experiment).put(plate, probes);    
    }
    
    public  ObservableList<probeTableData> getProbeListForPopulate(int experiment, int plate)
    {
        if(!probesListForPopulate.containsKey(experiment)  || !probesListForPopulate.get(experiment).containsKey(plate)) return null;
        return probesListForPopulate.get(experiment).get(plate);
    }
            
}
