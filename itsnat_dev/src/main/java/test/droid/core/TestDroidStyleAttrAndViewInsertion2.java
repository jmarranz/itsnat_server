/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import test.droid.shared.TestDroidBase;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestDroidStyleAttrAndViewInsertion2 extends TestDroidBase implements EventListener
{
    protected Element testStyleAttr;
    
    public TestDroidStyleAttrAndViewInsertion2(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);
        Document doc = itsNatDoc.getDocument();
        this.testStyleAttr = doc.getElementById("testStyleAttrId2");
        ((EventTarget)testStyleAttr).addEventListener("click", this, false);
    }
    
    @Override
    public void handleEvent(Event evt)
    {
        ((EventTarget)testStyleAttr).removeEventListener("click", this, false); // Evitamos ejecutar varias veces para evitar que falle textView.setAttribute("android:id","@+id/testStyleAttrTextId"); pues al reutilizarse el id varios elementos tendrán el mismo id (lo cual es correcto) y se devuelve el primero en el test y por tanto fallará
        
        
        Document doc = itsNatDoc.getDocument();        
        Element testStyleAttrHidden = doc.getElementById("testStyleAttrHiddenId2");  
        
        Element textView = doc.createElement("TextView");        
        // Test definir atributos antes de insertar
        textView.setAttribute("android:text", "OK if left/right padding, background=pink, width=match_parent, text size small, text color=red");         
        textView.setAttribute("style","@remote:style/droid/res/values/test_values_remote.xml:test_style_remote");
        
        testStyleAttrHidden.getParentNode().insertBefore(textView, testStyleAttrHidden);
        
        // Test definir atributos después de insertar            
        textView.setAttribute("android:layout_width", "match_parent");        
        textView.setAttribute("android:layout_height", "wrap_content");        
        textView.setAttribute("android:background", "#ffdddd");         
        textView.setAttribute("android:textSize", "@remote:dimen/droid/res/values/test_values_remote.xml:test_dimen_textSize");  
        textView.setAttribute("android:textColor", "@remote:color/droid/res/values/test_values_remote.xml:test_color_textColor");                
       
        Element textViewPrevious = textView;
        
        textView = doc.createElement("TextView");        
        // Test definir atributos antes de insertar
        textView.setAttribute("android:text", "OK if left/right padding, background=pink, width=match_parent, text size=default, text color=red");         
        textView.setAttribute("style","@remote:style/droid/res/values/test_values_remote.xml:test_style_remote");
        
        testStyleAttrHidden.getParentNode().insertBefore(textView, textViewPrevious);
        
        // Test definir atributos después de insertar            
        textView.setAttribute("android:layout_width", "match_parent");        
        textView.setAttribute("android:layout_height", "wrap_content");        
        textView.setAttribute("android:background", "#ffdddd");         
        // textView.setAttribute("android:textSize", "@remote:dimen/droid/res/values/test_values_remote.xml:test_dimen_textSize");  
        textView.setAttribute("android:textColor", "@remote:color/droid/res/values/test_values_remote.xml:test_color_textColor");         
    }
    
}
