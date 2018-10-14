/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyUtils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dravit
 */
public class CustomErrors {
    
    public static JSONObject err;
    
    public static JSONObject invalidMethod(){
        err = new JSONObject(true);
        try {
            err.put("error", MyStrings.INVALID_API_ACCESS_METHOD);
        } catch (JSONException ex) {
            Logger.getLogger(CustomErrors.class.getName()).log(Level.SEVERE, null, ex);
        }
        return err;
    }
}
