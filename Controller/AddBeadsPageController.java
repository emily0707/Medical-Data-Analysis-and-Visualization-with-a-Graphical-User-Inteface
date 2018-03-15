/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Util.StAXParser;
import Model.ModelForExperiments;
import Model.bead;
import Model.ModelForProbeTabe;
import Model.probeTableData;
import Util.ErrorMsg;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author feiping
 */
//controller for beads editing pop-up page.
public class AddBeadsPageController implements Initializable {

    
    @FXML
    private TableView<probeTableData> probeTable;
    @FXML
    private TableColumn<probeTableData, Integer> probeNoCol;
    @FXML
    private TableColumn<probeTableData, String> probeNameCol;
    ObservableList<probeTableData> probes = FXCollections.observableArrayList(); // list to holds all probes 
    
    @FXML
    private Button addBeadsBtn;    
    @FXML
    private TableView<probeTableData> experimentProbeTable;
    @FXML
    private TableColumn<probeTableData, Integer> experimentProbeNoCol;
    @FXML
    private TableColumn<probeTableData, String> experimentProbeNameCol;    
    public  ObservableList<probeTableData> slectedProbes = FXCollections.observableArrayList(); //list for selected probes


    @FXML
    private Button Delete;
    @FXML
    private Button moveUpBtn;
    @FXML
    private Button moveDownBtn;
    
    int curExperiment;
    int curPlate;
    int probeSize;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        probes = ModelForExperiments.getInstance().getProbesForLoad();

        probeNoCol.setCellValueFactory(new PropertyValueFactory<probeTableData,Integer>("probeCount"));
        probeNameCol.setCellValueFactory(new PropertyValueFactory<probeTableData,String>("probeForPlate"));
        probeTable.setItems(probes);
    
        // set experiement bead data into experiement bead table
        curExperiment = ModelForExperiments.getInstance().getCurrentExperiment();
        curPlate = ModelForExperiments.getInstance().getCurPlate();
        slectedProbes = ModelForExperiments.getInstance().getProbeListForPopulate(curExperiment, curPlate);
        probeSize = slectedProbes.size();
        experimentProbeNoCol.setCellValueFactory(new PropertyValueFactory<probeTableData,Integer>("probeCount"));
        experimentProbeNameCol.setCellValueFactory(new PropertyValueFactory<probeTableData,String>("probeForPlate"));
        experimentProbeTable.setItems(slectedProbes);
        
    }    

    @FXML
    private void addProbeAction(ActionEvent event) {
        probeTableData probeSelected = probeTable.getSelectionModel().getSelectedItem();
        String probe = probeSelected.getProbeForPlate();
       if(probeAlreadyChoosen(probe))
       {
            ErrorMsg error = new ErrorMsg();
            error.showError("proble " + probeSelected.getProbeForPlate() + " already choosen!");           
       }
       if(slectedProbes.size()>= probeSize)
       {
            ErrorMsg error = new ErrorMsg();
            error.showError("the Max probe allowed for experiment " + curExperiment + " plate" + curPlate + " is " + probeSize);
       }  
       else
       {
            //get user slected data and add it into the experiement beads so that it shows on the add bead page 
            int index = slectedProbes.size();
            probeTableData newProbe = new probeTableData(index+1, probe);
            slectedProbes.add(newProbe);
            ModelForExperiments.getInstance().setProbeListForOnePlate(curExperiment, curPlate, slectedProbes);         
       }

    }
    
    // helper function to check if probe already choosen 
    private boolean probeAlreadyChoosen(String probe )
    {
        for(probeTableData data :slectedProbes )
        {
            if(data.getProbeForPlate() == probe)
                return true;
        }
        return false;
    }

    @FXML
    private void deleteEvent(ActionEvent event) {
        probeTableData probeSelected = experimentProbeTable.getSelectionModel().getSelectedItem();
        // if the deleted probe is not the last one, need to other elements up. 
        int count = probeSelected.getProbeCount() ;
        if(count != slectedProbes.size()-1)
        {
            //move probes under the slected probe up one by one. 
            for(int i = count+1; i < slectedProbes.size();i++)
            {
                String nextProbe = slectedProbes.get(i).getProbeForPlate();
                slectedProbes.get(i-1).setProbeForPlate(nextProbe);
            }
        }
        //delete the last one
        slectedProbes.remove(slectedProbes.size()-1);      
        ModelForExperiments.getInstance().setProbeListForOnePlate(curExperiment, curPlate, slectedProbes);  
    }

    @FXML
    private void moveUpEvent(ActionEvent event) {
        probeTableData probeSelected = experimentProbeTable.getSelectionModel().getSelectedItem();
        int index = probeSelected.getProbeCount() - 1 ;
          if(index > 0) 
          {
                swap(slectedProbes, index, index - 1);
                ModelForExperiments.getInstance().setProbeListForOnePlate(curExperiment, curPlate, slectedProbes);   
          }
          else // if the selected item is the top item, no move
          {
              ErrorMsg error = new ErrorMsg();
              error.showError("can not move up the top item!");
          }
          
          
          experimentProbeTable.refresh();    
          
          
    }
    private void swap(ObservableList<probeTableData> slectedProbes, int j, int i) {
        String s1 = slectedProbes.get(i).getProbeForPlate();
        String s2 = slectedProbes.get(j).getProbeForPlate();
        slectedProbes.get(i).setProbeForPlate(s2);
        slectedProbes.get(j).setProbeForPlate(s1);
    }
    
    @FXML
    private void moveDownEvent(ActionEvent event) {
        probeTableData probeSelected = experimentProbeTable.getSelectionModel().getSelectedItem();
        int index = probeSelected.getProbeCount() - 1 ;
        if(index == slectedProbes.size())
        {
              ErrorMsg error = new ErrorMsg();
              error.showError("can not move up the bottom item!");
        }
        else
        {
            swap(slectedProbes, index, index + 1);
            ModelForExperiments.getInstance().setProbeListForOnePlate(curExperiment, curPlate, slectedProbes);   
        }
        experimentProbeTable.refresh();        
    }


}
