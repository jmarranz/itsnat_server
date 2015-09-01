/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.shared;

import java.io.IOException;
import java.io.OutputStream;
import org.itsnat.impl.core.servlet.ItsNatSessionObjectOutputStream;

/**
 *
 * @author jmarranz
 */
public class DebuggingObjectOutputStream extends ItsNatSessionObjectOutputStream // ItsNatSessionObjectInputStream es INTERNO DE ITSNAT sólo usar para estas pruebas
{
    static
    {
        showInfo = true;
    }

    public DebuggingObjectOutputStream(OutputStream out) throws IOException
    {
        super(out);

        if (showInfo) System.out.println("Created DebuggingObjectOutputStream");
    }

}
