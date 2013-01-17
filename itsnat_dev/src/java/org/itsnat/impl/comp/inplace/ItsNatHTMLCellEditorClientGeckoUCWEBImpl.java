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

package org.itsnat.impl.comp.inplace;

import org.itsnat.comp.ItsNatComponent;
import org.itsnat.core.event.ItsNatDOMStdEvent;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.core.browser.BrowserGeckoUCWEB;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLCellEditorClientGeckoUCWEBImpl extends ItsNatCellEditorClientImpl
{
    protected static final ItsNatHTMLCellEditorClientGeckoUCWEBImpl SINGLETON = new ItsNatHTMLCellEditorClientGeckoUCWEBImpl();

    public ItsNatHTMLCellEditorClientGeckoUCWEBImpl()
    {
    }

    @Override
    public void handleGlobalEvent(Event evt,ItsNatCellEditorImpl parent)
    {
        // Redefinimos totalmente en UCWEB porque en este navegador
        // los controles de texto (input text/password etc y textarea) no se emiten eventos click,
        // focus, change y blur por interacción del usuario, por lo que cuando
        // se detecta cualquier evento DOM se fuerza el envío de dichos eventos
        // a TODOS los controles de texto, esto provoca que este global listener
        // detecte incorrectamente que debemos de cancelar el elemento en edición
        // el cual posiblemente haya cambiado y no hemos tenido todavía la oportunidad
        // de simular el change.
        // Por otra parte estos eventos enviados manualmente (click,focus,change,blur) se envían antes que los eventos
        // normales de los controles que funcionan bien (select, input checkbox etc),
        // lo cual impide que el evento change llegue el primero provocando que el global
        // event listener cancele la edición sin haberse actualizado.
        // En definitiva, tenemos la garantía de que se va a emitir un change (si procede) y un blur
        // manualmente para el elemento editor si es un control problemático
        // y si es un control que funciona bien evitaremos quitarlo en el caso
        // de los eventos manuales y en el caso del change terminaremos la edición.

        if (evt instanceof ItsNatDOMStdEvent)
        {
            EventTarget target = evt.getTarget();

            ClientDocumentStfulImpl clientDoc = (ClientDocumentStfulImpl)((ItsNatEvent)evt).getClientDocument();
            BrowserGeckoUCWEB browser = (BrowserGeckoUCWEB)clientDoc.getBrowser();
            if (browser.isHTMLTextControlPassive((Node)target))
                return;

            ItsNatComponent compEditor = parent.getEditorDelegate().getCellEditorComponent();
            Node nodeEditor = compEditor.getNode();
            if (nodeEditor == target)
            {
                // Es un control que emite algun evento, si es el evento change o click en el caso de un select o un click en un checkbox o radio (no se usa
                // radio en edición pero en un futuro...) lo procesaremos normalmente y luego
                // quitaremos la edición pues el blur NO se emite. Hay que recordar que
                // en checkbox y radio es el click el que hace el cambio de estado (pues hay navegadores que no emiten el change).

                // El click en select usado como blur ya se hace en otro sitio

                // Procesamos primero el comportamiento por defecto
                ((ItsNatEvent)evt).getItsNatEventListenerChain().continueChain();

                String type = evt.getType();
                if (("click".equals(type) && DOMUtilHTML.isHTMLInputCheckBoxOrRadio(nodeEditor)) ||
                    ("change".equals(type) && (nodeEditor instanceof HTMLSelectElement)) )
                    parent.stopCellEditing();
            }
            else parent.stopCellEditing(); // Es el caso de un control que emite eventos change o click pero no blur y que no ha sido cambiado y que se ha tocado cualquier otro elemento no filtrado y que emite eventos (ej. un link, otro control form etc)
        }
    }

    @Override
    public void handleEvent(Event evt,ItsNatCellEditorImpl parent,ClientDocumentStfulImpl clientDoc)
    {
        super.handleEvent(evt, parent, clientDoc);

        // Caso del click en <select>

        // Se emite el click siempre después del change (si se emite el change),
        // por lo que podemos usar el click como un blur.
        // El propio change tras el proceso normal se utiliza para quitar el componente,
        // esto se hace más arriba en el event listener global.
        ItsNatComponent compEditor = parent.getCellEditorComponent();
        Node nodeEditor = compEditor.getNode();
        if (nodeEditor instanceof HTMLSelectElement)
            parent.stopCellEditing();
    }

    @Override
    public void registerEventListeners(ItsNatCellEditorImpl parent,ClientDocumentStfulImpl clientDoc)
    {
        super.registerEventListeners(parent,clientDoc);

        // Caso del click en <select>
        // Se emite el click siempre después del change, si se emite el change,
        // por lo que podemos usar el click como un blur en el caso de no cambiar
        // Como el componente ItsNat para el select no añade el listener click por defecto, tenemos
        // que registrar uno explícitamente aquí.

        ItsNatComponent compEditor = parent.getCellEditorComponent();
        Node nodeEditor = compEditor.getNode();
        if (nodeEditor instanceof HTMLSelectElement)
            clientDoc.addEventListener((EventTarget)nodeEditor,"click",parent,false);
    }

    @Override
    public void unregisterEventListeners(ItsNatCellEditorImpl parent,ClientDocumentStfulImpl clientDoc)
    {
        super.unregisterEventListeners(parent,clientDoc);

        ItsNatComponent compEditor = parent.getCellEditorComponent();
        Node nodeEditor = compEditor.getNode();
        if (nodeEditor instanceof HTMLSelectElement)
            clientDoc.removeEventListener((EventTarget)nodeEditor,"click",parent,false);
    }
}
