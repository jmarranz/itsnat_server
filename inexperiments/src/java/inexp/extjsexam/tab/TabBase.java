/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.extjsexam.tab;

import inexp.extjsexam.ExtJSExampleDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;

/**
 *
 * @author jmarranz
 */
public abstract class TabBase
{
    protected ExtJSExampleDocument extJSDoc;

    public TabBase(ExtJSExampleDocument extJSDoc)
    {
        this.extJSDoc = extJSDoc;
    }

    public abstract String getTitle();
    public abstract String getFragmentName();

    public ExtJSExampleDocument getExtJSExampleDocument()
    {
        return extJSDoc;
    }

    public ItsNatHTMLDocument getItsNatHTMLDocument()
    {
        return extJSDoc.getItsNatHTMLDocument();
    }

    public String toString()
    {
        return getTitle();
    }

    public void render()
    {
    }

    public void remove()
    {
    }
}
