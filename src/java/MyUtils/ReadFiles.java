/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author dravit
 */
public class ReadFiles {
    public static JSONArray readDataFile(String path) throws JSONException, IOException, FileNotFoundException{
        String str = "";
        JSONArray array = new JSONArray();
        FileReader fileReader = new FileReader(path);
        int i;    
        while((i=fileReader.read())!=-1)    
            str += (char)i;
        array = new JSONArray(str);
        return array;
    }
}
