
package inexp.groovyex;

import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

class GroovyExampleDocument
{
    def itsNatDoc // ItsNatHTMLDocument
    def textInput // ItsNatHTMLInputText
    def resultsElem // Element   

    GroovyExampleDocument(itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc

        def doc = itsNatDoc.getHTMLDocument()
   
        def compMgr = itsNatDoc.getItsNatComponentManager()
        this.textInput = compMgr.createItsNatComponentById("inputId")
        
        def buttonElem = doc.getElementById("buttonId")
        buttonElem.addEventListener("click", 
             { Event evt -> def text = textInput.getText(); resultsElem.setTextContent(text); } as EventListener, false)

        this.resultsElem = doc.getElementById("resultsId")
    }
}
