/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.shared;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 *
 * @author jmarranz
 */
public class DebuggingObjectOutputStream extends ObjectOutputStream
{
    protected static boolean showInfo = true;

    public DebuggingObjectOutputStream(OutputStream out) throws IOException
    {
        super(out);

        enableReplaceObject(true);

        if (showInfo) System.out.println("Created DebuggingObjectOutputStream");
    }

    protected Object replaceObject(Object obj) throws IOException
    {
        if (showInfo && (obj != null)) System.out.println("Serializing: " + obj.getClass());
	return obj;
    }
}
