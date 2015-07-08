/*
 * TestCreateItsNatComponentListener.java
 *
 * Created on 16 de febrero de 2007, 13:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.droid.comp;

import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.NameValue;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class TestDroidCreateItsNatComponentListener implements CreateItsNatComponentListener
{

    /** Creates a new instance of TestCreateItsNatComponentListener */
    public TestDroidCreateItsNatComponentListener()
    {
    }

    public ItsNatComponent before(Node node,String compType,NameValue[] artifacts, ItsNatComponentManager compMgr)
    {
        // No hacemos nada es por probar el registro/desregistro
        return null;
    }

    public ItsNatComponent after(ItsNatComponent comp)
    {
        return comp;
    }

}
