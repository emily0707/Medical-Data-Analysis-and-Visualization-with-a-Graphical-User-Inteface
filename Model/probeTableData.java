/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class probeTableData {
    private  SimpleIntegerProperty probeCount; 
    private  SimpleStringProperty probeForPlate;


    public probeTableData(Integer probeCount, String probeForPlate) {
            super();
            this.probeCount = new SimpleIntegerProperty(probeCount);
            this.probeForPlate = new SimpleStringProperty(probeForPlate);         
    }

    public Integer getProbeCount() {
        return probeCount.get();
    }
    public String getProbeForPlate() {
        return probeForPlate.get();
    }
    public void setProbeCount(Integer count)
    {
        this.probeCount = new SimpleIntegerProperty(count);
    }
    public void setProbeForPlate(String probe)
    {
        this.probeForPlate = new SimpleStringProperty(probe);
    }

}
