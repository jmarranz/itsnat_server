package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

/**
 * Métodos visibles desde BeanShell
 *
 * Created by jmarranz on 12/06/14.
 */
public interface ItsNatDoc
{
    public View getNode(Object[] idObj);
    public View getParentNode(View parentNode);

    public void setAttribute(View node,String name,String value);
    public void setAttribute2(Object[] idObj,String name,String value);
    public void setAttributeNS(View node,String namespaceURI,String name,String value);
    public void setAttributeNS2(Object[] idObj,String namespaceURI,String name,String value);

    public void removeAttribute(View node,String name);
    public void removeAttribute2(Object[] idObj,String name);
    public void removeAttributeNS(View node,String namespaceURI,String name);
    public void removeAttributeNS2(Object[] idObj,String namespaceURI,String name);

    public View createElement(String name);
    public View createElementNS(String namespaceURI,String name);

    public void insertBefore(View parentNode,View newChild,View childRef);
    public void insertBefore2(View parentNode,View newChild,View childRef,String newId);
    public void insertBefore3(Object[] parentIdObj,View newChild,Object[] childRefIdObj,String newId);

    public void appendChild(View parentNode,View newChild);
    public void appendChild2(View parentNode,View newChild,String newId);
    public void appendChild3(Object[] idObj,View newChild,String newId);
}
