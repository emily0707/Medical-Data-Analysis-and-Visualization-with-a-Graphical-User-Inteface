/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;

/**
 *
 * @author feiping
 */
// to hold xml files data on the homepage that uploaded by users
// pass and change data between the homepage and the set up experiment page
public class ModelForXMLFiles {
    private final static ModelForXMLFiles instance = new ModelForXMLFiles();
    private int numberOfExpriments; // total number of experiments
    private List<String> fileNames = new ArrayList<>(); // xml files upload by users
    private Map<Integer, List<String>> map = new HashMap<>(); // xml files for each experiemnt, experiment nubmer is key. 
    private ObservableList<Integer> experiments; // for choice box display # of experiments
    
    
    public static ModelForXMLFiles getInstance() {
        return instance;
    }
    public void setXMLFiles(List<String> list)
    {
        fileNames = list;
    }
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
    
    public void setExperimentsMap(Map<Integer, List<String>> map)
    {
        this.map = map;
    }
    public Map<Integer, List<String>>  getExperimentsMap( )
    {
        return map;
    }
    
}
