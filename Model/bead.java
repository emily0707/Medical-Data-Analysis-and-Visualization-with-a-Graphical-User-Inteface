/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
bead objects includes two members: 
* 1. region number 
* 2. analyte 
* read data from XML file and create bead objects to store the pair data. 
 */
public class bead {
 
 private SimpleIntegerProperty regionNumber;
 private SimpleStringProperty analyte;
 
 public bead(int regionNumber, String analyte )
 {
     super();
     this.regionNumber = new SimpleIntegerProperty(regionNumber);
     this.analyte = new SimpleStringProperty(analyte);
 }
 
 public int getRegionNumber() {
  return regionNumber.get();
 }
 public void setRegionNumber(int number) {
  this.regionNumber = new SimpleIntegerProperty(number);
 }
 public String getAnalyte() {
  return analyte.get();
 }
 public void setAnalyte(String name) {
  this.analyte = new SimpleStringProperty(name);
 }
}
   