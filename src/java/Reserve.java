/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import MyUtils.CustomErrors;
import MyUtils.MyStrings;
import MyUtils.ReadFiles;
import MyUtils.Retriever;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dravit
 */
@WebServlet(name = "Reserve", urlPatterns = {"/reserve/*"})
public class Reserve extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(CustomErrors.invalidMethod());
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject bookStatus = new JSONObject(true);
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        String screenName = pathParts[1];
        JSONObject bookingScreen = getBookingScreen(screenName);
        try {
            JSONObject inputObject = Retriever.retrieveJsonObjFromReserveRequest(request.getReader());
            System.out.print(screenName.toString());
            if(bookingScreen==null){
                throw new ServletException(MyStrings.NAME_NOT_FOUND);
            }
            if(bookTickets(inputObject.getJSONObject(MyStrings.SEATS), bookingScreen.getJSONObject(MyStrings.SEAT_INFO))){
                bookStatus.put(MyStrings.STATUS, true);
                bookStatus.put(MyStrings.MESSAGE, MyStrings.BOOKED_SUCCESSFULLY);
            } else {
                bookStatus.put(MyStrings.STATUS, false);
                bookStatus.put(MyStrings.MESSAGE, MyStrings.BOOKING_UNSUCCESSFUL);
            }
        } catch (JSONException ex) {
            Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(MyStrings.INVALID_PAYLOAD);
        }
        out.print(bookStatus);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private JSONObject getBookingScreen(String screenName) {
        try {
            String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/")) + "/data.txt";
            JSONArray allScreens = ReadFiles.readDataFile(absolutePath);
            for(int i = 0; i < allScreens.length(); ++i){
                JSONObject currentScreen = (JSONObject) allScreens.get(i);
                if(currentScreen.get("name").toString().equals(screenName)){
                    return currentScreen;
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private boolean bookTickets(JSONObject inputObject, JSONObject bookingScreenSeatInfo) throws JSONException{
        Iterator seatsRequired = inputObject.keys();
        Iterator seatsAvailable = bookingScreenSeatInfo.keys();
        int i, j;
        while(seatsRequired.hasNext()){
            JSONArray requiredSeatNumbers = inputObject.getJSONArray((String)seatsRequired.next());
            JSONObject availableSeats = bookingScreenSeatInfo.getJSONObject((String)seatsAvailable.next());
            JSONArray availableSeatNumbers = availableSeats.getJSONArray(MyStrings.UNRESERVED_SEATS);
            List <Integer> requiredList = new ArrayList<Integer>();
            List <Integer> availableList = new ArrayList<Integer>();
            i = 0;
            while(i < requiredSeatNumbers.length()){
                requiredList.add(Integer.parseInt(requiredSeatNumbers.get(i).toString()));
                ++i;
            }
            i = 0;
            while(i < availableSeatNumbers.length()){
                availableList.add(Integer.parseInt(availableSeatNumbers.get(i).toString()));
                ++i;
            }
            Collections.sort(requiredList);
            Collections.sort(availableList);
            i = j = 0;
            while(i<requiredList.size() && j<availableList.size()){
                if(requiredList.get(i)==availableList.get(j)){
                    ++i; ++j;
                } else if(requiredList.get(i)<availableList.get(j)){
                    System.out.print("here1");
                    return false;
                } else {
                    ++j;
                }
            }
            if(i!=requiredSeatNumbers.length()){
                System.out.print("here2");
                return false;
            }
        }
        return true;
    }
}
