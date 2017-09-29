/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class beadTableData1 {
	private  SimpleIntegerProperty bead; 
	private   SimpleStringProperty analyte;
	
	public beadTableData1(Integer bead, String analyte) {
		super();
		this.bead = new SimpleIntegerProperty(bead);
		this.analyte = new SimpleStringProperty (analyte);
	}
	public Integer getBead() {
		return bead.get();
	}
	public String getAnalyte() {
		return analyte.get();
	}
        public void setBead(Integer bead)
        {
           this.bead = new SimpleIntegerProperty(bead);
        }
        
        public void setAnalyte(String analyte)
        {
           this.analyte = new SimpleStringProperty(analyte);
        }
	
}
