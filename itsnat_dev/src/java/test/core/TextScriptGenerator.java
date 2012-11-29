/*
 * TextScriptGenerator.java
 *
 * Created on 30 de mayo de 2007, 13:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.script.ScriptUtil;

/**
 *
 * @author jmarranz
 */
public class TextScriptGenerator
{

    /**
     * Creates a new instance of TextScriptGenerator
     */
    public TextScriptGenerator(ItsNatDocument itsNatDoc)
    {
        ScriptUtil gen = itsNatDoc.getScriptUtil();
        String text = gen.encodeURIComponent(" % + < \" & á # \n ");

        itsNatDoc.addCodeToSend("if (!encodeURIComponent(decodeURIComponent(\"" + text + "\")) == \"" + text + "\") { alert('ERROR TextScriptGenerator'); throw 'ERROR'; } \n");
    }

}
