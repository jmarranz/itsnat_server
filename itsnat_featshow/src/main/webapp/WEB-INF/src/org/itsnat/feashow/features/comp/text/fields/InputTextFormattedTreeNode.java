
package org.itsnat.feashow.features.comp.text.fields;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Date;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Element;

public class InputTextFormattedTreeNode extends FeatureTreeNode implements PropertyChangeListener,VetoableChangeListener
{
    protected ItsNatHTMLInputTextFormatted inputComp;

    public InputTextFormattedTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.inputComp = (ItsNatHTMLInputTextFormatted)compMgr.createItsNatComponentById("inputId","formattedTextField",null);
        try{ inputComp.setValue(new Date()); } catch(PropertyVetoException ex) { throw new RuntimeException(ex); }

        inputComp.addPropertyChangeListener("value",this);

        inputComp.addVetoableChangeListener(this);
    }

    public void endExamplePanel()
    {
        this.inputComp.dispose();
        this.inputComp = null;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        Date value = (Date)evt.getNewValue();

        log("Value changed to: " + value + " ");
    }

    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException
    {
        Date newDate = (Date)evt.getNewValue();
        if (newDate.compareTo(new Date()) > 0)
            throw new PropertyVetoException("Future date is not allowed",evt);
    }
}
