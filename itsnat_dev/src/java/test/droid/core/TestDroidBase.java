/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;

/**
 *
 * @author jmarranz
 */
public abstract class TestDroidBase
{
    public static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    
    protected ItsNatDocument itsNatDoc;

    public TestDroidBase(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }
    
    public Document getDocument()
    {
        return itsNatDoc.getDocument();
    }
}
