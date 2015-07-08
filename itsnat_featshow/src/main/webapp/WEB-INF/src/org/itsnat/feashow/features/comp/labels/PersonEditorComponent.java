/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.comp.labels;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.feashow.features.comp.shared.MyCustomComponentBase;
import org.itsnat.feashow.features.comp.shared.Person;
import org.w3c.dom.Element;

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

