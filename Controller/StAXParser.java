/*
This Class is using StAX xml parser to parse data and save data into java objects. 
 */
package Controller;
import Model.bead;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author feiping
 */
public class StAXParser {
 
 // parse XML file <Analytes>...<Analytes> to get beads data: Region number & Analyte. 
ObservableList<bead> getBeads(String filePath) {
//read bead region number and analyte name and save into a ArrayList. 
ObservableList<bead> beads = FXCollections.observableArrayList();
 boolean inAnalyte = false;
 boolean inAnalyteName = false;
 String analyteName = "";
 int regionNumber = 0;
  XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
  try {
   XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(filePath));
   while(xmlStreamReader.hasNext()) {     
                int eventType = xmlStreamReader.next();
             switch(eventType) {
                 case XMLStreamConstants.START_ELEMENT:
                  if(xmlStreamReader.getLocalName().equals("Analyte")){
                      regionNumber = Integer.parseInt(xmlStreamReader.getAttributeValue(0));
                      inAnalyte = true;
                  }               
                  else if (xmlStreamReader.getLocalName().equals("AnalyteName")){
                      inAnalyteName = true;
                  }
                     break;
                 case XMLStreamConstants.CHARACTERS:
                        if(inAnalyteName){
                         analyteName = xmlStreamReader.getText();
                         inAnalyteName = false;
                        }
                     break;   
                 case XMLStreamConstants.END_ELEMENT:
                    if(xmlStreamReader.getLocalName().equals("Analyte")){
                        bead an = new bead(regionNumber, analyteName);
                        beads.add(an);
                        inAnalyte = false;
                        inAnalyteName = false;
                     }
                    if(xmlStreamReader.getLocalName().equals("Analytes"))
                    {
                        return beads;
                    }
                     break;
                 }
 
             }
  } catch (FileNotFoundException | XMLStreamException e) {
   e.printStackTrace();
  }
  return beads;
 }




}
    
