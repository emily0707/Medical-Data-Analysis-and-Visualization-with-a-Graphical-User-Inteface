/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author feiping
 */
public class Main extends Application {
    private static BorderPane root = new BorderPane();
      public static BorderPane getRoot() {
    return root;
  } 
    @Override
    public void start(Stage stage) throws Exception {
        
      /*  Parent root = FXMLLoader.load(getClass().getResource("/View/Homepage.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show(); */
       
    URL menuBarUrl = getClass().getResource("/View/menuBar.fxml");
    MenuBar bar = FXMLLoader.load( menuBarUrl );         
               
    URL paneOneUrl = getClass().getResource("/View/Homepage.fxml");
    AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
    
    root.setTop(bar);
    root.setCenter(paneOne);
        
    Scene scene = new Scene(root, 640, 480);
    stage.setScene(scene);
    stage.show(); 

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
