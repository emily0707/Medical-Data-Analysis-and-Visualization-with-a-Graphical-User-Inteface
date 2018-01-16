/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author feiping
 */
public class PlateStatus {
     private String plate;
     private String status;

    public PlateStatus(String plate, String status) {
        this.plate = plate;
        this.status = status;
    }
     public String getPlate()
     {
         return plate;
     }
     
     public void setPlate(String plate)
     {
         this.plate =  plate;
     }
     
     public String getStatus()
     {
         return status;
     }
     
     public void setStatus(String status)
     {
         this.status = status;
     }
 
    
}
