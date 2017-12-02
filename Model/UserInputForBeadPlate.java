/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author feiping
 * this class is created to store user inputs for each bead plates.
 */
public class UserInputForBeadPlate {
    private int  numberOfSamples;
    private  int numberOfReplicas;
    private String namesInput;
    private  String[] names;
    private  int numberOfProbes;
    private  List<String> probeList;

    
    public  UserInputForBeadPlate(int samples, int replicas, String namesInput, String[] names, 
            int probes, List<String> probeList)
    {
        super();
        this.numberOfSamples=samples;
        this.numberOfReplicas=replicas;
        this.namesInput=namesInput;
        this.names=names;
        this.numberOfProbes=probes;
        this.probeList=probeList;
                
    }
    
    public void updateProbe(String newProbe)
    {
        this.probeList.add(newProbe);       
    }
    
    public int getNumOfSamples()
    {
        return this.numberOfSamples;
    }
    
    public int getNumOfReplicas()
    {
        return this.numberOfReplicas;
    }
    public String getNameInput()
    {
        return this.namesInput;
    }
    public String[] getNames()
    {
        return this.names;
    }       
    
    public int getNumOfProbes()
    {
        return this.numberOfProbes;
    }
    
    public  List<String> getProbeList()
    {
        return this.probeList;
    }
    
    public void updateProbeList( List<String> probeList)
    {
        this.probeList=probeList;
    }
}
