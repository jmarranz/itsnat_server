package org.itsnat.droid.impl.browser.serveritsnat;

/**
 * Created by jmarranz on 9/07/14.
 */
public interface DOMPathResolver
{
    public Node getNodeFromPath(String pathStr,Node topParent);

    public String getStringPathFromNode(Node node,Node topParent);
}
