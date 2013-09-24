package inexp.jreloadex.jproxy.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author jmarranz
 */
public class JReloaderUtil 
{
    public static byte[] readURL(URL url)
    {
        URLConnection urlCon;
        try 
        { 
            urlCon = url.openConnection(); 
            return readInputStream(urlCon.getInputStream());           
        } 
        catch (IOException ex) { throw new RuntimeException(ex); }       
    }
   
    public static byte[] readFile(File file)
    {	
        FileInputStream fis = null;
        try 
        {
            fis = new FileInputStream(file);    			
        }
        catch (FileNotFoundException ex) 
        {
            throw new RuntimeException(ex);
        }			

        return readInputStream(fis);
    }		

    public static byte[] readInputStream(InputStream is)
    {
        return readInputStream(is,50); // 50Kb => unas 100 lecturas 5 Mb 
    }

    public static byte[] readInputStream(InputStream is,int bufferSizeKb)
    {	
        ByteArrayOutputStream out = new ByteArrayOutputStream();		
        try 
        {	
            byte[] buffer = new byte[bufferSizeKb*1024]; 

            int size;
            while( (size = is.read(buffer)) != -1 )
            {
                    out.write(buffer, 0, size);
            }             			
        }   		
        catch (IOException ex) 
        {
            throw new RuntimeException(ex);
        }	       
        finally
        {
            try { is.close(); } catch (IOException ex2) { throw new RuntimeException(ex2); }			
        }

        return out.toByteArray();
    }	    
}
