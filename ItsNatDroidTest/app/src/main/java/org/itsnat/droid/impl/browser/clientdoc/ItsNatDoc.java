package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

/**
 * Métodos visibles desde BeanShell
 *
 * Created by jmarranz on 12/06/14.
 */
public interface ItsNatDoc
{
    public View getNode(String id);
    public View getNode(Object[] idObj);
    public View getParentNode(View parentNode);
    public void setAttribute(View node,String name,String value);
    public void setAttribute2(String idObj,String name,String value);
    public void setAttribute2(Object[] idObj,String name,String value);
    public void setAttributeNS(View node,String namespaceURI,String name,String value);
    public void setAttributeNS2(String idObj,String namespaceURI,String name,String value);
    public void setAttributeNS2(Object[] idObj,String namespaceURI,String name,String value);
    public void removeAttribute(View node,String name);
    public void removeAttribute2(String idObj,String name);
    public void removeAttribute2(Object[] idObj,String name);
    public void removeAttributeNS(View node,String namespaceURI,String name);
    public void removeAttributeNS2(String idObj,String namespaceURI,String name);
    public void removeAttributeNS2(Object[] idObj,String namespaceURI,String name);
}
