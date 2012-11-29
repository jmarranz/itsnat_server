/*
 * OnClickFireEventFromServerTest.java
 *
 * Created on 6 de agosto de 2007, 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.shared;


import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;

/**
 *
 * @author jmarranz
 */
public class TestScriptRendering implements Serializable
{
    /** Creates a new instance of TestIFrameInsertion */
    public TestScriptRendering(ItsNatDocument itsNatDoc)
    {
        // La única finalidad de este código es probar si el "<" y el &
        // se convierte o no en &lt; y si da problemas
        // en los diferentes MIMEs con y sin CDATA section.
        String code = "for(var i = 0; i < 1; i++) { var res = (true & false);  }\n";
        itsNatDoc.addCodeToSend(code);
    }
}
