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
import org.itsnat.impl.comp.ItsNatHTMLFormCompChangeBasedSharedImpl;
import org.itsnat.core.ItsNatException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLFormTextCompSharedImpl extends ItsNatHTMLFormCompChangeBasedSharedImpl
{
    /**
     * Creates a new instance of ItsNatTextBasedSharedImpl
     */
    public ItsNatHTMLFormTextCompSharedImpl(ItsNatHTMLFormTextComponentInternal comp)
    {
        super(comp);
    }

    public void writeListeners(ObjectOutputStream out) throws IOException
    {
        // Este código es debido a que hay un bug al de-serializar AbstractDocument, el atributo
        // "EventListenerList listenerList" que es serializable se pierde al hacer absurdamente
        // un: listenerList = new EventListenerList();  tras deserializarlo, perdiendo los listeners
        // Este error está confirmado en 1.4.2_16 y en 1.6.0_18
        // Sabemos que se insertan en esta colección los DocumentListener y UndoableEditListener
        // los DocumentListener se usan en ItsNat pero incluimos UndoableEditListener por gentileza
        // al programador.

        DocumentListener[] docListeners = null;
        UndoableEditListener[] undoListeners = null;
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();
        javax.swing.text.Document dataModel = comp.getDocument();
        if (dataModel instanceof AbstractDocument)
        {
            docListeners = ((AbstractDocument)dataModel).getDocumentListeners();
            if (docListeners.length == 0)
                docListeners = null;
            undoListeners = ((AbstractDocument)dataModel).getUndoableEditListeners();
            if (undoListeners.length == 0)
                undoListeners = null;
        }

        out.writeObject(docListeners);
        out.writeObject(undoListeners);
    }

    public void readListeners(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        DocumentListener[] docListeners = (DocumentListener[])in.readObject();
        if (docListeners != null && docListeners.length > 0)
        {
            ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();
            AbstractDocument dataModel = (AbstractDocument)comp.getDocument();
            if (dataModel.getDocumentListeners().length == 0)
            {
                // Persiste el bug, no ha sido arreglado pues hemos salvado algún listener
                for(int i = 0; i < docListeners.length; i++)
                {
                    DocumentListener listener = docListeners[i];
                    dataModel.addDocumentListener(listener);
                }
            }
        }
        UndoableEditListener[] undoListeners = (UndoableEditListener[])in.readObject();
        if (undoListeners != null && undoListeners.length > 0)
        {
            ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();
            AbstractDocument dataModel = (AbstractDocument)comp.getDocument();
            if (dataModel.getUndoableEditListeners().length == 0)
            {
                // Persiste el bug, no ha sido arreglado pues hemos salvado algún listener
                for(int i = 0; i < undoListeners.length; i++)
                {
                    UndoableEditListener listener = undoListeners[i];
                    dataModel.addUndoableEditListener(listener);
                }
            }
        }
    }


    public ItsNatHTMLFormTextComponentInternal getItsNatHTMLFormTextComponentInternal()
    {
        return (ItsNatHTMLFormTextComponentInternal)comp;
    }

    public boolean isIgnoreChangeEvent(ClientDocumentImpl clientDoc)
    {
        return false;
    }

    public void bindDataModel()
    {
        // A partir de ahora los cambios los repercutimos en el DOM por eventos
        // No se debe cambiar el DOM por otra vía que por el objeto dataModel
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();
        Document dataModel = comp.getDocument();
        dataModel.addDocumentListener(comp);
    }

    public void unbindDataModel()
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();
        Document dataModel = comp.getDocument();
        dataModel.removeDocumentListener(comp);
    }

    public void initialSyncUIWithDataModel()
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        ItsNatHTMLFormTextCompUIImpl compUI = comp.getItsNatHTMLFormTextCompUIImpl();
        Document dataModel = comp.getDocument();

        String str;
        try
        {
            str = dataModel.getText(0,dataModel.getLength());
        }
        catch(BadLocationException ex)
        {
            throw new ItsNatException(ex,comp);
        }


        ItsNatHTMLFormCompValueBasedImpl compBase = (ItsNatHTMLFormCompValueBasedImpl)comp; // A día de hoy todos los componentes texto son elementos de formulario HTML, en el futuro ya veremos
        // Sincronizamos con el DOM
        boolean wasDisabled = compBase.disableSendCodeToRequesterIfServerUpdating();
        try
        {
            compUI.setText(str);
        }
        finally
        {
            if (wasDisabled) compBase.enableSendCodeToRequester();
        }
    }

    public void insertUpdate(DocumentEvent e)
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        ItsNatHTMLFormCompValueBasedImpl compBase = (ItsNatHTMLFormCompValueBasedImpl)comp; // A día de hoy todos los componentes texto son elementos de formulario HTML, en el futuro ya veremos
        if (!compBase.isUIEnabled()) return;

        ItsNatHTMLFormTextCompUIImpl compUI = comp.getItsNatHTMLFormTextCompUIImpl();
        // Sincronizamos con el DOM
        Document dataModel = e.getDocument();
        int offset = e.getOffset();
        int len = e.getLength();

        String str;
        try
        {
            str = dataModel.getText(offset,len);
        }
        catch(BadLocationException ex)
        {
            throw new ItsNatException(ex,comp);
        }

        // Sincronizamos con el DOM
        boolean wasDisabled = compBase.disableSendCodeToRequesterIfServerUpdating();
        try
        {
            compUI.insertString(offset,str);
        }
        finally
        {
            if (wasDisabled) compBase.enableSendCodeToRequester();
        }
    }

    public void removeUpdate(DocumentEvent e)
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        ItsNatHTMLFormCompValueBasedImpl compBase = (ItsNatHTMLFormCompValueBasedImpl)comp; // A día de hoy todos los componentes texto son elementos de formulario HTML, en el futuro ya veremos
        if (!compBase.isUIEnabled()) return;

        ItsNatHTMLFormTextCompUIImpl compUI = comp.getItsNatHTMLFormTextCompUIImpl();
        // Sincronizamos con el DOM
        int offset = e.getOffset();
        int len = e.getLength();

        boolean wasDisabled = compBase.disableSendCodeToRequesterIfServerUpdating();
        try
        {
            compUI.removeString(offset,len);
        }
        finally
        {
            if (wasDisabled) compBase.enableSendCodeToRequester();
        }
    }

    public void changedUpdate(DocumentEvent e)
    {
        // No hacemos nada pues no se gestionan atributos, en un futuro...
    }

    public String getText()
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        Document dataModel = comp.getDocument();
        return getText(0,dataModel.getLength());
    }

    public String getText(int offs, int len)
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        return getText(comp.getDocument(),offs,len);
    }

    public static String getText(Document dataModel,int offs, int len)
    {
        try
        {
            return dataModel.getText(offs, len);
        }
        catch(BadLocationException ex)
        {
            throw new ItsNatException(ex,dataModel);
        }
    }

    public void setText(String t)
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        Document dataModel = comp.getDocument();
        setText(dataModel,t);
    }

    public static void setText(Document dataModel,String t)
    {
        String old = getText(dataModel,0,dataModel.getLength());
        if (t.equals(old))
            return; // Evitamos llamar a replaceString el cual (AbstractDocument.replace) aunque no haya cambios elimina el contenido actual y añade el nuevo
        replaceString(dataModel,t,0,dataModel.getLength());
    }

    public void appendString(String str)
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        Document dataModel = comp.getDocument();
        insertString(str,dataModel.getLength());
    }

    public void replaceString(String str, int start, int end)
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        replaceString(comp.getDocument(),str,start,end);
    }

    public static void replaceString(Document dataModel,String str, int start, int end)
    {
        if (end < start)
            throw new ItsNatException("end before start",dataModel);

        try
        {
            if (dataModel instanceof AbstractDocument)
            {
                ((AbstractDocument)dataModel).replace(start, end - start, str, null);
            }
            else
            {
                dataModel.remove(start, end - start);
                dataModel.insertString(start, str, null);
            }
        }
        catch (BadLocationException ex)
        {
            throw new ItsNatException(ex,dataModel);
        }
    }

    public void insertString(String str, int pos)
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        Document dataModel = comp.getDocument();
        try
        {
            dataModel.insertString(pos, str, null);
        }
        catch (BadLocationException ex)
        {
            throw new ItsNatException(ex,comp);
        }
    }

    public void remove(int pos,int length)
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        Document dataModel = comp.getDocument();
        try
        {
            dataModel.remove(pos,length);
        }
        catch (BadLocationException ex)
        {
            throw new ItsNatException(ex,comp);
        }
    }

    public void fullChange(String newValue)
    {
        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        // Llamado por el evento "change"
        String oldValue = comp.getText();

        if (oldValue.equals(newValue))
            return; // No ha habido cambio alguno, evitamos repercutir falsos eventos "change" que al parecer se producen

        setText(newValue);
    }

    public void incrementalChange(String newValue)
    {
        // Es especialmente útil cuando es llamado por la pulsación de una tecla, dicha
        // tecla puede ser un cursor que no cambia el texto,
        // un CTRL-V que pega un trozo de texto, un CTRL-X que quita un trozo, el texto seleccionado que
        // es cambiado completamente al pegar otro
        // etc, además no sabemos donde se ha puesto el cursor inicialmente pues puede
        // ponerse en el medio del texto box por ejemplo. Por tanto no es posible
        // "añadir" o "quitar" algo a partir de la tecla pues newValue puede ser muy diferente al valor actual no sólo con un caracter cambiado.
        // Por otra parte un keyup en FireFox por ejemplo envía el keyCode que es la tecla pero no el charCode (a y A es el mimsmo keyCode)
        // tendríamos que andar viendo si el shift está activado etc para generar el charCode.
        // Por ello cada vez que se pulsa una tecla (keyup) se trae el valor actual del texto en el control,
        // no usamos keydown porque dicho valor del control no incluye la posible "nueva" letra (el cambio en general) porque el evento keydown
        // es cancelable por lo que hasta que no se procesa totalmente con éxito no se añade
        // al control en el navegador.

        // Suponemos que una sola parte ha cambiado: o bien eliminada, o bien
        // insertada o bien substituida
        // El objetivo es modificar el Document de forma incremental para que
        // un posible listener sepa qué ha cambiado exactamente

        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        String oldValue = comp.getText();

        // Si oldValue is igual a newValue se detecta

        int lenOld = oldValue.length();
        int lenNew = newValue.length();
        if (lenOld > lenNew)
        {
            // Se ha borrado una parte de oldValue
            int start; // Posición del comienzo de la zona quitada en oldValue
            for(start = 0; start < lenNew; start++)
            {
                if (oldValue.charAt(start) != newValue.charAt(start))
                    break;
            }

            int length = lenOld - lenNew;
            String newValueCalc = oldValue.substring(0,start) + oldValue.substring(start + length);
            if (newValueCalc.equals(newValue))
                remove(start,length);
            else
                fullChange(newValue); // Ha habido un cambio más complicado
        }
        else if (lenOld < lenNew)
        {
            // Se ha insertado una parte en oldValue
            int start; // Posición del comienzo de la zona insertada en oldValue
            for(start = 0; start < lenOld; start++)
            {
                if (oldValue.charAt(start) != newValue.charAt(start))
                    break;
            }
            int length = lenNew - lenOld;
            String strIns = newValue.substring(start,start + length);
            String newValueCalc = oldValue.substring(0,start) + strIns + oldValue.substring(start);
            if (newValueCalc.equals(newValue))
                insertString(strIns,start);
            else
                fullChange(newValue); // Ha habido un cambio más complicado
        }
        else // Se ha cambiado una parte en oldValue
        {
            int start; // Posición del comienzo de la zona insertada en oldValue
            for(start = 0; start < lenOld; start++)
            {
                if (oldValue.charAt(start) != newValue.charAt(start))
                    break;
            }
            if (start >= lenOld)
                return; // Son iguales, no ha cambiado nada

            int end;
            for(end = start; end < lenOld; end++)
            {
                if (oldValue.charAt(end) == newValue.charAt(end))
                    break;
            }
            String strReplace = newValue.substring(start,end);
            String newValueCalc = oldValue.substring(0,start) + strReplace + oldValue.substring(start + end - start);
            if (newValueCalc.equals(newValue))
                replaceString(strReplace,start,end);
            else
                fullChange(newValue); // Ha habido un cambio más complicado
        }
    }

    protected ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        if (isChangeEvent(type,clientDoc) ||
            type.equals("keyup"))
        {
            // Redefinimos porque es un poco más complicado que el código por defecto
            CustomParamTransport value = new CustomParamTransport("value","event.getCurrentTarget().value");
            return new ParamTransport[]{value};
        }
        else
            return null;
    }


    public void processDOMEvent(Event evt)
    {
        super.processDOMEvent(evt);

        String type = evt.getType();
        if (type.equals("keyup"))
        {
            // Ejecutado como respuesta al evento "keyup" en el navegador
            // No usamos ni keydown ni keypress porque ambos son cancelable y en el cliente
            // no se cambia el control hasta que el evento termina pues puede ser
            // cancelado y la finalidad aquí es el de cambiar el DOM para ir sincronizando
            // respecto a los cambios del cliente, cuando se emite keyup el control ya ha sido
            // cambiado y value tiene el valor actualizado aunque se cancele keyup

            handleEventOnKeyUp(evt);

            // Idem razones que el evento "change"

            ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();
            comp.postHandleEventOnKeyUp(evt);
        }
    }

    public void handleEventOnChange(Event evt)
    {
        ItsNatEvent itsNatEvent = (ItsNatEvent)evt;
        String newValue = (String)itsNatEvent.getExtraParam("value");

        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();

        ItsNatHTMLFormCompValueBasedImpl compBase = (ItsNatHTMLFormCompValueBasedImpl)comp; // A día de hoy todos los componentes texto son elementos de formulario HTML, en el futuro ya veremos
        compBase.setServerUpdatingFromClient(true); // Pues el evento viene del navegador y no se necesita enviar actualizaciones (salvo observers para que vean el cambio del cliente)

        try
        {
            comp.setNewValueOnChange(newValue,evt);
        }
        finally
        {
            compBase.setServerUpdatingFromClient(false);
        }
    }

    public void handleEventOnKeyUp(Event evt)
    {
        // Si se activó el evento "keyup" el DOM se actualizará para cada tecla
        ItsNatEvent itsNatEvent = (ItsNatEvent)evt;
        String newValue = (String)itsNatEvent.getExtraParam("value");

        ItsNatHTMLFormCompValueBasedImpl compBase = (ItsNatHTMLFormCompValueBasedImpl)comp; // A día de hoy todos los componentes texto son elementos de formulario HTML, en el futuro ya veremos
        compBase.setServerUpdatingFromClient(true); // Pues el evento viene del navegador y no se necesita enviar actualizaciones (salvo observers para que vean el cambio del cliente)

        ItsNatHTMLFormTextComponentInternal comp = getItsNatHTMLFormTextComponentInternal();
        try
        {
            comp.setNewValueOnKeyUp(newValue,evt);
        }
        finally
        {
            compBase.setServerUpdatingFromClient(false);
        }
    }

}
