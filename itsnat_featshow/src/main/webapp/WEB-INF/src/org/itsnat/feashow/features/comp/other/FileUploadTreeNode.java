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

package org.itsnat.feashow.features.comp.other;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import javax.servlet.ServletResponse;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.text.ItsNatHTMLInputFile;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;
import org.itsnat.comp.iframe.*;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ItsNatTimerHandle;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class FileUploadTreeNode extends FeatureTreeNode implements EventListener
{
    protected ItsNatHTMLInputFile input;
    protected ItsNatHTMLIFrame iframe;
    protected ItsNatHTMLButton button;
    protected Element progressElem;
    protected ItsNatTimerHandle currentTimerHnd;
    protected HTMLIFrameFileUpload currentIframeUpload;

    public FileUploadTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.input = (ItsNatHTMLInputFile)compMgr.createItsNatComponentById("fileUploadInputId");
        this.iframe = (ItsNatHTMLIFrame)compMgr.createItsNatComponentById("fileUploadIFrameId");
        this.button = (ItsNatHTMLButton)compMgr.createItsNatComponentById("fileUploadButtonId");
        button.addEventListener("click", this);
        this.progressElem = doc.getElementById("progressId");
    }

    public void endExamplePanel()
    {
        if (currentTimerHnd != null) currentTimerHnd.cancel();
        this.currentTimerHnd = null;

        if (currentIframeUpload != null) currentIframeUpload.dispose();
        this.currentIframeUpload = null;

        input.dispose();
        this.input = null;

        iframe.dispose();
        this.iframe = null;

        button.removeEventListener("click", this);
        button.dispose();
        this.button = null;

        this.progressElem = null;
    }

    public void updateProgression(long per)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        synchronized(itsNatDoc)
        {
            if (progressElem == null) return;
            Text text = (Text)progressElem.getFirstChild();
            text.setData(String.valueOf(per));
        }
    }

    public void handleEvent(Event evt)
    {
        ClientDocument clientDoc = ((ItsNatEvent)evt).getClientDocument();

        if (currentTimerHnd != null) currentTimerHnd.cancel();
        ItsNatTimer timer = clientDoc.createItsNatTimer();
        EventListener timerListener = new EventListener() {
            public void handleEvent(Event evt) { }   // Nothing to do, this timer just update the client with the current state of progressElem
        };
        this.currentTimerHnd = timer.schedule(null,timerListener,0,1000);
        final ItsNatTimerHandle timerHnd = currentTimerHnd;

        if (currentIframeUpload != null) currentIframeUpload.dispose();
        this.currentIframeUpload = iframe.getHTMLIFrameFileUpload(clientDoc,input.getHTMLInputElement());

        final HTMLIFrameFileUpload iframeUpload = currentIframeUpload;
        ItsNatServletRequestListener listener = new ItsNatServletRequestListener()
        {
            public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
            {
                FileUploadRequest fileUpReq = iframeUpload.processFileUploadRequest(request, response);

                try
                {
                    ServletResponse servRes = response.getServletResponse();
                    Writer out = servRes.getWriter();
                    out.write("<html><head /><body>");
                    out.write("<p>Content Type: \"" + fileUpReq.getContentType() + "\"</p>");
                    out.write("<p>Field Name: \"" + fileUpReq.getFieldName() + "\"</p>");
                    out.write("<p>File Name: \"" + fileUpReq.getFileName() + "\"</p>");
                    out.write("<p>File Size: " + fileUpReq.getFileSize() + "</p>");
                    if (fileUpReq.getFileSize() > 4*1024*1024)
                        out.write("<h2>TOO LARGE FILE</h2>");
                    out.write("</body></html>");

                    long fileSize = fileUpReq.getFileSize();
                    if ((fileSize == 0)||(fileSize > 4*1024*1024)) return;

                    byte[] buffer = new byte[10*1024];
                    InputStream fileUp = fileUpReq.getFileUploadInputStream();
                    long count = 0;
                    int read = 0;
                    do
                    {
                        if (iframeUpload.isDisposed())
                            return;

                        try { Thread.sleep(50); }catch(InterruptedException ex){ }
                        count += read;
                        updateProgression((count * 100) / fileSize);
                        read = fileUp.read(buffer);
                    }
                    while (read != -1);
                }
                catch(IOException ex)
                {
                    throw new RuntimeException(ex);
                }
                finally
                {
                    ItsNatDocument itsNatDoc = getItsNatDocument();
                    synchronized(itsNatDoc)
                    {
                        timerHnd.cancel();
                    }
                }
            }
        };
        iframeUpload.addItsNatServletRequestListener(listener);
    }

}
