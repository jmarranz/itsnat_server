/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        HttpSession session = (HttpSession)request.getSession();

        StringBuilder res;
        if ("true".equals(request.getParameter("addItem")))
        {
            int counter = (Integer)session.getAttribute("counter");
            counter++;
            session.setAttribute("counter",counter);

            res = loadFragment(counter);
        }
        else
        {
            session.setAttribute("counter",0);

            res = loadPage(request.getLocalAddr(),request.getLocalPort());
        }

        PrintWriter out = response.getWriter();
        try
        {
            out.println(res);
        }
        finally
        {
            out.close();
        }
    }

    private StringBuilder loadPage(String host,int port)
    {
        StringBuilder res = new StringBuilder();

        res.append(" <LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\" ");
        res.append("    android:layout_width=\"match_parent\" ");
        res.append("    android:layout_height=\"match_parent\" ");
        res.append("    android:padding=\"10dp\" ");
        res.append("    android:orientation=\"vertical\">  ");

        res.append("    <TextView  ");
        res.append("        android:layout_width=\"match_parent\" ");
        res.append("        android:layout_height=\"wrap_content\" ");
        res.append("        android:text=\"TEST NO ITSNAT SERVER\" ");
        res.append("        android:textSize=\"20dp\" ");
        res.append("        android:background=\"#00ff00\" /> ");

        res.append("    <Button ");
        res.append("        android:id=\"@id/back\" ");
        res.append("        android:layout_width=\"wrap_content\" ");
        res.append("        android:layout_height=\"wrap_content\" ");
        res.append("        android:text=\"BACK\" /> ");

        res.append("    <Button ");
        res.append("        android:id=\"@id/buttonReload\" ");
        res.append("        android:layout_width=\"wrap_content\" ");
        res.append("        android:layout_height=\"wrap_content\" ");
        res.append("        android:text=\"@string/button_reload\" />  ");

        res.append("    <TextView  ");
        res.append("        android:layout_width=\"match_parent\"  ");
        res.append("        android:layout_height=\"wrap_content\"  ");
        res.append("        android:layout_marginTop=\"20dp\"  ");
        res.append("        android:text=\"Test nine-patch (border must be green)\"  ");
        res.append("        android:textSize=\"20dp\"  ");
        res.append("        android:background=\"@remote:drawable/droid/res/drawable/test_nine_patch_remote.xml\" />  ");

        
        res.append("    <TextView id=\"testOnLoadScriptWithSrcId\"  ");
        res.append("        android:layout_width=\"match_parent\"  ");
        res.append("        android:layout_height=\"wrap_content\"  ");
        res.append("        android:text=\"test loading &lt;script src=&gt;: \" />  ");
        
        res.append("    <script src=\"droid/bs/test_script_loading.bs\" />  ");
       
        res.append("    <TextView id=\"testOnLoadScriptWithSrc2Id\"  ");
        res.append("        android:layout_width=\"match_parent\"  ");
        res.append("        android:layout_height=\"wrap_content\"  ");
        res.append("        android:text=\"test loading &lt;script src=&gt; 2: \" />  ");        

        res.append("    <script src=\"http://" + host + ":" + port + "/itsnat_dev/droid/bs/test_script_loading_2.bs\" /> " );
        
        res.append("    <script>"
                        + "void addItem() { "
                        + "  itsNatDoc.createGenericHttpClient()"
                        + "  .setRequestMethod(\"GET\")"
                        + "  .setOnHttpRequestListener(new OnHttpRequestListener(){"
                        + "     void onRequest(Page page,HttpRequestResult response){"
                        + "        var viewRoot = itsNatDoc.getRootView();"
                        + "        int viewParentId = itsNatDoc.getResourceIdentifier(\"parentId\");"
                        + "        var viewParent = viewRoot.findViewById(viewParentId);"
                        + "        int viewRefId = itsNatDoc.getResourceIdentifier(\"limitId\");"
                        + "        var viewRef = viewParent.findViewById(viewRefId);"
                        + "        itsNatDoc.insertFragment(viewParent,response.getResponseText(),viewRef);"
                        + "     }"
                        + "   })"
                        + "  .setOnHttpRequestErrorListener(new OnHttpRequestErrorListener(){"
                        + "     void onError(Page page,Exception ex,HttpRequestResult response){"
                        + "         ex.printStackTrace();"
                        + "         alert(\"addItem error:\" + ex.getMessage() );"
                        + "     }"
                        + "   })"
                        + "  .addParameter(\"addItem\",\"true\") "
                        + "  .requestAsync(); "
                        + "}"
                     + "</script> ");

        res.append("    <Button ");
        res.append("        android:layout_width=\"wrap_content\" ");
        res.append("        android:layout_height=\"wrap_content\" ");
        res.append("        android:layout_marginTop=\"10dp\" ");
        res.append("        android:text=\"ADD ITEM\" ");
        res.append("        onclick=\"addItem()\" /> ");


        res.append("    <ScrollView ");
        res.append("        android:layout_width=\"match_parent\" ");
        res.append("        android:layout_height=\"wrap_content\"> ");

        res.append("        <LinearLayout ");
        res.append("            android:id=\"@+id/parentId\"  ");
        res.append("            android:layout_width=\"match_parent\" ");
        res.append("            android:layout_height=\"wrap_content\" ");
        res.append("            android:orientation=\"vertical\">  ");


        res.append("            <View ");
        res.append("                android:id=\"@+id/limitId\"  ");
        res.append("                android:layout_width=\"match_parent\" ");
        res.append("                android:layout_height=\"2dp\" ");
        res.append("                android:layout_marginTop=\"10dp\" ");
        res.append("                android:background=\"#000000\" /> ");


        res.append("        </LinearLayout> ");

        res.append("    </ScrollView> ");


        res.append(" </LinearLayout> ");

        return res;
    }

    private StringBuilder loadFragment(int counter)
    {
        StringBuilder res = new StringBuilder();

        res.append("    <View ");
        res.append("        android:layout_width=\"match_parent\" ");
        res.append("        android:layout_height=\"2dp\" ");
        res.append("        android:layout_marginTop=\"10dp\" ");
        res.append("        android:background=\"#000000\" /> ");

        res.append("    <TextView  ");
        res.append("        android:layout_width=\"wrap_content\" ");
        res.append("        android:layout_height=\"wrap_content\" ");
        res.append("        android:layout_marginTop=\"10dp\" ");
        res.append("        android:text=\"ITEM " + counter + "\" ");
        res.append("        android:textSize=\"20dp\" ");
        res.append("        android:background=\"#ffff00\" /> ");

        res.append("    <TextView  ");
        res.append("        android:layout_width=\"match_parent\"  ");
        res.append("        android:layout_height=\"wrap_content\"  ");
        res.append("        android:layout_marginTop=\"20dp\"  ");
        res.append("        android:text=\"Test nine-patch (border must be green)\"  ");
        res.append("        android:textSize=\"20dp\"  ");
        res.append("        android:background=\"@remote:drawable/droid/res/drawable/test_nine_patch_remote.xml\" />  ");

        return res;
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
