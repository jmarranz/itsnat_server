/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.comp.layer;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.scriptren.jsren.node.JSRenderElementImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatModalLayerClientDocImpl implements Serializable
{
    protected ItsNatModalLayerImpl parentComp;
    protected ClientDocumentStfulDelegateWebImpl clientDoc;

    public ItsNatModalLayerClientDocImpl(ItsNatModalLayerImpl parentComp,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        this.parentComp = parentComp;
        this.clientDoc = clientDoc;
    }

    public ItsNatModalLayerImpl getItsNatModalLayer()
    {
        return parentComp;
    }

    public ClientDocumentStfulDelegateWebImpl getClientDocumentStfulDelegateWeb()
    {
        return clientDoc;
    }

    public void attachClientToComponent()
    {
        // Caso de cliente control remoto.
        initModalLayer();

        showHideBodyElements(true);
    }

    public void postInsertLayer()
    {
        initModalLayer();

        showHideBodyElements(true);
    }

    public void postRemoveLayer()
    {
        // Mostrar de nuevo los elementos bajo BODY
        showHideBodyElements(false);
    }

    public boolean isCleanBelowMode()
    {
        // Se redefine en navegadores con "EverClean"
        return parentComp.isCleanBelowMode();
    }

    protected void showHideBodyElements(boolean hide)
    {
        if (!isCleanBelowMode()) return;

        LinkedHashSet<Element> bodyElementsBefore = getItsNatModalLayer().getBodyElementsBefore();

        Element[] reverseBodyElements = new Element[bodyElementsBefore.size()];
        if (hide)
        {
            // Recorreremos el orden en inverso para evitar el "parpadeo" o "efecto persiana"
            // propio de ocultar/mostrar desde el primero, es más rápido para el navegador.
            int i = reverseBodyElements.length - 1;
            for(Iterator<Element> it = bodyElementsBefore.iterator(); it.hasNext(); i--)
            {
                Element elem = it.next();
                reverseBodyElements[i] = elem;
            }
        }
        else
        {
            reverseBodyElements = bodyElementsBefore.toArray(reverseBodyElements);
        }

        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();
        StringBuilder code = new StringBuilder();
        for(int i = 0; i < reverseBodyElements.length; i++ )
        {
            Element elem = reverseBodyElements[i];
            JSRenderElementImpl render = JSRenderElementImpl.getJSRenderElement(elem,clientDoc);
            String elemRef = clientDoc.getNodeReference(elem,true,true);
            // No usar el nombre "elem" por si acaso porque es usado por el modal layer
            code.append("var elem = " + elemRef + ";\n");
            renderShowHide(elem,"elem",hide,code,render);
        }
        clientDoc.addCodeToSend(code.toString());
    }

    protected void renderShowHide(Element elem,String elemVarName,boolean hide,StringBuilder code,JSRenderElementImpl render)
    {
        ClientDocumentStfulDelegateWebImpl clientDoc = getClientDocumentStfulDelegateWeb();
        if (hide)
        {
            code.append(render.getBackupAndSetStyleProperty(elemVarName,"display","none",clientDoc));
        }
        else
        {
            code.append(render.getRestoreBackupStyleProperty(elemVarName,"display",clientDoc) );
        }
    }

    public int getTimeout()
    {
        // Los navegadores móviles no tienen redimensionamiento de la ventana, sin embargo el tamaño de la página puede
        // verse afectado por lo que se ponga encima de los layers modales. Por tanto es deseable
        // la actualización por timer pero no es necesario forzar porque son máquinas poco potentes.
        BrowserWeb browser = clientDoc.getBrowserWeb();

        if (browser.isMobile())
        {
            return 1000; // Hay que tener en cuenta que si se crean varios layers hay más frecuencia de parpadeos
        }
        else return 500; // 250 10000
    }

    public abstract void preRemoveLayer();
    public abstract void initModalLayer();
}
