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

package org.itsnat.feashow.features.comp.text.fields;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatFormattedTextField.ItsNatFormatter;
import org.itsnat.comp.text.ItsNatFormatterFactoryDefault;
import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Element;

public class InputTextFormattedWithFactoryTreeNode extends FeatureTreeNode implements PropertyChangeListener,VetoableChangeListener
{
    protected ItsNatHTMLInputTextFormatted inputComp;

    public InputTextFormattedWithFactoryTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        this.inputComp = (ItsNatHTMLInputTextFormatted)compMgr.createItsNatComponentById("inputId","formattedTextField",null);

        ItsNatFormatterFactoryDefault factory = (ItsNatFormatterFactoryDefault)inputComp.createDefaultItsNatFormatterFactory();

        ItsNatFormatter dispFormatter = inputComp.createItsNatFormatter(DateFormat.getDateInstance(DateFormat.LONG ,Locale.US));
        factory.setDisplayFormatter(dispFormatter);
        ItsNatFormatter editFormatter = inputComp.createItsNatFormatter(DateFormat.getDateInstance(DateFormat.SHORT,Locale.US));
        factory.setEditFormatter(editFormatter);

        inputComp.setItsNatFormatterFactory(factory);

        try{ inputComp.setValue(new Date()); } catch(PropertyVetoException ex) { throw new RuntimeException(ex); }

        inputComp.addPropertyChangeListener("value",this);

        inputComp.addVetoableChangeListener(this);

        if (isUCWEB() || isBolt())
        {
            Element ucwebElem = itsNatDoc.getDocument().getElementById("ucwebId");
            ucwebElem.removeAttribute("style");
        }
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
