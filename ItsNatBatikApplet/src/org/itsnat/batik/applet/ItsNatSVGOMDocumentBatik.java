package org.itsnat.batik.applet;

import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.script.rhino.RhinoInterpreterFixed;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author jmarranz
 */
public class ItsNatSVGOMDocumentBatik extends ItsNatSVGOMDocument
{
    protected JSVGCanvasApplet canvas;
    protected SVGOMDocument doc;
    protected Scriptable itsNatDoc; // Para poder hacer document.itsNatDoc = ... y document.getItsNatDoc()

    public ItsNatSVGOMDocumentBatik(SVGOMDocument doc,JSVGCanvasApplet canvas)
    {
        this.doc = doc;
        this.canvas = canvas;
    }

    public SVGOMDocument getSVGOMDocument()
    {
        return doc;
    }

    public RhinoInterpreterFixed getRhinoInterpreterFixed()
    {
        return canvas.getRhinoInterpreterFixed();
    }

    public JSVGCanvasApplet getJSVGCanvasApplet()
    {
        return canvas;
    }

    public Scriptable getItsNatDoc()
    {
        return itsNatDoc;
    }

    public void setItsNatDoc(Scriptable itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }
}
