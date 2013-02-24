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

package org.itsnat.impl.comp.mgr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.ItsNatHTMLForm;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchor;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchorLabel;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.button.normal.ItsNatHTMLButtonLabel;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.normal.ItsNatHTMLInputImage;
import org.itsnat.comp.button.normal.ItsNatHTMLInputReset;
import org.itsnat.comp.button.normal.ItsNatHTMLInputSubmit;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputRadio;
import org.itsnat.comp.iframe.ItsNatHTMLIFrame;
import org.itsnat.comp.label.ItsNatHTMLLabel;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.comp.list.ItsNatListCellEditor;
import org.itsnat.comp.table.ItsNatHTMLTable;
import org.itsnat.comp.table.ItsNatTableCellEditor;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.comp.text.ItsNatHTMLInputFile;
import org.itsnat.comp.text.ItsNatHTMLInputHidden;
import org.itsnat.comp.text.ItsNatHTMLInputPassword;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.comp.tree.ItsNatTreeCellEditor;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.ItsNatComponentImpl;
import org.itsnat.impl.comp.ItsNatHTMLFormImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatComponentImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatHTMLComponentImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatHTMLFormImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatHTMLIFrameImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatHTMLInputImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLAnchorDefaultImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLAnchorLabelImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLButtonDefaultImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLButtonLabelImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLInputButtonImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLInputImageImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLInputResetImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLInputSubmitImpl;
import org.itsnat.impl.comp.factory.button.toggle.FactoryItsNatHTMLInputCheckBoxImpl;
import org.itsnat.impl.comp.factory.button.toggle.FactoryItsNatHTMLInputRadioImpl;
import org.itsnat.impl.comp.factory.label.FactoryItsNatHTMLLabelImpl;
import org.itsnat.impl.comp.factory.list.FactoryItsNatHTMLSelectImpl;
import org.itsnat.impl.comp.factory.table.FactoryItsNatHTMLTableImpl;
import org.itsnat.impl.comp.factory.text.FactoryItsNatHTMLInputFileImpl;
import org.itsnat.impl.comp.factory.text.FactoryItsNatHTMLInputHiddenImpl;
import org.itsnat.impl.comp.factory.text.FactoryItsNatHTMLInputPasswordImpl;
import org.itsnat.impl.comp.factory.text.FactoryItsNatHTMLInputTextDefaultImpl;
import org.itsnat.impl.comp.factory.text.FactoryItsNatHTMLInputTextFormattedImpl;
import org.itsnat.impl.comp.factory.text.FactoryItsNatHTMLTextAreaImpl;
import org.itsnat.impl.comp.label.ItsNatLabelEditorDefaultImpl;
import org.itsnat.impl.comp.layer.ItsNatModalLayerImpl;
import org.itsnat.impl.comp.list.ItsNatListCellEditorDefaultImpl;
import org.itsnat.impl.comp.table.ItsNatTableCellEditorDefaultImpl;
import org.itsnat.impl.comp.tree.ItsNatTreeCellEditorDefaultImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLLabelElement;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 * Actualmente los namespaces con eventos no X/HTML como son SVG y XUL soportan
 * XHTML embebido en los principales navegadores (FireFox, Opera, Chrome y Safari).
 * 
 * @author jmarranz
 */
public abstract class ItsNatStfulDocComponentManagerImpl extends ItsNatDocComponentManagerImpl implements ItsNatHTMLComponentManager
{
    protected static final Map<String,FactoryItsNatHTMLComponentImpl> HTMLFACTORIES = new HashMap<String,FactoryItsNatHTMLComponentImpl>(); // No sincronizamos porque va a ser siempre usada en modo lectura

    static
    {
        addHTMLFactory(FactoryItsNatHTMLAnchorDefaultImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLAnchorLabelImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLButtonDefaultImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLButtonLabelImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLFormImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLIFrameImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLInputButtonImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLInputCheckBoxImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLInputFileImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLInputHiddenImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLInputImageImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLInputPasswordImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLInputRadioImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLInputResetImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLInputSubmitImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLInputTextDefaultImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLInputTextFormattedImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLLabelImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLSelectImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLTableImpl.SINGLETON);
        addHTMLFactory(FactoryItsNatHTMLTextAreaImpl.SINGLETON);
    }

    protected LinkedList<ItsNatModalLayerImpl> modalLayers;

    /** Creates a new instance of ItsNatStfulDocComponentManagerImpl */
    public ItsNatStfulDocComponentManagerImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    protected static void addHTMLFactory(FactoryItsNatHTMLComponentImpl factory)
    {
        HTMLFACTORIES.put(factory.getKey(),factory);
    }

    protected static FactoryItsNatHTMLComponentImpl getHTMLFactoryStatic(HTMLElement elem,String compType)
    {
        String key;
        if (elem instanceof HTMLInputElement)
            key = FactoryItsNatHTMLInputImpl.getKeyHTMLInput((HTMLInputElement)elem,compType);
        else
            key = FactoryItsNatHTMLComponentImpl.getKey(elem,compType);

        return HTMLFACTORIES.get(key);
    }

    @Override
    protected FactoryItsNatComponentImpl getFactoryItsNatComponent(Element elem,String compType)
    {
        FactoryItsNatComponentImpl factory = super.getFactoryItsNatComponent(elem,compType);
        if (factory != null) return factory;

        if (!(elem instanceof HTMLElement))
            return null;

        return getHTMLFactoryStatic((HTMLElement)elem,compType);
    }

    public static boolean declaredAsHTMLComponent(Element element)
    {
        if (!(element instanceof HTMLElement))
            return false;

        boolean decAsComp = declaredAsComponent(element);
        if (decAsComp) return true;

        // Buscamos por tag name
        FactoryItsNatHTMLComponentImpl factory = getHTMLFactoryStatic((HTMLElement)element,null);
        if (factory == null)
            return false;

        return factory.declaredAsHTMLComponent(element);
    }

    public ItsNatHTMLFormImpl getItsNatHTMLForm(HTMLFormElement formElem)
    {
        ItsNatHTMLFormImpl form = (ItsNatHTMLFormImpl)findItsNatComponent(formElem);
        //if (form == null)
        //    form = (ItsNatHTMLFormImpl)addItsNatHTMLForm(formElem,null);
        return form;
    }

    public ItsNatHTMLForm addItsNatHTMLForm(HTMLFormElement element,NameValue[] artifacts)
    {
        // NO SE USA
        ItsNatHTMLForm component = createItsNatHTMLForm(element,artifacts);
        addItsNatComponent(component);
        return component;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return (ItsNatStfulDocumentImpl)itsNatDoc;
    }

    public boolean hasItsNatModalLayers()
    {
        if (modalLayers == null) return false;
        return !modalLayers.isEmpty();
    }

    public LinkedList<ItsNatModalLayerImpl> getItsNatModalLayers()
    {
        if (modalLayers == null) this.modalLayers = new LinkedList<ItsNatModalLayerImpl>();
        return modalLayers;
    }

    public void addClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        if (hasItsNatModalLayers())
        {
            // Es el caso de modal layers ya mostrados, es útil notificar
            // para que también en este caso se redimensionen los modal layers
            // en los observadores cuando cambia el tamaño de la ventana
            // o el layout y en MSIE v6 para ocultar también los HTML select
            // que están "detrás".
            LinkedList<ItsNatModalLayerImpl> modalLayers = getItsNatModalLayers();
            for(ItsNatModalLayerImpl comp : modalLayers)
            {
                comp.addClientDocumentAttachedClient(clientDoc);
            }
        }

        if (hasItsNatComponents())
        {
            WeakHashMap<ItsNatComponent,Object> compMap = getItsNatComponentWeakMap();
            for(Map.Entry<ItsNatComponent,Object> entry : compMap.entrySet())
            {
                ItsNatComponent comp = entry.getKey();
                if (comp instanceof ItsNatModalLayerImpl) continue; // Evitamos llamar dos veces, los modal layer deben llamarse en el orden de creación
                else if (!(comp instanceof ItsNatComponentImpl)) continue; // Componente del usuario

                ((ItsNatComponentImpl)comp).addClientDocumentAttachedClient(clientDoc);
            }
        }
    }

    public void removeClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        if (hasItsNatModalLayers())
        {
            LinkedList<ItsNatModalLayerImpl> modalLayers = getItsNatModalLayers();
            for(ItsNatModalLayerImpl comp : modalLayers)
            {
                // Da igual el orden en que se itere pues total el cliente se está cerrando
                // NO debería hacerse nada más que desregistrar pues el cliente puede
                // estar ya invalidado.
                comp.removeClientDocumentAttachedClient(clientDoc);
            }
        }

        if (hasItsNatComponents())
        {
            WeakHashMap<ItsNatComponent,Object> compMap = getItsNatComponentWeakMap();
            for(Map.Entry<ItsNatComponent,Object> entry : compMap.entrySet())
            {
                ItsNatComponent comp = entry.getKey();
                if (comp instanceof ItsNatModalLayerImpl) continue; // Evitamos llamar dos veces, los modal layer deben llamarse en el orden de creación
                else if (!(comp instanceof ItsNatComponentImpl)) continue; // Componente del usuario

                ((ItsNatComponentImpl)comp).removeClientDocumentAttachedClient(clientDoc);
            }
        }
    }

    public ItsNatHTMLLabel createItsNatHTMLLabel(HTMLLabelElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLLabelImpl.SINGLETON.createItsNatHTMLLabel(element,artifacts,true,this);
    }

    public ItsNatHTMLAnchor createItsNatHTMLAnchor(HTMLAnchorElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLAnchorDefaultImpl.SINGLETON.createItsNatHTMLAnchorDefault(element,artifacts,true,this);
    }

    public ItsNatHTMLAnchorLabel createItsNatHTMLAnchorLabel(HTMLAnchorElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLAnchorLabelImpl.SINGLETON.createItsNatHTMLAnchorLabel(element,artifacts,true,this);
    }

    public ItsNatHTMLForm createItsNatHTMLForm(HTMLFormElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLFormImpl.SINGLETON.createItsNatHTMLForm(element,artifacts,true,this);
    }

    public ItsNatHTMLInputText createItsNatHTMLInputText(HTMLInputElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLInputTextDefaultImpl.SINGLETON.createItsNatHTMLInputTextDefault(element,artifacts,true,this);
    }

    public ItsNatHTMLInputTextFormatted createItsNatHTMLInputTextFormatted(HTMLInputElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLInputTextFormattedImpl.SINGLETON.createItsNatHTMLInputTextFormatted(element,artifacts,true,this);
    }

    public ItsNatHTMLInputPassword createItsNatHTMLInputPassword(HTMLInputElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLInputPasswordImpl.SINGLETON.createItsNatHTMLInputPassword(element,artifacts,true,this);
    }

    public ItsNatHTMLInputCheckBox createItsNatHTMLInputCheckBox(HTMLInputElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLInputCheckBoxImpl.SINGLETON.createItsNatHTMLInputCheckBox(element,artifacts,true,this);
    }

    public ItsNatHTMLInputRadio createItsNatHTMLInputRadio(HTMLInputElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLInputRadioImpl.SINGLETON.createItsNatHTMLInputRadio(element,artifacts,true,this);
    }

    public ItsNatHTMLInputSubmit createItsNatHTMLInputSubmit(HTMLInputElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLInputSubmitImpl.SINGLETON.createItsNatHTMLInputSubmit(element,artifacts,true,this);
    }

    public ItsNatHTMLInputReset createItsNatHTMLInputReset(HTMLInputElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLInputResetImpl.SINGLETON.createItsNatHTMLInputReset(element,artifacts,true,this);
    }

    public ItsNatHTMLInputButton createItsNatHTMLInputButton(HTMLInputElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLInputButtonImpl.SINGLETON.createItsNatHTMLInputButton(element,artifacts,true,this);
    }

    public ItsNatHTMLInputImage createItsNatHTMLInputImage(HTMLInputElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLInputImageImpl.SINGLETON.createItsNatHTMLInputImage(element,artifacts,true,this);
    }

    public ItsNatHTMLInputHidden createItsNatHTMLInputHidden(HTMLInputElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLInputHiddenImpl.SINGLETON.createItsNatHTMLInputHidden(element,artifacts,true,this);
    }

    public ItsNatHTMLInputFile createItsNatHTMLInputFile(HTMLInputElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLInputFileImpl.SINGLETON.createItsNatHTMLInputFile(element,artifacts,true,this);
    }

    public ItsNatHTMLSelectMult createItsNatHTMLSelectMult(HTMLSelectElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLSelectImpl.SINGLETON.createItsNatHTMLSelectMult(element,artifacts,true,this);
    }

    public ItsNatHTMLSelectComboBox createItsNatHTMLSelectComboBox(HTMLSelectElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLSelectImpl.SINGLETON.createItsNatHTMLSelectComboBox(element,artifacts,true,this);
    }

    public ItsNatHTMLTextArea createItsNatHTMLTextArea(HTMLTextAreaElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLTextAreaImpl.SINGLETON.createItsNatHTMLTextArea(element,artifacts,true,this);
    }

    public ItsNatHTMLButton createItsNatHTMLButton(HTMLButtonElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLButtonDefaultImpl.SINGLETON.createItsNatHTMLButtonDefault(element,artifacts,true,this);
    }

    public ItsNatHTMLButtonLabel createItsNatHTMLButtonLabel(HTMLButtonElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLButtonLabelImpl.SINGLETON.createItsNatHTMLButtonLabel(element,artifacts,true,this);
    }

    public ItsNatHTMLTable createItsNatHTMLTable(HTMLTableElement element,ItsNatTableStructure structure,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLTableImpl.SINGLETON.createItsNatHTMLTable(element,structure,artifacts,true,this);
    }

    public ItsNatHTMLIFrame createItsNatHTMLIFrame(HTMLIFrameElement element,NameValue[] artifacts)
    {
        return FactoryItsNatHTMLIFrameImpl.SINGLETON.createItsNatHTMLIFrame(element,artifacts,true,this);
    }

    public ItsNatLabelEditor createDefaultItsNatLabelEditor(ItsNatComponent compEditor)
    {
        return new ItsNatLabelEditorDefaultImpl(compEditor,this);
    }

    public ItsNatListCellEditor createDefaultItsNatListCellEditor(ItsNatComponent compEditor)
    {
        return new ItsNatListCellEditorDefaultImpl(compEditor,this);
    }

    public ItsNatTableCellEditor createDefaultItsNatTableCellEditor(ItsNatComponent compEditor)
    {
        return new ItsNatTableCellEditorDefaultImpl(compEditor,this);
    }

    public ItsNatTreeCellEditor createDefaultItsNatTreeCellEditor(ItsNatComponent compEditor)
    {
        return new ItsNatTreeCellEditorDefaultImpl(compEditor,this);
    }
}
