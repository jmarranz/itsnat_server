
package inexp.juel;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.w3c.dom.Element;


/**
 *
 * @author jmarranz
 */
public class ELBasedElementList 
{
    protected ElementList elemListTable;
    
    public ELBasedElementList(String varName,Element listParent,ItsNatDocument itsNatDoc,ExpressionFactory factory)
    {
        ELContext context = new de.odysseus.el.util.SimpleContext();  // (using JUEL SimpleResolver by default)        
        
        ELBasedElementListRenderer listRenderer = new ELBasedElementListRenderer(factory,context,varName);              
        
        ElementGroupManager listFact = itsNatDoc.getElementGroupManager();        
        this.elemListTable = listFact.createElementList(listParent,true,null,listRenderer);     
    }
    
    public ElementList getElementList()
    {
        return elemListTable;
    }
}
