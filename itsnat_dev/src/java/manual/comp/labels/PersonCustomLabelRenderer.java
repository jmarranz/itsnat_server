/*
 * PersonCustomLabelRenderer.java
 *
 * Created on 12 de junio de 2007, 9:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp.labels;

import manual.comp.shared.Person;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.label.ItsNatLabel;
import org.itsnat.comp.label.ItsNatLabelRenderer;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class PersonCustomLabelRenderer implements ItsNatLabelRenderer
{
    public PersonCustomLabelRenderer()
    {
    }

    public void renderLabel(ItsNatLabel label, Object value, Element labelElem,boolean isNew)
    {
        Person person = (Person)value;

        ItsNatDocument itsNatDoc = label.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element firstNameElem = doc.getElementById("firstNameId");
        ItsNatDOMUtil.setTextContent(firstNameElem,person.getFirstName());
        Element lastNameElem = doc.getElementById("lastNameId");
        ItsNatDOMUtil.setTextContent(lastNameElem,person.getLastName());
    }

    public void unrenderLabel(ItsNatLabel label,Element labelElem)
    {
    }
}
