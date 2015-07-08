/*
 * PersonEditorComponent.java
 *
 * Created on 10 de junio de 2007, 12:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp.labels;

import manual.comp.shared.MyCustomComponentBase;
import manual.comp.shared.Person;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.w3c.dom.Element;


/**
 *
 * @author jmarranz
 */
public class PersonEditorComponent extends MyCustomComponentBase
{
    protected ItsNatHTMLInputText firstNameComp;
    protected ItsNatHTMLInputText lastNameComp;
    protected ItsNatHTMLInputButton okComp;
    protected ItsNatHTMLInputButton cancelComp;
    protected Person person;

    public PersonEditorComponent(Person person,Element parentElement,ItsNatComponentManager compMgr)
    {
        super(parentElement,compMgr);

        this.person = person;

        this.firstNameComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("firstNameId");
        this.lastNameComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("lastNameId");
        this.okComp = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("okPersonId");
        this.cancelComp = (ItsNatHTMLInputButton)compMgr.createItsNatComponentById("cancelPersonId");

        firstNameComp.setText(person.getFirstName());
        lastNameComp.setText(person.getLastName());
    }

    public void dispose()
    {
        firstNameComp.dispose();
        lastNameComp.dispose();
        okComp.dispose();
        cancelComp.dispose();
    }

    public Person getPerson()
    {
        return person;
    }

    public void updatePerson()
    {
        person.setFirstName(firstNameComp.getText());
        person.setLastName(lastNameComp.getText());
    }

    public ItsNatHTMLInputButton getOKButton()
    {
        return okComp;
    }

    public ItsNatHTMLInputButton getCancelButton()
    {
        return cancelComp;
    }
}

