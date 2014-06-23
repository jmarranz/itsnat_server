package org.itsnat.droid.impl.util;

import org.itsnat.droid.ItsNatDroidException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
}
