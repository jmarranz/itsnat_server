
package inexp.juel;

import java.util.ArrayList;
import javax.el.ExpressionFactory;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLInputElement;

public class JUELExampleDocument
{
    protected ItsNatHTMLDocument itsNatDoc;

    public JUELExampleDocument(ItsNatHTMLDocument itsNatDoc,final ExpressionFactory factory)
    {
        this.itsNatDoc = itsNatDoc;
   
        ArrayList<Person> persons = new ArrayList<Person>();         
        
        persons.add(new Person("Jhon",23));
        persons.add(new Person("Paul",60));          
        persons.add(new Person("George",65));         
        
        HTMLDocument doc = itsNatDoc.getHTMLDocument();        
        
        // TABLE based
        Element parentTBody = doc.getElementById("personListTable");          
        
        ELBasedElementList elemListTable = new ELBasedElementList("person",parentTBody,itsNatDoc,factory);
        
        for(Person person : persons)
            elemListTable.getElementList().addElement(person);          
  
        // UL based
        Element parentUL = doc.getElementById("personListUL");         

        ELBasedElementList elemListUL = new ELBasedElementList("person",parentUL,itsNatDoc,factory);        
        
        for(Person person : persons)
            elemListUL.getElementList().addElement(person);         
        
        // Input Text
        
        final Person person = new Person("Daniel",32);
        
        final Element resultsElem = doc.getElementById("resultsId");        
        
        Runnable onChanged = new Runnable()
        {
            public void run()
            {
                String name = person.getName();                  
                resultsElem.setTextContent(name);
            }
        };        
        
        HTMLInputElement inputElem = (HTMLInputElement)doc.getElementById("personNameId");        
        new ELBasedInputText("person",person,inputElem,itsNatDoc,factory,onChanged);        
    }
}

