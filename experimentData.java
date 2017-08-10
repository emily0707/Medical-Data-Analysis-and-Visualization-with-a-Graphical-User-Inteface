/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author feiping
 */
public class experimentData {
    private int  numberOfSamples;
    private  int numberOfReplicas;
    private  String[] names;
    private  int numberOfProbes;
    private  ObservableList<probeTableData> probeList;
    
    public  experimentData(int samples, int replicas, String[] names, 
            int probes, ObservableList<probeTableData> probeList)
    {
        super();
        this.numberOfSamples=samples;
        this.numberOfReplicas=replicas;
        this.names=names;
        this.numberOfProbes=probes;
        this.probeList=probeList;
                
    }
    
    public void updateProbe(probeTableData newProbe)
    {
        this.numberOfProbes++;
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
    
    public String[] getNames()
    {
        return this.names;
    }       
    
    public int getNumOfProbes()
    {
        return this.numberOfProbes;
    }
    
    public ObservableList<probeTableData>  getProbeList()
    {
        return this.probeList;
    }
    
    public void updateProbeList( ObservableList<probeTableData> newProbeList)
    {
        this.probeList=newProbeList;
    }
}
