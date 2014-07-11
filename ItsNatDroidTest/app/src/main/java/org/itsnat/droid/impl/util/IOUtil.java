package org.itsnat.droid.impl.util;

import org.itsnat.droid.ItsNatDroidException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by jmarranz on 16/05/14.
 */
public class IOUtil
{
    public static byte[] read(InputStream input)
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream(20*1024);
        byte[] buffer = new byte[10*1024];
        int read = 0;
        try
        {
            read = input.read(buffer);
            while (read != -1)
            {
                output.write(buffer, 0, read);
                read = input.read(buffer);
            }
        }
        catch (IOException ex)
        {
            throw new ItsNatDroidException(ex);
        }
        finally
        {
            try { input.close(); }
            catch (IOException ex2) { throw new ItsNatDroidException(ex2); }
        }
        return output.toByteArray();
    }

 /*
    public static String encodeURIComponent(char c)
    {
        return encodeURIComponent(Character.toString(c));
    }

    public static String encodeURIComponent(String text)
    {
        return encodeURIComponent(text,true);
    }

    public static String encodeURIComponent(String text,boolean encodeSpaces)
    {
        // Emulamos el encodeURIComponent de JavaScript para el cual está preparado el servidor

        try
        {
            text = java.net.URLEncoder.encode(text,"UTF-8");
        }
        catch(UnsupportedEncodingException ex)
        {
            throw new ItsNatDroidException(ex);
        }

        StringBuilder textBuff = new StringBuilder(text);
        for(int i = 0; i < textBuff.length(); i++)
        {
            char c = textBuff.charAt(i);
            if (c == '+')
            {
                if (encodeSpaces)
                {
                    textBuff.deleteCharAt(i);
                    textBuff.insert(i,"%20");
                    i += 2;
                }
                else
                    textBuff.setCharAt(i,' ');
            }
        }
        return textBuff.toString();
    }
*/
}
