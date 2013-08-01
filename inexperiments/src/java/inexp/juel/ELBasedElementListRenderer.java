/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inexp.juel;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ElementListRenderer;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 *
 * @author jmarranz
 */
public class ELBasedElementListRenderer implements ElementListRenderer
{
    protected ELContext context;
    protected ExpressionFactory factory;
    protected String varName;    
    
    public ELBasedElementListRenderer(ExpressionFactory factory,ELContext context,String varName)
    {
        this.factory = factory;
        this.context = context;    
        this.varName = varName;
    }
    
    public void renderList(ElementList list,int index,Object value,Element elemItem,boolean isNew)  
    {  
        Class clasz = value != null ? value.getClass() : Object.class;
        context.getVariableMapper().setVariable(varName, factory.createValueExpression(value, clasz));                   

        processTree(elemItem); 
    }  

    public void unrenderList(ElementList list,int index,Element contentElem)  
    {  
    }     
    
    private void processTree(Node node)
    {    
        processNode(node);
        Node child = node.getFirstChild();
        while(child != null)
        {
            processTree(child);
            child = child.getNextSibling();
        }
    }
    
    private void processNode(Node child)
    {
        if (!(child instanceof CharacterData)) return;
        
        Text content = (Text)child;  
        ValueExpression expPattern = factory.createValueExpression(context, content.getData(), String.class); 
        content.setData( expPattern.getValue(context).toString() );         
    }
}
