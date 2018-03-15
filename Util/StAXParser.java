/*
This Class is using StAX xml parser to parse data and save data into java objects. 
 */
package Util;
import Model.ModelForExperiments;
import Model.bead;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author feiping
 */
public class StAXParser {
 
 // parse XML file <Analytes>...<Analytes> to get beads data: Region number & Analyte. 
public ObservableList<bead> getAnalytes(String filePath) {
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

// read one xml file and get median value data of each bead well
public HashMap<Integer, HashMap<Integer,  Double>> getMedianValueData(String fileName) {
   //List for 96 wells. 
   HashMap<Integer, HashMap<Integer,  Double>> wells = new HashMap<>();    
   int wellNo = 0;
    // a map for one well:  to hold data that belong to each region number . key: region number; value: reporters from xml file. 
    HashMap<Integer, List<Integer>> medianValueDataMap = new HashMap<>();  
    //get analytes and inititiate meidanvalue data map
    ObservableList<bead> analytes = ModelForExperiments.getInstance().getAnalytes();
    for(bead n : analytes)
    {
        int regionNumber = n.getRegionNumber();
        List<Integer> list = new ArrayList<>();
        medianValueDataMap.put(regionNumber, list);
    }                

    // start parsing data from the xml file
    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    try {
        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
        while(xmlEventReader.hasNext()){
            XMLEvent xmlEvent = xmlEventReader.nextEvent();                
           if (xmlEvent.isStartElement()){
               StartElement startElement = xmlEvent.asStartElement();
               // get well No
               if(startElement.getName().getLocalPart().equals("Well"))
               {
                   Iterator iterator = startElement.getAttributes();
                    while (iterator.hasNext()) {
                        Attribute attribute = (Attribute) iterator.next();        
                        if(attribute.getName().getLocalPart().equals("WellNo"))
                        {
                            String value = attribute.getValue();
                            wellNo = Integer.parseInt(value);
                        }
                     }
               }    

               // get median value orignal data data and process data in to a hashtable base on analytes
               if(startElement.getName().getLocalPart().equals("BeadEventData")){      
                   // clear previous data 
                   for(bead b : analytes)
                   {
                       medianValueDataMap.get(b.getRegionNumber()).clear();
                   }
                String elementText = xmlEventReader.getElementText(); // read the whole block data 
                processDataText(medianValueDataMap,elementText );                                                        

                // after processing one well, calculate median value for each analytes and save it into wells. 
                while(xmlEventReader.hasNext())
                  {
                      XMLEvent XMLEvent = xmlEventReader.nextEvent();
                      if(XMLEvent.getEventType() == END_ELEMENT)
                      {
                            wells.put(wellNo, CalculateMedianValue(medianValueDataMap,analytes));
                            break;                             
                      }
                   }
           }
           }
        }
    } catch (FileNotFoundException | XMLStreamException e) {
        e.printStackTrace();
    }
    return wells;
}    


//process data text and put it in to a hashtable for later callculate median value        
public  HashMap<Integer, List<Integer>> processDataText(HashMap<Integer, List<Integer>> medianValueDataMap, String elementText)
{
    String[] entries = elementText.split("\\r?\\n");                       
    String[] numbers = null;
    int region = 0;
    int reporter =0;
    int doublet =0; 
    for(String line : entries)
    {
        numbers = line.split(" "); 
        doublet = Integer.parseInt(numbers[1]);
        if(doublet<5000 || doublet>25000)
            continue;
        region = Integer.parseInt(numbers[0]);
        reporter = Integer.parseInt(numbers[2]);
        if(medianValueDataMap.containsKey(region))
             medianValueDataMap.get(region).add(reporter);                     
    }         
    return medianValueDataMap;
}
        
private  HashMap<Integer, Double>  CalculateMedianValue(HashMap<Integer, List<Integer>> medianValueDataMap, ObservableList<bead> analytes) 
{
    HashMap<Integer, Double> res = new HashMap<>();
    for(bead b : analytes)
    {
        int key = b.getRegionNumber();
        List<Integer> data = medianValueDataMap.get(key);
        Collections.sort(data);
        double median = median(data);
        res.put(key, median);
    }    
    return res;
}

//helper function to calculate median value for one analyte
public  double median (List<Integer> a){
    int middle = a.size()/2;

    if (a.size() % 2 == 1) {
        return a.get(middle);
    } else {
       return (a.get(middle-1) + a.get(middle)) / 2.0;
    }
} 

}
    
