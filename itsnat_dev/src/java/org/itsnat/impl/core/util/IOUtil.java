/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itsnat.impl.core.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import org.itsnat.core.ItsNatException;

/**
 *
 * @author jmarranz
 */
public class IOUtil
{
    public static byte[] readURL(URL url)
    {
        URLConnection urlCon;
        try 
        { 
            urlCon = url.openConnection(); 
            return readInputStream(urlCon.getInputStream());           
        } 
        catch (IOException ex) { throw new ItsNatException(ex); }       
    }    
    
    public static byte[] readFile(File file) 
    {
        try
        {
            return readInputStream(new FileInputStream(file));
        }
        catch(FileNotFoundException ex) { throw new ItsNatException(ex); }        
    }    
    
    
    public static byte[] readInputStream(InputStream input) 
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try
        {
            byte[] buffer = new byte[10*1024];
            int res;
            while((res = input.read(buffer)) != -1)
            {
                output.write(buffer, 0, res);
            }
        }
        catch(IOException ex) 
        {
            throw new ItsNatException(ex);
        }
        finally
        {
            try
            {
                input.close();
                output.close();
            }
            catch(IOException ex) { throw new ItsNatException(ex); }            
        }
        return output.toByteArray();
    }    
    
    public static String readTextFile(File file,String charsetName)
    {            
        try
        {
            return IOUtil.readTextStream(new FileInputStream(file),charsetName);
        }
        catch(FileNotFoundException ex) { throw new ItsNatException(ex); }  
    }
    
    public static String readTextStream(InputStream input,String charsetName)
    {        
        StringBuilder code = new StringBuilder();
        readTextStream(input,charsetName,code);
        return code.toString();
    }
    
    public static void readTextStream(InputStream input,String charsetName,StringBuilder code)
    {     
        try
        {
            Reader reader = new InputStreamReader(input,charsetName);
            char[] cbuf = new char[1024*10];
            int count = reader.read(cbuf);
            while(count != -1)
            {
                code.append(cbuf, 0, count);
                count = reader.read(cbuf);
            }
            reader.close();
        }
        catch(UnsupportedEncodingException ex)
        {
            throw new ItsNatException(ex);
        }
        catch(IOException ex)
        {
            throw new ItsNatException(ex);
        }        
        finally
        {
            try
            {
                input.close();
            }
            catch (IOException ex)
            {
                throw new ItsNatException(ex);
            }            
        }        
    }    
}