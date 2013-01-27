
import javax.swing.JApplet; 

import org.itsnat.batik.applet.JSVGCanvasApplet;
import org.w3c.dom.Document;

/**
 * A applet demonstrating the JSVGCanvas.
 *
 * Tuned to be used with ItsNat
 *
 * @version $Id$
 */
public class ItsNatBatikAppletLauncher extends JApplet
{
    protected JSVGCanvasApplet canvas;

    public void init()
    {
        try
        {
            // Create a new JSVGCanvas.
            canvas = new JSVGCanvasApplet(this);
            getContentPane().add(canvas);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public void start()
    {
        canvas.start();
        String urlStr;
        try
        {
            urlStr = getParameter("src");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        if (urlStr == null) return;
        canvas.setSrcFromBrowser(urlStr);
    }

    public void stop()
    {
        canvas.stop();
    }

    public void destroy()
    {
        canvas.destroy();
    }

    public Document getSVGDocument()
    {
        // Simulamos el método getSVGDocument() que por ejemplo definen ASV 
        // No devolvemos SVGOMDocument sino un wrapper.
        // No podemos pasar el objeto wrapper a canvas.setDocument(doc);
        // porque internamente hace un cast a la clase SVGOMDocument
        // y ItsNatSVGOMDocBatikInBrowser implementa SVGDocument pero no
        // hereda de SVGOMDocument para evitar problemas.
        return canvas.getItsNatSVGOMDocBatikInBrowser();
    }

    public String getSrc()
    {
        return canvas.getSrcFromBrowser();
    }
    
    public void setSrc(String url)
    {
        canvas.setSrcFromBrowser(url);
    }

}
