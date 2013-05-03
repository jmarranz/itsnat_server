
package org.itsnat.spitut;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;

public class SPITutMainLoadRequestListener implements ItsNatServletRequestListener
{
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new SPITutMainDocument((ItsNatHttpServletRequest)request,(ItsNatHttpServletResponse)response);
    }
}
