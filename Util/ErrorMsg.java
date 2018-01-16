/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import javafx.scene.control.Alert;

/**
 *
 * @author feiping
 */
public class ErrorMsg {
       public void showError(String detail)
    {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(detail);
            alert.showAndWait();
    }
}
