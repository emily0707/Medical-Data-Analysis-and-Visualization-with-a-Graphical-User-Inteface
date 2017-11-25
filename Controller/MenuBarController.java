/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author feiping
 */
public class MenuBarController implements Initializable {

    @FXML
    private Menu input;
    @FXML
    private Menu medianValue;
    @FXML
    private Menu FoldChange;
    @FXML
    private Menu ANC;
    @FXML
    private Menu CNA;
    @FXML
    private Menu PCA;
    @FXML
    private Menu NED;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void switchToInput(ActionEvent event) {

    }

    @FXML
     void switchToMedianValue(ActionEvent event) {
             try {
      
      URL medianValueURL = getClass().getResource("/View/MedianValue.fxml");
      AnchorPane medianValue = FXMLLoader.load( medianValueURL );
      
      BorderPane border = Main.getRoot();
      border.setCenter(medianValue);
      
    } catch (IOException e) {
      e.printStackTrace();
    }
    }

    @FXML
    private void switchToFoldChange(ActionEvent event) {
    }

    @FXML
    private void switchToANC(ActionEvent event) {
    }

    @FXML
    private void switchToCNA(ActionEvent event) {
    }

    @FXML
    private void switchToPCA(ActionEvent event) {
    }

    @FXML
    private void switchToNED(ActionEvent event) {
    }
    
}
