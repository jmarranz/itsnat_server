/*
 * TestInsertFragment.java
 *
 * Created on 9 de enero de 2007, 9:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestInsertHTMLSVGFragment implements EventListener,Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected Element link;
    protected Element parent;

    /** Creates a new instance of TestInsertFragment */
    public TestInsertHTMLSVGFragment(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        this.link = doc.getElementById("insertHTMLSVGFragmentId");
        ((EventTarget)link).addEventListener("click",this,false);

        this.parent = doc.getElementById("insertHTMLSVGFragmentContainerId");
    }

    public void handleEvent(final Event evt)
    {
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        DocumentFragment docFrag = servlet.getItsNatDocFragmentTemplate("test_html_with_svg_fragment").loadDocumentFragment(itsNatDoc);
        //link.getParentNode().insertBefore(docFrag,link);
        parent.appendChild(docFrag); 

        /*
        <!-- Para ver si se inserta bien via DOM otros namespaces -->
        <svg:svg xmlns:svg="http://www.w3.org/2000/svg" style="display: none;">
            <svg:circle r="70" fill-opacity="0.5" fill="#0000ff" cy="150" cx="200"></circle>
        </svg:svg>

        <!-- Para ver si se inserta bien via DOM otros namespaces -->
        <svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
            <circle r="70" fill-opacity="0.5" fill="#0000ff" cy="150" cx="200"></circle>
        </svg>

        <!-- Caso de namespace desconocido -->
        <foo:foo xmlns:foo="http://foo/foo">
            <foo:fooelem />
        </foo:foo>

        <foo xmlns="http://foo/foo">
            <fooelem />
        </foo>
         */

        Element secondFoo = ItsNatTreeWalker.getLastChildElement(parent);
        Element firstFoo = ItsNatTreeWalker.getPreviousSiblingElement(secondFoo);
        Element secondSVG = ItsNatTreeWalker.getPreviousSiblingElement(firstFoo);
        Element firstSVG = ItsNatTreeWalker.getPreviousSiblingElement(secondSVG);

        itsNatDoc.addCodeToSend("try");
        itsNatDoc.addCodeToSend("{\n");

        String firstSVGRef = itsNatDoc.getScriptUtil().getNodeReference(firstSVG);
        itsNatDoc.addCodeToSend("var elem = " + firstSVGRef + ";");
        itsNatDoc.addCodeToSend("if ((elem.prefix != 'svg')||(elem.localName != 'svg')) { alert('WRONG TestInsertHTMLSVGFragment 1'); throw 'ERROR'; }\n" );
        itsNatDoc.addCodeToSend("if ((elem.getAttribute('xmlns:svg') != null)&&(elem.getAttribute('xmlns:svg') != 'http://www.w3.org/2000/svg')) { alert('WRONG TestInsertHTMLSVGFragment 2'); throw 'ERROR'; }\n" );
        itsNatDoc.addCodeToSend("else for(var i = 0; i < elem.attributes.length; i++) if ((elem.attributes[i].name == 'xmlns:svg')&&(elem.attributes[i].value != 'http://www.w3.org/2000/svg')) { alert('WRONG TestInsertHTMLSVGFragment 2'); throw 'ERROR'; }\n" ); // Opera

        Element firstSVG_child = ItsNatTreeWalker.getFirstChildElement(firstSVG);
        String firstSVGRef_child = itsNatDoc.getScriptUtil().getNodeReference(firstSVG_child);
        itsNatDoc.addCodeToSend("var elem = " + firstSVGRef_child + ";");
        itsNatDoc.addCodeToSend("if (elem.tagName != 'svg:circle') { alert('WRONG TestInsertHTMLSVGFragment 3'); throw 'ERROR'; }\n" );

        String secondSVGRef = itsNatDoc.getScriptUtil().getNodeReference(secondSVG);
        itsNatDoc.addCodeToSend("var elem = " + secondSVGRef + ";");
        itsNatDoc.addCodeToSend("if (elem.tagName != 'svg') { alert('WRONG TestInsertHTMLSVGFragment'); throw 'ERROR'; }\n" );
        itsNatDoc.addCodeToSend("if (elem.getAttribute('xmlns') != 'http://www.w3.org/2000/svg') { alert('WRONG TestInsertHTMLSVGFragment 4'); throw 'ERROR'; }\n" );

        Element secondSVG_child = ItsNatTreeWalker.getFirstChildElement(secondSVG);
        String secondRef_child = itsNatDoc.getScriptUtil().getNodeReference(secondSVG_child);
        itsNatDoc.addCodeToSend("var elem = " + secondRef_child + ";");
        itsNatDoc.addCodeToSend("if (elem.tagName != 'circle') { alert('WRONG TestInsertHTMLSVGFragment 5'); throw 'ERROR'; }\n" );


        String firstFooRef = itsNatDoc.getScriptUtil().getNodeReference(firstFoo);
        itsNatDoc.addCodeToSend("var elem = " + firstFooRef + ";");
        itsNatDoc.addCodeToSend("if ((elem.prefix != 'foo')||(elem.localName != 'foo')) { alert('WRONG TestInsertHTMLSVGFragment 6'); throw 'ERROR'; }\n" );
        itsNatDoc.addCodeToSend("if ((elem.getAttribute('xmlns:foo') != null)&&(elem.getAttribute('xmlns:foo') != 'http://foo/foo')) { alert('WRONG TestInsertHTMLSVGFragment 7'); throw 'ERROR'; }\n" );
        itsNatDoc.addCodeToSend("else for(var i = 0; i < elem.attributes.length; i++) if ((elem.attributes[i].name == 'xmlns:foo')&&(elem.attributes[i].value != 'http://foo/foo')) { alert('WRONG TestInsertHTMLSVGFragment 7'); throw 'ERROR'; }\n" ); // Opera

        Element firstFoo_child = ItsNatTreeWalker.getFirstChildElement(firstFoo);
        String firstFooRef_child = itsNatDoc.getScriptUtil().getNodeReference(firstFoo_child);
        itsNatDoc.addCodeToSend("var elem = " + firstFooRef_child + ";");
        itsNatDoc.addCodeToSend("if (elem.tagName != 'foo:fooelem') { alert('WRONG TestInsertHTMLSVGFragment 8'); throw 'ERROR'; }\n" );

        String secondFooRef = itsNatDoc.getScriptUtil().getNodeReference(secondFoo);
        itsNatDoc.addCodeToSend("var elem = " + secondFooRef + ";");
        itsNatDoc.addCodeToSend("if (elem.tagName != 'foo') { alert('WRONG TestInsertHTMLSVGFragment'); throw 'ERROR'; }\n" );
        itsNatDoc.addCodeToSend("if (elem.getAttribute('xmlns') != 'http://foo/foo') { alert('WRONG TestInsertHTMLSVGFragment 9'); throw 'ERROR'; }\n" );

        Element secondFoo_child = ItsNatTreeWalker.getFirstChildElement(secondFoo);
        String secondFooRef_child = itsNatDoc.getScriptUtil().getNodeReference(secondFoo_child);
        itsNatDoc.addCodeToSend("var elem = " + secondFooRef_child + ";");
        itsNatDoc.addCodeToSend("if (elem.tagName != 'fooelem') { alert('WRONG TestInsertHTMLSVGFragment 10'); throw 'ERROR'; }\n" );

        itsNatDoc.addCodeToSend("}\n");
        itsNatDoc.addCodeToSend("catch(e) { alert('WRONG TestInsertHTMLSVGFragment'); }\n");
    }
}
