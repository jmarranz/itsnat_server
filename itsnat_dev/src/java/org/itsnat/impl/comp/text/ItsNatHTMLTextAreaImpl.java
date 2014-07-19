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

package org.itsnat.impl.comp.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.itsnat.impl.comp.ItsNatHTMLFormCompValueBasedImpl;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.comp.text.ItsNatTextAreaUI;
import org.itsnat.comp.text.ItsNatTextComponentUI;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByClientImpl;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.scriptren.jsren.JSRenderMethodCallImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLTextAreaImpl extends ItsNatHTMLFormCompValueBasedImpl implements ItsNatHTMLTextArea,ItsNatHTMLFormTextComponentInternal
{
    protected ItsNatHTMLFormTextCompMarkupDrivenUtil markupDrivenUtil;
    protected ItsNatHTMLFormTextCompSharedImpl changeBasedDelegate = new ItsNatHTMLFormTextCompSharedImpl(this);

    /**
     * Creates a new instance of ItsNatHTMLTextAreaImpl
     */
    public ItsNatHTMLTextAreaImpl(HTMLTextAreaElement element,NameValue[] artifacts,ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        this.markupDrivenUtil = ItsNatHTMLFormTextCompMarkupDrivenUtil.initMarkupDriven(this);

        init();
    }

    public void init()
    {
        changeBasedDelegate.init();

        super.init();
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();

        changeBasedDelegate.writeListeners(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        changeBasedDelegate.readListeners(in);
    }

    public ItsNatHTMLFormTextCompSharedImpl getItsNatHTMLFormTextCompShared()
    {
        return changeBasedDelegate;
    }

    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        changeBasedDelegate.enableEventListenersByDoc();
    }

    public void enableEventListenersByClient(ItsNatCompDOMListenersByClientImpl domListeners)
    {
        super.enableEventListenersByClient(domListeners);

        changeBasedDelegate.enableEventListenersByClient(domListeners);
    }

    public void processNormalEvent(Event evt)
    {
        changeBasedDelegate.processNormalEvent(evt);

        super.processNormalEvent(evt);
    }

    public void handleEventOnChange(Event evt)
    {
        changeBasedDelegate.handleEventOnChange(evt);
    }

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        return changeBasedDelegate.getInternalParamTransports(type,clientDoc);
    }

    public void postHandleEventOnChange(Event evt)
    {
        // Nada que hacer
    }

    public void postHandleEventOnKeyUp(Event evt)
    {
        // Nada que hacer
    }

    public HTMLFormElement getHTMLFormElement()
    {
        return getHTMLTextAreaElement().getForm();
    }

    public HTMLTextAreaElement getHTMLTextAreaElement()
    {
        return (HTMLTextAreaElement)node;
    }

    public void setNewValueOnChange(String newValue, Event evt)
    {
        // Al final se hará un setValue al HTMLTextAreaElement pero a través del modelo de datos
        changeBasedDelegate.incrementalChange(newValue);
    }

    public void setNewValueOnKeyUp(String newValue,Event evt)
    {
        changeBasedDelegate.incrementalChange(newValue);
    }

    public String getText()
    {
        return changeBasedDelegate.getText();
    }

    public void setText(String t)
    {
        changeBasedDelegate.setText(t);
    }

    public Document getDocument()
    {
        // Ojo es el javax.swing.text.Document del Swing
        return (Document)dataModel;
    }

    public void setDocument(Document dataModel)
    {
        setDataModel(dataModel);
    }

    public ItsNatTextComponentUI getItsNatTextComponentUI()
    {
        return (ItsNatTextComponentUI)compUI;
    }

    public ItsNatTextAreaUI getItsNatTextAreaUI()
    {
        return (ItsNatTextAreaUI)compUI;
    }

    public ItsNatHTMLFormTextCompUIImpl getItsNatHTMLFormTextCompUIImpl()
    {
        return (ItsNatHTMLFormTextCompUIImpl)compUI;
    }

    public ItsNatHTMLTextAreaUIImpl getItsNatHTMLTextAreaUIImpl()
    {
        return (ItsNatHTMLTextAreaUIImpl)compUI;
    }
    
    public ItsNatTextAreaUI createDefaultItsNatHTMLTextAreaUI()
    {
        return new ItsNatHTMLTextAreaUIImpl(this);
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return createDefaultItsNatHTMLTextAreaUI();
    }

    public void bindDataModel()
    {
        changeBasedDelegate.bindDataModel();
    }

    public void unbindDataModel()
    {
        changeBasedDelegate.unbindDataModel();
    }

    public void initialSyncUIWithDataModel()
    {
        changeBasedDelegate.initialSyncUIWithDataModel();

        if (markupDrivenUtil != null)
            markupDrivenUtil.initialSyncUIWithDataModel();
    }

    public void insertUpdate(DocumentEvent e)
    {
        changeBasedDelegate.insertUpdate(e);
    }

    public void removeUpdate(DocumentEvent e)
    {
        changeBasedDelegate.removeUpdate(e);
    }

    public void changedUpdate(DocumentEvent e)
    {
        changeBasedDelegate.changedUpdate(e);
    }


    public void blur()
    {
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocumentImpl();
        JSRenderMethodCallImpl.addCallMethodHTMLFormControlCode(getHTMLElement(),"blur",itsNatDoc);
    }

    public void focus()
    {
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocumentImpl();
        JSRenderMethodCallImpl.addCallMethodHTMLFormControlCode(getHTMLElement(),"focus",itsNatDoc);
    }

    public void select()
    {
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocumentImpl();
        JSRenderMethodCallImpl.addCallMethodHTMLFormControlCode(getHTMLElement(),"select",itsNatDoc);
    }

    public Object createDefaultModelInternal()
    {
        return createDefaultDocument();
    }

    public Document createDefaultDocument()
    {
        return new PlainDocument();
    }

    public void appendString(String str)
    {
        changeBasedDelegate.appendString(str);
    }

    public void replaceString(String str, int start, int end)
    {
        changeBasedDelegate.replaceString(str,start,end);
    }

    public void insertString(String str, int pos)
    {
        changeBasedDelegate.insertString(str,pos);
    }

    public String getText(int offs, int len)
    {
        return changeBasedDelegate.getText(offs,len);
    }

    public Node createDefaultNode()
    {
        HTMLTextAreaElement elem = (HTMLTextAreaElement)getItsNatDocument().getDocument().createElementNS(NamespaceUtil.XHTML_NAMESPACE,"textarea");
        elem.setCols(10); // atributo obligatorio
        elem.setRows(5); // atributo obligatorio
        return elem;
    }

    public void setDefaultDataModel(Object dataModel)
    {
        if (markupDrivenUtil != null)
        {
            HTMLTextAreaElement elem = getHTMLTextAreaElement();
            CharacterData text = (CharacterData)elem.getFirstChild();
            String data;
            if (text != null) data = text.getData();
            else data = "";
            DOMUtilInternal.setAttribute(elem,"value", data);
        }

        if (markupDrivenUtil != null)
            markupDrivenUtil.preSetDefaultDataModel(dataModel);

        super.setDefaultDataModel(dataModel);
    }

    public void disposeEffective(boolean updateClient)
    {
        super.disposeEffective(updateClient);

        if (markupDrivenUtil != null)
        {
            markupDrivenUtil.dispose();
            this.markupDrivenUtil = null;
        }
    }

    public boolean isMarkupDriven()
    {
        return markupDrivenUtil != null;
    }

    public void setMarkupDriven(boolean value)
    {
        this.markupDrivenUtil = ItsNatHTMLFormTextCompMarkupDrivenUtil.setMarkupDriven(this, markupDrivenUtil, value);
    }

    public boolean isEnabled()
    {
        HTMLTextAreaElement element = getHTMLTextAreaElement();
        return !element.getDisabled();
    }

    public void setEnabled(boolean b)
    {
        HTMLTextAreaElement element = getHTMLTextAreaElement();
        element.setDisabled( ! b );
    }

    public void afterRender(Node node,MutationEvent evt)
    {
        super.afterRender(node,evt);

        String type = evt.getType();
        if (type.equals("DOMNodeInserted"))
        {
            // Si insertamos un nodo texarea
            // que el usuario creó de la siguiente forma (Java):
            //   Element elem = ...;
            //   Element textarea = document.createElement("textarea");
            //   textarea.setAttribute("value","HOLA \n QUE TAL");
            //   elem.appendChild(textarea);

            // Se inserta el textarea pero el valor visual NO es el dado, sino
            // el contenido del nodo de texto del <textarea> es decir la cadena vacía.
            // Ciertamente al código Java ejecutado en el cliente cmo JavaScript también le pasa lo mismo
            // sin embargo esto no ocurre si fuera un <input> pues el atributo value
            // es la única fuente inicial del valor del control (en textarea el atributo
            // value NO es la fuente inicial del control, es el nodo interno).
            // De igual manera ocurre insertando el textarea via innerHTML
            // con el valor en la propiedad value y sin nodo de texto => manda visualmente el nodo (vacío) de texto.
            // Sin embargo en JavaScript podríamos hacer:
            //   var textarea = document.createElement("textarea");
            //   textarea.value = "HOLA \n QUE TAL";
            //   elem.appendChild(textarea);
            // En este caso el control muestra el valor dado.
            // El problema es que en W3C Java no distinguimos entre propiedad y atributo
            // y sólo se renderiza la propiedad junto al atributo cuando el nodo está ya insertado.

            // Cuando renderizamos via métodos DOM (no innerHTML) usamos
            // normalmente llamadas setAttribute antes de insertar el objeto
            // y la propiedad value no se usa, y cuando es via innerHTML
            // definimos en el markup el atributo pero la propiedad no se toca

            // Por tanto el valor inicial del control lo toma del nodo de texto hijo, no del atributo "value"
            // Si fuera código del usuario no habría problema, él mismo al reinsertar un nodo
            // sería responsable de añadir el texto dentro de <textarea>, pero este no
            // es el caso pues estamos dentro de un componente cuya finalidad es que el usuario
            // no toque el DOM por debajo.

            // Por ello obligamos a que se defina la propiedad "value" incondicionalmente tras la reinserción,
            // lo haremos a través de la definición del atributo "value" que automáticamente ItsNat cambia
            // también la propiedad via JavaScript.
            // Hemos de evitar que la llamada no haga nada al detectar que no ha habido cambio en el valor del atributo
            // por ello llamamos primero con valor "" o similar.

            // Esto NO es necesario en XUL ni en SVG (comprobado en FireFox 3.5, Opera 9.x, Chrome 1.0 y Safari 3.x)
            // pues fuera de X/HTML el nodo de texto del textarea ES IGNORADO, sólo cuenta el atributo/propiedad "value".


            ItsNatHTMLTextAreaUIImpl compUI = getItsNatHTMLTextAreaUIImpl();
            String value = getText(); // Coincide con el valor del atributo "value"
            Element textArea = (Element)node;
            String content = DOMUtilInternal.getTextContent(textArea,false); // Nunca es nulo
            if (content.equals(value)) return; // Nada que hacer
            // Obligamos a que la siguiente sentencia con el valor bueno se ejecute sí o sí
            if (value.equals("")) compUI.setText("-reset-");
            else compUI.setText("");
            compUI.setText(value); // Ahora el valor bueno.
        }
    }

}
