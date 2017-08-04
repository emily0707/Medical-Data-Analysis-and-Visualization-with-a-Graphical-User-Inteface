/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class probeTableData {
    private  SimpleIntegerProperty probe; 



    
    private  SimpleStringProperty analyte;
    
    /*
         public void setProbe(int value) { probeProperty().set(value); }
     public Integer getProbe() { return probeProperty().get(); }
     public IntegerProperty probeProperty() { 
             probe = new SimpleIntegerProperty(this, "probe");
         return probe; 
     }
    public void setAnalyte(String value) { analyteProperty().set(value); }
    public String getAnalyte() { return analyteProperty().get(); }
    public StringProperty analyteProperty() { 
         if (analyte == null) analyte = new SimpleStringProperty(this, "analyte");
         return analyte; 
     } 
*/
    	public probeTableData(Integer probe, String analyte) {
		super();
		this.probe = new SimpleIntegerProperty(probe);
		this.analyte = new SimpleStringProperty (analyte);
	}
           
	public Integer getProbe() {
		return probe.get();
	}
	public String getAnalyte() {
		return analyte.get();
	}
}
