/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import static com.oracle.nio.BufferSecrets.instance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static jdk.nashorn.internal.objects.Global.instance;

/**
 *
 * @author feiping
 */
public class ModelForMedianValue {
     private  static ModelForMedianValue instance = new ModelForMedianValue();
    /*
    for each experiment and sample, HashTable<Integer, List<SampleData>> (key: number of experiment; value: list of sample data)
                   sample1  sample2
    experiment 1
    experiment 2
    
    Sampe data : List<List<List<HashMap<Integer,Double>>>>
    for each sample data : List<PlateData>
    for each Platedata List<probeData>
    for probe data: HashTable<Integer, double> (key: analyte, value: median value for that analyte)
               plate 1                        plate2         
               probe1    probe2   probe3      probe1     probe2    probe3
    Analyte1 
    Analyte2
    */
    HashMap<Integer, List<List<List<HashMap<Integer,Double>>>>> meidanValueMatrix = new HashMap<>();
    public static ModelForMedianValue getInstance() 
    {
         return instance;
    }
    
    public HashMap<Integer, List<List<List<HashMap<Integer,Double>>>>>   getMedianValueMatrix()
    {
        return meidanValueMatrix;
    }
    
    public void setMedianValueMatrix(HashMap<Integer, List<List<List<HashMap<Integer,Double>>>>>  meidanValueMatrix)
    {
        this.meidanValueMatrix = meidanValueMatrix;
    }
    
    //experiment starts from 1, sample starts from 1, plate starts from 1
    public void putOneProbeDataForOneExperiment(int experiment, int plate, int sample,   HashMap<Integer,Double> meidanValueDataForOneProbe)
    {
        if(!meidanValueMatrix.containsKey(experiment)) // when experiment data does not exisit, create one
        {
            List<List<List<HashMap<Integer,Double>>>> sampleList = new ArrayList<>();
            meidanValueMatrix.put(experiment, sampleList);
        }
        // when probe list data does not exisit, create one. 
        if(meidanValueMatrix.get(experiment).isEmpty() ||meidanValueMatrix.get(experiment).size() < plate ) 
        {
            List<List<HashMap<Integer,Double>>> plateList = new ArrayList<>();
            meidanValueMatrix.get(experiment).add(plateList);
        }
        // when probe list data does not exisit, creat one
        //plate starts from 1. plateIndex = plate -1;
        if(meidanValueMatrix.get(experiment).get(plate-1).isEmpty() || meidanValueMatrix.get(experiment).get(plate -1).size() < sample )
        {
            List<HashMap<Integer,Double>> probeList = new ArrayList<>();
            meidanValueMatrix.get(experiment).get(plate-1).add(probeList);
        }
        //sample starts from 1. sampleIndex = sample -1;
        meidanValueMatrix.get(experiment).get(sample -1).get(plate-1).add(meidanValueDataForOneProbe);
    }
            
}
