/*
 * TestRemoteCtrlLauncherDocLoadListener.java
 *
 * Created on 8 de noviembre de 2006, 17:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.droid.drawable;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TestDroidDrawableLoadListener implements ItsNatServletRequestListener
{

    /** Creates a new instance of TestRemoteCtrlLauncherDocLoadListener */
    public TestDroidDrawableLoadListener()
    {
    }

    @Override
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        Document doc = request.getItsNatDocument().getDocument();
        Element ninePatchElem = doc.getDocumentElement();
        ninePatchElem.setAttribute("android:src", "@remote:drawable/droid/res/drawable/list_selector_background_focused_light_remote.9.png");
        // Alternativa (también vale):        
        // ninePatchElem.setAttributeNS(ANDROID_NS,"android:src", "@remote:drawable/droid/res/drawable/list_selector_background_focused_light_remote.9.png");        
    }
}
