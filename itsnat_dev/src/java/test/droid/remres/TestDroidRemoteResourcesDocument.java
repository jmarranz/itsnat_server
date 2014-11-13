/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.remres;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.w3c.dom.Element;

public class TestDroidRemoteResourcesDocument implements Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element elem;


    public TestDroidRemoteResourcesDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request)
    {      
        HttpServletRequest httpReq = (HttpServletRequest)request.getServletRequest();
                    
        
        this.itsNatDoc = itsNatDoc;
      
        new TestDroidRemoteResFragmentInsertionInnerXML(itsNatDoc);
        new TestDroidRemoteResFragmentInsertionUsingAPI(itsNatDoc);        
    }

}
