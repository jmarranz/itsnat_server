/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.html;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletResponse;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.text.ItsNatHTMLInputFile;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.iframe.FileUploadRequest;
import org.itsnat.comp.iframe.HTMLIFrameFileUpload;
import org.itsnat.comp.iframe.ItsNatHTMLIFrame;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;
import test.shared.ItsNatServletRequestListenerSerial;
import test.shared.TestBaseHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestFileUpload extends TestBaseHTMLDocument implements EventListener
{
    protected ItsNatHTMLInputFile input;
    protected ItsNatHTMLIFrame iframe;
    protected ItsNatHTMLButton button;


    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestFileUpload(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("fileUploadInputId");
        this.input = (ItsNatHTMLInputFile)componentMgr.findItsNatComponent(inputElem);
        HTMLIFrameElement iframeElem = (HTMLIFrameElement)doc.getElementById("fileUploadIFrameId");
        this.iframe = (ItsNatHTMLIFrame)componentMgr.findItsNatComponent(iframeElem);
        HTMLButtonElement buttonElem = (HTMLButtonElement)doc.getElementById("fileUploadButtonId");
        this.button = (ItsNatHTMLButton)componentMgr.findItsNatComponent(buttonElem);
        button.addEventListener("click", this);
    }

    public void handleEvent(Event evt)
    {
        final HTMLIFrameFileUpload iframeUpload = iframe.getHTMLIFrameFileUpload(input.getHTMLInputElement());
        ItsNatServletRequestListener listener = new ItsNatServletRequestListenerSerial()
        {
            public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
            {
                FileUploadRequest fileUpReq = iframeUpload.processFileUploadRequest(request, response);

                StringBuffer html = new StringBuffer();
                html.append("<html><head /><body>");
                html.append("<p>Content Type:\"" + fileUpReq.getContentType() + "\"</p>");
                html.append("<p>Field Name:\"" + fileUpReq.getFieldName() + "\"</p>");
                html.append("<p>File Name:\"" + fileUpReq.getFileName() + "\"</p>");
                html.append("<p>File Size:\"" + fileUpReq.getFileSize() + "\"</p>");

                FileInputStream fileOrig = null;
                try
                {
                    fileOrig = new FileInputStream(fileUpReq.getFileName());
                }
                catch(IOException ex)
                {
                    html.append("<h2>Cannot test a local file</h2>");
                }

                try
                {
                    InputStream fileUp = fileUpReq.getFileUploadInputStream();
                    long count = 0;
                    if (fileOrig != null)
                    {
                        int b1,b2;
                        do
                        {
                            b1 = fileOrig.read();
                            b2 = fileUp.read();
                            if (b1 != b2) throw new RuntimeException("Different Files");
                            count++;
                        }
                        while ((b1 != -1) && (b2 != -1));
                    }
                    else
                    {
                        int b;
                        do
                        {
                            b = fileUp.read();
                            count++;
                        }
                        while (b != -1);
                    }

                    count--; // Quitamos la lectura del -1
                    long fileSize = fileUpReq.getFileSize();
                    if (fileSize != count) throw new RuntimeException("Wrong File Size");
                    html.append("</body></html>");

                    ServletResponse servRes = response.getServletResponse();
                    servRes.getWriter().print(html.toString());
                }
                catch(IOException ex)
                {
                    throw new RuntimeException(ex);
                }
            }
        };
        iframeUpload.addItsNatServletRequestListener(listener);
    }
}
