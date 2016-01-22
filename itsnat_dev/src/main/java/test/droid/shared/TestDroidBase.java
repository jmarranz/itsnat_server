/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.shared;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulOwnerImpl;
import org.itsnat.impl.core.clientdoc.CodeToSendRegistryImpl;
import org.itsnat.impl.core.scriptren.shared.node.InnerMarkupCodeImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class TestDroidBase implements Serializable
{
    public static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    
    protected final ItsNatDocument itsNatDoc;

    public TestDroidBase(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }
    
    public Document getDocument()
    {
        return itsNatDoc.getDocument();
    }
    
    public void logToTextView(Element outElem,String msg)
    {
        String text = outElem.getAttribute("android:text"); // Usamos getAttribute("android:text") y no getAttributeNS(ANDROID_NS,"text") o getAttributeNS(ANDROID_NS,"android:text") porque estamos logando con setAttribute("android:text",text), NO con setAttributeNS
        text += msg;
        outElem.setAttribute("android:text",text);         
    }
    
    public String getLogTextView(Element outElem)
    {
        return outElem.getAttribute("android:text");  // Ver nota de logToTextView   
    }    
    
    protected void checkUsingSetInnerXML(boolean mustUseInnerXML)
    {
        // Verificamos que el último código generado es un objeto InnerMarkupCodeImpl, lo que significa que se usa setInnerXML
        ClientDocumentStfulOwnerImpl clientTmp = (ClientDocumentStfulOwnerImpl)itsNatDoc.getClientDocumentOwner();
        CodeToSendRegistryImpl codeToSendRegistry = clientTmp.getCodeToSendRegistry();
        Object lastCodeToSend = codeToSendRegistry.getLastCodeToSend();
        
        if (lastCodeToSend instanceof InnerMarkupCodeImpl) // Usando setInnerXML
        {
            boolean doNotUseInnerXML = !mustUseInnerXML;
            if (doNotUseInnerXML) throw new RuntimeException("IS USING setInnerXML, TEST FAILED");
        }
        else // No usando setInnerXML
        {
            if (mustUseInnerXML) throw new RuntimeException("IS NOT USING setInnerXML, TEST FAILED");
        }        
    }
}
