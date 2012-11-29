/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.core.jsutils;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLTextAreaElement;

public class JavaToJavaScriptGenTreeNode extends FeatureTreeNode
{
    public JavaToJavaScriptGenTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        if (itsNatDoc.isLoading())
        {
            StringBuffer msg = new StringBuffer();
            msg.append("alert(\"This example (Java to JavaScript Generation) do not work in load time, ");
            msg.append("the value attribute do not change a textarea on load time, ");
            msg.append("click on any tab and return here\");");
            itsNatDoc.addCodeToSend(msg);
        }

        HTMLTextAreaElement textAreaElem = (HTMLTextAreaElement)doc.getElementById("textAreaId");

        ScriptUtil scriptGen = itsNatDoc.getScriptUtil();

        String code;
        String msg = "A Java String 'transported' \n\t as a \"JavaScript\" string";

        String textAreaElemJS = scriptGen.getNodeReference(textAreaElem);
        String jsStrLiteral = scriptGen.getTransportableStringLiteral(msg);
        code = textAreaElemJS + ".value = " + jsStrLiteral + ";";
        itsNatDoc.addCodeToSend(code);
        log(code);

        code = scriptGen.getSetPropertyCode(textAreaElem,"value",msg,true);
        itsNatDoc.addCodeToSend(code);
        log(code);

        code = scriptGen.getCallMethodCode(textAreaElem,"select",null,true);
        itsNatDoc.addCodeToSend(code);
        log(code);
    }

    public void endExamplePanel()
    {

    }
}
