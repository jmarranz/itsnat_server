/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inexp.juel;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class ELBasedInputText
{
    protected ItsNatHTMLInputText comp;
    protected ValueExpression expPattern;
    protected ELContext context;
    
    public ELBasedInputText(String varName,Object value,HTMLInputElement inputElem,ItsNatDocument itsNatDoc,ExpressionFactory factory,final Runnable onChanged)
    {
        this.context = new de.odysseus.el.util.SimpleContext(); 
        
        String pattern = inputElem.getValue();
        this.comp = (ItsNatHTMLInputText)itsNatDoc.getItsNatComponentManager().createItsNatComponent(inputElem);
        
        context.getVariableMapper().setVariable(varName, factory.createValueExpression(value, value.getClass()));                 
        
        this.expPattern = factory.createValueExpression(context, pattern, String.class); 

        String strVvalue = expPattern.getValue(context).toString();   // Initial value     
        comp.setText(strVvalue);

        comp.getDocument().addDocumentListener(new DocumentListener() 
        {
            public void insertUpdate(DocumentEvent e)
            {
                updateValueAndNotify();
            }

            public void removeUpdate(DocumentEvent e)
            {
                updateValueAndNotify();               
            }

            public void changedUpdate(DocumentEvent e)
            {
                updateValueAndNotify();             
            }      
            
            private void updateValueAndNotify()
            {
                String text = comp.getText();
                expPattern.setValue(context,text);

                System.out.println("changed " + text);
                onChanged.run();        
            }            
        } );        
    }


}
