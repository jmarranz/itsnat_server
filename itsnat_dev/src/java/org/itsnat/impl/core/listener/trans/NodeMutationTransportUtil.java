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

package org.itsnat.impl.core.listener.trans;

import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.event.client.domstd.w3c.W3CMutationEventImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.event.client.ClientItsNatNormalEventImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.CharacterData;

/**
 *
 * @author jmarranz
 */
public class NodeMutationTransportUtil extends ParamTransportUtil
{
    public static final NodeMutationTransportUtil SINGLETON = new NodeMutationTransportUtil();

    /**
     * Creates a new instance of ParamTransportUtil
     */
    public NodeMutationTransportUtil()
    {
    }

    public String getCodeToSend(ParamTransport param)
    {
        return null;
    }

    public void syncServerBeforeDispatch(ParamTransport param, RequestNormalEventImpl request,ClientItsNatNormalEventImpl event)
    {
        ItsNatStfulDocumentImpl itsNatDoc = event.getItsNatStfulDocument();

        MutationEvent mutEvent = (MutationEvent)event;
        String type = mutEvent.getType();

        if (type.equals("DOMNodeInserted"))
        {
            Element parent = (Element)mutEvent.getRelatedNode();
            W3CMutationEventImpl w3cMutEvent = (W3CMutationEventImpl)mutEvent;
            Node refNode = w3cMutEvent.getRefChild();

            /* El target debería ser el nuevo nodo pero en este caso el nodo se obtiene a partir del node path
             obtenido en el cliente a partir del nuevo nodo, si se usó un insertBefore el nuevo nodo ocupará
             el lugar (respecto al orden) del nodo usado como referencia al insertar (el refNode), por ello el servidor con dicho path
             obtenemos el nodo de referencia lo cual es genial pues sabemos donde insertar.
             Si se usó appendChild el path del nuevo nodo no existe en el servidor y el refNode será nulo, así sabemos que se usó appendChild
             (o insertBefore con referencia nula).
             Este tipo de cosas son las usadas para formar el path enviado por el cliente y usado en getRefChild()
             */

            // http://webfx.eae.net/dhtml/ieemu/htmlmodel.html
            // Con getNewValue() obtenemos el markup como string del nuevo nodo, esto no es estándar pero nos vale.
            String newValue = mutEvent.getNewValue();
            DocumentFragment newNodeFragment = itsNatDoc.getItsNatDocumentTemplateVersion().parseFragmentToDocFragment(newValue,itsNatDoc);
            Node newNode = newNodeFragment.getFirstChild();
            parent.insertBefore(newNode,refNode); // refNode puede ser null
            w3cMutEvent.setTargetNodeInserted((EventTarget)newNode); // De esta manera el usuario verá el target de una forma estándar
        }
        else if (type.equals("DOMNodeRemoved"))
        {
            // El evento "DOMNodeRemoved" se procesa antes de que se haya quitado
            // de forma efectiva el nodo, por lo que al servidor le llega el path del nodo a quitar (el target)
            // La eliminación ha de hacerse después del despachado de listeners de acuerdo con el W3C DOM Events
        }
        else if (type.equals("DOMAttrModified"))
        {
            Attr attr = (Attr)mutEvent.getRelatedNode();
            Element targetElem = (Element)mutEvent.getTarget();
            int changeType = mutEvent.getAttrChange();
            switch(changeType)
            {
                case MutationEvent.ADDITION:
                    String attrName = mutEvent.getAttrName();
                    DOMUtilInternal.setAttribute(targetElem,attrName,mutEvent.getNewValue());
                    attr = targetElem.getAttributeNode(attrName);
                    W3CMutationEventImpl w3cMutEvent = (W3CMutationEventImpl)mutEvent;
                    w3cMutEvent.setRelatedNodeAddedAttr(attr);
                    break;
                case MutationEvent.MODIFICATION:
                    attr.setValue(mutEvent.getNewValue());
                    break;
                case MutationEvent.REMOVAL:
                    targetElem.removeAttributeNode(attr);
                    break;
            }
        }
        else if (type.equals("DOMCharacterDataModified"))
        {
            CharacterData charNode = (CharacterData)mutEvent.getTarget();
            DOMUtilInternal.setCharacterDataContent(charNode, mutEvent.getNewValue());
        }

    }

    public void syncServerAfterDispatch(ParamTransport param, RequestNormalEventImpl request,ClientItsNatNormalEventImpl event)
    {
        MutationEvent mutEvent = (MutationEvent)event;
        String type = mutEvent.getType();

        if (type.equals("DOMNodeRemoved"))
        {
            // La eliminación ha de hacerse después del despachado de listeners de acuerdo con el W3C DOM Events
            Element parent = (Element)mutEvent.getRelatedNode();
            Node removedNode = (Node)mutEvent.getTarget();
            parent.removeChild(removedNode);
        }
    }
}
