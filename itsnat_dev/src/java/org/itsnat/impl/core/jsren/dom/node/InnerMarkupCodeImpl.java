/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.jsren.dom.node;

import java.lang.ref.WeakReference;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class InnerMarkupCodeImpl
{
    protected JSRenderElementImpl jsRender;
    protected WeakReference<Element> parentNodeRef; // No usamos una referencia normal pues supondria sujetar nodos inútilmente, pues cuando es usada todavía forma parte del documento y está sujeta por referencias normales, si se pierde no pasa nada porque devuelva null, no se usa para renderizar sólo para añadir nuevos trozos (implica que sigue referenciado)
    protected String parentNodeJSLocator;
    protected boolean useNodeLocation;
    protected StringBuilder innerMarkup = new StringBuilder();

    public InnerMarkupCodeImpl(JSRenderElementImpl jsRender,Element parentNode,String parentNodeJSLocator,boolean useNodeLocation,String firstInnerMarkup)
    {
        this.jsRender = jsRender;
        this.parentNodeRef = new WeakReference<Element>(parentNode);
        this.parentNodeJSLocator = parentNodeJSLocator;
        this.useNodeLocation = useNodeLocation;        
        innerMarkup.append(firstInnerMarkup);
    }

    public Element getParentNode()
    {
        return parentNodeRef.get();
    }

    public String getParentNodeJSLocator()
    {
        return parentNodeJSLocator;
    }

    public boolean isUseNodeLocation()
    {
        return useNodeLocation;
    }

    public void addInnerMarkup(String newInnerMarkup)
    {
        innerMarkup.append(newInnerMarkup);
    }

    public String getInnerMarkup()
    {
        return innerMarkup.toString();
    }

    @Override
    public String toString()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }
    
    public String render(ClientDocumentImpl clientDoc)
    {
        return jsRender.getAppendChildrenCodeAsMarkupSentence(this,(ClientDocumentStfulImpl)clientDoc);
    }
}
