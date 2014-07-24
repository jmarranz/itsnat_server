package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

import org.itsnat.droid.Page;

/**
 * Métodos llamados por el servidor pero ninguno público para el usuario
 *
 * Created by jmarranz on 8/07/14.
 */
public interface ItsNatDocPublic
{
    public void init(String stdSessionId,String sessionToken,String sessionId,String clientId,String servletPath,int errorMode);

    public void setDisabledEvents();
    public void onServerStateLost();

    public Node getNode(Object[] idObj);
    public View getView(Object[] idObj);

    public void setAttribute(Node node,String name,String value);
    public void setAttribute2(Object[] idObj,String name,String value);
    public void setAttributeNS(Node node,String namespaceURI,String name,String value);
    public void setAttributeNS2(Object[] idObj,String namespaceURI,String name,String value);

    public void setAttrBatch(Node node,String namespaceURI,String[] attrNames,String[] attrValues);

    public void removeAttribute(Node node,String name);
    public void removeAttribute2(Object[] idObj,String name);
    public void removeAttributeNS(Node node,String namespaceURI,String name);
    public void removeAttributeNS2(Object[] idObj,String namespaceURI,String name);

    public Node createElement(String name);
    public Node createElementNS(String namespaceURI,String name);

    public void insertBefore(Node parentNode,Node newChild,Node childRef);
    public void insertBefore2(Node parentNode,Node newChild,Node childRef,String newId);
    public void insertBefore3(Object[] parentIdObj,Node newChild,Object[] childRefIdObj,String newId);

    public void appendChild(Node parentNode,Node newChild);
    public void appendChild2(Node parentNode,Node newChild,String newId);
    public void appendChild3(Object[] idObj,Node newChild,String newId);

    public void removeChild(Node child); // Realmente no es público en ItsNatDroid (si lo es en web), por simetría lo ponemos
    public void removeChild2(String id,boolean isText);
    public void removeChild3(Object[] parentIdObj,String childRelPath,boolean isText);

    public void removeNodeCache(String[] idList);

    public void removeAllChild2(Object[] parentIdObj);

    public Node addNodeCache(Object[] idObj);

    public Node addDroidEL(Object[] idObj,String type,String listenerId,CustomFunction customFunction,boolean useCapture,int commMode,long timeout,int typeCode);
    public void removeDroidEL(String listenerId);

    public void addGlobalEL(GlobalEventListener listener);
    public void removeGlobalEL(GlobalEventListener listener);


}
