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

package org.itsnat.impl.comp.button.normal;

import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.event.ChangeEvent;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.comp.button.ItsNatButtonUI;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.ItsNatHTMLFormCompValueBasedImpl;
import org.itsnat.impl.comp.button.ItsNatButtonSharedImpl;
import org.itsnat.impl.comp.button.ItsNatHTMLFormCompButtonSharedImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLFormElement;

/**
 * El atributo type de un HTMLButtonElement puede ser "button","reset","submit"
 * pero en todos los casos el comportamiento, modelo y visualización es el mismo
 * (salvo la acción que generan) por lo que no derivamos nuevas clases.
 * Aunque un tipo "reset" o "submit" generen un reset o submit ante un click dicho evento
 * no es recogido por el propio botón, es decir onreset y onsubmit no son llamados
 * (es el <form> el que lo recibe)
 * No es el caso de HTMLInputElement en donde type="text" es totalmente diferente a
 * type="button"
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLButtonImpl extends ItsNatHTMLFormCompValueBasedImpl implements ItsNatHTMLButton, ItsNatButtonNormalInternal
{
    protected ItsNatButtonSharedImpl buttonDelegate = createItsNatButtonShared();
    protected ItsNatHTMLFormCompButtonSharedImpl htmlButtonDeleg = new ItsNatHTMLFormCompButtonSharedImpl(this);

    /** Creates a new instance of ItsNatHTMLButtonImpl */
    public ItsNatHTMLButtonImpl(HTMLButtonElement element, NameValue[] artifacts, ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(element, artifacts, componentMgr);
    }

    public ItsNatButtonSharedImpl getItsNatButtonShared()
    {
        return buttonDelegate;
    }

    public ItsNatButtonSharedImpl createItsNatButtonShared()
    {
        return new ItsNatButtonNormalSharedImpl(this);
    }

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        return null;
    }

    @Override
    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        enableEventListener("click"); // Por defecto se procesa, pues es lo importante
    }

    public HTMLButtonElement getHTMLButtonElement()
    {
        return (HTMLButtonElement)node;
    }

    public HTMLFormElement getHTMLFormElement()
    {
        return getHTMLButtonElement().getForm();  // Puede ser null, puede no estar dentro de un formulario
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return new ItsNatButtonNormalBasedUIImpl(this);
    }

    public void bindDataModel()
    {
        buttonDelegate.bindDataModel();
    }

    public void unbindDataModel()
    {
        buttonDelegate.unbindDataModel();
    }

    public void initialSyncUIWithDataModel()
    {
        buttonDelegate.initialSyncUIWithDataModel();
    }

    public void syncWithDataModel()
    {
        buttonDelegate.syncWithDataModel();
    }

    public void stateChanged(ChangeEvent e)
    {
        buttonDelegate.stateChanged(e);
    }

    public ButtonModel getButtonModel()
    {
        return (ButtonModel) dataModel;
    }

    public void setButtonModel(ButtonModel dataModel)
    {
        setDataModel(dataModel);
    }

    public boolean isEnabled()
    {
        // Está propiedad está en el modelo no sólo en el DOM
        // el modelo modifica el DOM via listeners pues es el modelo el que manda
        return getButtonModel().isEnabled();
    }

    public void setEnabled(boolean b)
    {
        // Está propiedad está en el modelo no sólo en el DOM
        // el modelo modificará el DOM via listeners pues es el modelo el que manda
        getButtonModel().setEnabled(b);
    }

    public void setDOMEnabled(boolean b)
    {
        // Llamada via button model listeners
        HTMLButtonElement element = getHTMLButtonElement();
        element.setDisabled( ! b );
    }

    @Override
    public void processNormalEvent(Event evt)
    {
        if (!htmlButtonDeleg.handleEvent(evt))
            return;

        super.processNormalEvent(evt);
    }

    public Object createDefaultModelInternal()
    {
        return createDefaultButtonModel();
    }

    public ButtonModel createDefaultButtonModel()
    {
        return new DefaultButtonModel();
    }

    public ItsNatButtonUI getItsNatButtonUI()
    {
        return (ItsNatButtonUI) compUI;
    }

    public Node createDefaultNode()
    {
        Document doc = getItsNatDocument().getDocument();
        HTMLButtonElement elem = (HTMLButtonElement) doc.createElementNS(NamespaceUtil.XHTML_NAMESPACE,"button");
        return elem;
    }
}
