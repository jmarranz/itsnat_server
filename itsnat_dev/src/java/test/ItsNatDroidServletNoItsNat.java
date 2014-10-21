/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jmarranz
 */
public class ItsNatDroidServletNoItsNat extends HttpServlet
{

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
            throws ServletException, IOException
    {
        response.setContentType("android/layout;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try
        {
            StringBuilder res = new StringBuilder();
          
            res.append(" <LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\" ");
            res.append("    android:layout_width=\"match_parent\" ");
            res.append("    android:layout_height=\"match_parent\" ");
            res.append("    android:orientation=\"vertical\">  ");

            res.append("    <TextView  ");
            res.append("        android:layout_width=\"match_parent\" ");
            res.append("        android:layout_height=\"wrap_content\" ");
            res.append("        android:text=\"TEST NO ITSNAT SERVER\" ");
            res.append("        android:textSize=\"20dp\" ");
            res.append("        android:background=\"#00ff00\"> ");
            res.append("   </TextView> ");
            
            res.append("   <Button ");
            res.append("        android:id=\"@id/back\" ");
            res.append("        android:layout_width=\"wrap_content\" ");
            res.append("        android:layout_height=\"wrap_content\" ");
            res.append("        android:text=\"BACK\" /> ");

            res.append("   <Button ");
            res.append("        android:id=\"@id/buttonReload\" ");
            res.append("        android:layout_width=\"wrap_content\" ");
            res.append("        android:layout_height=\"wrap_content\" ");
            res.append("        android:text=\"@string/button_reload\" />  ");          
            
            
            res.append("   <Button ");
            res.append("        android:layout_width=\"wrap_content\" ");
            res.append("        android:layout_height=\"wrap_content\" ");
            res.append("        android:layout_marginTop=\"30dp\" ");            
            res.append("        android:text=\"ADD\" />  ");             
            
            res.append(" </LinearLayout> ");
            
            out.println(res);
        }
        finally
        {
            out.close();
        }
    }

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
            throws ServletException, IOException
    {
        processRequest(request, response);
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
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        processRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        processRequest(req, resp);
    }

    @Override    
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        processRequest(req, resp);
    }

    @Override    
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        processRequest(req, resp);
    }

    @Override    
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        processRequest(req, resp);
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    } 
}
