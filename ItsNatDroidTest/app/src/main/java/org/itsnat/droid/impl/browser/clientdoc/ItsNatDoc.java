package org.itsnat.droid.impl.browser.clientdoc;

/**
 * Métodos visibles desde BeanShell
 *
 * Created by jmarranz on 12/06/14.
 */
public interface ItsNatDoc
{
    public Node getNode(Object[] idObj);

    public void setAttribute(Node node,String name,String value);
    public void setAttribute2(Object[] idObj,String name,String value);
    public void setAttributeNS(Node node,String namespaceURI,String name,String value);
    public void setAttributeNS2(Object[] idObj,String namespaceURI,String name,String value);

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
}
