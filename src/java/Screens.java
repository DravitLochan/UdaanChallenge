/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import MyUtils.CustomErrors;
import MyUtils.Retriever;
import MyUtils.MyStrings;
import MyUtils.ReadFiles;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author dravit
 */
@WebServlet(name = "Screens", urlPatterns = {"/screens"})
public class Screens extends HttpServlet {

    
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
        JSONObject outputResponse = new JSONObject();
        JSONObject inputObject;
        try {
            inputObject = Retriever.retrieveJsonObjFromAddMovieScreenRequest(request.getReader());
            if(writeToFile(inputObject)){
                outputResponse.put(MyStrings.STATUS, true);
                outputResponse.put(MyStrings.MESSAGE, MyStrings.DATA_STORED_SUCCESSFULY);
            } else {
                outputResponse.put(MyStrings.STATUS, false);
                outputResponse.put(MyStrings.MESSAGE, MyStrings.DATA_STOREING_FAILED);
            }
            out.print(outputResponse);
        } catch (JSONException ex) {
            Logger.getLogger(Reserve.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(MyStrings.INVALID_PAYLOAD);
        }
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

    private boolean writeToFile(JSONObject inputObject) {
        String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/")) + "/data.txt";
        
        try {
            JSONArray allScreens = ReadFiles.readDataFile(absolutePath);
            allScreens.put(inputObject);
            FileWriter filewriter;
            filewriter = new FileWriter (new File(absolutePath), false);
            filewriter.write("[");
            for(int i = 0; i < allScreens.length()-1; ++i){
                filewriter.write(allScreens.get(i).toString());
                filewriter.write(",");
            }
            filewriter.write(allScreens.get(allScreens.length()-1).toString());
            filewriter.write("]");
            filewriter.flush();
            filewriter.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Screens.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Screens.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(Screens.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

}
