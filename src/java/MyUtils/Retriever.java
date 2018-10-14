/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyUtils;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dravit
 */
public class Retriever {
    /**
     * this method converts the initial incoming payload to JSONObject 
     * to make it easy to store in the file
     */
    public static JSONObject retrieveJsonObjFromAddMovieScreenRequest(BufferedReader reader) throws JSONException{
        JSONObject obj = new JSONObject(true);
        String body = reader.lines().collect(Collectors.joining());
        obj = new JSONObject(body);
        return processAddMovieScreenData(obj);
    }
    
    /**
     * this method adds a new parameter, @totalSeats of the theater
     */
    
    public static JSONObject processAddMovieScreenData(JSONObject obj) throws JSONException{
        JSONObject seatInfo = (JSONObject) obj.get(MyStrings.SEAT_INFO);
        Iterator iter = seatInfo.keys();
        while(iter.hasNext()){
            String key = (String)iter.next();
            JSONObject seat = (JSONObject) seatInfo.get(key);
            int[] unreserved = new int[seat.getInt(MyStrings.NUMBER_OF_SEATS)];
            int [] reserved = new int[0];
            for(int i = 0; i < seat.getInt(MyStrings.NUMBER_OF_SEATS); ++i){
                unreserved[i] = i;
            }
            seat.put(MyStrings.RESERVED_SEATS, reserved);
            seat.put(MyStrings.UNRESERVED_SEATS, unreserved);
        }
        return obj;
    }
    
    public static JSONObject retrieveJsonObjFromReserveRequest(BufferedReader reader) throws JSONException{
        JSONObject obj = new JSONObject(true);
        String body = reader.lines().collect(Collectors.joining());
        obj = new JSONObject(body);
        return obj;
    }
    
}
