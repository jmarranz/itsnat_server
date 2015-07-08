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

package org.itsnat.feashow.features.core.domutils;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatVariableResolver;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementLabel;
import org.itsnat.core.domutil.ElementLabelRenderer;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

public class VariableResolverTreeNode extends FeatureTreeNode implements EventListener
{
    protected Element linkToResolve;
    protected HTMLInputElement inputElem;
    protected ElementLabel label;

    public VariableResolverTreeNode()
    {
    }


    public void startExamplePanel()
    {
        final ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.linkToResolve = doc.getElementById("linkToResolveId");
        ((EventTarget)linkToResolve).addEventListener("click",this,false);

        this.inputElem = (HTMLInputElement)doc.getElementById("inputElemId");
        ParamTransport prop = new NodePropertyTransport("value");
        itsNatDoc.addEventListener((EventTarget)inputElem,"change",this,false,prop);

        ElementLabelRenderer renderer = new ElementLabelRenderer()
        {
            protected ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver(true);

            public void renderLabel(ElementLabel label,Object value,Element elem,boolean isNew)
            {
                resolver.setLocalVariable("variable_to_resolve",value);
                resolver.resolve(elem);
            }

            public void unrenderLabel(ElementLabel label,Element elem)
            {
            }
        };
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        this.label = factory.createElementLabel(doc.getElementById("elementId"),false,renderer);
        label.setUsePatternMarkupToRender(true);

        example2();

        example3();
    }

    public void example2()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        Object value = new Date();

        DateFormat format = DateFormat.getDateInstance(DateFormat.LONG,Locale.US);
        // Format: June 8,2007
        String date = format.format(value);
        int pos = date.indexOf(' ');
        String month = date.substring(0,pos);
        int pos2 = date.indexOf(',');
        String day = date.substring(pos + 1,pos2);
        String year = date.substring(pos2 + 1);

        ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver(true);
        resolver.setLocalVariable("year",year);
        resolver.setLocalVariable("month",month);
        resolver.setLocalVariable("day",day);

        Document doc = itsNatDoc.getDocument();
        Element elem = doc.getElementById("elementId2");
        resolver.resolve(elem);
    }

    public void example3()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();

        Object value = new PersonExtended("John","Smith",30,true);

        Document doc = itsNatDoc.getDocument();
        ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver(true);
        resolver.introspect("person",value);

        Element elem = doc.getElementById("elementId3");
        resolver.resolve(elem);
    }

    public void endExamplePanel()
    {
        this.label = null;

        ((EventTarget)linkToResolve).removeEventListener("click",this,false);
        this.linkToResolve = null;

        ((EventTarget)inputElem).removeEventListener("change",this,false);
        this.inputElem = null;
    }

    public void handleEvent(Event evt)
    {
        if (linkToResolve == evt.getCurrentTarget())
            label.setLabelValue(inputElem.getValue());
    }

}
