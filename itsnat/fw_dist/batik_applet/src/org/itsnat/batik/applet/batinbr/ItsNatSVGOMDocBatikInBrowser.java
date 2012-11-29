
package org.itsnat.batik.applet.batinbr;

import org.itsnat.batik.applet.*;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author jmarranz
 */
public class ItsNatSVGOMDocBatikInBrowser extends ItsNatSVGOMDocument
{
    protected ItsNatSVGOMDocumentBatik batikDoc;
    protected ItsNatDocBatikInBrowser itsNatDoc;

    public ItsNatSVGOMDocBatikInBrowser(ItsNatSVGOMDocumentBatik batikDoc)
    {
        this.batikDoc = batikDoc;
        this.itsNatDoc = new ItsNatDocBatikInBrowser(this);
    }

    public ItsNatSVGOMDocumentBatik getItsNatSVGOMDocumentBatik()
    {
        return batikDoc;
    }

    public SVGOMDocument getSVGOMDocument()
    {
        return batikDoc.getSVGOMDocument();
    }

    public ItsNatDocBatikInBrowser getItsNatDoc()
    {
        // Llamado desde JavaScript
        return itsNatDoc;
    }

    public Scriptable getItsNatDocScriptable()
    {
        return batikDoc.getItsNatDoc();
    }
}
