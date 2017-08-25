## 1. What this program does?
This research aims to provide an intuitive graphical user interface that facilitates the integration of Quantitative Multiplex co-immunoprecipitation (QMI) analyses into end-user labs. Lab technicians will run QMI analysis at ease and get correct results. 
   
   
## 2. Demo & Features developed
Please note this is an ongoing project.   
#### ongoing features: 
  - parse xml file get Meidan value using java.   
#### Finished features: 
  - Display experiment setting base on users' inputs sing different color and content. 
  - Users can edit beads list through a txt file. 
  - Display Probes table information base on users' inputs using different color and content. 
  - User select target xml files for expeirments
  - Editable bead table: They can eaither edit cells or add from a text boxes. 
  - Redesigned the Experiment: One experiements includes 2 probes well. 
#### Demo:
   ![alt text](https://github.com/emily0707/Graphic-user-Interface-for-Cancer-Research/blob/master/images/Picture1.png "Demo ScreenShot")
  
   
## 3. Challenges/Problems encounterd 
This project deals with large scale datasets, contiunous changes, and integration with diffrent moudules. Challenges I've encoutered are as below: 
#### 1. JavaFX or Java Swing? Why?       
The key caritieras are flexibility, scalability, and performance. I choose JavaFx beacuase:
  - easy to use because it provides a very convienct fxml scene builder. 
  - Layout managers are nodes. this makes future changes easier to made. 
  - Skinnable with CSS. You can control formatting with Cascading Stype Sheets(CSS). Every aspect of the appreance of you user interface can be set by a style rule. 
  - Supports modern touch devices. Swing lacks any support for modern touch devices. JavaFX has build-in support for common touch gestures such as scrolling, swiping, scrolling. This GUI project probably will isntalled on touch devices. 

#### 2. Integration with different modules
  There are a lot of modules working together. the analysis processes run on a R program, a Matlab program and an excel file.    
  After seraching related documents online, I figured out how to call R and Matlab program from Java. Analysis using excel will be programmed into R or Java and intergrated into the GUI project.     
  - Solution for calling R from Java:     
  Using JRI to call R from Java. Intall rJava(includes JRI) in RStudio. Add JRI libarary jars into Java project properties.    
  - Solution for calling MATLAB from Java:     
  Using MatlabControl JMI Wrapper to launch a matlab program from Java. 
  
