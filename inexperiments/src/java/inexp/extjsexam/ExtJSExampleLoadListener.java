
package inexp.extjsexam;

import inexp.extjsexam.model.DBSimulator;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

/**
 *
 * @author jmarranz
 */
public class ExtJSExampleLoadListener implements ItsNatServletRequestListener
{
    protected DBSimulator dataModel;

    public ExtJSExampleLoadListener(DBSimulator dataModel)
    {
        this.dataModel = dataModel;
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        new ExtJSExampleDocument(dataModel,request);
    }

}
